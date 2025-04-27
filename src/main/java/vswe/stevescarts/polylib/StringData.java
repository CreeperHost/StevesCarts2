package vswe.stevescarts.polylib;

import net.creeperhost.polylib.data.serializable.AbstractDataStore;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import org.jetbrains.annotations.NotNull;

/**
 * Created by brandon3055 on 09/09/2023
 */
public class StringData extends AbstractDataStore<String> {

    public StringData() {
        super("");
    }

    public StringData(@NotNull String defaultValue) {
        super(defaultValue);
    }

    @Override
    public void toBytes(RegistryFriendlyByteBuf buf) {
        buf.writeUtf(value);
    }

    @Override
    public void fromBytes(RegistryFriendlyByteBuf buf) {
        value = validValue(buf.readUtf(), value);
    }

    @Override
    public Tag toTag(HolderLookup.Provider provider) {
        return StringTag.valueOf(value);
    }

    @Override
    public void fromTag(HolderLookup.Provider provider, Tag tag) {
        value = validValue(tag.asString().orElse(""), value);
    }
}
