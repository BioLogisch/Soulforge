package soulforge.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

import com.google.common.collect.Queues;

import net.minecraft.server.MinecraftServer;
import net.minecraft.src.World;
import net.minecraft.src.WorldServer;

public class TickRegistry {

	 /**
     * We register our delegate here
     * @param handler
     */

    public static class BOTickQueueElement implements Comparable<BOTickQueueElement>
    {
        public BOTickQueueElement(IScheduledTickHandler ticker, long tickCounter)
        {
            this.ticker = ticker;
            update(tickCounter);
        }
        @Override
        public int compareTo(BOTickQueueElement o)
        {
            return (int)(next - o.next);
        }

        public void update(long tickCounter)
        {
            next = tickCounter + Math.max(ticker.nextTickSpacing(),1);
        }

        private long next;
        public IScheduledTickHandler ticker;

        public boolean scheduledNow(long tickCounter)
        {
            return tickCounter >= next;
        }
    }

    private static PriorityQueue<BOTickQueueElement> clientTickHandlers = Queues.newPriorityQueue();
    private static PriorityQueue<BOTickQueueElement> serverTickHandlers = Queues.newPriorityQueue();

    private static AtomicLong clientTickCounter = new AtomicLong();
    private static AtomicLong serverTickCounter = new AtomicLong();

    public static void registerScheduledTickHandler(IScheduledTickHandler handler, Side side)
    {
        getQueue(side).add(new BOTickQueueElement(handler, getCounter(side).get()));
    }

    /**
     * @param side the side to get the tick queue for
     * @return the queue for the effective side
     */
    private static PriorityQueue<BOTickQueueElement> getQueue(Side side)
    {
        return side.isClient() ? clientTickHandlers : serverTickHandlers;
    }

    private static AtomicLong getCounter(Side side)
    {
        return side.isClient() ? clientTickCounter : serverTickCounter;
    }
    public static void registerTickHandler(ITickHandler handler, Side side)
    {
        registerScheduledTickHandler(new SingleIntervalHandler(handler), side);
    }

    public static void updateTickQueue(List<IScheduledTickHandler> ticks, Side side)
    {
        synchronized (ticks)
        {
            ticks.clear();
            long tick = getCounter(side).incrementAndGet();
            PriorityQueue<BOTickQueueElement> tickHandlers = getQueue(side);

            while (true)
            {
                if (tickHandlers.size()==0 || !tickHandlers.peek().scheduledNow(tick))
                {
                    break;
                }
                TickRegistry.BOTickQueueElement tickQueueElement  = tickHandlers.poll();
                tickQueueElement.update(tick);
                tickHandlers.offer(tickQueueElement);
                ticks.add(tickQueueElement.ticker);
            }
        }
    }


}
