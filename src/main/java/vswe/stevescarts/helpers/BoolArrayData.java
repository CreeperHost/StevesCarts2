package vswe.stevescarts.helpers;

import net.creeperhost.polylib.data.serializable.AbstractDataStore;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.ByteArrayTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import vswe.stevescarts.init.ModSerializers;

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
    public Tag toTag(HolderLookup.Provider provider) {
        return new ByteArrayTag(value.getBytes());
    }

    @Override
    public void fromTag(HolderLookup.Provider provider, Tag tag) {
        value = ModSerializers.BoolArray.fromBytes(((ByteArrayTag) tag).getAsByteArray());
    }
}
