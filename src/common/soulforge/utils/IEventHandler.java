package soulforge.utils;

import java.util.Random;

import net.minecraft.server.MinecraftServer;
import net.minecraft.src.World;

public interface IEventHandler {

	public void onClientTickEnd(boolean paused);

	public void onClientTickStart(boolean paused);

	public void onServerWillStart(MinecraftServer server);
	
	public void onServerDidStart(MinecraftServer server);
	
	public void onServerTickStart();
	
	public void onServerTickEnd();

	public void onWorldsDidLoad();
	
	public void onPostProcessChunk(World world, Random rand, int x, int z);

	
}
