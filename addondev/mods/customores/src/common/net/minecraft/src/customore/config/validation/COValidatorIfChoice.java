package net.minecraft.src.customore.config.validation;

import net.minecraft.src.customore.config.COChoiceOption;
import net.minecraft.src.customore.config.COConfigOption;
import net.minecraft.src.customore.config.parser.COParserException;

import org.w3c.dom.Node;

public class COValidatorIfChoice extends COValidatorCondition
{
    protected COValidatorIfChoice(COValidatorNode parent, Node node, boolean invert)
    {
        super(parent, node, invert);
    }

    protected boolean evaluateCondition() throws COParserException
    {
        String optionName = this.validateRequiredAttribute(String.class, "name", true);
        String strValue = this.validateNamedAttribute(String.class, "value", null, true);
        COConfigOption option = this.getParser().target.getConfigOption(optionName);
        boolean isOptionValid = option != null && option instanceof COChoiceOption;

        if (strValue == null)
        {
            return isOptionValid;
        }
        else if (isOptionValid)
        {
            return strValue.equalsIgnoreCase((String)option.getValue());
        }
        else
        {
            throw new COParserException("Option \'" + optionName + "\' is not a recognized Choice option.", this.getNode());
        }
    }
    
    public static class Factory implements IValidatorFactory<COValidatorIfChoice>
    {
        private final boolean _invert;

        public Factory(boolean invert)
        {
            this._invert = invert;
        }

        public COValidatorIfChoice createValidator(COValidatorNode parent, Node node)
        {
            return new COValidatorIfChoice(parent, node, this._invert);
        }
    }

}
