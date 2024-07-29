package vswe.stevescarts.helpers;

import net.creeperhost.polylib.data.serializable.AbstractDataStore;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.IntArrayTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.RegistryFriendlyByteBuf;

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
    public Tag toTag(HolderLookup.Provider provider) {
        return new IntArrayTag(value);
    }

    @Override
    public void fromTag(HolderLookup.Provider provider, Tag tag) {
        value = validValue(((IntArrayTag) tag).getAsIntArray(), value);
    }
}
