package vswe.stevescarts.init;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.neoforge.common.extensions.IMenuTypeExtension;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import vswe.stevescarts.Constants;
import vswe.stevescarts.containers.*;

public class ModContainers
{
    public static final DeferredRegister<MenuType<?>> CONTAINERS = DeferredRegister.create(BuiltInRegistries.MENU, Constants.MOD_ID);

    public static final DeferredHolder<MenuType<?>, MenuType<ContainerCartAssembler>> CONTAINER_CART_ASSEMBLER = CONTAINERS.register("cart_assembler", () -> IMenuTypeExtension.create(ContainerCartAssembler::new));

    public static final DeferredHolder<MenuType<?>, MenuType<ContainerCargo>> CONTAINER_CARGO = CONTAINERS.register("cargo_manager", () -> IMenuTypeExtension.create(ContainerCargo::new));

    public static final DeferredHolder<MenuType<?>, MenuType<ContainerLiquid>> CONTAINER_LIQUID = CONTAINERS.register("cargo_liquid", () -> IMenuTypeExtension.create(ContainerLiquid::new));

    public static final DeferredHolder<MenuType<?>, MenuType<ContainerDistributor>> CONTAINER_DISTRIBUTOR = CONTAINERS.register("distributor", () -> IMenuTypeExtension.create(ContainerDistributor::new));

    public static final DeferredHolder<MenuType<?>, MenuType<ContainerUpgrade>> CONTAINER_UPGRADE = CONTAINERS.register("upgrade", () -> IMenuTypeExtension.create(ContainerUpgrade::new));

    public static final DeferredHolder<MenuType<?>, MenuType<ContainerActivator>> CONTAINER_ACTIVATOR = CONTAINERS.register("activator", () -> IMenuTypeExtension.create(ContainerActivator::new));

    public static final DeferredHolder<MenuType<?>, MenuType<ContainerMinecart>> CONTAINER_MINECART = CONTAINERS.register("minecart", () -> IMenuTypeExtension.create(ContainerMinecart::new));

}
