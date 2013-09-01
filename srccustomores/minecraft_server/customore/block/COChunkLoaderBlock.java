package customore.block;

import net.minecraft.src.Block;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.ITileEntityProvider;
import net.minecraft.src.Icon;
import net.minecraft.src.Material;
import net.minecraft.src.TileEntity;
import net.minecraft.src.World;
import customore.entity.COChunkLoaderTileEntity;

public class COChunkLoaderBlock extends Block implements ITileEntityProvider {


	public COChunkLoaderBlock(int par1, Material par2Material) {
		super(par1, par2Material);
	}

	@Override
	public void breakBlock(World world, int x, int y, int z, int par5, int par6)
	{
		COChunkLoaderTileEntity chunkloader = (COChunkLoaderTileEntity)world.getBlockTileEntity(x, y, z);
		chunkloader.destroyedChunkLoader(world, x, y, z);
		world.removeBlockTileEntity(x, y, z);
	}

	@Override
	public void onPostBlockPlaced(World world, int x, int y, int z, int dontknow)
	{
		COChunkLoaderTileEntity chunkloader = (COChunkLoaderTileEntity)world.getBlockTileEntity(x, y, z);
		chunkloader.placedChunkLoader(world, x, y, z);
	}

	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer par5EntityPlayer, int par6, float par7, float par8, float par9)
	{
		if (!world.isRemote)
		{
			COChunkLoaderTileEntity chunkloader = (COChunkLoaderTileEntity)world.getBlockTileEntity(x, y, z);

			chunkloader.toogleActivation();
			if (chunkloader.activated) {
				world.setBlockMetadata(x, y, z, 1);
			} else {
				world.setBlockMetadata(x, y, z, 0);
			}
			world.notifyBlockChange(x, y, z, blockID);
		}
		return true;
	}

	@Override
	public TileEntity createNewTileEntity(World var1) {
		return new COChunkLoaderTileEntity();
	}

}
