package vswe.stevescarts.modules.addons;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.common.Tags;
import vswe.stevescarts.api.modules.template.ModuleAddon;
import vswe.stevescarts.entities.EntityMinecartModular;
import vswe.stevescarts.modules.workers.tools.ModuleDrill;

import java.util.ArrayList;

public class ModuleOreTracker extends ModuleAddon
{
    public ModuleOreTracker(final EntityMinecartModular cart)
    {
        super(cart);
    }

    public BlockPos findBlockToMine(final ModuleDrill drill, final BlockPos start)
    {
        return findBlockToMine(drill, new ArrayList<>(), start, true);
    }

    private BlockPos findBlockToMine(final ModuleDrill drill, final ArrayList<BlockPos> checked, final BlockPos current, final boolean first)
    {
        if (current == null || checked.contains(current) || (!first && !isOre(current)))
        {
            return null;
        }
        checked.add(current);
        if (checked.size() < 200)
        {
            for (int x = -1; x <= 1; ++x)
            {
                for (int y = -1; y <= 1; ++y)
                {
                    for (int z = -1; z <= 1; ++z)
                    {
                        if (Math.abs(x) + Math.abs(y) + Math.abs(z) == 1)
                        {
                            final BlockPos ret = findBlockToMine(drill, checked, current.offset(x, y, z), false);
                            if (ret != null)
                            {
                                return ret;
                            }
                        }
                    }
                }
            }
        }
        if (first && !isOre(current))
        {
            return null;
        }
        if (drill.isValidBlock(getCart().level(), current, 0, 1, true) == null)
        {
            return null;
        }
        return current;
    }

    private boolean isOre(BlockPos pos)
    {
        BlockState state = getCart().level().getBlockState(pos);
        Block b = state.getBlock();
        if (b == null || b == Blocks.AIR)
        {
            return false;
        }
        return state.is(Tags.Blocks.ORES);
    }
}
