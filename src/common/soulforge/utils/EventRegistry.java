package soulforge.utils;

import java.util.HashSet;

import net.minecraft.server.MinecraftServer;
import net.minecraft.src.ChunkCoordIntPair;
import net.minecraft.src.DedicatedServer;
import net.minecraft.src.EntityLiving;
import net.minecraft.src.FCAddOnHandler;
import net.minecraft.src.World;

public class EventRegistry {

	private static HashSet<IEventHandler> eventhandlers = new HashSet<IEventHandler>();
	
	public static void registerEventHandler(IEventHandler handler)
	{
		eventhandlers.add(handler);
	}

	public static void serverStarting(MinecraftServer server) {
		for (IEventHandler handler : eventhandlers)
		{
			handler.onServerStarting(server);
		}
	}

	public static void serverStarted() {
		for (IEventHandler handler : eventhandlers)
		{
			handler.serverStarted();
		}
	}

	public static void serverStopping() {
		for (IEventHandler handler : eventhandlers)
		{
			handler.serverStopping();
		}
	}

	public static void serverStopped() {
		for (IEventHandler handler : eventhandlers)
		{
			handler.serverStopped();
		}
		
	}

	public static void serverAboutToStart(MinecraftServer server) {
		for (IEventHandler handler : eventhandlers)
		{
			handler.serverAboutToStart(server);
		}
	}
	
	
	public static Boolean shouldUnloadChunk(World world, ChunkCoordIntPair chunkCoords) {
		Boolean result = true;
		for (IEventHandler handler : eventhandlers)
		{
			result = handler.shouldUnloadChunk(world, chunkCoords);
			if (!result) {
				break;
			}
		}
		return result;
	}
	
	public static Boolean hasLoadedChunksForDimension(int dimId) {
		Boolean result = false;
		for (IEventHandler handler : eventhandlers)
		{
			result = handler.hasLoadedChunksForDimension(dimId);
			if (result) {
				break;
			}
		}
		return result;
	}
	
	public static HashSet<ChunkCoordIntPair> loadedChunksForDimension(int dimId) {
		HashSet<ChunkCoordIntPair> result = new HashSet<ChunkCoordIntPair>();
		for (IEventHandler handler : eventhandlers)
		{
			result.addAll(handler.loadedChunksForDimension(dimId));
		}
		return result;
	}
	
	public static HashSet<ChunkCoordIntPair> spawnableChunksForDimension(int dimId) {
		HashSet<ChunkCoordIntPair> result = new HashSet<ChunkCoordIntPair>();
		for (IEventHandler handler : eventhandlers)
		{
			result.addAll(handler.spawnableChunksForDimension(dimId));
		}
		return result;
	}
	
	public static Boolean shouldDespawnEntityLiving(EntityLiving entity) {
		Boolean result = true;
		for (IEventHandler handler : eventhandlers)
		{
			result = handler.shouldDespawnEntityLiving(entity);
			if (!result) {
				break;
			}
		}
		return result;
	}
	
}
