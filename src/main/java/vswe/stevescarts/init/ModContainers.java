package vswe.stevescarts.init;

import net.minecraft.inventory.container.ContainerType;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import vswe.stevescarts.Constants;
import vswe.stevescarts.containers.*;

public class ModContainers
{
    public static final DeferredRegister<ContainerType<?>> CONTAINERS = DeferredRegister.create(ForgeRegistries.CONTAINERS, Constants.MOD_ID);

    public static final RegistryObject<ContainerType<ContainerCartAssembler>> CONTAINER_CART_ASSEMBLER = CONTAINERS.register("cart_assembler", () -> IForgeContainerType.create(ContainerCartAssembler::new));

    public static final RegistryObject<ContainerType<ContainerCargo>> CONTAINER_CARGO = CONTAINERS.register("cargo_manager", () -> IForgeContainerType.create(ContainerCargo::new));

    public static final RegistryObject<ContainerType<ContainerLiquid>> CONTAINER_LIQUID = CONTAINERS.register("cargo_liquid", () -> IForgeContainerType.create(ContainerLiquid::new));

    public static final RegistryObject<ContainerType<ContainerDistributor>> CONTAINER_DISTRIBUTOR = CONTAINERS.register("distributor", () -> IForgeContainerType.create(ContainerDistributor::new));

    public static final RegistryObject<ContainerType<ContainerUpgrade>> CONTAINER_UPGRADE = CONTAINERS.register("upgrade", () -> IForgeContainerType.create(ContainerUpgrade::new));

    public static final RegistryObject<ContainerType<ContainerActivator>> CONTAINER_ACTIVATOR = CONTAINERS.register("activator", () -> IForgeContainerType.create(ContainerActivator::new));

    public static final RegistryObject<ContainerType<ContainerDetector>> CONTAINER_DETECTOR = CONTAINERS.register("detector", () -> IForgeContainerType.create(ContainerDetector::new));

    public static final RegistryObject<ContainerType<ContainerMinecart>> CONTAINER_MINECART = CONTAINERS.register("minecart", () -> IForgeContainerType.create(ContainerMinecart::new));

}
