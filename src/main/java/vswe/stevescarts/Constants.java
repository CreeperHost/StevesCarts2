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
    public static boolean hasGreenScreen = false;
    public static boolean freezeCartSimulation = false;
    public static boolean arcadeDevOperator = false;

    public static final TagKey<Block> ORE = BlockTags.create(new ResourceLocation("forge:ores"));
    public static final TagKey<Item> SEEDS = ItemTags.create(new ResourceLocation("forge:seeds"));

}
