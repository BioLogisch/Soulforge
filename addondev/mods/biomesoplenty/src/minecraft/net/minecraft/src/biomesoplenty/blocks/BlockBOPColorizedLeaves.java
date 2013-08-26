package net.minecraft.src.biomesoplenty.blocks;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.minecraft.src.Block;
import net.minecraft.src.BlockLeavesBase;
import net.minecraft.src.ColorizerFoliage;
import net.minecraft.src.CreativeTabs;
import net.minecraft.src.IBlockAccess;
import net.minecraft.src.Icon;
import net.minecraft.src.IconRegister;
import net.minecraft.src.ItemStack;
import net.minecraft.src.Material;
import net.minecraft.src.World;
import net.minecraft.src.biomesoplenty.BiomesOPlenty;
import net.minecraft.src.biomesoplenty.api.BOPAPIBlocks;
import soulforge.utils.IShearable;

public class BlockBOPColorizedLeaves extends BlockLeavesBase implements IShearable
{
        private static final String[] leaves = new String[] {"acacia", "mangrove", "palm", "redwood", "willow", "pine"};
        private Icon[][] textures;
        int[] adjacentTreeBlocks;

        public BlockBOPColorizedLeaves(int blockID)
        {
                super(blockID, Material.leaves, false);
                setBurnProperties(this.blockID, 30, 60);
                this.setTickRandomly(true);
                setHardness(0.2F);
                setLightOpacity(1);
                setStepSound(Block.soundGrassFootstep);
                this.setCreativeTab(BiomesOPlenty.tabBiomesOPlenty);
        }
        
        public float GetMovementModifier(World var1, int var2, int var3, int var4)
        {
            return 0.5F;
        }

        public boolean CanSnowOnBlock(World var1, int var2, int var3, int var4)
        {
            return true;
        }

        @Override
        public void registerIcons(IconRegister iconRegister)
        {
        	textures = new Icon[3][leaves.length];

        	for (int i = 0; i < leaves.length; ++i)
        	{
        		textures[0][i] = iconRegister.registerIcon("biomesoplenty:leaves_" + leaves[i] + "_fancy");
        		textures[1][i] = iconRegister.registerIcon("biomesoplenty:leaves_" + leaves[i] + "_fast");
        	}
        }
                
        public Icon getIconBetterLeaves(int metadata, float randomIndex)
        {
          return textures[2][getTypeFromMeta(metadata)];
        }

        public Icon getIconFallingLeaves(int metadata)
        {
                return textures[1][getTypeFromMeta(metadata)];
        }


        @Override
        public int getBlockColor()
        {
                double temperature = 0.5D;
                double humidity = 1.0D;
                return ColorizerFoliage.getFoliageColor(temperature, humidity);
        }

        @Override
        public int getRenderColor(int par1)
        {
                switch (par1)
                {
                case 0:
                        return ColorizerFoliage.getFoliageColorBirch();

                case 3:
                        return ColorizerFoliage.getFoliageColorPine();

                default:
                        return ColorizerFoliage.getFoliageColorBasic();
                }
        }

        @Override
        public int colorMultiplier(IBlockAccess par1IBlockAccess, int par2, int par3, int par4)
        {
                int var6 = 0;
                int var7 = 0;
                int var8 = 0;

                for (int var9 = -1; var9 <= 1; ++var9)
                {
                        for (int var10 = -1; var10 <= 1; ++var10)
                        {
                                int var11 = par1IBlockAccess.getBiomeGenForCoords(par2 + var10, par4 + var9).getBiomeFoliageColor();
                                var6 += (var11 & 16711680) >> 16;
                        var7 += (var11 & 65280) >> 8;
                var8 += var11 & 255;
                        }
                }

                return (var6 / 9 & 255) << 16 | (var7 / 9 & 255) << 8 | var8 / 9 & 255;
        }

        @Override
        public Icon getIcon(int side, int meta)
        {
                return textures[(!isOpaqueCube() ? 0 : 1)][getTypeFromMeta(meta)];
        }

        @Override
        public boolean isOpaqueCube()
        {
                return Block.leaves.isOpaqueCube();
        }

        @Override
        @SuppressWarnings({ "rawtypes", "unchecked" })
        public void getSubBlocks(int blockID, CreativeTabs creativeTabs, List list) {
                for (int i = 0; i < textures[0].length; ++i) {
                        list.add(new ItemStack(blockID, 1, i));
                }
        }

        @Override
        public void randomDisplayTick(World world, int x, int y, int z, Random random)
        {
                if (world.canLightningStrikeAt(x, y + 1, z) && !world.doesBlockHaveSolidTopSurface(x, y - 1, z) && random.nextInt(15) == 1)
                {
                        double d0 = x + random.nextFloat();
                        double d1 = y - 0.05D;
                        double d2 = z + random.nextFloat();
                        world.spawnParticle("dripWater", d0, d1, d2, 0.0D, 0.0D, 0.0D);
                }

                super.randomDisplayTick(world, x, y, z, random);

        }

        @Override
        public void breakBlock(World world, int x, int y, int z, int par5, int par6)
        {
                byte radius = 1;
                int bounds = radius + 1;

                if (world.checkChunksExist(x - bounds, y - bounds, z - bounds, x + bounds, y + bounds, z + bounds)) {
                        for (int i = -radius; i <= radius; ++i) {
                                for (int j = -radius; j <= radius; ++j) {
                                        for (int k = -radius; k <= radius; ++k)
                                        {
                                                int blockID = world.getBlockId(x + i, y + j, z + k);

                                                if (Block.blocksList[blockID] != null) {
                                                        Block.blocksList[blockID].beginLeavesDecay(world, x + i, y + j, z + k);
                                                }
                                        }
                                }
                        }
                }
        }

        @Override
        public void updateTick(World world, int x, int y, int z, Random random)
        {
                if (world.isRemote)
                        return;

                int meta = world.getBlockMetadata(x, y, z);

                if ((meta & 8) != 0/* && (meta & 4) == 0*/)
                {
                        byte b0 = 4;
                        int i1 = b0 + 1;
                        byte b1 = 32;
                        int j1 = b1 * b1;
                        int k1 = b1 / 2;

                        if (adjacentTreeBlocks == null)
                        {
                                adjacentTreeBlocks = new int[b1 * b1 * b1];
                        }

                        int l1;

                        if (world.checkChunksExist(x - i1, y - i1, z - i1, x + i1, y + i1, z + i1))
                        {
                                int i2;
                                int j2;
                                int k2;

                                for (l1 = -b0; l1 <= b0; ++l1)
                                {
                                        for (i2 = -b0; i2 <= b0; ++i2)
                                        {
                                                for (j2 = -b0; j2 <= b0; ++j2)
                                                {
                                                        k2 = world.getBlockId(x + l1, y + i2, z + j2);

                                                        Block block = Block.blocksList[k2];

                                                        if (block != null && block.canSustainLeaves(world, x + l1, y + i2, z + j2))
                                                        {
                                                                adjacentTreeBlocks[(l1 + k1) * j1 + (i2 + k1) * b1 + j2 + k1] = 0;
                                                        }
                                                        else if (block != null && block.isLeaves(world, x + l1, y + i2, z + j2))
                                                        {
                                                                adjacentTreeBlocks[(l1 + k1) * j1 + (i2 + k1) * b1 + j2 + k1] = -2;
                                                        }
                                                        else
                                                        {
                                                                adjacentTreeBlocks[(l1 + k1) * j1 + (i2 + k1) * b1 + j2 + k1] = -1;
                                                        }
                                                }
                                        }
                                }

                                for (l1 = 1; l1 <= 4; ++l1)
                                {
                                        for (i2 = -b0; i2 <= b0; ++i2)
                                        {
                                                for (j2 = -b0; j2 <= b0; ++j2)
                                                {
                                                        for (k2 = -b0; k2 <= b0; ++k2)
                                                        {
                                                                if (adjacentTreeBlocks[(i2 + k1) * j1 + (j2 + k1) * b1 + k2 + k1] == l1 - 1)
                                                                {
                                                                        if (adjacentTreeBlocks[(i2 + k1 - 1) * j1 + (j2 + k1) * b1 + k2 + k1] == -2)
                                                                        {
                                                                                adjacentTreeBlocks[(i2 + k1 - 1) * j1 + (j2 + k1) * b1 + k2 + k1] = l1;
                                                                        }

                                                                        if (adjacentTreeBlocks[(i2 + k1 + 1) * j1 + (j2 + k1) * b1 + k2 + k1] == -2)
                                                                        {
                                                                                adjacentTreeBlocks[(i2 + k1 + 1) * j1 + (j2 + k1) * b1 + k2 + k1] = l1;
                                                                        }

                                                                        if (adjacentTreeBlocks[(i2 + k1) * j1 + (j2 + k1 - 1) * b1 + k2 + k1] == -2)
                                                                        {
                                                                                adjacentTreeBlocks[(i2 + k1) * j1 + (j2 + k1 - 1) * b1 + k2 + k1] = l1;
                                                                        }

                                                                        if (adjacentTreeBlocks[(i2 + k1) * j1 + (j2 + k1 + 1) * b1 + k2 + k1] == -2)
                                                                        {
                                                                                adjacentTreeBlocks[(i2 + k1) * j1 + (j2 + k1 + 1) * b1 + k2 + k1] = l1;
                                                                        }

                                                                        if (adjacentTreeBlocks[(i2 + k1) * j1 + (j2 + k1) * b1 + (k2 + k1 - 1)] == -2)
                                                                        {
                                                                                adjacentTreeBlocks[(i2 + k1) * j1 + (j2 + k1) * b1 + (k2 + k1 - 1)] = l1;
                                                                        }

                                                                        if (adjacentTreeBlocks[(i2 + k1) * j1 + (j2 + k1) * b1 + k2 + k1 + 1] == -2)
                                                                        {
                                                                                adjacentTreeBlocks[(i2 + k1) * j1 + (j2 + k1) * b1 + k2 + k1 + 1] = l1;
                                                                        }
                                                                }
                                                        }
                                                }
                                        }
                                }
                        }

                        l1 = adjacentTreeBlocks[k1 * j1 + k1 * b1 + k1];

                        if (l1 >= 0)
                        {
                                world.setBlockMetadataWithNotify(x, y, z, meta & -9, 4);
                        }
                        else
                        {
                                this.removeLeaves(world, x, y, z);
                        }
                }
        }

        private void removeLeaves(World world, int x, int y, int z)
        {
                this.dropBlockAsItem(world, x, y, z, world.getBlockMetadata(x, y, z), 0);
                world.setBlockToAir(x, y, z);
        }

        @Override
        public int idDropped(int par1, Random par2Random, int par3)
        {
        	return BOPAPIBlocks.saplingsColourized.blockID;
        }

        @Override
        public int damageDropped(int meta)
        {
                return getTypeFromMeta(meta);
        }

        @Override
        public int quantityDropped(Random random)
        {
                return random.nextInt(20) == 0 ? 1 : 0;
        }

        @Override
        public boolean isShearable(ItemStack item, World world, int x, int y, int z)
        {
                return true;
        }

        @Override
        public ArrayList<ItemStack> onSheared(ItemStack item, World world, int x, int y, int z, int fortune)
        {
                ArrayList<ItemStack> ret = new ArrayList<ItemStack>();
                ret.add(new ItemStack(this, 1, getTypeFromMeta(world.getBlockMetadata(x, y, z))));
                return ret;
        }

        public String getLeafType(int meta)
        {
                return leaves[getTypeFromMeta(meta)];
        }

        private static int getTypeFromMeta(int meta)
        {
                meta = meta & 7;
                if (meta < 0 || meta >= leaves.length) {
                        meta = 0;
                }
                return meta;
        }

        public void setGraphicsLevel(boolean par1)
        {
                graphicsLevel = par1;
        }

        @Override
        public boolean shouldSideBeRendered(IBlockAccess par1IBlockAccess, int par2, int par3, int par4, int par5)
        {
                return true;
        }

        @Override
        public void beginLeavesDecay(World world, int x, int y, int z)
        {
                world.setBlockMetadataWithNotify(x, y, z, world.getBlockMetadata(x, y, z) | 8, 4);
        }

        @Override
        public boolean isLeaves(World world, int x, int y, int z)
        {
                return true;
        }
}