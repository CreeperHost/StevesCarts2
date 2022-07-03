package vswe.stevescarts.client.renders.fluid;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.client.RenderProperties;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;

public class FluidUtils
{
    public static final FluidRenderMap<Int2ObjectMap<Model3D>> CACHED_FLUIDS = new FluidRenderMap<>();
    public static final int STAGES = 1400;

    public static TextureAtlasSprite getSprite(ResourceLocation spriteLocation)
    {
        return Minecraft.getInstance().getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(spriteLocation);
    }

    public static Model3D getFluidModel(FluidStack fluid, int stage)
    {
        if (CACHED_FLUIDS.containsKey(fluid) && CACHED_FLUIDS.get(fluid).containsKey(stage))
        {
            return CACHED_FLUIDS.get(fluid).get(stage);
        }
        Model3D model = new Model3D();
        model.setTexture(FluidRenderMap.getFluidTexture(fluid, FluidRenderMap.FluidType.STILL));

        if (RenderProperties.get(fluid.getFluid()).getStillTexture() != null)
        {
            model.minX = 0.135F;//0.125 + .01;
            model.minY = 0.0725F;//0.0625 + .01;
            model.minZ = 0.135F;//0.125 + .01;

            model.maxX = 0.865F;//0.875 - .01;
            model.maxY = 0.0525F + 0.875F * (stage / (float) 1_400);//0.0625 - .01 + 0.875 * (stage / (float) stages);
            model.maxZ = 0.865F;//0.875 - .01;
        }
        CACHED_FLUIDS.computeIfAbsent(fluid, f -> new Int2ObjectOpenHashMap<>()).put(stage, model);
        return model;
    }

    public static float getScale(FluidTank tank)
    {
        return getScale(tank.getFluidAmount(), tank.getCapacity(), tank.isEmpty());
    }

    public static float getScale(int stored, int capacity, boolean empty)
    {
        return (float) stored / capacity;
    }

    public static IFluidHandler getTank(Level world, BlockPos pos, Direction side)
    {
        BlockEntity tile = world.getBlockEntity(pos);
        if (tile == null)
        {
            return null;
        }
        return tile.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY).orElse(null);
    }

    public static boolean tryFillPositionFromTank(Level world, BlockPos posSide, Direction sideOpp, IFluidHandler tankFrom, int amount)
    {
        if (tankFrom == null)
        {
            return false;
        }
        try
        {
            IFluidHandler fluidTo = FluidUtil.getFluidHandler(world, posSide, sideOpp).orElse(null);
            if (fluidTo != null)
            {
                FluidStack wasDrained = tankFrom.drain(amount, IFluidHandler.FluidAction.SIMULATE);
                if (wasDrained == null)
                {
                    return false;
                }
                int filled = fluidTo.fill(wasDrained, IFluidHandler.FluidAction.SIMULATE);
                if (wasDrained != null && wasDrained.getAmount() > 0 && filled > 0)
                {
                    int realAmt = Math.min(filled, wasDrained.getAmount());
                    wasDrained = tankFrom.drain(realAmt, IFluidHandler.FluidAction.EXECUTE);
                    if (wasDrained == null)
                    {
                        return false;
                    }
                    return fluidTo.fill(wasDrained, IFluidHandler.FluidAction.EXECUTE) > 0;
                }
            }
            return false;
        } catch (Exception e)
        {
            return false;
        }
    }
}
