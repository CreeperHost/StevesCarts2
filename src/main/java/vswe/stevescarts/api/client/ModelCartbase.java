package vswe.stevescarts.api.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.resources.Identifier;
import org.jetbrains.annotations.NotNull;
import vswe.stevescarts.api.modules.ModuleBase;

import java.util.Collections;
import java.util.function.Function;

public abstract class ModelCartbase extends Model {
    protected final Identifier texture;
    protected ModelPart root;

    public ModelCartbase(Function<Identifier, RenderType> layerFactory, Identifier texture, ModelPart root) {
        super(new ModelPart(Collections.emptyList(), Collections.emptyMap()), layerFactory);
        this.texture = texture;
        this.root = root;
    }

    public ModelCartbase(ModelPart root, Identifier texture) {
        this(RenderTypes::entityCutout, texture, root);
    }

    @Override
    public void renderToBuffer(@NotNull PoseStack poseStack, @NotNull VertexConsumer vertexConsumer, int light, int overlay, int i) {
        if (root != null) {
            this.root.render(poseStack, vertexConsumer, light, overlay, i);
        }
    }

    public Identifier getTexture() {
        return texture;
    }

    public ModelPart getRoot() {
        return root;
    }

    public Identifier getResource(final ModuleBase module) {
        return texture;
    }

    public void applyEffects(ModuleBase module, PoseStack matrixStack, float yaw, float pitch, float roll) {
    }

    public RenderType getRenderType(ModuleBase moduleBase) {
        return RenderTypes.entitySolid(getResource(moduleBase));
    }
}
