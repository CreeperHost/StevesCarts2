package vswe.stevescarts.client.models;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import vswe.stevescarts.modules.ModuleBase;

import java.util.ArrayList;

public abstract class ModelCartbase extends Model
{
    protected final byte cartLength = 20;
    protected final byte cartHeight = 8;
    protected final byte cartWidth = 16;
    protected final byte cartOnGround = 4;
    private ArrayList<ModelPart> renderers;
    private RenderType renderType;

    public ModelCartbase()
    {
        super(RenderType::entitySolid);
        renderers = new ArrayList<>();
    }

    public RenderType getRenderType(ModuleBase moduleBase)
    {
        return RenderType.entitySolid(getResource(moduleBase));
    }

    public abstract ResourceLocation getResource(final ModuleBase p0);


    @Override
    public void renderToBuffer(PoseStack p_225598_1_, VertexConsumer p_225598_2_, int p_225598_3_, int p_225598_4_, float p_225598_5_, float p_225598_6_, float p_225598_7_, float p_225598_8_)
    {
        for (ModelPart renderer : renderers)
        {
            RenderSystem.setShaderTexture(0, new ResourceLocation("textures/entity/minecart.png"));
            renderer.render(p_225598_1_, p_225598_2_, p_225598_3_, p_225598_4_, p_225598_5_, p_225598_6_, p_225598_7_, p_225598_8_);
        }
    }

    public void render(PoseStack matrixStack, final ModuleBase module, final float yaw, final float pitch, final float roll, final float mult, final float partialtime)
    {
        //		final ResourceLocation resource = getResource(module);
        //		if (resource == null) {
        //			return;
        //		}
        //		ResourceHelper.bindResource(resource);
        //		applyEffects(module, yaw, pitch, roll, partialtime);
    }

    public void applyEffects(final ModuleBase module, PoseStack matrixStack, VertexConsumer rtb, final float yaw, final float pitch, final float roll, final float partialtime)
    {
        applyEffects(module, matrixStack, rtb, yaw, pitch, roll);
    }

    public void applyEffects(final ModuleBase module, PoseStack matrixStack, VertexConsumer rtb, final float yaw, final float pitch, final float roll)
    {
    }

    protected void AddRenderer(final ModelPart renderer)
    {
        renderers.add(fixSize(renderer));
    }

    public ModelPart fixSize(final ModelPart renderer)
    {
        return renderer;
        //TODO
//        return renderer.setTexSize(getTextureWidth(), getTextureHeight());
    }

    protected int getTextureWidth()
    {
        return 64;
    }

    protected int getTextureHeight()
    {
        return 64;
    }

    public float extraMult()
    {
        return 1.0f;
    }
}
