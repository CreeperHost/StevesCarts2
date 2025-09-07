package vswe.stevescarts.helpers;

import net.creeperhost.polylib.data.serializable.AbstractDataStore;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

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
    public void toTag(ValueOutput output) {
        value.save(output);
    }

    @Override
    public void fromTag(ValueInput input) {
        value = validValue(EnchantmentData.load(input), value);
    }
}
