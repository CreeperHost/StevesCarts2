package vswe.stevescarts.upgrades;

import net.minecraft.nbt.CompoundTag;
import vswe.stevescarts.blocks.tileentities.TileEntityUpgrade;

public abstract class RechargerBase extends BaseEffect
{
    @Override
    public void update(final TileEntityUpgrade upgrade)
    {
        if (!upgrade.getLevel().isClientSide && canGenerate(upgrade))
        {
            final CompoundTag comp = upgrade.getCompound();
            if (comp == null)
            {
                return;
            }
            if (comp.getShort("GenerateCooldown") >= 1200 / getAmount(upgrade))
            {
                comp.putShort("GenerateCooldown", (short) 0);
                upgrade.getMaster().increaseFuel(1);
            }
            else
            {
                comp.putShort("GenerateCooldown", (short) (comp.getShort("GenerateCooldown") + 1));
            }
        }
    }

    protected abstract boolean canGenerate(final TileEntityUpgrade p0);

    protected abstract int getAmount(final TileEntityUpgrade p0);

    @Override
    public void init(final TileEntityUpgrade upgrade)
    {
        upgrade.getCompound().putShort("GenerateCooldown", (short) 0);
    }
}
