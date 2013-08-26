package soulforge.utils;

import java.util.HashSet;

import net.minecraft.server.MinecraftServer;
import net.minecraft.src.DedicatedServer;

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
	
}
