package net.minecraft.src.biomesoplenty.blocks;

import java.util.Random;

import net.minecraft.src.Block;
import net.minecraft.src.BlockSapling;
import net.minecraft.src.Icon;
import net.minecraft.src.World;
import net.minecraft.src.WorldGenerator;
import net.minecraft.src.biomesoplenty.BiomesOPlenty;
import net.minecraft.src.biomesoplenty.worldgen.WorldGenAcacia;
import net.minecraft.src.biomesoplenty.worldgen.WorldGenPalmTree1;
import net.minecraft.src.biomesoplenty.worldgen.WorldGenPalmTree3;
import net.minecraft.src.biomesoplenty.worldgen.WorldGenPineTree;
import net.minecraft.src.biomesoplenty.worldgen.WorldGenRedwoodTree2;
import net.minecraft.src.biomesoplenty.worldgen.WorldGenWillow;

public class BlockBOPColorizedSapling extends BlockSapling
{
	private static final String[] saplings = new String[] {"acacia", "mangrove", "palm", "redwood", "willow", "pine"};
	private Icon[] textures;
	private static final int TYPES = 15;

	public BlockBOPColorizedSapling(int par1)
	{
		super(par1);
		setHardness(0.0F);
		setStepSound(Block.soundGrassFootstep);
		this.setCreativeTab(BiomesOPlenty.tabBiomesOPlenty);
	}

	@Override
	public void updateTick(World world, int x, int y, int z, Random random)
	{
		if (world.isRemote)
			return;

		this.checkFlowerChange(world, x, y, z);

		if (world.getBlockLightValue(x, y + 1, z) >= 9 && random.nextInt(7) == 0) {
			this.growTree(world, x, y, z, random);
		}
	}

	@Override
	public void growTree(World world, int x, int y, int z, Random random)
	{
		int meta = world.getBlockMetadata(x, y, z) & TYPES;
		Object obj = null;
		int rnd = random.nextInt(8);

		if (obj == null)
		{
			switch (meta)
			{
			case 0: // Acacia Tree
			obj = new WorldGenAcacia(false); //DONE
			break;

			/*case 1: // Mangrove Tree
			obj = new WorldGenMangrove(false);
			break;*/

			case 2: // Palm Tree
				rnd = random.nextInt(4);

				if (rnd == 0) {
					obj = new WorldGenPalmTree1(); //DONE
				} else {
					obj = new WorldGenPalmTree3(); //DONE
				}
				break;

			case 3: // Redwood Tree
				if (this.isSameSapling(world, x + 1, y, z, 3) && this.isSameSapling(world, x - 1, y, z, 3) && this.isSameSapling(world, x, y, z + 1, 3) && this.isSameSapling(world, x, y, z - 1, 3) && this.isSameSapling(world, x + 1, y, z + 1, 3) && this.isSameSapling(world, x + 1, y, z - 1, 3) && this.isSameSapling(world, x - 1, y, z + 1, 3) && this.isSameSapling(world, x - 1, y, z - 1, 3)) {
					obj = new WorldGenRedwoodTree2(false); //DONE
				}
				break;

			case 4: // Willow Tree
				obj = new WorldGenWillow(); //DONE
				break;

			case 5: // Pine Tree
				obj = new WorldGenPineTree(); //DONE
				break;
			}
		}

		if (obj != null)
		{
			world.setBlockToAir(x, y, z);

			if (!((WorldGenerator)obj).generate(world, random, x, y, z)) {
				world.setBlock(x, y, z, blockID, meta, 2);
			}
		}
	}

	@Override
	public int damageDropped(int meta)
	{
		return meta & TYPES;
	}

	@Override
	public int getDamageValue(World world, int x, int y, int z)
	{
		return world.getBlockMetadata(x, y, z) & TYPES;
	}
}