package vswe.stevescarts.client.models.storages.tanks;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
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

public class ModelSideTanks extends ModelCartbase
{
    private static ResourceLocation texture;

    @Override
    public RenderType getRenderType(ModuleBase moduleBase)
    {
        return RenderType.entityCutout(getResource(moduleBase));
    }

    @Override
    public ResourceLocation getResource(final ModuleBase module)
    {
        return ModelSideTanks.texture;
    }

    @Override
    protected int getTextureHeight()
    {
        return 16;
    }

    public ModelSideTanks()
    {
        super();
        //TODO
//        for (int i = 0; i < 2; ++i)
//        {
//            for (int j = 0; j < 2; ++j)
//            {
//                final ModelRenderer tankside = new ModelRenderer(this, 0, 0);
//                AddRenderer(tankside);
//                tankside.addBox(-6.0f, -3.0f, -0.5f, 12, 6, 1, 0.0f);
//                tankside.setPos(-2.0f, -0.5f, -10.5f + i * 22 - 3.0f + j * 5);
//                final ModelRenderer tanktopbot = new ModelRenderer(this, 0, 7);
//                AddRenderer(tanktopbot);
//                tanktopbot.addBox(-6.0f, -2.0f, -0.5f, 12, 4, 1, 0.0f);
//                tanktopbot.setPos(-2.0f, -3.0f + j * 5, -11.0f + i * 22);
//                tanktopbot.xRot = 1.5707964f;
//            }
//            final ModelRenderer tankfront = new ModelRenderer(this, 26, 0);
//            AddRenderer(tankfront);
//            tankfront.addBox(-2.0f, -2.0f, -0.5f, 4, 4, 1, 0.0f);
//            tankfront.setPos(-7.5f, -0.5f, -11.0f + i * 22);
//            tankfront.yRot = 1.5707964f;
//            final ModelRenderer tankback = new ModelRenderer(this, 36, 0);
//            AddRenderer(tankback);
//            tankback.addBox(-2.0f, -2.0f, -0.5f, 4, 4, 1, 0.0f);
//            tankback.setPos(4.5f, -0.5f, -11.0f + i * 22);
//            tankback.yRot = 1.5707964f;
//            final ModelRenderer tube1 = new ModelRenderer(this, 26, 5);
//            AddRenderer(tube1);
//            tube1.addBox(-1.0f, -1.0f, -1.0f, 2, 2, 2, 0.0f);
//            tube1.setPos(5.5f, -0.5f, -11.0f + i * 22);
//            final ModelRenderer tube2 = new ModelRenderer(this, 26, 5);
//            AddRenderer(tube2);
//            tube2.addBox(-2.0f, -1.0f, -1.0f, 4, 2, 2, 0.0f);
//            tube2.setPos(7.5f, -0.5f, -10.0f + i * 20);
//            tube2.yRot = 1.5707964f;
//            final ModelRenderer connection = new ModelRenderer(this, 36, 0);
//            AddRenderer(connection);
//            connection.addBox(-2.0f, -2.0f, -0.5f, 4, 4, 1, 0.0f);
//            connection.setPos(7.5f, -0.5f, -8.5f + i * 17);
//        }
    }

    @Override
    public void applyEffects(ModuleBase module, PoseStack matrixStack, VertexConsumer rtb, float yaw, float pitch, float roll)
    {
        super.applyEffects(module, matrixStack, rtb, yaw, pitch, roll);
        ModuleTank moduleTank = (ModuleTank) module;
        FluidStack fluidStack = moduleTank.getFluid();
        int light = 15;
        if(fluidStack != null && !fluidStack.isEmpty())
        {
            //TODO
//            matrixStack.pushPose();
//            RenderSystem.color3f(0F, 0F, 0F);
//            IVertexBuilder buffer = rtb.getBuffer(FluidTankRenderType.RESIZABLE);
//            //Left
//            matrixStack.mulPose(Vector3f.XP.rotationDegrees(180.0F));
//            matrixStack.translate(-0.45, -0.15F, -0.85);
//            matrixStack.scale(0.7F, (FluidUtils.getScale(moduleTank.getFluidAmount(), moduleTank.getCapacity(), fluidStack.isEmpty()) / 3), 0.3F);
//            RenderUtils.renderObject(FluidUtils.getFluidModel(fluidStack, FluidUtils.STAGES + 1), matrixStack, buffer, RenderUtils.getColorARGB(fluidStack, 0.2F),
//                    RenderUtils.calculateGlowLight(light, fluidStack));
//            matrixStack.popPose();
//
//            //Right
//            matrixStack.pushPose();
//            matrixStack.mulPose(Vector3f.XP.rotationDegrees(180.0F));
//            matrixStack.translate(-0.45, -0.15, 0.55);
//            matrixStack.scale(0.7F, FluidUtils.getScale(moduleTank.getFluidAmount(), moduleTank.getCapacity(), fluidStack.isEmpty()) / 3, 0.3F);
//            RenderUtils.renderObject(FluidUtils.getFluidModel(fluidStack, FluidUtils.STAGES + 1), matrixStack, buffer, RenderUtils.getColorARGB(fluidStack, 0.2F),
//                    RenderUtils.calculateGlowLight(light, fluidStack));
//            matrixStack.popPose();
        }
    }

    static
    {
        ModelSideTanks.texture = ResourceHelper.getResource("/models/tanksModel.png");
    }
}
