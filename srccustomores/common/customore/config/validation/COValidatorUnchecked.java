package customore.config.validation;

import org.w3c.dom.Node;

import customore.config.parser.COParserException;

public class COValidatorUnchecked extends COValidatorNode
{
    protected COValidatorUnchecked(COValidatorNode parent, Node node)
    {
        super(parent, node);
    }

    protected boolean validateChildren() throws COParserException
    {
        super.validateChildren();
        return false;
    }
    

    public static class Factory implements IValidatorFactory<COValidatorUnchecked>
    {
    	public COValidatorUnchecked createValidator(COValidatorNode parent, Node node)
    	{
    		return new COValidatorUnchecked(parent, node);
    	}
    }

}
