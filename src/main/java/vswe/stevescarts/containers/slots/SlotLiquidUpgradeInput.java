package vswe.stevescarts.containers.slots;

import vswe.stevescarts.blocks.tileentities.TileEntityUpgrade;
import vswe.stevescarts.helpers.storages.SCTank;

public class SlotLiquidUpgradeInput extends SlotLiquidInput
{
    private TileEntityUpgrade upgrade;

    public SlotLiquidUpgradeInput(final TileEntityUpgrade upgrade, final SCTank tank, final int maxsize, final int i, final int j, final int k)
    {
        super(upgrade, tank, maxsize, i, j, k);
        this.upgrade = upgrade;
    }
}
