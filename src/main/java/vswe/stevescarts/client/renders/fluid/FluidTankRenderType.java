package vswe.stevescarts.client.renders.fluid;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.inventory.InventoryMenu;
import vswe.stevescarts.Constants;

public class FluidTankRenderType extends RenderType
{
    private FluidTankRenderType(String nameIn, VertexFormat formatIn, VertexFormat.Mode drawModeIn, int bufferSizeIn, boolean useDelegateIn, boolean needsSortingIn, Runnable setupTaskIn, Runnable clearTaskIn)
    {
        super(nameIn, formatIn, drawModeIn, bufferSizeIn, useDelegateIn, needsSortingIn, setupTaskIn, clearTaskIn);
    }

    public static final RenderType RESIZABLE = create(Constants.MOD_ID + ":resizable_cuboid", DefaultVertexFormat.POSITION_COLOR_TEX_LIGHTMAP, VertexFormat.Mode.QUADS,
            256, true, false, RenderType.CompositeState.builder()
                    .setShaderState(RENDERTYPE_CUTOUT_SHADER)
                    .setTextureState(new RenderStateShard.TextureStateShard(InventoryMenu.BLOCK_ATLAS, false, false))
                    .setCullState(CULL)
                    .setLightmapState(LIGHTMAP)
                    .setWriteMaskState(COLOR_WRITE)
                    .setLightmapState(LIGHTMAP)
                    .createCompositeState(true));
}
