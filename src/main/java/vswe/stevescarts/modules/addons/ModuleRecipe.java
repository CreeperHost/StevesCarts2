package vswe.stevescarts.modules.addons;

import net.creeperhost.polylib.data.serializable.ByteData;
import net.creeperhost.polylib.data.serializable.StackData;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import vswe.stevescarts.api.modules.ModuleBase;
import vswe.stevescarts.api.modules.template.ModuleAddon;
import vswe.stevescarts.blocks.tileentities.TileEntityCargo;
import vswe.stevescarts.client.guis.GuiMinecart;
import vswe.stevescarts.api.slots.SlotStevesCarts;
import vswe.stevescarts.api.slots.SlotChest;
import vswe.stevescarts.entities.ModularMinecart;
import vswe.stevescarts.helpers.Localization;
import vswe.stevescarts.helpers.ResourceHelper;
import vswe.stevescarts.polylib.EntityData;

import javax.annotation.Nonnull;
import java.util.ArrayList;

public abstract class ModuleRecipe extends ModuleAddon
{
    protected boolean dirty;
    protected ArrayList<SlotStevesCarts> inputSlots;
    protected ArrayList<SlotStevesCarts> outputSlots;
    protected ArrayList<SlotStevesCarts> allTheSlots;

    private final EntityData<Byte> target = new EntityData<>(getCart(), new ByteData((byte) 3));
    private final EntityData<Byte> mode = new EntityData<>(getCart(), new ByteData((byte) 0));
    private final EntityData<Byte> maxItemCount = new EntityData<>(getCart(), new ByteData((byte) 1));

    public final EntityData<ItemStack> outputDisplay = new EntityData<>(getCart(), new StackData());

    public ModuleRecipe(ModularMinecart cart)
    {
        super(cart);
        dirty = true;
        allTheSlots = new ArrayList<>();
        outputSlots = new ArrayList<>();
    }

    protected abstract int getLimitStartX();

    protected abstract int getLimitStartY();

    @Override
    public void drawBackground(GuiGraphics guiGraphics, GuiMinecart gui, final int x, final int y)
    {
        if (canUseAdvancedFeatures())
        {
            final int[] area = getArea();
            ResourceHelper.bindResource("/gui/recipe.png");
            drawImage(guiGraphics, gui, area[0] - 2, area[1] - 2, 0, 0, 20, 20);
            if (mode.get() == 1)
            {
                for (int i = 0; i < 3; ++i)
                {
                    drawControlRect(guiGraphics, gui, x, y, i);
                }
            }
            else
            {
                drawControlRect(guiGraphics, gui, x, y, 1);
            }
        }
    }

    private void drawControlRect(GuiGraphics guiGraphics, GuiMinecart gui, final int x, final int y, final int i)
    {
        final int v = i * 11;
        final int[] rect = getControlRect(i);
        drawImage(guiGraphics, gui, rect, 20 + (inRect(x, y, rect) ? 22 : 0), v);
    }

    private int[] getControlRect(final int i)
    {
        return new int[]{getLimitStartX(), getLimitStartY() + 12 * i, 22, 11};
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void drawForeground(GuiGraphics guiGraphics, GuiMinecart gui)
    {
        if (canUseAdvancedFeatures())
        {
            String str = null;
            switch (mode.get())
            {
                case 0:
                {
                    str = "Inf";
                    break;
                }
                case 1:
                {
                    str = String.valueOf(0xFF & maxItemCount.get());
                    break;
                }
                default:
                {
                    str = "X";
                    break;
                }
            }
            drawString(guiGraphics, gui, str, getControlRect(1), 4210752);
        }
    }

    @Override
    public void drawBackgroundItems(GuiGraphics guiGraphics, final GuiMinecart gui, final int x, final int y)
    {
        if (canUseAdvancedFeatures())
        {
            ItemStack icon;
            if (isTargetInvalid())
            {
                icon = new ItemStack(Items.MINECART, 1);
            }
            else
            {
                icon = TileEntityCargo.itemSelections.get(target.get()).getIcon();
            }
            final int[] area = getArea();
            drawItemInInterface(guiGraphics, gui, icon, area[0], area[1]);
        }
    }

    private boolean isTargetInvalid()
    {
        return target.get() < 0 || target.get() >= TileEntityCargo.itemSelections.size() || TileEntityCargo.itemSelections.get(target.get()).getValidSlot() == null;
    }

    @Override
    public void drawMouseOver(GuiGraphics guiGraphics, GuiMinecart gui, final int x, final int y)
    {
        if (canUseAdvancedFeatures())
        {
            String str = Localization.MODULES.ADDONS.RECIPE_OUTPUT.translate() + "\n" + Localization.MODULES.ADDONS.CURRENT.translate() + ": ";
            if (isTargetInvalid())
            {
                str += Localization.MODULES.ADDONS.INVALID_OUTPUT.translate();
            }
            else
            {
                str += TileEntityCargo.itemSelections.get(target.get()).getName();
            }
            drawStringOnMouseOver(guiGraphics, gui, str, x, y, getArea());
            for (int i = 0; i < 3; ++i)
            {
                if (i == 1)
                {
                    str = Localization.MODULES.ADDONS.RECIPE_MODE.translate() + "\n" + Localization.MODULES.ADDONS.CURRENT.translate() + ": ";
                    switch (mode.get())
                    {
                        case 0:
                        {
                            str += Localization.MODULES.ADDONS.RECIPE_NO_LIMIT.translate();
                            break;
                        }
                        case 1:
                        {
                            str += Localization.MODULES.ADDONS.RECIPE_LIMIT.translate();
                            break;
                        }
                        default:
                        {
                            str += Localization.MODULES.ADDONS.RECIPE_DISABLED.translate();
                            break;
                        }
                    }
                }
                else if (mode.get() != 1)
                {
                    str = null;
                }
                else
                {
                    str = Localization.MODULES.ADDONS.RECIPE_CHANGE_AMOUNT.translate((i == 0) ? "0" : "1") + "\n" + Localization.MODULES.ADDONS.RECIPE_CHANGE_AMOUNT_10.translate() + "\n" + Localization.MODULES.ADDONS.RECIPE_CHANGE_AMOUNT_64.translate();
                }
                if (str != null)
                {
                    drawStringOnMouseOver(guiGraphics, gui, str, x, y, getControlRect(i));
                }
            }
        }
    }

    protected abstract int[] getArea();

    @Override
    public int numberOfGuiData()
    {
        return canUseAdvancedFeatures() ? 3 : 0;
    }

    @Override
    public void mouseClicked(final GuiMinecart gui, final int x, final int y, final int button)
    {
        if (canUseAdvancedFeatures())
        {
            if (inRect(x, y, getArea()))
            {
                sendPacket(0, (byte) button);
            }
            int i = 0;
            while (i < 3)
            {
                if ((mode.get() == 1 || i == 1) && inRect(x, y, getControlRect(i)))
                {
                    if (i == 1)
                    {
                        sendPacket(1, (byte) button);
                        break;
                    }
                    byte encodedData = (byte) ((i != 0) ? 1 : 0);
                    if (Screen.hasControlDown())
                    {
                        encodedData |= 0x2;
                    }
                    else if (Screen.hasShiftDown())
                    {
                        encodedData |= 0x4;
                    }
                    sendPacket(2, encodedData);
                    break;
                }
                else
                {
                    ++i;
                }
            }
        }
    }

    @Override
    protected int numberOfPackets()
    {
        return canUseAdvancedFeatures() ? 3 : 0;
    }

    @Override
    protected void receivePacket(final int id, final byte[] data, final Player player)
    {
        if (canUseAdvancedFeatures())
        {
            int mode = this.mode.get();
            if (id == 0)
            {
                dirty = true;
                changeTarget(data[0] == 0);
            }
            else if (id == 1)
            {
                if (data[0] == 0)
                {
                    if (++mode > 2)
                    {
                        mode = 0;
                    }
                }
                else if (--mode < 0)
                {
                    mode = 2;
                }
                this.mode.set((byte) mode);
            }
            else if (id == 2)
            {
                int dif = ((data[0] & 0x1) == 0x0) ? 1 : -1;
                if ((data[0] & 0x2) != 0x0)
                {
                    dif *= 64;
                }
                else if ((data[0] & 0x4) != 0x0)
                {
                    dif *= 10;
                }
                int maxItemCount = Math.min(Math.max(1, (0xFF & this.maxItemCount.get()) + dif), 128);
                this.maxItemCount.set((byte) maxItemCount);
            }
        }
    }

    private void changeTarget(final boolean up)
    {
        int target = this.target.get();
        if (target >= TileEntityCargo.itemSelections.size())
        {
            this.target.set((byte) 0);
        }
        else
        {
            if (up)
            {
                target++;
            }
            else
            {
                target--;
            }
            this.target.set((byte) target);
        }
    }

    protected abstract boolean canUseAdvancedFeatures();

    protected Class getValidSlot()
    {
        if (isTargetInvalid())
        {
            return null;
        }
        return TileEntityCargo.itemSelections.get(target.get()).getValidSlot();
    }

    @Override
    protected void load(CompoundTag tag, int id, HolderLookup.Provider provider)
    {
        if (canUseAdvancedFeatures())
        {
            target.load(generateNBTName("Target", id), tag, provider);
            mode.load(generateNBTName("Mode", id), tag, provider);
            maxItemCount.load(generateNBTName("MaxItems", id), tag, provider);
        }
    }

    @Override
    protected void save(CompoundTag tag, int id, HolderLookup.Provider provider)
    {
        if (canUseAdvancedFeatures())
        {
            target.save(generateNBTName("Target", id), tag, provider);
            mode.save(generateNBTName("Mode", id), tag, provider);
            maxItemCount.save(generateNBTName("MaxItems", id), tag, provider);
        }
    }

    protected void prepareLists()
    {
        if (inputSlots == null)
        {
            inputSlots = new ArrayList<>();
            for (final ModuleBase module : getCart().modules())
            {
                if (module.getSlots() != null)
                {
                    for (final SlotStevesCarts slot : module.getSlots())
                    {
                        if (slot instanceof SlotChest)
                        {
                            inputSlots.add(slot);
                        }
                    }
                }
            }
        }
        if (dirty)
        {
            allTheSlots.clear();
            outputSlots.clear();
            final Class validSlot = getValidSlot();
            if (validSlot != null) {
                for (final ModuleBase module2 : getCart().modules()) {
                    if (module2.getSlots() != null) {
                        for (final SlotStevesCarts slot2 : module2.getSlots()) {
                            if (validSlot.isInstance(slot2)) {
                                outputSlots.add(slot2);
                                allTheSlots.add(slot2);
                            } else {
                                if (!(slot2 instanceof SlotChest)) {
                                    continue;
                                }
                                allTheSlots.add(slot2);
                            }
                        }
                    }
                }
            }
            dirty = false;
        }
    }

    protected boolean canCraftMoreOfResult(@Nonnull ItemStack result)
    {
        if (mode.get() == 0)
        {
            return true;
        }
        if (mode.get() == 2)
        {
            return false;
        }
        int count = 0;
        for (int i = 0; i < outputSlots.size(); ++i)
        {
            @Nonnull ItemStack item = outputSlots.get(i).getItem();
            if (!item.isEmpty() && ItemStack.isSameItem(item, result) && ItemStack.isSameItemSameComponents(item, result))
            {
                count += item.getCount();
                if (count >= (0xFF & maxItemCount.get()))
                {
                    return false;
                }
            }
        }
        return true;
    }
}
