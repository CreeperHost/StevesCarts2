package vswe.stevescarts.client.models;

import net.minecraft.resources.ResourceLocation;
import vswe.stevescarts.helpers.ResourceHelper;
import vswe.stevescarts.modules.ModuleBase;

public class ModelShootingRig extends ModelCartbase
{
    private static ResourceLocation texture;

    @Override
    public ResourceLocation getResource(final ModuleBase module)
    {
        return ModelShootingRig.texture;
    }

    @Override
    protected int getTextureHeight()
    {
        return 16;
    }

    public ModelShootingRig()
    {
        //TODO
        //        final ModelRenderer base = new ModelRenderer(this, 0, 0);
        //        AddRenderer(base);
        //        base.addBox(-7.0f, -0.5f, -3.0f, 14, 1, 6, 0.0f);
        //        base.setPos(0.0f, -5.5f, -0.0f);
        //        base.yRot = 1.5707964f;
        //        final ModelRenderer pillar = new ModelRenderer(this, 0, 7);
        //        AddRenderer(pillar);
        //        pillar.addBox(-2.0f, -2.5f, -2.0f, 4, 5, 4, 0.0f);
        //        pillar.setPos(0.0f, -8.0f, -0.0f);
        //        final ModelRenderer top = new ModelRenderer(this, 16, 7);
        //        AddRenderer(top);
        //        top.addBox(-3.0f, -1.0f, -3.0f, 6, 2, 6, 0.0f);
        //        top.setPos(0.0f, -11.0f, -0.0f);
    }

    static
    {
        ModelShootingRig.texture = ResourceHelper.getResource("/models/rigModel.png");
    }
}
