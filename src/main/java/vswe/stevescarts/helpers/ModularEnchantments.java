package vswe.stevescarts.helpers;

import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ModularEnchantments {

    private static final Map<ResourceKey<Enchantment>, EnchantmentType> ENCHANTMENT_TYPES = new HashMap<>();
    private static final Map<ResourceKey<Enchantment>, Integer> ENCHANTMENT_BASE_VALUES = new HashMap<>();

    static {
        addValidEnchantment(Enchantments.FORTUNE, EnchantmentType.TOOL, 50000);
        addValidEnchantment(Enchantments.EFFICIENCY, EnchantmentType.TOOL, 50000);
        addValidEnchantment(Enchantments.UNBREAKING, EnchantmentType.TOOL, 64000);
        addValidEnchantment(Enchantments.POWER, EnchantmentType.SHOOTER, 750);
        addValidEnchantment(Enchantments.PUNCH, EnchantmentType.SHOOTER, 1000);
        addValidEnchantment(Enchantments.FLAME, EnchantmentType.SHOOTER, 1000);
        addValidEnchantment(Enchantments.INFINITY, EnchantmentType.SHOOTER, 500);
    }

    public static void addValidEnchantment(ResourceKey<Enchantment> enchantment, EnchantmentType type, int baseValue) {
        ENCHANTMENT_TYPES.put(enchantment, type);
        ENCHANTMENT_BASE_VALUES.put(enchantment, baseValue);
    }

    public static int getValue(ResourceKey<Enchantment> enchant, int level) {
        return (int) Math.pow(2.0, level - 1) * ENCHANTMENT_BASE_VALUES.getOrDefault(enchant, 0);
    }

    public static int getMaxValue(Enchantment enchant, ResourceKey<Enchantment> key) {
        if (enchant == null) return 0;
        int max = 0;
        for (int i = 0; i < enchant.getMaxLevel(); ++i) {
            max += getValue(key, i + 1);
        }
        return max;
    }

    public static boolean isValidBook(ItemStack stack, List<EnchantmentType> validTypes) {
        if (stack.isEmpty() || !stack.is(Items.ENCHANTED_BOOK)) return false;

        ItemEnchantments enchants = EnchantmentHelper.getEnchantmentsForCrafting(stack);
        for (Holder<Enchantment> enchantment : enchants.keySet()) {
            ResourceKey<Enchantment> key = enchantment.unwrapKey().orElse(null);
            if (key == null || !ENCHANTMENT_TYPES.containsKey(key)) continue;

            for (EnchantmentType validType : validTypes) {
                if (ENCHANTMENT_TYPES.get(key) == validType) {
                    return true;
                }
            }
        }
        return false;
    }

    public static EnchantmentData addBook(List<EnchantmentType> enabledTypes, EnchantmentData data, @NotNull ItemStack stack) {
        if (stack.isEmpty() || stack.getItem() != Items.ENCHANTED_BOOK) return data;
        ItemEnchantments enchants = EnchantmentHelper.getEnchantmentsForCrafting(stack);
        int addLevel = -1;

        if (data.getEnchantHolder() == null) {
            for (Holder<Enchantment> enchantment : enchants.keySet()) {
                ResourceKey<Enchantment> key = enchantment.unwrapKey().orElse(null);
                if (key != null && ENCHANTMENT_TYPES.containsKey(key) && enabledTypes.contains(ENCHANTMENT_TYPES.get(key))) {
                    data.setEnchantment(enchantment);
                    data.setValue(0);
                    addLevel = enchants.getLevel(enchantment);
                    break;
                }
            }
        } else if (enchants.keySet().contains(data.getEnchantHolder())) {
            addLevel = enchants.getLevel(data.getEnchantHolder());
        }

        if (addLevel == -1) return data;
        var opKey = data.getEnchantHolder().unwrapKey();
        if (opKey.isEmpty()) return data;
        int newValue = getValue(opKey.get(), addLevel) + data.getValue();
        if (newValue <= getMaxValue(data.getEnchant(), opKey.get())) {
            data.setValue(newValue);
            stack.shrink(1);
        }
        return data;
    }

    public static EnchantmentType getType(ResourceKey<Enchantment> enchant) {
        return ENCHANTMENT_TYPES.get(enchant);
    }

    public enum EnchantmentType {
        TOOL,
        SHOOTER
    }
}
