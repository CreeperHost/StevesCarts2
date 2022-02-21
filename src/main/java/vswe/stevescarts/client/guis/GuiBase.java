package vswe.stevescarts.client.guis;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;

public abstract class GuiBase extends AbstractContainerScreen
{
    public GuiBase(AbstractContainerMenu p_i51105_1_, Inventory p_i51105_2_, Component p_i51105_3_)
    {
        super(p_i51105_1_, p_i51105_2_, p_i51105_3_);
    }

    public void drawMouseOver(PoseStack matrixStack, final String str, final int x, final int y)
    {
        //TODO
//        final String[] split = str.split("\n");
//        final List<String> text = new ArrayList<>(Arrays.asList(split));
//        List<IReorderingProcessor> list = new ArrayList<>();
//        for (String s : text)
//        {
//            list.add(IReorderingProcessor.forward(s, Style.EMPTY));
//        }
//        renderTooltip(matrixStack, list, x, y);
    }

    public boolean inRect(final int x, final int y, final int[] coords)
    {
        return coords != null && x >= coords[0] && x < coords[0] + coords[2] && y >= coords[1] && y < coords[1] + coords[3];
    }


    @Override
    public void renderBackground(PoseStack matrixStack)
    {
        super.renderBackground(matrixStack);
    }

    @Override
    public void render(PoseStack matrixStack, int x, int y, float f)
    {
        super.render(matrixStack, x, y, f);
    }
}
