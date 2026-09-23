package vswe.stevescarts.modules.addons;

import net.creeperhost.polylib.data.serializable.ByteData;
import net.creeperhost.polylib.data.serializable.IntData;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import vswe.stevescarts.api.modules.ModuleBase;
import vswe.stevescarts.api.modules.template.ModuleAddon;
import vswe.stevescarts.api.modules.template.ModuleEngine;
import vswe.stevescarts.api.modules.template.ModuleTool;
import vswe.stevescarts.api.slots.SlotChest;
import vswe.stevescarts.api.slots.SlotStevesCarts;
import vswe.stevescarts.client.guis.GuiMinecart;
import vswe.stevescarts.entities.ModularMinecart;
import vswe.stevescarts.helpers.LabelInformation;
import vswe.stevescarts.helpers.ResourceHelper;
import vswe.stevescarts.polylib.EntityData;

import java.util.ArrayList;

public class ModuleLabel extends ModuleAddon {
    private final EntityData<Integer> seconds = new EntityData<>(getCart(), new IntData(0));
    private final EntityData<Byte> used = new EntityData<>(getCart(), new ByteData((byte) 0));
    private final EntityData<Integer> data = new EntityData<>(getCart(), new IntData(0));
    private final ArrayList<LabelInformation> labels;
    private int delay;
    private ArrayList<SlotStevesCarts> storageSlots;
    private ModuleTool tool;
    private final EntityData<Byte> active = new EntityData<>(getCart(), new ByteData((byte) (hasToolWithDurability() ? -1 : 0)));

    public ModuleLabel(ModularMinecart cart) {
        super(cart);
        delay = 0;
        (labels = new ArrayList<>()).add(new LabelInformation("modules.addons.stevescarts.informationProviderLabelName") {
            @Override
            public Component getLabel() {
                return getCart().getName();
            }
        });
        labels.add(new LabelInformation("modules.addons.stevescarts.informationProviderLabelDistance") {
            @Override
            public Component getLabel() {
                return Component.literal(Component.translatable("modules.addons.stevescarts.informationProviderMessageDistance", String.valueOf((int) getCart().distanceTo(getClientPlayer()))).getString());
            }
        });
        labels.add(new LabelInformation("modules.addons.stevescarts.informationProviderLabelPosition") {
            @Override
            public Component getLabel() {
                return Component.literal(Component.translatable("modules.addons.stevescarts.informationProviderMessagePosition", String.valueOf(getCart().x()), String.valueOf(getCart().y()), String.valueOf(getCart().z())).getString());
            }
        });
        labels.add(new LabelInformation("modules.addons.stevescarts.informationProviderLabelFuel") {
            @Override
            public Component getLabel() {
                int seconds = ModuleLabel.this.seconds.get();
                if (seconds == -1) {
                    return Component.literal(Component.translatable("modules.addons.stevescarts.informationProviderMessageNoConsumption").getString());
                }
                int minutes = seconds / 60;
                seconds -= minutes * 60;
                final int hours = minutes / 60;
                minutes -= hours * 60;
                return Component.literal(String.format(Component.translatable("modules.addons.stevescarts.informationProviderMessageFuel").getString() + ": %02d:%02d:%02d", hours, minutes, seconds));
            }
        });
        labels.add(new LabelInformation("modules.addons.stevescarts.informationProviderLabelStorage") {
            @Override
            public Component getLabel() {
                int used = ModuleLabel.this.used.get();
                if (used < 0) {
                    used += 256;
                }
                return Component.literal((storageSlots == null) ? "" : (Component.translatable("modules.addons.stevescarts.informationProviderLabelStorage").getString() + ": " + used + "/" + storageSlots.size() + ((storageSlots.size() == 0) ? "" : ("[" + (int) (100.0f * used / storageSlots.size()) + "%]"))));
            }
        });
    }

    @Override
    public void preInit() {
        if (getCart().modules() != null) {
            for (final ModuleBase moduleBase : getCart().modules()) {
                if (moduleBase instanceof ModuleTool) {
                    tool = (ModuleTool) moduleBase;
                    labels.add(new LabelInformation("modules.addons.stevescarts.informationProviderLabelDurability") {
                        @Override
                        public Component getLabel() {
                            if (!tool.useDurability()) {
                                return Component.literal(Component.translatable("modules.addons.stevescarts.informationProviderMessageUnbreakable").getString());
                            }
                            final int data = ModuleLabel.this.data.get();
                            if (data == 0) {
                                return Component.literal(Component.translatable("modules.addons.stevescarts.informationProviderMessageToolBroken").getString());
                            }
                            if (data > 0) {
                                return Component.literal(Component.translatable("modules.addons.stevescarts.informationProviderLabelDurability").getString() + ": " + data + " / " + tool.getMaxDurability() + " [" + 100 * data / tool.getMaxDurability() + "%]");
                            }
                            if (data == -1) {
                                return Component.empty();
                            }
                            if (data == -2) {
                                return Component.literal(Component.translatable("modules.addons.stevescarts.informationProviderMessageToolNotBroken").getString());
                            }
                            return Component.literal(Component.translatable("modules.addons.stevescarts.informationProviderMessageRepair").getString() + " [" + -(data + 3) + "%]");
                        }
                    });
                    break;
                }
            }
        }
    }

    @Override
    public void init() {
        storageSlots = new ArrayList<>();
        for (final ModuleBase module : getCart().modules()) {
            if (module.getSlots() != null) {
                for (final SlotStevesCarts slot : module.getSlots()) {
                    if (slot instanceof SlotChest) {
                        storageSlots.add(slot);
                    }
                }
            }
        }
    }

    private boolean hasTool() {
        return tool != null;
    }

    private boolean hasToolWithDurability() {
        return hasTool() && tool.useDurability();
    }

    @Override
    public void addToLabel(final ArrayList<Component> label) {
        for (int i = 0; i < labels.size(); ++i) {
            if (isActive(i)) {
                label.add(labels.get(i).getLabel());
            }
        }
    }

    private int[] getBoxArea(final int i) {
        return new int[]{10, 17 + i * 12, 8, 8};
    }

    @Override
    public void drawBackground(GuiGraphicsExtractor GuiGraphicsExtractor, GuiMinecart gui, final int x, final int y) {
        Identifier texture = ResourceHelper.getResource("/gui/label.png");
        for (int i = 0; i < labels.size(); ++i) {
            final int[] rect = getBoxArea(i);
            drawImage(GuiGraphicsExtractor, texture, gui, rect, isActive(i) ? 8 : 0, 0);
            drawImage(GuiGraphicsExtractor, texture, gui, rect, inRect(x, y, rect) ? 8 : 0, 8);
        }
    }

    private boolean isActive(final int i) {
        return !isPlaceholder() && (active.get() & 1 << i) != 0x0;
    }

    private void toggleActive(final int i) {
        active.set((byte) (active.get() ^ 1 << i));
    }

    @Override
    public void update() {
        if (!isPlaceholder() && !getCart().level().isClientSide()) {
            if (delay <= 0) {
                if (isActive(3)) {
                    int data = 0;
                    for (final ModuleEngine engine : getCart().engines()) {
                        if (engine.getPriority() != 3) {
                            data += engine.getTotalFuel();
                        }
                    }
                    if (data != 0) {
                        final int consumption = getCart().getConsumption();
                        if (consumption == 0) {
                            data = -1;
                        } else {
                            data /= consumption * 20;
                        }
                    }
                    seconds.set(data);
                }
                if (isActive(4)) {
                    int data = 0;
                    for (final SlotStevesCarts slot : storageSlots) {
                        if (slot.hasItem()) {
                            ++data;
                        }
                    }
                    used.set((byte) data);
                }
                if (hasToolWithDurability()) {
                    if (isActive(5)) {
                        if (tool.isRepairing()) {
                            if (tool.isActuallyRepairing()) {
                                data.set(-3 - tool.getRepairPercentage());
                            } else {
                                data.set(-2);
                            }
                        } else {
                            data.set(tool.getCurrentDurability());
                        }
                    } else if (data.get() != -1) {
                        data.set(-1);
                    }
                }
                delay = 20;
            } else if (delay > 0) {
                --delay;
            }
        }
    }

    @Override
    public void mouseClicked(final GuiMinecart gui, final int x, final int y, final int button) {
        for (int i = 0; i < labels.size(); ++i) {
            final int[] rect = getBoxArea(i);
            if (inRect(x, y, rect)) {
                sendPacket(0, (byte) i);
                break;
            }
        }
    }

    @Override
    protected int numberOfPackets() {
        return 1;
    }

    @Override
    protected void receivePacket(final int id, final byte[] data, final Player player) {
        if (id == 0) {
            toggleActive(data[0]);
        }
    }

    @Override
    public void drawForeground(GuiGraphicsExtractor GuiGraphicsExtractor, GuiMinecart gui) {
        drawString(GuiGraphicsExtractor, gui, Component.translatable("modules.addons.stevescarts.informationProviderLabels").getString(), 8, 6, 4210752);
        for (int i = 0; i < labels.size(); ++i) {
            final int[] rect = getBoxArea(i);
            drawString(GuiGraphicsExtractor, gui, labels.get(i).getName(), rect[0] + 12, rect[1] + 1, 4210752);
        }
    }

    @Override
    public boolean hasGui() {
        return true;
    }

    @Override
    public boolean hasSlots() {
        return false;
    }

    @Override
    public int guiWidth() {
        return 92;
    }

    @Override
    public int guiHeight() {
        return 77;
    }

    @Override
    protected void load(ValueInput input, int id) {
        active.load(generateNBTName("Active", id), input);
    }

    @Override
    protected void save(ValueOutput output, int id) {
        active.save(generateNBTName("Active", id), output);
    }
}
