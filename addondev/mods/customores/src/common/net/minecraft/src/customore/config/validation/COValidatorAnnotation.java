package net.minecraft.src.customore.config.validation;

import org.w3c.dom.Node;
import org.w3c.dom.UserDataHandler;

import net.minecraft.src.customore.config.parser.COExpressionEvaluator;
import net.minecraft.src.customore.config.parser.COParserException;
import net.minecraft.src.customore.config.validation.COValidatorNode.IValidatorFactory;

public class COValidatorAnnotation extends COValidatorSimpleNode
{
    protected COValidatorAnnotation(COValidatorNode parent, Node node)
    {
        super(parent, node, String.class, (COExpressionEvaluator)null);
    }

    protected boolean validateChildren() throws COParserException
    {
        super.validateChildren();
        this.getNode().setUserData("validated", Boolean.valueOf(true), (UserDataHandler)null);
        return false;
    }
    
    public static class Factory implements IValidatorFactory<COValidatorAnnotation>
    {
        public COValidatorAnnotation createValidator(COValidatorNode parent, Node node)
        {
            return new COValidatorAnnotation(parent, node);
        }
    }

}
