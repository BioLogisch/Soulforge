package biomesoplenty.itemblock;

import net.minecraft.src.ItemBlock;
import net.minecraft.src.ItemStack;

public class ItemBlockColorizedSapling extends ItemBlock
{
	private static final String[] saplings = new String[] {"acacia", "mangrove", "palm", "redwood", "willow", "pine"};

	public ItemBlockColorizedSapling(int par1)
	{
		super(par1 - 256);
		setMaxDamage(0);
		setHasSubtypes(true);
	}

	@Override
	public int getMetadata(int meta)
	{
		return meta & 15;
	}

	@Override
	public String getUnlocalizedName(ItemStack itemStack)
	{
		int meta = itemStack.getItemDamage();
		if (meta < 0 || meta >= saplings.length) {
			meta = 0;
		}

		return super.getUnlocalizedName() + "." + (new StringBuilder()).append(saplings[meta]).append("Sapling").toString();
	}
}
