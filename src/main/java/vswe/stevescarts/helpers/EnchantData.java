package vswe.stevescarts.helpers;

import net.creeperhost.polylib.data.serializable.AbstractDataStore;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.RegistryFriendlyByteBuf;

/**
 * Created by brandon3055 on 09/09/2023
 */
public class EnchantData extends AbstractDataStore<EnchantmentData> {

    public EnchantData(EnchantmentData defaultValue) {
        super(defaultValue);
    }

    @Override
    public void toBytes(RegistryFriendlyByteBuf buf) {
        value.write(buf);
    }

    @Override
    public void fromBytes(RegistryFriendlyByteBuf buf) {
        value = EnchantmentData.read(buf);
    }

    @Override
    public Tag toTag(HolderLookup.Provider provider) {
        return value.save(provider);
    }

    @Override
    public void fromTag(HolderLookup.Provider provider, Tag tag) {
        value = validValue(EnchantmentData.load((CompoundTag) tag, provider), value);
    }
}
