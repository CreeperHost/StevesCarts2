package vswe.stevescarts.client.models.storages.chests;

import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import vswe.stevescarts.api.client.ModelCartbase;
import vswe.stevescarts.helpers.ResourceHelper;

public class ModelSideChests extends ModelCartbase
{
    public ModelSideChests()
    {
        super(getTexturedModelData().bakeRoot(), ResourceHelper.getResource("/models/sideChestsModel.png"));
    }

    public static LayerDefinition getTexturedModelData()
    {
        MeshDefinition modelData = new MeshDefinition();
        PartDefinition modelPartData = modelData.getRoot();
        createSide(modelPartData, false);
        createSide(modelPartData, true);

        return LayerDefinition.create(modelData, 64, 32);
    }

    private static void createSide(PartDefinition partDefinition, final boolean opposite)
    {
        PartDefinition base = partDefinition.addOrReplaceChild("base_" + opposite, CubeListBuilder.create().texOffs(0, 7).mirror(opposite)
                .addBox(8.0f, 3.0f, 2.0f, 16, 6, 4),
                PartPose.offsetAndRotation(opposite ? -16.0f : 16.0f, -5.5f, opposite ? -14.0f : 14.0f, 0, opposite ? 0 : 3.1415927f, 0));
        partDefinition.addOrReplaceChild("lid_left_" + opposite, CubeListBuilder.create().texOffs(0, 0).mirror(opposite)
                .addBox(8.0f, -3.0f, -4.0f, 16, 3, 4),
                PartPose.offsetAndRotation(opposite ? -16.0f : 16.0f, -1.5f, opposite ? -8.0f : 8.0f, 0, opposite ? 0 : 3.1415927f, 0));
        partDefinition.addOrReplaceChild("lock_left_" + opposite, CubeListBuilder.create().texOffs(0, 17).mirror(opposite)
                .addBox(-15.0f, -1.5f, -7.5f, 2, 3, 1),
                PartPose.offsetAndRotation(opposite ? 14.0f : -14.0f, -1.5f, opposite ? -5.5f : 5.5f, 0, opposite ? 0 : 3.1415927f, 0));
    }
}
