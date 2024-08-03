package vswe.stevescarts.helpers;

import net.minecraft.ChatFormatting;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class EnchantmentData {
    @Nullable
    private Holder<Enchantment> enchant;
    private int value;
    private boolean dirty = false;

    public EnchantmentData(Holder<Enchantment> enchant) {
        this.enchant = enchant;
        value = 0;
    }

    public int getValue() {
        return value;
    }

    public void setValue(final int val) {
        value = val;
        dirty = true;
    }

    public void setEnchantment(Holder<Enchantment> enchant) {
        this.enchant = enchant;
        dirty = true;
    }

    @Nullable
    public Enchantment getEnchant() {
        return enchant == null ? null : enchant.value();
    }

    public Holder<Enchantment> getEnchantHolder() {
        return enchant;
    }

    public void damageEnchant(int dmg) {
        damageEnchantLevel(dmg, getValue(), 1);
        if (getValue() <= 0) setEnchantment(null);
    }

    private boolean damageEnchantLevel(int dmg, int value, int level) {
        if (enchant == null) return false;
        if (level > enchant.value().getMaxLevel() || value <= 0) {
            return false;
        }
        var optkey = enchant.unwrapKey();
        if (!optkey.isPresent()) return false;
        int levelvalue = ModularEnchantments.getValue(optkey.get(), level);
        if (!damageEnchantLevel(dmg, value - levelvalue, level + 1)) {
            int dmgdealt = dmg * (int) Math.pow(2.0, level - 1);
            if (dmgdealt > value) {
                dmgdealt = value;
            }
            setValue(getValue() - dmgdealt);
        }
        return true;
    }

    //Effective enchantment level
    public int getLevel() {
        if (enchant == null) return 0;
        var optkey = enchant.unwrapKey();
        if (!optkey.isPresent()) return 0;
        int value = getValue();
        for (int i = 0; i < enchant.value().getMaxLevel(); ++i) {
            if (value <= 0) {
                return i;
            }
            value -= ModularEnchantments.getValue(optkey.get(), i + 1);
        }
        return enchant.value().getMaxLevel();
    }

    public String getInfoText() {
        if (enchant == null) return "";
        var optkey = enchant.unwrapKey();
        if (!optkey.isPresent()) return "";
        int value = getValue();
        int level = 0;
        int percentage = 0;
        for (level = 1; level <= enchant.value().getMaxLevel(); ++level) {
            if (value > 0) {
                final int levelvalue = ModularEnchantments.getValue(optkey.get(), level);
                percentage = 100 * value / levelvalue;
                value -= levelvalue;
                if (value < 0) {
                    break;
                }
            }
        }
        return ChatFormatting.YELLOW + Enchantment.getFullname(enchant, getLevel()).getString() + "\n" + percentage + "% left of this tier";
    }

    public void write(RegistryFriendlyByteBuf buf) {
        buf.writeVarInt(enchant == null ? -1 : value);
        if (enchant != null){
            buf.writeResourceLocation(Objects.requireNonNull(ResourceLocation.parse(enchant.getRegisteredName())));
        }
    }

    public static EnchantmentData read(RegistryFriendlyByteBuf buf) {
        int value = buf.readVarInt();
        if (value == -1) return new EnchantmentData(null);
        ResourceKey<Enchantment> resKey = ResourceKey.create(Registries.ENCHANTMENT, buf.readResourceLocation());
        Holder<Enchantment> enchantment = buf.registryAccess().lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(resKey);
        EnchantmentData data = new EnchantmentData(enchantment);
        data.setValue(value);
        return data;
    }

    public CompoundTag save(HolderLookup.Provider provider) {
        CompoundTag tag = new CompoundTag();
        if (enchant == null) return tag;
        tag.putString("key", enchant.getRegisteredName());
        tag.putInt("value", getLevel());
        return tag;
    }

    @Nullable
    public static EnchantmentData load(CompoundTag tag, HolderLookup.Provider provider) {
        if (!tag.contains("key")) return null;
        ResourceLocation key = ResourceLocation.parse(tag.getString("key"));
        ResourceKey<Enchantment> resKey = ResourceKey.create(Registries.ENCHANTMENT, key);
        Holder<Enchantment> enchant = provider.lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(resKey);
        EnchantmentData data = new EnchantmentData(enchant);
        data.setValue(tag.getInt("value"));
        return data;
    }

    public boolean isDirty() {
        if (dirty) {
            dirty = false;
            return true;
        }
        return false;
    }
}
