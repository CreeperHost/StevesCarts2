package vswe.stevescarts.client.renders;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.inventory.container.PlayerContainer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.FluidStack;
import vswe.stevescarts.client.FluidRenderer;
import vswe.stevescarts.client.models.ModelCartbase;
import vswe.stevescarts.entitys.EntityMinecartModular;
import vswe.stevescarts.modules.ModuleBase;

public class RendererCart<T extends EntityMinecartModular> extends EntityRenderer<T>
{
    public static final int MAX_LIGHT = 15728880;
    EntityRendererManager entityRendererManager;

    public RendererCart(EntityRendererManager entityRendererManager)
    {
        super(entityRendererManager);
        this.entityRendererManager = entityRendererManager;
        shadowRadius = 0.5F;
    }

    public static void quad(Matrix4f matrix4f, IVertexBuilder buffer, TextureAtlasSprite sprite, float width, float height, float r, float g, float b) {
        quad(matrix4f, buffer, sprite, width, height, MAX_LIGHT, r, g, b, 1.0F);
    }

    public static void quad(Matrix4f matrix4f, IVertexBuilder buffer, TextureAtlasSprite sprite, float width, float height, int light, float r, float g, float b, float a) {
        buffer.vertex(matrix4f, 0, 0, height).color(r, g, b, a).uv(sprite.getU0(), sprite.getU1()).uv2(light).endVertex();
        buffer.vertex(matrix4f, width, 0, height).color(r, g, b, a).uv(sprite.getU0(), sprite.getU1()).uv2(light).endVertex();
        buffer.vertex(matrix4f, width, 0, 0).color(r, g, b, a).uv(sprite.getU0(), sprite.getU1()).uv2(light).endVertex();
        buffer.vertex(matrix4f, 0, 0, 0).color(r, g, b, a).uv(sprite.getU0(), sprite.getU1()).uv2(light).endVertex();
    }

    @Override
    public void render(T p_225623_1_, float entityYaw, float partialTicks, MatrixStack matrixStack, IRenderTypeBuffer buffer, int packedLight)
    {
        super.render(p_225623_1_, entityYaw, partialTicks, matrixStack, buffer, packedLight);
        matrixStack.pushPose();
        long i = (long) p_225623_1_.getId() * 493286711L;
        i = i * i * 4392167121L + i * 98761L;
        float f = (((float) (i >> 16 & 7L) + 0.5F) / 8.0F - 0.5F) * 0.004F;
        float f1 = (((float) (i >> 20 & 7L) + 0.5F) / 8.0F - 0.5F) * 0.004F;
        float f2 = (((float) (i >> 24 & 7L) + 0.5F) / 8.0F - 0.5F) * 0.004F;
        matrixStack.translate((double) f, (double) f1, (double) f2);
        double d0 = MathHelper.lerp((double) partialTicks, p_225623_1_.xOld, p_225623_1_.getX());
        double d1 = MathHelper.lerp((double) partialTicks, p_225623_1_.yOld, p_225623_1_.getY());
        double d2 = MathHelper.lerp((double) partialTicks, p_225623_1_.zOld, p_225623_1_.getZ());
        double d3 = (double) 0.3F;
        Vector3d vector3d = p_225623_1_.getPos(d0, d1, d2);
        float pitch = MathHelper.lerp(partialTicks, p_225623_1_.xRotO, p_225623_1_.xRot);

        if (vector3d != null)
        {
            Vector3d vector3d1 = p_225623_1_.getPosOffs(d0, d1, d2, (double) 0.3F);
            Vector3d vector3d2 = p_225623_1_.getPosOffs(d0, d1, d2, (double) -0.3F);
            if (vector3d1 == null)
            {
                vector3d1 = vector3d;
            }

            if (vector3d2 == null)
            {
                vector3d2 = vector3d;
            }

            matrixStack.translate(vector3d.x - d0, (vector3d1.y + vector3d2.y) / 2.0D - d1, vector3d.z - d2);
            Vector3d vector3d3 = vector3d2.add(-vector3d1.x, -vector3d1.y, -vector3d1.z);
            if (vector3d3.length() != 0.0D)
            {
                vector3d3 = vector3d3.normalize();
                entityYaw = (float) (Math.atan2(vector3d3.z, vector3d3.x) * 180.0D / Math.PI);
                pitch = (float) (Math.atan(vector3d3.y) * 73.0D);
            }
        }

        entityYaw = entityYaw % 360;
        if (entityYaw < 0)
        {
            entityYaw += 360;
        }
        entityYaw += 360;

        double rotationYaw = (p_225623_1_.yRot + 180) % 360;
        if (rotationYaw < 0)
        {
            rotationYaw = rotationYaw + 360;
        }
        rotationYaw = rotationYaw + 360;

        if (Math.abs(entityYaw - rotationYaw) > 90)
        {
            entityYaw += 180;
            pitch = -pitch;
        }

        matrixStack.translate(0.0D, 0.375D, 0.0D);
        matrixStack.mulPose(Vector3f.YP.rotationDegrees(180.0F - entityYaw));
        matrixStack.mulPose(Vector3f.ZP.rotationDegrees(-pitch));
        float f5 = (float) p_225623_1_.getHurtTime() - partialTicks;
        float f6 = p_225623_1_.getDamage() - partialTicks;
        if (f6 < 0.0F)
        {
            f6 = 0.0F;
        }

        if (f5 > 0.0F)
        {
            matrixStack.mulPose(Vector3f.XP.rotationDegrees(MathHelper.sin(f5) * f5 * f6 / 10.0F * (float) p_225623_1_.getHurtDir()));
        }

        matrixStack.scale(-1.0F, -1.0F, 1.0F);
        IVertexBuilder ivertexbuilder = buffer.getBuffer(RenderType.entitySolid(this.getTextureLocation(p_225623_1_)));
        EntityMinecartModular cart = p_225623_1_;
        if (cart.getModules() != null)
        {
            for (final ModuleBase module : cart.getModules())
            {
                if (module.haveModels())
                {
                    for (final ModelCartbase model : module.getModels())
                    {
                        if (model.getRenderType(module) != null)
                        {
                            buffer.getBuffer(model.getRenderType(module));
                            model.renderToBuffer(matrixStack, ivertexbuilder, packedLight, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
                            model.applyEffects(module, matrixStack, buffer, entityYaw, 1.0F, 1.0F, packedLight);
                        }
                    }
                }
            }
        }
        matrixStack.popPose();
    }

    private static final ResourceLocation MINECART_LOCATION = new ResourceLocation("textures/entity/minecart.png");

    @Override
    public ResourceLocation getTextureLocation(T p_110775_1_)
    {
        return MINECART_LOCATION;
    }
}
