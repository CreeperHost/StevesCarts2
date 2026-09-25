package vswe.stevescarts.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.EntityTypeTagsProvider;
import net.minecraft.world.entity.EntityTypes;
import org.jspecify.annotations.NonNull;
import vswe.stevescarts.Constants;
import vswe.stevescarts.api.StevesCartsTags;

import java.util.concurrent.CompletableFuture;

public class GeneratorEntityTypeTags extends EntityTypeTagsProvider {
    public GeneratorEntityTypeTags(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(output, lookupProvider, Constants.MOD_ID);
    }

    @Override
    protected void addTags(HolderLookup.@NonNull Provider provider) {
        tag(StevesCartsTags.EntityTypes.CAGE_BLACKLIST).add(
                EntityTypes.PLAYER.builtInRegistryHolder().key(),
                EntityTypes.IRON_GOLEM.builtInRegistryHolder().key(),
                EntityTypes.ENDER_DRAGON.builtInRegistryHolder().key(),
                EntityTypes.SLIME.builtInRegistryHolder().key(),
                EntityTypes.MAGMA_CUBE.builtInRegistryHolder().key(),
                EntityTypes.COD.builtInRegistryHolder().key(),
                EntityTypes.SALMON.builtInRegistryHolder().key(),
                EntityTypes.PUFFERFISH.builtInRegistryHolder().key(),
                EntityTypes.TROPICAL_FISH.builtInRegistryHolder().key(),
                EntityTypes.DOLPHIN.builtInRegistryHolder().key(),
                EntityTypes.SQUID.builtInRegistryHolder().key(),
                EntityTypes.GLOW_SQUID.builtInRegistryHolder().key(),
                EntityTypes.WITHER.builtInRegistryHolder().key(),
                EntityTypes.ENDERMAN.builtInRegistryHolder().key(),
                EntityTypes.SPIDER.builtInRegistryHolder().key(),
                EntityTypes.GIANT.builtInRegistryHolder().key(),
                EntityTypes.BEE.builtInRegistryHolder().key(),
                EntityTypes.PARROT.builtInRegistryHolder().key()
        );
    }
}
