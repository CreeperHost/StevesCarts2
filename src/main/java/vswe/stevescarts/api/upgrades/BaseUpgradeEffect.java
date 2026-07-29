package vswe.stevescarts.api.upgrades;

import net.minecraft.network.chat.Component;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import vswe.stevescarts.blocks.tileentities.TileEntityUpgrade;

public abstract class BaseUpgradeEffect {
    public void update(final TileEntityUpgrade tileEntityUpgrade) {
    }

    public void init(final TileEntityUpgrade tileEntityUpgrade) {
    }

    public void removed(final TileEntityUpgrade tileEntityUpgrade) {
    }

    public void load(final TileEntityUpgrade tileEntityUpgrade, ValueInput input) {
    }

    public void save(final TileEntityUpgrade tileEntityUpgrade, ValueOutput output) {
    }

    public abstract Component getName();
}
