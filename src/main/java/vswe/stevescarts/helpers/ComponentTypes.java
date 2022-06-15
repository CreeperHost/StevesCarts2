package vswe.stevescarts.helpers;

import net.minecraft.world.item.ItemStack;
import vswe.stevescarts.init.ModItems;
import vswe.stevescarts.items.ItemCartComponent;

import javax.annotation.Nonnull;

public enum ComponentTypes
{
    WOODEN_WHEELS(0, "Wooden Wheels"), IRON_WHEELS(1, "Iron Wheels"), RED_PIGMENT(2, "Red Pigment"), GREEN_PIGMENT(3, "Green Pigment"), BLUE_PIGMENT(4, "Blue Pigment"), GLASS_O_MAGIC(5, "Glass o'Magic"), DYNAMITE(6, "Dynamite"), REMOVED_1(7, null), REMOVED_2(8, null), SIMPLE_PCB(9, "Simple PCB"), GRAPHICAL_INTERFACE(10, "Graphical Interface"), RAW_HANDLE(11, "Raw Handle"), REFINED_HANDLE(12, "Refined Handle"), SPEED_HANDLE(13, "Speed Handle"), WHEEL(14, "Wheel"), SAW_BLADE(15, "Saw Blade"), ADVANCED_PCB(16, "Advanced PCB"), WOOD_CUTTING_CORE(17, "Wood Cutting Core"), RAW_HARDENER(18, "Raw Hardener"), REFINED_HARDENER(19, "Refined Hardener"), HARDENED_MESH(20, "Hardened Mesh"), STABILIZED_METAL(21, "Stabilized Metal"), REINFORCED_METAL(22, "Reinforced Metal"), REINFORCED_WHEELS(23, "Reinforced Wheels"), PIPE(24, "Pipe"), SHOOTING_STATION(25, "Shooting Station"), ENTITY_SCANNER(26, "Entity Scanner"), ENTITY_ANALYZER(27, "Entity Analyzer"), EMPTY_DISK(28, "Empty Disk"), TRI_TORCH(29, "Tri-torch"), CHEST_PANE(30, "Chest Pane"), LARGE_CHEST_PANE(31, "Large Chest Pane"), HUGE_CHEST_PANE(32, "Huge Chest Pane"), CHEST_LOCK(33, "Chest Lock"), IRON_PANE(34, "Iron Pane"), LARGE_IRON_PANE(35, "Large Iron Pane"), HUGE_IRON_PANE(36, "Huge Iron Pane"), DYNAMIC_PANE(37, "Dynamic Pane"), LARGE_DYNAMIC_PANE(38, "Large Dynamic Pane"), HUGE_DYNAMIC_PANE(39, "Huge Dynamic Pane"), CLEANING_FAN(40, "Cleaning Fan"), CLEANING_CORE(41, "Cleaning Core"), CLEANING_TUBE(42, "Cleaning Tube"), FUSE(43, "Fuse"), SOLAR_PANEL(44, "Solar Panel Component"), EYE_OF_GALGADOR(45, "Eye of Galgador"), LUMP_OF_GALGADOR(46, "Lump of Galgador"), GALGADORIAN_METAL(47, "Galgadorian Metal"), LARGE_LUMP_OF_GALGADOR(48, "Large Lump of Galgador"), ENHANCED_GALGADORIAN_METAL(49, "Enhanced Galgadorian Metal"), STOLEN_PRESENT(50, "Stolen Present"), GREEN_WRAPPING_PAPER(51, "Green Wrapping Paper"), RED_WRAPPING_PAPER(52, "Red Wrapping Paper"), WARM_HAT(53, "Warm hat"), RED_GIFT_RIBBON(54, "Red Gift Ribbon"), YELLOW_GIFT_RIBBON(55, "Yellow Gift Ribbon"), SOCK(56, "Sock"), STUFFED_SOCK(57, "Stuffed Sock"), ADVANCED_SOLAR_PANEL(58, "Advanced Solar Panel"), BLANK_UPGRADE(59, "Blank Upgrade"), TANK_VALVE(60, "SCTank Valve"), TANK_PANE(61, "SCTank Pane"), LARGE_TANK_PANE(62, "Large SCTank Pane"), HUGE_TANK_PANE(63, "Huge SCTank Pane"), LIQUID_CLEANING_CORE(64, "Liquid Cleaning Core"), LIQUID_CLEANING_TUBE(65, "Liquid Cleaning Tube"), EXPLOSIVE_EASTER_EGG(66, "Explosive Easter Egg"), BURNING_EASTER_EGG(67, "Burning Easter Egg"), GLISTERING_EASTER_EGG(68, "Glistering Easter Egg"), CHOCOLATE_EASTER_EGG(69, "Chocolate Easter Egg"), PAINTED_EASTER_EGG(70, "Painted Easter Egg"), BASKET(71, "Basket"), OAK_LOG(72, "Oak Log"), OAK_TWIG(73, "Oak Twig"), SPRUCE_LOG(74, "Spruce Log"), SPRUCE_TWIG(75, "Spruce Twig"), BIRCH_LOG(76, "Birch Log"), BIRCH_TWIG(77, "Birch Twig"), JUNGLE_LOG(78, "Jungle Log"), JUNGLE_TWIG(79, "Jungle Twig"), HARDENED_SAW_BLADE(80, "Hardened Saw Blade"), GALGADORIAN_SAW_BLADE(81, "Galgadorian Saw Blade"), GALGADORIAN_WHEELS(82, "Galgadorian Wheels"), IRON_BLADE(83, "Iron Blade"), BLADE_ARM(84, "Blade Arm");

    private int id;
    private String name;

    ComponentTypes(final int id, final String name)
    {
        this.id = id;
        this.name = name;
    }

    public String getName()
    {
        return name;
    }

    @Nonnull
    public ItemStack getItemStack(final int count)
    {
        return new ItemStack(getItemStack().getItem(), count);
    }

    @Nonnull
    public ItemStack getItemStack()
    {
        return new ItemStack(ModItems.COMPONENTS.get(this).get());
//        for (ItemCartComponent cartComponent : ModItems.CART_COMPONENTS)
//        {
//            if (cartComponent.componentType.getId() == this.id)
//            {
//                return new ItemStack(cartComponent);
//            }
//        }
//
//        return new ItemStack(ModItems.CART_COMPONENTS.get(1));
    }

    public int getId()
    {
        return id;
    }

    public boolean isStackOfType(@Nonnull ItemStack itemstack)
    {
        return !itemstack.isEmpty() && itemstack.getItem() instanceof ItemCartComponent;
    }
}
