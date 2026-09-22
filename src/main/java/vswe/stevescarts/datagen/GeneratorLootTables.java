package vswe.stevescarts.datagen;

import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;

import java.util.List;
import java.util.Set;

public class GeneratorLootTables extends LootTableProvider {

    public GeneratorLootTables() {
        super(Set.of(), List.of(
                new SubProviderEntry(GeneratorBlockLootTables::new, LootContextParamSets.BLOCK)
        ));
    }
}
