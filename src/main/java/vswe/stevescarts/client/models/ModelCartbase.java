package vswe.stevescarts.client.models;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.model.Model;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.util.ResourceLocation;
import vswe.stevescarts.modules.ModuleBase;

import java.util.ArrayList;

public abstract class ModelCartbase extends Model
{
    protected final byte cartLength = 20;
    protected final byte cartHeight = 8;
    protected final byte cartWidth = 16;
    protected final byte cartOnGround = 4;
    private ArrayList<ModelRenderer> renderers;
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
    public void renderToBuffer(MatrixStack p_225598_1_, IVertexBuilder p_225598_2_, int p_225598_3_, int p_225598_4_, float p_225598_5_, float p_225598_6_, float p_225598_7_, float p_225598_8_)
    {
        for (ModelRenderer renderer : renderers)
        {
            Minecraft.getInstance().getTextureManager().bind(new ResourceLocation("textures/entity/minecart.png"));
            renderer.render(p_225598_1_, p_225598_2_, p_225598_3_, p_225598_4_, p_225598_5_, p_225598_6_, p_225598_7_, p_225598_8_);
        }
    }

    public void render(MatrixStack matrixStack, final ModuleBase module, final float yaw, final float pitch, final float roll, final float mult, final float partialtime)
    {
        //		final ResourceLocation resource = getResource(module);
        //		if (resource == null) {
        //			return;
        //		}
        //		ResourceHelper.bindResource(resource);
        //		applyEffects(module, yaw, pitch, roll, partialtime);
    }

    public void applyEffects(final ModuleBase module, MatrixStack matrixStack, IRenderTypeBuffer rtb, final float yaw, final float pitch, final float roll, final float partialtime)
    {
        applyEffects(module, matrixStack, rtb, yaw, pitch, roll);
    }

    public void applyEffects(final ModuleBase module, MatrixStack matrixStack, IRenderTypeBuffer rtb, final float yaw, final float pitch, final float roll)
    {
    }

    protected void AddRenderer(final ModelRenderer renderer)
    {
        renderers.add(fixSize(renderer));
    }

    public ModelRenderer fixSize(final ModelRenderer renderer)
    {
        return renderer.setTexSize(getTextureWidth(), getTextureHeight());
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
