package vswe.stevescarts.init;

import net.minecraft.util.Util;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import vswe.stevescarts.Constants;
import vswe.stevescarts.api.StevesCartsAPI;
import vswe.stevescarts.api.modules.data.ModuleData;
import vswe.stevescarts.helpers.ComponentTypes;
import vswe.stevescarts.items.ItemCartComponent;
import vswe.stevescarts.items.ItemCartModule;
import vswe.stevescarts.items.ItemCarts;
import vswe.stevescarts.items.TooltipBlockItem;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Supplier;

public class ModItems
{
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(Constants.MOD_ID);

    public static final DeferredHolder<Item, Item> CART_ASSEMBLER = ITEMS.registerItem("blockcartassembler", props -> new BlockItem(ModBlocks.CART_ASSEMBLER.get(), props));
    public static final DeferredHolder<Item, Item> CARGO_MANAGER = ITEMS.registerItem("blockcargomanager", props -> new BlockItem(ModBlocks.CARGO_MANAGER.get(), props));
    public static final DeferredHolder<Item, Item> LIQUID_MANAGER = ITEMS.registerItem("blockliquidmanager", props -> new BlockItem(ModBlocks.LIQUID_MANAGER.get(), props));

    public static final DeferredHolder<Item, Item> EXTERNAL_DISTRIBUTOR = ITEMS.registerItem("blockdistributor", props -> new BlockItem(ModBlocks.EXTERNAL_DISTRIBUTOR.get(), props));
    public static final DeferredHolder<Item, Item> MODULE_TOGGLER = ITEMS.registerItem("blockactivator", props -> new BlockItem(ModBlocks.MODULE_TOGGLER.get(), props));
    public static final DeferredHolder<Item, Item> JUNCTION = ITEMS.registerItem("blockjunction", props -> new BlockItem(ModBlocks.JUNCTION.get(), props));
    public static final DeferredHolder<Item, Item> ADVANCED_DETECTOR = ITEMS.registerItem("blockadvdetector", props -> new BlockItem(ModBlocks.ADVANCED_DETECTOR.get(), props));

    //storage blocks
    public static final DeferredHolder<Item, Item> REINFORCED_METAL = ITEMS.registerItem("reinforced_metal", props -> new BlockItem(ModBlocks.REINFORCED_METAL.get(), props));
    public static final DeferredHolder<Item, Item> GALGADORIAN_METAL = ITEMS.registerItem("galgadorian_metal", props -> new BlockItem(ModBlocks.GALGADORIAN_METAL.get(), props));
    public static final DeferredHolder<Item, Item> ENHANCED_GALGADORIAN_METAL = ITEMS.registerItem("enhanced_galgadorian_metal", props -> new BlockItem(ModBlocks.ENHANCED_GALGADORIAN_METAL.get(), props));

    //upgrades
    public static final DeferredHolder<Item, Item> BATTERIES = ITEMS.registerItem("upgrade_batteries", props -> new TooltipBlockItem(ModBlocks.BATTERIES.get(), props));
    public static final DeferredHolder<Item, Item> POWER_CRYSTAL = ITEMS.registerItem("upgrade_power_crystal", props -> new TooltipBlockItem(ModBlocks.POWER_CRYSTAL.get(), props));
    public static final DeferredHolder<Item, Item> KNOWLEDGE = ITEMS.registerItem("upgrade_module_knowledge", props -> new TooltipBlockItem(ModBlocks.KNOWLEDGE.get(), props));
    public static final DeferredHolder<Item, Item> INDUSTRIAL_ESPIONAGE = ITEMS.registerItem("upgrade_industrial_espionage", props -> new TooltipBlockItem(ModBlocks.INDUSTRIAL_ESPIONAGE.get(), props));
    public static final DeferredHolder<Item, Item> EXPERIENCED_ASSEMBLER = ITEMS.registerItem("upgrade_experienced_assembler", props -> new TooltipBlockItem(ModBlocks.EXPERIENCED_ASSEMBLER.get(), props));
    public static final DeferredHolder<Item, Item> NEW_ERA = ITEMS.registerItem("upgrade_new_era", props -> new TooltipBlockItem(ModBlocks.NEW_ERA.get(), props));
    public static final DeferredHolder<Item, Item> COTWO_FRIENDLY = ITEMS.registerItem("upgrade_cotwo_friendly", props -> new TooltipBlockItem(ModBlocks.COTWO_FRIENDLY.get(), props));
    public static final DeferredHolder<Item, Item> GENERIC_ENGINE = ITEMS.registerItem("upgrade_generic_engine", props -> new TooltipBlockItem(ModBlocks.GENERIC_ENGINE.get(), props));
    public static final DeferredHolder<Item, Item> MODULE_INPUT = ITEMS.registerItem("upgrade_module_input", props -> new TooltipBlockItem(ModBlocks.MODULE_INPUT.get(), props));
    public static final DeferredHolder<Item, Item> PRODUCTION_LINE = ITEMS.registerItem("upgrade_production_line", props -> new TooltipBlockItem(ModBlocks.PRODUCTION_LINE.get(), props));
    public static final DeferredHolder<Item, Item> CART_DEPLOYER = ITEMS.registerItem("upgrade_cart_deployer", props -> new TooltipBlockItem(ModBlocks.CART_DEPLOYER.get(), props));
    public static final DeferredHolder<Item, Item> CART_MODIFIER = ITEMS.registerItem("upgrade_cart_modifier", props -> new TooltipBlockItem(ModBlocks.CART_MODIFIER.get(), props));
    public static final DeferredHolder<Item, Item> CART_CRANE = ITEMS.registerItem("upgrade_cart_crane", props -> new TooltipBlockItem(ModBlocks.CART_CRANE.get(), props));
    public static final DeferredHolder<Item, Item> REDSTONE_CONTROL = ITEMS.registerItem("upgrade_redstone_control", props -> new TooltipBlockItem(ModBlocks.REDSTONE_CONTROL.get(), props));
    public static final DeferredHolder<Item, Item> CREATIVE_MODE = ITEMS.registerItem("upgrade_creative_mode", props -> new TooltipBlockItem(ModBlocks.CREATIVE_MODE.get(), props));
    public static final DeferredHolder<Item, Item> QUICK_DEMOLISHER = ITEMS.registerItem("upgrade_quick_demolisher", props -> new TooltipBlockItem(ModBlocks.QUICK_DEMOLISHER.get(), props));
    public static final DeferredHolder<Item, Item> ENTROPY = ITEMS.registerItem("upgrade_entropy", props -> new TooltipBlockItem(ModBlocks.ENTROPY.get(), props));
    public static final DeferredHolder<Item, Item> MANAGER_BRIDGE = ITEMS.registerItem("upgrade_manager_bridge", props -> new TooltipBlockItem(ModBlocks.MANAGER_BRIDGE.get(), props));
    public static final DeferredHolder<Item, Item> THERMAL_ENGINE = ITEMS.registerItem("upgrade_thermal_engine", props -> new TooltipBlockItem(ModBlocks.THERMAL_ENGINE.get(), props));
    public static final DeferredHolder<Item, Item> SOLAR_PANEL = ITEMS.registerItem("upgrade_solar_panel", props -> new TooltipBlockItem(ModBlocks.SOLAR_PANEL.get(), props));


    public static final DeferredHolder<Item, Item> CARTS = ITEMS.registerItem("modularcart", ItemCarts::new, new Item.Properties().stacksTo(1).rarity(Rarity.EPIC));

    public static final Map<ComponentTypes, Supplier<Item>> COMPONENTS = Util.make(new LinkedHashMap<>(), map ->
    {
        for (ComponentTypes value : ComponentTypes.values())
        {
            if (value != null && value.getName() != null)
            {
                String name = ("component_" + value.getName());
                map.put(value, ITEMS.registerItem(name, props -> new ItemCartComponent(value, props)));
            }
        }
    });

    public static final Map<ModuleData, Supplier<Item>> MODULES = Util.make(new LinkedHashMap<>(), map -> {
        for (ModuleData value : StevesCartsAPI.MODULE_REGISTRY.values()) {
            if (value.getID().getNamespace().equalsIgnoreCase(Constants.MOD_ID)){
                map.put(value, ITEMS.registerItem(value.getName(), props -> new ItemCartModule(value, props)));
            }
        }
    });
}
