package vswe.stevescarts.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import org.jspecify.annotations.NonNull;
import vswe.stevescarts.Constants;
import vswe.stevescarts.init.ModBlocks;

import java.util.concurrent.CompletableFuture;

public class GeneratorBlockTags extends BlockTagsProvider {

    public GeneratorBlockTags(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(output, lookupProvider, Constants.MOD_ID);
    }

    @Override
    protected void addTags(HolderLookup.@NonNull Provider provider) {
        ModBlocks.BLOCKS.getEntries().forEach(block -> tag(BlockTags.MINEABLE_WITH_PICKAXE).add(block.get()));
        tag(BlockTags.RAILS).add(ModBlocks.ADVANCED_DETECTOR.get()).add(ModBlocks.JUNCTION.get());
        tag(BlockTags.BEACON_BASE_BLOCKS).add(ModBlocks.GALGADORIAN_METAL.get()).add(ModBlocks.REINFORCED_METAL.get()).add(ModBlocks.ENHANCED_GALGADORIAN_METAL.get());
    }
}
