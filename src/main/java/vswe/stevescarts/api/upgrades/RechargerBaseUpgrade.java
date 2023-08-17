package vswe.stevescarts.api.upgrades;

import net.minecraft.nbt.CompoundTag;
import vswe.stevescarts.blocks.tileentities.TileEntityUpgrade;

public abstract class RechargerBaseUpgrade extends BaseUpgradeEffect
{
    @Override
    public void update(final TileEntityUpgrade tileEntityUpgrade)
    {
        if(tileEntityUpgrade.getLevel() == null) return;

        if (!tileEntityUpgrade.getLevel().isClientSide && canGenerate(tileEntityUpgrade))
        {
            final CompoundTag comp = tileEntityUpgrade.getCompound();
            if (comp == null)
            {
                return;
            }
            if (comp.getShort("GenerateCooldown") >= 1200 / getAmount(tileEntityUpgrade))
            {
                comp.putShort("GenerateCooldown", (short) 0);
                tileEntityUpgrade.getMaster().increaseFuel(1);
            }
            else
            {
                comp.putShort("GenerateCooldown", (short) (comp.getShort("GenerateCooldown") + 1));
            }
        }
    }

    protected abstract boolean canGenerate(final TileEntityUpgrade tileEntityUpgrade);

    protected abstract int getAmount(final TileEntityUpgrade tileEntityUpgrade);

    @Override
    public void init(final TileEntityUpgrade tileEntityUpgrade)
    {
        tileEntityUpgrade.getCompound().putShort("GenerateCooldown", (short) 0);
    }
}
