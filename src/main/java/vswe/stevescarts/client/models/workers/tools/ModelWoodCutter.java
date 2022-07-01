package vswe.stevescarts.client.models.workers.tools;

import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.resources.ResourceLocation;
import vswe.stevescarts.api.client.ModelCartbase;

public class ModelWoodCutter extends ModelCartbase
{
    public ModelWoodCutter(final ResourceLocation resource)
    {
        super(getTexturedModelData().bakeRoot(), resource);
    }

    public static LayerDefinition getTexturedModelData()
    {
        MeshDefinition modelData = new MeshDefinition();
        PartDefinition modelPartData = modelData.getRoot();

        for (int i = -2; i <= 2; ++i)
        {
            modelPartData.addOrReplaceChild("side" + i, CubeListBuilder.create().texOffs(0, 0).addBox(-3.5f, -1.5f, -0.5f, 7, 3, 1), PartPose.offset(-13.0f, 0.0f, i * 2));

            modelPartData.addOrReplaceChild("tip" + i, CubeListBuilder.create().texOffs(0, 0).addBox(-0.5f, -0.5f, -0.5f, 1, 1, 1), PartPose.offset(-4.0f, 0.0f, 0.0f));
        }
        return LayerDefinition.create(modelData, 16, 8);
    }
}
