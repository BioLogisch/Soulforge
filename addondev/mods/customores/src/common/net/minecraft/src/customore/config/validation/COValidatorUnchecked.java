package net.minecraft.src.customore.config.validation;

import net.minecraft.src.customore.config.parser.COParserException;

import org.w3c.dom.Node;

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
