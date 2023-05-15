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
import org.jetbrains.annotations.NotNull;
import vswe.stevescarts.api.client.ModelCartbase;
import vswe.stevescarts.helpers.ResourceHelper;
import vswe.stevescarts.api.modules.ModuleBase;
import vswe.stevescarts.modules.realtimers.ModuleFlowerRemover;

import java.util.ArrayList;

public class ModelLawnMower extends ModelCartbase {
    private static final ArrayList<ModelPart> bladepins = new ArrayList<>();
    private static ModelPart leftSide;
    private static ModelPart rightSide;

    public ModelLawnMower() {
        super(null, ResourceHelper.getResource("/models/lawnmowerModel.png"));
        genModel();
    }

    public static void genModel() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        leftSide = createSide(partdefinition, false);
        rightSide = createSide(partdefinition, true);
    }

    private static ModelPart createSide(PartDefinition partDefinition, boolean opposite) {
        PartDefinition anchor = partDefinition.addOrReplaceChild("anchor", CubeListBuilder.create(),
                PartPose.rotation(0, opposite ? 3.1415927f : 0, 0)
        );

        PartDefinition base = anchor.addOrReplaceChild("base", CubeListBuilder.create()
                .addBox(-11.5f, -3.0f, -1.0f, 23, 6, 2),
                PartPose.offset(0.0f, -1.5f, -9.0f)
        );

        for (int i = 0; i < 2; i++) {
            PartDefinition arm = base.addOrReplaceChild("arm" + i, CubeListBuilder.create()
                            .texOffs(0, 8)
                            .addBox(-8.0f, -1.5f, -1.5f, 16, 3, 3),
                    PartPose.offsetAndRotation(-8.25f + i * 16.5f, 0.0f, -8.0f, 0, 1.5707964f, 0)
            );

            PartDefinition arm2 = arm.addOrReplaceChild("arm2" + i, CubeListBuilder.create()
                            .texOffs(0, 14)
                            .addBox(-1.5f, -1.5f, -1.5f, 3, 3, 3),
                    PartPose.offsetAndRotation(6.5f, 3.0f, 0.0f, 0, 0, 1.5707964f)
            );

            PartDefinition bladePin = arm2.addOrReplaceChild("bladepin" + i, CubeListBuilder.create()
                            .texOffs(0, 20)
                            .addBox(-1.0f, -0.5f, -0.5f, 2, 1, 1),
                    PartPose.offset(2.5f, 0.0f, 0.0f)
            );

            PartDefinition bladeAnchor = bladePin.addOrReplaceChild("bladeanchor" + i, CubeListBuilder.create(),
                    PartPose.rotation(0, 1.5707964f, 0)
            );

            for (int j = 0; j < 4; j++) {
                PartDefinition blade = bladeAnchor.addOrReplaceChild("blade" + j, CubeListBuilder.create()
                                .texOffs(0, 22)
                                .addBox(-1.5f, -1.5f, -0.5f, 8, 3, 1),
                        PartPose.offsetAndRotation(0.0f, 0.0f, j * 0.01f, 0, 0, 1.5707964f * (j + i * 0.5f))
                );

                PartDefinition bladetip = blade.addOrReplaceChild("bladetip" + j, CubeListBuilder.create()
                                .texOffs(0, 26)
                                .addBox(6.5f, -1.0f, -0.5f, 6, 2, 1),
                        PartPose.offset(0.0f, 0.0f, 0.005f)
                );
            }
        }

        ModelPart anchorPart = anchor.bake(64, 32);
        ModelPart basePart = anchorPart.getChild("base");

        for (int i = 0; i < 2; i++) {
            ModelPart armPart = basePart.getChild("arm" + i);
            ModelPart arm2Part = armPart.getChild("arm2" + i);
            ModelPart bladePinPart = arm2Part.getChild("bladepin" + i);
            bladepins.add(bladePinPart);
        }
        return anchorPart;
    }

    @Override
    public void applyEffects(ModuleBase module, PoseStack matrixStack, MultiBufferSource rtb, float yaw, float pitch, float roll) {
        genModel();
        int partialtime = 0;
        float angle = (module == null) ? 0.0f : (((ModuleFlowerRemover) module).getBladeAngle() + partialtime * ((ModuleFlowerRemover) module).getBladeSpeed());
        for (int i = 0; i < bladepins.size(); ++i) {
            ModelPart bladepin = bladepins.get(i);
            if (i % 2 == 0) {
                bladepin.xRot = angle;
            } else {
                bladepin.xRot = -angle;
            }
        }
    }

    @Override
    public void renderToBuffer(@NotNull PoseStack poseStack, @NotNull VertexConsumer vertexConsumers, int light, int overlay, float red, float green, float blue, float alpha) {
        leftSide.render(poseStack, vertexConsumers, light, overlay, red, green, blue, alpha);
        rightSide.render(poseStack, vertexConsumers, light, overlay, red, green, blue, alpha);
    }
}
