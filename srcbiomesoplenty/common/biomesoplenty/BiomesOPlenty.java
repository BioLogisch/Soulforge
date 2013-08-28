package biomesoplenty;


import net.minecraft.src.CreativeTabs;
import net.minecraft.src.FCAddOn;
import net.minecraft.src.FCAddOnHandler;
import net.minecraft.src.StringTranslate;
import net.minecraft.src.WorldType;
import soulforge.utils.LanguageRegistry;
import soulforge.utils.Localization;
import biomesoplenty.configuration.BOPBiomes;
import biomesoplenty.configuration.BOPBlocks;
import biomesoplenty.configuration.BOPConfiguration;
import biomesoplenty.configuration.BOPCrafting;
import biomesoplenty.configuration.BOPItems;
import biomesoplenty.configuration.CreativeTabsBOP;
import biomesoplenty.world.WorldTypeBOP;

public class BiomesOPlenty extends FCAddOn
{
    public static String bopVersionString = "0.1.3";
    public static BiomesOPlenty m_instance = new BiomesOPlenty();
    
    public static final WorldType BIOMEOP = (new WorldTypeBOP());  
    
	public static CreativeTabs tabBiomesOPlenty;
    
    public static void vanillaConstruct()
    {
    	//Called by Block Dirt to kick things off
    }
    
    @Override
    public void InitializeConfigs()
    {
    	BOPConfiguration.init();
    }
    
    @Override
    public void PreInitialize() 
    {
		tabBiomesOPlenty = new CreativeTabsBOP(CreativeTabs.getNextID(), "tabBiomesOPlenty");
    	
		if (BOPConfiguration.mainConfigFile.getBoolean("enableCustomContent"))
		{
			BOPBlocks.init();
			BOPItems.init();
		}
		
    	BOPBiomes.init();
    	biomesoplenty.integration.BetterThanHorsesIntegration.init();
    	
		LanguageRegistry.addStringLocalization("itemGroup.tabBiomesOPlenty", "Biomes O' Plenty");
    }
	
	@Override
	public void Initialize() 
	{
		FCAddOnHandler.LogMessage("[BiomesOPlenty] Biomes O Plenty Version " + bopVersionString + " Initializing...");
        FCAddOnHandler.LogMessage("[BiomesOPlenty] Biomes O Plenty Initialization Complete.");
	}
	
	@Override
	public void OnLanguageLoaded(StringTranslate translate) 
	{
		for (int l = 0; l < LanguageRegistry.localizations.size(); l++)
		{
			Localization localization = LanguageRegistry.localizations.get(l);
			
	        translate.GetTranslateTable().put(localization.getTarget(), localization.getName());
		}
	}
	
	@Override
	public void PostInitialize()
	{
		if (BOPConfiguration.mainConfigFile.getBoolean("enableCustomContent"))
		{
			BOPCrafting.init();
		}
	}
}
