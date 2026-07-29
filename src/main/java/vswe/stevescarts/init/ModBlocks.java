package vswe.stevescarts.init;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import vswe.stevescarts.Constants;
import vswe.stevescarts.blocks.*;
import vswe.stevescarts.blocks.tileentities.*;
import vswe.stevescarts.items.TooltipBlockItem;
import vswe.stevescarts.upgrades.AssemblerUpgrade;

import java.util.function.Function;
import java.util.function.Supplier;

public class ModBlocks {
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(Constants.MOD_ID);
    public static final DeferredRegister<BlockEntityType<?>> TILES_ENTITIES = DeferredRegister.create(BuiltInRegistries.BLOCK_ENTITY_TYPE, Constants.MOD_ID);

    public static final DeferredHolder<Block, Block> CART_ASSEMBLER = registerBlock("blockcartassembler", BlockCartAssembler::new, () -> Properties.of().strength(2.0F));
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<TileEntityCartAssembler>> CART_ASSEMBLER_TILE = TILES_ENTITIES.register("blockcartassembler", () -> new BlockEntityType<>(TileEntityCartAssembler::new, ModBlocks.CART_ASSEMBLER.get()));

    public static final DeferredHolder<Block, Block> CARGO_MANAGER = registerBlock("blockcargomanager", BlockCargoManager::new, () -> Properties.of().strength(2.0F));
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<TileEntityCargo>> CARGO_MANAGER_TILE = TILES_ENTITIES.register("blockcargomanager", () -> new BlockEntityType<>(TileEntityCargo::new, ModBlocks.CARGO_MANAGER.get()));

    public static final DeferredHolder<Block, Block> LIQUID_MANAGER = registerBlock("blockliquidmanager", BlockLiquidManager::new, () -> Properties.of().strength(2.0F));
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<TileEntityLiquid>> LIQUID_MANAGER_TILE = TILES_ENTITIES.register("blockliquidmanager", () -> new BlockEntityType<>(TileEntityLiquid::new, ModBlocks.LIQUID_MANAGER.get()));

    public static final DeferredHolder<Block, Block> EXTERNAL_DISTRIBUTOR = registerBlock("blockdistributor", BlockDistributor::new, () -> Properties.of().strength(2.0F));
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<TileEntityDistributor>> EXTERNAL_DISTRIBUTOR_TILE = TILES_ENTITIES.register("blockdistributor", () -> new BlockEntityType<>(TileEntityDistributor::new, ModBlocks.EXTERNAL_DISTRIBUTOR.get()));

    public static final DeferredHolder<Block, Block> MODULE_TOGGLER = registerBlock("blockactivator", BlockActivator::new, () -> Properties.of().strength(2.0F));
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<TileEntityActivator>> MODULE_TOGGLER_TILE = TILES_ENTITIES.register("blockactivator", () -> new BlockEntityType<>(TileEntityActivator::new, ModBlocks.MODULE_TOGGLER.get()));

    public static final DeferredHolder<Block, Block> JUNCTION = registerBlock("blockjunction", BlockRailJunction::new, () -> Properties.of().noCollision().strength(0.7F).sound(SoundType.METAL));
    public static final DeferredHolder<Block, Block> ADVANCED_DETECTOR = registerBlock("blockadvdetector", BlockRailAdvDetector::new, () -> Properties.of().noCollision().strength(0.7F).sound(SoundType.METAL));

    //Metal blocks
    public static final DeferredHolder<Block, Block> REINFORCED_METAL = registerBlock("reinforced_metal", BlockMetalStorage::new, () -> Properties.of().strength(2.0F));
    public static final DeferredHolder<Block, Block> GALGADORIAN_METAL = registerBlock("galgadorian_metal", BlockMetalStorage::new, () -> Properties.of().strength(2.0F));
    public static final DeferredHolder<Block, Block> ENHANCED_GALGADORIAN_METAL = registerBlock("enhanced_galgadorian_metal", BlockMetalStorage::new, () -> Properties.of().strength(2.0F));

    //Upgrades
    public static final DeferredHolder<Block, Block> BATTERIES = registerUpgrade("upgrade_batteries", props -> new BlockUpgrade(props, AssemblerUpgrade.getUpgrade(0)));
    public static final DeferredHolder<Block, Block> POWER_CRYSTAL = registerUpgrade("upgrade_power_crystal", props -> new BlockUpgrade(props, AssemblerUpgrade.getUpgrade(1)));
    public static final DeferredHolder<Block, Block> KNOWLEDGE = registerUpgrade("upgrade_module_knowledge", props -> new BlockUpgrade(props, AssemblerUpgrade.getUpgrade(2)));
    public static final DeferredHolder<Block, Block> INDUSTRIAL_ESPIONAGE = registerUpgrade("upgrade_industrial_espionage", props -> new BlockUpgrade(props, AssemblerUpgrade.getUpgrade(3)));
    public static final DeferredHolder<Block, Block> EXPERIENCED_ASSEMBLER = registerUpgrade("upgrade_experienced_assembler", props -> new BlockUpgrade(props, AssemblerUpgrade.getUpgrade(4)));
    public static final DeferredHolder<Block, Block> NEW_ERA = registerUpgrade("upgrade_new_era", props -> new BlockUpgrade(props, AssemblerUpgrade.getUpgrade(5)));
    public static final DeferredHolder<Block, Block> COTWO_FRIENDLY = registerUpgrade("upgrade_cotwo_friendly", props -> new BlockUpgrade(props, AssemblerUpgrade.getUpgrade(6)));
    public static final DeferredHolder<Block, Block> GENERIC_ENGINE = registerUpgrade("upgrade_generic_engine", props -> new BlockUpgrade(props, AssemblerUpgrade.getUpgrade(7)));
    public static final DeferredHolder<Block, Block> MODULE_INPUT = registerUpgrade("upgrade_module_input", props -> new BlockUpgrade(props, AssemblerUpgrade.getUpgrade(8)));
    public static final DeferredHolder<Block, Block> PRODUCTION_LINE = registerUpgrade("upgrade_production_line", props -> new BlockUpgrade(props, AssemblerUpgrade.getUpgrade(9)));
    public static final DeferredHolder<Block, Block> CART_DEPLOYER = registerUpgrade("upgrade_cart_deployer", props -> new BlockUpgrade(props, AssemblerUpgrade.getUpgrade(10)));
    public static final DeferredHolder<Block, Block> CART_MODIFIER = registerUpgrade("upgrade_cart_modifier", props -> new BlockUpgrade(props, AssemblerUpgrade.getUpgrade(11)));
    public static final DeferredHolder<Block, Block> CART_CRANE = registerUpgrade("upgrade_cart_crane", props -> new BlockUpgrade(props, AssemblerUpgrade.getUpgrade(12)));
    public static final DeferredHolder<Block, Block> REDSTONE_CONTROL = registerUpgrade("upgrade_redstone_control", props -> new BlockUpgrade(props, AssemblerUpgrade.getUpgrade(13)));
    public static final DeferredHolder<Block, Block> CREATIVE_MODE = registerUpgrade("upgrade_creative_mode", props -> new BlockUpgrade(props, AssemblerUpgrade.getUpgrade(14)));
    public static final DeferredHolder<Block, Block> QUICK_DEMOLISHER = registerUpgrade("upgrade_quick_demolisher", props -> new BlockUpgrade(props, AssemblerUpgrade.getUpgrade(15)));
    public static final DeferredHolder<Block, Block> ENTROPY = registerUpgrade("upgrade_entropy", props -> new BlockUpgrade(props, AssemblerUpgrade.getUpgrade(16)));
    public static final DeferredHolder<Block, Block> MANAGER_BRIDGE = registerUpgrade("upgrade_manager_bridge", props -> new BlockUpgrade(props, AssemblerUpgrade.getUpgrade(17)));
    public static final DeferredHolder<Block, Block> THERMAL_ENGINE = registerUpgrade("upgrade_thermal_engine", props -> new BlockUpgrade(props, AssemblerUpgrade.getUpgrade(18)));
    public static final DeferredHolder<Block, Block> SOLAR_PANEL = registerUpgrade("upgrade_solar_panel", props -> new BlockUpgrade(props, AssemblerUpgrade.getUpgrade(19)));

    private static DeferredBlock<Block> registerUpgrade(String name, Function<Properties, BlockUpgrade> func) {
        Properties props = Properties.of().noOcclusion().randomTicks().strength(2.0F);
        DeferredBlock<Block> block = BLOCKS.registerBlock(name, func, () -> props);
        ModItems.ITEMS.registerItem(name, (p) -> new TooltipBlockItem(block.get(), p));
        return block;
    }

    private static <B extends Block> DeferredBlock<B> registerBlock(String name, Function<Properties, ? extends B> func, Supplier<Properties> properties) {
        DeferredBlock<B> block = BLOCKS.registerBlock(name, func, properties);
        ModItems.ITEMS.registerSimpleBlockItem(block);
        return block;
    }

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<TileEntityUpgrade>> UPGRADE_TILE = TILES_ENTITIES.register("upgrade", () -> new BlockEntityType<>(TileEntityUpgrade::new, ModBlocks.BATTERIES.get(), ModBlocks.POWER_CRYSTAL.get(), ModBlocks.KNOWLEDGE.get(), ModBlocks.INDUSTRIAL_ESPIONAGE.get(), ModBlocks.EXPERIENCED_ASSEMBLER.get(), ModBlocks.NEW_ERA.get(), ModBlocks.COTWO_FRIENDLY.get(), ModBlocks.GENERIC_ENGINE.get(), ModBlocks.MODULE_INPUT.get(), ModBlocks.PRODUCTION_LINE.get(), ModBlocks.CART_DEPLOYER.get(), ModBlocks.CART_MODIFIER.get(), ModBlocks.CART_CRANE.get(), ModBlocks.REDSTONE_CONTROL.get(), ModBlocks.CREATIVE_MODE.get(), ModBlocks.QUICK_DEMOLISHER.get(), ModBlocks.ENTROPY.get(), ModBlocks.MANAGER_BRIDGE.get(), ModBlocks.THERMAL_ENGINE.get(), ModBlocks.SOLAR_PANEL.get()));
}