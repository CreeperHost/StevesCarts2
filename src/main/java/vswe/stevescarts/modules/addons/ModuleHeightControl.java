package vswe.stevescarts.modules.addons;

import net.creeperhost.polylib.data.serializable.IntData;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import vswe.stevescarts.StevesCartsClient;
import vswe.stevescarts.api.modules.template.ModuleAddon;
import vswe.stevescarts.client.guis.GuiMinecart;
import vswe.stevescarts.entities.ModularMinecart;
import vswe.stevescarts.helpers.HeightControlOre;
import vswe.stevescarts.helpers.ResourceHelper;
import vswe.stevescarts.polylib.EntityData;

public class ModuleHeightControl extends ModuleAddon {
    private final EntityData<Integer> yTarget = new EntityData<>(getCart(), new IntData(getCart().y()));
    private final int levelNumberBoxX;
    private final int levelNumberBoxY;
    private final int[] arrowUp;
    private final int[] arrowMiddle;
    private final int[] arrowDown;
    private final int oreMapX;
    private final int oreMapY;

    public ModuleHeightControl(ModularMinecart cart) {
        super(cart);
        levelNumberBoxX = 8;
        levelNumberBoxY = 18;
        arrowUp = new int[]{9, 36, 17, 9};
        arrowMiddle = new int[]{9, 46, 17, 6};
        arrowDown = new int[]{9, 53, 17, 9};
        oreMapX = 40;
        oreMapY = 18;
    }

    @Override
    public boolean hasSlots() {
        return false;
    }

    @Override
    public boolean hasGui() {
        return true;
    }

    @Override
    public int guiWidth() {
        return Math.max(100, oreMapX + 5 + HeightControlOre.ores.size() * 4);
    }

    @Override
    public int guiHeight() {
        return 65;
    }

    @Override
    public void drawForeground(GuiGraphicsExtractor GuiGraphicsExtractor, GuiMinecart gui) {
        drawString(GuiGraphicsExtractor, gui, getModuleName(), 8, 6, 4210752);
        final String s = String.valueOf(getYTarget());
        int x = levelNumberBoxX + 6;
        int color = 16777215;
        if (getYTarget() >= 100) {
            x -= 4;
        } else if (getYTarget() < 0) {
            x -= 4;
        } else if (getYTarget() < 10) {
            x += 3;
        }
        drawString(GuiGraphicsExtractor, gui, s, x, levelNumberBoxY + 5, color);
    }

    @Override
    public void drawBackground(GuiGraphicsExtractor GuiGraphicsExtractor, GuiMinecart gui, int x, int y) {
        Identifier texture = ResourceHelper.getResource("/gui/heightcontrol.png");
        drawImage(GuiGraphicsExtractor, texture, gui, levelNumberBoxX, levelNumberBoxY, 4, 36, 21, 15);
        drawImage(GuiGraphicsExtractor, texture, gui, arrowUp, 4, 12);
        drawImage(GuiGraphicsExtractor, texture, gui, arrowMiddle, 4, 21);
        drawImage(GuiGraphicsExtractor, texture, gui, arrowDown, 4, 27);
        for (int i = 0; i < HeightControlOre.ores.size(); ++i) {
            final HeightControlOre ore = HeightControlOre.ores.get(i);
            for (int j = 0; j < 11; ++j) {
                int altitude = getYTarget() - j + 5;
                boolean empty = ore.spanLowest() > altitude || altitude > ore.spanHighest();
                boolean high = ore.bestLowest() <= altitude && altitude <= ore.bestHighest();
                int srcY;
                int srcX;
                if (empty) {
                    srcY = 0;
                    srcX = 0;
                } else {
                    srcX = ore.srcX();
                    srcY = ore.srcY();
                    if (high) {
                        srcY += 4;
                    }
                }
                drawImage(GuiGraphicsExtractor, texture, gui, oreMapX + i * 4, oreMapY + j * 4, srcX, srcY, 4, 4);
            }
        }
        if (getYTarget() != getCart().y()) {
            drawMarker(GuiGraphicsExtractor, texture, gui, 5, false);
        }
        int pos = getYTarget() + 5 - getCart().y();
        if (pos >= 0 && pos < 11) {
            drawMarker(GuiGraphicsExtractor, texture, gui, pos, true);
        }
    }

    private void drawMarker(GuiGraphicsExtractor GuiGraphicsExtractor, Identifier texture, GuiMinecart gui, int pos, boolean isTargetLevel) {
        int srcX = 4;
        int srcY = isTargetLevel ? 6 : 0;
        drawImage(GuiGraphicsExtractor, texture, gui, oreMapX - 1, oreMapY + pos * 4 - 1, srcX, srcY, 1, 6);
        for (int i = 0; i < HeightControlOre.ores.size(); ++i) {
            drawImage(GuiGraphicsExtractor, texture, gui, oreMapX + i * 4, oreMapY + pos * 4 - 1, srcX + 1, srcY, 4, 6);
        }
        drawImage(GuiGraphicsExtractor, texture, gui, oreMapX + HeightControlOre.ores.size() * 4, oreMapY + pos * 4 - 1, srcX + 5, srcY, 1, 6);
    }

    @Override
    public void mouseClicked(GuiMinecart gui, int x, int y, int button) {
        if (button == 0) {
            byte packetData = 0;
            if (inRect(x, y, arrowMiddle)) {
                packetData |= 0x1;
            } else {
                if (!inRect(x, y, arrowUp)) {
                    if (!inRect(x, y, arrowDown)) {
                        return;
                    }
                    packetData |= 0x2;
                }
                if (StevesCartsClient.hasShiftDown()) {
                    packetData |= 0x4;
                }
            }
            sendPacket(0, packetData);
        }
    }

    @Override
    protected void receivePacket(int id, byte[] data, Player player) {
        if (id == 0) {
            byte info = data[0];
            if ((info & 0x1) != 0x0) {
                setYTarget(getCart().y());
            } else {
                int mult;
                if ((info & 0x2) == 0x0) {
                    mult = 1;
                } else {
                    mult = -1;
                }
                int dif;
                if ((info & 0x4) == 0x0) {
                    dif = 1;
                } else {
                    dif = 10;
                }
                int targetY = getYTarget();
                targetY += mult * dif;
                int min = getCart().level().getMinY();
                int max = getCart().level().getMaxY();
                if (targetY < min) {
                    targetY = min;
                } else if (targetY > max) {
                    targetY = max;
                }
                setYTarget(targetY);
            }
        }
    }

    @Override
    public int numberOfPackets() {
        return 1;
    }

    @Override
    public int getYTarget() {
        if (isPlaceholder()) {
            return 64;
        }
        return yTarget.get();
    }

    public void setYTarget(int val) {
        yTarget.set(val);
    }

    @Override
    protected void save(ValueOutput output, int id) {
        super.save(output, id);
        output.putShort(generateNBTName("Height", id), (short) getYTarget());
    }

    @Override
    protected void load(ValueInput input, int id) {
        super.load(input, id);
        setYTarget(input.getShortOr(generateNBTName("Height", id), (short) 0));
    }
}
