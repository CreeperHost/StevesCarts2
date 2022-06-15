package vswe.stevescarts;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public class Constants
{
    public static final String MOD_ID = "stevescarts";
    public static final String NAME = "Steve's Carts 2";
    public static boolean hasGreenScreen = false;
    public static boolean isChristmas = false;
    public static boolean isHalloween = false;
    public static boolean isEaster = false;
    public static boolean freezeCartSimulation = false;
    public static boolean renderSteve = false;
    public static boolean arcadeDevOperator = false;

    //Blocks
    public static final TagKey<Block> DIRT = BlockTags.DIRT;
    public static final TagKey<Block> ORE = BlockTags.create(new ResourceLocation("forge:ores"));

    //Items
    public static final TagKey<Item> SEEDS = ItemTags.create(new ResourceLocation("forge:seeds"));

}
