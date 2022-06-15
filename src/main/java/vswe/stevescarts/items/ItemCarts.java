package vswe.stevescarts.items;

import net.minecraft.ChatFormatting;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.ByteArrayTag;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.MinecartItem;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.IItemRenderProperties;
import vswe.stevescarts.StevesCarts;
import vswe.stevescarts.client.renders.ItemStackRenderer;
import vswe.stevescarts.entitys.EntityMinecartModular;
import vswe.stevescarts.helpers.CartVersion;
import vswe.stevescarts.helpers.ModuleCountPair;
import vswe.stevescarts.modules.ModuleBase;
import vswe.stevescarts.modules.data.ModuleData;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class ItemCarts extends MinecartItem
{
    public ItemCarts()
    {
        super(AbstractMinecart.Type.RIDEABLE, new Item.Properties().stacksTo(1));
    }

    public String getName()
    {
        return "Modular Cart";
    }

    @Override
    public InteractionResult useOn(UseOnContext itemUseContext)
    {
        Player player = itemUseContext.getPlayer();
        Level world = itemUseContext.getLevel();
        ItemStack stack = player.getItemInHand(itemUseContext.getHand());
        BlockPos pos = itemUseContext.getClickedPos();
        CartVersion.updateItemStack(stack);
        if (!world.isClientSide)
        {
            BlockState blockstate = world.getBlockState(pos);
            if (blockstate.is(BlockTags.RAILS))
            {
                try
                {
                    final CompoundTag info = stack.getTag();
                    if (info != null && !info.contains("maxTime"))
                    {
                        try
                        {
                            final EntityMinecartModular cart = new EntityMinecartModular(world, pos.getX() + 0.5f, pos.getY() + 0.5f, pos.getZ() + 0.5f, info, Component.literal(""));
                            world.addFreshEntity(cart);
                        } catch (Exception e)
                        {
                            e.printStackTrace();
                            player.displayClientMessage(Component.literal("The cart failed to be placed into the world, this is due to an issue with one or more modules. " + "Please post your log on the issue tracker here: " + ChatFormatting.BLUE + " https://github.com/modmuss50/SC2/issues"), false);
                            StevesCarts.logger.error(" --------------- Broken cart info --------------- ");
                            StevesCarts.logger.error(info);
                            ByteArrayTag moduleIDTag = (ByteArrayTag) info.get("Modules");
                            for (final byte id : moduleIDTag.getAsByteArray())
                            {
                                try
                                {
                                    final Class<? extends ModuleBase> moduleClass = ModuleData.getList().get(id).getModuleClass();
                                    StevesCarts.logger.error("--- " + moduleClass.getCanonicalName());
                                } catch (Exception ex)
                                {
                                    StevesCarts.logger.error("Failed to load module with ID " + id + "! More info below.");
                                    e.printStackTrace();
                                }
                            }
                            StevesCarts.logger.error(" --------------- Broken cart info --------------- ");
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
    public void appendHoverText(ItemStack item, @Nullable Level p_77624_2_, List<Component> list, TooltipFlag p_77624_4_)
    {
        CartVersion.updateItemStack(item);
        final CompoundTag info = item.getTag();
        if (info != null)
        {
            final ByteArrayTag moduleIDTag = (ByteArrayTag) info.get("Modules");
            final byte[] bytes = moduleIDTag.getAsByteArray();
            final ArrayList<ModuleCountPair> counts = new ArrayList<>();
            for (int i = 0; i < bytes.length; ++i)
            {
                final byte id = bytes[i];
                final ModuleData module = ModuleData.getList().get(id);
                if (module != null)
                {
                    boolean found = false;
                    if (!info.contains("Data" + i))
                    {
                        for (final ModuleCountPair count : counts)
                        {
                            if (count.isContainingData(module))
                            {
                                count.increase();
                                found = true;
                                break;
                            }
                        }
                    }
                    if (!found)
                    {
                        final ModuleCountPair count2 = new ModuleCountPair(module);
                        if (info.contains("Data" + i))
                        {
                            count2.setExtraData(info.getByte("Data" + i));
                        }
                        counts.add(count2);
                    }
                }
            }
            for (final ModuleCountPair count3 : counts)
            {
                list.add(Component.translatable(count3.toString()));
            }
            if (info.contains("Spares"))
            {
                final byte[] spares = info.getByteArray("Spares");
                for (int j = 0; j < spares.length; ++j)
                {
                    final byte id2 = spares[j];
                    final ModuleData module2 = ModuleData.getList().get(id2);
                    if (module2 != null)
                    {
                        String name = module2.getName();
                        if (info.contains("Data" + (bytes.length + j)))
                        {
                            name = module2.getCartInfoText(name, info.getByte("Data" + (bytes.length + j)));
                        }
                        list.add(Component.translatable(ChatFormatting.GOLD + name));
                    }
                }
            }
            if (info.contains("maxTime"))
            {
                list.add(Component.literal(ChatFormatting.RED + "Incomplete cart!"));
                final int maxTime = info.getInt("maxTime");
                final int currentTime = info.getInt("currentTime");
                final int timeLeft = maxTime - currentTime;
                list.add(Component.literal(ChatFormatting.RED + "Time left: " + formatTime(timeLeft)));
            }
        }
        else
        {
            list.add(Component.literal("No modules loaded"));
        }
    }

    private String formatTime(int ticks)
    {
        int seconds = ticks / 20;
        ticks -= seconds * 20;
        int minutes = seconds / 60;
        seconds -= minutes * 60;
        final int hours = minutes / 60;
        minutes -= hours * 60;
        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }

    @Override
    public void initializeClient(Consumer<IItemRenderProperties> consumer)
    {
        consumer.accept(new IItemRenderProperties() {
            @Override
            public BlockEntityWithoutLevelRenderer getItemStackRenderer() {
                return ItemStackRenderer.getInstance();
            }
        });
    }
}