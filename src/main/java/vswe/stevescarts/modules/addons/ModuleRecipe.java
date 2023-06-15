package vswe.stevescarts.modules.addons;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import vswe.stevescarts.blocks.tileentities.TileEntityCargo;
import vswe.stevescarts.client.guis.GuiMinecart;
import vswe.stevescarts.containers.slots.SlotBase;
import vswe.stevescarts.containers.slots.SlotChest;
import vswe.stevescarts.entities.EntityMinecartModular;
import vswe.stevescarts.helpers.Localization;
import vswe.stevescarts.helpers.ResourceHelper;
import vswe.stevescarts.api.modules.ModuleBase;

import javax.annotation.Nonnull;
import java.util.ArrayList;

public abstract class ModuleRecipe extends ModuleAddon
{
    private EntityDataAccessor<Byte> TARGET;
    private EntityDataAccessor<Byte> MAX_ITEM_COUNT;
    private EntityDataAccessor<Byte> MODE;

    protected boolean dirty;
    protected ArrayList<SlotBase> inputSlots;
    protected ArrayList<SlotBase> outputSlots;
    protected ArrayList<SlotBase> allTheSlots;


    public ModuleRecipe(final EntityMinecartModular cart)
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
            if (getDw(MODE) == 1)
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
            switch (getDw(MODE))
            {
                case 0:
                {
                    str = "Inf";
                    break;
                }
                case 1:
                {
                    str = String.valueOf(getDw(MAX_ITEM_COUNT));
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
                icon = TileEntityCargo.itemSelections.get(getDw(TARGET)).getIcon();
            }
            final int[] area = getArea();
            drawItemInInterface(guiGraphics, gui, icon, area[0], area[1]);
        }
    }

    private boolean isTargetInvalid()
    {
        return getDw(TARGET) < 0 || getDw(TARGET) >= TileEntityCargo.itemSelections.size() || TileEntityCargo.itemSelections.get(getDw(TARGET)).getValidSlot() == null;
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
                str += TileEntityCargo.itemSelections.get(getDw(TARGET)).getName();
            }
            drawStringOnMouseOver(guiGraphics, gui, str, x, y, getArea());
            for (int i = 0; i < 3; ++i)
            {
                if (i == 1)
                {
                    str = Localization.MODULES.ADDONS.RECIPE_MODE.translate() + "\n" + Localization.MODULES.ADDONS.CURRENT.translate() + ": ";
                    switch (getDw(MODE))
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
                else if (getDw(MODE) != 1)
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

//  I dont think these are needed with EntityDataAccessor
//    @Override
//    protected void checkGuiData(final Object[] info)
//    {
//        if (canUseAdvancedFeatures())
//        {
//            updateGuiData(info, 0, (short) getDw(TARGET));
//            updateGuiData(info, 1, (short) getDw(MODE));
//            updateGuiData(info, 2, (short) getDw(MAX_ITEM_COUNT));
//        }
//    }
//
//    @Override
//    public void receiveGuiData(final int id, final short data)
//    {
//        if (canUseAdvancedFeatures())
//        {
//            if (id == 0)
//            {
//                updateDw(TARGET, (byte) data);
//            }
//            else if (id == 1)
//            {
//                updateDw(MODE, (byte) data);
//            }
//            else if (id == 2)
//            {
//                updateDw(MAX_ITEM_COUNT, (byte) data);
//            }
//        }
//    }

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
                if ((getDw(MODE) == 1 || i == 1) && inRect(x, y, getControlRect(i)))
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
            int mode = getDw(MODE);
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
                updateDw(MODE, (byte) mode);
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
                int maxItemCount = Math.min(Math.max(1, getDw(MAX_ITEM_COUNT) + dif), 999);
                updateDw(MAX_ITEM_COUNT, (byte) maxItemCount);
            }
        }
    }

    private void changeTarget(final boolean up)
    {
        int target = getDw(TARGET);
        if (target >= TileEntityCargo.itemSelections.size())
        {
            updateDw(TARGET, (byte) 0);
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
            updateDw(TARGET, (byte) target);
        }
    }

    protected abstract boolean canUseAdvancedFeatures();

    protected Class getValidSlot()
    {
        if (isTargetInvalid())
        {
            return null;
        }
        return TileEntityCargo.itemSelections.get(getDw(TARGET)).getValidSlot();
    }

    @Override
    public int numberOfDataWatchers()
    {
        return 3;
    }

    @Override
    public void initDw()
    {
        super.initDw();
        TARGET = createDw(EntityDataSerializers.BYTE);
        MODE = createDw(EntityDataSerializers.BYTE);
        MAX_ITEM_COUNT = createDw(EntityDataSerializers.BYTE);
        registerDw(TARGET, (byte) 3);
        registerDw(MODE, (byte) 0);
        registerDw(MAX_ITEM_COUNT, (byte) 1);
    }

    @Override
    protected void Load(final CompoundTag tagCompound, final int id)
    {
        if (canUseAdvancedFeatures())
        {
            updateDw(TARGET, tagCompound.getByte(generateNBTName("Target", id)));
            updateDw(MODE, tagCompound.getByte(generateNBTName("Mode", id)));
            updateDw(MAX_ITEM_COUNT, tagCompound.getByte(generateNBTName("MaxItems", id)));
        }
    }

    @Override
    protected void Save(final CompoundTag tagCompound, final int id)
    {
        if (canUseAdvancedFeatures())
        {
            tagCompound.putByte(generateNBTName("Target", id), (byte) getDw(TARGET));
            tagCompound.putByte(generateNBTName("Mode", id), (byte) getDw(MODE));
            tagCompound.putShort(generateNBTName("MaxItems", id), (byte) getDw(MAX_ITEM_COUNT));
        }
    }

    protected void prepareLists()
    {
        if (inputSlots == null)
        {
            inputSlots = new ArrayList<>();
            for (final ModuleBase module : getCart().getModules())
            {
                if (module.getSlots() != null)
                {
                    for (final SlotBase slot : module.getSlots())
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
            for (final ModuleBase module2 : getCart().getModules())
            {
                if (module2.getSlots() != null)
                {
                    for (final SlotBase slot2 : module2.getSlots())
                    {
                        if (validSlot.isInstance(slot2))
                        {
                            outputSlots.add(slot2);
                            allTheSlots.add(slot2);
                        }
                        else
                        {
                            if (!(slot2 instanceof SlotChest))
                            {
                                continue;
                            }
                            allTheSlots.add(slot2);
                        }
                    }
                }
            }
            dirty = false;
        }
    }

    protected boolean canCraftMoreOfResult(@Nonnull ItemStack result)
    {
        if (getDw(MODE) == 0)
        {
            return true;
        }
        if (getDw(MODE) == 2)
        {
            return false;
        }
        int count = 0;
        for (int i = 0; i < outputSlots.size(); ++i)
        {
            @Nonnull ItemStack item = outputSlots.get(i).getItem();
            if (!item.isEmpty() && ItemStack.isSameItem(item, result) && ItemStack.isSameItemSameTags(item, result))
            {
                count += item.getCount();
                if (count >= getDw(MAX_ITEM_COUNT))
                {
                    return false;
                }
            }
        }
        return true;
    }
}
