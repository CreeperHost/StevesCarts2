package vswe.stevescarts.client.models;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.resources.ResourceLocation;
import vswe.stevescarts.modules.ILeverModule;
import vswe.stevescarts.modules.ModuleBase;

public class ModelLever extends ModelCartbase
{
    ModelPart lever;
    ResourceLocation resource;

    @Override
    public ResourceLocation getResource(final ModuleBase module)
    {
        return resource;
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

    public ModelLever(final ResourceLocation resource)
    {
        this.resource = resource;
        //TODO
        //        final ModelRenderer base = new ModelRenderer(this, 0, 0);
        //        AddRenderer(base);
        //        base.addBox(-2.5f, -1.5f, -0.5f, 5, 3, 1, 0.0f);
        //        base.setPos(0.0f, 2.0f, 8.5f);
        //        base.addChild(lever = new ModelRenderer(this, 0, 4));
        //        fixSize(lever);
        //        lever.addBox(-0.5f, -12.0f, -0.5f, 1, 11, 1, 0.0f);
        //        lever.setPos(0.0f, 0.0f, 0.0f);
        //        final ModelRenderer handle = new ModelRenderer(this, 4, 4);
        //        lever.addChild(handle);
        //        fixSize(handle);
        //        handle.addBox(-1.0f, -13.0f, -1.0f, 2, 2, 2, 0.0f);
        //        handle.setPos(0.0f, 0.0f, 0.0f);
    }

    @Override
    public void applyEffects(final ModuleBase module, PoseStack matrixStack, VertexConsumer rtb, final float yaw, final float pitch, final float roll)
    {
        lever.zRot = ((module == null) ? 0.0f : (0.3926991f - ((ILeverModule) module).getLeverState() * 3.1415927f / 4.0f));
    }
}
