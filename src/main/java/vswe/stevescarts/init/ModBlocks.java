package vswe.stevescarts.init;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import vswe.stevescarts.Constants;
import vswe.stevescarts.blocks.*;
import vswe.stevescarts.blocks.tileentities.*;
import vswe.stevescarts.upgrades.AssemblerUpgrade;

public class ModBlocks
{
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(Constants.MOD_ID);
    public static final DeferredRegister<BlockEntityType<?>> TILES_ENTITIES = DeferredRegister.create(BuiltInRegistries.BLOCK_ENTITY_TYPE, Constants.MOD_ID);

    public static final DeferredHolder<Block, Block> CART_ASSEMBLER = BLOCKS.registerBlock("blockcartassembler", BlockCartAssembler::new, BlockBehaviour.Properties.of().strength(2.0F));
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<TileEntityCartAssembler>> CART_ASSEMBLER_TILE = TILES_ENTITIES.register("blockcartassembler", () -> new BlockEntityType<>(TileEntityCartAssembler::new, ModBlocks.CART_ASSEMBLER.get()));

    public static final DeferredHolder<Block, Block> CARGO_MANAGER = BLOCKS.registerBlock("blockcargomanager", BlockCargoManager::new, BlockBehaviour.Properties.of().strength(2.0F));
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<TileEntityCargo>> CARGO_MANAGER_TILE = TILES_ENTITIES.register("blockcargomanager", () -> new BlockEntityType<>(TileEntityCargo::new, ModBlocks.CARGO_MANAGER.get()));

    public static final DeferredHolder<Block, Block> LIQUID_MANAGER = BLOCKS.registerBlock("blockliquidmanager", BlockLiquidManager::new, BlockBehaviour.Properties.of().strength(2.0F));
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<TileEntityLiquid>> LIQUID_MANAGER_TILE = TILES_ENTITIES.register("blockliquidmanager", () -> new BlockEntityType<>(TileEntityLiquid::new, ModBlocks.LIQUID_MANAGER.get()));

    public static final DeferredHolder<Block, Block> EXTERNAL_DISTRIBUTOR = BLOCKS.registerBlock("blockdistributor", BlockDistributor::new, BlockBehaviour.Properties.of().strength(2.0F));
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<TileEntityDistributor>> EXTERNAL_DISTRIBUTOR_TILE = TILES_ENTITIES.register("blockdistributor", () -> new BlockEntityType<>(TileEntityDistributor::new, ModBlocks.EXTERNAL_DISTRIBUTOR.get()));

    public static final DeferredHolder<Block, Block> MODULE_TOGGLER = BLOCKS.registerBlock("blockactivator", BlockActivator::new, BlockBehaviour.Properties.of().strength(2.0F));
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<TileEntityActivator>> MODULE_TOGGLER_TILE = TILES_ENTITIES.register("blockactivator", () -> new BlockEntityType<>(TileEntityActivator::new, ModBlocks.MODULE_TOGGLER.get()));

    public static final DeferredHolder<Block, Block> JUNCTION = BLOCKS.registerBlock("blockjunction", BlockRailJunction::new, BlockBehaviour.Properties.of().noCollission().strength(0.7F).sound(SoundType.METAL));
    public static final DeferredHolder<Block, Block> ADVANCED_DETECTOR = BLOCKS.registerBlock("blockadvdetector", BlockRailAdvDetector::new, BlockBehaviour.Properties.of().noCollission().strength(0.7F).sound(SoundType.METAL));

    //Metal blocks
    public static final DeferredHolder<Block, Block> REINFORCED_METAL = BLOCKS.registerBlock("reinforced_metal", BlockMetalStorage::new, BlockBehaviour.Properties.of().strength(2.0F));
    public static final DeferredHolder<Block, Block> GALGADORIAN_METAL = BLOCKS.registerBlock("galgadorian_metal", BlockMetalStorage::new, BlockBehaviour.Properties.of().strength(2.0F));
    public static final DeferredHolder<Block, Block> ENHANCED_GALGADORIAN_METAL = BLOCKS.registerBlock("enhanced_galgadorian_metal", BlockMetalStorage::new, BlockBehaviour.Properties.of().strength(2.0F));

    //Upgrades
    public static final DeferredHolder<Block, Block> BATTERIES = BLOCKS.registerBlock("upgrade_batteries", props -> new BlockUpgrade(props, AssemblerUpgrade.getUpgrade(0)), BlockBehaviour.Properties.of().noOcclusion().randomTicks().strength(2.0F));
    public static final DeferredHolder<Block, Block> POWER_CRYSTAL = BLOCKS.registerBlock("upgrade_power_crystal", props -> new BlockUpgrade(props, AssemblerUpgrade.getUpgrade(1)), BlockBehaviour.Properties.of().noOcclusion().randomTicks().strength(2.0F));
    public static final DeferredHolder<Block, Block> KNOWLEDGE = BLOCKS.registerBlock("upgrade_module_knowledge", props -> new BlockUpgrade(props, AssemblerUpgrade.getUpgrade(2)), BlockBehaviour.Properties.of().noOcclusion().randomTicks().strength(2.0F));
    public static final DeferredHolder<Block, Block> INDUSTRIAL_ESPIONAGE = BLOCKS.registerBlock("upgrade_industrial_espionage", props -> new BlockUpgrade(props, AssemblerUpgrade.getUpgrade(3)), BlockBehaviour.Properties.of().noOcclusion().randomTicks().strength(2.0F));
    public static final DeferredHolder<Block, Block> EXPERIENCED_ASSEMBLER = BLOCKS.registerBlock("upgrade_experienced_assembler", props -> new BlockUpgrade(props, AssemblerUpgrade.getUpgrade(4)), BlockBehaviour.Properties.of().noOcclusion().randomTicks().strength(2.0F));
    public static final DeferredHolder<Block, Block> NEW_ERA = BLOCKS.registerBlock("upgrade_new_era", props -> new BlockUpgrade(props, AssemblerUpgrade.getUpgrade(5)), BlockBehaviour.Properties.of().noOcclusion().randomTicks().strength(2.0F));
    public static final DeferredHolder<Block, Block> COTWO_FRIENDLY = BLOCKS.registerBlock("upgrade_cotwo_friendly", props -> new BlockUpgrade(props, AssemblerUpgrade.getUpgrade(6)), BlockBehaviour.Properties.of().noOcclusion().randomTicks().strength(2.0F));
    public static final DeferredHolder<Block, Block> GENERIC_ENGINE = BLOCKS.registerBlock("upgrade_generic_engine", props -> new BlockUpgrade(props, AssemblerUpgrade.getUpgrade(7)), BlockBehaviour.Properties.of().noOcclusion().randomTicks().strength(2.0F));
    public static final DeferredHolder<Block, Block> MODULE_INPUT = BLOCKS.registerBlock("upgrade_module_input", props -> new BlockUpgrade(props, AssemblerUpgrade.getUpgrade(8)), BlockBehaviour.Properties.of().noOcclusion().randomTicks().strength(2.0F));
    public static final DeferredHolder<Block, Block> PRODUCTION_LINE = BLOCKS.registerBlock("upgrade_production_line", props -> new BlockUpgrade(props, AssemblerUpgrade.getUpgrade(9)), BlockBehaviour.Properties.of().noOcclusion().randomTicks().strength(2.0F));
    public static final DeferredHolder<Block, Block> CART_DEPLOYER = BLOCKS.registerBlock("upgrade_cart_deployer", props -> new BlockUpgrade(props, AssemblerUpgrade.getUpgrade(10)), BlockBehaviour.Properties.of().noOcclusion().randomTicks().strength(2.0F));
    public static final DeferredHolder<Block, Block> CART_MODIFIER = BLOCKS.registerBlock("upgrade_cart_modifier", props -> new BlockUpgrade(props, AssemblerUpgrade.getUpgrade(11)), BlockBehaviour.Properties.of().noOcclusion().randomTicks().strength(2.0F));
    public static final DeferredHolder<Block, Block> CART_CRANE = BLOCKS.registerBlock("upgrade_cart_crane", props -> new BlockUpgrade(props, AssemblerUpgrade.getUpgrade(12)), BlockBehaviour.Properties.of().noOcclusion().randomTicks().strength(2.0F));
    public static final DeferredHolder<Block, Block> REDSTONE_CONTROL = BLOCKS.registerBlock("upgrade_redstone_control", props -> new BlockUpgrade(props, AssemblerUpgrade.getUpgrade(13)), BlockBehaviour.Properties.of().noOcclusion().randomTicks().strength(2.0F));
    public static final DeferredHolder<Block, Block> CREATIVE_MODE = BLOCKS.registerBlock("upgrade_creative_mode", props -> new BlockUpgrade(props, AssemblerUpgrade.getUpgrade(14)), BlockBehaviour.Properties.of().noOcclusion().randomTicks().strength(2.0F));
    public static final DeferredHolder<Block, Block> QUICK_DEMOLISHER = BLOCKS.registerBlock("upgrade_quick_demolisher", props -> new BlockUpgrade(props, AssemblerUpgrade.getUpgrade(15)), BlockBehaviour.Properties.of().noOcclusion().randomTicks().strength(2.0F));
    public static final DeferredHolder<Block, Block> ENTROPY = BLOCKS.registerBlock("upgrade_entropy", props -> new BlockUpgrade(props, AssemblerUpgrade.getUpgrade(16)), BlockBehaviour.Properties.of().noOcclusion().randomTicks().strength(2.0F));
    public static final DeferredHolder<Block, Block> MANAGER_BRIDGE = BLOCKS.registerBlock("upgrade_manager_bridge", props -> new BlockUpgrade(props, AssemblerUpgrade.getUpgrade(17)), BlockBehaviour.Properties.of().noOcclusion().randomTicks().strength(2.0F));
    public static final DeferredHolder<Block, Block> THERMAL_ENGINE = BLOCKS.registerBlock("upgrade_thermal_engine", props -> new BlockUpgrade(props, AssemblerUpgrade.getUpgrade(18)), BlockBehaviour.Properties.of().noOcclusion().randomTicks().strength(2.0F));
    public static final DeferredHolder<Block, Block> SOLAR_PANEL = BLOCKS.registerBlock("upgrade_solar_panel", props -> new BlockUpgrade(props, AssemblerUpgrade.getUpgrade(19)), BlockBehaviour.Properties.of().noOcclusion().randomTicks().strength(2.0F));


    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<TileEntityUpgrade>> UPGRADE_TILE = TILES_ENTITIES.register("upgrade", () -> new BlockEntityType<>(TileEntityUpgrade::new, ModBlocks.BATTERIES.get(), ModBlocks.POWER_CRYSTAL.get(), ModBlocks.KNOWLEDGE.get(), ModBlocks.INDUSTRIAL_ESPIONAGE.get(), ModBlocks.EXPERIENCED_ASSEMBLER.get(), ModBlocks.NEW_ERA.get(), ModBlocks.COTWO_FRIENDLY.get(), ModBlocks.GENERIC_ENGINE.get(), ModBlocks.MODULE_INPUT.get(), ModBlocks.PRODUCTION_LINE.get(), ModBlocks.CART_DEPLOYER.get(), ModBlocks.CART_MODIFIER.get(), ModBlocks.CART_CRANE.get(), ModBlocks.REDSTONE_CONTROL.get(), ModBlocks.CREATIVE_MODE.get(), ModBlocks.QUICK_DEMOLISHER.get(), ModBlocks.ENTROPY.get(), ModBlocks.MANAGER_BRIDGE.get(), ModBlocks.THERMAL_ENGINE.get(), ModBlocks.SOLAR_PANEL.get()));

}
