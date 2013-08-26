package soulforge.utils;

import net.minecraft.client.Minecraft;
import net.minecraft.server.MinecraftServer;

public class ClientHandler implements ISidedHandler {

	
	  /**
     * The singleton
     */
    private static final ClientHandler INSTANCE = new ClientHandler();
    
	 /**
     * A reference to the server itself
     */
    private Minecraft client;

    private boolean loading;

    /**
     * @return the instance
     */
    public static ClientHandler instance()
    {
        return INSTANCE;
    }

    /**
     * Called to start the whole game off
     *
     * @param minecraft The minecraft instance being launched
     */
    public void beginMinecraftLoading(Minecraft minecraft)
    {
        client = minecraft;
        loading = true;
        CommonHandler.instance().beginLoading(this);
    }
    
    public void finishMinecraftLoading()
    {
        loading = false;
    }
    
    @Override
    public Side getSide()
    {
        return Side.CLIENT;
    }

	@Override
	public void beginServerLoading(MinecraftServer server) {
		// NOOP		
	}

	@Override
	public void finishServerLoading() {
		// NOOP		
	}

	@Override
	public MinecraftServer getServer() {
		return client.getIntegratedServer();
	}
	
    
  
}
