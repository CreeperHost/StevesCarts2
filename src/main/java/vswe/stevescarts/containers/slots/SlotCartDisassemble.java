package vswe.stevescarts.containers.slots;


import net.minecraft.world.Container;

public class SlotCartDisassemble extends SlotCart
{
    public SlotCartDisassemble(final Container iinventory, final int i, final int j, final int k)
    {
        super(iinventory, i, j, k);
    }

    //TODO
    //	@Override
    //	public boolean isItemValid(
    //		@Nonnull
    //			ItemStack itemstack) {
    //		if (inventory instanceof TileEntityUpgrade) {
    //			final TileEntityUpgrade upgrade = (TileEntityUpgrade) inventory;
    //			if (upgrade.getUpgrade() != null) {
    //				for (final BaseEffect effect : upgrade.getUpgrade().getEffects()) {
    //					if (effect instanceof Disassemble) {
    //						return ((Disassemble) effect).canDisassemble(upgrade) == 2 && super.isItemValid(itemstack);
    //					}
    //				}
    //			}
    //		}
    //		return false;
    //	}
}
