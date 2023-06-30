package vswe.stevescarts.init;

import net.minecraft.Util;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import vswe.stevescarts.Constants;
import vswe.stevescarts.api.StevesCartsAPI;
import vswe.stevescarts.api.modules.data.ModuleData;
import vswe.stevescarts.helpers.ComponentTypes;
import vswe.stevescarts.items.ItemCartComponent;
import vswe.stevescarts.items.ItemCartModule;
import vswe.stevescarts.items.ItemCarts;

import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import java.util.function.Supplier;

public class ModItems
{
    public static final Item.Properties ITEM_GROUP = new Item.Properties();
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Constants.MOD_ID);

    public static final RegistryObject<Item> CART_ASSEMBLER = ITEMS.register("blockcartassembler", () -> new BlockItem(ModBlocks.CART_ASSEMBLER.get(), ITEM_GROUP));
    public static final RegistryObject<Item> CARGO_MANAGER = ITEMS.register("blockcargomanager", () -> new BlockItem(ModBlocks.CARGO_MANAGER.get(), ITEM_GROUP));
    public static final RegistryObject<Item> LIQUID_MANAGER = ITEMS.register("blockliquidmanager", () -> new BlockItem(ModBlocks.LIQUID_MANAGER.get(), ITEM_GROUP));

    public static final RegistryObject<Item> EXTERNAL_DISTRIBUTOR = ITEMS.register("blockdistributor", () -> new BlockItem(ModBlocks.EXTERNAL_DISTRIBUTOR.get(), ITEM_GROUP));
    public static final RegistryObject<Item> MODULE_TOGGLER = ITEMS.register("blockactivator", () -> new BlockItem(ModBlocks.MODULE_TOGGLER.get(), ITEM_GROUP));
    public static final RegistryObject<Item> JUNCTION = ITEMS.register("blockjunction", () -> new BlockItem(ModBlocks.JUNCTION.get(), ITEM_GROUP));
    public static final RegistryObject<Item> ADVANCED_DETECTOR = ITEMS.register("blockadvdetector", () -> new BlockItem(ModBlocks.ADVANCED_DETECTOR.get(), ITEM_GROUP));

    //storage blocks
    public static final RegistryObject<Item> REINFORCED_METAL = ITEMS.register("reinforced_metal", () -> new BlockItem(ModBlocks.REINFORCED_METAL.get(), ITEM_GROUP));
    public static final RegistryObject<Item> GALGADORIAN_METAL = ITEMS.register("galgadorian_metal", () -> new BlockItem(ModBlocks.GALGADORIAN_METAL.get(), ITEM_GROUP));
    public static final RegistryObject<Item> ENHANCED_GALGADORIAN_METAL = ITEMS.register("enhanced_galgadorian_metal", () -> new BlockItem(ModBlocks.ENHANCED_GALGADORIAN_METAL.get(), ITEM_GROUP));

    //upgrades
    public static final RegistryObject<Item> BATTERIES = ITEMS.register("upgrade_batteries", () -> new BlockItem(ModBlocks.BATTERIES.get(), ITEM_GROUP));
    public static final RegistryObject<Item> POWER_CRYSTAL = ITEMS.register("upgrade_power_crystal", () -> new BlockItem(ModBlocks.POWER_CRYSTAL.get(), ITEM_GROUP));
    public static final RegistryObject<Item> KNOWLEDGE = ITEMS.register("upgrade_module_knowledge", () -> new BlockItem(ModBlocks.KNOWLEDGE.get(), ITEM_GROUP));
    public static final RegistryObject<Item> INDUSTRIAL_ESPIONAGE = ITEMS.register("upgrade_industrial_espionage", () -> new BlockItem(ModBlocks.INDUSTRIAL_ESPIONAGE.get(), ITEM_GROUP));
    public static final RegistryObject<Item> EXPERIENCED_ASSEMBLER = ITEMS.register("upgrade_experienced_assembler", () -> new BlockItem(ModBlocks.EXPERIENCED_ASSEMBLER.get(), ITEM_GROUP));
    public static final RegistryObject<Item> NEW_ERA = ITEMS.register("upgrade_new_era", () -> new BlockItem(ModBlocks.NEW_ERA.get(), ITEM_GROUP));
    public static final RegistryObject<Item> COTWO_FRIENDLY = ITEMS.register("upgrade_cotwo_friendly", () -> new BlockItem(ModBlocks.COTWO_FRIENDLY.get(), ITEM_GROUP));
    public static final RegistryObject<Item> GENERIC_ENGINE = ITEMS.register("upgrade_generic_engine", () -> new BlockItem(ModBlocks.GENERIC_ENGINE.get(), ITEM_GROUP));
    public static final RegistryObject<Item> MODULE_INPUT = ITEMS.register("upgrade_module_input", () -> new BlockItem(ModBlocks.MODULE_INPUT.get(), ITEM_GROUP));
    public static final RegistryObject<Item> PRODUCTION_LINE = ITEMS.register("upgrade_production_line", () -> new BlockItem(ModBlocks.PRODUCTION_LINE.get(), ITEM_GROUP));
    public static final RegistryObject<Item> CART_DEPLOYER = ITEMS.register("upgrade_cart_deployer", () -> new BlockItem(ModBlocks.CART_DEPLOYER.get(), ITEM_GROUP));
    public static final RegistryObject<Item> CART_MODIFIER = ITEMS.register("upgrade_cart_modifier", () -> new BlockItem(ModBlocks.CART_MODIFIER.get(), ITEM_GROUP));
    public static final RegistryObject<Item> CART_CRANE = ITEMS.register("upgrade_cart_crane", () -> new BlockItem(ModBlocks.CART_CRANE.get(), ITEM_GROUP));
    public static final RegistryObject<Item> REDSTONE_CONTROL = ITEMS.register("upgrade_redstone_control", () -> new BlockItem(ModBlocks.REDSTONE_CONTROL.get(), ITEM_GROUP));
    public static final RegistryObject<Item> CREATIVE_MODE = ITEMS.register("upgrade_creative_mode", () -> new BlockItem(ModBlocks.CREATIVE_MODE.get(), ITEM_GROUP));
    public static final RegistryObject<Item> QUICK_DEMOLISHER = ITEMS.register("upgrade_quick_demolisher", () -> new BlockItem(ModBlocks.QUICK_DEMOLISHER.get(), ITEM_GROUP));
    public static final RegistryObject<Item> ENTROPY = ITEMS.register("upgrade_entropy", () -> new BlockItem(ModBlocks.ENTROPY.get(), ITEM_GROUP));
    public static final RegistryObject<Item> MANAGER_BRIDGE = ITEMS.register("upgrade_manager_bridge", () -> new BlockItem(ModBlocks.MANAGER_BRIDGE.get(), ITEM_GROUP));
    public static final RegistryObject<Item> THERMAL_ENGINE = ITEMS.register("upgrade_thermal_engine", () -> new BlockItem(ModBlocks.THERMAL_ENGINE.get(), ITEM_GROUP));
    public static final RegistryObject<Item> SOLAR_PANEL = ITEMS.register("upgrade_solar_panel", () -> new BlockItem(ModBlocks.SOLAR_PANEL.get(), ITEM_GROUP));


    public static final RegistryObject<Item> CARTS = ITEMS.register("modularcart", ItemCarts::new);

    public static final Map<ComponentTypes, Supplier<Item>> COMPONENTS = Util.make(new LinkedHashMap<>(), map ->
    {
        for (ComponentTypes value : ComponentTypes.values())
        {
            if (value != null && value.getName() != null)
            {
                String name = ("component_" + value.getName()).trim().replace(" ", "_").replace("'", "_").toLowerCase(Locale.ROOT);
                map.put(value, ITEMS.register(name, () -> new ItemCartComponent(value)));
            }
        }
    });

    public static final Map<ModuleData, Supplier<Item>> MODULES = Util.make(new LinkedHashMap<>(), map -> {
        for (ModuleData value : StevesCartsAPI.MODULE_REGISTRY.values()) {
            if (value.getID().getNamespace().equalsIgnoreCase(Constants.MOD_ID)){
                map.put(value, ITEMS.register(value.getName(), () -> new ItemCartModule(value)));
            }
        }
    });
}
