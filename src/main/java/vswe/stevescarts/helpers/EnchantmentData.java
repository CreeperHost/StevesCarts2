package vswe.stevescarts.helpers;

import net.minecraft.ChatFormatting;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class EnchantmentData {
    @Nullable
    private Enchantment enchant;
    private int value;

    public EnchantmentData(Enchantment enchant) {
        this.enchant = enchant;
        value = 0;
    }

    public int getValue() {
        return value;
    }

    public void setValue(final int val) {
        value = val;
    }

    public void setEnchantment(Enchantment enchant) {
        this.enchant = enchant;
    }

    public Enchantment getEnchant() {
        return enchant;
    }

    public void damageEnchant(int dmg) {
        damageEnchantLevel(dmg, getValue(), 1);
        if (getValue() <= 0) setEnchantment(null);
    }

    private boolean damageEnchantLevel(int dmg, int value, int level) {
        if (enchant == null) return false;
        if (level > enchant.getMaxLevel() || value <= 0) {
            return false;
        }
        int levelvalue = ModularEnchantments.getValue(enchant, level);
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
        int value = getValue();
        for (int i = 0; i < enchant.getMaxLevel(); ++i) {
            if (value <= 0) {
                return i;
            }
            value -= ModularEnchantments.getValue(enchant, i + 1);
        }
        return enchant.getMaxLevel();
    }

    public String getInfoText() {
        if (enchant == null) return "";
        int value = getValue();
        int level = 0;
        int percentage = 0;
        for (level = 1; level <= enchant.getMaxLevel(); ++level) {
            if (value > 0) {
                final int levelvalue = ModularEnchantments.getValue(enchant, level);
                percentage = 100 * value / levelvalue;
                value -= levelvalue;
                if (value < 0) {
                    break;
                }
            }
        }
        return ChatFormatting.YELLOW + enchant.getFullname(getLevel()).getString() + "\n" + percentage + "% left of this tier";
    }

    public void write(FriendlyByteBuf buf) {
        buf.writeVarInt(enchant == null ? -1 : value);
        if (enchant != null){
            buf.writeResourceLocation(Objects.requireNonNull(ForgeRegistries.ENCHANTMENTS.getKey(enchant)));
        }
    }

    public static EnchantmentData read(FriendlyByteBuf buf) {
        int value = buf.readVarInt();
        EnchantmentData data = new EnchantmentData(value == -1 ? null : ForgeRegistries.ENCHANTMENTS.getValue(buf.readResourceLocation()));
        data.setValue(value);
        return data;
    }
}
