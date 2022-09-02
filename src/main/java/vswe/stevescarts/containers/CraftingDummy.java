package vswe.stevescarts.containers;

import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import vswe.stevescarts.modules.addons.ModuleCrafter;

public class CraftingDummy extends CraftingContainer
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
    public void setItem(int id, ItemStack itemStack)
    {
    }
}
