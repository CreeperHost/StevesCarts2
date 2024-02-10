package vswe.stevescarts.helpers;

import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SmeltingRecipe;
import net.minecraft.world.level.Level;
import net.minecraft.world.ticks.ContainerSingleItem;

import java.util.Optional;

/**
 * Created by brandon3055 on 10/02/2024
 */
//TODO, Replace the broken RecipeHelper in Poly
public class RecipeHelper {

    public static Optional<RecipeHolder<SmeltingRecipe>> findSmeltRecipe(ItemStack stack, Level level) {
        return findRecipe(RecipeType.SMELTING, new DummySingleItem(stack), level);
    }

    public static <C extends Container, T extends Recipe<C>> Optional<RecipeHolder<T>> findRecipe(RecipeType<T> type, C container, Level level) {
        return level.getRecipeManager().getRecipeFor(type, container, level);
    }

    private static class DummySingleItem implements ContainerSingleItem {
        private final ItemStack stack;

        public DummySingleItem(ItemStack stack) {
            this.stack = stack;
        }

        @Override
        public ItemStack getItem(int p_18941_) { return stack; }

        @Override
        public ItemStack removeItem(int p_18942_, int p_18943_) { return ItemStack.EMPTY; }

        @Override
        public void setItem(int p_18944_, ItemStack p_18945_) {}

        @Override
        public void setChanged() {}

        @Override
        public boolean stillValid(Player p_18946_) { return true; }
    }

}
