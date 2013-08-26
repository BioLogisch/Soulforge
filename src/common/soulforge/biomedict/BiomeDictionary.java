package soulforge.biomedict;

import static net.minecraft.src.BiomeGenBase.beach;
import static net.minecraft.src.BiomeGenBase.desert;
import static net.minecraft.src.BiomeGenBase.desertHills;
import static net.minecraft.src.BiomeGenBase.extremeHills;
import static net.minecraft.src.BiomeGenBase.extremeHillsEdge;
import static net.minecraft.src.BiomeGenBase.forest;
import static net.minecraft.src.BiomeGenBase.forestHills;
import static net.minecraft.src.BiomeGenBase.frozenOcean;
import static net.minecraft.src.BiomeGenBase.frozenRiver;
import static net.minecraft.src.BiomeGenBase.hell;
import static net.minecraft.src.BiomeGenBase.iceMountains;
import static net.minecraft.src.BiomeGenBase.icePlains;
import static net.minecraft.src.BiomeGenBase.jungle;
import static net.minecraft.src.BiomeGenBase.jungleHills;
import static net.minecraft.src.BiomeGenBase.mushroomIsland;
import static net.minecraft.src.BiomeGenBase.mushroomIslandShore;
import static net.minecraft.src.BiomeGenBase.ocean;
import static net.minecraft.src.BiomeGenBase.plains;
import static net.minecraft.src.BiomeGenBase.river;
import static net.minecraft.src.BiomeGenBase.sky;
import static net.minecraft.src.BiomeGenBase.swampland;
import static net.minecraft.src.BiomeGenBase.taiga;
import static net.minecraft.src.BiomeGenBase.taigaHills;


import java.util.ArrayList;
import java.util.EnumSet;

import net.minecraft.src.BiomeGenBase;



public class BiomeDictionary
{
	public enum Type
	{
		FOREST,
		PLAINS,
		MOUNTAIN,
		HILLS,
		SWAMP,
		WATER,
		DESERT,
		FROZEN,
		JUNGLE,
		WASTELAND,
		BEACH,
		NETHER,
		END,
		MUSHROOM,
		MAGICAL;
	}

	private static final int BIOME_LIST_SIZE = 256;
	private static BiomeInfo[] biomeList = new BiomeInfo[BIOME_LIST_SIZE];
	private static ArrayList<BiomeGenBase>[] typeInfoList = new ArrayList[Type.values().length];

	private static class BiomeInfo
	{
		public EnumSet<Type> typeList;

		public BiomeInfo(Type[] types)
		{
			typeList = EnumSet.noneOf(Type.class);
			for(Type t : types)
			{
				typeList.add(t);
			}
		}
	}

	static
	{
		registerVanillaBiomes();
	}

	/**
	 * Registers a biome with a specific biome type
	 * 
	 * @param biome the biome to be registered
	 * @param type the type to register the biome as
	 * @return returns true if the biome was registered successfully
	 */
	public static boolean registerBiomeType(BiomeGenBase biome, Type ... types)
	{   
		if(BiomeGenBase.biomeList[biome.biomeID] != null)
		{
			for(Type type : types)
			{
				if(typeInfoList[type.ordinal()] == null)
				{
					typeInfoList[type.ordinal()] = new ArrayList<BiomeGenBase>();
				}

				typeInfoList[type.ordinal()].add(biome);
			}

			if(biomeList[biome.biomeID] == null)
			{
				biomeList[biome.biomeID] = new BiomeInfo(types);
			}
			else
			{
				for(Type type : types)
				{
					biomeList[biome.biomeID].typeList.add(type);
				}
			}

			return true;
		}

		return false;
	}

	/**
	 * Returns a list of biomes registered with a specific type
	 * 
	 * @param type the Type to look for
	 * @return a list of biomes of the specified type, null if there are none
	 */
	public static BiomeGenBase[] getBiomesForType(Type type)
	{
		if(typeInfoList[type.ordinal()] != null)
		{
			return (BiomeGenBase[])typeInfoList[type.ordinal()].toArray(new BiomeGenBase[0]);
		}

		return new BiomeGenBase[0];
	}

	/**
	 * Gets a list of Types that a specific biome is registered with
	 * 
	 * @param biome the biome to check
	 * @return the list of types, null if there are none
	 */
	public static Type[] getTypesForBiome(BiomeGenBase biome)
	{
		checkRegistration(biome);

		if(biomeList[biome.biomeID] != null)
		{
			return (Type[])biomeList[biome.biomeID].typeList.toArray(new Type[0]);
		}

		return new Type[0];
	}

	/**
	 * Checks to see if two biomes are registered as having the same type
	 * 
	 * @param biomeA
	 * @param biomeB
	 * @return returns true if a common type is found, false otherwise
	 */
	public static boolean areBiomesEquivalent(BiomeGenBase biomeA, BiomeGenBase biomeB)
	{
		int a = biomeA.biomeID;
		int b = biomeB.biomeID;

		checkRegistration(biomeA);
		checkRegistration(biomeB);

		if(biomeList[a] != null && biomeList[b] != null)
		{
			for(Type type : biomeList[a].typeList)
			{
				if(containsType(biomeList[b], type))
				{
					return true;
				}
			}
		}

		return false;
	}

	/**
	 * Checks to see if the given biome is registered as being a specific type
	 * 
	 * @param biome the biome to be considered
	 * @param type the type to check for
	 * @return returns true if the biome is registered as being of type type, false otherwise
	 */
	public static boolean isBiomeOfType(BiomeGenBase biome, Type type)
	{
		checkRegistration(biome);

		if(biomeList[biome.biomeID] != null)
		{
			return containsType(biomeList[biome.biomeID], type);
		}

		return false;
	}

	/**
	 * Checks to see if the given biome has been registered as being of any type
	 * @param biome the biome to consider
	 * @return returns true if the biome has been registered, false otherwise
	 */
	public static boolean isBiomeRegistered(BiomeGenBase biome)
	{    
		return biomeList[biome.biomeID] != null;
	}

	public static boolean isBiomeRegistered(int biomeID)
	{
		return biomeList[biomeID] != null;
	}

	/**
	 * Loops through the biome list and automatically adds tags to any biome that does not have any
	 * This should be called in postInit to make sure all biomes have been registered
	 * 
	 * DO NOT call this during world generation
	 */
	public static void registerAllBiomes()
	{
		for(int i = 0; i < BIOME_LIST_SIZE; i++)
		{
			if(BiomeGenBase.biomeList[i] != null)
			{
				checkRegistration(BiomeGenBase.biomeList[i]);
			}
		}
	}

	/**
	 * Automatically looks for and registers a given biome with appropriate tags
	 * This method is called automatically if a biome has not been registered with any tags,
	 * And another method requests information about it
	 * 
	 * NOTE: You can opt out of having your biome registered by registering it as type NULL
	 * 
	 * @param biome the biome to be considered
	 */
	public static void makeBestGuess(BiomeGenBase biome)
	{    
		if(biome.theBiomeDecorator.treesPerChunk >= 3)
		{
			if(biome.isHighHumidity() && biome.temperature >= 1.0F)
			{
				BiomeDictionary.registerBiomeType(biome, Type.JUNGLE);
			}
			else if(!biome.isHighHumidity())
			{
				BiomeDictionary.registerBiomeType(biome, Type.FOREST);
			}
		}
		else if(biome.maxHeight <= 0.3F && biome.maxHeight >= 0.0F)
		{
			if(!biome.isHighHumidity() || biome.minHeight >= 0.0F)
			{
				BiomeDictionary.registerBiomeType(biome, Type.PLAINS);
			}
		}

		if(biome.isHighHumidity() && biome.minHeight < 0.0F && (biome.maxHeight <= 0.3F && biome.maxHeight >= 0.0F))
		{
			BiomeDictionary.registerBiomeType(biome, Type.SWAMP);
		}

		if(biome.minHeight <= -0.5F)
		{
			BiomeDictionary.registerBiomeType(biome, Type.WATER);
		}

		if(biome.maxHeight >= 1.5F)
		{
			BiomeDictionary.registerBiomeType(biome, Type.MOUNTAIN);
		}

		if(biome.getEnableSnow() || biome.temperature < 0.2F)
		{
			BiomeDictionary.registerBiomeType(biome, Type.FROZEN);
		}

		if(!biome.isHighHumidity() && biome.temperature >= 1.0F)
		{
			BiomeDictionary.registerBiomeType(biome, Type.DESERT);
		}
	}

	//Internal implementation    
	private static void checkRegistration(BiomeGenBase biome)
	{
		if(!isBiomeRegistered(biome))
		{
			makeBestGuess(biome);
		}
	}

	private static boolean containsType(BiomeInfo info, Type type)
	{
		return info.typeList.contains(type);
	}

	private static void registerVanillaBiomes()
	{
		registerBiomeType(ocean,               Type.WATER          );
		registerBiomeType(plains,              Type.PLAINS         );
		registerBiomeType(desert,              Type.DESERT         );
		registerBiomeType(extremeHills,        Type.MOUNTAIN       );
		registerBiomeType(forest,              Type.FOREST         );
		registerBiomeType(taiga,               Type.FOREST,  Type.FROZEN);
		registerBiomeType(taigaHills,          Type.FOREST,  Type.FROZEN);
		registerBiomeType(swampland,           Type.SWAMP          );
		registerBiomeType(river,               Type.WATER          );
		registerBiomeType(frozenOcean,         Type.WATER,   Type.FROZEN);
		registerBiomeType(frozenRiver,         Type.WATER,   Type.FROZEN);
		registerBiomeType(icePlains,           Type.FROZEN         );
		registerBiomeType(iceMountains,        Type.FROZEN         );
		registerBiomeType(beach,               Type.BEACH          );
		registerBiomeType(desertHills,         Type.DESERT         );
		registerBiomeType(jungle,              Type.JUNGLE         );
		registerBiomeType(jungleHills,         Type.JUNGLE         );
		registerBiomeType(forestHills,         Type.FOREST         );
		registerBiomeType(sky,                 Type.END            );
		registerBiomeType(hell,                Type.NETHER         );
		registerBiomeType(mushroomIsland,      Type.MUSHROOM       );
		registerBiomeType(extremeHillsEdge,    Type.MOUNTAIN       );
		registerBiomeType(mushroomIslandShore, Type.MUSHROOM, Type.BEACH);
	}
}