package vswe.stevescarts.helpers;

import net.creeperhost.polylib.data.serializable.AbstractDataStore;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.RegistryFriendlyByteBuf;

/**
 * Created by brandon3055 on 09/09/2023
 */
public class EnchantData extends AbstractDataStore<EnchantmentData> {

    public EnchantData() {
        super(null);
    }

    public EnchantData(EnchantmentData defaultValue) {
        super(defaultValue);
    }

    @Override
    public void toBytes(RegistryFriendlyByteBuf buf) {
        buf.writeBoolean(value != null);
        if (value != null) {
            value.write(buf);
        }
    }

    @Override
    public void fromBytes(RegistryFriendlyByteBuf buf) {
        if (buf.readBoolean()) {
            value = EnchantmentData.read(buf);
        } else {
            value = null;
        }
    }

    @Override
    public Tag toTag(HolderLookup.Provider provider) {
        if (value != null) {
            return value.save(provider);
        }
        return new CompoundTag();
    }

    @Override
    public void fromTag(HolderLookup.Provider provider, Tag tag) {
        if (tag instanceof CompoundTag compound && !compound.isEmpty()) {
            value = validValue(EnchantmentData.load(compound, provider), value);
        } else {
            value = null;
        }
    }
}
