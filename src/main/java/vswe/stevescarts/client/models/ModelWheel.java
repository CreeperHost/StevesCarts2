package vswe.stevescarts.client.models;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.resources.ResourceLocation;
import vswe.stevescarts.helpers.ResourceHelper;
import vswe.stevescarts.modules.ModuleBase;
import vswe.stevescarts.modules.realtimers.ModuleAdvControl;

public class ModelWheel extends ModelCartbase
{
    private static ResourceLocation texture;
    private ModelPart anchor;

    @Override
    public ResourceLocation getResource(final ModuleBase module)
    {
        return ModelWheel.texture;
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

    @Override
    public float extraMult()
    {
        return 0.65f;
    }

    public ModelWheel()
    {
        //TODO
        //        AddRenderer(anchor = new ModelRenderer(this));
        //        anchor.setPos(-10.0f, -5.0f, 0.0f);
        //        final ModelRenderer top = new ModelRenderer(this, 0, 0);
        //        anchor.addChild(top);
        //        fixSize(top);
        //        top.addBox(-4.5f, -1.0f, -1.0f, 9, 2, 2, 0.0f);
        //        top.setPos(0.0f, -6.0f, 0.0f);
        //        top.yRot = -1.5707964f;
        //        final ModelRenderer topleft = new ModelRenderer(this, 0, 4);
        //        anchor.addChild(topleft);
        //        fixSize(topleft);
        //        topleft.addBox(-1.0f, -1.0f, -1.0f, 2, 2, 2, 0.0f);
        //        topleft.setPos(0.0f, -4.0f, -5.5f);
        //        topleft.yRot = -1.5707964f;
        //        final ModelRenderer topright = new ModelRenderer(this, 0, 4);
        //        anchor.addChild(topright);
        //        fixSize(topright);
        //        topright.addBox(-1.0f, -1.0f, -1.0f, 2, 2, 2, 0.0f);
        //        topright.setPos(0.0f, -4.0f, 5.5f);
        //        topright.yRot = -1.5707964f;
        //        final ModelRenderer left = new ModelRenderer(this, 0, 12);
        //        anchor.addChild(left);
        //        fixSize(left);
        //        left.addBox(-1.0f, -2.5f, -1.0f, 2, 5, 2, 0.0f);
        //        left.setPos(0.0f, -0.5f, -7.5f);
        //        left.yRot = -1.5707964f;
        //        final ModelRenderer right = new ModelRenderer(this, 0, 12);
        //        anchor.addChild(right);
        //        fixSize(right);
        //        right.addBox(-1.0f, -2.5f, -1.0f, 2, 5, 2, 0.0f);
        //        right.setPos(0.0f, -0.5f, 7.5f);
        //        right.yRot = -1.5707964f;
        //        final ModelRenderer bottomleft = new ModelRenderer(this, 0, 4);
        //        anchor.addChild(bottomleft);
        //        fixSize(bottomleft);
        //        bottomleft.addBox(-1.0f, -1.0f, -1.0f, 2, 2, 2, 0.0f);
        //        bottomleft.setPos(0.0f, 3.0f, -5.5f);
        //        bottomleft.yRot = -1.5707964f;
        //        final ModelRenderer bottomright = new ModelRenderer(this, 0, 4);
        //        anchor.addChild(bottomright);
        //        fixSize(bottomright);
        //        bottomright.addBox(-1.0f, -1.0f, -1.0f, 2, 2, 2, 0.0f);
        //        bottomright.setPos(0.0f, 3.0f, 5.5f);
        //        bottomright.yRot = -1.5707964f;
        //        final ModelRenderer bottominnerleft = new ModelRenderer(this, 0, 4);
        //        anchor.addChild(bottominnerleft);
        //        fixSize(bottominnerleft);
        //        bottominnerleft.addBox(-1.0f, -1.0f, -1.0f, 2, 2, 2, 0.0f);
        //        bottominnerleft.setPos(0.0f, 5.0f, -3.5f);
        //        bottominnerleft.yRot = -1.5707964f;
        //        final ModelRenderer bottominnerright = new ModelRenderer(this, 0, 4);
        //        anchor.addChild(bottominnerright);
        //        fixSize(bottominnerright);
        //        bottominnerright.addBox(-1.0f, -1.0f, -1.0f, 2, 2, 2, 0.0f);
        //        bottominnerright.setPos(0.0f, 5.0f, 3.5f);
        //        bottominnerright.yRot = -1.5707964f;
        //        final ModelRenderer bottom = new ModelRenderer(this, 0, 8);
        //        anchor.addChild(bottom);
        //        fixSize(bottom);
        //        bottom.addBox(-2.5f, -1.0f, -1.0f, 5, 2, 2, 0.0f);
        //        bottom.setPos(0.0f, 7.0f, 0.0f);
        //        bottom.yRot = -1.5707964f;
        //        final ModelRenderer middlebottom = new ModelRenderer(this, 0, 19);
        //        anchor.addChild(middlebottom);
        //        fixSize(middlebottom);
        //        middlebottom.addBox(-0.5f, -2.5f, -0.5f, 1, 5, 1, 0.0f);
        //        middlebottom.setPos(0.5f, 3.5f, 0.0f);
        //        middlebottom.yRot = -1.5707964f;
        //        final ModelRenderer middle = new ModelRenderer(this, 0, 25);
        //        anchor.addChild(middle);
        //        fixSize(middle);
        //        middle.addBox(-1.5f, -1.0f, -0.5f, 3, 2, 1, 0.0f);
        //        middle.setPos(0.5f, 0.0f, 0.0f);
        //        middle.yRot = -1.5707964f;
        //        final ModelRenderer middleleft = new ModelRenderer(this, 0, 25);
        //        anchor.addChild(middleleft);
        //        fixSize(middleleft);
        //        middleleft.addBox(-1.5f, -1.0f, -0.5f, 3, 2, 1, 0.0f);
        //        middleleft.setPos(0.5f, -1.0f, -3.0f);
        //        middleleft.yRot = -1.5707964f;
        //        final ModelRenderer middleright = new ModelRenderer(this, 0, 25);
        //        anchor.addChild(middleright);
        //        fixSize(middleright);
        //        middleright.addBox(-1.5f, -1.0f, -0.5f, 3, 2, 1, 0.0f);
        //        middleright.setPos(0.5f, -1.0f, 3.0f);
        //        middleright.yRot = -1.5707964f;
        //        final ModelRenderer innerleft = new ModelRenderer(this, 0, 28);
        //        anchor.addChild(innerleft);
        //        fixSize(innerleft);
        //        innerleft.addBox(-1.5f, -0.5f, -0.5f, 2, 1, 1, 0.0f);
        //        innerleft.setPos(0.5f, -1.5f, -5.0f);
        //        innerleft.yRot = -1.5707964f;
        //        final ModelRenderer innerright = new ModelRenderer(this, 0, 28);
        //        anchor.addChild(innerright);
        //        fixSize(innerright);
        //        innerright.addBox(-1.5f, -0.5f, -0.5f, 2, 1, 1, 0.0f);
        //        innerright.setPos(0.5f, -1.5f, 6.0f);
        //        innerright.yRot = -1.5707964f;
    }

    @Override
    public void applyEffects(final ModuleBase module, PoseStack matrixStack, VertexConsumer rtb, final float yaw, final float pitch, final float roll)
    {
        //TODO
//        anchor.xRot = ((module == null) ? 0.0f : ((ModuleAdvControl) module).getWheelAngle());
    }

    static
    {
        ModelWheel.texture = ResourceHelper.getResource("/models/wheelModel.png");
    }
}
