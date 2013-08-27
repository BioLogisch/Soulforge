package soulforge.server;

import soulforge.utils.CommonHandler;
import soulforge.utils.ISidedHandler;
import soulforge.utils.Side;
import net.minecraft.server.MinecraftServer;
import net.minecraft.src.FCAddOnHandler;

public class ServerHandler implements ISidedHandler
{
    /**
     * The singleton
     */
    private static final ServerHandler INSTANCE = new ServerHandler();

    /**
     * A reference to the server itself
     */
    private MinecraftServer server;

    private ServerHandler()
    {
    	FCAddOnHandler.LogMessage("Create ServerHandler");
        CommonHandler.instance().beginLoading(this);
    }
    /**
     * Called to start the whole game off from
     * {@link MinecraftServer#startServer}
     *
     * @param minecraftServer
     */
    @Override
    public void beginServerLoading(MinecraftServer minecraftServer)
    {
    	FCAddOnHandler.LogMessage("beginServerLoading");
        server = minecraftServer;
    }
    
    @Override
	public void finishServerLoading() {
		
	}

    /**
     * Get the server instance
     */
    public MinecraftServer getServer()
    {
        return server;
    }

    /**
     * @return the instance
     */
    public static ServerHandler instance()
    {
        return INSTANCE;
    }

    @Override
    public Side getSide()
    {
        return Side.SERVER;
    }
	

}
	
	
