package vswe.stevescarts.modules.workers.tools;

import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import vswe.stevescarts.entities.ModularMinecart;

public class ModuleDrillGalgadorian extends ModuleDrill
{
    public ModuleDrillGalgadorian(ModularMinecart cart)
    {
        super(cart);
    }

    @Override
    protected int blocksOnTop()
    {
        return 9;
    }

    @Override
    protected int blocksOnSide()
    {
        return 4;
    }

    @Override
    protected float getTimeMult()
    {
        return 0.0f;
    }

    @Override
    public int getMaxDurability()
    {
        return 1;
    }

    @Override
    public Identifier getRepairItem() {
        return null;
    }

    @Override
    public int getRepairItemUnits(@NotNull ItemStack item)
    {
        return 0;
    }

    @Override
    public boolean useDurability()
    {
        return false;
    }

    @Override
    public int getRepairSpeed()
    {
        return 1;
    }
}
