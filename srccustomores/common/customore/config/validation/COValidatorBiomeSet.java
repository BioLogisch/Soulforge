package customore.config.validation;

import java.util.Collection;

import org.w3c.dom.Node;

import customore.config.parser.COParserException;
import customore.util.COBiomeDescriptor;

public class COValidatorBiomeSet extends COValidatorNode {
	
	public COBiomeDescriptor biomeSet;
	public float weight = 1.0F;
	
	protected COValidatorBiomeSet(COValidatorNode parent, Node node) {
		super(parent, node);
	}

	protected boolean validateChildren() throws COParserException
	{
		super.validateChildren();
		
		this.biomeSet = new COBiomeDescriptor();
		String name = this.validateNamedAttribute(String.class, "name", null, true);
		if (name != null) {
			this.biomeSet.setName(name);
			this.getParser().target.getBiomeSets().add(this.biomeSet);
		}
		
		this.weight = this.validateNamedAttribute(Float.class, "Weight", this.weight, true);
		
		String inherits = this.validateNamedAttribute(String.class, "inherits", null, true);
		if (inherits != null)	
		{
			Collection<COBiomeDescriptor> sets = this.getParser().target.getBiomeSets(inherits);

			if (sets.isEmpty())
			{
				throw new COParserException("Cannot inherit biomes (\'" + inherits + "\' is not a loaded biome set).", this.getNode());
			}

			if (sets.size() > 1)
			{
	            throw new COParserException("Cannot inherit biomes (\'" + inherits + "\' is ambiguous; matching " + sets.size() + " loaded biome sets).", this.getNode());
			}

			try
			{
				this.biomeSet.addAll(sets.iterator().next(), this.weight);
			}
			catch (IllegalArgumentException e)
			{
				throw new COParserException("Cannot inherit biomes (" + e.getMessage() + ").", this.getNode(), e);
			}
		}
	        
		validateBiomes();
		
		this.getNode().setUserData("validated", true, null);
		
		return true;
	}
	    
	public void validateBiomes() throws COParserException {
		for (COValidatorBiomeDescriptor biome : validateNamedChildren(2, "Biome", new COValidatorBiomeDescriptor.Factory())) {
			this.biomeSet.add(biome.biome, biome.weight * this.weight, biome.climate, false);
        }
        for (COValidatorBiomeDescriptor biomeType : validateNamedChildren(2, "BiomeType", new COValidatorBiomeDescriptor.Factory())) {
        	this.biomeSet.add(biomeType.biome, biomeType.weight * this.weight, biomeType.climate, true);
        }
        for (COValidatorBiomeSet biomeSet : validateNamedChildren(2, "BiomeSet", new COValidatorBiomeSet.Factory())) {
        	this.biomeSet.addAll(biomeSet.biomeSet, this.weight);
        }
	}
	
	public static class Factory implements IValidatorFactory<COValidatorBiomeSet>
	{
		public COValidatorBiomeSet createValidator(COValidatorNode parent, Node node)
		{
			return new COValidatorBiomeSet(parent, node);
		}
	}

}
