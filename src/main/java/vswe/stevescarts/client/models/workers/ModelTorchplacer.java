package vswe.stevescarts.client.models.workers;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.util.ResourceLocation;
import vswe.stevescarts.client.models.ModelCartbase;
import vswe.stevescarts.helpers.ResourceHelper;
import vswe.stevescarts.modules.ModuleBase;
import vswe.stevescarts.modules.workers.ModuleTorch;

public class ModelTorchplacer extends ModelCartbase
{
    private static ResourceLocation texture;
    ModelRenderer[] torches1;
    ModelRenderer[] torches2;

    @Override
    public ResourceLocation getResource(final ModuleBase module)
    {
        return ModelTorchplacer.texture;
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

    public ModelTorchplacer()
    {
        super();
        torches1 = createSide(false);
        torches2 = createSide(true);
    }

    private ModelRenderer[] createSide(final boolean opposite)
    {
        final ModelRenderer anchor = new ModelRenderer(this, 0, 0);
        AddRenderer(anchor);
        if (opposite)
        {
            anchor.yRot = 3.1415927f;
        }
        final ModelRenderer base = new ModelRenderer(this, 0, 0);
        anchor.addChild(base);
        fixSize(base);
        base.addBox(-7.0f, -2.0f, -1.0f, 14, 4, 2, 0.0f);
        base.setPos(0.0f, -2.0f, -9.0f);
        final ModelRenderer[] torches = new ModelRenderer[3];
        for (int i = -1; i <= 1; ++i)
        {
            final ModelRenderer torchHolder = new ModelRenderer(this, 0, 6);
            base.addChild(torchHolder);
            fixSize(torchHolder);
            torchHolder.addBox(-1.0f, -1.0f, -0.5f, 2, 2, 1, 0.0f);
            torchHolder.setPos(i * 4, 0.0f, -1.5f);
            final ModelRenderer torch = new ModelRenderer(this, 0, 9);
            torchHolder.addChild(torches[i + 1] = torch);
            fixSize(torch);
            torch.addBox(-1.0f, -5.0f, -1.0f, 2, 10, 2, 0.0f);
            torch.setPos(0.0f, 0.0f, -1.5f);
        }
        return torches;
    }

    @Override
    public void applyEffects(final ModuleBase module, MatrixStack matrixStack, IRenderTypeBuffer rtb, final float yaw, final float pitch, final float roll)
    {
        final int torches = (module == null) ? 7 : ((ModuleTorch) module).getTorches();
        for (int i = 0; i < 3; ++i)
        {
            final boolean isTorch = (torches & 1 << i) != 0x0;
            //			torches1[i].isHidden = !isTorch;
            //			torches2[2 - i].isHidden = !isTorch;
        }
    }

    static
    {
        ModelTorchplacer.texture = ResourceHelper.getResource("/models/torchModel.png");
    }
}
