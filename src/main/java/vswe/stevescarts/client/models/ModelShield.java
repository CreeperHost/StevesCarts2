package vswe.stevescarts.client.models;

import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import vswe.stevescarts.api.client.ModelCartbase;
import vswe.stevescarts.helpers.ResourceHelper;

public class ModelShield extends ModelCartbase
{
    public ModelShield()
    {
        super(getTexturedModelData().bakeRoot(), ResourceHelper.getResource("/models/shieldModel.png"));
    }

    public static LayerDefinition getTexturedModelData()
    {
        MeshDefinition modelData = new MeshDefinition();
        PartDefinition modelPartData = modelData.getRoot();

        for (int i = 0; i < 4; i++)
        {
            modelPartData.addOrReplaceChild("shieldAnchor" + i, CubeListBuilder.create().texOffs(0, 0)
                            .addBox(-1.0f, -1.0f, -1.0f, 2, 2, 2),
                    PartPose.offset(0.0f, 0.0f, 0.0f));
        }


        return LayerDefinition.create(modelData, 8, 4);
    }
}
