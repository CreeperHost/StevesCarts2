package vswe.stevescarts.modules.workers.tools;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import vswe.stevescarts.client.guis.GuiMinecart;
import vswe.stevescarts.containers.slots.SlotBase;
import vswe.stevescarts.containers.slots.SlotRepair;
import vswe.stevescarts.entities.EntityMinecartModular;
import vswe.stevescarts.helpers.ModularEnchantments;
import vswe.stevescarts.helpers.Localization;
import vswe.stevescarts.helpers.ResourceHelper;
import vswe.stevescarts.api.modules.ModuleBase;
import vswe.stevescarts.modules.addons.ModuleEnchants;
import vswe.stevescarts.modules.workers.ModuleWorker;

import javax.annotation.Nonnull;

public abstract class ModuleTool extends ModuleWorker
{
    private int initialDurability = -1;
    private EntityDataAccessor<Integer> DURABILITY;
    private int remainingRepairUnits;
    private int maximumRepairUnits;
    protected ModuleEnchants enchanter;
    private final int[] durabilityRect;

    public ModuleTool(final EntityMinecartModular cart)
    {
        super(cart);
        maximumRepairUnits = 1;
        durabilityRect = new int[]{10, 15, 52, 8};
    }

    public abstract int getMaxDurability();

    public abstract String getRepairItemName();

    public abstract int getRepairItemUnits(@Nonnull ItemStack p0);

    public abstract int getRepairSpeed();

    public abstract boolean useDurability();

    @Override
    public void init()
    {
        super.init();
        for (final ModuleBase module : getCart().getModules())
        {
            if (module instanceof ModuleEnchants)
            {
                (enchanter = (ModuleEnchants) module).addType(ModularEnchantments.EnchantmentType.TOOL);
                break;
            }
        }
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void drawBackground(GuiGraphics guiGraphics, GuiMinecart gui, final int x, final int y)
    {
        ResourceHelper.bindResource("/gui/tool.png");
        drawBox(guiGraphics, gui, 0, 0, 1.0f);
        drawBox(guiGraphics, gui, 0, 8, useDurability() ? (((float) getCurrentDurability()) / ((float) getMaxDurability())) : 1.0f);
        drawBox(guiGraphics, gui, 0, 16, ((float) remainingRepairUnits) / ((float) maximumRepairUnits));
        if (inRect(x, y, durabilityRect))
        {
            drawBox(guiGraphics, gui, 0, 24, 1.0f);
        }
    }

    private void drawBox(GuiGraphics guiGraphics, GuiMinecart gui, final int u, final int v, final float mult)
    {
        final int w = (int) (durabilityRect[2] * mult);
        if (w > 0)
        {
            drawImage(guiGraphics, gui, durabilityRect[0], durabilityRect[1], u, v, w, durabilityRect[3]);
        }
    }

    public boolean isValidRepairMaterial(@Nonnull ItemStack item)
    {
        return getRepairItemUnits(item) > 0;
    }

    @Override
    public boolean hasGui()
    {
        return true;
    }

    @Override
    protected SlotBase getSlot(final int slotId, final int x, final int y)
    {
        return new SlotRepair(this, getCart(), slotId, 76, 8);
    }

    @Override
    protected int getInventoryWidth()
    {
        return 1;
    }

    @Override
    public int guiWidth()
    {
        return 100;
    }

    @Override
    public int guiHeight()
    {
        return 50;
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void drawMouseOver(GuiGraphics guiGraphics, GuiMinecart gui, final int x, final int y)
    {
        String str;
        if (useDurability())
        {
            str = Localization.MODULES.TOOLS.DURABILITY.translate() + ": " + getCurrentDurability() + "/" + getMaxDurability();
            if (isBroken())
            {
                str = str + " [" + Localization.MODULES.TOOLS.BROKEN.translate() + "]";
            }
            else
            {
                str = str + " [" + 100 * getCurrentDurability() / getMaxDurability() + "%]";
            }
            str += "\n";
            if (isRepairing())
            {
                if (isActuallyRepairing())
                {
                    str = str + " [" + getRepairPercentage() + "%]";
                }
                else
                {
                    str += Localization.MODULES.TOOLS.DECENT.translate();
                }
            }
            else
            {
                str += Localization.MODULES.TOOLS.INSTRUCTION.translate(getRepairItemName());
            }
        }
        else
        {
            str = Localization.MODULES.TOOLS.UNBREAKABLE.translate();
            if (isRepairing() && !isActuallyRepairing())
            {
                str = str + " " + Localization.MODULES.TOOLS.UNBREAKABLE_REPAIR.translate();
            }
        }
        drawStringOnMouseOver(guiGraphics, gui, str, x, y, durabilityRect);
    }

    @Override
    public int numberOfDataWatchers()
    {
        return 1;
    }

    @Override
    public void initDw()
    {
        DURABILITY = createDw(EntityDataSerializers.INT);
        registerDw(DURABILITY, getMaxDurability());
    }

    @Override
    public void update()
    {
        super.update();
        if (initialDurability != -1 && !getCart().level().isClientSide) {
            setDurability(initialDurability);
            initialDurability = -1;
        }

        if (!getCart().level().isClientSide && useDurability())
        {
            if (isActuallyRepairing())
            {
                final int dif = 1;
                remainingRepairUnits -= dif;
                setDurability(getCurrentDurability() + dif * getRepairSpeed());
                if (getCurrentDurability() > getMaxDurability())
                {
                    setDurability(getCurrentDurability());
                }
            }
            if (!isActuallyRepairing())
            {
                final int units = getRepairItemUnits(getStack(0));
                if (units > 0 && units <= getMaxDurability() - getCurrentDurability())
                {
                    final int n = units / getRepairSpeed();
                    remainingRepairUnits = n;
                    maximumRepairUnits = n;
                    @Nonnull ItemStack stack = getStack(0);
                    stack.shrink(1);
                    if (getStack(0).getCount() <= 0)
                    {
                        setStack(0, ItemStack.EMPTY);
                    }
                }
            }
        }
    }

    @Override
    public boolean stopEngines()
    {
        return isRepairing();
    }

    public boolean isRepairing()
    {
        return !getStack(0).isEmpty() || isActuallyRepairing();
    }

    public boolean isActuallyRepairing()
    {
        return remainingRepairUnits > 0;
    }

    public boolean isBroken()
    {
        return getCurrentDurability() == 0 && useDurability();
    }

    public void damageTool(final int val)
    {
        final int unbreaking = (enchanter != null) ? enchanter.getUnbreakingLevel() : 0;
        if (getCart().random.nextInt(100) < 100 / (unbreaking + 1))
        {
            setDurability(getCurrentDurability() - val);
            if (getCurrentDurability() < 0)
            {
                setDurability(0);
            }
        }
        if (enchanter != null)
        {
            enchanter.damageEnchant(ModularEnchantments.EnchantmentType.TOOL, val);
        }
    }

    @Override
    public void receiveGuiData(final int id, final short data)
    {
        int dataint = data;
        if (dataint < 0)
        {
            dataint += 65536;
        }
        if (id == 0)
        {
            setDurability(((getCurrentDurability() & 0xFFFF0000) | dataint));
        }
        else if (id == 1)
        {
            setDurability(((getCurrentDurability() & 0xFFFF) | dataint << 16));
        }
        else if (id == 2)
        {
            remainingRepairUnits = data;
        }
        else if (id == 3)
        {
            maximumRepairUnits = data;
        }
    }

    @Override
    protected void Save(final CompoundTag tagCompound, final int id)
    {
        tagCompound.putInt(generateNBTName("Durability", id), getCurrentDurability());
        tagCompound.putShort(generateNBTName("Repair", id), (short) remainingRepairUnits);
        tagCompound.putShort(generateNBTName("MaxRepair", id), (short) maximumRepairUnits);
    }

    @Override
    protected void Load(final CompoundTag tagCompound, final int id)
    {
        setDurability(tagCompound.getInt(generateNBTName("Durability", id)));
        remainingRepairUnits = tagCompound.getShort(generateNBTName("Repair", id));
        maximumRepairUnits = tagCompound.getShort(generateNBTName("MaxRepair", id));
    }

    public void setDurability(int amount)
    {
        updateDw(DURABILITY, amount);
    }

    public int getCurrentDurability()
    {
        return getDw(DURABILITY);
    }

    public int getRepairPercentage()
    {
        return 100 - 100 * remainingRepairUnits / maximumRepairUnits;
    }

    @Override
    public boolean hasExtraData() {
        return true;
    }

    @Override
    public CompoundTag writeExtraData() {
        CompoundTag tag = super.writeExtraData();
        tag.putInt("durability", getCurrentDurability());
        tag.putShort("repair", (short) remainingRepairUnits);
        tag.putShort("max_repair", (short) maximumRepairUnits);
        return tag;
    }

    @Override
    public void readExtraData(CompoundTag nbt) {
        initialDurability = nbt.getInt("durability");
        remainingRepairUnits = nbt.getShort("repair");
        maximumRepairUnits = nbt.getShort("max_repair");
    }
}
