package customore;

import java.util.EnumSet;
import java.util.Random;

import net.minecraft.server.MinecraftServer;
import net.minecraft.src.FCAddOn;
import net.minecraft.src.FCAddOnHandler;
import net.minecraft.src.IChunkProvider;
import net.minecraft.src.World;
import net.minecraft.src.WorldInfo;
import soulforge.utils.CommonHandler;
import soulforge.utils.EventRegistry;
import soulforge.utils.GameRegistry;
import soulforge.utils.ICommonHandlerDelegate;
import soulforge.utils.IEventHandler;
import soulforge.utils.ITickHandler;
import soulforge.utils.IWorldGenerator;
import soulforge.utils.TickRegistry;
import soulforge.utils.TickType;
import customore.util.COConfig;
import customore.util.CORuntime;
import customore.util.COUtil;

public class CustomOre extends FCAddOn implements ITickHandler, IWorldGenerator, IEventHandler, ICommonHandlerDelegate
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
    	
    }
	
	@Override
	public void Initialize() {
		FCAddOnHandler.LogMessage("[BetterOre] Better Ore Version " + coVersion + " Initializing...");
		CommonHandler.instance().registerDelegate(this);
	}
	
	@Override
	public void initialized(CommonHandler handler) {
		 TickRegistry.registerTickHandler(this, handler.getSide());
	     FCAddOnHandler.LogMessage("initialized handler" + handler);
	     GameRegistry.registerWorldGenerator(this);
	     EventRegistry.registerEventHandler(this);
	     FCAddOnHandler.LogMessage("[BetterOre] Better Ore Initialization Complete.");
	}
	
	@Override
	public void PostInitialize()
	{
		
	}
	
	public void onClientTick() {
		if (COUtil.isSinglePlayer())
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