package vswe.stevescarts.client.models.pig;

import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import vswe.stevescarts.api.client.ModelCartbase;
import vswe.stevescarts.helpers.ResourceHelper;

public class ModelPigTail extends ModelCartbase {
    public ModelPigTail() {
        super(getTexturedModelData().bakeRoot(), ResourceHelper.getResource("/models/pigtailModel.png"));
    }

    public static LayerDefinition getTexturedModelData() {
        MeshDefinition modelData = new MeshDefinition();
        PartDefinition modelPartData = modelData.getRoot();

        PartDefinition anchor = modelPartData.addOrReplaceChild("tail_anchor", CubeListBuilder.create(),
                PartPose.offsetAndRotation(10.0F, -4.0F, 0.0F, 0.0F, 1.5707964F, 0.0F));

        anchor.addOrReplaceChild("tail_base", CubeListBuilder.create().texOffs(0, 0)
                        .addBox(-1.5F, -0.5F, 0.0F, 3.0F, 1.0F, 1.0F),
                PartPose.ZERO);
        anchor.addOrReplaceChild("tail_right", CubeListBuilder.create().texOffs(0, 0)
                        .addBox(-0.5F, -1.5F, 0.0F, 1.0F, 3.0F, 1.0F),
                PartPose.offset(2.0F, -2.0F, 0.0F));
        anchor.addOrReplaceChild("tail_top", CubeListBuilder.create().texOffs(0, 0)
                        .addBox(-1.0F, -0.5F, 0.0F, 2.0F, 1.0F, 1.0F),
                PartPose.offset(0.5F, -4.0F, 0.0F));
        anchor.addOrReplaceChild("tail_left", CubeListBuilder.create().texOffs(0, 0)
                        .addBox(-0.5F, -0.5F, 0.0F, 1.0F, 1.0F, 1.0F),
                PartPose.offset(-1.0F, -3.0F, 0.0F));
        anchor.addOrReplaceChild("tail_center", CubeListBuilder.create().texOffs(0, 0)
                        .addBox(-0.5F, -0.5F, 0.0F, 1.0F, 1.0F, 1.0F),
                PartPose.offset(0.0F, -2.0F, 0.0F));

        return LayerDefinition.create(modelData, 32, 32);
    }
}
