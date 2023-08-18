package vswe.stevescarts.modules.addons;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.registries.ForgeRegistries;
import vswe.stevescarts.api.modules.template.ModuleAddon;
import vswe.stevescarts.client.guis.GuiMinecart;
import vswe.stevescarts.api.slots.SlotStevesCarts;
import vswe.stevescarts.containers.slots.SlotEnchantment;
import vswe.stevescarts.entities.EntityMinecartModular;
import vswe.stevescarts.helpers.EnchantmentData;
import vswe.stevescarts.helpers.Localization;
import vswe.stevescarts.helpers.ModularEnchantments;
import vswe.stevescarts.helpers.ResourceHelper;
import vswe.stevescarts.network.DataSerializers;

import javax.annotation.Nonnull;
import java.util.ArrayList;

public class ModuleEnchants extends ModuleAddon {
    private final EntityDataAccessor<EnchantmentData> ENCHANT_0 = createDw(DataSerializers.ENCHANT_DATA);
    private final EntityDataAccessor<EnchantmentData> ENCHANT_1 = createDw(DataSerializers.ENCHANT_DATA);
    private final EntityDataAccessor<EnchantmentData> ENCHANT_2 = createDw(DataSerializers.ENCHANT_DATA);
    private final ArrayList<ModularEnchantments.EnchantmentType> enabledTypes;

    public ModuleEnchants(EntityMinecartModular cart) {
        super(cart);
        enabledTypes = new ArrayList<>();
    }

    @Override
    public void initDw() {
        registerDw(ENCHANT_0, new EnchantmentData(null));
        registerDw(ENCHANT_1, new EnchantmentData(null));
        registerDw(ENCHANT_2, new EnchantmentData(null));
    }

    @Override
    public int numberOfDataWatchers() {
        return 3;
    }

    public EnchantmentData getEnchant(int index) {
        return getDw(index == 0 ? ENCHANT_0 : index == 1 ? ENCHANT_1 : ENCHANT_2);
    }

    public void setEnchant(int index, EnchantmentData data) {
        updateDw(index == 0 ? ENCHANT_0 : index == 1 ? ENCHANT_1 : ENCHANT_2, data);
    }


    public int getFortuneLevel() {
        if (useSilkTouch()) {
            return 0;
        }
        return getEnchantLevel(Enchantments.BLOCK_FORTUNE);
    }

    public boolean useSilkTouch() {
        return false;
    }

    public int getUnbreakingLevel() {
        return getEnchantLevel(Enchantments.UNBREAKING);
    }

    public int getEfficiencyLevel() {
        return getEnchantLevel(Enchantments.BLOCK_EFFICIENCY);
    }

    public int getPowerLevel() {
        return getEnchantLevel(Enchantments.POWER_ARROWS);
    }

    public int getPunchLevel() {
        return getEnchantLevel(Enchantments.PUNCH_ARROWS);
    }

    public boolean useFlame() {
        return getEnchantLevel(Enchantments.FLAMING_ARROWS) > 0;
    }

    public boolean useInfinity() {
        return getEnchantLevel(Enchantments.INFINITY_ARROWS) > 0;
    }

    private int getEnchantLevel(Enchantment enchant) {
        for (int i = 0; i < 3; ++i) {
            EnchantmentData test = getEnchant(i);
            if (test.getEnchant() != null && test.getEnchant() == enchant) {
                return test.getLevel();
            }
        }
        return 0;
    }

    @Override
    public boolean hasGui() {
        return true;
    }

    @Override
    public void drawForeground(GuiGraphics guiGraphics, GuiMinecart gui) {
        drawString(guiGraphics, gui, getModuleName(), 8, 6, 4210752);
    }

    @Override
    protected int getInventoryWidth() {
        return 1;
    }

    @Override
    protected int getInventoryHeight() {
        return 3;
    }

    @Override
    protected SlotStevesCarts getSlot(int slotId, final int x, final int y) {
        return new SlotEnchantment(getCart(), enabledTypes, slotId, 8, 14 + y * 20);
    }

    @Override
    public void update() {
        super.update();
        if (getCart().level().isClientSide) return;

        for (int i = 0; i < 3; ++i) {
            if (!getStack(i).isEmpty() && getStack(i).getCount() > 0) {
                int count = getStack(i).getCount();
                EnchantmentData data = ModularEnchantments.addBook(enabledTypes, getEnchant(i), getStack(i));

                if (getStack(i).getCount() != count) {
                    boolean valid = true;
                    for (int j = 0; j < 3; ++j) {
                        if (i == j) continue;
                        EnchantmentData data2 = getEnchant(j);
                        if (data.getEnchant() != null && data2.getEnchant() != null && data.getEnchant() == data2.getEnchant()) {
                            data.setEnchantment(null);
                            @Nonnull ItemStack stack = getStack(i);
                            stack.grow(1);
                            valid = false;
                            break;
                        }
                    }
                    if (valid && getStack(i).getCount() <= 0) {
                        setStack(i, ItemStack.EMPTY);
                    }
                    setEnchant(i, data);
                }
            }
        }
    }

    public void damageEnchant(final ModularEnchantments.EnchantmentType type, int dmg) {
        for (int i = 0; i < 3; ++i) {
            EnchantmentData data = getEnchant(i);
            if (data.getEnchant() != null && ModularEnchantments.getType(data.getEnchant()) == type) {
                data.damageEnchant(dmg);
            }
        }
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void drawBackground(GuiGraphics guiGraphics, GuiMinecart gui, final int x, final int y) {
        ResourceHelper.bindResource("/gui/enchant.png");
        for (int i = 0; i < 3; ++i) {
            int[] box = getBoxRect(i);
            if (inRect(x, y, box)) {
                drawImage(guiGraphics, gui, box, 65, 0);
            } else {
                drawImage(guiGraphics, gui, box, 0, 0);
            }
            EnchantmentData data = getEnchant(i);
            if (data.getEnchant() != null) {
                int maxlevel = data.getEnchant().getMaxLevel();
                int value = data.getValue();
                for (int j = 0; j < maxlevel; ++j) {
                    int[] bar = getBarRect(i, j, maxlevel);
                    if (j != maxlevel - 1) {
                        drawImage(guiGraphics, gui, bar[0] + bar[2], bar[1], 61 + j, 1, 1, bar[3]);
                    }
                    int levelmaxvalue = ModularEnchantments.getValue(data.getEnchant(), j + 1);
                    if (value > 0) {
                        float mult = (float) value / (float) levelmaxvalue;
                        if (mult > 1.0f) {
                            mult = 1.0f;
                        }
                        bar[2] *= mult;
                        drawImage(guiGraphics, gui, bar, 1, 13 + 11 * j);
                    }
                    value -= levelmaxvalue;
                }
            }
        }
    }

    @Override
    public void drawMouseOver(GuiGraphics guiGraphics, GuiMinecart gui, final int x, final int y) {
        for (int i = 0; i < 3; ++i) {
            EnchantmentData data = getEnchant(i);
            String str;
            if (data.getEnchant() != null) {
                str = data.getInfoText();
            } else {
                str = Localization.MODULES.ADDONS.ENCHANT_INSTRUCTION.translate();
            }
            drawStringOnMouseOver(guiGraphics, gui, str, x, y, getBoxRect(i));
        }
    }

    private int[] getBoxRect(final int id) {
        return new int[]{40, 17 + id * 20, 61, 12};
    }

    private int[] getBarRect(final int id, final int barid, final int maxlevel) {
        final int width = (59 - (maxlevel - 1)) / maxlevel;
        return new int[]{41 + (width + 1) * barid, 18 + id * 20, width, 10};
    }

    @Override
    public int numberOfGuiData() {
        return 9;
    }

    @Override
    protected void Save(CompoundTag nbt, int id) {
        super.Save(nbt, id);
        for (int i = 0; i < 3; ++i) {
            EnchantmentData data = getEnchant(i);
            if (data.getEnchant() == null) continue;

            nbt.putString(generateNBTName("EffectId" + i, id), ForgeRegistries.ENCHANTMENTS.getKey(data.getEnchant()).toString());
            nbt.putInt(generateNBTName("Value" + i, id), data.getValue());
        }
    }

    @Override
    protected void Load(CompoundTag nbt, int id) {
        super.Load(nbt, id);
        for (int i = 0; i < 3; ++i) {
            if (!nbt.contains(generateNBTName("EffectId" + i, id))) continue;
            EnchantmentData data = new EnchantmentData(ForgeRegistries.ENCHANTMENTS.getValue(new ResourceLocation(nbt.getString(generateNBTName("EffectId" + i, id)))));
            data.setValue(nbt.getInt(generateNBTName("Value" + i, id)));
            setEnchant(i, data);
        }
    }

    @Override
    public int guiWidth() {
        return 110;
    }

    public void addType(final ModularEnchantments.EnchantmentType type) {
        enabledTypes.add(type);
    }
}
