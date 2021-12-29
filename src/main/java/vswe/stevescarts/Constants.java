package vswe.stevescarts;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ITag;
import net.minecraft.tags.ItemTags;

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
    public static final ITag.INamedTag<Block> DIRT = BlockTags.bind("forge:dirt");
    public static final ITag.INamedTag<Block> ORE = BlockTags.bind("forge:ores");


    //Items
    public static final ITag.INamedTag<Item> SEEDS = ItemTags.bind("forge:seeds");

}
