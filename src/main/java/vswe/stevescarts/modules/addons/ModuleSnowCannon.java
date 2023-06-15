package vswe.stevescarts.modules.addons;

import net.minecraft.core.BlockPos;
import vswe.stevescarts.entities.EntityMinecartModular;

public class ModuleSnowCannon extends ModuleAddon
{
    private int tick;

    public ModuleSnowCannon(final EntityMinecartModular cart)
    {
        super(cart);
    }

    @Override
    public void update()
    {
        super.update();
        if (getCart().level().isClientSide)
        {
            return;
        }
        if (getCart().hasFuel())
        {
            if (tick >= getInterval())
            {
                tick = 0;
                generateSnow();
            }
            else
            {
                ++tick;
            }
        }
    }

    protected int getInterval()
    {
        return 70;
    }

    protected int getBlocksOnSide()
    {
        return 7;
    }

    protected int getBlocksFromLevel()
    {
        return 1;
    }

    private void generateSnow()
    {
        BlockPos cartPos = getCart().getExactPosition();
        for (int x = -getBlocksOnSide(); x <= getBlocksOnSide(); ++x)
        {
            for (int z = -getBlocksOnSide(); z <= getBlocksOnSide(); ++z)
            {
                for (int y = -getBlocksFromLevel(); y <= getBlocksFromLevel(); ++y)
                {
                    BlockPos pos = cartPos.offset(x, y, z);
                    //TODO
                    //                    if (countsAsAir(pos) && getCart().level.getBiome(pos).getTemperature(pos) <= 1.0f && Blocks.SNOW.canSurvive(Blocks.SNOW.defaultBlockState(), getCart().level, pos))
                    //                    {
                    //                        getCart().level.setBlock(pos, Blocks.SNOW.defaultBlockState(), 3);
                    //                    }
                }
            }
        }
    }
}
