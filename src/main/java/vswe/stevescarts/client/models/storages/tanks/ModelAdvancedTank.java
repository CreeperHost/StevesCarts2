package vswe.stevescarts.client.models.storages.tanks;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import vswe.stevescarts.client.models.ModelCartbase;
import vswe.stevescarts.helpers.ResourceHelper;
import vswe.stevescarts.modules.ModuleBase;

public class ModelAdvancedTank extends ModelCartbase
{
    private static ResourceLocation texture;

    @Override
    public RenderType getRenderType(ModuleBase moduleBase)
    {
        return RenderType.entityCutout(getResource(moduleBase));
    }

    @Override
    public ResourceLocation getResource(final ModuleBase module)
    {
        return ModelAdvancedTank.texture;
    }

    public ModelAdvancedTank()
    {
        super();
        //TODO
//        for (int i = 0; i < 2; ++i)
//        {
//            final ModelRenderer tankside = new ModelRenderer(this, 0, 13);
//            AddRenderer(tankside);
//            tankside.addBox(-8.0f, -6.5f, -0.5f, 16, 13, 1, 0.0f);
//            tankside.setPos(0.0f, -4.5f, -5.5f + i * 11);
//            final ModelRenderer tanktopbot = new ModelRenderer(this, 0, 0);
//            AddRenderer(tanktopbot);
//            tanktopbot.addBox(-8.0f, -6.0f, -0.5f, 16, 12, 1, 0.0f);
//            tanktopbot.setPos(0.0f, 2.5f - i * 14, 0.0f);
//            tanktopbot.xRot = 1.5707964f;
//            final ModelRenderer tankfrontback = new ModelRenderer(this, 0, 27);
//            AddRenderer(tankfrontback);
//            tankfrontback.addBox(-5.0f, -6.5f, -0.5f, 10, 13, 1, 0.0f);
//            tankfrontback.setPos(-7.5f + i * 15, -4.5f, 0.0f);
//            tankfrontback.yRot = 1.5707964f;
//        }
    }

    static
    {
        ModelAdvancedTank.texture = ResourceHelper.getResource("/models/tankModelLarge.png");
    }

    @Override
    public void renderToBuffer(@NotNull PoseStack p_225598_1_, @NotNull VertexConsumer p_225598_2_, int p_225598_3_, int p_225598_4_, float p_225598_5_, float p_225598_6_, float p_225598_7_, float p_225598_8_)
    {
        super.renderToBuffer(p_225598_1_, p_225598_2_, p_225598_3_, p_225598_4_, p_225598_5_, p_225598_6_, p_225598_7_, p_225598_8_);
    }
}
