package vswe.stevescarts.mixin.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.AbstractMinecartRenderer;
import net.minecraft.client.renderer.entity.state.MinecartRenderState;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.world.entity.vehicle.minecart.AbstractMinecart;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import vswe.stevescarts.api.carts.CartTrain;
import vswe.stevescarts.api.carts.CartTrainLinker;
import vswe.stevescarts.client.renders.CartHitchRenderer;
import vswe.stevescarts.internal.MinecartRenderStateExtension;

@Mixin(AbstractMinecartRenderer.class)
public abstract class AbstractMinecartRendererMixin {
    @Inject(method = "extractRenderState", at = @At("TAIL"))
    private void stevescarts$extractTrainHitches(AbstractMinecart cart, MinecartRenderState state,
                                                 float partialTick, CallbackInfo ci) {
        MinecartRenderStateExtension extendedState = (MinecartRenderStateExtension) state;
        extendedState.stevescarts$getHitches().clear();
        Vec3 cartPosition = cart.getPosition(partialTick);
        for (int i = 0; i < CartTrain.MAX_DIRECT_LINKS; i++) {
            int linkedId = CartTrainLinker.getLinkedCartEntityId(cart, i);
            if (linkedId < 0 || cart.getId() >= linkedId) {
                continue;
            }
            if (cart.level().getEntity(linkedId) instanceof AbstractMinecart linkedCart) {
                extendedState.stevescarts$getHitches().add(new CartHitchRenderer.HitchConnection(
                        linkedCart.getPosition(partialTick).subtract(cartPosition), state.yRot, linkedCart.getYRot()));
            }
        }
    }

    @Inject(method = "submit", at = @At("TAIL"))
    private void stevescarts$submitTrainHitches(MinecartRenderState state, PoseStack poseStack,
                                                SubmitNodeCollector nodeCollector, CameraRenderState cameraRenderState,
                                                CallbackInfo ci) {
        for (CartHitchRenderer.HitchConnection hitch
                : ((MinecartRenderStateExtension) state).stevescarts$getHitches()) {
            CartHitchRenderer.submit(hitch, poseStack, nodeCollector, state.lightCoords);
        }
    }
}
