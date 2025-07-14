package vswe.stevescarts.items;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.level.block.Block;

import java.util.function.Consumer;

/**
 * Created by brandon3055 on 14/07/2025
 */
public class TooltipBlockItem extends BlockItem {

    public TooltipBlockItem(Block block, Properties properties) {
        super(block, properties);
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, TooltipDisplay display, Consumer<Component> consumer, TooltipFlag tooltipFlag) {
        if (getBlock() instanceof TooltipBlock block) {
            block.appendHoverText(stack, context, display, consumer, tooltipFlag);
        }
    }
}
