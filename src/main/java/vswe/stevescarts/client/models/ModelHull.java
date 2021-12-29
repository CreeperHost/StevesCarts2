package vswe.stevescarts.client.models;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.util.ResourceLocation;
import vswe.stevescarts.modules.ModuleBase;

public class ModelHull extends ModelCartbase
{
    private ResourceLocation resource;

    @Override
    public ResourceLocation getResource(final ModuleBase module)
    {
        return resource;
    }

    @Override
    protected int getTextureHeight()
    {
        return 32;
    }

    public ModelHull(final ResourceLocation resource)
    {
        this.resource = resource;
        final ModelRenderer bot = new ModelRenderer(this, 0, 0);
        final ModelRenderer front = new ModelRenderer(this, 0, 18);
        final ModelRenderer left = new ModelRenderer(this, 0, 18);
        final ModelRenderer right = new ModelRenderer(this, 0, 18);
        final ModelRenderer back = new ModelRenderer(this, 0, 18);
        ModelRenderer tmp = new ModelRenderer(this, 0, 18);
        AddRenderer(bot);
        AddRenderer(front);
        AddRenderer(left);
        AddRenderer(right);
        AddRenderer(back);
        AddRenderer(tmp);
        bot.addBox(-10.0f, -8.0f, -1.0f, 20, 16, 2, 0.0f);
        bot.setPos(0.0f, 4.0f, 0.0f);

        front.addBox(-8.0f, -9.0f, -1.0f, 16, 8, 2, 0.0f);
        front.setPos(-9.0f, 4.0f, 0.0f);

        left.addBox(-8.0f, -9.0f, -1.0f, 16, 8, 2, 0.0f);
        left.setPos(0.0f, 4.0f, -7.0f);

        right.addBox(-8.0f, -9.0f, -1.0f, 16, 8, 2, 0.0f);
        right.setPos(0.0f, 4.0f, 7.0f);

        back.addBox(-8.0f, -9.0f, -1.0f, 16, 8, 2, 0.0f);
        back.setPos(9.0f, 4.0f, 0.0f);

        bot.xRot = 1.5707964f;
        front.yRot = 4.712389f;
        left.yRot = 3.1415927f;
        back.yRot = 1.5707964f;
    }

    @Override
    public void renderToBuffer(MatrixStack p_225598_1_, IVertexBuilder p_225598_2_, int p_225598_3_, int p_225598_4_, float p_225598_5_, float p_225598_6_, float p_225598_7_, float p_225598_8_)
    {
        super.renderToBuffer(p_225598_1_, p_225598_2_, p_225598_3_, p_225598_4_, p_225598_5_, p_225598_6_, p_225598_7_, p_225598_8_);
    }

    @Override
    public void render(MatrixStack matrixStack, final ModuleBase module, final float yaw, final float pitch, final float roll, final float mult, final float partialtime)
    {
        //		if (module != null) {
        //			final float[] color = module.getCart().getColor();
        //			GlStateManager._color4f(color[0], color[1], color[2], 1.0f);
        //		}
        //		super.render(matrixStack, module, yaw, pitch, roll, mult, partialtime);
        //		GlStateManager._color4f(1.0f, 1.0f, 1.0f, 1.0f);
    }
}
