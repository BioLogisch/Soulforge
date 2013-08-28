package biomesoplenty.biomes;

import java.util.Random;

import net.minecraft.src.BiomeGenBase;
import net.minecraft.src.Block;
import net.minecraft.src.EntitySnowman;
import net.minecraft.src.SpawnListEntry;
import net.minecraft.src.World;
import net.minecraft.src.WorldGenerator;
import biomesoplenty.worldgen.WorldGenIceTree;

public class BiomeGenIcyHills extends BiomeGenBase
{
	private BiomeDecoratorBOP customBiomeDecorator;

	@SuppressWarnings("unchecked")
	public BiomeGenIcyHills(int par1)
	{
		super(par1);
		spawnableCreatureList.clear();
		topBlock = (byte)Block.blockSnow.blockID;
		fillerBlock = (byte)Block.blockSnow.blockID;
		theBiomeDecorator = new BiomeDecoratorBOP(this);
		customBiomeDecorator = (BiomeDecoratorBOP)theBiomeDecorator;
		customBiomeDecorator.treesPerChunk = 2;
		customBiomeDecorator.flowersPerChunk = -999;
		customBiomeDecorator.grassPerChunk = -999;
		spawnableCreatureList.add(new SpawnListEntry(EntitySnowman.class, 30, 2, 4));
	}
	
	@Override
	public void decorate(World par1World, Random par2Random, int par3, int par4)
	{
		super.decorate(par1World, par2Random, par3, par4);
		int var5 = 12 + par2Random.nextInt(6);

		for (int var6 = 0; var6 < var5; ++var6)
		{
			int var7 = par3 + par2Random.nextInt(16);
			int var8 = par2Random.nextInt(28) + 4;
			int var9 = par4 + par2Random.nextInt(16);
			int var10 = par1World.getBlockId(var7, var8, var9);
		}
	}

	/**
	 * Gets a WorldGen appropriate for this biome.
	 */
	@Override
	public WorldGenerator getRandomWorldGenForTrees(Random par1Random)
	{
		return new WorldGenIceTree(false);
	}

	/**
	 * takes temperature, returns color
	 */
	@Override
	public int getSkyColorByTemp(float par1)
	{
		//if (BOPConfiguration.skyColors)
			return 16777215;
		/*else
		{
			par1 /= 3.0F;

			if (par1 < -1.0F)
			{
				par1 = -1.0F;
			}

			if (par1 > 1.0F)
			{
				par1 = 1.0F;
			}

			return Color.getHSBColor(0.62222224F - par1 * 0.05F, 0.5F + par1 * 0.1F, 1.0F).getRGB();
		}*/
	}
}
