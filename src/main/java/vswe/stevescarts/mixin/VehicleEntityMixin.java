package vswe.stevescarts.mixin;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.vehicle.VehicleEntity;
import net.minecraft.world.entity.vehicle.minecart.AbstractMinecart;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import vswe.stevescarts.api.carts.CartTrainLinker;

@Mixin(VehicleEntity.class)
public abstract class VehicleEntityMixin {
    @Inject(
            method = "destroy(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/damagesource/DamageSource;)V",
            at = @At("HEAD")
    )
    private void stevescarts$unlinkDestroyedMinecart(ServerLevel level, DamageSource source, CallbackInfo ci) {
        if ((Object) this instanceof AbstractMinecart cart) {
            CartTrainLinker.onCartDestroyed(cart);
        }
    }
}
