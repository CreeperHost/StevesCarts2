package vswe.stevescarts.client.models.workers.tools;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.resources.ResourceLocation;
import vswe.stevescarts.client.models.ModelCartbase;
import vswe.stevescarts.modules.ModuleBase;
import vswe.stevescarts.modules.workers.tools.ModuleWoodcutter;

public class ModelWoodCutter extends ModelCartbase
{
    private ModelPart[] anchors;
    private ResourceLocation resource;

    @Override
    public ResourceLocation getResource(final ModuleBase module)
    {
        return resource;
    }

    @Override
    protected int getTextureWidth()
    {
        return 16;
    }

    @Override
    protected int getTextureHeight()
    {
        return 8;
    }

    public ModelWoodCutter(final ResourceLocation resource)
    {
        super();
        this.resource = resource;
        //TODO
//        anchors = new ModelRenderer[5];
//        for (int i = -2; i <= 2; ++i)
//        {
//            final ModelRenderer anchor = new ModelRenderer(this);
//            AddRenderer(anchors[i + 2] = anchor);
//            final ModelRenderer main = new ModelRenderer(this, 0, 0);
//            anchor.addChild(main);
//            fixSize(main);
//            main.addBox(-3.5f, -1.5f, -0.5f, 7, 3, 1, 0.0f);
//            main.setPos(-13.0f, 0.0f, i * 2);
//            final ModelRenderer tip = new ModelRenderer(this, 0, 4);
//            main.addChild(tip);
//            fixSize(tip);
//            tip.addBox(-0.5f, -0.5f, -0.5f, 1, 1, 1, 0.0f);
//            tip.setPos(-4.0f, 0.0f, 0.0f);
//        }
    }

    @Override
    public void applyEffects(final ModuleBase module, PoseStack matrixStack, VertexConsumer rtb, final float yaw, final float pitch, final float roll)
    {
        final float commonAngle = (module == null) ? 0.0f : ((ModuleWoodcutter) module).getCutterAngle();
        for (int i = 0; i < anchors.length; ++i)
        {
            float specificAngle;
            if (i % 2 == 0)
            {
                specificAngle = (float) Math.sin(commonAngle);
            }
            else
            {
                specificAngle = (float) Math.cos(commonAngle);
            }
            anchors[i].x = specificAngle * 1.25f;
        }
    }
}
