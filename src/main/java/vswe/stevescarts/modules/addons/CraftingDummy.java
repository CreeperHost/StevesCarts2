package vswe.stevescarts.modules.addons;

import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;

public class CraftingDummy extends CraftingInventory
{
    private ModuleCrafter module;

    public CraftingDummy(final ModuleCrafter module)
    {
        super(null, 3, 3);
        this.module = module;
    }

    @Override
    public int getContainerSize()
    {
        return 9;
    }

    @Override
    public ItemStack getItem(int id)
    {
        return (id >= getContainerSize()) ? ItemStack.EMPTY : module.getStack(id);
    }

    @Override
    public void setItem(int id, ItemStack itemStack) {}
}
