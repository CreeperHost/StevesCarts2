package vswe.stevescarts.init;

import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.neoforged.bus.api.IEventBus;
import vswe.stevescarts.Constants;

import java.util.function.Consumer;

/**
 * Created by brandon3055 on 22/07/2024
 */
public class ModItemData {

    public static final DeferredRegister<DataComponentType<?>> DATA = DeferredRegister.create(Registries.DATA_COMPONENT_TYPE, Constants.MOD_ID);
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<CustomData>> ITEM_TAG = DATA.register("item_tag", () -> DataComponentType.<CustomData>builder().persistent(CustomData.CODEC).networkSynchronized(CustomData.STREAM_CODEC).build());

    public static void init(IEventBus modBus) {
        DATA.register(modBus);
    }

    public static boolean hasTag(ItemStack stack) {
        return stack.has(ITEM_TAG);
    }

    /**
     * Please not this is called getTag'Copy' for a reason!
     * Modifying this does not update the tag on the item!
     * You need to use setTag if you make changes.
     */
    public static CompoundTag getTagCopy(ItemStack stack) {
        return stack.getOrDefault(ITEM_TAG, CustomData.of(new CompoundTag())).copyTag();
    }

    public static void setTag(ItemStack stack, CompoundTag tag) {
        CustomData.set(ITEM_TAG.get(), stack, tag);
    }

    public static void removeTag(ItemStack stack) {
        stack.remove(ITEM_TAG);
    }

    public static void modifyTag(ItemStack stack, Consumer<CompoundTag> modifyCallback) {
        CompoundTag tag = getTagCopy(stack);
        modifyCallback.accept(tag);
        setTag(stack, tag);
    }

}
