package net.minecraft.src.customore;

import java.io.DataInputStream;
import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import net.minecraft.server.MinecraftServer;
import net.minecraft.src.BiomeDecorator;
import net.minecraft.src.BiomeGenBase;
import net.minecraft.src.BlockSand;
import net.minecraft.src.ChunkCoordIntPair;
import net.minecraft.src.CompressedStreamTools;
import net.minecraft.src.ISaveFormat;
import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.RegionFileCache;
import net.minecraft.src.SaveFormatOld;
import net.minecraft.src.World;
import net.minecraft.src.WorldInfo;
import net.minecraft.src.WorldServer;
import net.minecraft.src.customore.config.COWorldConfig;
import net.minecraft.src.customore.config.ui.COErrorHandler;
import net.minecraft.src.customore.generator.ICOOreDistribution;
import net.minecraft.src.customore.util.COLogger;

public class COHandler {

	private static MinecraftServer _server = null;
	private static Map<Integer, HashMap<ChunkCoordIntPair, int[]>> _populatedChunks = new HashMap<Integer, HashMap<ChunkCoordIntPair, int[]>>();


	public static void onPopulateChunk(World world, Random rand, int chunkX, int chunkZ)
	{
		COWorldConfig cfg = COWorldConfig.getWorldConfig(world);
		HashMap<ChunkCoordIntPair, int[]> dimChunkMap = null;
		Integer cRange = Integer.valueOf(world.provider.dimensionId);
		dimChunkMap = _populatedChunks.get(cRange);

		if (dimChunkMap == null)
		{
			dimChunkMap = new HashMap();
			_populatedChunks.put(cRange, dimChunkMap);
		}

		ChunkCoordIntPair neighborMax = new ChunkCoordIntPair(chunkX >>> 4, chunkZ >>> 4);
		int[] cX = (int[])((Map)dimChunkMap).get(neighborMax);

		if (cX == null)
		{
			cX = new int[16];
			((Map)dimChunkMap).put(neighborMax, cX);
		}

		cX[chunkX & 15] |= 65537 << (chunkZ & 15);
		int var16 = (cfg.deferredPopulationRange + 15) / 16;
		int var17 = 4 * var16 * (var16 + 1) + 1;

		for (int var18 = chunkX - var16; var18 <= chunkX + var16; ++var18)
		{
			for (int cZ = chunkZ - var16; cZ <= chunkZ + var16; ++cZ)
			{
				int neighborCount = 0;

				for (int iX = var18 - var16; iX <= var18 + var16; ++iX)
				{
					for (int iZ = cZ - var16; iZ <= cZ + var16; ++iZ)
					{
						ChunkCoordIntPair chunkKey = new ChunkCoordIntPair(iX >>> 4, iZ >>> 4);
						int[] chunkData = (int[])((Map)dimChunkMap).get(chunkKey);

						if (chunkData == null)
						{
							chunkData = new int[16];
							((Map)dimChunkMap).put(chunkKey, chunkData);
						}

						if ((chunkData[iX & 15] >>> (iZ & 15) & 65536) == 0)
						{
							boolean populated = isChunkSavedPopulated(world, iX, iZ);
							chunkData[iX & 15] |= (populated ? 65537 : 65536) << (iZ & 15);
						}

						if ((chunkData[iX & 15] >>> (iZ & 15) & 1) != 0)
						{
							++neighborCount;
						}
					}
				}

				if (neighborCount == var17)
				{
					populateDistributions(cfg.getOreDistributions(), world, var18, cZ);
				}
			}
		}
	}

	public static void populateDistributions(Collection<ICOOreDistribution> distributions, World world, int chunkX, int chunkZ)
	{
		BlockSand.fallInstantly = true;
		world.scheduledUpdatesAreImmediate = true;

		for (ICOOreDistribution dist : distributions) {
			dist.generate(world, chunkX, chunkZ);
			dist.populate(world, chunkX, chunkZ);
			dist.cull();
		}

		world.scheduledUpdatesAreImmediate = false;
		BlockSand.fallInstantly = false;
	}


	private static boolean isChunkSavedPopulated(World world, int chunkX, int chunkZ)
	{
		File saveFolder = COWorldConfig.getWorldConfig(world).dimensionDir;
		DataInputStream stream = RegionFileCache.getChunkInputStream(saveFolder, chunkX, chunkZ);

		if (stream != null)
		{
			try
			{
				NBTTagCompound ex = CompressedStreamTools.read(stream);

				if (ex.hasKey("Level") && ex.getCompoundTag("Level").getBoolean("TerrainPopulated"))
				{
					return true;
				}
			}
			catch (IOException var6)
			{
				;
			}
		}

		return false;
	}

	public static  boolean checkIfServerChanged(MinecraftServer currentServer, WorldInfo worldInfo)
	{
		if (_server == currentServer)
		{
			return false;
		}
		else
		{
			if (currentServer != null && worldInfo == null)
			{
				if (currentServer.worldServers == null)
				{
					return false;
				}

				for (WorldServer world : currentServer.worldServers) {
					if (world != null)
					{
						worldInfo = world.getWorldInfo();
					}

					if (worldInfo != null)
					{
						break;
					}                	
				}

				if (worldInfo == null)
				{
					return false;
				}
			}

			onServerChanged(currentServer, worldInfo);
			return true;
		}
	}

	public static void onServerChanged(MinecraftServer server, WorldInfo worldInfo)
	{
		COWorldConfig.clearWorldConfigs();;
		COWorldConfig.loadedOptionOverrides[1] = COWorldConfig.loadedOptionOverrides[2] = null;
		_populatedChunks.clear();



		_server = server;
		BiomeGenBase[] worldBaseDir = BiomeGenBase.biomeList;
		int saveFormat = worldBaseDir.length;

		for (int cfg = 0; cfg < saveFormat; ++cfg)
		{
			BiomeGenBase ex = worldBaseDir[cfg];

			if (ex != null && ex.theBiomeDecorator != null)
			{
				patchBiomeDecorator(ex.theBiomeDecorator);
			}
		}

		File saveDir = null;
		ISaveFormat var9 = _server.getActiveAnvilConverter();

		if (var9 != null && var9 instanceof SaveFormatOld)
		{
			saveDir = ((SaveFormatOld)var9).getSavesDirectory();
		}

		saveDir = new File(saveDir, _server.getFolderName());
		COWorldConfig var10 = null;

		while (var10 == null)
		{
			try
			{
				var10 = new COWorldConfig(worldInfo, saveDir);
				COWorldConfig.validateOptions(var10.getConfigOptions(), false);
				COWorldConfig.validateDistributions(var10.getOreDistributions(), false);
			}
			catch (Exception var7)
			{

				if (!COErrorHandler.onConfigError(var7))
				{
					break;
				}

				var10 = null;
			}
		}
	}

	private static void patchBiomeDecorator(BiomeDecorator decorator)
	{

		COLogger.log.throwing("patchBiomeDecorator", "not supported",new Exception("Not supported"));

		/*
		try
		{
			
			decorator.coalGen =  new WorldGenEmpty(decorator.coalGen);
			WorldGenerator ironGen = (WorldGenerator)PrivateAccess.getPrivateValue(BiomeDecorator.class, decorator, 11);
			WorldGenerator goldGen = (WorldGenerator)PrivateAccess.getPrivateValue(BiomeDecorator.class, decorator, 12);
			WorldGenerator redstoneGen = (WorldGenerator)PrivateAccess.getPrivateValue(BiomeDecorator.class, decorator, 13);
			WorldGenerator diamondGen = (WorldGenerator)PrivateAccess.getPrivateValue(BiomeDecorator.class, decorator, 14);
			WorldGenerator lapisGen = (WorldGenerator)PrivateAccess.getPrivateValue(BiomeDecorator.class, decorator, 15);
			//    PrivateAccess.setPrivateValue(BiomeDecorator.class, decorator, 10, new WorldGenEmpty(ex));
			//    PrivateAccess.setPrivateValue(BiomeDecorator.class, decorator, 11, new WorldGenEmpty(ironGen));
			//    PrivateAccess.setPrivateValue(BiomeDecorator.class, decorator, 12, new WorldGenEmpty(goldGen));
			//    PrivateAccess.setPrivateValue(BiomeDecorator.class, decorator, 13, new WorldGenEmpty(redstoneGen));
			//    PrivateAccess.setPrivateValue(BiomeDecorator.class, decorator, 14, new WorldGenEmpty(diamondGen));
			//    PrivateAccess.setPrivateValue(BiomeDecorator.class, decorator, 15, new WorldGenEmpty(lapisGen));
		}
		catch (Exception var7)
		{
			BOLogger.log.throwing("CustomOreGenBase", "patchBiomeDecorator", var7);
		}
		*/
	}


}
