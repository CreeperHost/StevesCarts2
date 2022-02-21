package vswe.stevescarts.init;

import net.minecraft.client.gui.screens.MenuScreens;
import vswe.stevescarts.client.guis.*;

public class ModScreens
{
    public static void init()
    {
        MenuScreens.register(ModContainers.CONTAINER_CART_ASSEMBLER.get(), GuiCartAssembler::new);
        MenuScreens.register(ModContainers.CONTAINER_MINECART.get(), GuiMinecart::new);
        MenuScreens.register(ModContainers.CONTAINER_CARGO.get(), GuiCargo::new);
        MenuScreens.register(ModContainers.CONTAINER_DISTRIBUTOR.get(), GuiDistributor::new);
        MenuScreens.register(ModContainers.CONTAINER_UPGRADE.get(), GuiUpgrade::new);
        MenuScreens.register(ModContainers.CONTAINER_LIQUID.get(), GuiLiquid::new);
        MenuScreens.register(ModContainers.CONTAINER_ACTIVATOR.get(), GuiActivator::new);
        MenuScreens.register(ModContainers.CONTAINER_DETECTOR.get(), GuiDetector::new);
    }
}
