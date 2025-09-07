package vswe.stevescarts.helpers;

import com.mojang.serialization.Codec;
import net.creeperhost.polylib.data.serializable.AbstractDataStore;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.ShortTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.apache.commons.lang3.ArrayUtils;
import vswe.stevescarts.init.ModSerializers;

import java.util.Collections;
import java.util.List;

/**
 * Created by brandon3055 on 09/09/2023
 */
public class ShortArrayData extends AbstractDataStore<ModSerializers.ShortArray> {

    public ShortArrayData(ModSerializers.ShortArray defaultValue) {
        super(defaultValue);
    }

    @Override
    public void toBytes(RegistryFriendlyByteBuf buf) {
        value.write(buf);
    }

    @Override
    public void fromBytes(RegistryFriendlyByteBuf buf) {
        value = ModSerializers.ShortArray.read(buf);
    }

    @Override
    public void toTag(ValueOutput output) {
        output.store("value", Codec.SHORT.listOf(), List.of(ArrayUtils.toObject(value.getArray())));
    }

    @Override
    public void fromTag(ValueInput input) {
        value = new ModSerializers.ShortArray(input.read("value", Codec.SHORT.listOf()).orElse(Collections.emptyList()).toArray(new Short[0]));
    }

//    @Override
//    public Tag toTag(HolderLookup.Provider provider) {
//        ListTag list = new ListTag();
//        for (short s : value.getArray()) {
//            list.add(ShortTag.valueOf(s));
//        }
//        return list;
//    }
//
//    @Override
//    public void fromTag(HolderLookup.Provider provider, Tag tag) {
//        ListTag list = (ListTag) tag;
//        short[] shorts = new short[list.size()];
//        for (int i = 0; i < list.size(); i++) {
//            shorts[i] = list.getShortOr(i, (short) 0);
//        }
//        value = new ModSerializers.ShortArray(shorts);
//    }
}
