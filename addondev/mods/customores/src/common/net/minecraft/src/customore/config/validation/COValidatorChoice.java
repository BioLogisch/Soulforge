package net.minecraft.src.customore.config.validation;

import net.minecraft.src.customore.config.parser.COParserException;

import org.w3c.dom.Node;

public class COValidatorChoice extends COValidatorNode
{
    public String value = null;
    public String displayValue = null;
    public String description = null;

    protected COValidatorChoice(COValidatorNode parent, Node node)
    {
        super(parent, node);
    }

    protected boolean validateChildren() throws COParserException
    {
        super.validateChildren();
        this.value = this.validateRequiredAttribute(String.class, "Value", true);
        this.displayValue = this.validateNamedAttribute(String.class, "DisplayValue", this.displayValue, true);
        this.description = this.validateNamedAttribute(String.class, "Description", null, true);
        return true;
    }
    
    public static class Factory implements IValidatorFactory<COValidatorChoice>
    {
        public COValidatorChoice createValidator(COValidatorNode parent, Node node)
        {
            return new COValidatorChoice(parent, node);
        }
    }

}
