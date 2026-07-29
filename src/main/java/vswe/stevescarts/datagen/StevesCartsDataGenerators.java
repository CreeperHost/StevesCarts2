package vswe.stevescarts.datagen;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import vswe.stevescarts.Constants;

@EventBusSubscriber(modid = Constants.MOD_ID)
public class StevesCartsDataGenerators {

    @SubscribeEvent
    public static void gatherData(GatherDataEvent.Client event) {
        event.createProvider(GeneratorModels::new);
        event.createProvider(GeneratorLootTables::new);
        event.createProvider(GeneratorBlockTags::new);
        event.createProvider(GeneratorLanguage::new);
        event.createProvider(GeneratorRecipes.Runner::new);
    }
}
