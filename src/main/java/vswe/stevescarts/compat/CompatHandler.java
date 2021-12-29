package vswe.stevescarts.compat;

import net.minecraft.client.gui.ScreenManager;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModList;
import vswe.stevescarts.client.guis.GuiCartAssembler;
import vswe.stevescarts.compat.ftbic.CompatFtbic;
import vswe.stevescarts.compat.ftbic.GuiIndustrialManager;
import vswe.stevescarts.init.ModContainers;
import vswe.stevescarts.modules.data.ModuleData;

public class CompatHandler
{
    public static void init(IEventBus eventBus)
    {
        if(ModList.get().isLoaded("ftbic"))
        {
            CompatFtbic.BLOCKS.register(eventBus);
            CompatFtbic.TILES_ENTITIES.register(eventBus);
            CompatFtbic.ITEMS.register(eventBus);
            CompatFtbic.CONTAINERS.register(eventBus);
        }
    }

    public static void initClient()
    {
        if(ModList.get().isLoaded("ftbic"))
        {
            ScreenManager.register(CompatFtbic.INDUSTRIAL_MANAGER_CONTAINER.get(), GuiIndustrialManager::new);
        }
    }
}
