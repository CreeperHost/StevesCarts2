package vswe.stevescarts.mixin.client;

import net.minecraft.client.renderer.entity.state.MinecartRenderState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import vswe.stevescarts.client.renders.CartHitchRenderer;
import vswe.stevescarts.internal.MinecartRenderStateExtension;

import java.util.ArrayList;
import java.util.List;

@Mixin(MinecartRenderState.class)
public class MinecartRenderStateMixin implements MinecartRenderStateExtension {
    @Unique
    private final List<CartHitchRenderer.HitchConnection> stevescarts$hitches = new ArrayList<>();

    @Override
    public List<CartHitchRenderer.HitchConnection> stevescarts$getHitches() {
        return stevescarts$hitches;
    }
}
