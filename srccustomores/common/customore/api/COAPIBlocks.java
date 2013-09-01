package customore.api;

import customore.block.COChunkLoaderBlock;
import customore.block.COItemBlockChunkLoader;
import customore.entity.COChunkLoaderTileEntity;
import soulforge.utils.GameRegistry;
import soulforge.utils.LanguageRegistry;
import biomesoplenty.BiomesOPlenty;
import biomesoplenty.api.BOPAPIBlocks;
import biomesoplenty.blocks.BlockBOPColorizedLeaves;
import biomesoplenty.blocks.BlockBOPColorizedSapling;
import biomesoplenty.blocks.BlockBOPLeaves;
import biomesoplenty.blocks.BlockBOPLog;
import biomesoplenty.blocks.BlockBOPPlank;
import biomesoplenty.blocks.BlockBOPSapling;
import biomesoplenty.blocks.BlockBOPLeaves.LeafCategory;
import biomesoplenty.blocks.BlockBOPLog.LogCategory;
import biomesoplenty.itemblock.ItemBlockColorizedLeaves;
import biomesoplenty.itemblock.ItemBlockColorizedSapling;
import biomesoplenty.itemblock.ItemBlockLeaves;
import biomesoplenty.itemblock.ItemBlockLog;
import biomesoplenty.itemblock.ItemBlockPlank;
import biomesoplenty.itemblock.ItemBlockSapling;
import biomesoplenty.utils.ConfigUtils;
import net.minecraft.src.Block;
import net.minecraft.src.Item;
import net.minecraft.src.ItemAxe;
import net.minecraft.src.ItemPickaxe;
import net.minecraft.src.ItemStack;
import net.minecraft.src.Material;

public class COAPIBlocks {
		// ChunkLoader
		public static Block chunkloader = null;
		
		public static void init()
		{
			initializeBlocks();
			registerBlocks();
			setToolEffectiveness();
			setBuoyancy();
			localizeNames();
		}

		private static void initializeBlocks()
		{
			chunkloader = new COChunkLoaderBlock(4000, Material.rock).setUnlocalizedName("co.block.chunkloader");
		}
		
		private static void registerBlocks()
		{
			GameRegistry.registerCustomBlock(chunkloader, new COItemBlockChunkLoader(chunkloader.blockID));
		}
		
		private static void setToolEffectiveness()
		{
			ItemPickaxe.SetAllPicksToBeEffectiveVsBlock(chunkloader);
		}
		
		private static void setBuoyancy()
		{
	        Item.itemsList[chunkloader.blockID].SetBuoyancy(0.0F);
		}
		
		private static void localizeNames()
		{
			LanguageRegistry.addName(new ItemStack(chunkloader, 1, 0), "Chunkloader");
		}
}
