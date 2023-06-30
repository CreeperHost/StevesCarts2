package vswe.stevescarts.client.models.storages.tanks;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.creeperhost.polylib.client.render.RenderUtils;
import net.creeperhost.polylib.client.render.fluid.FluidRenderHelper;
import net.creeperhost.polylib.client.render.rendertypes.FluidTankRenderType;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraftforge.fluids.FluidStack;
import vswe.stevescarts.api.client.ModelCartbase;
import vswe.stevescarts.api.modules.ModuleBase;
import vswe.stevescarts.helpers.ResourceHelper;
import vswe.stevescarts.modules.storages.tanks.ModuleTank;

public class ModelSideTanks extends ModelCartbase
{
    public ModelSideTanks()
    {
        super(getTexturedModelData().bakeRoot(), ResourceHelper.getResource("/models/tanksModel.png"));
    }

    public static LayerDefinition getTexturedModelData()
    {
        MeshDefinition modelData = new MeshDefinition();
        PartDefinition modelPartData = modelData.getRoot();
        for (int i = 0; i < 2; ++i)
        {
            for (int j = 0; j < 2; ++j)
            {
                modelPartData.addOrReplaceChild("side" + i + j, CubeListBuilder.create().texOffs(0, 0).addBox(-6.0f, -3.0f, -0.5f, 12, 6, 1), PartPose.offset(-2.0f, -0.5f, -10.5f + i * 22 - 3.0f + j * 5));
                modelPartData.addOrReplaceChild("topbot" + i + j, CubeListBuilder.create().texOffs(0, 7).addBox(-6.0f, -2.0f, -0.5f, 12, 4, 1), PartPose.offsetAndRotation(-2.0f, -3.0f + j * 5, -11.0f + i * 22, 1.5707964f, 0.0F, 0.0F));
            }
            modelPartData.addOrReplaceChild("front" + i, CubeListBuilder.create().texOffs(26, 0).addBox(-2.0f, -2.0f, -0.5f, 4, 4, 1), PartPose.offsetAndRotation(-7.5f, -0.5f, -11.0f + i * 22, 0.0F, 1.5707964f, 0.0F));
            modelPartData.addOrReplaceChild("back" + i, CubeListBuilder.create().texOffs(36, 0).addBox(-2.0f, -2.0f, -0.5f, 4, 4, 1), PartPose.offsetAndRotation(4.5f, -0.5f, -11.0f + i * 22, 0.0F, 1.5707964f, 0.0F));
            modelPartData.addOrReplaceChild("tube1" + i, CubeListBuilder.create().texOffs(26, 5).addBox(-1.0f, -1.0f, -1.0f, 2, 2, 2), PartPose.offset(5.5f, -0.5f, -11.0f + i * 22));
            modelPartData.addOrReplaceChild("tube2" + i, CubeListBuilder.create().texOffs(26, 5).addBox(-2.0f, -1.0f, -1.0f, 4, 2, 2), PartPose.offsetAndRotation(7.5f, -0.5f, -10.0f + i * 20, 0.0F, 1.5707964f, 0.0F));
            modelPartData.addOrReplaceChild("connection" + i, CubeListBuilder.create().texOffs(36, 0).addBox(-2.0f, -2.0f, -0.5f, 4, 4, 1), PartPose.offset(7.5f, -0.5f, -8.5f + i * 17));
        }
        return LayerDefinition.create(modelData, 64, 16);
    }

    @Override
    public RenderType getRenderType(ModuleBase moduleBase)
    {
        return RenderType.entityCutout(getTexture());
    }

    @Override
    public void applyEffects(ModuleBase module, PoseStack matrixStack, MultiBufferSource rtb, float yaw, float pitch, float roll)
    {
        super.applyEffects(module, matrixStack, rtb, yaw, pitch, roll);
        ModuleTank moduleTank = (ModuleTank) module;
        FluidStack fluidStack = moduleTank.getFluid();
        int light = 15;
        if(fluidStack != null && !fluidStack.isEmpty())
        {
            matrixStack.pushPose();
            VertexConsumer buffer = rtb.getBuffer(FluidTankRenderType.RESIZABLE);
            //Left
            matrixStack.mulPose(Axis.XP.rotationDegrees(180.0F));
            matrixStack.translate(-0.45, -0.15F, -0.85);

            dev.architectury.fluid.FluidStack fluidStackA = dev.architectury.fluid.FluidStack.create(fluidStack.getFluid(), fluidStack.getAmount());


            matrixStack.scale(0.7F, (FluidRenderHelper.getScale(moduleTank.getFluidAmount(), moduleTank.getCapacity(), fluidStack.isEmpty()) / 3), 0.3F);
            RenderUtils.renderObject(FluidRenderHelper.getFluidModel(fluidStackA, FluidRenderHelper.STAGES + 1), matrixStack, buffer, RenderUtils.getColorARGB(fluidStackA, 0.2F),
                    RenderUtils.calculateGlowLight(light, fluidStackA));
            matrixStack.popPose();

            //Right
            matrixStack.pushPose();
            matrixStack.mulPose(Axis.XP.rotationDegrees(180.0F));
            matrixStack.translate(-0.45, -0.15, 0.55);
            matrixStack.scale(0.7F, FluidRenderHelper.getScale(moduleTank.getFluidAmount(), moduleTank.getCapacity(), fluidStack.isEmpty()) / 3, 0.3F);
            RenderUtils.renderObject(FluidRenderHelper.getFluidModel(fluidStackA, FluidRenderHelper.STAGES + 1), matrixStack, buffer, RenderUtils.getColorARGB(fluidStackA, 0.2F),
                    RenderUtils.calculateGlowLight(light, fluidStackA));
            matrixStack.popPose();
        }
    }
}
