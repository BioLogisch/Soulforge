package net.minecraft.src.biomesoplenty.worldgen;

import java.util.Random;

import net.minecraft.src.Block;
import net.minecraft.src.World;
import net.minecraft.src.WorldGenerator;
import net.minecraft.src.biomesoplenty.api.BOPAPIBlocks;
import net.minecraft.src.biomesoplenty.configuration.BOPConfiguration;

public class WorldGenOminous1 extends WorldGenerator
{
	public WorldGenOminous1(boolean par1)
	{
		super(par1);
	}

	@Override
	public boolean generate(World par1World, Random par2Random, int par3, int par4, int par5)
	{
		int var6 = par2Random.nextInt(6) + 14;
		int var7 = 4 + par2Random.nextInt(3);
		int var8 = var6 - var7;
		int var9 = 2 + par2Random.nextInt(2);
		boolean var10 = true;

		if (par4 >= 1 && par4 + var6 + 1 <= 256)
		{
			int var11;
			int var13;
			int var15;
			int var21;

			for (var11 = par4; var11 <= par4 + 1 + var6 && var10; ++var11)
			{
				boolean var12 = true;

				if (var11 - par4 < var7)
				{
					var21 = 0;
				}
				else
				{
					var21 = var9;
				}

				for (var13 = par3 - var21; var13 <= par3 + var21 && var10; ++var13)
				{
					for (int var14 = par5 - var21; var14 <= par5 + var21 && var10; ++var14)
					{
						if (var11 >= 0 && var11 < 256)
						{
							var15 = par1World.getBlockId(var13, var11, var14);

							if (var15 != 0 && (BOPConfiguration.mainConfigFile.getBoolean("enableCustomContent") && var15 != BOPAPIBlocks.leaves1.blockID || var15 != Block.leaves.blockID))
							{
								var10 = false;
							}
						}
						else
						{
							var10 = false;
						}
					}
				}
			}

			if (!var10)
				return false;
			else
			{
				var11 = par1World.getBlockId(par3, par4 - 1, par5);

				if ((var11 == Block.grass.blockID || var11 == Block.dirt.blockID) && par4 < 256 - var6 - 1)
				{
					this.setBlock(par1World, par3, par4 - 1, par5, Block.dirt.blockID);
					var21 = par2Random.nextInt(2);
					var13 = 1;
					byte var22 = 0;
					int var17;
					int var16;

					for (var15 = 0; var15 <= var8; ++var15)
					{
						var16 = par4 + var6 - var15;

						for (var17 = par3 - var21; var17 <= par3 + var21; ++var17)
						{
							int var18 = var17 - par3;

							for (int var19 = par5 - var21; var19 <= par5 + var21; ++var19)
							{
								int var20 = var19 - par5;

								if ((Math.abs(var18) != var21 || Math.abs(var20) != var21 || var21 <= 0) && !Block.opaqueCubeLookup[par1World.getBlockId(var17, var16, var19)])
								{
									if (BOPConfiguration.mainConfigFile.getBoolean("enableCustomContent"))
									{
										this.setBlockAndMetadata(par1World, var17, var16, var19, BOPAPIBlocks.leaves1.blockID, 3);
									}
									else
									{
										this.setBlockAndMetadata(par1World, var17, var16, var19, Block.leaves.blockID, 0);
									}
								}
							}
						}

						if (var21 >= var13)
						{
							var21 = var22;
							var22 = 1;
							++var13;

							if (var13 > var9)
							{
								var13 = var9;
							}
						}
						else
						{
							++var21;
						}
					}

					var15 = par2Random.nextInt(3);

					for (var16 = 0; var16 < var6 - var15; ++var16)
					{
						var17 = par1World.getBlockId(par3, par4 + var16, par5);

						if (var17 == 0 || (BOPConfiguration.mainConfigFile.getBoolean("enableCustomContent") && var17 == BOPAPIBlocks.leaves1.blockID) || var17 == Block.leaves.blockID)
						{
							if (BOPConfiguration.mainConfigFile.getBoolean("enableCustomContent"))
							{
								this.setBlockAndMetadata(par1World, par3, par4 + var16, par5, BOPAPIBlocks.logs1.blockID, 2);
							}
							else
							{
								this.setBlockAndMetadata(par1World, par3, par4 + var16, par5, Block.wood.blockID, 0);
							}
						}
					}

					return true;
				} else
					return false;
			}
		} else
			return false;
	}
}
