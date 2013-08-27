package net.minecraft.src.customore.config.validation;

import net.minecraft.src.customore.config.COConfigOption;
import net.minecraft.src.customore.config.parser.COParserException;

import org.w3c.dom.Node;
import org.w3c.dom.UserDataHandler;

public class COValidatorRefOption extends COValidatorNode
{
    public COValidatorRefOption(COValidatorNode parent, Node node)
    {
        super(parent, node);
    }

    protected boolean validateChildren() throws COParserException
    {
        super.validateChildren();
        String optionName = (String)this.validateRequiredAttribute(String.class, "name", true);
        COConfigOption option = this.getParser().target.getConfigOption(optionName);

        if (option == null)
        {
            throw new COParserException("Option \'" + optionName + "\' is not a recognized option.", this.getNode());
        }
        else
        {
            this.getNode().setUserData("validated", Boolean.valueOf(true), (UserDataHandler)null);
            this.checkChildrenValid();
            Object value = option.getValue();
            this.replaceWithNode(new Node[] {value == null ? null : this.getNode().getOwnerDocument().createTextNode(value.toString())});
            return false;
        }
    }
    
    public static class Factory implements IValidatorFactory<COValidatorRefOption>
    {
        public COValidatorRefOption createValidator(COValidatorNode parent, Node node)
        {
            return new COValidatorRefOption(parent, node);
        }
    }

}
