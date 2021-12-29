package vswe.stevescarts.client.renders.fluid;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.LightTexture;
import net.minecraftforge.fluids.FluidStack;

public class RenderUtils
{
    public static void renderObject(Model3D object, MatrixStack matrix, IVertexBuilder buffer, int argb, int light) {
        if (object != null) {
            RenderResizableCuboid.INSTANCE.renderCube(object, matrix, buffer, argb, light);
        }
    }

    public static int calculateGlowLight(int light, FluidStack fluid) {
        return fluid.isEmpty() ? light : calculateGlowLight(light, fluid.getFluid().getAttributes().getLuminosity(fluid));
    }

    public static final int FULL_LIGHT = 0xF000F0;

    public static int calculateGlowLight(int light, int glow) {
        return FULL_LIGHT;
    }

    //KEEP
    public static int getColorARGB(FluidStack fluidStack, float fluidScale) {
        if (fluidStack.isEmpty()) {
            return -1;
        }
        return getColorARGB(fluidStack);
    }

    private static int getColorARGB(FluidStack fluidStack) {
        return fluidStack.getFluid().getAttributes().getColor(fluidStack);
    }

    public static float getRed(int color) {
        return (color >> 16 & 0xFF) / 255.0F;
    }

    public static float getGreen(int color) {
        return (color >> 8 & 0xFF) / 255.0F;
    }

    public static float getBlue(int color) {
        return (color & 0xFF) / 255.0F;
    }

    public static float getAlpha(int color) {
        return (color >> 24 & 0xFF) / 255.0F;
    }
}
