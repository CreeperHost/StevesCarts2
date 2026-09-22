package vswe.stevescarts.client.models.storages.tanks;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.SubmitNodeCollector;
import vswe.stevescarts.api.client.ModelCartbase;
import vswe.stevescarts.api.modules.ModuleBase;
import vswe.stevescarts.client.renders.FluidTankRenderer;
import vswe.stevescarts.helpers.ResourceHelper;
import vswe.stevescarts.modules.storages.tanks.ModuleTank;

public class ModelFrontTank extends ModelCartbase {
    public ModelFrontTank() {
        super(getTexturedModelData().bakeRoot(), ResourceHelper.getResource("/models/tankModelFront.png"));
    }

    public static LayerDefinition getTexturedModelData() {
        MeshDefinition modelData = new MeshDefinition();
        PartDefinition modelPartData = modelData.getRoot();
        for (int i = 0; i < 2; ++i) {
            modelPartData.addOrReplaceChild("side" + i, CubeListBuilder.create().texOffs(0, 15).addBox(-4.0f, -3.0f, -0.5f, 8, 6, 1), PartPose.offset(-14.0f, 0.0f, -6.5f + i * 13));
            modelPartData.addOrReplaceChild("topbot" + i, CubeListBuilder.create().texOffs(0, 0).addBox(-4.0f, -7.0f, -0.5f, 8, 14, 1), PartPose.offsetAndRotation(-14.0f, 3.5f - i * 7, 0.0f, 1.5707964f, 0.0F, 0.0F));
            modelPartData.addOrReplaceChild("frontback" + i, CubeListBuilder.create().texOffs(0, 22).addBox(-6.0f, -3.0f, -0.5f, 12, 6, 1), PartPose.offsetAndRotation(-17.5f + i * 7, 0.0f, 0.0f, 0.0F, 1.5707964f, 0.0F));
        }
        return LayerDefinition.create(modelData, 32, 32);
    }

    @Override
    public RenderType getRenderType(ModuleBase moduleBase) {
        return RenderTypes.entityCutout(getTexture());
    }

    @Override
    public void submitExtraGeometry(ModuleBase module, PoseStack poseStack, SubmitNodeCollector nodeCollector, int light) {
        FluidTankRenderer.submit((ModuleTank) module, poseStack, nodeCollector, light,
                -1.05F, -0.175F, -0.3625F, -0.70F, 0.175F, 0.3625F);
    }
}
