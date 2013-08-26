package net.minecraft.src.biomesoplenty.worldgen;

import java.util.Random;

import net.minecraft.src.Block;
import net.minecraft.src.World;
import net.minecraft.src.WorldGenerator;

public class WorldGenChaparral3 extends WorldGenerator
{
	@Override
	public boolean generate(World var1, Random var2, int var3, int var4, int var5)
	{
		while (var1.isAirBlock(var3, var4, var5) && var4 > 2)
		{
			--var4;
		}

		int var6 = var1.getBlockId(var3, var4, var5);

		if (var6 != Block.grass.blockID && var6 != Block.sand.blockID)
			return false;
		else
		{
			for (int var7 = -2; var7 <= 2; ++var7)
			{
				for (int var8 = -2; var8 <= 2; ++var8)
				{
					if (var1.isAirBlock(var3 + var7, var4 - 1, var5 + var8) && var1.isAirBlock(var3 + var7, var4 - 2, var5 + var8))
						return false;
				}
			}

			var1.setBlock(var3, var4, var5, Block.wood.blockID);
			var1.setBlock(var3, var4 + 1, var5, Block.leaves.blockID);
			return true;
		}
	}
}
