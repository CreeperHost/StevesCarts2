package vswe.stevescarts.client.models.storages.tanks;

import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.renderer.RenderType;
import vswe.stevescarts.api.client.ModelCartbase;
import vswe.stevescarts.helpers.ResourceHelper;
import vswe.stevescarts.api.modules.ModuleBase;

public class ModelFrontTank extends ModelCartbase
{
    public ModelFrontTank()
    {
        super(getTexturedModelData().bakeRoot(), ResourceHelper.getResource("/models/tankModelFront.png"));
    }

    public static LayerDefinition getTexturedModelData()
    {
        MeshDefinition modelData = new MeshDefinition();
        PartDefinition modelPartData = modelData.getRoot();
        for (int i = 0; i < 2; ++i)
        {
            modelPartData.addOrReplaceChild("side" + i, CubeListBuilder.create().texOffs(0, 15).addBox(-4.0f, -3.0f, -0.5f, 8, 6, 1), PartPose.offset(-14.0f, 0.0f, -6.5f + i * 13));
            modelPartData.addOrReplaceChild("topbot" + i, CubeListBuilder.create().texOffs(0, 0).addBox(-4.0f, -7.0f, -0.5f, 8, 14, 1), PartPose.offsetAndRotation(-14.0f, 3.5f - i * 7, 0.0f, 1.5707964f, 0.0F, 0.0F));
            modelPartData.addOrReplaceChild("frontback" + i, CubeListBuilder.create().texOffs(0, 22).addBox(-6.0f, -3.0f, -0.5f, 12, 6, 1), PartPose.offsetAndRotation(-17.5f + i * 7, 0.0f, 0.0f, 0.0F, 1.5707964f, 0.0F));
        }
        return LayerDefinition.create(modelData, 32, 32);
    }

    @Override
    public RenderType getRenderType(ModuleBase moduleBase)
    {
        return RenderType.entityCutout(getTexture());
    }

    //TODO
    //    @Override
    //    public void applyEffects(ModuleBase module, PoseStack matrixStack, VertexConsumer rtb, float yaw, float pitch, float roll)
    //    {
    //        super.applyEffects(module, matrixStack, rtb, yaw, pitch, roll);
    //        ModuleTank moduleTank = (ModuleTank) module;
    //        FluidStack fluidStack = moduleTank.getFluid();
    //        int light = 15;
    //        if(fluidStack != null && !fluidStack.isEmpty())
    //        {
    //            matrixStack.pushPose();
    //            //TODO
    ////            RenderSystem.color3f(0F, 0F, 0F);
    ////            IVertexBuilder buffer = rtb.getBuffer(FluidTankRenderType.RESIZABLE);
    //            matrixStack.mulPose(Vector3f.XP.rotationDegrees(180.0F));
    //            matrixStack.translate(-1.1, -0.25D, -0.4);
    //            matrixStack.scale(0.5F, FluidUtils.getScale(moduleTank.getFluidAmount(), moduleTank.getCapacity(), fluidStack.isEmpty()) / 2, 0.8F);
    ////            RenderUtils.renderObject(FluidUtils.getFluidModel(fluidStack, FluidUtils.STAGES + 1), matrixStack, buffer, RenderUtils.getColorARGB(fluidStack, 0.2F),
    ////                    RenderUtils.calculateGlowLight(light, fluidStack));
    //
    //            matrixStack.popPose();
    //        }
    //    }
}
