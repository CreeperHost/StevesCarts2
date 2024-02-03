package vswe.stevescarts.init;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import vswe.stevescarts.Constants;
import vswe.stevescarts.blocks.*;
import vswe.stevescarts.blocks.tileentities.*;
import vswe.stevescarts.upgrades.AssemblerUpgrade;

public class ModBlocks
{
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(BuiltInRegistries.BLOCK, Constants.MOD_ID);
    public static final DeferredRegister<BlockEntityType<?>> TILES_ENTITIES = DeferredRegister.create(BuiltInRegistries.BLOCK_ENTITY_TYPE, Constants.MOD_ID);

    public static final DeferredHolder<Block, Block> CART_ASSEMBLER = BLOCKS.register("blockcartassembler", BlockCartAssembler::new);
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<TileEntityCartAssembler>> CART_ASSEMBLER_TILE = TILES_ENTITIES.register("blockcartassembler", () -> BlockEntityType.Builder.of(TileEntityCartAssembler::new, ModBlocks.CART_ASSEMBLER.get()).build(null));

    public static final DeferredHolder<Block, Block> CARGO_MANAGER = BLOCKS.register("blockcargomanager", BlockCargoManager::new);
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<TileEntityCargo>> CARGO_MANAGER_TILE = TILES_ENTITIES.register("blockcargomanager", () -> BlockEntityType.Builder.of(TileEntityCargo::new, ModBlocks.CARGO_MANAGER.get()).build(null));

    public static final DeferredHolder<Block, Block> LIQUID_MANAGER = BLOCKS.register("blockliquidmanager", BlockLiquidManager::new);
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<TileEntityLiquid>> LIQUID_MANAGER_TILE = TILES_ENTITIES.register("blockliquidmanager", () -> BlockEntityType.Builder.of(TileEntityLiquid::new, ModBlocks.LIQUID_MANAGER.get()).build(null));

    public static final DeferredHolder<Block, Block> EXTERNAL_DISTRIBUTOR = BLOCKS.register("blockdistributor", BlockDistributor::new);
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<TileEntityDistributor>> EXTERNAL_DISTRIBUTOR_TILE = TILES_ENTITIES.register("blockdistributor", () -> BlockEntityType.Builder.of(TileEntityDistributor::new, ModBlocks.EXTERNAL_DISTRIBUTOR.get()).build(null));

    public static final DeferredHolder<Block, Block> MODULE_TOGGLER = BLOCKS.register("blockactivator", BlockActivator::new);
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<TileEntityActivator>> MODULE_TOGGLER_TILE = TILES_ENTITIES.register("blockactivator", () -> BlockEntityType.Builder.of(TileEntityActivator::new, ModBlocks.MODULE_TOGGLER.get()).build(null));

    public static final DeferredHolder<Block, Block> JUNCTION = BLOCKS.register("blockjunction", BlockRailJunction::new);
    public static final DeferredHolder<Block, Block> ADVANCED_DETECTOR = BLOCKS.register("blockadvdetector", BlockRailAdvDetector::new);

    //Metal blocks
    public static final DeferredHolder<Block, Block> REINFORCED_METAL = BLOCKS.register("reinforced_metal", BlockMetalStorage::new);
    public static final DeferredHolder<Block, Block> GALGADORIAN_METAL = BLOCKS.register("galgadorian_metal", BlockMetalStorage::new);
    public static final DeferredHolder<Block, Block> ENHANCED_GALGADORIAN_METAL = BLOCKS.register("enhanced_galgadorian_metal", BlockMetalStorage::new);

    //Upgrades
    public static final DeferredHolder<Block, Block> BATTERIES = BLOCKS.register("upgrade_batteries", () -> new BlockUpgrade(AssemblerUpgrade.getUpgrade(0)));
    public static final DeferredHolder<Block, Block> POWER_CRYSTAL = BLOCKS.register("upgrade_power_crystal", () -> new BlockUpgrade(AssemblerUpgrade.getUpgrade(1)));
    public static final DeferredHolder<Block, Block> KNOWLEDGE = BLOCKS.register("upgrade_module_knowledge", () -> new BlockUpgrade(AssemblerUpgrade.getUpgrade(2)));
    public static final DeferredHolder<Block, Block> INDUSTRIAL_ESPIONAGE = BLOCKS.register("upgrade_industrial_espionage", () -> new BlockUpgrade(AssemblerUpgrade.getUpgrade(3)));
    public static final DeferredHolder<Block, Block> EXPERIENCED_ASSEMBLER = BLOCKS.register("upgrade_experienced_assembler", () -> new BlockUpgrade(AssemblerUpgrade.getUpgrade(4)));
    public static final DeferredHolder<Block, Block> NEW_ERA = BLOCKS.register("upgrade_new_era", () -> new BlockUpgrade(AssemblerUpgrade.getUpgrade(5)));
    public static final DeferredHolder<Block, Block> COTWO_FRIENDLY = BLOCKS.register("upgrade_cotwo_friendly", () -> new BlockUpgrade(AssemblerUpgrade.getUpgrade(6)));
    public static final DeferredHolder<Block, Block> GENERIC_ENGINE = BLOCKS.register("upgrade_generic_engine", () -> new BlockUpgrade(AssemblerUpgrade.getUpgrade(7)));
    public static final DeferredHolder<Block, Block> MODULE_INPUT = BLOCKS.register("upgrade_module_input", () -> new BlockUpgrade(AssemblerUpgrade.getUpgrade(8)));
    public static final DeferredHolder<Block, Block> PRODUCTION_LINE = BLOCKS.register("upgrade_production_line", () -> new BlockUpgrade(AssemblerUpgrade.getUpgrade(9)));
    public static final DeferredHolder<Block, Block> CART_DEPLOYER = BLOCKS.register("upgrade_cart_deployer", () -> new BlockUpgrade(AssemblerUpgrade.getUpgrade(10)));
    public static final DeferredHolder<Block, Block> CART_MODIFIER = BLOCKS.register("upgrade_cart_modifier", () -> new BlockUpgrade(AssemblerUpgrade.getUpgrade(11)));
    public static final DeferredHolder<Block, Block> CART_CRANE = BLOCKS.register("upgrade_cart_crane", () -> new BlockUpgrade(AssemblerUpgrade.getUpgrade(12)));
    public static final DeferredHolder<Block, Block> REDSTONE_CONTROL = BLOCKS.register("upgrade_redstone_control", () -> new BlockUpgrade(AssemblerUpgrade.getUpgrade(13)));
    public static final DeferredHolder<Block, Block> CREATIVE_MODE = BLOCKS.register("upgrade_creative_mode", () -> new BlockUpgrade(AssemblerUpgrade.getUpgrade(14)));
    public static final DeferredHolder<Block, Block> QUICK_DEMOLISHER = BLOCKS.register("upgrade_quick_demolisher", () -> new BlockUpgrade(AssemblerUpgrade.getUpgrade(15)));
    public static final DeferredHolder<Block, Block> ENTROPY = BLOCKS.register("upgrade_entropy", () -> new BlockUpgrade(AssemblerUpgrade.getUpgrade(16)));
    public static final DeferredHolder<Block, Block> MANAGER_BRIDGE = BLOCKS.register("upgrade_manager_bridge", () -> new BlockUpgrade(AssemblerUpgrade.getUpgrade(17)));
    public static final DeferredHolder<Block, Block> THERMAL_ENGINE = BLOCKS.register("upgrade_thermal_engine", () -> new BlockUpgrade(AssemblerUpgrade.getUpgrade(18)));
    public static final DeferredHolder<Block, Block> SOLAR_PANEL = BLOCKS.register("upgrade_solar_panel", () -> new BlockUpgrade(AssemblerUpgrade.getUpgrade(19)));


    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<TileEntityUpgrade>> UPGRADE_TILE = TILES_ENTITIES.register("upgrade", () -> BlockEntityType.Builder.of(TileEntityUpgrade::new, ModBlocks.BATTERIES.get(), ModBlocks.POWER_CRYSTAL.get(), ModBlocks.KNOWLEDGE.get(), ModBlocks.INDUSTRIAL_ESPIONAGE.get(), ModBlocks.EXPERIENCED_ASSEMBLER.get(), ModBlocks.NEW_ERA.get(), ModBlocks.COTWO_FRIENDLY.get(), ModBlocks.GENERIC_ENGINE.get(), ModBlocks.MODULE_INPUT.get(), ModBlocks.PRODUCTION_LINE.get(), ModBlocks.CART_DEPLOYER.get(), ModBlocks.CART_MODIFIER.get(), ModBlocks.CART_CRANE.get(), ModBlocks.REDSTONE_CONTROL.get(), ModBlocks.CREATIVE_MODE.get(), ModBlocks.QUICK_DEMOLISHER.get(), ModBlocks.QUICK_DEMOLISHER.get(), ModBlocks.ENTROPY.get(), ModBlocks.MANAGER_BRIDGE.get(), ModBlocks.THERMAL_ENGINE.get(), ModBlocks.SOLAR_PANEL.get()).build(null));

}
