package vswe.stevescarts.client.models.workers.tools;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import vswe.stevescarts.api.client.ModelCartbase;
import vswe.stevescarts.api.modules.ModuleBase;
import vswe.stevescarts.modules.workers.tools.ModuleWoodcutter;

public class ModelWoodCutter extends ModelCartbase
{
    private static ModelPart[] anchors;
    public ModelWoodCutter(final ResourceLocation resource)
    {
        super(null, resource);
        MeshDefinition modelData = new MeshDefinition();
        PartDefinition modelPartData = modelData.getRoot();

        anchors = new ModelPart[5];

        for (int i = -2; i <= 2; ++i)
        {
            PartDefinition anchor = modelPartData.addOrReplaceChild("anchor" + i, CubeListBuilder.create(),
                    PartPose.offset(0, 0, 0));

            anchor.addOrReplaceChild("side" + i, CubeListBuilder.create().texOffs(0, 0)
                            .addBox(-3.5f, -1.5f, -0.5f, 7, 3, 1),
                    PartPose.offset(-13.0f, 0.0f, i * 2));

            anchor.addOrReplaceChild("tip" + i, CubeListBuilder.create().texOffs(0, 0)
                            .addBox(-0.5f, -0.5f, -0.5f, 1, 1, 1),
                    PartPose.offset(-4.0f, 0.0f, 0.0f));

            anchors[i + 2] = anchor.bake(16, 8);
        }
    }

    @Override
    public void renderToBuffer(@NotNull PoseStack poseStack, @NotNull VertexConsumer vertexConsumers, int light, int overlay, float red, float green, float blue, float alpha) {
        for (ModelPart anchor : anchors) {
            anchor.render(poseStack, vertexConsumers, light, overlay, red, green, blue, alpha);
        }
    }

    @Override
    public void applyEffects(ModuleBase module, PoseStack matrixStack, MultiBufferSource rtb, float yaw, float pitch, float roll)
    {
        super.applyEffects(module, matrixStack, rtb, yaw, pitch, roll);
        final float commonAngle = (module == null) ? 0.0f : ((ModuleWoodcutter) module).getCutterAngle();
        for (int i = 0; i < anchors.length; ++i)
        {
            float specificAngle;
            if (i % 2 == 0)
            {
                specificAngle = (float) Math.sin(commonAngle);
            }
            else
            {
                specificAngle = (float) Math.cos(commonAngle);
            }
            anchors[i].x = specificAngle * 1.25f;
        }
    }
}
