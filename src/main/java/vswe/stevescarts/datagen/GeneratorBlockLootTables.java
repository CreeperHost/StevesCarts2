package vswe.stevescarts.datagen;

import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.data.loot.LootTableSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.block.Block;
import org.jspecify.annotations.NonNull;
import vswe.stevescarts.init.ModBlocks;

import java.util.Set;

public class GeneratorBlockLootTables extends BlockLootSubProvider {

    protected GeneratorBlockLootTables(LootTableSubProvider.Context registries) {
        super(Set.of(), FeatureFlags.REGISTRY.allFlags(), registries);
    }

    @Override
    protected void generate() {
        ModBlocks.BLOCKS.getEntries().forEach(holder -> dropSelf(holder.get()));
    }

    @Override
    protected @NonNull Iterable<Block> getKnownBlocks() {
        return ModBlocks.BLOCKS.getEntries().stream().map(e -> (Block) e.value()).toList();
    }
}
