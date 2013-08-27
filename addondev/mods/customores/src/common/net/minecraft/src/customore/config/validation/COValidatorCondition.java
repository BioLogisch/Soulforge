package net.minecraft.src.customore.config.validation;

import net.minecraft.src.customore.config.parser.COParserException;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.UserDataHandler;

public abstract class COValidatorCondition extends COValidatorNode
{
    protected boolean invert = false;

    protected COValidatorCondition(COValidatorNode parent, Node node, boolean invert)
    {
        super(parent, node);
        this.invert = invert;
    }

    protected boolean validateChildren() throws COParserException
    {
        Element trueBlock = null;
        Element falseBlock = null;

        for (Node condition = this.getNode().getFirstChild(); condition != null; condition = condition.getNextSibling())
        {
            if (condition.getNodeType() == 1)
            {
                if (condition.getNodeName().equalsIgnoreCase("Then"))
                {
                    trueBlock = (Element)condition;
                }
                else if (condition.getNodeName().equalsIgnoreCase("Else"))
                {
                    falseBlock = (Element)condition;
                }
            }
        }

        if (trueBlock == null)
        {
            if (falseBlock != null)
            {
                throw new COParserException("Cannot have Else without Then", falseBlock);
            }

            trueBlock = this.getNode().getOwnerDocument().createElement("Then");

            while (this.getNode().hasChildNodes())
            {
                trueBlock.appendChild(this.getNode().getFirstChild());
            }

            this.getNode().appendChild(trueBlock);
        }

        trueBlock.setUserData("validated", Boolean.valueOf(true), (UserDataHandler)null);

        if (falseBlock != null)
        {
            falseBlock.setUserData("validated", Boolean.valueOf(true), (UserDataHandler)null);
        }

        super.validateChildren();
        boolean condition1 = this.evaluateCondition();

        if (this.invert)
        {
            condition1 = !condition1;
        }

        this.getNode().setUserData("validated", Boolean.valueOf(true), (UserDataHandler)null);
        this.checkChildrenValid();
        Element resultBlock = condition1 ? trueBlock : falseBlock;

        if (resultBlock != null)
        {
            (new COValidatorUnchecked(this, resultBlock)).validate();
        }

        this.replaceWithNodeContents(new Node[] {resultBlock});
        return false;
    }

    protected abstract boolean evaluateCondition() throws COParserException;
}
