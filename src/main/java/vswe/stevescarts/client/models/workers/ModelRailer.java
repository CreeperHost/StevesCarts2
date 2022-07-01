package vswe.stevescarts.client.models.workers;

import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import vswe.stevescarts.api.client.ModelCartbase;
import vswe.stevescarts.helpers.ResourceHelper;

public class ModelRailer extends ModelCartbase
{
    private static int railCount;

    public ModelRailer(final int railCount)
    {
        super(getTexturedModelData().bakeRoot(), ResourceHelper.getResource("/models/builderModel.png"));
        this.railCount = railCount;
    }

    public static LayerDefinition getTexturedModelData()
    {
        MeshDefinition modelData = new MeshDefinition();
        PartDefinition modelPartData = modelData.getRoot();
        for (int i = 0; i < railCount; i++)
        {
            PartDefinition railAnchor = modelPartData.addOrReplaceChild("railAnchor" + i, CubeListBuilder.create().texOffs(0, 0),
                    PartPose.offset(0.0f, (-i), 0.0f));

            PartDefinition rail1 = railAnchor.addOrReplaceChild("rail1" + i, CubeListBuilder.create().texOffs(24, 0)
                    .addBox(1.0f, 8.0f, 0.5f, 2, 16, 1),
                    PartPose.offsetAndRotation(-16.0f, -6.5f, -7.0f, 0, 4.712389f, 4.712389f));

            PartDefinition rail2 = railAnchor.addOrReplaceChild("rail2" + i, CubeListBuilder.create().texOffs(24, 0)
                            .addBox(1.0f, 8.0f, 0.5f, 2, 16, 1),
                    PartPose.offsetAndRotation(-16.0f, -6.5f, 3.0f, 0, 4.712389f, 4.712389f));

            for (int z = 0; z < 4; ++z)
            {
                PartDefinition railbedMiddle = railAnchor.addOrReplaceChild("railbedMiddle" + z, CubeListBuilder.create().texOffs(0, 0)
                                .addBox(4.0f, 1.0f, 0.5f, 8, 2, 1),
                        PartPose.offsetAndRotation(-8.0f + z * 4, -6.5f, -8.0f, 0, 4.712389f, 4.712389f));

                PartDefinition railbedSide1 = railAnchor.addOrReplaceChild("railbedSide1" + z, CubeListBuilder.create().texOffs(0, 3)
                                .addBox(0.5f, 1.0f, 0.5f, 1, 2, 1),
                        PartPose.offsetAndRotation(-8.0f + z * 4, -6.5f, -7.5f, 0, 4.712389f, 4.712389f));

                PartDefinition railbedSide2 = railAnchor.addOrReplaceChild("railbedSide2" + z, CubeListBuilder.create().texOffs(0, 3)
                                .addBox(0.5f, 1.0f, 0.5f, 1, 2, 1),
                        PartPose.offsetAndRotation(-8.0f + z * 4, -6.5f, 5.5f, 0, 4.712389f, 4.712389f));
            }
        }

        return LayerDefinition.create(modelData, 32, 32);
    }

}
