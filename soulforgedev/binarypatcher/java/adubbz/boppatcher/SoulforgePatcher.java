package adubbz.boppatcher;

import java.io.File;
import java.io.IOException;

public class SoulforgePatcher 
{
	public static File jarLoc;
	
	public static void main(String[] args)
	{
		setJarLoc();

		new Console();

		System.out.println("Unzipping BTW");
		FileHandler.unzipBTW(jarLoc);
		System.out.println("Applying Soulforge Binary Pathces");
		BinaryPatcher.applyBinaryPatches(jarLoc);
		System.out.println("Applying Mod Binary Pathces");
		BinaryPatcher.applyModBinaryPatches(jarLoc);
		System.out.println("Copying Non BTW Edits");
		FileHandler.copyNonBTWEdits(jarLoc);
		System.out.println("Copying Mod Non BTW Edits");
		FileHandler.copyModNonBTWEdits(jarLoc);
		System.out.println("Rezipping BTW");
		FileHandler.rezipBTW(jarLoc);
		System.out.println("Removing Temporary Directory");
		FileHandler.removeTempDir(jarLoc);
		System.out.println("Done!");
	}
	
	private static void setJarLoc()
	{
		try
		{
			jarLoc = new File(SoulforgePatcher.class.getProtectionDomain().getCodeSource().getLocation().toURI());
			jarLoc = new File(jarLoc.getAbsolutePath().substring(0, jarLoc.getAbsolutePath().lastIndexOf(File.separator)) + File.separator);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}
