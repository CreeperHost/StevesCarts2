package vswe.stevescarts.client.guis;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class GuiBase extends AbstractContainerScreen
{
    public GuiBase(AbstractContainerMenu abstractContainerMenu, Inventory inventory, Component component)
    {
        super(abstractContainerMenu, inventory, component);
    }

    public void drawMouseOver(PoseStack matrixStack, final String str, final int x, final int y)
    {
        final String[] split = str.split("\n");
        final List<String> text = new ArrayList<>(Arrays.asList(split));
        List<FormattedCharSequence> list = new ArrayList<>();
        for (String s : text)
        {
            list.add(FormattedCharSequence.forward(s, Style.EMPTY));
        }
        renderTooltip(matrixStack, list, x, y);
    }

    public boolean inRect(final int x, final int y, final int[] coords)
    {
        return coords != null && x >= coords[0] && x < coords[0] + coords[2] && y >= coords[1] && y < coords[1] + coords[3];
    }


    @Override
    public void renderBackground(@NotNull PoseStack poseStack)
    {
        super.renderBackground(poseStack);
    }

    @Override
    public void render(@NotNull PoseStack poseStack, int x, int y, float f)
    {
        super.render(poseStack, x, y, f);
    }
}
