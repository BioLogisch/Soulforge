package net.minecraft.src.customore.config.validation;

import org.w3c.dom.Node;

import net.minecraft.src.customore.config.parser.COParserException;
import net.minecraft.src.customore.generator.ICOOreDistribution;
import net.minecraft.src.customore.generator.tools.COPDist;
import net.minecraft.src.customore.generator.tools.COPDist.Type;

public class COValidatorPDist extends COValidatorNode
{
    private final ICOOreDistribution _parentDist;
    public String name = null;
    public COPDist pdist = null;

    protected COValidatorPDist(COValidatorNode parent, Node node, ICOOreDistribution parentDistribution)
    {
        super(parent, node);
        this._parentDist = parentDistribution;
    }

    protected boolean validateChildren() throws COParserException
    {
        super.validateChildren();
        this.name = (String)this.validateRequiredAttribute(String.class, "Name", true);

        if (this._parentDist == null)
        {
            this.pdist = new COPDist();
        }
        else
        {
            try
            {
                this.pdist = (COPDist)this._parentDist.getDistributionSetting(this.name);
            }
            catch (ClassCastException var4)
            {
                throw new COParserException("Setting \'" + this.name + "\' is not supported by this distribution.", this.getNode(), var4);
            }

            if (this.pdist == null)
            {
                throw new COParserException("Setting \'" + this.name + "\' is not supported by this distribution.", this.getNode());
            }
        }

        this.pdist.mean = ((Float)this.validateNamedAttribute(Float.class, "Avg", Float.valueOf(this.pdist.mean), true)).floatValue();
        this.pdist.range = ((Float)this.validateNamedAttribute(Float.class, "Range", Float.valueOf(this.pdist.range), true)).floatValue();
        this.pdist.type = (Type)this.validateNamedAttribute(Type.class, "Type", this.pdist.type, true);

        if (this._parentDist != null)
        {
            try
            {
                this._parentDist.setDistributionSetting(this.name, this.pdist);
            }
            catch (IllegalAccessException var2)
            {
                throw new COParserException("Setting \'" + this.name + "\' is not configurable.", this.getNode(), var2);
            }
            catch (IllegalArgumentException var3)
            {
                throw new COParserException("Setting \'" + this.name + "\' is not supported by this distribution.", this.getNode(), var3);
            }
        }

        return true;
    }
    
    public static class Factory implements IValidatorFactory<COValidatorPDist>
    {
        private final ICOOreDistribution _parentDist;

        public Factory(ICOOreDistribution parentDistribution)
        {
            this._parentDist = parentDistribution;
        }

        public COValidatorPDist createValidator(COValidatorNode parent, Node node)
        {
            return new COValidatorPDist(parent, node, this._parentDist);
        }
    }

}
