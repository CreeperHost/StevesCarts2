package vswe.stevescarts.helpers;

import net.minecraft.world.item.ItemStack;
import vswe.stevescarts.init.ModItems;
import vswe.stevescarts.items.ItemCartComponent;

import javax.annotation.Nonnull;

public enum ComponentTypes
{
    WOODEN_WHEELS( "wooden_wheels"),
    IRON_WHEELS("iron_wheels"),
    RED_PIGMENT("red_pigment"),
    GREEN_PIGMENT("green_pigment"),
    BLUE_PIGMENT("blue_pigment"),
    GLASS_O_MAGIC("glass_o_magic"),
    DYNAMITE("dynamite"),
    SIMPLE_PCB("simple_pcb"),
    GRAPHICAL_INTERFACE("graphical_interface"),
    RAW_HANDLE("raw_handle"),
    REFINED_HANDLE("refined_handle"),
    SPEED_HANDLE("speed_handle"),
    WHEEL("wheel"),
    SAW_BLADE("saw_blade"),
    ADVANCED_PCB("advanced_pcb"),
    WOOD_CUTTING_CORE("wood_cutting_core"),
    RAW_HARDENER("raw_hardener"),
    REFINED_HARDENER("refined_hardener"),
    HARDENED_MESH("hardened_mesh"),
    STABILIZED_METAL("stabilized_metal"),
    REINFORCED_METAL("reinforced_metal"),
    REINFORCED_WHEELS("reinforced_wheels"),
    PIPE("pipe"),
    SHOOTING_STATION("shooting_station"),
    ENTITY_SCANNER("entity_scanner"),
    ENTITY_ANALYZER("entity_analyzer"),
    EMPTY_DISK("empty_disk"),
    TRI_TORCH("tri_torch"),
    CHEST_PANE("chest_pane"),
    LARGE_CHEST_PANE("large_chest_pane"),
    HUGE_CHEST_PANE("huge_chest_pane"),
    CHEST_LOCK("chest_lock"),
    IRON_PANE("iron_pane"),
    LARGE_IRON_PANE("large_iron_pane"),
    HUGE_IRON_PANE("huge_iron_pane"),
    DYNAMIC_PANE("dynamic_pane"),
    LARGE_DYNAMIC_PANE("large_dynamic_pane"),
    HUGE_DYNAMIC_PANE("huge_dynamic_pane"),
    CLEANING_FAN("cleaning_fan"),
    CLEANING_CORE("cleaning_core"),
    CLEANING_TUBE("cleaning_tube"),
    FUSE( "fuse"),
    SOLAR_PANEL("solar_panel_component"),
    EYE_OF_GALGADOR("eye_of_galgador"),
    LUMP_OF_GALGADOR( "lump_of_galgador"),
    GALGADORIAN_METAL("galgadorian_metal"),
    LARGE_LUMP_OF_GALGADOR("large_lump_of_galgador"),
    ENHANCED_GALGADORIAN_METAL( "enhanced_galgadorian_metal"),
    STOLEN_PRESENT( "stolen_present"),
    GREEN_WRAPPING_PAPER("green_wrapping_paper"),
    RED_WRAPPING_PAPER("red_wrapping_paper"),
    WARM_HAT( "warm_hat"),
    RED_GIFT_RIBBON("red_gift_ribbon"),
    YELLOW_GIFT_RIBBON( "yellow_gift_ribbon"),
    SOCK( "sock"),
    STUFFED_SOCK( "stuffed_sock"),
    ADVANCED_SOLAR_PANEL("advanced_solar_panel"),
    BLANK_UPGRADE("blank_upgrade"),
    TANK_VALVE( "sctank_valve"),
    TANK_PANE( "sctank_pane"),
    LARGE_TANK_PANE("large_sctank_pane"),
    HUGE_TANK_PANE("huge_sctank_pane"),
    LIQUID_CLEANING_CORE("liquid_cleaning_core"),
    LIQUID_CLEANING_TUBE("liquid_cleaning_tube"),
    EXPLOSIVE_EASTER_EGG("explosive_easter_egg"),
    BURNING_EASTER_EGG( "burning_easter_egg"),
    GLISTERING_EASTER_EGG("glistering_easter_egg"),
    CHOCOLATE_EASTER_EGG( "chocolate_easter_egg"),
    PAINTED_EASTER_EGG("painted_easter_egg"),
    BASKET("basket"),
    OAK_LOG("oak_log"),
    OAK_TWIG("oak_twig"),
    SPRUCE_LOG("spruce_log"),
    SPRUCE_TWIG( "spruce_twig"),
    BIRCH_LOG( "birch_log"),
    BIRCH_TWIG( "birch_twig"),
    JUNGLE_LOG("jungle_log"),
    JUNGLE_TWIG( "jungle_twig"),
    HARDENED_SAW_BLADE( "hardened_saw_blade"),
    GALGADORIAN_SAW_BLADE( "galgadorian_saw_blade"),
    GALGADORIAN_WHEELS( "galgadorian_wheels"),
    IRON_BLADE("iron_blade"),
    BLADE_ARM("blade_arm");

    private final String name;

    ComponentTypes(final String name)
    {
        this.name = name;
    }

    public String getName()
    {
        return name;
    }

    public String getRawName()
    {
        return name;
//        return name.replace(":", "").replace("'", "").replace(" ", "_").replace("-", "_").toLowerCase();
    }

    @Nonnull
    public ItemStack getItemStack()
    {
        return new ItemStack(ModItems.COMPONENTS.get(this).get());
    }

    public boolean isStackOfType(@Nonnull ItemStack itemstack)
    {
        return !itemstack.isEmpty() && itemstack.getItem() instanceof ItemCartComponent;
    }
}
