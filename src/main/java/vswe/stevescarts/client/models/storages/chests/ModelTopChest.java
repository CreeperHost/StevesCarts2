package vswe.stevescarts.client.models.storages.chests;

import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import vswe.stevescarts.client.models.ModelCartbase;
import vswe.stevescarts.helpers.ResourceHelper;

public class ModelTopChest extends ModelCartbase
{
    public ModelTopChest()
    {
        super(getTexturedModelData().bakeRoot(), ResourceHelper.getResource("/models/topChestModel.png"));
        this.root.setPos(-2.5f, -3.0f, 2.0f);
        this.root.setRotation(0.0f, 4.712389f, 0.0f);
    }

    public static LayerDefinition getTexturedModelData()
    {
        MeshDefinition modelData = new MeshDefinition();
        PartDefinition modelPartData = modelData.getRoot();
        modelPartData.addOrReplaceChild("base", CubeListBuilder.create().texOffs(0, 19).addBox(6.0f, 2.0f, 8.0f, 12, 4, 16), PartPose.offset(-14.0f, -5.5f, -18.5f));

        modelPartData.addOrReplaceChild("lid", CubeListBuilder.create().texOffs(0, 0).addBox(6.0f, -3.0f, -16.0f, 12, 3, 16), PartPose.offset(-14.0f, -2.5f, 5.5f));

        return LayerDefinition.create(modelData, 64, 64);
    }
}
