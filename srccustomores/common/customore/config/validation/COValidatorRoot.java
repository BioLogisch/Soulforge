package customore.config.validation;

import java.util.Collection;

import org.w3c.dom.Node;
import org.w3c.dom.UserDataHandler;

import customore.config.parser.COParserException;

public class COValidatorRoot extends COValidatorNode
{
    private final Collection<String> _topLevelNodes;

    protected COValidatorRoot(COValidatorNode parent, Node node, Collection<String> topLevelNodes)
    {
        super(parent, node);
        this._topLevelNodes = topLevelNodes;
    }

    protected boolean validateChildren() throws COParserException
    {
        super.validateChildren();
        Node parent = this.getNode().getParentNode();

        if (parent != null && parent.getNodeType() == 9)
        {
            this.getNode().setUserData("validated", true, (UserDataHandler)null);

            if (this._topLevelNodes != null)
            {
            	for (String nodeName : this._topLevelNodes) {
            		this.validateNamedChildren(2, nodeName, (IValidatorFactory)null);
            	}
            }
        }

        return true;
    }
    
    public static class Factory implements IValidatorFactory<COValidatorRoot>
    {
        private final Collection<String> _topLevelNodes;

        public Factory(Collection<String> topLevelNodes)
        {
            this._topLevelNodes = topLevelNodes;
        }

        public COValidatorRoot createValidator(COValidatorNode parent, Node node)
        {
            return new COValidatorRoot(parent, node, this._topLevelNodes);
        }
    }

}
