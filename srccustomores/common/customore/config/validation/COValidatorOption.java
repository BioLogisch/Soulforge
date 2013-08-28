package customore.config.validation;

import java.util.Iterator;

import org.w3c.dom.Node;

import customore.config.COChoiceOption;
import customore.config.COConfigOption;
import customore.config.CONumericOption;
import customore.config.parser.COConfigParser;
import customore.config.parser.COParserException;
import customore.util.COLogger;


public class COValidatorOption extends COValidatorNode
{
    private final Class _type;

    protected COValidatorOption(COValidatorNode parent, Node node, Class type)
    {
        super(parent, node);
        this._type = type;
    }

    protected boolean validateChildren() throws COParserException
    {
        super.validateChildren();
        String optionName = this.validateRequiredAttribute(String.class, "name", true);
        COConfigOption option = null;
        Class valueType = null;

        if (this._type == COChoiceOption.class)
        {
            valueType = String.class;
            COChoiceOption groupName = new COChoiceOption(optionName);
            option = groupName;
            Iterator<COValidatorChoice> defValue = this.validateNamedChildren(2, "Choice", new COValidatorChoice.Factory()).iterator();

            while (defValue.hasNext())
            {
                COValidatorChoice loadedValueStr = defValue.next();
                groupName.addPossibleValue(loadedValueStr.value, loadedValueStr.displayValue, loadedValueStr.description);
            }

            if (groupName.getValue() == null)
            {
                throw new COParserException("Choice option has no possible values.", this.getNode());
            }
        }
        else if (this._type == CONumericOption.class)
        {
            valueType = Double.class;
            CONumericOption groupName1 = new CONumericOption(optionName);
            option = groupName1;
            double defValue1 = this.validateNamedAttribute(valueType, "min", groupName1.getMin(), true);
            double err = this.validateNamedAttribute(valueType, "max", groupName1.getMax(), true);

            if (!groupName1.setLimits(defValue1, err))
            {
                throw new COParserException("Numeric option value range [" + defValue1 + "," + err + "] is invalid.", this.getNode());
            }

            double dmin = this.validateNamedAttribute(valueType, "displayMin", groupName1.getMin(), true);
            double dmax = this.validateNamedAttribute(valueType, "displayMax", groupName1.getMax(), true);
            double dincr = this.validateNamedAttribute(valueType, "displayIncrement", (dmax - dmin) / 100.0D, true);

            if (!groupName1.setDisplayLimits(dmin, dmax, dincr))
            {
                throw new COParserException("Numeric option display range/increment [" + dmin + "," + dmax + "]/" + dincr + " is invalid.", this.getNode());
            }
        }
        else if (this._type == COConfigOption.DisplayGroup.class)
        {
            option = new COConfigOption.DisplayGroup(optionName);
        }

        option.setDisplayState(this.validateNamedAttribute(COConfigOption.DisplayState.class, "displayState", option.getDisplayState(), true));
        option.setDisplayName(this.validateNamedAttribute(String.class, "displayName", option.getDisplayName(), true));
        option.setDescription(this.validateNamedAttribute(String.class, "description", option.getDescription(), true));
        String groupName2 = this.validateNamedAttribute(String.class, "displayGroup", null, true);

        if (groupName2 != null)
        {
            COConfigOption defValue3 = this.getParser().target.getConfigOption(groupName2);

            if (defValue3 == null || !(defValue3 instanceof COConfigOption.DisplayGroup))
            {
                throw new COParserException("Option \'" + groupName2 + "\' is not a recognized Display Group.", this.getNode());
            }

            option.setDisplayGroup((COConfigOption.DisplayGroup)defValue3);
        }

        Object defValue2 = valueType == null ? null : this.validateNamedAttribute(valueType, "default", (Object)null, true);

        if (this.getParser().target.getConfigOption(option.getName()) != null)
        {
            throw new COParserException("An Option named \'" + option.getName() + "\' already exists.", this.getNode());
        }
        else
        {
            this.getParser().target.getConfigOptions().add(option);
            String loadedValueStr1 = this.getParser().target.loadConfigOption(option.getName());

            if (loadedValueStr1 != null)
            {
                String err1 = null;

                try
                {
                    Object ex = COConfigParser.parseString(valueType, loadedValueStr1);

                    if (!((COConfigOption)option).setValue(ex))
                    {
                        err1 = "";
                    }
                }
                catch (IllegalArgumentException var15)
                {
                    err1 = " (" + var15.getMessage() + ")";
                }

                if (err1 == null)
                {
                    return true;
                }

                COLogger.log.warning("The saved value \'" + loadedValueStr1 + "\' for Config Option \'" + ((COConfigOption)option).getName() + "\' is invalid" + err1 + ".  " + "The default value \'" + (defValue2 == null ? option.getValue() : defValue2) + "\' will be used instead.");
            }

            if (defValue2 != null && !(option.setValue(defValue2)))
            {
                throw new COParserException("Invalid default value \'" + defValue2 + "\' for option \'" + option.getName() + "\'.", this.getNode());
            }
            else
            {
                return true;
            }
        }
    }
    
    public static class Factory implements IValidatorFactory<COValidatorOption>
    {
        private final Class _type;

        public Factory(Class type)
        {
            this._type = type;
        }

        public COValidatorOption createValidator(COValidatorNode parent, Node node)
        {
            return new COValidatorOption(parent, node, this._type);
        }
    }

}
