package vswe.stevescarts.datagen;

import net.minecraft.core.registries.Registries;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import net.neoforged.neoforge.data.event.GatherDataRegistryEntriesEvent;
import vswe.stevescarts.Constants;

@EventBusSubscriber(modid = Constants.MOD_ID)
public class StevesCartsDataGenerators {

    @SubscribeEvent
    public static void gatherData(GatherDataEvent.Client event) {
        event.createProvider(GeneratorModels::new);
        event.createProvider(GeneratorBlockTags::new);
        event.createProvider(GeneratorEntityTypeTags::new);
        event.createProvider(GeneratorLanguage::new);
    }

    @SubscribeEvent
    public static void gatherRegistryEntries(GatherDataRegistryEntriesEvent event) {
        event.add(Registries.LOOT_TABLE, new GeneratorLootTables())
                .recipe(GeneratorRecipes::new);
    }
}
