package customore.config.validation;

import org.w3c.dom.Node;
import org.w3c.dom.UserDataHandler;

import customore.config.parser.COParserException;

public class COValidatorSection extends COValidatorNode
{
    protected COValidatorSection(COValidatorNode parent, Node node)
    {
        super(parent, node);
    }

    protected boolean validateChildren() throws COParserException
    {
        super.validateChildren();
        this.getNode().setUserData("validated", Boolean.valueOf(true), (UserDataHandler)null);
        this.replaceWithNodeContents(new Node[] {this.getNode()});
        return false;
    }
    
    public static class Factory implements IValidatorFactory<COValidatorSection>
    {
        public COValidatorSection createValidator(COValidatorNode parent, Node node)
        {
            return new COValidatorSection(parent, node);
        }
    }

}
