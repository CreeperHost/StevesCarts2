package vswe.stevescarts.client.models.engines;

import net.minecraft.resources.ResourceLocation;
import vswe.stevescarts.helpers.ResourceHelper;
import vswe.stevescarts.modules.ModuleBase;

public class ModelEngineFrame extends ModelEngineBase
{
    private static ResourceLocation texture;

    @Override
    public ResourceLocation getResource(final ModuleBase module)
    {
        return ModelEngineFrame.texture;
    }

    @Override
    protected int getTextureWidth()
    {
        return 8;
    }

    @Override
    protected int getTextureHeight()
    {
        return 8;
    }

    public ModelEngineFrame()
    {
        //TODO
//        final ModelRenderer left = new ModelRenderer(this, 0, 0);
//        anchor.addChild(left);
//        fixSize(left);
//        left.addBox(-0.5f, -2.5f, -0.5f, 1, 5, 1, 0.0f);
//        left.setPos(-4.0f, 0.0f, 0.0f);
//        final ModelRenderer right = new ModelRenderer(this, 0, 0);
//        anchor.addChild(right);
//        fixSize(right);
//        right.addBox(-0.5f, -2.5f, -0.5f, 1, 5, 1, 0.0f);
//        right.setPos(4.0f, 0.0f, 0.0f);
//        final ModelRenderer top = new ModelRenderer(this, 4, 0);
//        anchor.addChild(top);
//        fixSize(top);
//        top.addBox(-0.5f, -3.5f, -0.5f, 1, 7, 1, 0.0f);
//        top.setPos(0.0f, -3.0f, 0.0f);
//        top.zRot = 1.5707964f;
//        final ModelRenderer bot = new ModelRenderer(this, 4, 0);
//        anchor.addChild(bot);
//        fixSize(bot);
//        bot.addBox(-0.5f, -3.5f, -0.5f, 1, 7, 1, 0.0f);
//        bot.setPos(0.0f, 2.0f, 0.0f);
//        bot.zRot = 1.5707964f;
    }

    static
    {
        ModelEngineFrame.texture = ResourceHelper.getResource("/models/engineModelFrame.png");
    }
}
