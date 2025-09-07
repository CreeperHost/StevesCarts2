package vswe.stevescarts.helpers;

import net.creeperhost.polylib.data.serializable.AbstractDataStore;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

/**
 * Created by brandon3055 on 09/09/2023
 */
public class IntArrayData extends AbstractDataStore<int[]> {

    public IntArrayData(int[] defaultValue) {
        super(defaultValue);
    }

    @Override
    public void toBytes(RegistryFriendlyByteBuf buf) {
        buf.writeVarIntArray(value);
    }

    @Override
    public void fromBytes(RegistryFriendlyByteBuf buf) {
        value = validValue(buf.readVarIntArray(), value);
    }

    @Override
    public void toTag(ValueOutput output) {
        output.putIntArray("value", value);
    }

    @Override
    public void fromTag(ValueInput input) {
        value = input.getIntArray("value").orElse(value);
    }
}
