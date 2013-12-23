package soulforge.utils;

import java.util.Collection;
import java.util.HashSet;

import net.minecraft.server.MinecraftServer;
import net.minecraft.src.ChunkCoordIntPair;
import net.minecraft.src.DedicatedServer;
import net.minecraft.src.EntityLiving;
import net.minecraft.src.World;

public interface IEventHandler {

	public void onServerStarting(MinecraftServer server);

	public void serverStarted();

	public void serverStopping();

	public void serverStopped();

	public void serverAboutToStart(MinecraftServer server);

	public Boolean shouldUnloadChunk(World world, ChunkCoordIntPair chunkCoord);
	public Boolean hasLoadedChunksForDimension(int dimId);
	public HashSet<ChunkCoordIntPair> loadedChunksForDimension(int dimId);


	public Boolean shouldDespawnEntityLiving(EntityLiving entity);

	public HashSet<ChunkCoordIntPair> spawnableChunksForDimension(int dimId);

}
