package vswe.stevescarts.modules.addons;

import net.creeperhost.polylib.data.serializable.ByteData;
import net.creeperhost.polylib.data.serializable.IntData;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.world.entity.player.Player;
import net.neoforged.fml.ModLoader;
import vswe.stevescarts.api.modules.ModuleBase;
import vswe.stevescarts.api.modules.template.ModuleAddon;
import vswe.stevescarts.api.modules.template.ModuleEngine;
import vswe.stevescarts.client.guis.GuiMinecart;
import vswe.stevescarts.api.slots.SlotStevesCarts;
import vswe.stevescarts.api.slots.SlotChest;
import vswe.stevescarts.entities.EntityMinecartModular;
import vswe.stevescarts.helpers.LabelInformation;
import vswe.stevescarts.helpers.Localization;
import vswe.stevescarts.helpers.ResourceHelper;
import vswe.stevescarts.api.modules.template.ModuleTool;
import vswe.stevescarts.polylib.EntityData;

import java.util.ArrayList;

public class ModuleLabel extends ModuleAddon
{
    private ArrayList<LabelInformation> labels;
    private int delay;
    private ArrayList<SlotStevesCarts> storageSlots;
    private ModuleTool tool;
    private final EntityData<Integer> seconds = new EntityData<>(getCart(), new IntData(0));
    private final EntityData<Byte> used = new EntityData<>(getCart(), new ByteData((byte) 0));
    private final EntityData<Integer> data = new EntityData<>(getCart(), new IntData(0));
    private final EntityData<Byte> active = new EntityData<>(getCart(), new ByteData((byte) (hasToolWithDurability() ? -1 : 0)));

    public ModuleLabel(final EntityMinecartModular cart)
    {
        super(cart);
        delay = 0;
        (labels = new ArrayList<>()).add(new LabelInformation(Localization.MODULES.ADDONS.NAME)
        {
            @Override
            public Component getLabel()
            {
                return getCart().getName();
            }
        });
        labels.add(new LabelInformation(Localization.MODULES.ADDONS.DISTANCE)
        {
            @Override
            public Component getLabel()
            {
                return Component.literal(Localization.MODULES.ADDONS.DISTANCE_LONG.translate(String.valueOf((int) getCart().distanceTo(getClientPlayer()))));
            }
        });
        labels.add(new LabelInformation(Localization.MODULES.ADDONS.POSITION)
        {
            @Override
            public Component getLabel()
            {
                return Component.literal(Localization.MODULES.ADDONS.POSITION_LONG.translate(String.valueOf(getCart().x()), String.valueOf(getCart().y()), String.valueOf(getCart().z())));
            }
        });
        labels.add(new LabelInformation(Localization.MODULES.ADDONS.FUEL)
        {
            @Override
            public Component getLabel()
            {
                int seconds = ModuleLabel.this.seconds.get();
                if (seconds == -1)
                {
                    return Component.literal(Localization.MODULES.ADDONS.FUEL_NO_CONSUMPTION.translate());
                }
                int minutes = seconds / 60;
                seconds -= minutes * 60;
                final int hours = minutes / 60;
                minutes -= hours * 60;
                return Component.literal(String.format(Localization.MODULES.ADDONS.FUEL_LONG.translate() + ": %02d:%02d:%02d", hours, minutes, seconds));
            }
        });
        labels.add(new LabelInformation(Localization.MODULES.ADDONS.STORAGE)
        {
            @Override
            public Component getLabel()
            {
                int used = ModuleLabel.this.used.get();
                if (used < 0)
                {
                    used += 256;
                }
                return Component.literal((storageSlots == null) ? "" : (Localization.MODULES.ADDONS.STORAGE.translate() + ": " + used + "/" + storageSlots.size() + ((storageSlots.size() == 0) ? "" : ("[" + (int) (100.0f * used / storageSlots.size()) + "%]"))));
            }
        });
    }

    @Override
    public void preInit()
    {
        if (getCart().getModules() != null)
        {
            for (final ModuleBase moduleBase : getCart().getModules())
            {
                if (moduleBase instanceof ModuleTool)
                {
                    tool = (ModuleTool) moduleBase;
                    labels.add(new LabelInformation(Localization.MODULES.ADDONS.DURABILITY)
                    {
                        @Override
                        public Component getLabel()
                        {
                            if (!tool.useDurability())
                            {
                                return Component.literal(Localization.MODULES.ADDONS.UNBREAKABLE.translate());
                            }
                            final int data = ModuleLabel.this.data.get();
                            if (data == 0)
                            {
                                return Component.literal(Localization.MODULES.ADDONS.BROKEN.translate());
                            }
                            if (data > 0)
                            {
                                return Component.literal(Localization.MODULES.ADDONS.DURABILITY.translate() + ": " + data + " / " + tool.getMaxDurability() + " [" + 100 * data / tool.getMaxDurability() + "%]");
                            }
                            if (data == -1)
                            {
                                return Component.empty();
                            }
                            if (data == -2)
                            {
                                return Component.literal(Localization.MODULES.ADDONS.NOT_BROKEN.translate());
                            }
                            return Component.literal(Localization.MODULES.ADDONS.REPAIR.translate() + " [" + -(data + 3) + "%]");
                        }
                    });
                    break;
                }
            }
        }
    }

    @Override
    public void init()
    {
        storageSlots = new ArrayList<>();
        for (final ModuleBase module : getCart().getModules())
        {
            if (module.getSlots() != null)
            {
                for (final SlotStevesCarts slot : module.getSlots())
                {
                    if (slot instanceof SlotChest)
                    {
                        storageSlots.add(slot);
                    }
                }
            }
        }
    }

    private boolean hasTool()
    {
        return tool != null;
    }

    private boolean hasToolWithDurability()
    {
        return hasTool() && tool.useDurability();
    }

    @Override
    public void addToLabel(final ArrayList<Component> label)
    {
        for (int i = 0; i < labels.size(); ++i)
        {
            if (isActive(i))
            {
                label.add(labels.get(i).getLabel());
            }
        }
    }

    private int[] getBoxArea(final int i)
    {
        return new int[]{10, 17 + i * 12, 8, 8};
    }

    @Override
    public void drawBackground(GuiGraphics guiGraphics, GuiMinecart gui, final int x, final int y)
    {
        ResourceHelper.bindResource("/gui/label.png");
        for (int i = 0; i < labels.size(); ++i)
        {
            final int[] rect = getBoxArea(i);
            drawImage(guiGraphics, gui, rect, isActive(i) ? 8 : 0, 0);
            drawImage(guiGraphics, gui, rect, inRect(x, y, rect) ? 8 : 0, 8);
        }
    }

    private boolean isActive(final int i)
    {
        return !isPlaceholder() && (active.get() & 1 << i) != 0x0;
    }

    private void toggleActive(final int i)
    {
        active.set((byte) (active.get() ^ 1 << i));
    }

    @Override
    public void update()
    {
        if (!isPlaceholder() && !getCart().level().isClientSide)
        {
            if (delay <= 0)
            {
                if (isActive(3))
                {
                    int data = 0;
                    for (final ModuleEngine engine : getCart().getEngines())
                    {
                        if (engine.getPriority() != 3)
                        {
                            data += engine.getTotalFuel();
                        }
                    }
                    if (data != 0)
                    {
                        final int consumption = getCart().getConsumption();
                        if (consumption == 0)
                        {
                            data = -1;
                        }
                        else
                        {
                            data /= consumption * 20;
                        }
                    }
                    seconds.set(data);
                }
                if (isActive(4))
                {
                    int data = 0;
                    for (final SlotStevesCarts slot : storageSlots)
                    {
                        if (slot.hasItem())
                        {
                            ++data;
                        }
                    }
                    used.set((byte) data);
                }
                if (hasToolWithDurability())
                {
                    if (isActive(5))
                    {
                        if (tool.isRepairing())
                        {
                            if (tool.isActuallyRepairing())
                            {
                                data.set(-3 - tool.getRepairPercentage());
                            }
                            else
                            {
                                data.set(-2);
                            }
                        }
                        else
                        {
                            data.set(tool.getCurrentDurability());
                        }
                    }
                    else if (data.get() != -1)
                    {
                        data.set(-1);
                    }
                }
                delay = 20;
            }
            else if (delay > 0)
            {
                --delay;
            }
        }
    }

    @Override
    public void mouseClicked(final GuiMinecart gui, final int x, final int y, final int button)
    {
        for (int i = 0; i < labels.size(); ++i)
        {
            final int[] rect = getBoxArea(i);
            if (inRect(x, y, rect))
            {
                sendPacket(0, (byte) i);
                break;
            }
        }
    }

    @Override
    protected int numberOfPackets()
    {
        return 1;
    }

    @Override
    protected void receivePacket(final int id, final byte[] data, final Player player)
    {
        if (id == 0)
        {
            toggleActive(data[0]);
        }
    }

    @Override
    public void drawForeground(GuiGraphics guiGraphics, GuiMinecart gui)
    {
        drawString(guiGraphics, gui, Localization.MODULES.ADDONS.LABELS.translate(), 8, 6, 4210752);
        for (int i = 0; i < labels.size(); ++i)
        {
            final int[] rect = getBoxArea(i);
            drawString(guiGraphics, gui, labels.get(i).getName(), rect[0] + 12, rect[1] + 1, 4210752);
        }
    }

    @Override
    public boolean hasGui()
    {
        return true;
    }

    @Override
    public boolean hasSlots()
    {
        return false;
    }

    @Override
    public int guiWidth()
    {
        return 92;
    }

    @Override
    public int guiHeight()
    {
        return 77;
    }

    @Override
    protected void load(CompoundTag tag, int id, HolderLookup.Provider provider)
    {
        active.load(generateNBTName("Active", id), tag, provider);
    }

    @Override
    protected void save(CompoundTag tag, int id, HolderLookup.Provider provider)
    {
        active.save(generateNBTName("Active", id), tag, provider);
    }
}
