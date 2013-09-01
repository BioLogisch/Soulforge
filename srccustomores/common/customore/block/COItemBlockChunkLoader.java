package customore.block;

import customore.entity.COChunkLoaderTileEntity;
import biomesoplenty.blocks.BlockBOPLeaves;
import net.minecraft.src.Block;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.ItemBlock;
import net.minecraft.src.ItemStack;
import net.minecraft.src.World;

public class COItemBlockChunkLoader extends ItemBlock {

	public COItemBlockChunkLoader(int par1) {
		super(par1 - 256);
		setMaxDamage(0);
		setHasSubtypes(false);
	}
	
	@Override
	public int getMetadata(int meta)
	{
		return meta;
	}

	@Override
	public String getUnlocalizedName(ItemStack itemStack)
	{
		return "co.block.item.chunkloader";
	}

}
