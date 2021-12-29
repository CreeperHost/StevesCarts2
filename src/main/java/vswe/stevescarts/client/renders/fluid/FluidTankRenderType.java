package vswe.stevescarts.client.renders.fluid;

import net.minecraft.client.gui.screen.inventory.InventoryScreen;
import net.minecraft.client.renderer.RenderState;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.container.PlayerContainer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.loading.moddiscovery.ModInfo;

public class FluidTankRenderType extends RenderType
{
    private static final AlphaState ALPHA = new RenderState.AlphaState(0.1F);

    private FluidTankRenderType(String nameIn, VertexFormat formatIn, int drawModeIn, int bufferSizeIn, boolean useDelegateIn, boolean needsSortingIn, Runnable setupTaskIn, Runnable clearTaskIn)
    {
        super(nameIn, formatIn, drawModeIn, bufferSizeIn, useDelegateIn, needsSortingIn, setupTaskIn, clearTaskIn);
    }

    public static final RenderType RESIZABLE = create( "stevescarts:resizable_cuboid", DefaultVertexFormats.POSITION_COLOR_TEX_LIGHTMAP, 7,
            256, true, false, RenderType.State.builder()
                    .setTextureState(new RenderState.TextureState(PlayerContainer.BLOCK_ATLAS, false, false))
                    .setCullState(NO_CULL)
                    .setLightmapState(LIGHTMAP)
                    .setWriteMaskState(COLOR_WRITE)
                    .setLightmapState(LIGHTMAP)
                    .createCompositeState(true));
}
