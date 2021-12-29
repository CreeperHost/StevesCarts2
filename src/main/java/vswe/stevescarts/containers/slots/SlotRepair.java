package vswe.stevescarts.containers.slots;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import vswe.stevescarts.helpers.storages.TransferHandler;
import vswe.stevescarts.modules.workers.tools.ModuleTool;

import javax.annotation.Nonnull;

public class SlotRepair extends SlotBase implements ISpecialItemTransferValidator
{
    private ModuleTool tool;

    public SlotRepair(final ModuleTool tool, final IInventory iinventory, final int i, final int j, final int k)
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
