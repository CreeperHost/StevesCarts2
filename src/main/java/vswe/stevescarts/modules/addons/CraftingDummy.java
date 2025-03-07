package vswe.stevescarts.modules.addons;

import com.google.common.collect.Lists;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.StackedContents;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public class CraftingDummy implements CraftingContainer {
	private ModuleCrafter module;

	public CraftingDummy(final ModuleCrafter module) {
		this.module = module;
	}

	@Override
	public int getWidth() {
		return 3;
	}

	@Override
	public int getHeight() {
		return 3;
	}

	@Override
	public List<ItemStack> getItems() {
		return Lists.newArrayList(module.getStack(0), module.getStack(1), module.getStack(2), module.getStack(3), module.getStack(4), module.getStack(5), module.getStack(6), module.getStack(7), module.getStack(8));
	}

	@Override
	public int getContainerSize() {
		return 9;
	}

	@Override
	public boolean isEmpty() {
		return false;
	}

	@Override
	public ItemStack getItem(int index) {
		return (index >= getContainerSize()) ? ItemStack.EMPTY : module.getStack(index);
	}

	@Override
	public ItemStack removeItem(int p_18942_, int p_18943_) {
		return ItemStack.EMPTY;
	}

	@Override
	public ItemStack removeItemNoUpdate(int index) {
		return ItemStack.EMPTY;
	}

	@Override
	public void setItem(int index, ItemStack p_18945_) {

	}

	@Override
	public void setChanged() {

	}

	@Override
	public boolean stillValid(Player player) {
		return true;
	}

	@Override
	public void clearContent() {
	}

	@Override
	public void fillStackedContents(StackedContents p_40281_) {
	}
}