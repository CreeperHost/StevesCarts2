package vswe.stevescarts.polylib;

import net.creeperhost.polylib.data.serializable.AbstractDataStore;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.neoforged.neoforge.fluids.FluidStack;

import java.util.Objects;

/**
 * Created by brandon3055 on 15/11/2024
 */
public class FluidDataNeo extends AbstractDataStore<FluidStack> {

    public FluidDataNeo() {
        super(FluidStack.EMPTY);
    }

    public FluidDataNeo(FluidStack defaultValue) {
        super(defaultValue);
    }

    @Override
    public FluidStack set(FluidStack value) {
        if (!Objects.equals(value, this.value) && validator.test(value)) {
            this.value = value.copy();
            markDirty();
        }
        return this.value;
    }

    @Override
    public void toBytes(RegistryFriendlyByteBuf buf) {
        buf.writeBoolean(!value.isEmpty());
        if (!value.isEmpty()) {
            FluidStack.STREAM_CODEC.encode(buf, value);
        }
    }

    @Override
    public void fromBytes(RegistryFriendlyByteBuf buf) {
        if (buf.readBoolean()) {
            value = FluidStack.STREAM_CODEC.decode(buf);
        } else {
            value = FluidStack.EMPTY;
        }
    }

    @Override
    public Tag toTag(HolderLookup.Provider provider) {
        return value.saveOptional(provider);
    }

    @Override
    public void fromTag(HolderLookup.Provider provider, Tag tag) {
        value = validValue(FluidStack.parseOptional(provider, (CompoundTag) tag), value);
    }

    @Override
    public boolean isSameValue(FluidStack newValue) {
        return value.equals(newValue);
    }
}
