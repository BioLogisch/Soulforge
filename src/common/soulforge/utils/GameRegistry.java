package soulforge.utils;

import java.util.HashSet;
import java.util.Random;

import soulforge.itemblock.ItemMultiBlock;
import net.minecraft.src.Block;
import net.minecraft.src.CraftingManager;
import net.minecraft.src.IChunkProvider;
import net.minecraft.src.IRecipe;
import net.minecraft.src.Item;
import net.minecraft.src.ItemBlock;
import net.minecraft.src.ItemStack;
import net.minecraft.src.World;

public class GameRegistry 
{

	private static HashSet<IWorldGenerator> worldGenerators = new HashSet<IWorldGenerator>();


	/**
	 * Register a world generator - something that inserts new block types into the world
	 *
	 * @param generator
	 */
	public static void registerWorldGenerator(IWorldGenerator generator)
	{
		worldGenerators.add(generator);
	}

	/**
	 * Callback hook for world gen - if your mod wishes to add extra mod related generation to the world
	 * call this
	 *
	 * @param chunkX
	 * @param chunkZ
	 * @param world
	 * @param chunkGenerator
	 * @param chunkProvider
	 */
	public static void generateWorld(int chunkX, int chunkZ, World world, IChunkProvider chunkGenerator, IChunkProvider chunkProvider)
	{
		long worldSeed = world.getSeed();
		Random random = new Random(worldSeed);
		long xSeed = random.nextLong() >> 2 + 1L;
		long zSeed = random.nextLong() >> 2 + 1L;
		random.setSeed((xSeed * chunkX + zSeed * chunkZ) ^ worldSeed);

		for (IWorldGenerator generator : worldGenerators)
		{
			generator.generate(random, chunkX, chunkZ, world, chunkGenerator, chunkProvider);
		}
	}

	public static void registerBlock(Block block) 
	{
		Item.itemsList[block.blockID] = new ItemBlock(block.blockID - 256);
	}

	public static void registerCustomBlock(Block block, ItemBlock type)
	{	
		Item.itemsList[block.blockID] = type;
	}

	public static void registerMultiBlock(Block block, String[] types)
	{
		Item.itemsList[block.blockID] = new ItemMultiBlock(block, types);
	}

	public static void addRecipe(ItemStack output, Object... params)
	{
		addShapedRecipe(output, params);
	}

	public static IRecipe addShapedRecipe(ItemStack output, Object... params)
	{
		return CraftingManager.getInstance().addRecipe(output, params);
	}

	public static void addShapelessRecipe(ItemStack output, Object... params)
	{
		CraftingManager.getInstance().addShapelessRecipe(output, params);
	}

	public static void addRecipe(IRecipe recipe)
	{
		CraftingManager.getInstance().getRecipeList().add(recipe);
	}
}
