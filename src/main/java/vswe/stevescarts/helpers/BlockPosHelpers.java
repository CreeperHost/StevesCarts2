package vswe.stevescarts.helpers;

import net.minecraft.core.BlockPos;
import vswe.stevescarts.entities.ModularMinecart;
import vswe.stevescarts.entities.ModularMinecart;

public class BlockPosHelpers
{
    public static double getHorizontalDistToCartSquared(BlockPos pos, ModularMinecart cart)
    {
        final double xDif = pos.getX() - cart.getX();
        final double zDif = pos.getZ() - cart.getZ();
        return Math.pow(xDif, 2.0) + Math.pow(zDif, 2.0);
    }

    public double getDistToCartSquared(BlockPos pos, ModularMinecart cart)
    {
        final int xDif = pos.getX() - cart.x();
        final int yDif = pos.getY() - cart.y();
        final int zDif = pos.getZ() - cart.z();
        return Math.pow(xDif, 2.0) + Math.pow(yDif, 2.0) + Math.pow(zDif, 2.0);
    }
}
