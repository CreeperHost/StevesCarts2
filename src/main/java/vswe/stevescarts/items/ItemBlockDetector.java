//package vswe.stevescarts.items;
//
//import net.minecraft.block.Block;
//import net.minecraft.item.ItemStack;
//import vswe.stevescarts.StevesCarts;
//
//import javax.annotation.Nonnull;
//
//public class ItemBlockDetector extends ItemBlock {
//	public ItemBlockDetector(final Block b) {
//		super(b);
//		setHasSubtypes(true);
//		setMaxDamage(0);
//		setCreativeTab(StevesCarts.tabsSC2Blocks);
//	}
//
//	@Override
//	public String getTranslationKey(@Nonnull ItemStack item) {
//		if (!item.isEmpty()) {
//			return "item.SC2:BlockDetector" + item.getItemDamage();
//		}
//		return "item.unknown";
//	}
//
//	@Override
//	public int getMetadata(final int dmg) {
//		return dmg;
//	}
//
//}
