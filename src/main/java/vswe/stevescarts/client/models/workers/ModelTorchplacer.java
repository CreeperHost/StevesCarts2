package vswe.stevescarts.client.models.workers;

import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import vswe.stevescarts.api.client.ModelCartbase;
import vswe.stevescarts.helpers.ResourceHelper;

public class ModelTorchplacer extends ModelCartbase
{
    public ModelTorchplacer()
    {
        super(getTexturedModelData().bakeRoot(), ResourceHelper.getResource("/models/torchModel.png"));
    }

    public static LayerDefinition getTexturedModelData()
    {
        MeshDefinition modelData = new MeshDefinition();
        PartDefinition modelPartData = modelData.getRoot();
        modelPartData.addOrReplaceChild("base", CubeListBuilder.create().texOffs(0, 0).addBox(-7.0f, -2.0f, -1.0f, 14, 4, 2), PartPose.offset(0.0f, -2.0f, -9.0f));

        for (int i = -1; i <= 1; ++i)
        {
            modelPartData.addOrReplaceChild("holder" + (i + 1), CubeListBuilder.create().texOffs(0, 6).addBox(-1.0f, -1.0f, -0.5f, 2, 2, 1), PartPose.offset(i * 4, -2.0f, -10.5f));
            modelPartData.addOrReplaceChild("torch" + (i + 1), CubeListBuilder.create().texOffs(0, 9).addBox(-1.0f, -5.0f, -1.0f, 2, 10, 2), PartPose.offset(i * 4, -2.0f, -12.0f));
        }
        return LayerDefinition.create(modelData, 32, 32);
    }
}
