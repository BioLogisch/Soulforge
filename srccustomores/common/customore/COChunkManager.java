package customore;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;

import customore.util.COLogger;
import soulforge.utils.ConfigFile;
import net.minecraft.server.MinecraftServer;
import net.minecraft.src.ChunkCoordIntPair;
import net.minecraft.src.FCAddOnHandler;
import net.minecraft.src.IChunkProvider;
import net.minecraft.src.ISaveFormat;
import net.minecraft.src.SaveFormatOld;
import net.minecraft.src.World;
import net.minecraft.src.WorldServer;

public class COChunkManager {
	private static COChunkManager INSTANCE = new COChunkManager();
	private MinecraftServer server;

	private Properties chunkLoaders = null;
	
	private HashMap<String, HashSet<ChunkCoordIntPair>> loadedChunksForDimension;
	private HashMap<String, HashSet<ChunkCoordIntPair>> spawnableChunksForDimension;

	
	public static COChunkManager instance() {
		return INSTANCE;
	}
	
	public void init(MinecraftServer server) {
		this.chunkLoaders = new Properties();
		this.server = server;
		this.chunkLoaders = loadConfig();
		this.loadedChunksForDimension = new HashMap<String, HashSet<ChunkCoordIntPair>>();
		this.spawnableChunksForDimension = new HashMap<String, HashSet<ChunkCoordIntPair>>();

	}
	
	public void load() {
		for (Entry<Object, Object> entry : chunkLoaders.entrySet()) {
			String key = (String)entry.getKey();
			String activated = (String)entry.getValue();
			if (Boolean.parseBoolean(activated)) {
				String dimension = key.split(":")[0];
				HashSet<ChunkCoordIntPair> loadedChunks = getLoadedChunksForDimension(dimension);
				loadedChunks.addAll(getChunkCoordsForId(key));
				
				if (getSpawnableFromId(key)) {
					HashSet<ChunkCoordIntPair> spawnChunks = getSpawnableChunksForDimension(dimension);
					spawnChunks.addAll(getChunkCoordsForId(key));
				}
				
			}
		}
		
		for (WorldServer world : server.worldServers) {
			
			String id = String.valueOf(world.provider.dimensionId);
			HashSet<ChunkCoordIntPair> coords =	getLoadedChunksForDimension(id);
			FCAddOnHandler.LogMessage("Loaded Chunks : " + coords.size() + " for world " + id );

			IChunkProvider provider = world.getChunkProvider();
						
			for (ChunkCoordIntPair chunkCoordIntPair : coords) {
				COLogger.log.fine("Loading chunks : " + chunkCoordIntPair + " for world " + id + " provider " + provider);
				provider.provideChunk(chunkCoordIntPair.chunkXPos, chunkCoordIntPair.chunkZPos);
			}
		}
	}

	public void close() {
		this.loadedChunksForDimension = new HashMap<String, HashSet<ChunkCoordIntPair>>();
		this.spawnableChunksForDimension = new HashMap<String, HashSet<ChunkCoordIntPair>>();
	}
	
	
	
	public void addChunkLoader(String id, Boolean activated) {
		chunkLoaders.put(id, String.valueOf(activated));
		saveConfig(chunkLoaders);
	}
	
	public void updateChunkLoader(String id, Boolean activated) {
		chunkLoaders.put(id, String.valueOf(activated));
		saveConfig(chunkLoaders);
		if (activated) {
			String dimension = id.split(":")[0];
			HashSet<ChunkCoordIntPair> loadedChunks = getLoadedChunksForDimension(dimension);
			loadedChunks.addAll(getChunkCoordsForId(id));
			
			if (getSpawnableFromId(id)) {
				HashSet<ChunkCoordIntPair> spawnChunks = getSpawnableChunksForDimension(dimension);
				spawnChunks.addAll(getChunkCoordsForId(id));
			}
			
		} else {
			String dimension = id.split(":")[0];
			HashSet<ChunkCoordIntPair> loadedChunks = getLoadedChunksForDimension(dimension);
			loadedChunks.removeAll(getChunkCoordsForId(id));
			
			if (getSpawnableFromId(id)) {
				HashSet<ChunkCoordIntPair> spawnChunks = getSpawnableChunksForDimension(dimension);
				spawnChunks.removeAll(getChunkCoordsForId(id));
			}
		}
	}
	
	public void removeChunkLoader(String id) {
		Boolean activate = Boolean.parseBoolean(chunkLoaders.getProperty(id));
		if (activate) {
			String dimension = id.split(":")[0];
			HashSet<ChunkCoordIntPair> loadedChunks = getLoadedChunksForDimension(dimension);
			loadedChunks.removeAll(getChunkCoordsForId(id));
			
			if (getSpawnableFromId(id)) {
				HashSet<ChunkCoordIntPair> spawnChunks = getSpawnableChunksForDimension(dimension);
				spawnChunks.removeAll(getChunkCoordsForId(id));
			}
		}
		chunkLoaders.remove(id);
		saveConfig(chunkLoaders);
	}
	
	
	

	public Boolean canUnloadChunk(World world, ChunkCoordIntPair chunkCoord) {
		String dimId = String.valueOf(world.provider.dimensionId);
		Boolean result = !getLoadedChunksForDimension(dimId).contains(chunkCoord);		
		return result;
	}
	
	public Boolean hasLoadedChunksInDimension(int dimension) {
		Boolean result = !getLoadedChunksForDimension(String.valueOf(dimension)).isEmpty();
		return result;
	}
	
	public HashSet<ChunkCoordIntPair> loadedChunksForDimension(int dimId) {
		return getLoadedChunksForDimension(String.valueOf(dimId));
	}
	
	public HashSet<ChunkCoordIntPair> spawnableChunksForDimension(int dimId) {
		return getSpawnableChunksForDimension(String.valueOf(dimId));

	}
	
	public Boolean coordinateIsInSpawnableChunk(int x, int y, int z, int dimId) {
		int centerChunkX = x >> 4;
		int centerChunkZ = z >> 4;
		ChunkCoordIntPair chunkCoord = new ChunkCoordIntPair(centerChunkX, centerChunkZ);
		if (spawnableChunksForDimension(dimId).contains(chunkCoord)) {
			return true;
		}
		return false;
	}

	
	private HashSet<ChunkCoordIntPair> getChunkCoordsForId(String id) {
		HashSet<ChunkCoordIntPair> result = new HashSet<ChunkCoordIntPair>();

		String[] comps = id.split(":");
		
		int x = Integer.parseInt(comps[1]);
		int z = Integer.parseInt(comps[3]);

		int centerChunkX = x >> 4;
		int centerChunkZ = z >> 4;

		COLogger.log.fine("Center Coords " + centerChunkX + ":" + centerChunkZ);
		
		for (int i = -1; i < 2; i++) {
			for (int j = -1; j < 2; j++) {
				int chunkX = centerChunkX + (i);
				int chunkZ = centerChunkZ + (j);
				result.add(new ChunkCoordIntPair(chunkX, chunkZ));
			}
		}
		return result;
	}
	
	private Boolean getSpawnableFromId(String id) {
		Boolean result = false;
		String[] comps = id.split(":");
		if (comps.length > 4) {
			result = Boolean.parseBoolean(comps[4]);	
		}
		return result;
	}
	
	
	private HashSet<ChunkCoordIntPair> getLoadedChunksForDimension(String dimId) {
		if (!this.loadedChunksForDimension.containsKey(dimId)) {
			this.loadedChunksForDimension.put(dimId, new HashSet<ChunkCoordIntPair>());
		}
		return this.loadedChunksForDimension.get(dimId);
	}
	
	private HashSet<ChunkCoordIntPair> getSpawnableChunksForDimension(String dimId) {
		if (!this.spawnableChunksForDimension.containsKey(dimId)) {
			this.spawnableChunksForDimension.put(dimId, new HashSet<ChunkCoordIntPair>());
		}
		return this.spawnableChunksForDimension.get(dimId);
	}
	
	private File getWorldConfigFile() {
		File saveDir = null;
		ISaveFormat var9 = server.getActiveAnvilConverter();

		if (var9 != null && var9 instanceof SaveFormatOld)
		{
			saveDir = ((SaveFormatOld)var9).getSavesDirectory();
		}

		return new File(saveDir, server.getFolderName() + File.separatorChar + "chunkloader.cfg");
	}
	
	private Properties loadConfig() {
		Properties props = new Properties();
		File configFile = getWorldConfigFile();
		if (configFile.exists()) {
			try {
				props.load(new FileInputStream(configFile));
			} catch (Exception e) {
				e.printStackTrace();
			}
			COLogger.log.fine("Loaded ChunkloaderConfig: " + getWorldConfigFile().getAbsolutePath());
		}
		return props;
	}
	
	private void saveConfig(Properties config) {
		try {
			config.store(new FileOutputStream(getWorldConfigFile()), "Chunloader properties");
			COLogger.log.fine("Save ChunkloaderConfig: " + getWorldConfigFile().getAbsolutePath());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


}
