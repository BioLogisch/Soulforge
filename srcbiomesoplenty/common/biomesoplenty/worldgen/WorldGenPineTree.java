package biomesoplenty.worldgen;

import java.util.Random;

import net.minecraft.src.Block;
import net.minecraft.src.World;
import net.minecraft.src.WorldGenerator;
import biomesoplenty.api.BOPAPIBlocks;
import biomesoplenty.configuration.BOPConfiguration;

public class WorldGenPineTree extends WorldGenerator
{
	@Override
	public boolean generate(World var1, Random var2, int x, int y, int z)
	{
		while (var1.isAirBlock(x, y, z) && y > 2)
		{
			--y;
		}

		int var6 = var1.getBlockId(x, y, z);

		if (var6 != Block.grass.blockID /*&& var6 != Blocks.hardDirt.get().blockID*/ && var6 != Block.stone.blockID && var6 != Block.dirt.blockID)
			return false;
		else
		{
			for (int xoff = -2; xoff <= 2; ++xoff)
			{
				for (int zoff = -2; zoff <= 2; ++zoff)
				{
					if (var1.isAirBlock(x + xoff, y - 1, z + zoff) && var1.isAirBlock(x + xoff, y - 2, z + zoff) && !var1.isAirBlock(x + xoff, y, z + zoff))
						return false;
				}
			}

			int var99 = var2.nextInt(2);
			
			int leavesID;
			int logID;
			int leavesMeta;
			int logMeta;
			
			if (BOPConfiguration.mainConfigFile.getBoolean("enableCustomContent"))
			{
				logID = BOPAPIBlocks.logs4.blockID;
				leavesID = BOPAPIBlocks.leavesColourized.blockID;
				
				logMeta = 0;
				leavesMeta = 5;
			}
			else
			{
				logID = Block.wood.blockID;
				leavesID = Block.leaves.blockID;
				
				logMeta = 0;
				leavesMeta = 0;
			}

			if (var99 == 0)
			{
				var1.setBlock(x, y, z, Block.dirt.blockID);
				var1.setBlock(x, y + 1, z, logID, logMeta, 2);
				var1.setBlock(x, y + 2, z, logID, logMeta, 2);
				var1.setBlock(x, y + 3, z, logID, logMeta, 2);
				var1.setBlock(x, y + 4, z, logID, logMeta, 2);
				var1.setBlock(x, y + 5, z, logID, logMeta, 2);
				var1.setBlock(x, y + 6, z, logID, logMeta, 2);
				var1.setBlock(x, y + 7, z, logID, logMeta, 2);
				var1.setBlock(x, y + 8, z, logID, logMeta, 2);
				var1.setBlock(x, y + 9, z, logID, logMeta, 2);
				var1.setBlock(x, y + 10, z, logID, logMeta, 2);

				var1.setBlock(x + 1, y + 6, z, leavesID, leavesMeta, 2);
				var1.setBlock(x - 1, y + 6, z, leavesID, leavesMeta, 2);
				var1.setBlock(x, y + 6, z + 1, leavesID, leavesMeta, 2);
				var1.setBlock(x, y + 6, z - 1, leavesID, leavesMeta, 2);
				var1.setBlock(x + 1, y + 6, z + 1, leavesID, leavesMeta, 2);
				var1.setBlock(x + 1, y + 6, z - 1, leavesID, leavesMeta, 2);
				var1.setBlock(x - 1, y + 6, z + 1, leavesID, leavesMeta, 2);
				var1.setBlock(x - 1, y + 6, z - 1, leavesID, leavesMeta, 2);
				var1.setBlock(x - 1, y + 6, z - 2, leavesID, leavesMeta, 2);
				var1.setBlock(x - 2, y + 6, z - 1, leavesID, leavesMeta, 2);
				var1.setBlock(x - 2, y + 6, z - 2, leavesID, leavesMeta, 2);
				var1.setBlock(x + 1, y + 6, z + 2, leavesID, leavesMeta, 2);
				var1.setBlock(x + 2, y + 6, z + 1, leavesID, leavesMeta, 2);
				var1.setBlock(x + 2, y + 6, z + 2, leavesID, leavesMeta, 2);

				var1.setBlock(x + 1, y + 8, z, leavesID, leavesMeta, 2);
				var1.setBlock(x - 1, y + 8, z, leavesID, leavesMeta, 2);
				var1.setBlock(x, y + 8, z + 1, leavesID, leavesMeta, 2);
				var1.setBlock(x, y + 8, z - 1, leavesID, leavesMeta, 2);
				var1.setBlock(x + 1, y + 8, z + 1, leavesID, leavesMeta, 2);
				var1.setBlock(x + 1, y + 8, z - 1, leavesID, leavesMeta, 2);
				var1.setBlock(x - 1, y + 8, z + 1, leavesID, leavesMeta, 2);
				var1.setBlock(x - 1, y + 8, z - 1, leavesID, leavesMeta, 2);
				var1.setBlock(x + 1, y + 8, z - 2, leavesID, leavesMeta, 2);
				var1.setBlock(x + 2, y + 8, z - 1, leavesID, leavesMeta, 2);
				var1.setBlock(x + 2, y + 8, z - 2, leavesID, leavesMeta, 2);
				var1.setBlock(x - 1, y + 8, z + 2, leavesID, leavesMeta, 2);
				var1.setBlock(x - 2, y + 8, z + 1, leavesID, leavesMeta, 2);
				var1.setBlock(x - 2, y + 8, z + 2, leavesID, leavesMeta, 2);

				var1.setBlock(x + 1, y + 10, z, leavesID, leavesMeta, 2);
				var1.setBlock(x - 1, y + 10, z, leavesID, leavesMeta, 2);
				var1.setBlock(x, y + 10, z + 1, leavesID, leavesMeta, 2);
				var1.setBlock(x, y + 10, z - 1, leavesID, leavesMeta, 2);
				var1.setBlock(x + 1, y + 10, z + 1, leavesID, leavesMeta, 2);
				var1.setBlock(x + 1, y + 10, z - 1, leavesID, leavesMeta, 2);
				var1.setBlock(x - 1, y + 10, z + 1, leavesID, leavesMeta, 2);
				var1.setBlock(x - 1, y + 10, z - 1, leavesID, leavesMeta, 2);

				var1.setBlock(x + 1, y + 11, z, leavesID, leavesMeta, 2);
				var1.setBlock(x - 1, y + 11, z, leavesID, leavesMeta, 2);
				var1.setBlock(x, y + 11, z + 1, leavesID, leavesMeta, 2);
				var1.setBlock(x, y + 11, z - 1, leavesID, leavesMeta, 2);

				var1.setBlock(x, y + 12, z, leavesID, leavesMeta, 2);
			}

			if (var99 == 1)
			{
				var1.setBlock(x, y, z, Block.dirt.blockID);
				var1.setBlock(x, y + 1, z, logID, logMeta, 2);
				var1.setBlock(x, y + 2, z, logID, logMeta, 2);
				var1.setBlock(x, y + 3, z, logID, logMeta, 2);
				var1.setBlock(x, y + 4, z, logID, logMeta, 2);
				var1.setBlock(x, y + 5, z, logID, logMeta, 2);
				var1.setBlock(x, y + 6, z, logID, logMeta, 2);
				var1.setBlock(x, y + 7, z, logID, logMeta, 2);
				var1.setBlock(x, y + 8, z, logID, logMeta, 2);
				var1.setBlock(x, y + 9, z, logID, logMeta, 2);
				var1.setBlock(x, y + 10, z, logID, logMeta, 2);

				var1.setBlock(x + 1, y + 6, z, leavesID, leavesMeta, 2);
				var1.setBlock(x - 1, y + 6, z, leavesID, leavesMeta, 2);
				var1.setBlock(x, y + 6, z + 1, leavesID, leavesMeta, 2);
				var1.setBlock(x, y + 6, z - 1, leavesID, leavesMeta, 2);
				var1.setBlock(x + 1, y + 6, z + 1, leavesID, leavesMeta, 2);
				var1.setBlock(x + 1, y + 6, z - 1, leavesID, leavesMeta, 2);
				var1.setBlock(x - 1, y + 6, z + 1, leavesID, leavesMeta, 2);
				var1.setBlock(x - 1, y + 6, z - 1, leavesID, leavesMeta, 2);

				var1.setBlock(x + 1, y + 6, z - 2, leavesID, leavesMeta, 2);
				var1.setBlock(x + 2, y + 6, z - 1, leavesID, leavesMeta, 2);
				var1.setBlock(x + 2, y + 6, z - 2, leavesID, leavesMeta, 2);
				var1.setBlock(x - 1, y + 6, z + 2, leavesID, leavesMeta, 2);
				var1.setBlock(x - 2, y + 6, z + 1, leavesID, leavesMeta, 2);
				var1.setBlock(x - 2, y + 6, z + 2, leavesID, leavesMeta, 2);

				var1.setBlock(x + 1, y + 8, z, leavesID, leavesMeta, 2);
				var1.setBlock(x - 1, y + 8, z, leavesID, leavesMeta, 2);
				var1.setBlock(x, y + 8, z + 1, leavesID, leavesMeta, 2);
				var1.setBlock(x, y + 8, z - 1, leavesID, leavesMeta, 2);
				var1.setBlock(x + 1, y + 8, z + 1, leavesID, leavesMeta, 2);
				var1.setBlock(x + 1, y + 8, z - 1, leavesID, leavesMeta, 2);
				var1.setBlock(x - 1, y + 8, z + 1, leavesID, leavesMeta, 2);
				var1.setBlock(x - 1, y + 8, z - 1, leavesID, leavesMeta, 2);

				var1.setBlock(x - 1, y + 8, z - 2, leavesID, leavesMeta, 2);
				var1.setBlock(x - 2, y + 8, z - 1, leavesID, leavesMeta, 2);
				var1.setBlock(x - 2, y + 8, z - 2, leavesID, leavesMeta, 2);
				var1.setBlock(x + 1, y + 8, z + 2, leavesID, leavesMeta, 2);
				var1.setBlock(x + 2, y + 8, z + 1, leavesID, leavesMeta, 2);
				var1.setBlock(x + 2, y + 8, z + 2, leavesID, leavesMeta, 2);

				var1.setBlock(x + 1, y + 10, z, leavesID, leavesMeta, 2);
				var1.setBlock(x - 1, y + 10, z, leavesID, leavesMeta, 2);
				var1.setBlock(x, y + 10, z + 1, leavesID, leavesMeta, 2);
				var1.setBlock(x, y + 10, z - 1, leavesID, leavesMeta, 2);
				var1.setBlock(x + 1, y + 10, z + 1, leavesID, leavesMeta, 2);
				var1.setBlock(x + 1, y + 10, z - 1, leavesID, leavesMeta, 2);
				var1.setBlock(x - 1, y + 10, z + 1, leavesID, leavesMeta, 2);
				var1.setBlock(x - 1, y + 10, z - 1, leavesID, leavesMeta, 2);

				var1.setBlock(x + 1, y + 11, z, leavesID, leavesMeta, 2);
				var1.setBlock(x - 1, y + 11, z, leavesID, leavesMeta, 2);
				var1.setBlock(x, y + 11, z + 1, leavesID, leavesMeta, 2);
				var1.setBlock(x, y + 11, z - 1, leavesID, leavesMeta, 2);

				var1.setBlock(x, y + 12, z, leavesID, leavesMeta, 2);
			}

			return true;
		}
	}
}
