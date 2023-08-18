package vswe.stevescarts.client.models.workers;

import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import vswe.stevescarts.api.client.ModelCartbase;
import vswe.stevescarts.helpers.ResourceHelper;

public class ModelTrackRemover extends ModelCartbase
{
    public ModelTrackRemover()
    {
        super(getTexturedModelData().bakeRoot(), ResourceHelper.getResource("/models/removerModel.png"));
    }

    public static LayerDefinition getTexturedModelData()
    {
        MeshDefinition modelData = new MeshDefinition();
        PartDefinition modelPartData = modelData.getRoot();
//        modelPartData.addOrReplaceChild("sit", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0f, -2.0f, -2.0f, 8, 4, 4), PartPose.offset(0.0f, 1.0f, 0.0f));
//        modelPartData.addOrReplaceChild("back", CubeListBuilder.create().texOffs(0, 8).addBox(-4.0f, -2.0f, -1.0f, 8, 12, 2), PartPose.offset(0.0f, -7.0f, 3.0f));

        //TODO
        //        final ModelRenderer base = new ModelRenderer(this, 0, 0);
        //        AddRenderer(base);
        //        base.addBox(-5.0f, -5.0f, -0.5f, 10, 10, 1, 0.0f);
        //        base.setPos(0.0f, -5.5f, -0.0f);
        //        base.xRot = 1.5707964f;
        //        final ModelRenderer pipe = new ModelRenderer(this, 0, 11);
        //        AddRenderer(pipe);
        //        pipe.addBox(-2.5f, -2.5f, -2.5f, 6, 5, 5, 0.0f);
        //        pipe.setPos(0.0f, -9.5f, -0.0f);
        //        pipe.zRot = 1.5707964f;
        //        final ModelRenderer pipe2 = new ModelRenderer(this, 0, 21);
        //        pipe.addChild(pipe2);
        //        fixSize(pipe2);
        //        pipe2.addBox(-2.5f, -2.5f, -2.5f, 19, 5, 5, 0.0f);
        //        pipe2.setPos(0.005f, -0.005f, -0.005f);
        //        pipe2.zRot = -1.5707964f;
        //        final ModelRenderer pipe3 = new ModelRenderer(this, 22, 0);
        //        pipe2.addChild(pipe3);
        //        fixSize(pipe3);
        //        pipe3.addBox(-2.5f, -2.5f, -2.5f, 14, 5, 5, 0.0f);
        //        pipe3.setPos(14.005f, -0.005f, 0.005f);
        //        pipe3.zRot = 1.5707964f;
        //        final ModelRenderer end = new ModelRenderer(this, 0, 31);
        //        pipe3.addChild(end);
        //        fixSize(end);
        //        end.addBox(-7.0f, -11.0f, -0.5f, 14, 14, 1, 0.0f);
        //        end.setPos(12.0f, 0.0f, -0.0f);
        //        end.yRot = 1.5707964f;


        return LayerDefinition.create(modelData, 32, 32);
    }
}
