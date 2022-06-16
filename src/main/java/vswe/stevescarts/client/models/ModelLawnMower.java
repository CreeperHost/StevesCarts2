package vswe.stevescarts.client.models;

import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.resources.ResourceLocation;
import vswe.stevescarts.helpers.ResourceHelper;
import vswe.stevescarts.modules.ModuleBase;

import java.util.ArrayList;

public class ModelLawnMower extends ModelCartbase
{
    private static ResourceLocation texture;
    private ArrayList<ModelPart> bladepins;

    @Override
    public ResourceLocation getResource(final ModuleBase module)
    {
        return ModelLawnMower.texture;
    }

    @Override
    protected int getTextureWidth()
    {
        return 64;
    }

    @Override
    protected int getTextureHeight()
    {
        return 32;
    }

    public ModelLawnMower()
    {
        bladepins = new ArrayList<>();
        createSide(false);
        createSide(true);
    }

    private void createSide(final boolean opposite)
    {
        //TODO
//        final ModelRenderer anchor = new ModelRenderer(this, 0, 0);
//        AddRenderer(anchor);
//        if (opposite)
//        {
//            anchor.yRot = 3.1415927f;
//        }
//        final ModelRenderer base = new ModelRenderer(this, 0, 0);
//        anchor.addChild(base);
//        fixSize(base);
//        base.addBox(-11.5f, -3.0f, -1.0f, 23, 6, 2, 0.0f);
//        base.setPos(0.0f, -1.5f, -9.0f);
//        for (int i = 0; i < 2; ++i)
//        {
//            final ModelRenderer arm = new ModelRenderer(this, 0, 8);
//            base.addChild(arm);
//            fixSize(arm);
//            arm.addBox(-8.0f, -1.5f, -1.5f, 16, 3, 3, 0.0f);
//            arm.setPos(-8.25f + i * 16.5f, 0.0f, -8.0f);
//            arm.yRot = 1.5707964f;
//            final ModelRenderer arm2 = new ModelRenderer(this, 0, 14);
//            arm.addChild(arm2);
//            fixSize(arm2);
//            arm2.addBox(-1.5f, -1.5f, -1.5f, 3, 3, 3, 0.0f);
//            arm2.setPos(6.5f, 3.0f, 0.0f);
//            arm2.zRot = 1.5707964f;
//            final ModelRenderer bladepin = new ModelRenderer(this, 0, 20);
//            arm2.addChild(bladepin);
//            fixSize(bladepin);
//            bladepin.addBox(-1.0f, -0.5f, -0.5f, 2, 1, 1, 0.0f);
//            bladepin.setPos(2.5f, 0.0f, 0.0f);
//            final ModelRenderer bladeanchor = new ModelRenderer(this, 0, 0);
//            bladepin.addChild(bladeanchor);
//            bladeanchor.yRot = 1.5707964f;
//            for (int j = 0; j < 4; ++j)
//            {
//                final ModelRenderer blade = new ModelRenderer(this, 0, 22);
//                bladeanchor.addChild(blade);
//                fixSize(blade);
//                blade.addBox(-1.5f, -1.5f, -0.5f, 8, 3, 1, 0.0f);
//                blade.setPos(0.0f, 0.0f, j * 0.01f);
//                blade.zRot = 1.5707964f * (j + i * 0.5f);
//                final ModelRenderer bladetip = new ModelRenderer(this, 0, 26);
//                blade.addChild(bladetip);
//                fixSize(bladetip);
//                bladetip.addBox(6.5f, -1.0f, -0.5f, 6, 2, 1, 0.0f);
//                bladetip.setPos(0.0f, 0.0f, 0.005f);
//            }
//            bladepins.add(bladepin);
//        }
    }

    //TODO
//    @Override
//    public void applyEffects(final ModuleBase module, PoseStack matrixStack, VertexConsumer rtb, final float yaw, final float pitch, final float roll, final float partialtime)
//    {
//        super.applyEffects(module, matrixStack, rtb, yaw, pitch, roll, partialtime);
//        final float angle = (module == null) ? 0.0f : (((ModuleFlowerRemover) module).getBladeAngle() + partialtime * ((ModuleFlowerRemover) module).getBladeSpindSpeed());
//        for (int i = 0; i < bladepins.size(); ++i)
//        {
//            final ModelPart bladepin = bladepins.get(i);
//            if (i % 2 == 0)
//            {
//                bladepin.xRot = angle;
//            }
//            else
//            {
//                bladepin.xRot = -angle;
//            }
//        }
//    }

    static
    {
        ModelLawnMower.texture = ResourceHelper.getResource("/models/lawnmowerModel.png");
    }
}
