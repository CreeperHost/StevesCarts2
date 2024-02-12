package vswe.stevescarts.client.models.workers;

import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import vswe.stevescarts.api.client.ModelCartbase;
import vswe.stevescarts.helpers.ResourceHelper;

public class ModelTrackRemover extends ModelCartbase {
    public ModelTrackRemover() {
        super(getTexturedModelData().bakeRoot(), ResourceHelper.getResource("/models/removerModel.png"));
    }

    public static LayerDefinition getTexturedModelData() {
        MeshDefinition modelData = new MeshDefinition();
        PartDefinition modelPartData = modelData.getRoot();

        PartDefinition base = modelPartData.addOrReplaceChild("top", CubeListBuilder.create()
                        .addBox(-5.0f, -5.0f, -0.5f, 10, 10, 1),
                PartPose.offsetAndRotation(0.0f, -5.5f, -0.0f, 1.5707964f, 0, 0)
        );

        PartDefinition pipe = modelPartData.addOrReplaceChild("pipe", CubeListBuilder.create()
                        .texOffs(0, 11)
                        .addBox(-2.5f, -2.5f, -2.5f, 6, 5, 5),
                PartPose.offsetAndRotation(0.0f, -9.5f, -0.0f, 0, 0, 1.5707964f)
        );

        PartDefinition pipe2 = pipe.addOrReplaceChild("pipe2", CubeListBuilder.create()
                        .texOffs(0, 21)
                        .addBox(-2.5f, -2.5f, -2.5f, 19, 5, 5),
                PartPose.offsetAndRotation(0.005f, -0.005f, -0.005f, 0, 0, -1.5707964f)
        );

        PartDefinition pipe3 = pipe2.addOrReplaceChild("pipe3", CubeListBuilder.create()
                        .texOffs(22, 0)
                        .addBox(-2.5f, -2.5f, -2.5f, 14, 5, 5),
                PartPose.offsetAndRotation(14.005f, -0.005f, 0.005f, 0, 0, 1.5707964f)
        );

        PartDefinition end = pipe3.addOrReplaceChild("end", CubeListBuilder.create()
                        .texOffs(0, 31)
                        .addBox(-7.0f, -11.0f, -0.5f, 14, 14, 1),
                PartPose.offsetAndRotation(12.0f, 0.0f, -0.0f, 0, 1.5707964f, 0)
        );

        return LayerDefinition.create(modelData, 64, 64);
    }
}
