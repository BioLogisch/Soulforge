package net.minecraft.src.customore.config.ui;

import net.minecraft.src.customore.util.COLogger;


public class COErrorHandler {

	public static boolean onConfigError(Throwable error)
	{
		COLogger.log.throwing("CustomOre", "onConfigError", error);
		error.printStackTrace();
		return false;
	}

}
