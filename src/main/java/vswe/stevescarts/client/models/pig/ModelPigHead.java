package vswe.stevescarts.client.models.pig;

import net.minecraft.resources.ResourceLocation;
import vswe.stevescarts.client.models.ModelCartbase;
import vswe.stevescarts.helpers.ResourceHelper;
import vswe.stevescarts.modules.ModuleBase;

public class ModelPigHead extends ModelCartbase
{
    private static ResourceLocation texture;

    @Override
    public ResourceLocation getResource(final ModuleBase module)
    {
        return ModelPigHead.texture;
    }

    @Override
    protected int getTextureHeight()
    {
        return 32;
    }

    public ModelPigHead()
    {
        //TODO
//        final ModelRenderer head = new ModelRenderer(this, 0, 0);
//        AddRenderer(head);
//        head.addBox(-4.0f, -4.0f, -8.0f, 8, 8, 8, 0.0f);
//        head.setPos(-9.0f, -5.0f, 0.0f);
//        head.yRot = 1.5707964f;
//        head.texOffs(16, 16).addBox(-2.0f, 0.0f, -9.0f, 4, 3, 1, 0.0f);
    }

    static
    {
        ModelPigHead.texture = ResourceHelper.getResourceFromPath("/entity/pig/pig.png");
    }
}
