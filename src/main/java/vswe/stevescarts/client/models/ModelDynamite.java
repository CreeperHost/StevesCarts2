package vswe.stevescarts.client.models;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import vswe.stevescarts.helpers.ResourceHelper;
import vswe.stevescarts.modules.ModuleBase;
import vswe.stevescarts.modules.realtimers.ModuleDynamite;

public class ModelDynamite extends ModelCartbase
{
    private static ResourceLocation texture;
    private ModelPart anchor;
    private ModelPart[] dynamites;
    private float sizemult;

    @Override
    public ResourceLocation getResource(final ModuleBase module)
    {
        return ModelDynamite.texture;
    }

    @Override
    public float extraMult()
    {
        return 0.25f;
    }

    public ModelDynamite()
    {
        //TODO
//        AddRenderer(anchor = new ModelRenderer(this, 0, 0));
//        (dynamites = new ModelRenderer[54])[0] = createDynamite(0.0f, 0.0f, 0.0f);
//        dynamites[3] = createDynamite(-1.0f, 0.0f, 0.0f);
//        dynamites[4] = createDynamite(1.0f, 0.0f, 0.0f);
//        dynamites[18] = createDynamite(-2.0f, 0.0f, 0.0f);
//        dynamites[19] = createDynamite(2.0f, 0.0f, 0.0f);
//        dynamites[9] = createDynamite(-0.5f, 1.0f, 0.0f);
//        dynamites[10] = createDynamite(0.5f, 1.0f, 0.0f);
//        dynamites[24] = createDynamite(-1.5f, 1.0f, 0.0f);
//        dynamites[25] = createDynamite(1.5f, 1.0f, 0.0f);
//        dynamites[15] = createDynamite(0.0f, 2.0f, 0.0f);
//        dynamites[30] = createDynamite(-1.0f, 2.0f, 0.0f);
//        dynamites[31] = createDynamite(1.0f, 2.0f, 0.0f);
//        dynamites[36] = createDynamite(-3.0f, 0.0f, 0.0f);
//        dynamites[37] = createDynamite(3.0f, 0.0f, 0.0f);
//        dynamites[42] = createDynamite(-2.5f, 1.0f, 0.0f);
//        dynamites[43] = createDynamite(2.5f, 1.0f, 0.0f);
//        dynamites[48] = createDynamite(-2.0f, 2.0f, 0.0f);
//        dynamites[49] = createDynamite(2.0f, 2.0f, 0.0f);
//        dynamites[1] = createDynamite(0.0f, 0.0f, -1.0f);
//        dynamites[5] = createDynamite(-1.0f, 0.0f, -1.0f);
//        dynamites[7] = createDynamite(1.0f, 0.0f, -1.0f);
//        dynamites[20] = createDynamite(-2.0f, 0.0f, -1.0f);
//        dynamites[22] = createDynamite(2.0f, 0.0f, -1.0f);
//        dynamites[11] = createDynamite(-0.5f, 1.0f, -1.0f);
//        dynamites[13] = createDynamite(0.5f, 1.0f, -1.0f);
//        dynamites[26] = createDynamite(-1.5f, 1.0f, -1.0f);
//        dynamites[28] = createDynamite(1.5f, 1.0f, -1.0f);
//        dynamites[16] = createDynamite(0.0f, 2.0f, -1.0f);
//        dynamites[32] = createDynamite(-1.0f, 2.0f, -1.0f);
//        dynamites[34] = createDynamite(1.0f, 2.0f, -1.0f);
//        dynamites[38] = createDynamite(-3.0f, 0.0f, -1.0f);
//        dynamites[40] = createDynamite(3.0f, 0.0f, -1.0f);
//        dynamites[44] = createDynamite(-2.5f, 1.0f, -1.0f);
//        dynamites[46] = createDynamite(2.5f, 1.0f, -1.0f);
//        dynamites[50] = createDynamite(-2.0f, 2.0f, -1.0f);
//        dynamites[52] = createDynamite(2.0f, 2.0f, -1.0f);
//        dynamites[2] = createDynamite(0.0f, 0.0f, 1.0f);
//        dynamites[8] = createDynamite(-1.0f, 0.0f, 1.0f);
//        dynamites[6] = createDynamite(1.0f, 0.0f, 1.0f);
//        dynamites[21] = createDynamite(-2.0f, 0.0f, 1.0f);
//        dynamites[23] = createDynamite(2.0f, 0.0f, 1.0f);
//        dynamites[14] = createDynamite(-0.5f, 1.0f, 1.0f);
//        dynamites[12] = createDynamite(0.5f, 1.0f, 1.0f);
//        dynamites[29] = createDynamite(-1.5f, 1.0f, 1.0f);
//        dynamites[27] = createDynamite(1.5f, 1.0f, 1.0f);
//        dynamites[17] = createDynamite(0.0f, 2.0f, 1.0f);
//        dynamites[35] = createDynamite(-1.0f, 2.0f, 1.0f);
//        dynamites[33] = createDynamite(1.0f, 2.0f, 1.0f);
//        dynamites[41] = createDynamite(-3.0f, 0.0f, 1.0f);
//        dynamites[39] = createDynamite(3.0f, 0.0f, 1.0f);
//        dynamites[47] = createDynamite(-2.5f, 1.0f, 1.0f);
//        dynamites[45] = createDynamite(2.5f, 1.0f, 1.0f);
//        dynamites[53] = createDynamite(-2.0f, 2.0f, 1.0f);
//        dynamites[51] = createDynamite(2.0f, 2.0f, 1.0f);
    }

    private ModelPart createDynamite(final float x, final float y, final float z)
    {
        //TODO
//        final ModelRenderer dynamite = new ModelRenderer(this, 0, 0);
//        anchor.addChild(dynamite);
//        fixSize(dynamite);
//        dynamite.addBox(-8.0f, -4.0f, -4.0f, 16, 8, 8, 0.0f);
//        dynamite.setPos(x * 10.0f, y * -8.0f, z * 18.0f);
//        dynamite.yRot = 1.5707964f;
//        return dynamite;

        return null;
    }

    @Override
    public void applyEffects(final ModuleBase module, PoseStack matrixStack, VertexConsumer rtb, final float yaw, final float pitch, final float roll)
    {
        if (module == null)
        {
            //			for (int i = 0; i < dynamites.length; ++i) {
            //				dynamites[i].isHidden = false;
            //			}
        }
        else
        {
            final float size = ((ModuleDynamite) module).explosionSize();
            final float max = 44.0f;
            final float perModel = max / dynamites.length;
            for (int j = 0; j < dynamites.length; ++j)
            {
                //				dynamites[j].isHidden = (j * perModel >= size);
            }
        }
        anchor.setPos(0.0f, -24.0f / sizemult, 0.0f);
    }

    //TODO
    //	@Override
    //	public void render(final Render render, final ModuleBase module, final float yaw, final float pitch, final float roll, final float mult, final float partialtime) {
    //		if (module == null) {
    //			sizemult = 1.0f;
    //			super.render(render, module, yaw, pitch, roll, mult, partialtime);
    //		} else {
    //			final float fusemult = (float) Math.abs(Math.sin(((ModuleDynamite) module).getFuse() / ((ModuleDynamite) module).getFuseLength() * 3.141592653589793 * 6.0));
    //			GlStateManager.scale(sizemult = fusemult * 0.5f + 1.0f, sizemult, sizemult);
    //			super.render(render, module, yaw, pitch, roll, mult, partialtime);
    //			GlStateManager.disableTexture2D();
    //			GlStateManager.disableLighting();
    //			GlStateManager.enableBlend();
    //			GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_DST_ALPHA);
    //			GlStateManager.color(1.0F, 1.0F, 1.0F, fusemult);
    //			super.render(render,module,yaw,pitch,roll, mult, partialtime);
    //			GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
    //			GlStateManager.disableBlend();
    //			GlStateManager.enableLighting();
    //			GlStateManager.enableTexture2D();
    //			GlStateManager.scale(1.0f / sizemult, 1.0f / sizemult, 1.0f / sizemult);
    //		}
    //	}

    static
    {
        ModelDynamite.texture = ResourceHelper.getResource("/models/tntModel.png");
    }

    @Override
    public void renderToBuffer(@NotNull PoseStack p_225598_1_, @NotNull VertexConsumer p_225598_2_, int p_225598_3_, int p_225598_4_, float p_225598_5_, float p_225598_6_, float p_225598_7_, float p_225598_8_)
    {
        super.renderToBuffer(p_225598_1_, p_225598_2_, p_225598_3_, p_225598_4_, p_225598_5_, p_225598_6_, p_225598_7_, p_225598_8_);
    }
}
