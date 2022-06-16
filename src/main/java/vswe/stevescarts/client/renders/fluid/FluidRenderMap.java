package vswe.stevescarts.client.renders.fluid;

import it.unimi.dsi.fastutil.Hash;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenCustomHashMap;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.fluids.FluidStack;

public class FluidRenderMap<V> extends Object2ObjectOpenCustomHashMap<FluidStack, V>
{
    public enum FluidType
    {
        STILL, FLOWING
    }

    public FluidRenderMap()
    {
        super(FluidHashStrategy.INSTANCE);
    }

    public static TextureAtlasSprite getFluidTexture(FluidStack fluidStack, FluidType type)
    {
        Fluid fluid = fluidStack.getFluid();
        ResourceLocation spriteLocation;
        if (type == FluidType.STILL)
        {
            //TODO Fluid Render
            //            spriteLocation = fluid.getAttributes().getStillTexture(fluidStack);
        }
        else
        {
            //            spriteLocation = fluid.getAttributes().getFlowingTexture(fluidStack);
        }
        //        return getSprite(spriteLocation);
        return null;
    }

    public static TextureAtlasSprite getSprite(ResourceLocation spriteLocation)
    {
        return Minecraft.getInstance().getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(spriteLocation);
    }

    /**
     * Implements equals & hashCode that ignore FluidStack#amount
     */
    public static class FluidHashStrategy implements Hash.Strategy<FluidStack>
    {

        public static FluidHashStrategy INSTANCE = new FluidHashStrategy();

        @Override
        public int hashCode(FluidStack stack)
        {
            if (stack == null || stack.isEmpty())
            {
                return 0;
            }
            int code = 1;
            code = 31 * code + stack.getFluid().hashCode();
            if (stack.hasTag())
            {
                code = 31 * code + stack.getTag().hashCode();
            }
            return code;
        }

        @Override
        public boolean equals(FluidStack a, FluidStack b)
        {
            return a == null ? b == null : b != null && a.isFluidEqual(b);
        }
    }
}
