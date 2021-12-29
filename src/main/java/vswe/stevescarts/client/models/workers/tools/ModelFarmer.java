package vswe.stevescarts.client.models.workers.tools;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.util.ResourceLocation;
import vswe.stevescarts.client.models.ModelCartbase;
import vswe.stevescarts.modules.ModuleBase;
import vswe.stevescarts.modules.workers.tools.ModuleFarmer;

public class ModelFarmer extends ModelCartbase
{
    private ModelRenderer mainAnchor;
    private ModelRenderer anchor;
    private ModelRenderer[] outers;
    private ResourceLocation resource;

    @Override
    public ResourceLocation getResource(final ModuleBase module)
    {
        return resource;
    }

    @Override
    protected int getTextureWidth()
    {
        return 128;
    }

    @Override
    public float extraMult()
    {
        return 0.5f;
    }

    public ModelFarmer(final ResourceLocation resource)
    {
        this.resource = resource;
        AddRenderer(mainAnchor = new ModelRenderer(this));
        mainAnchor.setPos(-18.0f, 4.0f, 0.0f);
        for (int i = -1; i <= 1; i += 2)
        {
            final ModelRenderer smallarm = new ModelRenderer(this, 26, 23);
            mainAnchor.addChild(smallarm);
            fixSize(smallarm);
            smallarm.addBox(-1.0f, -1.0f, -1.0f, 8, 2, 2, 0.0f);
            smallarm.setPos(0.0f, 0.0f, i * 17);
        }
        final ModelRenderer mainarm = new ModelRenderer(this, 0, 37);
        mainAnchor.addChild(mainarm);
        fixSize(mainarm);
        mainarm.addBox(-30.0f, -2.0f, -2.0f, 60f, 4f, 4f, 0.0f);
        mainarm.setPos(8.0f, 0.0f, 0.0f);
        mainarm.yRot = 1.5707964f;
        for (int j = -1; j <= 1; j += 2)
        {
            final ModelRenderer extra = new ModelRenderer(this, 26, 27);
            mainAnchor.addChild(extra);
            fixSize(extra);
            extra.addBox(-2.5f, -2.5f, -1.0f, 5, 5, 2, 0.0f);
            extra.setPos(8.0f, 0.0f, j * 30);
            final ModelRenderer bigarm = new ModelRenderer(this, 26, 17);
            mainAnchor.addChild(bigarm);
            fixSize(bigarm);
            bigarm.addBox(-1.0f, -2.0f, -1.0f, 16, 4, 2, 0.0f);
            bigarm.setPos(8.0f, 0.0f, j * 32);
        }
        anchor = new ModelRenderer(this);
        mainAnchor.addChild(anchor);
        anchor.setPos(22.0f, 0.0f, 0.0f);
        final float start = -1.5f;
        final float end = 1.5f;
        for (float k = -1.5f; k <= 1.5f; ++k)
        {
            for (int l = 0; l < 6; ++l)
            {
                final ModelRenderer side = new ModelRenderer(this, 0, 0);
                anchor.addChild(side);
                fixSize(side);
                side.addBox(-5.0f, -8.8f, -1.0f, 10, 4, 2, 0.0f);
                side.setPos(0.0f, 0.0f, k * 20.0f + l % 2 * 0.005f);
                side.zRot = l * 6.2831855f / 6.0f;
            }
            if (k == start || k == end)
            {
                final ModelRenderer sidecenter = new ModelRenderer(this, 0, 12);
                anchor.addChild(sidecenter);
                fixSize(sidecenter);
                sidecenter.addBox(-6.0f, -6.0f, -0.5f, 12, 12, 1, 0.0f);
                sidecenter.setPos(0.0f, 0.0f, k * 20.0f);
            }
            else
            {
                for (int l = 0; l < 3; ++l)
                {
                    final ModelRenderer sidecenter2 = new ModelRenderer(this, 26, 12);
                    anchor.addChild(sidecenter2);
                    fixSize(sidecenter2);
                    sidecenter2.addBox(-1.0f, -2.0f, -0.5f, 8, 4, 1, 0.0f);
                    sidecenter2.setPos(0.0f, 0.0f, k * 20.0f);
                    sidecenter2.zRot = (l + 0.25f) * 6.2831855f / 3.0f;
                }
            }
        }
        for (int m = 0; m < 6; ++m)
        {
            final ModelRenderer middle = new ModelRenderer(this, 0, 6);
            anchor.addChild(middle);
            fixSize(middle);
            middle.addBox(-30.0f, -1.7f, -1.0f, 60, 2, 2, 0.0f);
            middle.setPos(0.0f, 0.0f, m % 2 * 0.005f);
            middle.xRot = m * 6.2831855f / 6.0f;
            middle.yRot = 1.5707964f;
        }
        outers = new ModelRenderer[6];
        for (int m = 0; m < 6; ++m)
        {
            final ModelRenderer nailAnchor = new ModelRenderer(this);
            anchor.addChild(nailAnchor);
            nailAnchor.xRot = nailRot(m);
            nailAnchor.yRot = 1.5707964f;
            final ModelRenderer outer = new ModelRenderer(this, 0, 10);
            nailAnchor.addChild(outer);
            fixSize(outer);
            outer.addBox(-30.0f, -0.5f, -0.5f, 60, 1, 1, 0.0f);
            outer.setPos(0.0f, -8.8f, 0.0f);
            outer.xRot = 3.1415927f;
            outers[m] = outer;
            for (int j2 = -13; j2 <= 13; ++j2)
            {
                if (Math.abs(j2) > 6 || Math.abs(j2) < 4)
                {
                    final ModelRenderer nail = new ModelRenderer(this, 44, 13);
                    outer.addChild(nail);
                    fixSize(nail);
                    nail.addBox(-0.5f, -1.5f, -0.5f, 1, 3, 1, 0.0f);
                    nail.setPos(j2 * 2, -2.0f, 0.0f);
                }
            }
        }
    }

    @Override
    public void applyEffects(final ModuleBase module, MatrixStack matrixStack, IRenderTypeBuffer rtb, final float yaw, final float pitch, final float roll)
    {

        mainAnchor.zRot = ((module == null) ? 3.926991f : (-((ModuleFarmer) module).getRigAngle()));
        final float farmAngle = (module == null) ? 0.0f : ((ModuleFarmer) module).getFarmAngle();
        anchor.zRot = -farmAngle;
        for (int i = 0; i < 6; ++i)
        {
            outers[i].xRot = farmAngle + nailRot(i);
        }
    }

    @Override
    public void renderToBuffer(MatrixStack matrixStack, IVertexBuilder p_225598_2_, int p_225598_3_, int p_225598_4_, float p_225598_5_, float p_225598_6_, float p_225598_7_, float p_225598_8_)
    {
        matrixStack.pushPose();
        matrixStack.scale(0.5F, 0.5F, 0.5F);
        super.renderToBuffer(matrixStack, p_225598_2_, p_225598_3_, p_225598_4_, p_225598_5_, p_225598_6_, p_225598_7_, p_225598_8_);
        matrixStack.popPose();
    }

    private float nailRot(final int i)
    {
        return (i + 0.5f) * 6.2831855f / 6.0f;
    }
}
