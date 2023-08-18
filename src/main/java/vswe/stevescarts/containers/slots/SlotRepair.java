package vswe.stevescarts.containers.slots;

import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import vswe.stevescarts.api.slots.SlotStevesCarts;
import vswe.stevescarts.helpers.storages.TransferHandler;
import vswe.stevescarts.api.modules.template.ModuleTool;

import javax.annotation.Nonnull;

public class SlotRepair extends SlotStevesCarts implements ISpecialItemTransferValidator
{
    private final ModuleTool tool;

    public SlotRepair(final ModuleTool tool, final Container iinventory, final int i, final int j, final int k)
    {
        super(iinventory, i, j, k);
        this.tool = tool;
    }

    @Override
    public boolean isItemValidForTransfer(@Nonnull ItemStack item, final TransferHandler.TRANSFER_TYPE type)
    {
        return false;
    }

    @Override
    public boolean mayPlace(@Nonnull ItemStack itemstack)
    {
        return tool.isValidRepairMaterial(itemstack);
    }
}
