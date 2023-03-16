package vswe.stevescarts.client.renders;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import vswe.stevescarts.api.client.ModelCartbase;
import vswe.stevescarts.entities.EntityMinecartModular;
import vswe.stevescarts.api.modules.ModuleBase;

public class RenderModulerCart extends EntityRenderer<EntityMinecartModular>
{
    public RenderModulerCart(EntityRendererProvider.Context context)
    {
        super(context);
    }

    @Override
    public void render(EntityMinecartModular entity, float yaw, float tickDelta, @NotNull PoseStack matrices, @NotNull MultiBufferSource vertexConsumers, int light)
    {
        if (entity.isInvisible())
        {
            return;
        }
        super.render(entity, yaw, tickDelta, matrices, vertexConsumers, light);
        matrices.pushPose();
        long l = (long) entity.getId() * 493286711L;
        l = l * l * 4392167121L + l * 98761L;
        float h = (((float) (l >> 16 & 7L) + 0.5f) / 8.0f - 0.5f) * 0.004f;
        float j = (((float) (l >> 20 & 7L) + 0.5f) / 8.0f - 0.5f) * 0.004f;
        float k = (((float) (l >> 24 & 7L) + 0.5f) / 8.0f - 0.5f) * 0.004f;
        matrices.translate(h, j, k);
        double renderX = Mth.lerp(tickDelta, entity.xOld, entity.getX());
        double renderY = Mth.lerp(tickDelta, entity.yOld, entity.getY());
        double renderZ = Mth.lerp(tickDelta, entity.zOld, entity.getZ());
        double offset = 0.3f;
        Vec3 vec3 = entity.getPos(renderX, renderY, renderZ);
        float pitch = Mth.lerp(tickDelta, entity.xRotO, entity.getXRot());
        if (vec3 != null)
        {
            Vec3 vec3d2 = entity.getPosOffs(renderX, renderY, renderZ, offset);
            Vec3 vec3d3 = entity.getPosOffs(renderX, renderY, renderZ, -offset);
            if (vec3d2 == null)
            {
                vec3d2 = vec3;
            }
            if (vec3d3 == null)
            {
                vec3d3 = vec3;
            }
            matrices.translate(vec3.x - renderX, (vec3d2.y + vec3d3.y) / 2.0 - renderY, vec3.z - renderZ);
            Vec3 vec3d4 = vec3d3.add(-vec3d2.x, -vec3d2.y, -vec3d2.z);
            if (vec3d4.length() != 0.0)
            {
                vec3d4 = vec3d4.normalize();
                yaw = (float) (Math.atan2(vec3d4.z, vec3d4.x) * 180.0 / Math.PI);
                pitch = (float) (Math.atan(vec3d4.y) * 73.0);
            }
        }
        yaw = yaw % 360;
        if (yaw < 0)
        {
            yaw += 360;
        }
        yaw += 360;
        double rotationYaw = (entity.getYRot() + 180) % 360;
        if (rotationYaw < 0)
        {
            rotationYaw = rotationYaw + 360;
        }
        rotationYaw = rotationYaw + 360;

        if (Math.abs(yaw - rotationYaw) > 90)
        {
            yaw += 180;
            pitch = -pitch;
        }

        boolean flip = entity.getDeltaMovement().x > 0.0 != entity.getDeltaMovement().z > 0.0;
        if (entity.cornerFlip) {
            flip = !flip;
        }
        if (entity.getRenderFlippedYaw(yaw + (flip ? 0.0f : 180.0f))) {
            flip = !flip;
        }

        matrices.translate(0.0, 0.375, 0.0);
        matrices.mulPose(Vector3f.YP.rotationDegrees(180.0F - yaw));
        matrices.mulPose(Vector3f.ZP.rotationDegrees(-pitch));
        float damageWobbleTicks = (float) entity.getHurtTime() - tickDelta;
        float damageWobbleStrength = Mth.clamp(entity.getDamage() - tickDelta, 0, Float.MAX_VALUE);
        if (damageWobbleTicks > 0.0f)
        {
            matrices.mulPose(Vector3f.XP.rotationDegrees(Mth.sin(damageWobbleTicks) * damageWobbleTicks * damageWobbleStrength / 10.0f * entity.getHurtDir()));
        }

        yaw += (flip ? 0.0f : 180.0f);
        matrices.mulPose(Vector3f.YP.rotationDegrees(flip ? 0.0f : 180.0f));

        matrices.scale(-1.0f, -1.0f, 1.0f);
        if(entity.getModules() != null)
        {
            for (ModuleBase module : entity.getModules())
            {
                if (module.haveModels())
                {
                    for (ModelCartbase model : module.getModels())
                    {
                        if (model.getRenderType(module) != null)
                        {
                            //TODO Yaw is supposed to be radians. May want to change this at some point.
                            // Why do we even need applyEffects? It seems it was added so that Model#renderToBuffer can be used. But SC uses its own ModelCartbase so we can just add out own render method.
                            model.applyEffects(module, matrices, vertexConsumers, yaw, pitch, 0);
                            model.renderToBuffer(matrices, vertexConsumers.getBuffer(model.getRenderType(module)), light, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);

                        }
                    }
                }
            }
        }
        matrices.popPose();
    }

    @Override
    public @NotNull ResourceLocation getTextureLocation(@NotNull EntityMinecartModular p_114482_)
    {
        return new ResourceLocation("textures/entity/minecart.png");
    }
}
