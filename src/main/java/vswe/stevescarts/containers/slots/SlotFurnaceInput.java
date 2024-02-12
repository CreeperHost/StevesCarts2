package vswe.stevescarts.containers.slots;

import net.minecraft.client.Minecraft;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.ticks.ContainerSingleItem;
import vswe.stevescarts.helpers.RecipeHelper;

import javax.annotation.Nonnull;

public class SlotFurnaceInput extends SlotFake {
    public SlotFurnaceInput(final Container iinventory, final int i, final int j, final int k) {
        super(iinventory, i, j, k);
    }

    @Override
    public boolean mayPlace(@Nonnull ItemStack itemstack) {
        Level level = Minecraft.getInstance().level;
        return RecipeHelper.findSmeltRecipe(itemstack, level).isPresent();
    }

}
