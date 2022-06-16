package vswe.stevescarts.client.models.storages.tanks;

import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.renderer.RenderType;
import vswe.stevescarts.client.models.ModelCartbase;
import vswe.stevescarts.helpers.ResourceHelper;
import vswe.stevescarts.modules.ModuleBase;

public class ModelAdvancedTank extends ModelCartbase
{
    public ModelAdvancedTank()
    {
        super(getTexturedModelData().bakeRoot(), ResourceHelper.getResource("/models/tankModelLarge.png"));
    }

    public static LayerDefinition getTexturedModelData()
    {
        MeshDefinition modelData = new MeshDefinition();
        PartDefinition modelPartData = modelData.getRoot();
        for (int i = 0; i < 2; ++i)
        {
            modelPartData.addOrReplaceChild("side" + i, CubeListBuilder.create().texOffs(0, 13).addBox(-8.0f, -6.5f, -0.5f, 16, 13, 1), PartPose.offset(0.0f, -4.5f, -5.5f + i * 11));
            modelPartData.addOrReplaceChild("topbot" + i, CubeListBuilder.create().texOffs(0, 0).addBox(-8.0f, -6.0f, -0.5f, 16, 12, 1), PartPose.offsetAndRotation(0.0f, 2.5f - i * 14, 0.0f, 1.5707964f, 0.0F, 0.0F));
            modelPartData.addOrReplaceChild("frontback" + i, CubeListBuilder.create().texOffs(0, 27).addBox(-5.0f, -6.5f, -0.5f, 10, 13, 1), PartPose.offsetAndRotation(-7.5f + i * 15, -4.5f, 0.0f, 0.0F, 1.5707964f, 0.0F));
        }
        return LayerDefinition.create(modelData, 64, 64);
    }

    @Override
    public RenderType getRenderType(ModuleBase moduleBase)
    {
        return RenderType.entityCutout(getTexture());
    }
}
