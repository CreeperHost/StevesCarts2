package vswe.stevescarts.api.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import vswe.stevescarts.api.modules.ModuleBase;

import java.util.Collections;
import java.util.function.Function;

public abstract class ModelCartbase extends Model
{
    protected final ResourceLocation texture;
    protected ModelPart root;

    public ModelCartbase(Function<ResourceLocation, RenderType> layerFactory, ResourceLocation texture, ModelPart root)
    {
        super(new ModelPart(Collections.emptyList(), Collections.emptyMap()), layerFactory);
        this.texture = texture;
        this.root = root;
    }

    public ModelCartbase(ModelPart root, ResourceLocation texture)
    {
        this(RenderType::entityCutoutNoCull, texture, root);
    }

    @Override
    public void renderToBuffer(@NotNull PoseStack poseStack, @NotNull VertexConsumer vertexConsumer, int light, int overlay, int i)
    {
        if(root != null)
        {
            this.root.render(poseStack, vertexConsumer, light, overlay, i);
        }
    }

    public ResourceLocation getTexture()
    {
        return texture;
    }

    public ModelPart getRoot()
    {
        return root;
    }

    public ResourceLocation getResource(final ModuleBase module)
    {
        return texture;
    }

    public void applyEffects(final ModuleBase module, PoseStack matrixStack, MultiBufferSource rtb, final float yaw, final float pitch, final float roll)
    {
    }

    public RenderType getRenderType(ModuleBase moduleBase)
    {
        return RenderType.entitySolid(getResource(moduleBase));
    }
}
