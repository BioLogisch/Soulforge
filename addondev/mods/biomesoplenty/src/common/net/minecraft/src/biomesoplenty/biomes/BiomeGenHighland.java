package net.minecraft.src.biomesoplenty.biomes;

import java.util.Random;

import net.minecraft.src.BiomeGenBase;
import net.minecraft.src.Block;
import net.minecraft.src.World;

public class BiomeGenHighland extends BiomeGenBase
{
	private BiomeDecoratorBOP customBiomeDecorator;

	public BiomeGenHighland(int par1)
	{
		super(par1);
		theBiomeDecorator = new BiomeDecoratorBOP(this);
		customBiomeDecorator = (BiomeDecoratorBOP)theBiomeDecorator;
		customBiomeDecorator.treesPerChunk = -999;
		//customBiomeDecorator.highGrassPerChunk = 25;
		customBiomeDecorator.grassPerChunk = 25;
		customBiomeDecorator.potatoesPerChunk = -999;
		customBiomeDecorator.generateBoulders = false;
		customBiomeDecorator.carrotsPerChunk = 1;
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
}
