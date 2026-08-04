package vswe.stevescarts.client.renders;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import org.jspecify.annotations.NonNull;
import vswe.stevescarts.api.client.ModelCartbase;
import vswe.stevescarts.api.modules.ModuleBase;
import vswe.stevescarts.api.modules.template.ModuleHull;
import vswe.stevescarts.entities.ModularMinecart;
import vswe.stevescarts.entities.ModularMinecartBehavior;
import vswe.stevescarts.init.StevesCartsModules;
import vswe.stevescarts.modules.hull.ModuleReinforced;

import java.util.ArrayList;
import java.util.List;

public class RenderModulerCart extends EntityRenderer<ModularMinecart, RenderModulerCart.ModularCartRenderState> {
    public RenderModulerCart(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public void submit(ModularCartRenderState state, @NonNull PoseStack poseStack, @NonNull SubmitNodeCollector nodeCollector, @NonNull CameraRenderState cameraRenderState) {
        if (state.isInvisible) {
            return;
        }

        super.submit(state, poseStack, nodeCollector, cameraRenderState);

        poseStack.pushPose();
        long offsetSeed = state.offsetSeed;
        float xOffset = (((float) (offsetSeed >> 16 & 7L) + 0.5F) / 8.0F - 0.5F) * 0.004F;
        float yOffset = (((float) (offsetSeed >> 20 & 7L) + 0.5F) / 8.0F - 0.5F) * 0.004F;
        float zOffset = (((float) (offsetSeed >> 24 & 7L) + 0.5F) / 8.0F - 0.5F) * 0.004F;
        poseStack.translate(xOffset, yOffset, zOffset);

        //New render transforms
        poseStack.mulPose(Axis.YP.rotationDegrees(state.yRot));
        poseStack.mulPose(Axis.ZP.rotationDegrees(-state.xRot));
        poseStack.translate(0.0F, 0.375F, 0.0F);

        float f3 = state.hurtTime;
        if (f3 > 0.0F) {
            poseStack.mulPose(Axis.XP.rotationDegrees(Mth.sin(f3) * f3 * state.damageTime / 10.0F * (float) state.hurtDir));
        }

        poseStack.scale(-1.0F, -1.0F, 1.0F);

        for (ModuleBase module : state.modules) {
            if (!module.haveModels()) continue;
            for (ModelCartbase model : module.getModels()) {
                if (model.getRenderType(module) == null) continue;
                model.applyEffects(module, poseStack, state.yRot, state.xRot, 0);
                nodeCollector.submitModel(model, null, poseStack, model.getRenderType(module), 240, OverlayTexture.NO_OVERLAY, 0, null);
            }
        }

        if (state.modules.isEmpty()) {
            //Fallback Render
            ModuleHull hull = new ModuleReinforced(null);
            for (ModelCartbase model : StevesCartsModules.REINFORCED_HULL.getModels(true).values()) {
                model.applyEffects(hull, poseStack, 0, 0, 0);
                nodeCollector.submitModel(model, null, poseStack, model.getRenderType(hull), 240, OverlayTexture.NO_OVERLAY, 0, null);
            }
        }

        poseStack.popPose();

        renderTag(state.label, poseStack, nodeCollector, 240, cameraRenderState, state.distanceToCameraSq);
    }

    //    @Override
//    public void render(ModularCartRenderState state, PoseStack poseStack, MultiBufferSource bufferSource, int light) {
//        if (state.isInvisible) {
//            return;
//        }
//
//        super.render(state, poseStack, bufferSource, light);
//
//        poseStack.pushPose();
//        long offsetSeed = state.offsetSeed;
//        float xOffset = (((float) (offsetSeed >> 16 & 7L) + 0.5F) / 8.0F - 0.5F) * 0.004F;
//        float yOffset = (((float) (offsetSeed >> 20 & 7L) + 0.5F) / 8.0F - 0.5F) * 0.004F;
//        float zOffset = (((float) (offsetSeed >> 24 & 7L) + 0.5F) / 8.0F - 0.5F) * 0.004F;
//        poseStack.translate(xOffset, yOffset, zOffset);
//
//        //New render transforms
//        poseStack.mulPose(Axis.YP.rotationDegrees(state.yRot));
//        poseStack.mulPose(Axis.ZP.rotationDegrees(-state.xRot));
//        poseStack.translate(0.0F, 0.375F, 0.0F);
//
//        float f3 = state.hurtTime;
//        if (f3 > 0.0F) {
//            poseStack.mulPose(Axis.XP.rotationDegrees(Mth.sin(f3) * f3 * state.damageTime / 10.0F * (float) state.hurtDir));
//        }
//
//        poseStack.scale(-1.0F, -1.0F, 1.0F);
//
//        for (ModuleBase module : state.modules) {
//            if (!module.haveModels()) continue;
//            for (ModelCartbase model : module.getModels()) {
//                if (model.getRenderType(module) == null) continue;
//                model.applyEffects(module, poseStack, bufferSource, state.yRot, state.xRot, 0);
//                model.renderToBuffer(poseStack, bufferSource.getBuffer(model.getRenderType(module)), light, OverlayTexture.NO_OVERLAY, 0xFFFFFFFF);
//            }
//        }
//
//        poseStack.popPose();
//
//        renderTag(state.label, poseStack, bufferSource, light);
//    }

    protected void renderTag(List<Component> label, PoseStack poseStack, SubmitNodeCollector nodeCollector, int light, CameraRenderState cameraRenderState, double distanceToCameraSq) {
        boolean throughWalls = true;
        poseStack.pushPose();
        poseStack.translate(0, 0.5, 0);
        Font font = this.getFont();
        double lineHeight = 0.3;
        double rowOffset = label.size() * lineHeight;
        for (Component component : label) {
            nodeCollector.submitNameTag(poseStack, new Vec3(0, rowOffset, 0), 0, component, throughWalls, light, distanceToCameraSq, cameraRenderState);
            rowOffset -= lineHeight;
        }
        poseStack.popPose();
    }

    @Override
    public @NonNull ModularCartRenderState createRenderState() {
        return new ModularCartRenderState();
    }

    @Override
    public void extractRenderState(@NonNull ModularMinecart entity, @NonNull ModularCartRenderState state, float partialTick) {
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

        long seedBase = (long) entity.getId() * 493286711L;
        state.offsetSeed = seedBase * seedBase * 4392167121L + seedBase * 98761L;
        state.hurtTime = (float) entity.getHurtTime() - partialTick;
        state.hurtDir = entity.getHurtDir();
        state.damageTime = Math.max(entity.getDamage() - partialTick, 0.0F);
        state.displayOffset = entity.getDisplayOffset();

        state.modules.clear();
        state.modules.addAll(entity.modules());
        state.isInvisible = entity.isInvisible() || state.modules.stream().anyMatch(e -> !e.shouldCartRender());
        state.label.clear();
        state.label.addAll(entity.getLabel());
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
        public List<Component> label = new ArrayList<>();
    }
}
