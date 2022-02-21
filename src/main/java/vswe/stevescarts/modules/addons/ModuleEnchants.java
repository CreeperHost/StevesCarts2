package vswe.stevescarts.modules.addons;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import vswe.stevescarts.client.guis.GuiMinecart;
import vswe.stevescarts.containers.slots.SlotBase;
import vswe.stevescarts.containers.slots.SlotEnchantment;
import vswe.stevescarts.entitys.EntityMinecartModular;
import vswe.stevescarts.helpers.EnchantmentData;
import vswe.stevescarts.helpers.EnchantmentInfo;
import vswe.stevescarts.helpers.Localization;
import vswe.stevescarts.helpers.ResourceHelper;

import javax.annotation.Nonnull;
import java.util.ArrayList;

public class ModuleEnchants extends ModuleAddon
{
    private EnchantmentData[] enchants;
    private ArrayList<EnchantmentInfo.ENCHANTMENT_TYPE> enabledTypes;

    public ModuleEnchants(final EntityMinecartModular cart)
    {
        super(cart);
        enchants = new EnchantmentData[3];
        enabledTypes = new ArrayList<>();
    }

    public int getFortuneLevel()
    {
        if (useSilkTouch())
        {
            return 0;
        }
        return getEnchantLevel(EnchantmentInfo.fortune);
    }

    public boolean useSilkTouch()
    {
        return false;
    }

    public int getUnbreakingLevel()
    {
        return getEnchantLevel(EnchantmentInfo.unbreaking);
    }

    public int getEfficiencyLevel()
    {
        return getEnchantLevel(EnchantmentInfo.efficiency);
    }

    public int getPowerLevel()
    {
        return getEnchantLevel(EnchantmentInfo.power);
    }

    public int getPunchLevel()
    {
        return getEnchantLevel(EnchantmentInfo.punch);
    }

    public boolean useFlame()
    {
        return getEnchantLevel(EnchantmentInfo.flame) > 0;
    }

    public boolean useInfinity()
    {
        return getEnchantLevel(EnchantmentInfo.infinity) > 0;
    }

    @Override
    public boolean hasGui()
    {
        return true;
    }

    @Override
    public void drawForeground(PoseStack matrixStack, GuiMinecart gui)
    {
        drawString(matrixStack, gui, getModuleName(), 8, 6, 4210752);
    }

    @Override
    protected int getInventoryWidth()
    {
        return 1;
    }

    @Override
    protected int getInventoryHeight()
    {
        return 3;
    }

    @Override
    protected SlotBase getSlot(final int slotId, final int x, final int y)
    {
        return new SlotEnchantment(getCart(), enabledTypes, slotId, 8, 14 + y * 20);
    }

    @Override
    public void update()
    {
        super.update();
        if (!getCart().level.isClientSide)
        {
            for (int i = 0; i < 3; ++i)
            {
                if (!getStack(i).isEmpty() && getStack(i).getCount() > 0)
                {
                    final int stacksize = getStack(i).getCount();
                    enchants[i] = EnchantmentInfo.addBook(enabledTypes, enchants[i], getStack(i));
                    if (getStack(i).getCount() != stacksize)
                    {
                        boolean valid = true;
                        for (int j = 0; j < 3; ++j)
                        {
                            if (i != j && enchants[i] != null && enchants[j] != null && enchants[i].getEnchantment() == enchants[j].getEnchantment())
                            {
                                enchants[i] = null;
                                @Nonnull ItemStack stack = getStack(i);
                                stack.grow(1);
                                valid = false;
                                break;
                            }
                        }
                        if (valid && getStack(i).getCount() <= 0)
                        {
                            setStack(i, ItemStack.EMPTY);
                        }
                    }
                }
            }
        }
    }

    public void damageEnchant(final EnchantmentInfo.ENCHANTMENT_TYPE type, final int dmg)
    {
        for (int i = 0; i < 3; ++i)
        {
            if (enchants[i] != null && enchants[i].getEnchantment().getType() == type)
            {
                enchants[i].damageEnchant(dmg);
                if (enchants[i].getValue() <= 0)
                {
                    enchants[i] = null;
                }
            }
        }
    }

    private int getEnchantLevel(final EnchantmentInfo info)
    {
        if (info != null)
        {
            for (int i = 0; i < 3; ++i)
            {
                if (enchants[i] != null && enchants[i].getEnchantment() == info)
                {
                    return enchants[i].getLevel();
                }
            }
        }
        return 0;
    }

    @Override
    public void drawBackground(PoseStack matrixStack, GuiMinecart gui, final int x, final int y)
    {
        ResourceHelper.bindResource("/gui/enchant.png");
        for (int i = 0; i < 3; ++i)
        {
            final int[] box = getBoxRect(i);
            if (inRect(x, y, box))
            {
                drawImage(matrixStack, gui, box, 65, 0);
            }
            else
            {
                drawImage(matrixStack, gui, box, 0, 0);
            }
            final EnchantmentData data = enchants[i];
            if (data != null)
            {
                final int maxlevel = data.getEnchantment().getEnchantment().getMaxLevel();
                int value = data.getValue();
                for (int j = 0; j < maxlevel; ++j)
                {
                    final int[] bar = getBarRect(i, j, maxlevel);
                    if (j != maxlevel - 1)
                    {
                        drawImage(matrixStack, gui, bar[0] + bar[2], bar[1], 61 + j, 1, 1, bar[3]);
                    }
                    int levelmaxvalue = data.getEnchantment().getValue(j + 1);
                    if (value > 0)
                    {
                        float mult = (float) value / (float) levelmaxvalue;
                        if (mult > 1.0f)
                        {
                            mult = 1.0f;
                        }
                        bar[2] *= mult;
                        drawImage(matrixStack, gui, bar, 1, 13 + 11 * j);
                    }
                    value -= levelmaxvalue;
                }
            }
        }
    }

    @Override
    public void drawMouseOver(PoseStack matrixStack, GuiMinecart gui, final int x, final int y)
    {
        for (int i = 0; i < 3; ++i)
        {
            final EnchantmentData data = enchants[i];
            String str;
            if (data != null)
            {
                str = data.getInfoText();
            }
            else
            {
                str = Localization.MODULES.ADDONS.ENCHANT_INSTRUCTION.translate();
            }
            drawStringOnMouseOver(matrixStack, gui, str, x, y, getBoxRect(i));
        }
    }

    private int[] getBoxRect(final int id)
    {
        return new int[]{40, 17 + id * 20, 61, 12};
    }

    private int[] getBarRect(final int id, final int barid, final int maxlevel)
    {
        final int width = (59 - (maxlevel - 1)) / maxlevel;
        return new int[]{41 + (width + 1) * barid, 18 + id * 20, width, 10};
    }

    @Override
    public int numberOfGuiData()
    {
        return 9;
    }

    @Override
    protected void checkGuiData(final Object[] info)
    {
        for (int i = 0; i < 3; ++i)
        {
            final EnchantmentData data = enchants[i];
            if (data == null)
            {
                updateGuiData(info, i * 3 + 0, (short) (-1));
            }
            else
            {
                updateGuiData(info, i * 3 + 1, (short) (data.getValue() & 0xFFFF));
                updateGuiData(info, i * 3 + 2, (short) (data.getValue() >> 16 & 0xFFFF));
            }
        }
    }

    @Override
    public void receiveGuiData(int id, final short data)
    {
        int dataint = data;
        if (dataint < 0)
        {
            dataint += 65536;
        }
        final int enchantId = id / 3;
        id %= 3;
        if (id == 0)
        {
            if (data == -1)
            {
                enchants[enchantId] = null;
            }
            else
            {
                enchants[enchantId] = EnchantmentInfo.createDataFromEffectId(enchants[enchantId], data);
            }
        }
        else if (enchants[enchantId] != null)
        {
            if (id == 1)
            {
                enchants[enchantId].setValue((enchants[enchantId].getValue() & 0xFFFF0000) | dataint);
            }
            else if (id == 2)
            {
                enchants[enchantId].setValue((enchants[enchantId].getValue() & 0xFFFF) | dataint << 16);
            }
        }
    }

    @Override
    protected void Save(final CompoundTag tagCompound, final int id)
    {
        super.Save(tagCompound, id);
        for (int i = 0; i < 3; ++i)
        {
            if (enchants[i] == null)
            {
                tagCompound.putShort(generateNBTName("EffectId" + i, id), (short) (-1));
            }
            else
            {
                tagCompound.putInt(generateNBTName("Value" + i, id), enchants[i].getValue());
            }
        }
    }

    @Override
    protected void Load(final CompoundTag tagCompound, final int id)
    {
        super.Load(tagCompound, id);
        for (int i = 0; i < 3; ++i)
        {
            final short effect = tagCompound.getShort(generateNBTName("EffectId" + i, id));
            if (effect == -1)
            {
                enchants[i] = null;
            }
            else
            {
                enchants[i] = EnchantmentInfo.createDataFromEffectId(enchants[i], effect);
                if (enchants[i] != null)
                {
                    enchants[i].setValue(tagCompound.getInt(generateNBTName("Value" + i, id)));
                }
            }
        }
    }

    @Override
    public int guiWidth()
    {
        return 110;
    }

    public void addType(final EnchantmentInfo.ENCHANTMENT_TYPE type)
    {
        enabledTypes.add(type);
    }
}
