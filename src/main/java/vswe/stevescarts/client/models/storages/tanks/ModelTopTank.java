package vswe.stevescarts.client.models.storages.tanks;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.fluids.FluidStack;
import vswe.stevescarts.client.models.ModelCartbase;
import vswe.stevescarts.client.renders.fluid.FluidTankRenderType;
import vswe.stevescarts.client.renders.fluid.FluidUtils;
import vswe.stevescarts.client.renders.fluid.RenderUtils;
import vswe.stevescarts.helpers.ResourceHelper;
import vswe.stevescarts.modules.ModuleBase;
import vswe.stevescarts.modules.storages.tanks.ModuleTank;

public class ModelTopTank extends ModelCartbase
{
    public ModelTopTank()
    {
        super(getTexturedModelData().bakeRoot(), ResourceHelper.getResource("/models/tankModelTop.png"));
    }

    public static LayerDefinition getTexturedModelData()
    {
        MeshDefinition modelData = new MeshDefinition();
        PartDefinition modelPartData = modelData.getRoot();
        for (int i = 0; i < 2; ++i) {
            modelPartData.addOrReplaceChild("side" + i, CubeListBuilder.create().texOffs(0, 13).addBox(-8.0f, -2.5f, -0.5f, 16, 5, 1), PartPose.offset(0.0f, -8.5f, -5.5f + i * 11));
            modelPartData.addOrReplaceChild("topbot" + i, CubeListBuilder.create().texOffs(0, 0).addBox(-8.0f, -6.0f, -0.5f, 16, 12, 1), PartPose.offsetAndRotation(0.0f, -5.5f - i * 6, 0.0f, 1.5707964f, 0.0F, 0.0F));
            modelPartData.addOrReplaceChild("frontback" + i, CubeListBuilder.create().texOffs(0, 19).addBox(-5.0f, -2.5f, -0.5f, 10, 5, 1), PartPose.offsetAndRotation(-7.5f + i * 15, -8.5f, 0.0f, 0.0F, 1.5707964f, 0.0F));
        }
        return LayerDefinition.create(modelData, 64, 32);
    }

    @Override
    public RenderType getRenderType(ModuleBase moduleBase)
    {
        return RenderType.entityCutout(getTexture());
    }
}
