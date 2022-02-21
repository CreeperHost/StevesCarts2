//package vswe.stevescarts.compat.ftbic;
//
//import net.minecraft.block.Block;
//import net.minecraft.inventory.container.ContainerType;
//import net.minecraft.item.BlockItem;
//import net.minecraft.item.Item;
//import net.minecraft.tileentity.TileEntityType;
//import net.minecraftforge.common.extensions.IForgeContainerType;
//import net.minecraftforge.fml.RegistryObject;
//import net.minecraftforge.registries.DeferredRegister;
//import net.minecraftforge.registries.ForgeRegistries;
//import vswe.stevescarts.Constants;
//import vswe.stevescarts.blocks.tileentities.TileEntityCargo;
//import vswe.stevescarts.containers.ContainerCargo;
//import vswe.stevescarts.init.ModBlocks;
//import vswe.stevescarts.init.ModItems;
//import vswe.stevescarts.modules.data.ModuleData;
//import vswe.stevescarts.modules.engines.ModuleElectricEngine;
//
//public class CompatFtbic
//{
//    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, Constants.MOD_ID);
//    public static final RegistryObject<Block> INDUSTRIAL_MANAGER = BLOCKS.register("industrial_manager", BlockIndustrialManager::new);
//    public static final DeferredRegister<TileEntityType<?>> TILES_ENTITIES = DeferredRegister.create(ForgeRegistries.TILE_ENTITIES, Constants.MOD_ID);
//    public static final DeferredRegister<ContainerType<?>> CONTAINERS = DeferredRegister.create(ForgeRegistries.CONTAINERS, Constants.MOD_ID);
//
//
//    public static final RegistryObject<TileEntityType<TileIndustrialManager>> INDUSTRIAL_MANAGER_TILE = TILES_ENTITIES.register("industrial_manager", () -> TileEntityType.Builder.of(TileIndustrialManager::new, INDUSTRIAL_MANAGER.get()).build(null));
//    public static final RegistryObject<ContainerType<ContainerIndustrialManager>> INDUSTRIAL_MANAGER_CONTAINER = CONTAINERS.register("industrial_manager", () -> IForgeContainerType.create(ContainerIndustrialManager::new));
//
//
//    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Constants.MOD_ID);
//    public static final RegistryObject<Item> INDUSTRIAL_MANAGER_ITEM = ITEMS.register("industrial_manager", () -> new BlockItem(INDUSTRIAL_MANAGER.get(), ModItems.ITEM_GROUP));
//}
