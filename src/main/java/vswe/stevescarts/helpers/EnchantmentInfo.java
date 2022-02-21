package vswe.stevescarts.helpers;

import net.minecraft.nbt.ListTag;
import net.minecraft.world.item.EnchantedBookItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;

import javax.annotation.Nonnull;
import java.util.ArrayList;

public class EnchantmentInfo
{
    private Enchantment enchantment;
    private int rank1Value;
    private ENCHANTMENT_TYPE type;
    public static ArrayList<EnchantmentInfo> enchants;
    public static EnchantmentInfo fortune;
    public static EnchantmentInfo efficiency;
    public static EnchantmentInfo unbreaking;
    public static EnchantmentInfo power;
    public static EnchantmentInfo punch;
    public static EnchantmentInfo flame;
    public static EnchantmentInfo infinity;

    public EnchantmentInfo(final Enchantment enchantment, final ENCHANTMENT_TYPE type, final int rank1Value)
    {
        this.enchantment = enchantment;
        this.rank1Value = rank1Value;
        this.type = type;
        EnchantmentInfo.enchants.add(this);
    }

    public Enchantment getEnchantment()
    {
        return enchantment;
    }

    public int getMaxValue()
    {
        int max = 0;
        for (int i = 0; i < getEnchantment().getMaxLevel(); ++i)
        {
            max += getValue(i + 1);
        }
        return max;
    }

    public int getValue(final int level)
    {
        return (int) Math.pow(2.0, level - 1) * rank1Value;
    }

    public static boolean isItemValid(final ArrayList<ENCHANTMENT_TYPE> enabledTypes, @Nonnull ItemStack itemstack)
    {
        if (!itemstack.isEmpty() && itemstack.getItem() == Items.ENCHANTED_BOOK)
        {
            for (final EnchantmentInfo info : EnchantmentInfo.enchants)
            {
                boolean isValid = false;
                for (final ENCHANTMENT_TYPE type : enabledTypes)
                {
                    if (info.type == type)
                    {
                        isValid = true;
                    }
                }
            }
        }
        return false;
    }

    public static EnchantmentData addBook(final ArrayList<ENCHANTMENT_TYPE> enabledTypes, EnchantmentData data, @Nonnull ItemStack itemstack)
    {
        if (!itemstack.isEmpty() && itemstack.getItem() == Items.ENCHANTED_BOOK)
        {
            if (data == null)
            {
                for (final EnchantmentInfo info : EnchantmentInfo.enchants)
                {
                    data = addEnchantment(enabledTypes, data, itemstack, info);
                }
            }
            else
            {
                addEnchantment(enabledTypes, data, itemstack, data.getEnchantment());
            }
        }
        return data;
    }

    private static EnchantmentData addEnchantment(final ArrayList<ENCHANTMENT_TYPE> enabledTypes, EnchantmentData data, @Nonnull ItemStack itemstack, final EnchantmentInfo info)
    {
        boolean isValid = false;
        for (final ENCHANTMENT_TYPE type : enabledTypes)
        {
            if (info.type == type)
            {
                isValid = true;
            }
        }
        return data;
    }

    private static int getEnchantmentLevel(final int par0, @Nonnull ItemStack par1ItemStack)
    {
        if (par1ItemStack.isEmpty())
        {
            return 0;
        }
        final ListTag nbttaglist = EnchantedBookItem.getEnchantments(par1ItemStack);
        if (nbttaglist == null)
        {
            return 0;
        }
        return 0;
    }

    public static EnchantmentData createDataFromEffectId(EnchantmentData data, final short id)
    {
        return data;
    }

    public ENCHANTMENT_TYPE getType()
    {
        return type;
    }

    static
    {
        EnchantmentInfo.enchants = new ArrayList<>();
        EnchantmentInfo.fortune = new EnchantmentInfo(Enchantments.BLOCK_FORTUNE, ENCHANTMENT_TYPE.TOOL, 50000);
        EnchantmentInfo.efficiency = new EnchantmentInfo(Enchantments.BLOCK_EFFICIENCY, ENCHANTMENT_TYPE.TOOL, 50000);
        EnchantmentInfo.unbreaking = new EnchantmentInfo(Enchantments.UNBREAKING, ENCHANTMENT_TYPE.TOOL, 64000);
        EnchantmentInfo.power = new EnchantmentInfo(Enchantments.POWER_ARROWS, ENCHANTMENT_TYPE.SHOOTER, 750);
        EnchantmentInfo.punch = new EnchantmentInfo(Enchantments.PUNCH_ARROWS, ENCHANTMENT_TYPE.SHOOTER, 1000);
        EnchantmentInfo.flame = new EnchantmentInfo(Enchantments.FLAMING_ARROWS, ENCHANTMENT_TYPE.SHOOTER, 1000);
        EnchantmentInfo.infinity = new EnchantmentInfo(Enchantments.INFINITY_ARROWS, ENCHANTMENT_TYPE.SHOOTER, 500);
    }

    public enum ENCHANTMENT_TYPE
    {
        TOOL, SHOOTER
    }
}
