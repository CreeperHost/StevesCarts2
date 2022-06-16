package vswe.stevescarts.client.models.engines;

import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import vswe.stevescarts.helpers.ResourceHelper;

public class ModelEngineInside extends ModelEngineBase
{
    public ModelEngineInside()
    {
        super(getTexturedModelData().bakeRoot(), ResourceHelper.getResource("/models/engineModelBack.png"));
    }

    public static LayerDefinition getTexturedModelData()
    {
        MeshDefinition modelData = new MeshDefinition();
        PartDefinition modelPartData = modelData.getRoot();
        modelPartData.addOrReplaceChild("back", CubeListBuilder.create().texOffs(0, 0).addBox(-3.5f, -2.0f, 0.0f, 7, 4, 0), PartPose.offset(0.0f, -0.5f, 0.3f));
        return LayerDefinition.create(modelData, 8, 4);
    }
}
