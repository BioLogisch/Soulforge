package net.minecraft.src.customore;

import java.util.EnumSet;
import java.util.Random;

import soulforge.utils.CommonHandler;
import soulforge.utils.EventRegistry;
import soulforge.utils.GameRegistry;
import soulforge.utils.IEventHandler;
import soulforge.utils.ITickHandler;
import soulforge.utils.IWorldGenerator;
import soulforge.utils.Side;
import soulforge.utils.TickRegistry;
import soulforge.utils.TickType;
import net.minecraft.server.MinecraftServer;
import net.minecraft.src.FCAddOn;
import net.minecraft.src.FCAddOnHandler;
import net.minecraft.src.IChunkProvider;
import net.minecraft.src.World;
import net.minecraft.src.WorldInfo;
import net.minecraft.src.customore.util.COConfig;
import net.minecraft.src.customore.util.CORuntime;

public class CustomOre extends FCAddOn implements ITickHandler, IWorldGenerator, IEventHandler
{

    public static String coVersion = "0.0.1";
    public static CustomOre m_instance = new CustomOre();
    
    public static void vanillaConstruct()
    {
    	//Called by Block Stone to kick things off
    }

    @Override
    public void InitializeConfigs()
    {
		FCAddOnHandler.LogMessage("[BetterOre] Better Ore Version " + coVersion + " Loading configs...");
    	COConfig.init();
    }
    
    @Override
    public void PreInitialize() 
    {
    	 TickRegistry.registerTickHandler(this, Side.CLIENT);
	     GameRegistry.registerWorldGenerator(this);
	     EventRegistry.registerEventHandler(this);
    }
	
	@Override
	public void Initialize() {
		FCAddOnHandler.LogMessage("[BetterOre] Better Ore Version " + coVersion + " Initializing...");
		
		
        FCAddOnHandler.LogMessage("[BetterOre] Better Ore Initialization Complete.");
	}
	
	@Override
	public void PostInitialize()
	{
		
	}
	
	public void onClientTick() {
		Object minecraft = CORuntime.getObject(CORuntime.getMincraftClass(), "getMinecraft");
		if (minecraft != null && CORuntime.getBoolean(minecraft, "isSingleplayer"))
		{
			this.onServerTick();
		}
	}
	
	private void onServerTick()
	{
		COHandler.checkIfServerChanged(MinecraftServer.getServer(), (WorldInfo)null);
	}
	

	@Override
	public void onServerStarting(MinecraftServer server) {
		COHandler.onServerChanged(server, null);
	}

	@Override
	public void serverStarted() {}

	@Override
	public void serverStopping() {}

	@Override
	public void serverStopped() {}

	@Override
	public void serverAboutToStart(MinecraftServer server) {}

	@Override
	public void generate(Random random, int chunkX, int chunkZ, World world, IChunkProvider chunkGenerator, IChunkProvider chunkProvider) {
		COHandler.checkIfServerChanged(MinecraftServer.getServer(), world.getWorldInfo());
		COHandler.onPopulateChunk(world, random, chunkX, chunkZ);
	}

	@Override
	public void tickStart(EnumSet<TickType> type, Object... tickData) {}

	@Override
	public void tickEnd(EnumSet<TickType> type, Object... tickData) {
		if (type.contains(TickType.SERVER))
        {
            this.onServerTick();
        }

        if (type.contains(TickType.CLIENT))
        {
            this.onClientTick();
        }
	}

	@Override
	public EnumSet<TickType> ticks() {
        return EnumSet.of(TickType.CLIENT, TickType.SERVER);
	}

	@Override
	public String getLabel() {
        return "CustomOre.SFInterface";
	}

}
