package biomesoplenty.worldgen;

import java.util.Random;

import net.minecraft.src.Block;
import net.minecraft.src.Direction;
import net.minecraft.src.World;
import net.minecraft.src.WorldGenerator;
import biomesoplenty.api.BOPAPIBlocks;
import biomesoplenty.configuration.BOPConfiguration;

public class WorldGenRedwoodTree extends WorldGenerator
{
	/** The minimum height of a generated tree. */
	private final int minTreeHeight;

	/** True if this tree should grow Vines. */
	private final boolean vinesGrow;

	/** The metadata value of the wood to use in tree generation. */
	private final int metaWood;

	/** The metadata value of the leaves to use in tree generation. */
	private final int metaLeaves;

	public WorldGenRedwoodTree(boolean par1)
	{
		this(par1, 30, 0, 0, false);
	}

	public WorldGenRedwoodTree(boolean par1, int par2, int par3, int par4, boolean par5)
	{
		super(par1);
		minTreeHeight = par2;
		metaWood = par3;
		metaLeaves = par4;
		vinesGrow = par5;
	}

	@Override
	public boolean generate(World par1World, Random par2Random, int par3, int par4, int par5)
	{
		int var6 = par2Random.nextInt(10) + minTreeHeight;
		boolean var7 = true;

		if (par4 >= 1 && par4 + var6 + 1 <= 256)
		{
			int var8;
			byte var9;
			int var11;
			int var12;
			int var81;
			int var82;
			int var83;
			int var84;
			int var85;
			int var86;
			int var87;
			int var88;
			int var89;
			int var90;
			int var91;
			
			int leavesID;
			int logID;
			int leavesMeta;
			
			if (BOPConfiguration.mainConfigFile.getBoolean("enableCustomContent"))
			{
				logID = BOPAPIBlocks.logs3.blockID;
				leavesID = BOPAPIBlocks.leavesColourized.blockID;
				
				leavesMeta = 3;
			}
			else
			{
				logID = Block.wood.blockID;
				leavesID = Block.leaves.blockID;
				
				leavesMeta = 0;
			}

			for (var8 = par4; var8 <= par4 + 1 + var6; ++var8)
			{
				var9 = 1;

				if (var8 == par4)
				{
					var9 = 0;
				}

				if (var8 >= par4 + 1 + var6 - 2)
				{
					var9 = 2;
				}

				for (int var10 = par3 - var9; var10 <= par3 + var9 && var7; ++var10)
				{
					for (var11 = par5 - var9; var11 <= par5 + var9 && var7; ++var11)
					{
						if (var8 >= 0 && var8 < 256)
						{
							var12 = par1World.getBlockId(var10, var8, var11);

							if (var12 != 0 && var12 != leavesID && var12 != Block.grass.blockID && var12 != Block.dirt.blockID && var12 != logID)
							{
								var7 = false;
							}
						}
						else
						{
							var7 = false;
						}
					}
				}
			}

			if (!var7)
				return false;
			else
			{
				var8 = par1World.getBlockId(par3 - 1, par4 - 1, par5);
				var81 = par1World.getBlockId(par3 + 1, par4 - 1, par5);
				var82 = par1World.getBlockId(par3, par4 - 1, par5 - 1);
				var83 = par1World.getBlockId(par3, par4 - 1, par5 + 1);
				var84 = par1World.getBlockId(par3 - 1, par4 - 1, par5 - 1);
				var85 = par1World.getBlockId(par3 + 1, par4 - 1, par5 - 1);
				var86 = par1World.getBlockId(par3 - 1, par4 - 1, par5 + 1);
				var87 = par1World.getBlockId(par3 + 1, par4 - 1, par5 + 1);
				var88 = par1World.getBlockId(par3 - 2, par4 - 1, par5);
				var89 = par1World.getBlockId(par3 + 2, par4 - 1, par5);
				var90 = par1World.getBlockId(par3, par4 - 1, par5 - 2);
				var91 = par1World.getBlockId(par3, par4 - 1, par5 + 2);

				if (var81 != Block.grass.blockID)
					return false;

				if (var82 != Block.grass.blockID)
					return false;

				if (var83 != Block.grass.blockID)
					return false;

				if (var84 != Block.grass.blockID)
					return false;

				if (var85 != Block.grass.blockID)
					return false;

				if (var86 != Block.grass.blockID)
					return false;

				if (var87 != Block.grass.blockID)
					return false;

				if (var88 != Block.grass.blockID)
					return false;

				if (var89 != Block.grass.blockID)
					return false;

				if (var90 != Block.grass.blockID)
					return false;

				if (var91 != Block.grass.blockID)
					return false;

				if ((var8 == Block.grass.blockID || var8 == Block.dirt.blockID) && par4 < 256 - var6 - 1)
				{
					this.setBlock(par1World, par3, par4 - 1, par5, Block.dirt.blockID);
					var9 = 9;
					byte var18 = 0;
					int var13;
					int var14;
					int var15;

					for (var11 = par4 - var9 + var6; var11 <= par4 + var6; ++var11)
					{
						var12 = var11 - (par4 + var6);
						var13 = var18 + 1 - var12 / 4;

						for (var14 = par3 - var13; var14 <= par3 + var13; ++var14)
						{
							var15 = var14 - par3;

							for (int var16 = par5 - var13; var16 <= par5 + var13; ++var16)
							{
								int var17 = var16 - par5;

								if ((Math.abs(var15) != var13 || Math.abs(var17) != var13 || par2Random.nextInt(2) != 0 && var12 != 0) && !Block.opaqueCubeLookup[par1World.getBlockId(var14, var11, var16)])
								{
									this.setBlockAndMetadata(par1World, var14, var11 + 10, var16, leavesID, leavesMeta);
									this.setBlockAndMetadata(par1World, var14, var11 + 6, var16, leavesID, leavesMeta);
									this.setBlockAndMetadata(par1World, var14, var11, var16, leavesID, leavesMeta);
								}
							}
						}
					}

					for (var11 = 0; var11 < var6; ++var11)
					{
						var12 = par1World.getBlockId(par3, par4 + var11, par5);

						if (var12 == 0 || var12 == leavesID)
						{
							//1
							this.setBlockAndMetadata(par1World, par3, par4 + (var6), par5, logID, metaWood);
							this.setBlockAndMetadata(par1World, par3, par4 + (var6 + 1), par5, logID, metaWood);
							this.setBlockAndMetadata(par1World, par3, par4 + (var6 + 2), par5, logID, metaWood);
							this.setBlockAndMetadata(par1World, par3, par4 + (var6 + 3), par5, logID, metaWood);
							this.setBlockAndMetadata(par1World, par3, par4 + (var6 + 4), par5, logID, metaWood);
							this.setBlockAndMetadata(par1World, par3, par4 + (var6 + 5), par5, logID, metaWood);
							this.setBlockAndMetadata(par1World, par3, par4 + var11, par5, logID, metaWood);
							this.setBlockAndMetadata(par1World, par3 - 1, par4 + var11, par5, logID, metaWood);
							this.setBlockAndMetadata(par1World, par3 + 1, par4 + var11, par5, logID, metaWood);
							this.setBlockAndMetadata(par1World, par3, par4 + var11, par5 - 1, logID, metaWood);
							this.setBlockAndMetadata(par1World, par3, par4 + var11, par5 + 1, logID, metaWood);

							//2
							this.setBlockAndMetadata(par1World, par3 - 1, par4 + (var11 / 2), par5 - 1, logID, metaWood);
							this.setBlockAndMetadata(par1World, par3 + 1, par4 + (var11 / 2), par5 - 1, logID, metaWood);
							this.setBlockAndMetadata(par1World, par3 - 1, par4 + (var11 / 2), par5 + 1, logID, metaWood);
							this.setBlockAndMetadata(par1World, par3 + 1, par4 + (var11 / 2), par5 + 1, logID, metaWood);

							//3
							this.setBlockAndMetadata(par1World, par3 - 2, par4 + (var11 / 4), par5, logID, metaWood);
							this.setBlockAndMetadata(par1World, par3 - 2, par4 + (var11 / 4), par5 - 1, logID, metaWood);
							this.setBlockAndMetadata(par1World, par3 - 2, par4 + (var11 / 4), par5 + 1, logID, metaWood);
							this.setBlockAndMetadata(par1World, par3 + 2, par4 + (var11 / 4), par5, logID, metaWood);
							this.setBlockAndMetadata(par1World, par3 + 2, par4 + (var11 / 4), par5 - 1, logID, metaWood);
							this.setBlockAndMetadata(par1World, par3 + 2, par4 + (var11 / 4), par5 + 1, logID, metaWood);
							this.setBlockAndMetadata(par1World, par3, par4 + (var11 / 4), par5 - 2, logID, metaWood);
							this.setBlockAndMetadata(par1World, par3 - 1, par4 + (var11 / 4), par5 - 2, logID, metaWood);
							this.setBlockAndMetadata(par1World, par3 + 1, par4 + (var11 / 4), par5 - 2, logID, metaWood);
							this.setBlockAndMetadata(par1World, par3, par4 + (var11 / 4), par5 + 2, logID, metaWood);
							this.setBlockAndMetadata(par1World, par3 - 1, par4 + (var11 / 4), par5 + 2, logID, metaWood);
							this.setBlockAndMetadata(par1World, par3 + 1, par4 + (var11 / 4), par5 + 2, logID, metaWood);

							//4
							this.setBlockAndMetadata(par1World, par3 - 2, par4 + (var11 / 6), par5 - 2, logID, metaWood);
							this.setBlockAndMetadata(par1World, par3 + 2, par4 + (var11 / 6), par5 + 2, logID, metaWood);
							this.setBlockAndMetadata(par1World, par3 + 2, par4 + (var11 / 6), par5 - 2, logID, metaWood);
							this.setBlockAndMetadata(par1World, par3 - 2, par4 + (var11 / 6), par5 + 2, logID, metaWood);
							this.setBlockAndMetadata(par1World, par3 - 3, par4 + (var11 / 6), par5, logID, metaWood);
							this.setBlockAndMetadata(par1World, par3 + 3, par4 + (var11 / 6), par5, logID, metaWood);
							this.setBlockAndMetadata(par1World, par3, par4 + (var11 / 6), par5 - 3, logID, metaWood);
							this.setBlockAndMetadata(par1World, par3, par4 + (var11 / 6), par5 + 3, logID, metaWood);

							//5
							this.setBlockAndMetadata(par1World, par3 - 3, par4 + (var11 / 8), par5 - 1, logID, metaWood);
							this.setBlockAndMetadata(par1World, par3 - 3, par4 + (var11 / 8), par5 + 1, logID, metaWood);
							this.setBlockAndMetadata(par1World, par3 + 3, par4 + (var11 / 8), par5 - 1, logID, metaWood);
							this.setBlockAndMetadata(par1World, par3 + 3, par4 + (var11 / 8), par5 + 1, logID, metaWood);
							this.setBlockAndMetadata(par1World, par3 - 1, par4 + (var11 / 8), par5 - 3, logID, metaWood);
							this.setBlockAndMetadata(par1World, par3 + 1, par4 + (var11 / 8), par5 - 3, logID, metaWood);
							this.setBlockAndMetadata(par1World, par3 - 1, par4 + (var11 / 8), par5 + 3, logID, metaWood);
							this.setBlockAndMetadata(par1World, par3 + 1, par4 + (var11 / 8), par5 + 3, logID, metaWood);


							if (vinesGrow && var11 > 0)
							{
								if (par2Random.nextInt(3) > 0 && par1World.isAirBlock(par3 - 1, par4 + var11, par5))
								{
									this.setBlockAndMetadata(par1World, par3 - 1, par4 + var11, par5, Block.vine.blockID, 8);
								}

								if (par2Random.nextInt(3) > 0 && par1World.isAirBlock(par3 + 1, par4 + var11, par5))
								{
									this.setBlockAndMetadata(par1World, par3 + 1, par4 + var11, par5, Block.vine.blockID, 2);
								}

								if (par2Random.nextInt(3) > 0 && par1World.isAirBlock(par3, par4 + var11, par5 - 1))
								{
									this.setBlockAndMetadata(par1World, par3, par4 + var11, par5 - 1, Block.vine.blockID, 1);
								}

								if (par2Random.nextInt(3) > 0 && par1World.isAirBlock(par3, par4 + var11, par5 + 1))
								{
									this.setBlockAndMetadata(par1World, par3, par4 + var11, par5 + 1, Block.vine.blockID, 4);
								}
							}
						}
					}

					if (vinesGrow)
					{
						for (var11 = par4 - 3 + var6; var11 <= par4 + var6; ++var11)
						{
							var12 = var11 - (par4 + var6);
							var13 = 2 - var12 / 2;

							for (var14 = par3 - var13; var14 <= par3 + var13; ++var14)
							{
								for (var15 = par5 - var13; var15 <= par5 + var13; ++var15)
								{
									if (par1World.getBlockId(var14, var11, var15) == Block.leaves.blockID)
									{
										if (par2Random.nextInt(4) == 0 && par1World.getBlockId(var14 - 1, var11, var15) == 0)
										{
											this.growVines(par1World, var14 - 1, var11, var15, 8);
										}

										if (par2Random.nextInt(4) == 0 && par1World.getBlockId(var14 + 1, var11, var15) == 0)
										{
											this.growVines(par1World, var14 + 1, var11, var15, 2);
										}

										if (par2Random.nextInt(4) == 0 && par1World.getBlockId(var14, var11, var15 - 1) == 0)
										{
											this.growVines(par1World, var14, var11, var15 - 1, 1);
										}

										if (par2Random.nextInt(4) == 0 && par1World.getBlockId(var14, var11, var15 + 1) == 0)
										{
											this.growVines(par1World, var14, var11, var15 + 1, 4);
										}
									}
								}
							}
						}

						if (par2Random.nextInt(5) == 0 && var6 > 5)
						{
							for (var11 = 0; var11 < 2; ++var11)
							{
								for (var12 = 0; var12 < 4; ++var12)
								{
									if (par2Random.nextInt(4 - var11) == 0)
									{
										var13 = par2Random.nextInt(3);
										this.setBlockAndMetadata(par1World, par3 + Direction.offsetX[Direction.rotateOpposite[var12]], par4 + var6 - 5 + var11, par5 + Direction.offsetZ[Direction.rotateOpposite[var12]], Block.cocoaPlant.blockID, var13 << 2 | var12);
									}
								}
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

	/**
	 * Grows vines downward from the given block for a given length. Args: World, x, starty, z, vine-length
	 */
	private void growVines(World par1World, int par2, int par3, int par4, int par5)
	{
		this.setBlockAndMetadata(par1World, par2, par3, par4, Block.vine.blockID, par5);
		int var6 = 4;

		while (true)
		{
			--par3;

			if (par1World.getBlockId(par2, par3, par4) != 0 || var6 <= 0)
				return;

			this.setBlockAndMetadata(par1World, par2, par3, par4, Block.vine.blockID, par5);
			--var6;
		}
	}
}
