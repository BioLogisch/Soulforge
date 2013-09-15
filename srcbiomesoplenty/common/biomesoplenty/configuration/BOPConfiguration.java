package biomesoplenty.configuration;

import java.io.File;

import soulforge.utils.ConfigFile;
import biomesoplenty.utils.ConfigUtils;

public class BOPConfiguration
{
	public static ConfigFile mainConfigFile;
	public static ConfigFile biomeGenConfigFile;
	public static ConfigFile terrainGenConfigFile;
	public static ConfigFile idConfigFile;
	public static ConfigFile miscConfigFile;
	
	public static void init()
	{
	    mainConfigFile = new BOPConfiguration.Main();
	    biomeGenConfigFile = new BOPConfiguration.BiomeGen();
	    idConfigFile = new BOPConfiguration.IDs();
	}

	public static class Main extends ConfigFile
	{
		@Override
		public void setDefaults() 
		{
			properties.setProperty("enableCustomContent", "true");
		}

		@Override
		public File getConfigFile() 
		{
			return new File(this.baseConfigDir + File.separator + "biomesoplenty" + File.separator + "main.cfg");
		}

		@Override
		public String getHeader()
		{
			return "Biomes O Plenty Main Config";
		}
	}

	public static class BiomeGen extends ConfigFile
	{
		@Override
		public void setDefaults() 
		{
			properties.setProperty("alpsGeneration", "false");
			properties.setProperty("arcticGeneration", "false");
			properties.setProperty("birchForestGeneration", "false");
			properties.setProperty("bogGeneration", "false");
			properties.setProperty("borealForestGeneration", "false");
			properties.setProperty("brushlandGeneration", "false");
			properties.setProperty("chaparralGeneration", "false");
			properties.setProperty("coniferousForestGeneration", "false");
			properties.setProperty("coniferousForestSnowGeneration", "false");
			properties.setProperty("deciduousForestGeneration", "false");
			properties.setProperty("dunesGeneration", "false");
			properties.setProperty("fenGeneration", "false");
			properties.setProperty("fieldGeneration", "true");
			properties.setProperty("frostForestGeneration", "false");
			properties.setProperty("glacierGeneration", "false");
			properties.setProperty("grasslandGeneration", "false");
			properties.setProperty("groveGeneration", "false");
			properties.setProperty("heathlandGeneration", "false");
			properties.setProperty("highlandGeneration", "true");
			properties.setProperty("jadeCliffsGeneration", "true");
			properties.setProperty("lushSwampGeneration", "false");
			properties.setProperty("marshGeneration", "false");
			properties.setProperty("meadowGeneration", "false");
			properties.setProperty("moorGeneration", "false");
			properties.setProperty("mountainGeneration", "true");
			properties.setProperty("mysticGroveGeneration", "true");
			properties.setProperty("oasisGeneration", "false");
			properties.setProperty("ominousWoodsGeneration", "true");
			properties.setProperty("originValleyGeneration", "false");
			properties.setProperty("polarGeneration", "false");
			properties.setProperty("prairieGeneration", "true");
			properties.setProperty("rainforestGeneration", "false");
			properties.setProperty("redwoodForestGeneration", "true");
			properties.setProperty("sacredSpringsGeneration", "true");
			properties.setProperty("savannaGeneration", "true");
			properties.setProperty("scrublandGeneration", "false");
			properties.setProperty("shieldGeneration", "false");
			properties.setProperty("shrublandGeneration", "false");
			properties.setProperty("sludgepitGeneration", "true");
			properties.setProperty("spruceWoodsGeneration", "true");
			properties.setProperty("steppeGeneration", "false");
			properties.setProperty("temperateRainforestGeneration", "false");
			properties.setProperty("thicketGeneration", "false");
			properties.setProperty("timberGeneration", "false");
			properties.setProperty("tropicalRainforestGeneration", "true");
			properties.setProperty("tropicsGeneration", "false");
			properties.setProperty("tundraGeneration", "false");
			properties.setProperty("volcanoGeneration", "true");
			properties.setProperty("wetlandGeneration", "false");
			properties.setProperty("woodlandGeneration", "true");
			properties.setProperty("desertGeneration", "true");
			properties.setProperty("jungleGeneration", "true");
			properties.setProperty("swampGeneration", "true");
			properties.setProperty("mushroomGeneration", "true");

		}

		@Override
		public File getConfigFile() 
		{
			return new File(this.baseConfigDir + File.separator + "biomesoplenty" + File.separator + "biomegen.cfg");
		}

		@Override
		public String getHeader()
		{
			return "Biomes O Plenty Biome Gen Config";
		}
	}

	public static class IDs extends ConfigFile
	{
		@Override
		public void setDefaults() 
		{
			ConfigUtils.setItemID(properties, "barkID", 25000);
			
			ConfigUtils.setBlockID(properties, "leaves1ID", 1923);
			ConfigUtils.setBlockID(properties, "leaves2ID", 1924);
			
			ConfigUtils.setBlockID(properties, "planksID", 1947);
			ConfigUtils.setBlockID(properties, "logs1ID", 1933);
			ConfigUtils.setBlockID(properties, "logs2ID", 1934);
			ConfigUtils.setBlockID(properties, "logs3ID", 1935);
			ConfigUtils.setBlockID(properties, "logs4ID", 1936);
			
			ConfigUtils.setBlockID(properties, "saplingsID", 1937);
			ConfigUtils.setBlockID(properties, "saplingsColourizedID", 1938);
			
			ConfigUtils.setBlockID(properties, "leavesColourizedID", 1962);
		}

		@Override
		public File getConfigFile() 
		{
			return new File(this.baseConfigDir + File.separator + "biomesoplenty" + File.separator + "ids.cfg");
		}

		@Override
		public String getHeader()
		{
			return "Biomes O Plenty ID Config";
		}
	}
}
