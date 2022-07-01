package vswe.stevescarts.containers.slots;

import vswe.stevescarts.api.modules.ModuleType;
import vswe.stevescarts.blocks.tileentities.TileEntityCartAssembler;

public class SlotHull extends SlotAssembler
{
    public SlotHull(final TileEntityCartAssembler assembler, final int i, final int j, final int k)
    {
        super(assembler, i, j, k, ModuleType.HULL, true, 0);
    }
}
