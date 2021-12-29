package vswe.stevescarts.client.models.workers;

import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.util.ResourceLocation;
import vswe.stevescarts.client.models.ModelCartbase;
import vswe.stevescarts.helpers.ResourceHelper;
import vswe.stevescarts.modules.ModuleBase;

public class ModelTrackRemover extends ModelCartbase
{
    private static ResourceLocation texture;

    @Override
    public ResourceLocation getResource(final ModuleBase module)
    {
        return ModelTrackRemover.texture;
    }

    public ModelTrackRemover()
    {
        super();
        final ModelRenderer base = new ModelRenderer(this, 0, 0);
        AddRenderer(base);
        base.addBox(-5.0f, -5.0f, -0.5f, 10, 10, 1, 0.0f);
        base.setPos(0.0f, -5.5f, -0.0f);
        base.xRot = 1.5707964f;
        final ModelRenderer pipe = new ModelRenderer(this, 0, 11);
        AddRenderer(pipe);
        pipe.addBox(-2.5f, -2.5f, -2.5f, 6, 5, 5, 0.0f);
        pipe.setPos(0.0f, -9.5f, -0.0f);
        pipe.zRot = 1.5707964f;
        final ModelRenderer pipe2 = new ModelRenderer(this, 0, 21);
        pipe.addChild(pipe2);
        fixSize(pipe2);
        pipe2.addBox(-2.5f, -2.5f, -2.5f, 19, 5, 5, 0.0f);
        pipe2.setPos(0.005f, -0.005f, -0.005f);
        pipe2.zRot = -1.5707964f;
        final ModelRenderer pipe3 = new ModelRenderer(this, 22, 0);
        pipe2.addChild(pipe3);
        fixSize(pipe3);
        pipe3.addBox(-2.5f, -2.5f, -2.5f, 14, 5, 5, 0.0f);
        pipe3.setPos(14.005f, -0.005f, 0.005f);
        pipe3.zRot = 1.5707964f;
        final ModelRenderer end = new ModelRenderer(this, 0, 31);
        pipe3.addChild(end);
        fixSize(end);
        end.addBox(-7.0f, -11.0f, -0.5f, 14, 14, 1, 0.0f);
        end.setPos(12.0f, 0.0f, -0.0f);
        end.yRot = 1.5707964f;
    }

    static
    {
        ModelTrackRemover.texture = ResourceHelper.getResource("/models/removerModel.png");
    }
}
