package vswe.stevescarts.client.renders.fluid;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraftforge.fluids.FluidStack;

public class RenderUtils
{
    public static void renderObject(Model3D object, PoseStack matrix, VertexConsumer buffer, int argb, int light) {
        if (object != null) {
            RenderResizableCuboid.INSTANCE.renderCube(object, matrix, buffer, argb, light);
        }
    }

    public static int calculateGlowLight(int light, FluidStack fluid) {
        //TODO fluid render
//        return fluid.isEmpty() ? light : calculateGlowLight(light, fluid.getFluid().getAttributes().getLuminosity(fluid));
        return 0;
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
        //TODO fluid render

        //        return fluidStack.getFluid().getAttributes().getColor(fluidStack);
        return 0;
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
