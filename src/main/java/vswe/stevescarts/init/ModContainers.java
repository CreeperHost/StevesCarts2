package vswe.stevescarts.init;

import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import vswe.stevescarts.Constants;
import vswe.stevescarts.containers.*;

public class ModContainers
{
    public static final DeferredRegister<MenuType<?>> CONTAINERS = DeferredRegister.create(ForgeRegistries.CONTAINERS, Constants.MOD_ID);

    public static final RegistryObject<MenuType<ContainerCartAssembler>> CONTAINER_CART_ASSEMBLER = CONTAINERS.register("cart_assembler", () -> IForgeMenuType.create(ContainerCartAssembler::new));

    public static final RegistryObject<MenuType<ContainerCargo>> CONTAINER_CARGO = CONTAINERS.register("cargo_manager", () -> IForgeMenuType.create(ContainerCargo::new));

    public static final RegistryObject<MenuType<ContainerLiquid>> CONTAINER_LIQUID = CONTAINERS.register("cargo_liquid", () -> IForgeMenuType.create(ContainerLiquid::new));

    public static final RegistryObject<MenuType<ContainerDistributor>> CONTAINER_DISTRIBUTOR = CONTAINERS.register("distributor", () -> IForgeMenuType.create(ContainerDistributor::new));

    public static final RegistryObject<MenuType<ContainerUpgrade>> CONTAINER_UPGRADE = CONTAINERS.register("upgrade", () -> IForgeMenuType.create(ContainerUpgrade::new));

    public static final RegistryObject<MenuType<ContainerActivator>> CONTAINER_ACTIVATOR = CONTAINERS.register("activator", () -> IForgeMenuType.create(ContainerActivator::new));

    public static final RegistryObject<MenuType<ContainerMinecart>> CONTAINER_MINECART = CONTAINERS.register("minecart", () -> IForgeMenuType.create(ContainerMinecart::new));

}
