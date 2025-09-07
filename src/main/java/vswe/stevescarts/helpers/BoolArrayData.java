package vswe.stevescarts.helpers;

import com.mojang.serialization.Codec;
import net.creeperhost.polylib.data.serializable.AbstractDataStore;
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
public class BoolArrayData extends AbstractDataStore<ModSerializers.BoolArray> {

    public BoolArrayData(ModSerializers.BoolArray defaultValue) {
        super(defaultValue);
    }

    @Override
    public void toBytes(RegistryFriendlyByteBuf buf) {
        value.write(buf);
    }

    @Override
    public void fromBytes(RegistryFriendlyByteBuf buf) {
        value = ModSerializers.BoolArray.read(buf);
    }

    @Override
    public void toTag(ValueOutput output) {
        output.store("value", Codec.BYTE.listOf(), List.of(ArrayUtils.toObject(value.getBytes())));
    }

    @Override
    public void fromTag(ValueInput input) {
        value = ModSerializers.BoolArray.fromBytes(input.read("value", Codec.BYTE.listOf()).orElse(Collections.emptyList()).toArray(new Byte[0]));
    }
}
