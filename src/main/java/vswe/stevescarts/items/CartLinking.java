package vswe.stevescarts.items;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import vswe.stevescarts.entities.ModularMinecart;
import vswe.stevescarts.init.ModItemData;

import java.util.UUID;

public final class CartLinking {
    private static final String SELECTED_CART = "SelectedCart";
    private static final double MAX_LINK_DISTANCE_SQR = 64.0;

    private CartLinking() {
    }

    public static InteractionResult interactWithCart(ItemStack chain, ServerPlayer player, ModularMinecart cart) {
        UUID selectedId = getSelectedCart(chain);
        if (selectedId == null) {
            selectCart(chain, cart);
            player.sendSystemMessage(Component.translatable("message.stevescarts.chain.selected"));
            return InteractionResult.SUCCESS;
        }

        if (selectedId.equals(cart.getUUID())) {
            clearSelection(chain);
            player.sendSystemMessage(Component.translatable("message.stevescarts.chain.selection_cleared"));
            return InteractionResult.SUCCESS;
        }

        Entity selectedEntity = player.level().getEntity(selectedId);
        if (!(selectedEntity instanceof ModularMinecart selectedCart) || !selectedCart.isAlive()) {
            selectCart(chain, cart);
            player.sendSystemMessage(Component.translatable("message.stevescarts.chain.reselected"));
            return InteractionResult.SUCCESS;
        }

        if (selectedCart.distanceToSqr(cart) > MAX_LINK_DISTANCE_SQR) {
            player.sendSystemMessage(Component.translatable("message.stevescarts.chain.too_far"));
            return InteractionResult.SUCCESS;
        }

        ModularMinecart.LinkResult result = selectedCart.linkCart(cart);
        switch (result) {
            case LINKED -> {
                clearSelection(chain);
                if (!player.hasInfiniteMaterials()) {
                    chain.shrink(1);
                }
                player.sendSystemMessage(Component.translatable("message.stevescarts.chain.linked"));
            }
            case ALREADY_CONNECTED -> player.sendSystemMessage(Component.translatable("message.stevescarts.chain.already_connected"));
            case NO_FREE_LINK -> player.sendSystemMessage(Component.translatable("message.stevescarts.chain.no_free_link"));
            case TRAIN_FULL -> player.sendSystemMessage(Component.translatable("message.stevescarts.chain.train_full"));
        }
        return InteractionResult.SUCCESS;
    }

    public static InteractionResult cutCartLinks(ItemStack shears, ServerPlayer player, ModularMinecart cart, InteractionHand hand) {
        int linkCount = cart.unlinkAllCarts();
        if (linkCount == 0) {
            player.sendSystemMessage(Component.translatable("message.stevescarts.chain.not_linked"));
            return InteractionResult.SUCCESS;
        }

        if (!player.hasInfiniteMaterials()) {
            ItemStack returnedChains = new ItemStack(Items.IRON_CHAIN, linkCount);
            if (!player.addItem(returnedChains) && player.level() instanceof ServerLevel serverLevel) {
                player.spawnAtLocation(serverLevel, returnedChains);
            }
            shears.hurtAndBreak(linkCount, player, hand);
        }
        player.sendSystemMessage(Component.translatable("message.stevescarts.chain.disconnected", linkCount));
        return InteractionResult.SUCCESS;
    }

    private static UUID getSelectedCart(ItemStack stack) {
        String id = ModItemData.getTagCopy(stack).getStringOr(SELECTED_CART, "");
        if (id.isEmpty()) {
            return null;
        }
        try {
            return UUID.fromString(id);
        } catch (IllegalArgumentException ignored) {
            clearSelection(stack);
            return null;
        }
    }

    private static void selectCart(ItemStack stack, ModularMinecart cart) {
        ModItemData.modifyTag(stack, tag -> tag.putString(SELECTED_CART, cart.getUUID().toString()));
    }

    private static void clearSelection(ItemStack stack) {
        CompoundTag tag = ModItemData.getTagCopy(stack);
        tag.remove(SELECTED_CART);
        if (tag.isEmpty()) {
            ModItemData.removeTag(stack);
        } else {
            ModItemData.setTag(stack, tag);
        }
    }
}
