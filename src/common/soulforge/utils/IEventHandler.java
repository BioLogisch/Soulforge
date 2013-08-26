package soulforge.utils;

import net.minecraft.server.MinecraftServer;
import net.minecraft.src.DedicatedServer;

public interface IEventHandler {

	public void onServerStarting(MinecraftServer server);

	public void serverStarted();

	public void serverStopping();

	public void serverStopped();

	public void serverAboutToStart(MinecraftServer server);

}
