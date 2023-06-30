package vswe.stevescarts.helpers.storages;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nonnull;

public interface ITankHolder
{
    @Nonnull
    ItemStack getInputContainer(final int p0);

    void setInputContainer(final int p0, ItemStack stack);

    void addToOutputContainer(final int p0, @Nonnull ItemStack p1);

    void onFluidUpdated(final int p0);

    void drawImage(GuiGraphics guiGraphics, int tankid, AbstractContainerScreen<?> gui, TextureAtlasSprite sprite, int targetX, int targetY, int width, int height);
}
