package customore.api;

import net.minecraft.src.Block;
import net.minecraft.src.CraftingManager;
import net.minecraft.src.FCBetterThanWolves;
import net.minecraft.src.FCRecipes;
import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;
import soulforge.oredict.OreDictionary;
import biomesoplenty.api.BOPAPIBlocks;
import biomesoplenty.api.BOPAPIItems;
import biomesoplenty.handlers.BOPRecipesLogChopping;

public class COAPICrafting {

	public static void init()
	{
		addCraftingRecipes();
	}
	
	private static void addCraftingRecipes()
	{
        FCRecipes.AddVanillaRecipe(new ItemStack(COAPIBlocks.chunkloader, 1), new Object[] {"XX ", "XX ", "   ", 'X', Block.cobblestone});
        FCRecipes.AddVanillaRecipe(new ItemStack(COAPIBlocks.chunkloaderspawn, 1), new Object[] {"XXX", "XXX", "XXX", 'X', Block.stone});

	}
	

	
	
}
