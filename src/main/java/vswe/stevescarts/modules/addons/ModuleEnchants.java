package vswe.stevescarts.modules.addons;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;
import vswe.stevescarts.api.modules.template.ModuleAddon;
import vswe.stevescarts.api.slots.SlotStevesCarts;
import vswe.stevescarts.client.guis.GuiMinecart;
import vswe.stevescarts.containers.slots.SlotEnchantment;
import vswe.stevescarts.entities.ModularMinecart;
import vswe.stevescarts.helpers.*;
import vswe.stevescarts.polylib.EntityData;

import javax.annotation.Nonnull;
import java.util.ArrayList;

public class ModuleEnchants extends ModuleAddon {
    private final ArrayList<ModularEnchantments.EnchantmentType> enabledTypes;

    private final EntityData<EnchantmentData> enchant0 = new EntityData<>(getCart(), new EnchantData(new EnchantmentData(null)));
    private final EntityData<EnchantmentData> enchant1 = new EntityData<>(getCart(), new EnchantData(new EnchantmentData(null)));
    private final EntityData<EnchantmentData> enchant2 = new EntityData<>(getCart(), new EnchantData(new EnchantmentData(null)));

    public ModuleEnchants(ModularMinecart cart) {
        super(cart);
        enabledTypes = new ArrayList<>();
    }

    @NotNull
    public EnchantmentData getEnchant(int index) {
        return (index == 0 ? enchant0 : index == 1 ? enchant1 : enchant2).get();
    }

    public void setEnchant(int index, EnchantmentData data) {
        (index == 0 ? enchant0 : index == 1 ? enchant1 : enchant2).set(data, data.isDirty());
    }

    public int getFortuneLevel() {
        if (useSilkTouch()) {
            return 0;
        }
        return getEnchantLevel(Enchantments.FORTUNE);
    }

    public boolean useSilkTouch() {
        return false;
    }

    public int getUnbreakingLevel() {
        return getEnchantLevel(Enchantments.UNBREAKING);
    }

    public int getEfficiencyLevel() {
        return getEnchantLevel(Enchantments.EFFICIENCY);
    }

    public int getPowerLevel() {
        return getEnchantLevel(Enchantments.POWER);
    }

    public int getPunchLevel() {
        return getEnchantLevel(Enchantments.PUNCH);
    }

    public boolean useFlame() {
        return getEnchantLevel(Enchantments.FLAME) > 0;
    }

    public boolean useInfinity() {
        return getEnchantLevel(Enchantments.INFINITY) > 0;
    }

    private int getEnchantLevel(ResourceKey<Enchantment> enchant) {
        for (int i = 0; i < 3; ++i) {
            EnchantmentData test = getEnchant(i);
            if (test.getEnchantHolder() != null && test.getEnchantHolder().is(enchant)) {
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
        if (getCart().level().isClientSide()) return;

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
            if (data.getEnchantHolder() != null) {
                var optkey = data.getEnchantHolder().unwrapKey();
                if (optkey.isPresent() && ModularEnchantments.getType(optkey.get()) == type){
                    data.damageEnchant(dmg);
                }
            }
        }
    }

    @Override
    @OnlyIn (Dist.CLIENT)
    public void drawBackground(GuiGraphics guiGraphics, GuiMinecart gui, final int x, final int y) {
        Identifier texture = ResourceHelper.getResource("/gui/enchant.png");
        for (int i = 0; i < 3; ++i) {
            int[] box = getBoxRect(i);
            if (inRect(x, y, box)) {
                drawImage(guiGraphics, texture, gui, box, 65, 0);
            } else {
                drawImage(guiGraphics, texture, gui, box, 0, 0);
            }
            EnchantmentData data = getEnchant(i);
            if (data.getEnchantHolder() == null) continue;
            var optkey = data.getEnchantHolder().unwrapKey();
            if (!optkey.isPresent()) continue;

            int maxlevel = data.getEnchant().getMaxLevel();
            int value = data.getValue();
            for (int j = 0; j < maxlevel; ++j) {
                int[] bar = getBarRect(i, j, maxlevel);
                if (j != maxlevel - 1) {
                    drawImage(guiGraphics, texture, gui, bar[0] + bar[2], bar[1], 61 + j, 1, 1, bar[3]);
                }
                int levelmaxvalue = ModularEnchantments.getValue(optkey.get(), j + 1);
                if (value > 0) {
                    float mult = (float) value / (float) levelmaxvalue;
                    if (mult > 1.0f) {
                        mult = 1.0f;
                    }
                    bar[2] *= mult;
                    drawImage(guiGraphics, texture, gui, bar, 1, 13 + 11 * j);
                }
                value -= levelmaxvalue;
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
    protected void save(ValueOutput output, int id) {
        super.save(output, id);
        for (int i = 0; i < 3; ++i) {
            EnchantmentData data = getEnchant(i);
            if (data.getEnchant() == null) continue;
            data.save(output.child(generateNBTName("enchant" + i, id)));
        }
    }

    @Override
    protected void load(ValueInput input, int id) {
        super.load(input, id);
        for (int i = 0; i < 3; ++i) {
            int finalI = i;
            input.child(generateNBTName("enchant" + i, id)).ifPresent(e -> {
                EnchantmentData data = EnchantmentData.load(e);
                setEnchant(finalI, data);
            });
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
