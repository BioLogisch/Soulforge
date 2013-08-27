package net.minecraft.src.customore.config.validation;

import org.w3c.dom.Node;
import org.w3c.dom.UserDataHandler;

import net.minecraft.src.customore.config.parser.COExpressionEvaluator;
import net.minecraft.src.customore.config.parser.COParserException;
import net.minecraft.src.customore.config.parser.COExpressionEvaluator.EvaluatorException;

public class COValidatorExpression extends COValidatorSimpleNode
{
    protected COValidatorExpression(COValidatorNode parent, Node node, COExpressionEvaluator evaluator)
    {
        super(parent, node, String.class, evaluator);
    }

    protected boolean validateChildren() throws COParserException
    {
        super.validateChildren();
        this.getNode().setUserData("validated", Boolean.valueOf(true), (UserDataHandler)null);
        this.checkChildrenValid();
        Object value = null;

        try
        {
            value = this.evaluator.evaluate((String)this.content);
        }
        catch (EvaluatorException var3)
        {
            throw new COParserException(var3.getMessage(), this.getNode(), var3);
        }

        this.replaceWithNode(new Node[] {value == null ? null : this.getNode().getOwnerDocument().createTextNode(value.toString())});
        return false;
    }
    
    public static class Factory implements IValidatorFactory<COValidatorExpression>
    {
        private final COExpressionEvaluator _evaluator;

        public Factory(COExpressionEvaluator evaluator)
        {
            this._evaluator = evaluator;
        }

        public COValidatorExpression createValidator(COValidatorNode parent, Node node)
        {
            return new COValidatorExpression(parent, node, this._evaluator);
        }
    }

}
