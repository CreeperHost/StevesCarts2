package vswe.stevescarts.upgrades;

import net.minecraft.client.resources.language.I18n;
import vswe.stevescarts.api.upgrades.RechargerBaseUpgrade;
import vswe.stevescarts.blocks.tileentities.TileEntityUpgrade;
import vswe.stevescarts.helpers.Localization;

public class Recharger extends RechargerBaseUpgrade
{
    protected int amount;

    public Recharger(final int amount)
    {
        this.amount = amount;
    }

    @Override
    protected int getAmount(final TileEntityUpgrade upgrade)
    {
        return amount;
    }

    @Override
    protected boolean canGenerate(final TileEntityUpgrade upgrade)
    {
        return true;
    }

    @Override
    public String getName()
    {
        return I18n.get("info.stevescarts.effectGenerator") + amount + " " + amount;
    }
}
