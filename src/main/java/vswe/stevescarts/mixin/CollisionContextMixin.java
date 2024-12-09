package vswe.stevescarts.mixin;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.MinecartCollisionContext;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import vswe.stevescarts.entities.ModularMinecart;

/**
 * Created by brandon3055 on 08/12/2024
 */
@Mixin(CollisionContext.class)
public interface CollisionContextMixin {

    @Inject(
            method = "Lnet/minecraft/world/phys/shapes/CollisionContext;of(Lnet/minecraft/world/entity/Entity;)Lnet/minecraft/world/phys/shapes/CollisionContext;",
            at = @At("HEAD"),
            cancellable = true
    )
    private static void of(Entity entity, CallbackInfoReturnable<CollisionContext> cir) {
        if (entity instanceof ModularMinecart minecart) {
            cir.setReturnValue(new MinecartCollisionContext(minecart, false));
        }
    }
}
