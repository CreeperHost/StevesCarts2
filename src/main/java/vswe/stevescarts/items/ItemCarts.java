package vswe.stevescarts.items;

import net.minecraft.block.BlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.item.minecart.AbstractMinecartEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.item.MinecartItem;
import net.minecraft.nbt.ByteArrayNBT;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
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

public class ItemCarts extends MinecartItem
{
    public ItemCarts()
    {
        super(AbstractMinecartEntity.Type.RIDEABLE, new Item.Properties().stacksTo(1).setISTER(() -> ItemStackRenderer::new));
    }

    public String getName()
    {
        return "Modular Cart";
    }

    @Override
    public ActionResultType useOn(ItemUseContext itemUseContext)
    {
        PlayerEntity player = itemUseContext.getPlayer();
        World world = itemUseContext.getLevel();
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
                    final CompoundNBT info = stack.getTag();
                    if (info != null && !info.contains("maxTime"))
                    {
                        try
                        {
                            final EntityMinecartModular cart = new EntityMinecartModular(world, pos.getX() + 0.5f, pos.getY() + 0.5f, pos.getZ() + 0.5f, info, new TranslationTextComponent(""));
                            world.addFreshEntity(cart);
                        } catch (Exception e)
                        {
                            e.printStackTrace();
                            player.sendMessage(new TranslationTextComponent("The cart failed to be placed into the world, this is due to an issue with one or more modules. " + "Please post your log on the issue tracker here: " + TextFormatting.BLUE + " https://github.com/modmuss50/SC2/issues"), null);
                            StevesCarts.logger.error(" --------------- Broken cart info --------------- ");
                            StevesCarts.logger.error(info);
                            ByteArrayNBT moduleIDTag = (ByteArrayNBT) info.get("Modules");
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
                            return ActionResultType.FAIL;
                        }
                    }
                } catch (Exception e)
                {
                    e.printStackTrace();
                    return ActionResultType.FAIL;
                }
                stack.shrink(1);
                return ActionResultType.SUCCESS;
            }
        }
        return ActionResultType.PASS;
    }

    @Override
    public void appendHoverText(ItemStack item, @Nullable World p_77624_2_, List<ITextComponent> list, ITooltipFlag p_77624_4_)
    {
        CartVersion.updateItemStack(item);
        final CompoundNBT info = item.getTag();
        if (info != null)
        {
            final ByteArrayNBT moduleIDTag = (ByteArrayNBT) info.get("Modules");
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
                list.add(new TranslationTextComponent(count3.toString()));
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
                        list.add(new TranslationTextComponent(TextFormatting.GOLD + name));
                    }
                }
            }
            if (info.contains("maxTime"))
            {
                list.add(new TranslationTextComponent(TextFormatting.RED + "Incomplete cart!"));
                final int maxTime = info.getInt("maxTime");
                final int currentTime = info.getInt("currentTime");
                final int timeLeft = maxTime - currentTime;
                list.add(new TranslationTextComponent(TextFormatting.RED + "Time left: " + formatTime(timeLeft)));
            }
        }
        else
        {
            list.add(new TranslationTextComponent("No modules loaded"));
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
}
