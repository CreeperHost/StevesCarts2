package vswe.stevescarts.client.models;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import vswe.stevescarts.api.client.ModelCartbase;
import vswe.stevescarts.api.modules.ModuleBase;
import vswe.stevescarts.helpers.ResourceHelper;
import vswe.stevescarts.modules.addons.ModuleLiquidSensors;

public class ModelLiquidSensors extends ModelCartbase {
    private static ResourceLocation texture = ResourceHelper.getResource("/models/sensorModel.png");

    private int activeColour = 0;
    private ModelPart[] sensorLeft;
    private ModelPart[] sensorRight;

    public ModelLiquidSensors() {
        super(null, texture);
        sensorLeft = new ModelPart[]{createSensor(false, 1), createSensor(false, 2), createSensor(false, 3)};
        sensorRight = new ModelPart[]{createSensor(true, 1), createSensor(true, 2), createSensor(true, 3)};
    }

    private ModelPart createSensor(final boolean right, int colour) {
        MeshDefinition modelData = new MeshDefinition();
        PartDefinition modelPartData = modelData.getRoot();

        PartDefinition base = modelPartData.addOrReplaceChild("base", CubeListBuilder.create()
                        .addBox(-0.5f, 2.0f, -0.5f, 1, 4, 1),
                PartPose.offset(-9f, -11f, right ? 7F : -7F));


        PartDefinition head = base.addOrReplaceChild("head", CubeListBuilder.create()
                        .texOffs(4, 0)
                        .addBox(-3f, -2.0f, -3.0f, 4, 4, 4),
                PartPose.offset(1.0f, 0.0f, 1.0f));

        PartDefinition face = head.addOrReplaceChild("face", CubeListBuilder.create()
                        .texOffs(20, 0)
                        .addBox(-1.5f, -1.0f, -2.0f, 1, 2, 2),
                PartPose.offset(-2.5f, 0.0f, 0.0f));


        PartDefinition light = head.addOrReplaceChild("light_" + colour, CubeListBuilder.create()
                        .texOffs(20, 1 + colour * 3)
                        .addBox(-2.0f, -0.5f, -2.0f, 2, 1, 2),
                PartPose.offset(0.0f, -2.5f, 0.0f));

        return base.bake(32, 16);
    }

    @Override
    public void renderToBuffer(@NotNull PoseStack poseStack, @NotNull VertexConsumer vertexConsumers, int light, int overlay, float red, float green, float blue, float alpha) {
        this.sensorLeft[activeColour].render(poseStack, vertexConsumers, light, overlay, red, green, blue, alpha);
        this.sensorRight[activeColour].render(poseStack, vertexConsumers, light, overlay, red, green, blue, alpha);
    }

    @Override
    public void applyEffects(final ModuleBase module, PoseStack matrixStack, MultiBufferSource rtb, final float yaw, final float pitch, final float roll) {
        sensorLeft[activeColour].yRot = ((module == null) ? 0.0f : (-((ModuleLiquidSensors) module).getSensorRotation()));
        sensorRight[activeColour].yRot = ((module == null) ? 0.0f : ((ModuleLiquidSensors) module).getSensorRotation());
        activeColour = ((module == null) ? 2 : ((ModuleLiquidSensors) module).getLight()) - 1;
    }
}
