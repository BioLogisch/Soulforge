package customore.config.validation;

import org.w3c.dom.Node;

import customore.config.parser.COParserException;
import customore.util.COBiomeDescriptor.Climate;

public class COValidatorBiomeDescriptor extends COValidatorNode
{
    public String biome = null;
    public float weight = 1.0F;
    public Climate climate = new Climate(); 

    protected COValidatorBiomeDescriptor(COValidatorNode parent, Node node)
    {
        super(parent, node);
    }

    protected boolean validateChildren() throws COParserException
    {
        super.validateChildren();
        this.biome = this.validateRequiredAttribute(String.class, "Name", true);
        this.weight = this.validateNamedAttribute(Float.class, "Weight", this.weight, true);
        float minTemperature = this.validateNamedAttribute(Float.class, "MinTemperature", climate.minTemperature, true);
        float maxTemperature = this.validateNamedAttribute(Float.class, "MaxTemperature", climate.maxTemperature, true);
        float minRainfall = this.validateNamedAttribute(Float.class, "MinRainfall", climate.minRainfall, true);
        float maxRainfall = this.validateNamedAttribute(Float.class, "MaxRainfall", climate.maxRainfall, true);
        this.climate = new Climate(minTemperature, maxTemperature, minRainfall, maxRainfall);
        return true;
    }
    
    public static class Factory implements IValidatorFactory<COValidatorBiomeDescriptor>
    {
        public COValidatorBiomeDescriptor createValidator(COValidatorNode parent, Node node)
        {
            return new COValidatorBiomeDescriptor(parent, node);
        }
    }

}
