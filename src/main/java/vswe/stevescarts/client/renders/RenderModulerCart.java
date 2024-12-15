package vswe.stevescarts.client.renders;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4f;
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
                model.applyEffects(module, poseStack, bufferSource, state.yRot, state.xRot, 0);
                model.renderToBuffer(poseStack, bufferSource.getBuffer(model.getRenderType(module)), light, OverlayTexture.NO_OVERLAY, 0xFFFFFFFF);
            }
        }

        poseStack.popPose();

        renderTag(state.label, poseStack, bufferSource, light);
    }

    protected void renderTag(List<Component> label, PoseStack poseStack, MultiBufferSource source, int light) {
        boolean throughWalls = true;
        poseStack.pushPose();
        poseStack.translate(0, 1.2, 0);
        poseStack.mulPose(this.entityRenderDispatcher.cameraOrientation());
        poseStack.scale(0.025F, -0.025F, 0.025F);
        Matrix4f matrix4f = poseStack.last().pose();
        Font font = this.getFont();
        int rowOffset = -((label.size() - 1) * (font.lineHeight + 1));
        for (Component component : label) {
            float f = (float) (-font.width(component)) / 2.0F;
            int j = (int) (Minecraft.getInstance().options.getBackgroundOpacity(0.25F) * 255.0F) << 24;
            font.drawInBatch(component, f, rowOffset, -2130706433, false, matrix4f, source, throughWalls ? Font.DisplayMode.SEE_THROUGH : Font.DisplayMode.NORMAL, j, light);
            if (throughWalls) {
                font.drawInBatch(component, f, rowOffset, -1, false, matrix4f, source, Font.DisplayMode.NORMAL, 0, LightTexture.lightCoordsWithEmission(light, 2));
            }
            rowOffset += font.lineHeight + 1;
        }
        poseStack.popPose();
    }

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
