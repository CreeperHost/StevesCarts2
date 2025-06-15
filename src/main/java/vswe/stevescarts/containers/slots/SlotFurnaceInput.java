package vswe.stevescarts.containers.slots;

import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;

import javax.annotation.Nonnull;

public class SlotFurnaceInput extends SlotFake
{
    private final Level level;

    public SlotFurnaceInput(final Container iinventory, Level level, final int i, final int j, final int k)
    {
        super(iinventory, i, j, k);
        this.level = level;
    }

    @Override
    public boolean mayPlace(@Nonnull ItemStack itemstack) {
        return level.getRecipeManager().getRecipeFor(RecipeType.SMELTING, new SimpleContainer(itemstack), level).isPresent();
    }
}
