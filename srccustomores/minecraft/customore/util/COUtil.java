package customore.util;

import net.minecraft.client.Minecraft;

public class COUtil {

	public static Boolean isSinglePlayer() {
		return Minecraft.getMinecraft().isSingleplayer();
	}
}
