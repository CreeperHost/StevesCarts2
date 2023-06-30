package vswe.stevescarts.items;

import net.minecraft.ChatFormatting;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.ByteArrayTag;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.world.item.*;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import org.jetbrains.annotations.NotNull;
import vswe.stevescarts.StevesCarts;
import vswe.stevescarts.api.StevesCartsAPI;
import vswe.stevescarts.api.modules.ModuleBase;
import vswe.stevescarts.api.modules.data.ModuleData;
import vswe.stevescarts.client.renders.ItemStackRenderer;
import vswe.stevescarts.entities.EntityMinecartModular;

import javax.annotation.Nullable;
import java.util.List;
import java.util.function.Consumer;

public class ItemCarts extends MinecartItem
{
    public ItemCarts()
    {
        super(AbstractMinecart.Type.RIDEABLE, new Item.Properties().stacksTo(1).rarity(Rarity.EPIC));
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
    public void appendHoverText(@NotNull ItemStack item, @Nullable Level p_77624_2_, @NotNull List<Component> list, @NotNull TooltipFlag p_77624_4_) {
        if (item.getTag() != null) {
            if (item.getTag().contains("modules")) {
                list.add(Component.literal(ChatFormatting.BLUE + "Installed Modules:"));
                ListTag moduleListTag = (ListTag) item.getTag().get("modules");
                if (moduleListTag != null && !moduleListTag.isEmpty()) {
                    for (int i = 0; i < moduleListTag.size(); i++) {
                        CompoundTag moduleTag = (CompoundTag) moduleListTag.get(i);
                        ResourceLocation resourceLocation = new ResourceLocation(moduleTag.getString(String.valueOf(i)));
                        ModuleData moduleData = StevesCartsAPI.MODULE_REGISTRY.get(resourceLocation);
                        if (moduleData != null) list.add(Component.literal(ChatFormatting.GOLD + moduleData.getDisplayName()));
                    }
                } else {
                    list.add(Component.literal(ChatFormatting.RED + "No modules loaded"));
                }
            }
        }
    }

    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer)
    {
        consumer.accept(new IClientItemExtensions()
        {
            @Override
            public BlockEntityWithoutLevelRenderer getCustomRenderer()
            {
                return ItemStackRenderer.getInstance();
            }
        });
    }
}
