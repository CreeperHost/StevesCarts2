package vswe.stevescarts.client.models;

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
import vswe.stevescarts.helpers.ResourceHelper;
import vswe.stevescarts.modules.realtimers.ModuleAdvControl;

public class ModelWheel extends ModelCartbase {

    private ModelPart anchor;

    public ModelWheel() {
        super(null, ResourceHelper.getResource("/models/wheelModel.png"));
        makeModel();
    }

    public void makeModel() {
        MeshDefinition modelData = new MeshDefinition();
        PartDefinition modelPartData = modelData.getRoot();

        PartDefinition mainAnchor = modelPartData.addOrReplaceChild("mainAnchor", CubeListBuilder.create(),
                PartPose.offset(-10.0f, -5.0f, 0.0f)
        );

        mainAnchor.addOrReplaceChild("top", CubeListBuilder.create()
                        .addBox(-4.5f, -1.0f, -1.0f, 9, 2, 2),
                PartPose.offsetAndRotation(0.0f, -6.0f, 0.0f, 0, -1.5707964f, 0)
        );

        mainAnchor.addOrReplaceChild("topLeft", CubeListBuilder.create()
                        .texOffs(0, 4)
                        .addBox(-1.0f, -1.0f, -1.0f, 2, 2, 2),
                PartPose.offsetAndRotation(0.0f, -4.0f, -5.5f, 0, -1.5707964f, 0)
        );

        mainAnchor.addOrReplaceChild("topRight", CubeListBuilder.create()
                        .texOffs(0, 4)
                        .addBox(-1.0f, -1.0f, -1.0f, 2, 2, 2),
                PartPose.offsetAndRotation(0.0f, -4.0f, 5.5f, 0, -1.5707964f, 0)
        );

        mainAnchor.addOrReplaceChild("left", CubeListBuilder.create()
                        .texOffs(0, 12)
                        .addBox(-1.0f, -2.5f, -1.0f, 2, 5, 2),
                PartPose.offsetAndRotation(0.0f, -0.5f, -7.5f, 0, -1.5707964f, 0)
        );

        mainAnchor.addOrReplaceChild("right", CubeListBuilder.create()
                        .texOffs(0, 12)
                        .addBox(-1.0f, -2.5f, -1.0f, 2, 5, 2),
                PartPose.offsetAndRotation(0.0f, -0.5f, 7.5f, 0, -1.5707964f, 0)
        );

        mainAnchor.addOrReplaceChild("bottomleft", CubeListBuilder.create()
                        .texOffs(0, 4)
                        .addBox(-1.0f, -1.0f, -1.0f, 2, 2, 2),
                PartPose.offsetAndRotation(0.0f, 3.0f, -5.5f, 0, -1.5707964f, 0)
        );

        mainAnchor.addOrReplaceChild("bottomright", CubeListBuilder.create()
                        .texOffs(0, 4)
                        .addBox(-1.0f, -1.0f, -1.0f, 2, 2, 2),
                PartPose.offsetAndRotation(0.0f, 3.0f, 5.5f, 0, -1.5707964f, 0)
        );

        mainAnchor.addOrReplaceChild("bottominnerleft", CubeListBuilder.create()
                        .texOffs(0, 4)
                        .addBox(-1.0f, -1.0f, -1.0f, 2, 2, 2),
                PartPose.offsetAndRotation(0.0f, 5.0f, -3.5f, 0, -1.5707964f, 0)
        );

        mainAnchor.addOrReplaceChild("bottominnerright", CubeListBuilder.create()
                        .texOffs(0, 4)
                        .addBox(-1.0f, -1.0f, -1.0f, 2, 2, 2),
                PartPose.offsetAndRotation(0.0f, 5.0f, 3.5f, 0, -1.5707964f, 0)
        );

        mainAnchor.addOrReplaceChild("bottom", CubeListBuilder.create()
                        .texOffs(0, 8)
                        .addBox(-2.5f, -1.0f, -1.0f, 5, 2, 2),
                PartPose.offsetAndRotation(0.0f, 7.0f, 0.0f, 0, -1.5707964f, 0)
        );

        mainAnchor.addOrReplaceChild("middlebottom", CubeListBuilder.create()
                        .texOffs(0, 19)
                        .addBox(-0.5f, -2.5f, -0.5f, 1, 5, 1),
                PartPose.offsetAndRotation(0.5f, 3.5f, 0.0f, 0, -1.5707964f, 0)
        );

        mainAnchor.addOrReplaceChild("middle", CubeListBuilder.create()
                        .texOffs(0, 25)
                        .addBox(-1.5f, -1.0f, -0.5f, 3, 2, 1),
                PartPose.offsetAndRotation(0.5f, 0.0f, 0.0f, 0, -1.5707964f, 0)
        );

        mainAnchor.addOrReplaceChild("middleleft", CubeListBuilder.create()
                        .texOffs(0, 25)
                        .addBox(-1.5f, -1.0f, -0.5f, 3, 2, 1),
                PartPose.offsetAndRotation(0.5f, -1.0f, -3.0f, 0, -1.5707964f, 0)
        );

        mainAnchor.addOrReplaceChild("middleright", CubeListBuilder.create()
                        .texOffs(0, 25)
                        .addBox(-1.5f, -1.0f, -0.5f, 3, 2, 1),
                PartPose.offsetAndRotation(0.5f, -1.0f, 3.0f, 0, -1.5707964f, 0)
        );

        mainAnchor.addOrReplaceChild("innerleft", CubeListBuilder.create()
                        .texOffs(0, 28)
                        .addBox(-1.5f, -0.5f, -0.5f, 2, 1, 1),
                PartPose.offsetAndRotation(0.5f, -1.5f, -5.0f, 0, -1.5707964f, 0)
        );

        mainAnchor.addOrReplaceChild("innerright", CubeListBuilder.create()
                        .texOffs(0, 28)
                        .addBox(-1.5f, -0.5f, -0.5f, 2, 1, 1),
                PartPose.offsetAndRotation(0.5f, -1.5f, 6.0f, 0, -1.5707964f, 0)
        );

        ModelPart model = LayerDefinition.create(modelData, 32, 32).bakeRoot();
        anchor = model.getChild("mainAnchor");
        root = this.anchor;
    }

    @Override
    public void applyEffects(final ModuleBase module, PoseStack matrixStack, MultiBufferSource rtb, final float yaw, final float pitch, final float roll) {
        anchor.xRot = ((module == null) ? 0.0f : ((ModuleAdvControl) module).getWheelAngle());
    }

    @Override
    public void renderToBuffer(@NotNull PoseStack poseStack, @NotNull VertexConsumer vertexConsumers, int light, int overlay, float red, float green, float blue, float alpha) {
        poseStack.pushPose();
        poseStack.scale(0.65F, 0.65F, 0.65F);
        super.renderToBuffer(poseStack, vertexConsumers, light, overlay, red, green, blue, alpha);
        poseStack.popPose();
    }
}
