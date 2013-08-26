package net.minecraft.src.biomesoplenty.world.layer;

import net.minecraft.src.IntCache;

public class BiomeLayerRiverInit extends BiomeLayer
{
	public BiomeLayerRiverInit(long par1, BiomeLayer par3GenLayer)
	{
		super(par1);
		parent = par3GenLayer;
	}

	@Override
	public int[] getInts(int par1, int par2, int par3, int par4)
	{
		int[] aint = parent.getInts(par1, par2, par3, par4);
		int[] aint1 = IntCache.getIntCache(par3 * par4);

		for (int i1 = 0; i1 < par4; ++i1)
		{
			for (int j1 = 0; j1 < par3; ++j1)
			{
				this.initChunkSeed(j1 + par1, i1 + par2);
				aint1[j1 + i1 * par3] = aint[j1 + i1 * par3] > 0 ? this.nextInt(2) + 2 : 0;
			}
		}

		return aint1;
	}
}