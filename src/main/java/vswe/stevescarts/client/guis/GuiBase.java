package vswe.stevescarts.client.guis;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.util.IReorderingProcessor;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.Style;
import vswe.stevescarts.modules.data.ModuleData;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class GuiBase extends ContainerScreen
{
    public GuiBase(Container p_i51105_1_, PlayerInventory p_i51105_2_, ITextComponent p_i51105_3_)
    {
        super(p_i51105_1_, p_i51105_2_, p_i51105_3_);
    }

    public void drawMouseOver(MatrixStack matrixStack, final String str, final int x, final int y)
    {
        final String[] split = str.split("\n");
        final List<String> text = new ArrayList<>(Arrays.asList(split));
        List<IReorderingProcessor> list = new ArrayList<>();
        for (String s : text)
        {
            list.add(IReorderingProcessor.forward(s, Style.EMPTY));
        }
        renderTooltip(matrixStack, list, x, y);
    }

    public boolean inRect(final int x, final int y, final int[] coords)
    {
        return coords != null && x >= coords[0] && x < coords[0] + coords[2] && y >= coords[1] && y < coords[1] + coords[3];
    }


    @Override
    public void renderBackground(MatrixStack matrixStack)
    {
        super.renderBackground(matrixStack);
    }

    @Override
    public void render(MatrixStack matrixStack, int x, int y, float f)
    {
        super.render(matrixStack, x, y, f);
    }
}
