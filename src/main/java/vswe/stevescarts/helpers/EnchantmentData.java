package vswe.stevescarts.helpers;

import net.minecraft.ChatFormatting;

public class EnchantmentData
{
    private EnchantmentInfo type;
    private int value;

    public EnchantmentData(final EnchantmentInfo type)
    {
        this.type = type;
        value = 0;
    }

    public int getValue()
    {
        return value;
    }

    public void setValue(final int val)
    {
        value = val;
    }

    public EnchantmentInfo getEnchantment()
    {
        return type;
    }

    public void setEnchantment(final EnchantmentInfo info)
    {
        type = info;
    }

    public void damageEnchant(final int dmg)
    {
        damageEnchantLevel(dmg, getValue(), 1);
    }

    private boolean damageEnchantLevel(final int dmg, final int value, final int level)
    {
        if (level > type.getEnchantment().getMaxLevel() || value <= 0)
        {
            return false;
        }
        final int levelvalue = getEnchantment().getValue(level);
        if (!damageEnchantLevel(dmg, value - levelvalue, level + 1))
        {
            int dmgdealt = dmg * (int) Math.pow(2.0, level - 1);
            if (dmgdealt > value)
            {
                dmgdealt = value;
            }
            setValue(getValue() - dmgdealt);
        }
        return true;
    }

    public int getLevel()
    {
        int value = getValue();
        for (int i = 0; i < type.getEnchantment().getMaxLevel(); ++i)
        {
            if (value <= 0)
            {
                return i;
            }
            value -= getEnchantment().getValue(i + 1);
        }
        return type.getEnchantment().getMaxLevel();
    }

    public String getInfoText()
    {
        int value = getValue();
        int level = 0;
        int percentage = 0;
        for (level = 1; level <= type.getEnchantment().getMaxLevel(); ++level)
        {
            if (value > 0)
            {
                final int levelvalue = getEnchantment().getValue(level);
                percentage = 100 * value / levelvalue;
                value -= levelvalue;
                if (value < 0)
                {
                    break;
                }
            }
        }
        return ChatFormatting.YELLOW + "TODO";//TODO //getEnchantment().getEnchantment().getTranslatedName(getLevel()) + "\n" + percentage + "% left of this tier";
    }
}
