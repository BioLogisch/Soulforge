package customore.config.validation;

import org.w3c.dom.Node;

import customore.config.parser.COParserException;

public class COValidatorBlockDescriptor extends COValidatorNode
{
    public String blocks = null;
    public float weight = 1.0F;

    protected COValidatorBlockDescriptor(COValidatorNode parent, Node node)
    {
        super(parent, node);
    }

    protected boolean validateChildren() throws COParserException
    {
        super.validateChildren();
        this.blocks = (String)this.validateRequiredAttribute(String.class, "Block", true);
        this.weight = ((Float)this.validateNamedAttribute(Float.class, "Weight", Float.valueOf(this.weight), true)).floatValue();
        return true;
    }
    
    public static class Factory implements IValidatorFactory<COValidatorBlockDescriptor>
    {
        public COValidatorBlockDescriptor createValidator(COValidatorNode parent, Node node)
        {
            return new COValidatorBlockDescriptor(parent, node);
        }
    }

}
