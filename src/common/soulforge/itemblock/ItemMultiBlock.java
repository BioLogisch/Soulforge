package soulforge.itemblock;

import net.minecraft.src.Block;
import net.minecraft.src.ItemBlockWithMetadata;
import net.minecraft.src.ItemStack;

public class ItemMultiBlock extends ItemBlockWithMetadata
{
  private static String[] blockTypes;

  public ItemMultiBlock(Block block, String[] types)
  {
    super(block.blockID - 256, block);
    setMaxDamage(0);
    setHasSubtypes(true);
    setUnlocalizedName(block.getUnlocalizedName());
    this.blockTypes = types;
  }

  @Override
  public String getUnlocalizedName(ItemStack itemstack) 
  {
    return super.getUnlocalizedName() + "." + this.blockTypes[itemstack.getItemDamage()];
  }
}
