package vswe.stevescarts.client.models;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.resources.ResourceLocation;
import vswe.stevescarts.helpers.ResourceHelper;
import vswe.stevescarts.modules.ModuleBase;
import vswe.stevescarts.modules.realtimers.ModuleSeat;

public class ModelSeat extends ModelCartbase
{
    private static ResourceLocation texture;
    ModelPart sit;

    @Override
    public ResourceLocation getResource(final ModuleBase module)
    {
        return ModelSeat.texture;
    }

    @Override
    protected int getTextureWidth()
    {
        return 32;
    }

    @Override
    protected int getTextureHeight()
    {
        return 32;
    }

    public ModelSeat()
    {
        //TODO
//        AddRenderer(sit = new ModelRenderer(this, 0, 0));
//        sit.addBox(-4.0f, -2.0f, -2.0f, 8, 4, 4, 0.0f);
//        sit.setPos(0.0f, 1.0f, 0.0f);
//        final ModelRenderer back = new ModelRenderer(this, 0, 8);
//        sit.addChild(back);
//        fixSize(back);
//        back.addBox(-4.0f, -2.0f, -1.0f, 8, 12, 2, 0.0f);
//        back.setPos(0.0f, -8.0f, 3.0f);
    }

    @Override
    public void applyEffects(final ModuleBase module, PoseStack matrixStack, VertexConsumer rtb, final float yaw, final float pitch, final float roll)
    {
        sit.yRot = 1.5707964f;

        //sit.yRot = ((module == null) ? 1.5707964f : (((ModuleSeat) module).getChairAngle() + (((ModuleSeat) module).useRelativeRender() ? 0.0f : yaw)));
    }

    static
    {
        ModelSeat.texture = ResourceHelper.getResource("/models/chairModel.png");
    }
}
