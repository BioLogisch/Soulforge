package customore.config.validation;

import org.w3c.dom.Node;

import customore.config.parser.COParserException;

public class COValidatorIfCondition extends COValidatorCondition
{
    protected COValidatorIfCondition(COValidatorNode parent, Node node)
    {
        super(parent, node, false);
    }

    protected boolean evaluateCondition() throws COParserException
    {
        return ((Boolean)this.validateRequiredAttribute(Boolean.class, "Condition", true)).booleanValue();
    }
    
    public static class Factory implements IValidatorFactory<COValidatorIfCondition>
    {
        public COValidatorIfCondition createValidator(COValidatorNode parent, Node node)
        {
            return new COValidatorIfCondition(parent, node);
        }
    }

}
