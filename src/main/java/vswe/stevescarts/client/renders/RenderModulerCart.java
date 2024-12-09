package vswe.stevescarts.client.renders;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import vswe.stevescarts.api.client.ModelCartbase;
import vswe.stevescarts.api.modules.ModuleBase;
import vswe.stevescarts.entities.ModularMinecart;
import vswe.stevescarts.entities.ModularMinecartBehavior;

import java.util.ArrayList;
import java.util.List;

public class RenderModulerCart extends EntityRenderer<ModularMinecart, RenderModulerCart.ModularCartRenderState> {
    public RenderModulerCart(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public void render(ModularCartRenderState state, PoseStack poseStack, MultiBufferSource bufferSource, int light) {
        if (state.isInvisible) {
            return;
        }

        super.render(state, poseStack, bufferSource, light);

        poseStack.pushPose();
        long offsetSeed = state.offsetSeed;
        float xOffset = (((float)(offsetSeed >> 16 & 7L) + 0.5F) / 8.0F - 0.5F) * 0.004F;
        float yOffset = (((float)(offsetSeed >> 20 & 7L) + 0.5F) / 8.0F - 0.5F) * 0.004F;
        float zOffset = (((float)(offsetSeed >> 24 & 7L) + 0.5F) / 8.0F - 0.5F) * 0.004F;
        poseStack.translate(xOffset, yOffset, zOffset);

        //New render transforms
        poseStack.mulPose(Axis.YP.rotationDegrees(state.yRot));
        poseStack.mulPose(Axis.ZP.rotationDegrees(-state.xRot));
        poseStack.translate(0.0F, 0.375F, 0.0F);

        float f3 = state.hurtTime;
        if (f3 > 0.0F) {
            poseStack.mulPose(Axis.XP.rotationDegrees(Mth.sin(f3) * f3 * state.damageTime / 10.0F * (float)state.hurtDir));
        }

        poseStack.scale(-1.0F, -1.0F, 1.0F);

        for (ModuleBase module : state.modules) {
            if (!module.haveModels()) continue;
            for (ModelCartbase model : module.getModels()) {
                if (model.getRenderType(module) == null) continue;
                model.applyEffects(module, poseStack, bufferSource, state.yRot, state.xRot, 0);
                model.renderToBuffer(poseStack, bufferSource.getBuffer(model.getRenderType(module)), light, OverlayTexture.NO_OVERLAY, 0xFFFFFFFF);
            }
        }

        poseStack.popPose();

//        renderLabels(entity, poseStack, bufferSource, light);


//
//        poseStack.pushPose();
//
//        //Slight random position offset?
//        long randomIsh = (long) entity.getId() * 493286711L;
//        randomIsh = randomIsh * randomIsh * 4392167121L + randomIsh * 98761L;
//        float h = (((float) (randomIsh >> 16 & 7L) + 0.5f) / 8.0f - 0.5f) * 0.004f;
//        float j = (((float) (randomIsh >> 20 & 7L) + 0.5f) / 8.0f - 0.5f) * 0.004f;
//        float k = (((float) (randomIsh >> 24 & 7L) + 0.5f) / 8.0f - 0.5f) * 0.004f;
//        poseStack.translate(h, j, k);
//
//        double renderX = Mth.lerp(state.partialTick, entity.xOld, entity.getX());
//        double renderY = Mth.lerp(state.partialTick, entity.yOld, entity.getY());
//        double renderZ = Mth.lerp(state.partialTick, entity.zOld, entity.getZ());
//        double offset = 0.3f;
////        Vec3 vec3 = entity.getPos(renderX, renderY, renderZ);
//        Vec3 vec3 = null;//entity.getPosition(state.partialTick);
//        float pitch = Mth.lerp(state.partialTick, entity.xRotO, entity.getXRot());
//        if (vec3 != null) {
//            Vec3 vec3d2 = null;//entity.getPosOffs(renderX, renderY, renderZ, offset);
//            Vec3 vec3d3 = null;//entity.getPosOffs(renderX, renderY, renderZ, -offset);
//            if (vec3d2 == null) vec3d2 = vec3;
//            if (vec3d3 == null) vec3d3 = vec3;
//
//            poseStack.translate(vec3.x - renderX, (vec3d2.y + vec3d3.y) / 2.0 - renderY, vec3.z - renderZ);
//            Vec3 vec3d4 = vec3d3.add(-vec3d2.x, -vec3d2.y, -vec3d2.z);
//            if (vec3d4.length() != 0.0) {
//                vec3d4 = vec3d4.normalize();
//                yaw = (float) (Math.atan2(vec3d4.z, vec3d4.x) * 180.0 / Math.PI);
//                pitch = (float) (Math.atan(vec3d4.y) * 73.0);
//            }
//        }


//        yaw = yaw % 360;
//        if (yaw < 0) {
//            yaw += 360;
//        }
//        yaw += 360;
//        double rotationYaw = (entity.getYRot() + 180) % 360;
//        if (rotationYaw < 0) {
//            rotationYaw = rotationYaw + 360;
//        }
//        rotationYaw = rotationYaw + 360;

//        if (Math.abs(yaw - rotationYaw) > 90) {
//            pitch = -pitch;
//        }

//        boolean flip = entity.getDeltaMovement().x > 0.0 != entity.getDeltaMovement().z > 0.0;
//        if (entity.cornerFlip) {
//            flip = !flip;
//        }
//        if (entity.getRenderFlippedYaw(yaw + (flip ? 0.0f : 180.0f))) {
//            flip = !flip;
//        }

//        poseStack.translate(0.0, 0.375, 0.0);
//        poseStack.mulPose(Axis.YP.rotationDegrees(180.0F - yaw));
//        poseStack.mulPose(Axis.ZP.rotationDegrees(-pitch));
//        float damageWobbleTicks = (float) entity.getHurtTime() - state.partialTick;
//        float damageWobbleStrength = Mth.clamp(entity.getDamage() - state.partialTick, 0, Float.MAX_VALUE);
//        if (damageWobbleStrength < 0.0F) {
//            damageWobbleStrength = 0.0F;
//        }
//        if (damageWobbleTicks > 0.0f) {
//            poseStack.mulPose(Axis.XP.rotationDegrees(Mth.sin(damageWobbleTicks) * damageWobbleTicks * damageWobbleStrength / 10.0f * entity.getHurtDir()));
//        }
//
//        yaw += (flip ? 0.0f : 180.0f);
//        poseStack.mulPose(Axis.YP.rotationDegrees(flip ? 0.0f : 180.0f));
//
//        poseStack.scale(-1.0f, -1.0f, 1.0f);
//        if (entity.getModules() != null) {
//            for (ModuleBase module : entity.getModules()) {
//                if (module.haveModels()) {
//                    for (ModelCartbase model : module.getModels()) {
//                        if (model.getRenderType(module) != null) {
//                            //TODO Yaw is supposed to be radians. May want to change this at some point.
//                            // Why do we even need applyEffects? It seems it was added so that Model#renderToBuffer can be used. But SC uses its own ModelCartbase so we can just add out own render method.
//                            model.applyEffects(module, poseStack, bufferSource, yaw, pitch, 0);
//                            model.renderToBuffer(poseStack, bufferSource.getBuffer(model.getRenderType(module)), light, OverlayTexture.NO_OVERLAY, 0xFFFFFFFF);
//
//                        }
//                    }
//                }
//            }
//        }
//        poseStack.popPose();
//        renderLabels(entity, poseStack, bufferSource, light);
    }


    //    @Override
//    public @NotNull ResourceLocation getTextureLocation(@NotNull ModularMinecart p_114482_) {
//        return ResourceLocation.withDefaultNamespace("textures/entity/minecart.png");
//    }

//    protected void renderLabels(ModularMinecart cart, PoseStack poseStack, MultiBufferSource bufferSource, int p_114502_) {
//        ArrayList<Component> labels = cart.getLabel();
//        if (labels == null || this.entityRenderDispatcher.distanceToSqr(cart) > 64 * 64) {
//            return;
//        }
//
//        float f = cart.getBbHeight() + 1.5F;
//        poseStack.pushPose();
//        poseStack.translate(0.0D, f, 0.0D);
//
//        poseStack.mulPose(new Quaternionf(this.entityRenderDispatcher.cameraOrientation()).rotateY((float) Math.PI));
//        poseStack.scale(-0.025F, -0.025F, 0.025F);
//        Matrix4f matrix4f = poseStack.last().pose();
//        float f1 = Minecraft.getInstance().options.getBackgroundOpacity(0.25F);
//        int j = (int) (f1 * 255.0F) << 24;
//        Font font = this.getFont();
//
//        int height = 0;
//
//        for (int i = 0; i < labels.size(); i++) {
//            Component label = labels.get(i);
//            float f2 = (float) (-font.width(label) / 2);
//            font.drawInBatch(label, f2, height, 0x20ffffff, false, matrix4f, bufferSource, Font.DisplayMode.NORMAL, j, p_114502_);
//            font.drawInBatch(label, f2, height, 0xffffffff, false, matrix4f, bufferSource, Font.DisplayMode.NORMAL, 0, p_114502_);
//
//            height += font.lineHeight + 1;
//        }
//
//        poseStack.popPose();
//    }

    @Override
    public ModularCartRenderState createRenderState() {
        return new ModularCartRenderState();
    }

    @Override
    public void extractRenderState(ModularMinecart entity, ModularCartRenderState state, float partialTick) {
        super.extractRenderState(entity, state, partialTick);
        ModularMinecartBehavior behavior = (ModularMinecartBehavior) entity.getBehavior();
        if (behavior.cartHasPosRotLerp()) {
            state.renderPos = behavior.getCartLerpPosition(partialTick);
            state.xRot = behavior.getCartLerpXRot(partialTick);
            state.yRot = behavior.getCartLerpYRot(partialTick);
        } else {
            state.renderPos = null;
            state.xRot = entity.getXRot();
            state.yRot = entity.getYRot();
        }

        long seedBase = (long)entity.getId() * 493286711L;
        state.offsetSeed = seedBase * seedBase * 4392167121L + seedBase * 98761L;
        state.hurtTime = (float)entity.getHurtTime() - partialTick;
        state.hurtDir = entity.getHurtDir();
        state.damageTime = Math.max(entity.getDamage() - partialTick, 0.0F);
        state.displayOffset = entity.getDisplayOffset();

        state.modules.clear();
        state.modules.addAll(entity.modules());
        state.isInvisible = entity.isInvisible() || state.modules.stream().anyMatch(e -> !e.shouldCartRender());
    }

    public static class ModularCartRenderState extends EntityRenderState {
        public boolean isInvisible;
        public float xRot;
        public float yRot;
        public long offsetSeed;
        public int hurtDir;
        public float hurtTime;
        public float damageTime;
        public int displayOffset;
        @Nullable
        public Vec3 renderPos;
        public List<ModuleBase> modules = new ArrayList<>();
    }
}
