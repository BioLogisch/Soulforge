package customore.config.validation;

import org.w3c.dom.Node;
import org.w3c.dom.UserDataHandler;

import customore.config.parser.COConfigParser;
import customore.config.parser.COExpressionEvaluator;
import customore.config.parser.COExpressionEvaluator.EvaluatorException;
import customore.config.parser.COParserException;

public class COValidatorSimpleNode extends COValidatorNode
{
    public Object content = null;
    public COExpressionEvaluator evaluator;
    private final Class _targetClass;

    protected COValidatorSimpleNode(COValidatorNode parent, Node node, Class targetClass, COExpressionEvaluator evaluator)
    {
        super(parent, node);
        this._targetClass = targetClass;
        this.evaluator = (COExpressionEvaluator)(evaluator == null ? this.getParser().defaultEvaluator : evaluator);
    }

    protected boolean validateChildren() throws COParserException
    {
        super.validateChildren();
        StringBuilder buffer = new StringBuilder();

        for (Node input = this.getNode().getFirstChild(); input != null; input = input.getNextSibling())
        {
            if (input.getNodeType() == 3)
            {
                input.setUserData("validated", Boolean.valueOf(true), (UserDataHandler)null);
                buffer.append(input.getNodeValue());
            }
        }

        String input1 = buffer.toString().trim();

        try
        {
            if (input1.startsWith(":="))
            {
                Object ex = this.evaluator.evaluate(input1.substring(2));

                if (ex == null)
                {
                    this.content = null;
                }
                else if (this._targetClass.isInstance(ex))
                {
                    this.content = ex;
                }
                else if (ex instanceof Number && Number.class.isAssignableFrom(this._targetClass))
                {
                    this.getParser();
                    this.content = COConfigParser.convertNumber(this._targetClass, (Number)ex);
                }
                else
                {
                    this.content = COConfigParser.parseString(this._targetClass, ex.toString());
                }
            }
            else
            {
                this.content = COConfigParser.parseString(this._targetClass, input1);
            }

            return true;
        }
        catch (IllegalArgumentException var4)
        {
            throw new COParserException(var4.getMessage(), this.getNode(), var4);
        }
        catch (EvaluatorException var5)
        {
            throw new COParserException(var5.getMessage(), this.getNode(), var5);
        }
    }
    
    public static class Factory implements IValidatorFactory<COValidatorSimpleNode>
    {
        private final Class _targetClass;
        private final COExpressionEvaluator _evaluator;

        public Factory(Class targetClass, COExpressionEvaluator evaluator)
        {
            this._targetClass = targetClass;
            this._evaluator = evaluator;
        }

        public Factory(Class targetClass)
        {
            this._targetClass = targetClass;
            this._evaluator = null;
        }

        public COValidatorSimpleNode createValidator(COValidatorNode parent, Node node)
        {
            return new COValidatorSimpleNode(parent, node, this._targetClass, this._evaluator);
        }
    }

}
