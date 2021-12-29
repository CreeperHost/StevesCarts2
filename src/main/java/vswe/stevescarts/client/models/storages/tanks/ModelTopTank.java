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

public class ModelTopTank extends ModelCartbase
{
    private static ResourceLocation texture;
    private static ResourceLocation textureOpen;
    private boolean open;

    @Override
    public RenderType getRenderType(ModuleBase moduleBase)
    {
        return RenderType.entityCutout(getResource(moduleBase));
    }

    @Override
    public ResourceLocation getResource(final ModuleBase module)
    {
        return open ? ModelTopTank.textureOpen : ModelTopTank.texture;
    }

    @Override
    protected int getTextureHeight()
    {
        return 32;
    }

    public ModelTopTank(final boolean open)
    {
        super();
        this.open = open;
        for (int i = 0; i < 2; ++i)
        {
            final ModelRenderer tankside = new ModelRenderer(this, 0, 13);
            AddRenderer(tankside);
            tankside.addBox(-8.0f, -2.5f, -0.5f, 16, 5, 1, 0.0f);
            tankside.setPos(0.0f, -8.5f, -5.5f + i * 11);
            if (!open || i == 0)
            {
                final ModelRenderer tanktopbot = new ModelRenderer(this, 0, 0);
                AddRenderer(tanktopbot);
                tanktopbot.addBox(-8.0f, -6.0f, -0.5f, 16, 12, 1, 0.0f);
                tanktopbot.setPos(0.0f, -5.5f - i * 6, 0.0f);
                tanktopbot.xRot = 1.5707964f;
            }
            final ModelRenderer tankfrontback = new ModelRenderer(this, 0, 19);
            AddRenderer(tankfrontback);
            tankfrontback.addBox(-5.0f, -2.5f, -0.5f, 10, 5, 1, 0.0f);
            tankfrontback.setPos(-7.5f + i * 15, -8.5f, 0.0f);
            tankfrontback.yRot = 1.5707964f;
        }
    }

    static
    {
        ModelTopTank.texture = ResourceHelper.getResource("/models/tankModelTop.png");
        ModelTopTank.textureOpen = ResourceHelper.getResource("/models/tankModelTopOpen.png");
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
            matrixStack.translate(-0.5, 0.3, -0.35);
            matrixStack.scale(0.95F, (FluidUtils.getScale(moduleTank.getFluidAmount(), moduleTank.getCapacity(), fluidStack.isEmpty()) / 2.2F), 0.7F);
            RenderUtils.renderObject(FluidUtils.getFluidModel(fluidStack, FluidUtils.STAGES + 1), matrixStack, buffer, RenderUtils.getColorARGB(fluidStack, 0.2F),
                    RenderUtils.calculateGlowLight(light, fluidStack));

            matrixStack.popPose();
        }
    }
}
