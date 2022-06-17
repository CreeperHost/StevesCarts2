package vswe.stevescarts.client.models;

import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.resources.ResourceLocation;
import vswe.stevescarts.helpers.ResourceHelper;
import vswe.stevescarts.modules.ModuleBase;

public class ModelToolPlate extends ModelCartbase
{
    public ModelToolPlate()
    {
        super(getTexturedModelData().bakeRoot(), ResourceHelper.getResource("/models/toolPlateModel.png"));
    }

    public static LayerDefinition getTexturedModelData()
    {
        MeshDefinition modelData = new MeshDefinition();
        PartDefinition modelPartData = modelData.getRoot();

        modelPartData.addOrReplaceChild("base", CubeListBuilder.create().texOffs(0, 0)
                        .addBox(-5.0f, -7.0f, -2.0f, 10, 6, 1),
                PartPose.offsetAndRotation(-9.0f, 4.0f, 0.0f, 0.0f, 1.5707964f, 0.0f));

        return LayerDefinition.create(modelData, 32, 8);
    }

}
