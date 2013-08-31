package customore.util;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

import soulforge.utils.ConfigFile;

public class COFileUtils {
	
	
	public static File getBaseDir() {
		File baseDir = new File(ConfigFile.baseConfigDir);
		return baseDir;
	}
	
	public static File getConfigDir() {
		File configdir = new File(getBaseDir(), "betterores");
		if (!configdir.exists()){ configdir.mkdirs();}
		return configdir;
	}
	
	public static File getConfigFile(String fileName) {
		File file = new File(getConfigDir(), fileName);
		if (!file.getParentFile().exists()){ file.getParentFile().mkdirs();}
		return file;
	}
	
	public static boolean unpackResourceFile(String resourceName, File destination)
	{
		if (destination.exists())
		{
			return false;
		}
		else
		{
			try
			{
				COLogger.log.fine("Unpacking \'" + resourceName + "\' ...");
				ClassLoader classLoader = Thread.currentThread().getContextClassLoader();

				InputStream ex = classLoader.getResourceAsStream(resourceName);
				if (ex == null) {
					throw new RuntimeException("Failed to unpack resource \'" + resourceName + "\'");
				}
				BufferedOutputStream streamOut = new BufferedOutputStream(new FileOutputStream(destination));
				byte[] buffer = new byte[1024];
				boolean len = false;
				int len1;

				while ((len1 = ex.read(buffer)) >= 0)
				{
					streamOut.write(buffer, 0, len1);
				}

				ex.close();
				streamOut.close();
				return true;
			}
			catch (Exception var6)
			{
				throw new RuntimeException("Failed to unpack resource \'" + resourceName + "\'", var6);
			}
		}
	}
	
}
