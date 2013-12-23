package customore.entity;

import customore.COChunkManager;
import customore.util.COLogger;
import net.minecraft.server.MinecraftServer;
import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.TileEntity;
import net.minecraft.src.World;

public class COChunkLoaderTileEntity extends TileEntity {

	public int loaderId = 1;
	public Boolean activated = false;
	public Boolean spawnable = false;

	public COChunkLoaderTileEntity() {

	}
	
	public COChunkLoaderTileEntity(Boolean spawnable) {
		this.spawnable = spawnable;
	}
	
	public void toogleActivation() {
		this.activated = !this.activated;
		COLogger.log.fine("Toogled ChunkLoader: " +this+ " at " + this.getId() + " activated: " + this.activated.toString());
		COChunkManager.instance().updateChunkLoader(getId(), activated);
	}
	
	public void placedChunkLoader(World world, int x, int y, int z) {
		COLogger.log.fine("Placed ChunkLoader: " + this + " at " + getId());;
		COChunkManager.instance().addChunkLoader(getId(), activated);
	}
	
	public void destroyedChunkLoader(World world, int x, int y, int z) {
		COLogger.log.fine("Destroyed ChunkLoader: " +this+ " at " + this.getId());
		COChunkManager.instance().removeChunkLoader(getId());
	}
	
	public String getId(int x, int y, int z) {
		return this.worldObj.provider.dimensionId + ":" + x + ":" + y + ":" + z + ":" + spawnable; 
	}
	
	public String getId() {
		return getId(this.xCoord, this.yCoord, this.zCoord); 
	}
	
	@Override
	public void writeToNBT(NBTTagCompound par1)
	{
	   super.writeToNBT(par1);
	   par1.setInteger("loaderId", loaderId);
	   par1.setBoolean("activated", activated);
	   par1.setBoolean("spawnable", spawnable);

	}

	@Override
	public void readFromNBT(NBTTagCompound par1)
	{
	   super.readFromNBT(par1);
	   this.loaderId = par1.getInteger("loaderId");
	   this.activated = par1.getBoolean("activated");
	   this.spawnable = par1.getBoolean("spawnable");
	}

	
	
}
