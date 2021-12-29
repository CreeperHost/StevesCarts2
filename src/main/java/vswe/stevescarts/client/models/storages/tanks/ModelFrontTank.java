package vswe.stevescarts.client.models.storages.tanks;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraftforge.fluids.FluidStack;
import vswe.stevescarts.client.models.ModelCartbase;
import vswe.stevescarts.client.renders.fluid.FluidTankRenderType;
import vswe.stevescarts.client.renders.fluid.FluidUtils;
import vswe.stevescarts.client.renders.fluid.RenderUtils;
import vswe.stevescarts.helpers.ResourceHelper;
import vswe.stevescarts.modules.ModuleBase;
import vswe.stevescarts.modules.storages.tanks.ModuleTank;

public class ModelFrontTank extends ModelCartbase
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
        return ModelFrontTank.texture;
    }

    @Override
    protected int getTextureWidth()
    {
        return 32;
    }

    @Override
    protected int getTextureHeight()
    {
        return 32;
    }

    public ModelFrontTank()
    {
        super();
        for (int i = 0; i < 2; ++i)
        {
            final ModelRenderer tankside = new ModelRenderer(this, 0, 15);
            AddRenderer(tankside);
            tankside.addBox(-4.0f, -3.0f, -0.5f, 8, 6, 1, 0.0f);
            tankside.setPos(-14.0f, 0.0f, -6.5f + i * 13);
            final ModelRenderer tanktopbot = new ModelRenderer(this, 0, 0);
            AddRenderer(tanktopbot);
            tanktopbot.addBox(-4.0f, -7.0f, -0.5f, 8, 14, 1, 0.0f);
            tanktopbot.setPos(-14.0f, 3.5f - i * 7, 0.0f);
            tanktopbot.xRot = 1.5707964f;
            final ModelRenderer tankfrontback = new ModelRenderer(this, 0, 22);
            AddRenderer(tankfrontback);
            tankfrontback.addBox(-6.0f, -3.0f, -0.5f, 12, 6, 1, 0.0f);
            tankfrontback.setPos(-17.5f + i * 7, 0.0f, 0.0f);
            tankfrontback.yRot = 1.5707964f;
        }
    }

    @Override
    public void applyEffects(ModuleBase module, MatrixStack matrixStack, IRenderTypeBuffer rtb, float yaw, float pitch, float roll)
    {
        super.applyEffects(module, matrixStack, rtb, yaw, pitch, roll);
        ModuleTank moduleTank = (ModuleTank) module;
        FluidStack fluidStack = moduleTank.getFluid();
        int light = 15;
        if(fluidStack != null && !fluidStack.isEmpty())
        {
            matrixStack.pushPose();
            RenderSystem.color3f(0F, 0F, 0F);
            IVertexBuilder buffer = rtb.getBuffer(FluidTankRenderType.RESIZABLE);
            matrixStack.mulPose(Vector3f.XP.rotationDegrees(180.0F));
            matrixStack.translate(-1.1, -0.25D, -0.4);
            matrixStack.scale(0.5F, FluidUtils.getScale(moduleTank.getFluidAmount(), moduleTank.getCapacity(), fluidStack.isEmpty()) / 2, 0.8F);
            RenderUtils.renderObject(FluidUtils.getFluidModel(fluidStack, FluidUtils.STAGES + 1), matrixStack, buffer, RenderUtils.getColorARGB(fluidStack, 0.2F),
                    RenderUtils.calculateGlowLight(light, fluidStack));

            matrixStack.popPose();
        }
    }

    static
    {
        ModelFrontTank.texture = ResourceHelper.getResource("/models/tankModelFront.png");
    }
}
