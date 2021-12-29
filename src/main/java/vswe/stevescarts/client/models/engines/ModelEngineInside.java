package vswe.stevescarts.client.models.engines;

import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.util.ResourceLocation;
import vswe.stevescarts.helpers.ResourceHelper;
import vswe.stevescarts.modules.ModuleBase;
import vswe.stevescarts.modules.engines.ModuleCoalBase;

public class ModelEngineInside extends ModelEngineBase
{
    private static ResourceLocation[] textures;

    @Override
    public ResourceLocation getResource(final ModuleBase module)
    {
        final int index = (module == null) ? 0 : ((ModuleCoalBase) module).getFireIndex();
        return ModelEngineInside.textures[index];
    }

    @Override
    protected int getTextureWidth()
    {
        return 8;
    }

    @Override
    protected int getTextureHeight()
    {
        return 4;
    }

    public ModelEngineInside()
    {
        final ModelRenderer back = new ModelRenderer(this, 0, 0);
        anchor.addChild(back);
        fixSize(back);
        back.addBox(-3.5f, -2.0f, 0.0f, 7, 4, 0, 0.0f);
        back.setPos(0.0f, -0.5f, 0.3f);
    }

    static
    {
        (ModelEngineInside.textures = new ResourceLocation[5])[0] = ResourceHelper.getResource("/models/engineModelBack.png");
        for (int i = 1; i < ModelEngineInside.textures.length; ++i)
        {
            ModelEngineInside.textures[i] = ResourceHelper.getResource("/models/engineModelFire" + i + ".png");
        }
    }
}
