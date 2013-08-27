package soulforge.utils;

import java.util.EnumSet;
import java.util.List;

import net.minecraft.server.MinecraftServer;
import net.minecraft.src.DedicatedServer;
import net.minecraft.src.FCAddOnHandler;

import com.google.common.base.Objects;
import com.google.common.collect.Lists;

import soulforge.server.ServerHandler;

public class CommonHandler {
	  /**
     * The singleton
     */
    private static final CommonHandler INSTANCE = new CommonHandler();
    
    private List<IScheduledTickHandler> scheduledClientTicks = Lists.newArrayList();
    private List<IScheduledTickHandler> scheduledServerTicks = Lists.newArrayList();
    
    /**
     * The delegate for side specific data and functions
     */
    private ISidedHandler sidedDelegate;
    
    /**
     * @return the instance
     */
    public static CommonHandler instance()
    {
        return INSTANCE;
    }
    
    public Side getSide()
    {
        return sidedDelegate.getSide();
    }

    
    public void beginLoading(ISidedHandler handler)
    {
        sidedDelegate = handler;
    }
    
    public void handleServerStarted()
    {
    	EventRegistry.serverStarted();
    }

    public void handleServerStopping()
    {
    	EventRegistry.serverStopping();
    }
    

	public void handleServerStopped() {
    	EventRegistry.serverStopped();
	}

    public void onServerStart(MinecraftServer server)
    {
        sidedDelegate.beginServerLoading(server);
    }

    public void onServerStarted()
    {
        sidedDelegate.finishServerLoading();
    }
    
    public boolean handleServerStarting(MinecraftServer server)
    {
    	EventRegistry.serverStarting(server);
        return true;
    }
    
    public boolean handleServerAboutToStart(MinecraftServer server) {
		EventRegistry.serverAboutToStart(server);
		return true;
	}

    public MinecraftServer getMinecraftServerInstance()
    {
        return sidedDelegate.getServer();
    }

    
    public void rescheduleTicks(Side side)
    {
        TickRegistry.updateTickQueue(side.isClient() ? scheduledClientTicks : scheduledServerTicks, side);
    }
    
    
    public void onPreServerTick()
    {
        tickStart(EnumSet.of(TickType.SERVER), Side.SERVER);
    }

    public void onPostServerTick()
    {
        tickEnd(EnumSet.of(TickType.SERVER), Side.SERVER);
    }
    
    public void onPreClientTick()
    {
    	tickStart(EnumSet.of(TickType.CLIENT), Side.CLIENT);
    }

    public void onPostClientTick()
    {
    	tickEnd(EnumSet.of(TickType.CLIENT), Side.CLIENT);
    }
    
    public void tickStart(EnumSet<TickType> ticks, Side side, Object ... data)
    {
        List<IScheduledTickHandler> scheduledTicks = side.isClient() ? scheduledClientTicks : scheduledServerTicks;

        if (scheduledTicks.size()==0)
        {
            return;
        }
        for (IScheduledTickHandler ticker : scheduledTicks)
        {
            EnumSet<TickType> ticksToRun = EnumSet.copyOf(Objects.firstNonNull(ticker.ticks(), EnumSet.noneOf(TickType.class)));
            ticksToRun.retainAll(ticks);
            if (!ticksToRun.isEmpty())
            {
                ticker.tickStart(ticksToRun, data);
            }
        }
    }

    public void tickEnd(EnumSet<TickType> ticks, Side side, Object ... data)
    {
        List<IScheduledTickHandler> scheduledTicks = side.isClient() ? scheduledClientTicks : scheduledServerTicks;

        if (scheduledTicks.size()==0)
        {
            return;
        }
        for (IScheduledTickHandler ticker : scheduledTicks)
        {
            EnumSet<TickType> ticksToRun = EnumSet.copyOf(Objects.firstNonNull(ticker.ticks(), EnumSet.noneOf(TickType.class)));
            ticksToRun.retainAll(ticks);
            if (!ticksToRun.isEmpty())
            {
                ticker.tickEnd(ticksToRun, data);
            }
        }
    }

	

    
    

  
}
