package vswe.stevescarts.client.models;

import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.resources.ResourceLocation;
import vswe.stevescarts.helpers.ResourceHelper;
import vswe.stevescarts.modules.ModuleBase;

public class ModelNote extends ModelCartbase
{
    public ModelNote()
    {
        super(getTexturedModelData().bakeRoot(), ResourceHelper.getResource("/models/noteModel.png"));
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
        PartDefinition anchor = partDefinition.addOrReplaceChild("anchor", CubeListBuilder.create().texOffs(0, 0), PartPose.ZERO);
        anchor.addOrReplaceChild("base_" + opposite, CubeListBuilder.create().texOffs(0, 0).mirror(opposite)
                        .addBox(8.0f, 6.0f, 6.0f, 16, 12, 12),
                PartPose.offset(-16.0f, -13.5f, -12.0f + 14.0f * (opposite ? 1 : -1)));
    }
}
