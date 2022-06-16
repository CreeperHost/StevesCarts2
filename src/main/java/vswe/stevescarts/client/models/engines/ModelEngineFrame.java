package vswe.stevescarts.client.models.engines;

import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import vswe.stevescarts.helpers.ResourceHelper;

public class ModelEngineFrame extends ModelEngineBase
{
    public ModelEngineFrame()
    {
        super(getTexturedModelData().bakeRoot(), ResourceHelper.getResource("/models/engineModelFrame.png"));
    }

    public static LayerDefinition getTexturedModelData()
    {
        MeshDefinition modelData = new MeshDefinition();
        PartDefinition modelPartData = modelData.getRoot();
        modelPartData.addOrReplaceChild("left", CubeListBuilder.create().texOffs(0, 0)
                .addBox(-0.5f, -2.5f, -0.5f, 1, 5, 1), PartPose.offset(-4.0f, 0.0f, 0.0f));
        modelPartData.addOrReplaceChild("right", CubeListBuilder.create().texOffs(0, 0)
                .addBox(-0.5f, -2.5f, -0.5f, 1, 5, 1), PartPose.offset(4.0f, 0.0f, 0.0f));
        modelPartData.addOrReplaceChild("top", CubeListBuilder.create().texOffs(4, 0)
                .addBox(-0.5f, -3.5f, -0.5f, 1, 7, 1), PartPose.offsetAndRotation(0.0f, -3.0f, 0.0f, 0.0f, 0.0f, 1.5707964f));
        modelPartData.addOrReplaceChild("bot", CubeListBuilder.create().texOffs(4, 0)
                .addBox(-0.5f, -3.5f, -0.5f, 1, 7, 1), PartPose.offsetAndRotation(0.0f, 2.0f, 0.0f, 0.0f, 0.0f, 1.5707964f));
        return LayerDefinition.create(modelData, 8, 8);
    }
}
