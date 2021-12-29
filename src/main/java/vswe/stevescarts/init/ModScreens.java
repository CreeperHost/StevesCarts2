package vswe.stevescarts.init;

import net.minecraft.client.gui.ScreenManager;
import vswe.stevescarts.client.guis.*;

public class ModScreens
{
    public static void init()
    {
        ScreenManager.register(ModContainers.CONTAINER_CART_ASSEMBLER.get(), GuiCartAssembler::new);
        ScreenManager.register(ModContainers.CONTAINER_MINECART.get(), GuiMinecart::new);
        ScreenManager.register(ModContainers.CONTAINER_CARGO.get(), GuiCargo::new);
        ScreenManager.register(ModContainers.CONTAINER_DISTRIBUTOR.get(), GuiDistributor::new);
        ScreenManager.register(ModContainers.CONTAINER_UPGRADE.get(), GuiUpgrade::new);
        ScreenManager.register(ModContainers.CONTAINER_LIQUID.get(), GuiLiquid::new);
        ScreenManager.register(ModContainers.CONTAINER_ACTIVATOR.get(), GuiActivator::new);
        ScreenManager.register(ModContainers.CONTAINER_DETECTOR.get(), GuiDetector::new);
    }
}
