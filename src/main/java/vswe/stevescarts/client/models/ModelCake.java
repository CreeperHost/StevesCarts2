package vswe.stevescarts.client.models;

import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import vswe.stevescarts.api.client.ModelCartbase;
import vswe.stevescarts.helpers.ResourceHelper;

public class ModelCake extends ModelCartbase
{
    public ModelCake()
    {
        super(createBodyLayer().bakeRoot(), ResourceHelper.getResource("/models/cakeModel.png"));
    }

    public static LayerDefinition createBodyLayer()
    {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        for (int i = 0; i < 6; ++i)
        {
            partdefinition.addOrReplaceChild("cake" + i, CubeListBuilder.create().texOffs(0, 22 * (6 - i))
                            .addBox(-7.0f, -4.0f, -7.0f, 2 * i + ((i == 6) ? 2 : 1), 8, 14),
                    PartPose.offset(0.0f, -9.0f, 0.0f));
        }

        return LayerDefinition.create(meshdefinition, 64, 256);
    }

}
