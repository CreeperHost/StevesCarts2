package vswe.stevescarts.client.models.pig;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import vswe.stevescarts.api.client.ModelCartbase;
import vswe.stevescarts.api.modules.ModuleBase;
import vswe.stevescarts.helpers.ResourceHelper;

public class ModelPigTail extends ModelCartbase
{
    private static ResourceLocation texture;

    @Override
    public ResourceLocation getResource(final ModuleBase module)
    {
        return ModelPigTail.texture;
    }

    @Override
    protected int getTextureHeight()
    {
        return 32;
    }

    public ModelPigTail()
    {
        //TODO
        //        final ModelRenderer tailanchor = new ModelRenderer(this);
        //        AddRenderer(tailanchor);
        //        tailanchor.setPos(10.0f, -4.0f, 0.0f);
        //        tailanchor.yRot = 1.5707964f;
        //        final ModelRenderer tail1 = new ModelRenderer(this, 0, 0);
        //        fixSize(tail1);
        //        tailanchor.addChild(tail1);
        //        tail1.addBox(-1.5f, -0.5f, -0.0f, 3, 1, 1, 0.0f);
        //        tail1.setPos(0.0f, 0.0f, 0.0f);
        //        final ModelRenderer tail2 = new ModelRenderer(this, 0, 0);
        //        fixSize(tail2);
        //        tailanchor.addChild(tail2);
        //        tail2.addBox(-0.5f, -1.5f, -0.0f, 1, 3, 1, 0.0f);
        //        tail2.setPos(2.0f, -2.0f, 0.0f);
        //        final ModelRenderer tail3 = new ModelRenderer(this, 0, 0);
        //        fixSize(tail3);
        //        tailanchor.addChild(tail3);
        //        tail3.addBox(-1.0f, -0.5f, -0.0f, 2, 1, 1, 0.0f);
        //        tail3.setPos(0.5f, -4.0f, 0.0f);
        //        final ModelRenderer tail4 = new ModelRenderer(this, 0, 0);
        //        fixSize(tail4);
        //        tailanchor.addChild(tail4);
        //        tail4.addBox(-0.5f, -0.5f, -0.0f, 1, 1, 1, 0.0f);
        //        tail4.setPos(-1.0f, -3.0f, 0.0f);
        //        final ModelRenderer tail5 = new ModelRenderer(this, 0, 0);
        //        fixSize(tail5);
        //        tailanchor.addChild(tail5);
        //        tail5.addBox(-0.5f, -0.5f, -0.0f, 1, 1, 1, 0.0f);
        //        tail5.setPos(0.0f, -2.0f, 0.0f);
    }

    static
    {
        ModelPigTail.texture = ResourceHelper.getResource("/models/pigtailModel.png");
    }

    @Override
    public void renderToBuffer(@NotNull PoseStack p_225598_1_, @NotNull VertexConsumer p_225598_2_, int p_225598_3_, int p_225598_4_, float p_225598_5_, float p_225598_6_, float p_225598_7_, float p_225598_8_)
    {
        super.renderToBuffer(p_225598_1_, p_225598_2_, p_225598_3_, p_225598_4_, p_225598_5_, p_225598_6_, p_225598_7_, p_225598_8_);
    }
}
