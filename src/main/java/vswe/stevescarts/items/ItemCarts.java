package vswe.stevescarts.items;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.ByteArrayTag;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.MinecartItem;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import vswe.stevescarts.StevesCarts;
import vswe.stevescarts.api.StevesCartsAPI;
import vswe.stevescarts.api.modules.ModuleBase;
import vswe.stevescarts.api.modules.data.ModuleData;
import vswe.stevescarts.entities.ModularMinecart;
import vswe.stevescarts.init.ModEntities;
import vswe.stevescarts.init.ModItemData;

import java.util.List;
import java.util.function.Consumer;

public class ItemCarts extends MinecartItem
{
    public ItemCarts(Properties props)
    {
        super(ModEntities.MODULAR_CART.get(), props);
    }

//    @Override
//    public Component getName()
//    {
//        return Component.literal("Modular Cart");
//    }

    @Override
    public InteractionResult useOn(UseOnContext itemUseContext)
    {
        Player player = itemUseContext.getPlayer();
        Level world = itemUseContext.getLevel();
        ItemStack stack = player.getItemInHand(itemUseContext.getHand());
        BlockPos pos = itemUseContext.getClickedPos();
        if (!world.isClientSide)
        {
            BlockState blockstate = world.getBlockState(pos);
            if (blockstate.is(BlockTags.RAILS))
            {
                try
                {
                    final CompoundTag info = ModItemData.getTagCopy(stack);
                    if (!info.contains("maxTime"))
                    {
                        try
                        {
                            ModularMinecart cart = new ModularMinecart(world, pos.getX() + 0.5f, pos.getY() + 0.5f, pos.getZ() + 0.5f, info);
                            cart.setYRot(-(player.getDirection().toYRot() + 90) + 360);
                            world.addFreshEntity(cart);
                        } catch (Exception e)
                        {
                            e.printStackTrace();
                            player.displayClientMessage(Component.literal("The cart failed to be placed into the world, this is due to an issue with one or more modules. " + "Please post your log on the issue tracker here: " + ChatFormatting.BLUE + " https://github.com/modmuss50/SC2/issues"), false);
                            StevesCarts.LOGGER.error(" --------------- Broken cart info --------------- ");
                            StevesCarts.LOGGER.error(info);
                            ByteArrayTag moduleIDTag = (ByteArrayTag) info.get("Modules");
                            for (final byte id : moduleIDTag.getAsByteArray())
                            {
                                try
                                {
                                    final Class<? extends ModuleBase> moduleClass = StevesCartsAPI.MODULE_REGISTRY.get(id).getModuleClass();
                                    StevesCarts.LOGGER.error("--- " + moduleClass.getCanonicalName());
                                } catch (Exception ex)
                                {
                                    StevesCarts.LOGGER.error("Failed to load module with ID " + id + "! More info below.");
                                    e.printStackTrace();
                                }
                            }
                            StevesCarts.LOGGER.error(" --------------- Broken cart info --------------- ");
                            return InteractionResult.FAIL;
                        }
                    }
                } catch (Exception e)
                {
                    e.printStackTrace();
                    return InteractionResult.FAIL;
                }
                stack.shrink(1);
                return InteractionResult.SUCCESS;
            }
        }
        return InteractionResult.PASS;
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, TooltipDisplay display, Consumer<Component> consumer, TooltipFlag flag) {
        if (!ModItemData.hasTag(stack)) return;
        CompoundTag tag = ModItemData.getTagCopy(stack);
        if (tag.contains("modules")) {
            consumer.accept(Component.literal(ChatFormatting.BLUE + "Installed Modules:"));
            ListTag moduleListTag = (ListTag) tag.get("modules");
            if (moduleListTag != null && !moduleListTag.isEmpty()) {
                for (int i = 0; i < moduleListTag.size(); i++) {
                    CompoundTag moduleTag = (CompoundTag) moduleListTag.get(i);
                    ResourceLocation resourceLocation = ResourceLocation.parse(moduleTag.getStringOr(String.valueOf(i), ""));
                    ModuleData moduleData = StevesCartsAPI.MODULE_REGISTRY.get(resourceLocation);
                    if (moduleData != null) consumer.accept(Component.literal(ChatFormatting.GOLD + moduleData.getDisplayName()));
                }
            } else {
                consumer.accept(Component.literal(ChatFormatting.RED + "No modules loaded"));
            }
        }
    }
}
