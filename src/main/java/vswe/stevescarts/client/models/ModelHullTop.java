package vswe.stevescarts.client.models;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.util.ResourceLocation;
import vswe.stevescarts.modules.ModuleBase;

public class ModelHullTop extends ModelCartbase
{
    private ResourceLocation resource;
    private boolean useColors;

    @Override
    public RenderType getRenderType(ModuleBase moduleBase)
    {
        return RenderType.entityCutout(getResource(moduleBase));
    }

    @Override
    public ResourceLocation getResource(final ModuleBase module)
    {
        return resource;
    }

    @Override
    protected int getTextureHeight()
    {
        return 16;
    }

    public ModelHullTop(final ResourceLocation resource)
    {
        this(resource, true);
    }

    public ModelHullTop(final ResourceLocation resource, final boolean useColors)
    {
        this.resource = resource;
        this.useColors = useColors;
        final ModelRenderer top = new ModelRenderer(this, 0, 0);
        AddRenderer(top);
        top.addBox(-8.0f, -6.0f, -1.0f, 16, 12, 2, 0.0f);
        top.setPos(0.0f, -4.0f, 0.0f);
        top.xRot = -1.5707964f;
    }

    @Override
    public void renderToBuffer(MatrixStack p_225598_1_, IVertexBuilder p_225598_2_, int p_225598_3_, int p_225598_4_, float p_225598_5_, float p_225598_6_, float p_225598_7_, float p_225598_8_)
    {
        super.renderToBuffer(p_225598_1_, p_225598_2_, p_225598_3_, p_225598_4_, p_225598_5_, p_225598_6_, p_225598_7_, p_225598_8_);
        //TODO render
    }

    //TODO
    //	@Override
    //	public void render(final Render render, final ModuleBase module, final float yaw, final float pitch, final float roll, final float mult, final float partialtime) {
    //		if (useColors && module != null) {
    //			final float[] color = module.getCart().getColor();
    //			GlStateManager.color(color[0], color[1], color[2], 1.0f);
    //		}
    //		super.render(render, module, yaw, pitch, roll, mult, partialtime);
    //		GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
    //	}
}
