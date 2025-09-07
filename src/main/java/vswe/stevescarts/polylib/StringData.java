package vswe.stevescarts.polylib;

import net.creeperhost.polylib.data.serializable.AbstractDataStore;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
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
    public void toTag(ValueOutput output) {
        output.putString("value", value);
    }

    @Override
    public void fromTag(ValueInput input) {
        value = validValue(input.getStringOr("value", ""), value);
    }
}
