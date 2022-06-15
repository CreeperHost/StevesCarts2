package vswe.stevescarts.client.models;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import vswe.stevescarts.helpers.ResourceHelper;
import vswe.stevescarts.modules.ModuleBase;
import vswe.stevescarts.modules.addons.ModuleShield;

public class ModelShield extends ModelCartbase
{
    private static ResourceLocation texture;
    private ModelPart[][] shieldAnchors;
    private ModelPart[][] shields;

    @Override
    public ResourceLocation getResource(final ModuleBase module)
    {
        return ModelShield.texture;
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

    public ModelShield()
    {
        //TODO
//        shields = new ModelRenderer[4][5];
//        shieldAnchors = new ModelRenderer[shields.length][shields[0].length];
//        for (int i = 0; i < shields.length; ++i)
//        {
//            for (int j = 0; j < shields[i].length; ++j)
//            {
//                AddRenderer(shieldAnchors[i][j] = new ModelRenderer(this));
//                fixSize(shields[i][j] = new ModelRenderer(this, 0, 0));
//                shieldAnchors[i][j].addChild(shields[i][j]);
//                shields[i][j].addBox(-1.0f, -1.0f, -1.0f, 2, 2, 2, 0.0f);
//                shields[i][j].setPos(0.0f, 0.0f, 0.0f);
//            }
//        }
    }

    //	@Override
    //	public void render(final Render render, final ModuleBase module, final float yaw, final float pitch, final float roll, final float mult, final float partialtime) {
    //		if (render == null || module == null || ((ModuleShield) module).hasShield()) {
    //			super.render(render, module, yaw, pitch, roll, mult, partialtime);
    //		}
    //	}

    @Override
    public void applyEffects(final ModuleBase module, PoseStack matrixStack, VertexConsumer rtb, final float yaw, final float pitch, final float roll)
    {
        final float shieldAngle = (module == null) ? 0.0f : ((ModuleShield) module).getShieldAngle();
        final float shieldDistance = (module == null) ? 18.0f : ((ModuleShield) module).getShieldDistance();
        for (int i = 0; i < shields.length; ++i)
        {
            for (int j = 0; j < shields[i].length; ++j)
            {
                float a = shieldAngle + 6.2831855f * (j / (float) shields[i].length + i / (float) shields.length);
                a %= 314.1592653589793;
                shieldAnchors[i][j].yRot = a;
                shields[i][j].yRot = ((float) Math.sin(a / 5.0f) * 3.0f + (i - (shields.length - 1) / 2.0f) * 5.0f - 5.0f) * shieldDistance / 18.0f;
                shields[i][j].zRot = shieldDistance;
            }
        }
    }

    static
    {
        ModelShield.texture = ResourceHelper.getResource("/models/shieldModel.png");
    }

    @Override
    public void renderToBuffer(@NotNull PoseStack p_225598_1_, @NotNull VertexConsumer p_225598_2_, int p_225598_3_, int p_225598_4_, float p_225598_5_, float p_225598_6_, float p_225598_7_, float p_225598_8_)
    {
        super.renderToBuffer(p_225598_1_, p_225598_2_, p_225598_3_, p_225598_4_, p_225598_5_, p_225598_6_, p_225598_7_, p_225598_8_);
        //TOOD render
    }
}
