package soulforge.utils;

import java.util.EnumSet;
import java.util.List;

import net.minecraft.src.FCAddOnHandler;

import com.google.common.base.Objects;
import com.google.common.collect.Lists;

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
    
    public void beginLoading(ISidedHandler handler)
    {
        sidedDelegate = handler;
       
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
