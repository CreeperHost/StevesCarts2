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
import vswe.stevescarts.api.client.ModelCartbase;
import vswe.stevescarts.helpers.ResourceHelper;
import vswe.stevescarts.api.modules.ModuleBase;
import vswe.stevescarts.modules.realtimers.ModuleFlowerRemover;

import java.util.ArrayList;

public class ModelLawnMower extends ModelCartbase
{
    private static final ArrayList<ModelPart> bladepins = new ArrayList<>();

    public ModelLawnMower()
    {
        super(createBodyLayer().bakeRoot(), ResourceHelper.getResource("/models/lawnmowerModel.png"));
    }

    public static LayerDefinition createBodyLayer()
    {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();
        createSide(partdefinition, false);
        createSide(partdefinition, true);

        return LayerDefinition.create(meshdefinition, 64, 32);
    }

    private static void createSide(PartDefinition partDefinition, final boolean opposite)
    {
        PartDefinition anchor = partDefinition.addOrReplaceChild("anchor" + opposite, CubeListBuilder.create().texOffs(0, 0)
                        .addBox(-11.5f, -3.0f, -1.0f, 23, 6, 2).mirror(opposite),
                PartPose.offsetAndRotation(0.0f, -1.5f, opposite ? -9.0f : 9.0f, 0, opposite ? 0 : 3.1415927f, 0));

        for (int i = 0; i < 2; i++)
        {
            PartDefinition arm = anchor.addOrReplaceChild("arm" + i + opposite, CubeListBuilder.create().texOffs(0, 8)
                            .addBox(-8.0f, -1.5f, -1.5f, 16, 3, 3).mirror(opposite),
                    PartPose.offsetAndRotation(-8.25f + i * 16.5f, 0.0f, -8.0f, 0, 1.5707964f, 0));

            PartDefinition arm2 = arm.addOrReplaceChild("arm2" + i + opposite, CubeListBuilder.create().texOffs(0, 8)
                            .addBox(-1.5f, -1.5f, -1.5f, 3, 3, 3).mirror(opposite),
                    PartPose.offsetAndRotation(6.5f, 3.0f, 0.0f, 0, 0, 1.5707964f));

            PartDefinition bladePin = arm2.addOrReplaceChild("bladepin" + i + opposite, CubeListBuilder.create().texOffs(0, 8)
                            .addBox(-1.0f, -0.5f, -0.5f, 2, 1, 1).mirror(opposite),
                    PartPose.offsetAndRotation(2.5f, 0.0f, 0.0f, 0, 1.5707964f, 0));

            PartDefinition bladeAnchor = bladePin.addOrReplaceChild("bladeanchor" + i + opposite,
                    CubeListBuilder.create().texOffs(0, 0), PartPose.rotation(0, 0, 0));

            for (int j = 0; j < 4; j++)
            {
                PartDefinition blade = bladeAnchor.addOrReplaceChild("blade" + j + opposite, CubeListBuilder.create().texOffs(0, 22)
                                .addBox(-1.5f, -1.5f, -0.5f, 8, 3, 1).mirror(opposite),
                        PartPose.offsetAndRotation(0.0f, 0.0f, 0.005f, 0, 0, 1.5707964f * (j + i * 0.5f)));

                PartDefinition bladetip = blade.addOrReplaceChild("bladetip" + j + opposite, CubeListBuilder.create().texOffs(0, 22)
                                .addBox(6.5f, -1.0f, -0.5f, 6, 2, 1).mirror(opposite),
                        PartPose.offsetAndRotation(0.0f, 0.0f, 0.005f, 0, 0, 0));
            }
            bladepins.add(bladePin.bake(1, 1));
        }
    }

    @Override
    public void applyEffects(final ModuleBase module, PoseStack matrixStack, MultiBufferSource rtb, final float yaw, final float pitch, final float roll)
    {
        super.applyEffects(module, matrixStack, rtb, yaw, pitch, roll);
        int partialtime = 0;
        final float angle = (module == null) ? 0.0f : (((ModuleFlowerRemover) module).getBladeAngle() + partialtime * ((ModuleFlowerRemover) module).getBladeSpindSpeed());
        for (int i = 0; i < bladepins.size(); ++i)
        {
            final ModelPart bladepin = bladepins.get(i);
            if (i % 2 == 0)
            {
                bladepin.xRot = angle;
            }
            else
            {
                bladepin.xRot = -angle;
            }
        }
    }

}
