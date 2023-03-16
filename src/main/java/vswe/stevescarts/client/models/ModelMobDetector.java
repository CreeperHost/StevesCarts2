package vswe.stevescarts.client.models;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.renderer.MultiBufferSource;
import vswe.stevescarts.api.client.ModelCartbase;
import vswe.stevescarts.api.modules.ModuleBase;
import vswe.stevescarts.helpers.ResourceHelper;
import vswe.stevescarts.modules.realtimers.ModuleShooterAdv;

public class ModelMobDetector extends ModelCartbase {
    public ModelMobDetector() {
        super(getTexturedModelData().bakeRoot(), ResourceHelper.getResource("/models/mobDetectorModel.png"));
    }

    public static LayerDefinition getTexturedModelData() {
        MeshDefinition modelData = new MeshDefinition();
        PartDefinition modelPartData = modelData.getRoot();

        PartDefinition base = modelPartData.addOrReplaceChild("base", CubeListBuilder.create()
                        .addBox(-1.0f, -2.0f, -1.0f, 2, 4, 2),
                PartPose.offset(0.0f, -14.0f, 0.0f));

        PartDefinition body = base.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 8)
                        .addBox(-2.5f, -1.5f, -0.5f, 5, 3, 1),
                PartPose.offset(0.0f, -1.5f, -1.5f));

        for (int i = 0; i < 2; ++i) {
            body.addOrReplaceChild("side" + i, CubeListBuilder.create().texOffs(0, 13)
                            .addBox(-2.5f, -0.5f, -0.5f, 5, 1, 1),
                    PartPose.offset(0.0f, 2.0f * (i * 2 - 1), -1.0f));
        }

        for (int i = 0; i < 2; ++i) {
            body.addOrReplaceChild("side2" + i, CubeListBuilder.create().texOffs(12, 13)
                            .addBox(-1.5f, -0.5f, -0.5f, 3, 1, 1),
                    PartPose.offsetAndRotation(3.0f * (i * 2 - 1), 0.0f, -1.0f, 0, 0, 1.5707964f));
        }

        body.addOrReplaceChild("receiver", CubeListBuilder.create().texOffs(8, 0)
                        .addBox(-0.5f, -0.5f, -0.5f, 1, 1, 1),
                PartPose.offsetAndRotation(0.0f, 0.0f, -1.0f, 0, 1.5707964f, 0));

        body.addOrReplaceChild("dot", CubeListBuilder.create().texOffs(8, 2)
                        .addBox(-0.5f, -0.5f, -0.5f, 1, 1, 1),
                PartPose.offset(0.0f, 0.0f, -2.0f));

        return LayerDefinition.create(modelData, 64, 16);
    }

    @Override
    public void applyEffects(ModuleBase module, PoseStack matrixStack, MultiBufferSource rtb, float yaw, float pitch, float roll) {
        root.yRot = ((module == null) ? 0.0f : (((ModuleShooterAdv) module).getDetectorAngle() + yaw));
    }
}
