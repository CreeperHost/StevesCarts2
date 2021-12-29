package vswe.stevescarts.client.models;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.util.ResourceLocation;
import vswe.stevescarts.helpers.ResourceHelper;
import vswe.stevescarts.modules.ModuleBase;
import vswe.stevescarts.modules.realtimers.ModuleShooterAdv;

public class ModelMobDetector extends ModelCartbase
{
    private static ResourceLocation texture;
    ModelRenderer base;

    @Override
    public ResourceLocation getResource(final ModuleBase module)
    {
        return ModelMobDetector.texture;
    }

    @Override
    protected int getTextureHeight()
    {
        return 16;
    }

    public ModelMobDetector()
    {
        AddRenderer(base = new ModelRenderer(this, 0, 0));
        base.addBox(-1.0f, -2.0f, -1.0f, 2, 4, 2, 0.0f);
        base.setPos(0.0f, -14.0f, -0.0f);
        final ModelRenderer body = new ModelRenderer(this, 0, 8);
        base.addChild(body);
        fixSize(body);
        body.addBox(-2.5f, -1.5f, -0.5f, 5, 3, 1, 0.0f);
        body.setPos(0.0f, -1.5f, -1.5f);
        for (int i = 0; i < 2; ++i)
        {
            final ModelRenderer side = new ModelRenderer(this, 0, 13);
            body.addChild(side);
            fixSize(side);
            side.addBox(-2.5f, -0.5f, -0.5f, 5, 1, 1, 0.0f);
            side.setPos(0.0f, 2.0f * (i * 2 - 1), -1.0f);
        }
        for (int i = 0; i < 2; ++i)
        {
            final ModelRenderer side = new ModelRenderer(this, 12, 13);
            body.addChild(side);
            fixSize(side);
            side.addBox(-1.5f, -0.5f, -0.5f, 3, 1, 1, 0.0f);
            side.setPos(3.0f * (i * 2 - 1), 0.0f, -1.0f);
            side.zRot = 1.5707964f;
        }
        final ModelRenderer receiver = new ModelRenderer(this, 8, 0);
        body.addChild(receiver);
        fixSize(receiver);
        receiver.addBox(-0.5f, -0.5f, -0.5f, 1, 1, 1, 0.0f);
        receiver.setPos(0.0f, 0.0f, -1.0f);
        receiver.yRot = 1.5707964f;
        final ModelRenderer dot = new ModelRenderer(this, 8, 2);
        body.addChild(dot);
        fixSize(dot);
        dot.addBox(-0.5f, -0.5f, -0.5f, 1, 1, 1, 0.0f);
        dot.setPos(0.0f, 0.0f, -2.0f);
    }

    @Override
    public void applyEffects(final ModuleBase module, MatrixStack matrixStack, IRenderTypeBuffer rtb, final float yaw, final float pitch, final float roll)
    {
        base.y = ((module == null) ? 0.0f : (((ModuleShooterAdv) module).getDetectorAngle() + yaw));
    }

    static
    {
        ModelMobDetector.texture = ResourceHelper.getResource("/models/mobDetectorModel.png");
    }
}
