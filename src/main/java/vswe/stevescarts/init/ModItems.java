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

public class ModItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(Constants.MOD_ID);

    public static final DeferredHolder<Item, Item> CARTS = ITEMS.registerItem("modularcart", ItemCarts::new, () -> new Item.Properties().stacksTo(1).rarity(Rarity.EPIC));

    public static final Map<ComponentTypes, Supplier<Item>> COMPONENTS = Util.make(new LinkedHashMap<>(), map ->
    {
        for (ComponentTypes value : ComponentTypes.values()) {
            if (value != null && value.getName() != null) {
                String name = ("component_" + value.getName());
                map.put(value, ITEMS.registerItem(name, props -> new ItemCartComponent(value, props)));
            }
        }
    });

    public static final Map<ModuleData, Supplier<Item>> MODULES = Util.make(new LinkedHashMap<>(), map -> {
        for (ModuleData value : StevesCartsAPI.MODULE_REGISTRY.values()) {
            if (value.getID().getNamespace().equalsIgnoreCase(Constants.MOD_ID)) {
                map.put(value, ITEMS.registerItem(value.getName(), props -> new ItemCartModule(value, props)));
            }
        }
    });
}
