package vswe.stevescarts.client.models;

import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import vswe.stevescarts.helpers.ResourceHelper;

public class ModelCage extends ModelCartbase
{
    private static final float cageHeight = 26;

    public ModelCage()
    {
        super(getTexturedModelData().bakeRoot(), ResourceHelper.getResource("/models/cageModel.png"));
    }

    public static LayerDefinition getTexturedModelData()
    {
        MeshDefinition modelData = new MeshDefinition();
        PartDefinition modelPartData = modelData.getRoot();
        for (float x = -9.0f; x <= 9.0f; x += 6.0f)
        {
            if (Math.abs(x) != 9.0f)
            {
                createBar(modelPartData, x, 7.0f);
                createBar(modelPartData, x, -7.0f);
            }
            createTopBarShort(modelPartData, x);
        }
        for (float z = -7.0f; z <= 7.0f; z += 4.6666665f)
        {
            createBar(modelPartData, 9.0f, z);
            createBar(modelPartData, -9.0f, z);
            createTopBarLong(modelPartData, z);
        }

        return LayerDefinition.create(modelData, 32, 32);
    }

    private static void createBar(PartDefinition partDefinition, final float offsetX, final float offsetZ)
    {
        partDefinition.addOrReplaceChild("bar" + offsetX, CubeListBuilder.create().texOffs(0, 0)
                .addBox(-0.5f, -cageHeight / 2.0f, -0.5f, 1, cageHeight, 1),
                PartPose.offset(offsetX, (float) (-cageHeight / 2.0 - 4.0), offsetZ));
    }

    private static void createTopBarLong(PartDefinition partDefinition, final float offsetZ)
    {
        partDefinition.addOrReplaceChild("barlong" + offsetZ, CubeListBuilder.create().texOffs(0, 0)
                .addBox(-0.5f, -9.5f, -0.5f, 1, 19, 1),
                PartPose.offsetAndRotation(0.005f, -cageHeight - 4.005f, offsetZ + 0.005f, 0, 0, 1.5707964f));
    }

    private static void createTopBarShort(PartDefinition partDefinition, final float offsetX)
    {
        partDefinition.addOrReplaceChild("barshort" + offsetX, CubeListBuilder.create().texOffs(0, 0)
                .addBox(-0.5f, -7.5f, -0.5f, 1, 15, 1),
                PartPose.offsetAndRotation(offsetX - 0.005f, -cageHeight - 4 + 0.005f, -0.005f, 1.5707964f, 0, 0));
    }
}
