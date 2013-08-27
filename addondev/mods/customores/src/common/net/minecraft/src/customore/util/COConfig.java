package net.minecraft.src.customore.util;

import net.minecraft.src.customore.config.COWorldConfig;
import net.minecraft.src.customore.config.ui.COErrorHandler;

public class COConfig {

	
	public static void init() {
		unpackConfigStandardFiles();
	}
	
	private static void unpackConfigStandardFiles() {
		COFileUtils.unpackResourceFile("net/minecraft/src/customore/resources/CustomOreGen_Config.xml", COFileUtils.getConfigFile("CustomOreGen_Config.xml"));
		COFileUtils.unpackResourceFile("net/minecraft/src/customore/resources/MinecraftOres.xml",COFileUtils.getConfigFile("modules/MinecraftOres.xml"));
	}
	
	private static void validateConfigs() {
		COWorldConfig config = null;

		while (config == null)
		{
			try
			{
				config = new COWorldConfig();
			}
			catch (Exception exception)
			{
				if (!COErrorHandler.onConfigError(exception))
				{
					break;
				}

				config = null;
			}
		}
	}
	
}
