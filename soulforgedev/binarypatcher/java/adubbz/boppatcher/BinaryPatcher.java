package adubbz.boppatcher;

import ie.wombat.jbdiff.JBPatch;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.Iterator;

import org.apache.commons.io.FileUtils;

public class BinaryPatcher 
{
	public static void applyBinaryPatches(File jarloc)
	{
		File mcJarPatches = new File(jarloc.getAbsoluteFile() + File.separator + "patches" + File.separator + "MINECRAFT-JAR");
		File mcServerJarPatches = new File(jarloc.getAbsoluteFile() + File.separator + "patches" + File.separator + "MINECRAFT_SERVER-JAR");
		
		File mcJarTarget = new File(jarloc.getAbsoluteFile() + File.separator + "btw" + File.separator + "btwtemp" + File.separator + "MINECRAFT-JAR");
		File mcServerJarTarget = new File(jarloc.getAbsoluteFile() + File.separator + "btw" + File.separator + "btwtemp" + File.separator + "MINECRAFT_SERVER-JAR");
		
		if (mcJarPatches.exists())
		{
			applyPatches(mcJarPatches, mcJarTarget);
		}
		
		if (mcServerJarPatches.exists())
		{
			applyPatches(mcServerJarPatches, mcServerJarTarget);
		}
	}
	
	public static void applyModBinaryPatches(File jarloc)
	{
		File modsDir = new File(jarloc.getAbsoluteFile() + File.separator + "mods");
		
		File mcJarTarget = new File(jarloc.getAbsoluteFile() + File.separator + "btw" + File.separator + "btwtemp" + File.separator + "MINECRAFT-JAR");
		File mcServerJarTarget = new File(jarloc.getAbsoluteFile() + File.separator + "btw" + File.separator + "btwtemp" + File.separator + "MINECRAFT_SERVER-JAR");

		for (File subdir : modsDir.listFiles(new FilenameFilter() 
		{ @Override public boolean accept(File dir, String name) { return new File(dir, name).isDirectory(); } })) 
		{
			File mcJarPatches = new File(subdir.getAbsoluteFile() + File.separator + "patches" + File.separator + "MINECRAFT-JAR");
			File mcServerJarPatches = new File(subdir.getAbsoluteFile() + File.separator + "patches" + File.separator + "MINECRAFT_SERVER-JAR");
			
			if (mcJarPatches.exists())
			{
				applyPatches(mcJarPatches, mcJarTarget);
			}
			
			if (mcServerJarPatches.exists())
			{
				applyPatches(mcServerJarPatches, mcServerJarTarget);
			}
		}
	}
	
	private static String findTargetFile(File infile)
	{
		return infile.getName().substring(0, infile.getName().lastIndexOf('.'));
	}
	
	private static void applyPatches(File patchdir, File targetdir)
	{
		Iterator iterator = FileUtils.iterateFiles(patchdir, new String[] {"patch"}, true);
		
		while (iterator.hasNext())
		{
			File iteratedFile = (File)iterator.next();
			File targetfile = new File(targetdir + File.separator + findTargetFile(iteratedFile));
			
			try 
			{
				JBPatch.bspatch(targetfile, targetfile, iteratedFile);
				System.out.println("Patched " + targetfile.getName());
			} 
			catch (IOException e) 
			{
				e.printStackTrace();
			}
		}
	}
}
