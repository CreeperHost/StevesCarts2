package vswe.stevescarts.client.models;

import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import vswe.stevescarts.api.client.ModelCartbase;
import vswe.stevescarts.api.modules.ModuleBase;
import vswe.stevescarts.helpers.ResourceHelper;

public class ModelCleaner extends ModelCartbase
{

    public ModelCleaner()
    {
        super(getTexturedModelData().bakeRoot(), ResourceHelper.getResource("/models/cleanerModel.png"));
    }

    public static LayerDefinition getTexturedModelData()
    {
        MeshDefinition modelData = new MeshDefinition();
        PartDefinition modelPartData = modelData.getRoot();

        //TODO
        //        final ModelRenderer box = new ModelRenderer(this, 0, 0);
        //        AddRenderer(box);
        //        box.addBox(-4.0f, -3.0f, -4.0f, 8, 6, 8, 0.0f);
        //        box.setPos(4.0f, -0.0f, -0.0f);
        //        for (int i = 0; i < 2; ++i)
        //        {
        //            final ModelRenderer sidetube = new ModelRenderer(this, 0, 14);
        //            AddRenderer(sidetube);
        //            sidetube.addBox(-2.0f, -2.0f, -1.0f, 4, 4, 2, 0.0f);
        //            sidetube.setPos(4.0f, -0.0f, -5.0f * (i * 2 - 1));
        //        }
        //        final ModelRenderer tube = new ModelRenderer(this, 0, 14);
        //        AddRenderer(tube);
        //        tube.addBox(-2.0f, -2.0f, -1.0f, 4, 4, 2, 0.0f);
        //        tube.setPos(-1.0f, 0.0f, 0.0f);
        //        tube.yRot = 1.5707964f;
        //        for (int j = 0; j < 2; ++j)
        //        {
        //            final ModelRenderer endtube = new ModelRenderer(this, 0, 14);
        //            AddRenderer(endtube);
        //            endtube.addBox(-2.0f, -2.0f, -1.0f, 4, 4, 2, 0.0f);
        //            endtube.setPos(-7.0f, -0.0f, -3.0f * (j * 2 - 1));
        //            endtube.yRot = 1.5707964f;
        //        }
        //        final ModelRenderer connectiontube = new ModelRenderer(this, 0, 20);
        //        AddRenderer(connectiontube);
        //        connectiontube.addBox(-5.0f, -5.0f, -1.0f, 10, 4, 4, 0.0f);
        //        connectiontube.setPos(-5.0f, 3.0f, 0.0f);
        //        connectiontube.yRot = 1.5707964f;
        //        for (int k = 0; k < 2; ++k)
        //        {
        //            final ModelRenderer externaltube = new ModelRenderer(this, 0, 14);
        //            AddRenderer(externaltube);
        //            externaltube.addBox(-2.0f, -2.0f, -1.0f, 4, 4, 2, 0.0f);
        //            externaltube.setPos(-10.95f, -0.0f, -3.05f * (k * 2 - 1));
        //            externaltube.yRot = 1.5707964f;
        //        }
        return LayerDefinition.create(modelData, 32, 32);
    }
}
