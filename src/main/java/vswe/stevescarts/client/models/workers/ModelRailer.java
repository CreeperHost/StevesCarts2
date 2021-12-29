package vswe.stevescarts.client.models.workers;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.util.ResourceLocation;
import vswe.stevescarts.client.models.ModelCartbase;
import vswe.stevescarts.helpers.ResourceHelper;
import vswe.stevescarts.modules.ModuleBase;
import vswe.stevescarts.modules.workers.ModuleRailer;

public class ModelRailer extends ModelCartbase
{
    private static ResourceLocation texture;
    private ModelRenderer[] rails;

    @Override
    public ResourceLocation getResource(final ModuleBase module)
    {
        return ModelRailer.texture;
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

    public ModelRailer(final int railCount)
    {
        super();
        rails = new ModelRenderer[railCount];
        for (int r = 0; r < rails.length; ++r)
        {
            final ModelRenderer railAnchor = new ModelRenderer(this);
            AddRenderer(railAnchor);
            (rails[r] = railAnchor).setPos(0.0f, (-r), 0.0f);
            final ModelRenderer rail1 = new ModelRenderer(this, 18, 0);
            fixSize(rail1);
            railAnchor.addChild(rail1);
            rail1.addBox(1.0f, 8.0f, 0.5f, 2, 16, 1, 0.0f);
            rail1.setPos(-16.0f, -6.5f, -7.0f);
            rail1.zRot = 4.712389f;
            rail1.yRot = 4.712389f;
            final ModelRenderer rail2 = new ModelRenderer(this, 24, 0);
            fixSize(rail2);
            railAnchor.addChild(rail2);
            rail2.addBox(1.0f, 8.0f, 0.5f, 2, 16, 1, 0.0f);
            rail2.setPos(-16.0f, -6.5f, 3.0f);
            rail2.zRot = 4.712389f;
            rail2.yRot = 4.712389f;
            for (int i = 0; i < 4; ++i)
            {
                final ModelRenderer railbedMiddle = new ModelRenderer(this, 0, 0);
                fixSize(railbedMiddle);
                railAnchor.addChild(railbedMiddle);
                railbedMiddle.addBox(4.0f, 1.0f, 0.5f, 8, 2, 1, 0.0f);
                railbedMiddle.setPos(-8.0f + i * 4, -6.5f, -8.0f);
                railbedMiddle.zRot = 4.712389f;
                railbedMiddle.yRot = 4.712389f;
                final ModelRenderer railbedSide1 = new ModelRenderer(this, 0, 3);
                fixSize(railbedSide1);
                railAnchor.addChild(railbedSide1);
                railbedSide1.addBox(0.5f, 1.0f, 0.5f, 1, 2, 1, 0.0f);
                railbedSide1.setPos(-8.0f + i * 4, -6.5f, -7.5f);
                railbedSide1.zRot = 4.712389f;
                railbedSide1.yRot = 4.712389f;
                final ModelRenderer railbedSide2 = new ModelRenderer(this, 0, 3);
                fixSize(railbedSide2);
                railAnchor.addChild(railbedSide2);
                railbedSide2.addBox(0.5f, 1.0f, 0.5f, 1, 2, 1, 0.0f);
                railbedSide2.setPos(-8.0f + i * 4, -6.5f, 5.5f);
                railbedSide2.zRot = 4.712389f;
                railbedSide2.yRot = 4.712389f;
            }
        }
    }

    @Override
    public void applyEffects(final ModuleBase module, MatrixStack matrixStack, IRenderTypeBuffer rtb, final float yaw, final float pitch, final float roll)
    {
        final int valid = (module == null) ? 1 : ((ModuleRailer) module).getRails();
        for (int i = 0; i < rails.length; ++i)
        {
            rails[i].yRot = ((module == null) ? 0.0f : ((ModuleRailer) module).getRailAngle(i));
            //			rails[i].isHidden = (i >= valid);
        }
    }

    static
    {
        ModelRailer.texture = ResourceHelper.getResource("/models/builderModel.png");
    }
}
