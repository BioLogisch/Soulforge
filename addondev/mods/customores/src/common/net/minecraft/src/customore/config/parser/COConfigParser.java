package net.minecraft.src.customore.config.parser;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Vector;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.UserDataHandler;
import org.xml.sax.SAXException;

import net.minecraft.src.customore.config.COChoiceOption;
import net.minecraft.src.customore.config.COConfigOption;
import net.minecraft.src.customore.config.CONumericOption;
import net.minecraft.src.customore.config.COWorldConfig;
import net.minecraft.src.customore.config.parser.COExpressionEvaluator.EvaluationDelegate;
import net.minecraft.src.customore.config.validation.COValidatorAnnotation;
import net.minecraft.src.customore.config.validation.COValidatorBiomeSet;
import net.minecraft.src.customore.config.validation.COValidatorDistribution;
import net.minecraft.src.customore.config.validation.COValidatorExpression;
import net.minecraft.src.customore.config.validation.COValidatorIfChoice;
import net.minecraft.src.customore.config.validation.COValidatorIfCondition;
import net.minecraft.src.customore.config.validation.COValidatorImport;
import net.minecraft.src.customore.config.validation.COValidatorNode;
import net.minecraft.src.customore.config.validation.COValidatorOption;
import net.minecraft.src.customore.config.validation.COValidatorRefOption;
import net.minecraft.src.customore.config.validation.COValidatorRoot;
import net.minecraft.src.customore.config.validation.COValidatorSection;
import net.minecraft.src.customore.config.validation.COValidatorIfChoice.Factory;
import net.minecraft.src.customore.config.validation.COValidatorNode.IValidatorFactory;
import net.minecraft.src.customore.generator.COMapGenCloud;
import net.minecraft.src.customore.generator.COMapGenClusters;
import net.minecraft.src.customore.generator.COMapGenVeins;
import net.minecraft.src.customore.generator.COWorldGenSubstitution;
import net.minecraft.src.customore.generator.ICOOreDistribution;
import net.minecraft.src.customore.generator.ICOOreDistribution.IDistributionFactory;
import net.minecraft.src.customore.util.COBiomeDescriptor;
import net.minecraft.src.customore.util.COBlockDescriptor;
import net.minecraft.src.customore.util.COCIStringMap;

public class COConfigParser
{
    public final COWorldConfig target;
    public final ConfigExpressionEvaluator defaultEvaluator = new ConfigExpressionEvaluator();
    protected Random rng = null;
    protected final DocumentBuilder domBuilder;
    protected final SAXParser saxParser;
    private static final Map<String,IValidatorFactory> distributionValidators = new HashMap();

    public boolean blockExists(String blockDescription)
    {
        return (new COBlockDescriptor(blockDescription)).getTotalMatchWeight() > 0.0F;
    }

    public boolean biomeExists(String biomeDescription)
    {
        return (new COBiomeDescriptor(biomeDescription)).getTotalMatchWeight() > 0.0F;
    }

    public float nextRandom()
    {
        return this.rng == null ? 0.0F : this.rng.nextFloat();
    }

    public COConfigParser(COWorldConfig target) throws ParserConfigurationException, SAXException
    {
        this.target = target;
        DocumentBuilderFactory domBuilderFactory = DocumentBuilderFactory.newInstance();
        domBuilderFactory.setNamespaceAware(true);
        domBuilderFactory.setIgnoringComments(true);
        domBuilderFactory.setIgnoringElementContentWhitespace(true);
        domBuilderFactory.setExpandEntityReferences(true);
        this.domBuilder = domBuilderFactory.newDocumentBuilder();
        SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
        saxParserFactory.setNamespaceAware(true);
        this.saxParser = saxParserFactory.newSAXParser();
    }

    public void parseFile(File file) throws ParserConfigurationException, IOException, SAXException
    {
        Document fileDOM = this.domBuilder.newDocument();
        fileDOM.setUserData("value", file, (UserDataHandler)null);
        this.saxParser.parse(file, new COLineAwareSAXHandler(fileDOM));
        COValidatorNode validator = new COValidatorNode(this, fileDOM);
        Vector topLevelNodes = new Vector();
        validator.addGlobalValidator(Node.ELEMENT_NODE, "Import", new COValidatorImport.Factory(true));
        validator.addGlobalValidator(Node.ELEMENT_NODE, "OptionalImport", new COValidatorImport.Factory(false));
        validator.addGlobalValidator(Node.ELEMENT_NODE, "Description", new COValidatorAnnotation.Factory());
        validator.addGlobalValidator(Node.ATTRIBUTE_NODE, "Description", new COValidatorAnnotation.Factory());
        validator.addGlobalValidator(Node.ELEMENT_NODE, "Comment", new COValidatorAnnotation.Factory());
        validator.addGlobalValidator(Node.ELEMENT_NODE, "ConfigSection", new COValidatorSection.Factory());
        validator.addGlobalValidator(Node.ELEMENT_NODE, "IfCondition", new COValidatorIfCondition.Factory());
        validator.addGlobalValidator(Node.ELEMENT_NODE, "IfChoice", new COValidatorIfChoice.Factory(false));
        validator.addGlobalValidator(Node.ELEMENT_NODE, "IfNotChoice", new COValidatorIfChoice.Factory(true));
        validator.addGlobalValidator(Node.ELEMENT_NODE, "GetOption", new COValidatorRefOption.Factory());
        validator.addGlobalValidator(Node.ELEMENT_NODE, "Expression", new COValidatorExpression.Factory(this.defaultEvaluator));
        topLevelNodes.add("OptionDisplayGroup");
        validator.addGlobalValidator(Node.ELEMENT_NODE, "OptionDisplayGroup", new COValidatorOption.Factory(COConfigOption.DisplayGroup.class));
        topLevelNodes.add("OptionChoice");
        validator.addGlobalValidator(Node.ELEMENT_NODE, "OptionChoice", new COValidatorOption.Factory(COChoiceOption.class));
        topLevelNodes.add("OptionNumeric");
        validator.addGlobalValidator(Node.ELEMENT_NODE, "OptionNumeric", new COValidatorOption.Factory(CONumericOption.class));
        topLevelNodes.add("MystcraftSymbol");
        validator.addGlobalValidator(Node.ELEMENT_NODE, "BiomeSet", new COValidatorBiomeSet.Factory());
        
        for (Entry<String,IValidatorFactory> entry : distributionValidators.entrySet()) {
        	validator.addGlobalValidator(Node.ELEMENT_NODE, entry.getKey(), entry.getValue());
            topLevelNodes.add(entry.getKey());
        }
        
        validator.addGlobalValidator(Node.ELEMENT_NODE, "Config", new COValidatorRoot.Factory(topLevelNodes));

        if (this.target.worldInfo == null)
        {
            this.rng = null;
        }
        else
        {
            this.rng = new Random(this.target.worldInfo.getSeed());
            this.rng.nextInt();
        }

        validator.validate();
    }

    public static Object parseString(Class type, String value) throws IllegalArgumentException
    {
        if (type != null && value != null)
        {
            if (type.isAssignableFrom(String.class))
            {
                return value;
            }
            else if (type.isEnum())
            {
            	for (Enum val : (Enum[])type.getEnumConstants()) {
            		if (val.name().equalsIgnoreCase(value))
                    {
                        return val;
                    }
            	}
                throw new IllegalArgumentException("Invalid enumeration value \'" + value + "\'");
            }
            else if (!type.isAssignableFrom(Character.TYPE) && !type.isAssignableFrom(Character.class))
            {
                if (!type.isAssignableFrom(Boolean.TYPE) && !type.isAssignableFrom(Boolean.class))
                {
                    try
                    {
                        if (!type.isAssignableFrom(Byte.TYPE) && !type.isAssignableFrom(Byte.class))
                        {
                            if (!type.isAssignableFrom(Short.TYPE) && !type.isAssignableFrom(Short.class))
                            {
                                if (!type.isAssignableFrom(Integer.TYPE) && !type.isAssignableFrom(Integer.class))
                                {
                                    if (!type.isAssignableFrom(Long.TYPE) && !type.isAssignableFrom(Long.class))
                                    {
                                        if (!type.isAssignableFrom(Float.TYPE) && !type.isAssignableFrom(Float.class))
                                        {
                                            if (!type.isAssignableFrom(Double.TYPE) && !type.isAssignableFrom(Double.class))
                                            {
                                                throw new IllegalArgumentException("Type \'" + type.getSimpleName() + "\' is not a string, enumeration, or primitve type.");
                                            }
                                            else
                                            {
                                                return Double.valueOf(Double.parseDouble(value));
                                            }
                                        }
                                        else
                                        {
                                            return Float.valueOf(Float.parseFloat(value));
                                        }
                                    }
                                    else
                                    {
                                        return Long.decode(value);
                                    }
                                }
                                else
                                {
                                    return Integer.decode(value);
                                }
                            }
                            else
                            {
                                return Short.decode(value);
                            }
                        }
                        else
                        {
                            return Byte.decode(value);
                        }
                    }
                    catch (NumberFormatException var6)
                    {
                        throw new IllegalArgumentException("Invalid numerical value \'" + value + "\'", var6);
                    }
                }
                else
                {
                    return Boolean.valueOf(Boolean.parseBoolean(value));
                }
            }
            else
            {
                return value.length() == 0 ? Character.valueOf('\u0000') : Character.valueOf(value.charAt(0));
            }
        }
        else
        {
            return null;
        }
    }

    public static Number convertNumber(Class<? extends Number> type, Number value) throws IllegalArgumentException
    {
        if (type != null && value != null)
        {
            if (!type.isAssignableFrom(Byte.TYPE) && !type.isAssignableFrom(Byte.class))
            {
                if (!type.isAssignableFrom(Short.TYPE) && !type.isAssignableFrom(Short.class))
                {
                    if (!type.isAssignableFrom(Integer.TYPE) && !type.isAssignableFrom(Integer.class))
                    {
                        if (!type.isAssignableFrom(Long.TYPE) && !type.isAssignableFrom(Long.class))
                        {
                            if (!type.isAssignableFrom(Float.TYPE) && !type.isAssignableFrom(Float.class))
                            {
                                if (!type.isAssignableFrom(Double.TYPE) && !type.isAssignableFrom(Double.class))
                                {
                                    throw new IllegalArgumentException("Type \'" + type.getSimpleName() + "\' is not a numeric type.");
                                }
                                else
                                {
                                    return Double.valueOf(value.doubleValue());
                                }
                            }
                            else
                            {
                                return Float.valueOf(value.floatValue());
                            }
                        }
                        else
                        {
                            return Long.valueOf(value.longValue());
                        }
                    }
                    else
                    {
                        return Integer.valueOf(value.intValue());
                    }
                }
                else
                {
                    return Short.valueOf(value.shortValue());
                }
            }
            else
            {
                return Byte.valueOf(value.byteValue());
            }
        }
        else
        {
            return null;
        }
    }

    public static void addDistributionType(String distributionName, IValidatorFactory validatorFactory)
    {
        if (distributionValidators.containsKey(distributionName))
        {
            throw new IllegalArgumentException("A distribution with the name \'" + distributionName + "\' already exists.");
        }
        else
        {
            distributionValidators.put(distributionName, validatorFactory);
        }
    }

    public static void addDistributionType(String distributionName, IDistributionFactory distFactory)
    {
        addDistributionType(distributionName, (IValidatorFactory)(new COValidatorDistribution.Factory(distFactory)));
    }

    public static void addDistributionType(String distributionName, Class clazz, boolean canGenerate)
    {
        addDistributionType(distributionName, (IDistributionFactory)(new StdDistFactory(clazz, canGenerate)));
    }

    static
    {
        addDistributionType("StandardGen", COMapGenClusters.class, true);
        addDistributionType("StandardGenPreset", COMapGenClusters.class, false);
        addDistributionType("Veins", COMapGenVeins.class, true);
        addDistributionType("VeinsPreset", COMapGenVeins.class, false);
        addDistributionType("Cloud", COMapGenCloud.class, true);
        addDistributionType("CloudPreset", COMapGenCloud.class, false);
        addDistributionType("Substitute", COWorldGenSubstitution.class, true);
        addDistributionType("SubstitutePreset", COWorldGenSubstitution.class, false);
    }
    
    public class ConfigExpressionEvaluator extends COExpressionEvaluator
    {
        private Map localIdentifiers;

        public ConfigExpressionEvaluator()
        {
            this.localIdentifiers = new COCIStringMap();
            this.localIdentifiers.put("blockExists", new EvaluationDelegate(false, COConfigParser.this, "blockExists", new Class[] {String.class}));
            this.localIdentifiers.put("biomeExists", new EvaluationDelegate(false, COConfigParser.this, "biomeExists", new Class[] {String.class}));
            this.localIdentifiers.put("world.nextRandom", new EvaluationDelegate(false, COConfigParser.this, "nextRandom", new Class[0]));
        }

        public ConfigExpressionEvaluator(Object defaultValue)
        {
            this.localIdentifiers = new COCIStringMap();
            this.localIdentifiers.put("_default_", defaultValue);
        }

        protected Object getIdentifierValue(String identifier)
        {
            String lkey = identifier.toLowerCase();
            COConfigOption option = target.getConfigOption(identifier);

            if (option != null)
            {
                return option.getValue();
            }
            else
            {
                Object property = target.getWorldProperty(identifier);

                if (property != null)
                {
                    return property;
                }
                else
                {
                    Object value = this.localIdentifiers.get(identifier);
                    return value != null ? value : (lkey.startsWith("age.") ? Integer.valueOf(0) : super.getIdentifierValue(identifier));
                }
            }
        }
    }

    private static class StdDistFactory implements IDistributionFactory
    {
        protected Constructor _ctor;
        protected boolean _canGen;

        public StdDistFactory(Class clazz, boolean canGenerate)
        {
            try
            {
                this._ctor = clazz.getConstructor(new Class[] {Integer.TYPE, Boolean.TYPE});
            }
            catch (NoSuchMethodException var4)
            {
                throw new IllegalArgumentException(var4);
            }

            this._canGen = canGenerate;
        }

        public ICOOreDistribution createDistribution(int distributionID)
        {
            try
            {
                return (ICOOreDistribution)this._ctor.newInstance(new Object[] {Integer.valueOf(distributionID), Boolean.valueOf(this._canGen)});
            }
            catch (InvocationTargetException var3)
            {
                throw new IllegalArgumentException(var3);
            }
            catch (IllegalAccessException var4)
            {
                throw new IllegalArgumentException(var4);
            }
            catch (InstantiationException var5)
            {
                throw new IllegalArgumentException(var5);
            }
        }
    }
    
    public SAXParser getSaxParser() {
		return this.saxParser;
	}

}
