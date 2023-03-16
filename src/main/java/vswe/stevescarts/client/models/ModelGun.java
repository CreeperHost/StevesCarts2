package vswe.stevescarts.client.models;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.renderer.MultiBufferSource;
import org.jetbrains.annotations.NotNull;
import vswe.stevescarts.api.client.ModelCartbase;
import vswe.stevescarts.api.modules.ModuleBase;
import vswe.stevescarts.helpers.ResourceHelper;
import vswe.stevescarts.modules.realtimers.ModuleShooter;

import java.util.Arrays;

public class ModelGun extends ModelCartbase {
    private ModelPart[] guns;
    private float[] angles = new float[8];

    public ModelGun() {
        super(null, ResourceHelper.getResource("/models/gunModel.png"));
        buildModels(angles);
    }

    public void buildModels(float[] newAngles) {
        angles = newAngles;
        MeshDefinition modelData = new MeshDefinition();
        PartDefinition modelPartData = modelData.getRoot();

        guns = new ModelPart[8];
        for (int i = 0; i < guns.length; i++) {
            float angle = (new int[]{3, 4, 5, 2, 6, 1, 0, 7})[i];
            angle *= (float) Math.PI / 4F;

            PartDefinition gunAnchorAnchor = modelPartData.addOrReplaceChild("gun_aa" + i, CubeListBuilder.create(),
                    PartPose.rotation(0f, angle, 0f));

            PartDefinition gunAnchor = gunAnchorAnchor.addOrReplaceChild("gun_a" + i, CubeListBuilder.create(),
                    PartPose.offset(2.5f, 0.0f, 0.0f));

            PartDefinition gun = gunAnchor.addOrReplaceChild("gun_" + i, CubeListBuilder.create()
                            .texOffs(0, 16)
                            .addBox(-1.5f, -2.5f, -1.5f, 7, 3, 3),
                    PartPose.offsetAndRotation(0.0f, -9.0f, 0.0f, 0, 0, angles[i]));

            guns[i] = gunAnchorAnchor.bake(32, 8);
        }
    }

    @Override
    public void renderToBuffer(@NotNull PoseStack poseStack, @NotNull VertexConsumer vertexConsumers, int light, int overlay, float red, float green, float blue, float alpha) {
        for (ModelPart gun : guns) {
            gun.render(poseStack, vertexConsumers, light, overlay, red, green, blue, alpha);
        }
    }


    @Override
    public void applyEffects(final ModuleBase module, PoseStack matrixStack, MultiBufferSource rtb, final float yaw, final float pitch, final float roll) {
        float[] newAngles = new float[guns.length];

        for (int i = 0; i < guns.length; ++i) {
            newAngles[i] = ((module == null) ? 0.0f : ((ModuleShooter) module).getPipeRotation(i));
        }

        if (!Arrays.equals(angles, newAngles)) {
            buildModels(newAngles);
        }
    }
}