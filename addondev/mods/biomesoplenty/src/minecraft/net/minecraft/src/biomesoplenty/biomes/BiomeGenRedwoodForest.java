package net.minecraft.src.biomesoplenty.biomes;

import java.util.Random;

import net.minecraft.src.BiomeGenBase;
import net.minecraft.src.Block;
import net.minecraft.src.World;
import net.minecraft.src.WorldGenShrub;
import net.minecraft.src.WorldGenTallGrass;
import net.minecraft.src.WorldGenerator;
import net.minecraft.src.biomesoplenty.worldgen.WorldGenRedwoodTree;
import net.minecraft.src.biomesoplenty.worldgen.WorldGenRedwoodTree2;

public class BiomeGenRedwoodForest extends BiomeGenBase
{
	private BiomeDecoratorBOP customBiomeDecorator;

	public BiomeGenRedwoodForest(int par1)
	{
		super(par1);
		theBiomeDecorator = new BiomeDecoratorBOP(this);
		customBiomeDecorator = (BiomeDecoratorBOP)theBiomeDecorator;
		customBiomeDecorator.treesPerChunk = 6;
		customBiomeDecorator.grassPerChunk = 16;
		//customBiomeDecorator.bushesPerChunk = 4;
		//customBiomeDecorator.berryBushesPerChunk = 1;
		customBiomeDecorator.generatePumpkins = false;
	}

	/**
	 * Gets a WorldGen appropriate for this biome.
	 */
	@Override
	public WorldGenerator getRandomWorldGenForTrees(Random par1Random)
	{
		return par1Random.nextInt(4) == 0 ? new WorldGenRedwoodTree(false) : (par1Random.nextInt(2) == 0 ? new WorldGenShrub(0,0) : new WorldGenRedwoodTree2(false));
	}
	
	@Override
	public void decorate(World par1World, Random par2Random, int par3, int par4)
	{
		super.decorate(par1World, par2Random, par3, par4);
		int var5 = 3 + par2Random.nextInt(6);

		for (int var6 = 0; var6 < var5; ++var6)
		{
			int var7 = par3 + par2Random.nextInt(16);
			int var8 = par2Random.nextInt(28) + 4;
			int var9 = par4 + par2Random.nextInt(16);
			int var10 = par1World.getBlockId(var7, var8, var9);

            if (var10 == Block.stone.blockID)
            {
                int var11 = 0;

                if (var8 <= 48 + par1World.rand.nextInt(2))
                {
                    byte var12 = 1;

                    if (var8 <= 24 + par1World.rand.nextInt(2))
                    {
                        var12 = 2;
                    }

                    var11 = Block.oreEmerald.GetMetadataConversionForStrataLevel(var12, 0);
                }

                par1World.setBlock(var7, var8, var9, Block.oreEmerald.blockID, var11, 2);
            }
		}
	}

	/**
	 * Gets a WorldGen appropriate for this biome.
	 */
	@Override
	public WorldGenerator getRandomWorldGenForGrass(Random par1Random)
	{
		return par1Random.nextInt(4) == 0 ? new WorldGenTallGrass(Block.tallGrass.blockID, 2) : new WorldGenTallGrass(Block.tallGrass.blockID, 1);
	}
}
