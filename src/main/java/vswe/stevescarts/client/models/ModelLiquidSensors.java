package vswe.stevescarts.client.models;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.resources.ResourceLocation;
import vswe.stevescarts.helpers.ResourceHelper;
import vswe.stevescarts.modules.ModuleBase;
import vswe.stevescarts.modules.addons.ModuleLiquidSensors;

public class ModelLiquidSensors extends ModelCartbase
{
    private static ResourceLocation texture;
    ModelPart[] sensor1;
    ModelPart[] sensor2;

    @Override
    public ResourceLocation getResource(final ModuleBase module)
    {
        return ModelLiquidSensors.texture;
    }

    @Override
    protected int getTextureWidth()
    {
        return 32;
    }

    @Override
    protected int getTextureHeight()
    {
        return 16;
    }

    public ModelLiquidSensors()
    {
        sensor1 = createSensor(false);
        sensor2 = createSensor(true);
    }

    private ModelPart[] createSensor(final boolean right)
    {
        //TODO
        //        final ModelRenderer base = new ModelRenderer(this, 0, 0);
        //        AddRenderer(base);
        //        base.addBox(0.5f, 2.0f, 0.5f, 1, 4, 1, 0.0f);
        //        if (right)
        //        {
        //            base.setPos(-10.0f, -11.0f, 6.0f);
        //        }
        //        else
        //        {
        //            base.setPos(-10.0f, -11.0f, -8.0f);
        //        }
        //        final ModelRenderer head = new ModelRenderer(this, 4, 0);
        //        fixSize(head);
        //        base.addChild(head);
        //        head.addBox(-2.0f, -2.0f, -2.0f, 4, 4, 4, 0.0f);
        //        head.setPos(1.0f, 0.0f, 1.0f);
        //        final ModelRenderer face = new ModelRenderer(this, 20, 0);
        //        fixSize(face);
        //        head.addChild(face);
        //        face.addBox(-0.5f, -1.0f, -1.0f, 1, 2, 2, 0.0f);
        //        face.setPos(-2.5f, 0.0f, 0.0f);
        //        final ModelRenderer[] dynamic = new ModelRenderer[4];
        //        dynamic[0] = head;
        //        for (int i = 1; i < 4; ++i)
        //        {
        //            final ModelRenderer light = new ModelRenderer(this, 20, 1 + i * 3);
        //            fixSize(light);
        //            head.addChild(light);
        //            light.addBox(-1.0f, -0.5f, -1.0f, 2, 1, 2, 0.0f);
        //            light.setPos(0.0f, -2.5f, 0.0f);
        //            dynamic[i] = light;
        //        }
        //        return dynamic;

        return null;
    }

    @Override
    public void applyEffects(final ModuleBase module, PoseStack matrixStack, VertexConsumer rtb, final float yaw, final float pitch, final float roll)
    {
        sensor1[0].yRot = ((module == null) ? 0.0f : (-((ModuleLiquidSensors) module).getSensorRotation()));
        sensor2[0].y = ((module == null) ? 0.0f : ((ModuleLiquidSensors) module).getSensorRotation());
        final int active = (module == null) ? 2 : ((ModuleLiquidSensors) module).getLight();
        for (int i = 1; i < 4; ++i)
        {
            //			sensor1[i].isHidden = (i != active);
            //			sensor2[i].isHidden = (i != active);
        }
    }

    static
    {
        ModelLiquidSensors.texture = ResourceHelper.getResource("/models/sensorModel.png");
    }
}
