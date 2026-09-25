package vswe.stevescarts.mixin;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.vehicle.minecart.AbstractMinecart;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import vswe.stevescarts.api.carts.CartTrainLinker;

@Mixin(AbstractMinecart.class)
public abstract class MinecartTrainMixin {
    @Inject(method = "tick", at = @At("TAIL"))
    private void stevescarts$tickTrain(CallbackInfo ci) {
        CartTrainLinker.tick((AbstractMinecart) (Object) this);
    }

    @Inject(method = "push", at = @At("HEAD"), cancellable = true)
    private void stevescarts$ignoreLinkedCartCollision(Entity entity, CallbackInfo ci) {
        AbstractMinecart cart = (AbstractMinecart) (Object) this;
        if (entity instanceof AbstractMinecart other && CartTrainLinker.isConnected(cart, other)) {
            ci.cancel();
        }
    }

}
