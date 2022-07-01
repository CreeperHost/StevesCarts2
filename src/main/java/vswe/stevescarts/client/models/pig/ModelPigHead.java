package vswe.stevescarts.client.models.pig;

import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import vswe.stevescarts.api.client.ModelCartbase;
import vswe.stevescarts.helpers.ResourceHelper;

public class ModelPigHead extends ModelCartbase
{
    public ModelPigHead()
    {
        super(getTexturedModelData().bakeRoot(), ResourceHelper.getResourceFromPath("/entity/pig/pig.png"));
        //TODO
        //        final ModelRenderer head = new ModelRenderer(this, 0, 0);
        //        AddRenderer(head);
        //        head.addBox(-4.0f, -4.0f, -8.0f, 8, 8, 8, 0.0f);
        //        head.setPos(-9.0f, -5.0f, 0.0f);
        //        head.yRot = 1.5707964f;
        //        head.texOffs(16, 16).addBox(-2.0f, 0.0f, -9.0f, 4, 3, 1, 0.0f);
    }

    public static LayerDefinition getTexturedModelData()
    {
        MeshDefinition modelData = new MeshDefinition();
        PartDefinition modelPartData = modelData.getRoot();

        PartDefinition head = modelPartData.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 0)
                        .addBox(-4.0f, -4.0f, -8.0f, 8, 8, 8),
                PartPose.offsetAndRotation(-9.0f, -5.0f, 0.0f, 0.0f, 1.5707964f, 0.0f));

        head.addOrReplaceChild("box", CubeListBuilder.create().texOffs(16, 16)
                .addBox(-2.0f, 0.0f, -9.0f, 4, 3, 1),
                PartPose.ZERO);

        return LayerDefinition.create(modelData, 64, 32);
    }
}
