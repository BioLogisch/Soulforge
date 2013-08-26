package soulforge.utils;

import net.minecraft.server.MinecraftServer;

public interface ISidedHandler {

    public Side getSide();

    public void beginServerLoading(MinecraftServer server);

    public void finishServerLoading();

    public MinecraftServer getServer();

}
