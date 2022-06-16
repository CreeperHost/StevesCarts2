package vswe.stevescarts.client.renders.fluid;

import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.renderer.RenderType;

public class FluidTankRenderType extends RenderType
{
    //    private static final AlphaState ALPHA = new RenderState.AlphaState(0.1F);

    private FluidTankRenderType(String nameIn, VertexFormat formatIn, VertexFormat.Mode drawModeIn, int bufferSizeIn, boolean useDelegateIn, boolean needsSortingIn, Runnable setupTaskIn, Runnable clearTaskIn)
    {
        super(nameIn, formatIn, drawModeIn, bufferSizeIn, useDelegateIn, needsSortingIn, setupTaskIn, clearTaskIn);
    }

    //    public static final RenderType RESIZABLE = create( "stevescarts:resizable_cuboid", VertexFormat.POSITION_COLOR_TEX_LIGHTMAP, 7,
    //            256, true, false, RenderType.State.builder()
    //                    .setTextureState(new RenderState.TextureState(PlayerContainer.BLOCK_ATLAS, false, false))
    //                    .setCullState(NO_CULL)
    //                    .setLightmapState(LIGHTMAP)
    //                    .setWriteMaskState(COLOR_WRITE)
    //                    .setLightmapState(LIGHTMAP)
    //                    .createCompositeState(true));
}
