package net.minecraft.src.customore.util;

import net.minecraft.src.FCAddOnHandler;

public class COLogger {

	public static COLogger log = new COLogger();
	
	
	
	public void warning(String message) {
        FCAddOnHandler.LogMessage("[BetterOre] WARN:" + message);

	}
	
	public void fine(String message) {
        FCAddOnHandler.LogMessage("[BetterOre] FINE:" + message);

	}
	
	public void finer(String message) {
        FCAddOnHandler.LogMessage("[BetterOre] FINER:" + message);

	}
	
	public void throwing(String message,String omessage, Throwable e) {
        FCAddOnHandler.LogMessage("[BetterOre] FATAL:" + message + " " + omessage + " " + e.getMessage());

	}
	
}
