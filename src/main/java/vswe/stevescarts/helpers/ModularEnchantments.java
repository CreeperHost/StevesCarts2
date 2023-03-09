package vswe.stevescarts.helpers;

import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.EnchantedBookItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraftforge.common.Tags;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ModularEnchantments {

    private static final Map<Enchantment, EnchantmentType> ENCHANTMENT_TYPES = new HashMap<>();
    private static final Map<Enchantment, Integer> ENCHANTMENT_BASE_VALUES = new HashMap<>();

    static {
        addValidEnchantment(Enchantments.BLOCK_FORTUNE, EnchantmentType.TOOL, 50000);
        addValidEnchantment(Enchantments.BLOCK_EFFICIENCY, EnchantmentType.TOOL, 50000);
        addValidEnchantment(Enchantments.UNBREAKING, EnchantmentType.TOOL, 64000);
        addValidEnchantment(Enchantments.POWER_ARROWS, EnchantmentType.SHOOTER, 750);
        addValidEnchantment(Enchantments.PUNCH_ARROWS, EnchantmentType.SHOOTER, 1000);
        addValidEnchantment(Enchantments.FLAMING_ARROWS, EnchantmentType.SHOOTER, 1000);
        addValidEnchantment(Enchantments.INFINITY_ARROWS, EnchantmentType.SHOOTER, 500);
    }

    public static void addValidEnchantment(Enchantment enchantment, EnchantmentType type, int baseValue) {
        ENCHANTMENT_TYPES.put(enchantment, type);
        ENCHANTMENT_BASE_VALUES.put(enchantment, baseValue);
    }

    public static int getValue(Enchantment enchant, int level) {
        return (int) Math.pow(2.0, level - 1) * ENCHANTMENT_BASE_VALUES.getOrDefault(enchant, 0);
    }

    public static int getMaxValue(Enchantment enchant) {
        int max = 0;
        for (int i = 0; i < enchant.getMaxLevel(); ++i) {
            max += getValue(enchant, i + 1);
        }
        return max;
    }

    public static boolean isValidBook(ItemStack stack, List<EnchantmentType> validTypes) {
        if (stack.isEmpty() || !stack.is(Items.ENCHANTED_BOOK)) return false;

        Map<Enchantment, Integer> enchants = EnchantmentHelper.getEnchantments(stack);
        for (Enchantment enchantment : enchants.keySet()) {
            if (!ENCHANTMENT_TYPES.containsKey(enchantment)) continue;

            for (EnchantmentType validType : validTypes) {
                if (ENCHANTMENT_TYPES.get(enchantment) == validType) {
                    return true;
                }
            }
        }
        return false;
    }

    public static EnchantmentData addBook(List<EnchantmentType> enabledTypes, EnchantmentData data, @NotNull ItemStack stack) {
        if (stack.isEmpty() || stack.getItem() != Items.ENCHANTED_BOOK) return data;
        Map<Enchantment, Integer> enchants = EnchantmentHelper.getEnchantments(stack);
        int addLevel = -1;

        if (data.getEnchant() == null) {
            for (Enchantment enchantment : enchants.keySet()) {
                if (ENCHANTMENT_TYPES.containsKey(enchantment) && enabledTypes.contains(ENCHANTMENT_TYPES.get(enchantment))) {
                    data.setEnchantment(enchantment);
                    data.setValue(0);
                    addLevel = enchants.get(enchantment);
                    break;
                }
            }
        } else if (enchants.containsKey(data.getEnchant())) {
            addLevel = enchants.get(data.getEnchant());
        }

        if (addLevel == -1) return data;
        int newValue = getValue(data.getEnchant(), addLevel) + data.getValue();
        if (newValue <= getMaxValue(data.getEnchant())) {
            data.setValue(newValue);
            stack.shrink(1);
        }
        return data;
    }

    public static EnchantmentType getType(Enchantment enchant) {
        return ENCHANTMENT_TYPES.get(enchant);
    }


//    private final Enchantment enchantment;
//    private final int rank1Value;
//    private final ENCHANTMENT_TYPE type;
//    public static ArrayList<EnchantmentInfo> enchants;
//    public static EnchantmentInfo fortune;
//    public static EnchantmentInfo efficiency;
//    public static EnchantmentInfo unbreaking;
//    public static EnchantmentInfo power;
//    public static EnchantmentInfo punch;
//    public static EnchantmentInfo flame;
//    public static EnchantmentInfo infinity;
//
//    public EnchantmentInfo(final Enchantment enchantment, final ENCHANTMENT_TYPE type, final int rank1Value)
//    {
//        this.enchantment = enchantment;
//        this.rank1Value = rank1Value;
//        this.type = type;
//        EnchantmentInfo.enchants.add(this);
//    }
//
//    public Enchantment getEnchantment()
//    {
//        return enchantment;
//    }
//
//    public int getMaxValue()
//    {
//        int max = 0;
//        for (int i = 0; i < getEnchantment().getMaxLevel(); ++i)
//        {
//            max += getValue(i + 1);
//        }
//        return max;
//    }
//
//    public int getValue(final int level)
//    {
//        return (int) Math.pow(2.0, level - 1) * rank1Value;
//    }
//
//    public static boolean isItemValid(final ArrayList<ENCHANTMENT_TYPE> enabledTypes, @Nonnull ItemStack itemstack) {
//        if (itemstack.isEmpty() || itemstack.getItem() != Items.ENCHANTED_BOOK) return false;
//
//        for (final EnchantmentInfo info : EnchantmentInfo.enchants) {
//            boolean isValid = false;
//            for (final ENCHANTMENT_TYPE type : enabledTypes) {
//                if (info.type == type) {
//                    isValid = true;
//                }
//                if (isValid) {
//                    final int level = getEnchantmentLevel(Enchantment.getEnchantmentID(info.getEnchantment()), itemstack);
//                    if (level > 0) {
//                        return true;
//                    }
//                    continue;
//                }
//            }
//        }
//        return false;
//    }
//
//    public static EnchantmentData addBook(final ArrayList<ENCHANTMENT_TYPE> enabledTypes, EnchantmentData data, @Nonnull ItemStack itemstack)
//    {
//        if (!itemstack.isEmpty() && itemstack.getItem() == Items.ENCHANTED_BOOK)
//        {
//            if (data == null)
//            {
//                for (final EnchantmentInfo info : EnchantmentInfo.enchants)
//                {
//                    data = addEnchantment(enabledTypes, data, itemstack, info);
//                }
//            }
//            else
//            {
//                addEnchantment(enabledTypes, data, itemstack, data.getEnchantment());
//            }
//        }
//        return data;
//    }
//
//    private static EnchantmentData addEnchantment(final ArrayList<ENCHANTMENT_TYPE> enabledTypes, EnchantmentData data, @Nonnull ItemStack itemstack, final EnchantmentInfo info)
//    {
//        boolean isValid = false;
//        for (final ENCHANTMENT_TYPE type : enabledTypes)
//        {
//            if (info.type == type)
//            {
//                isValid = true;
//            }
//        }
//        return data;
//    }
//
//    private static int getEnchantmentLevel(final int par0, @Nonnull ItemStack par1ItemStack)
//    {
//        if (par1ItemStack.isEmpty())
//        {
//            return 0;
//        }
//        final ListTag nbttaglist = EnchantedBookItem.getEnchantments(par1ItemStack);
//        if (nbttaglist == null)
//        {
//            return 0;
//        }
//        return 0;
//    }
//
//    public static EnchantmentData createDataFromEffectId(EnchantmentData data, final short id)
//    {
//        return data;
//    }
//
//    public ENCHANTMENT_TYPE getType()
//    {
//        return type;
//    }
//
//    static
//    {
//        EnchantmentInfo.enchants = new ArrayList<>();
//        EnchantmentInfo.fortune = new EnchantmentInfo(Enchantments.BLOCK_FORTUNE, ENCHANTMENT_TYPE.TOOL, 50000);
//        EnchantmentInfo.efficiency = new EnchantmentInfo(Enchantments.BLOCK_EFFICIENCY, ENCHANTMENT_TYPE.TOOL, 50000);
//        EnchantmentInfo.unbreaking = new EnchantmentInfo(Enchantments.UNBREAKING, ENCHANTMENT_TYPE.TOOL, 64000);
//        EnchantmentInfo.power = new EnchantmentInfo(Enchantments.POWER_ARROWS, ENCHANTMENT_TYPE.SHOOTER, 750);
//        EnchantmentInfo.punch = new EnchantmentInfo(Enchantments.PUNCH_ARROWS, ENCHANTMENT_TYPE.SHOOTER, 1000);
//        EnchantmentInfo.flame = new EnchantmentInfo(Enchantments.FLAMING_ARROWS, ENCHANTMENT_TYPE.SHOOTER, 1000);
//        EnchantmentInfo.infinity = new EnchantmentInfo(Enchantments.INFINITY_ARROWS, ENCHANTMENT_TYPE.SHOOTER, 500);
//    }

    public enum EnchantmentType {
        TOOL,
        SHOOTER
    }
}
