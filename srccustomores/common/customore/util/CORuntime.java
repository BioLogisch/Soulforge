package customore.util;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class CORuntime {

	public static Class getMincraftClass() {
		Class<?> cls = null;
		try {
			cls = Class.forName("net.minecraft.client.Minecraft");
		} catch (Exception e) {
			//e.printStackTrace();
		}
		return cls;
	}
	
	

}
