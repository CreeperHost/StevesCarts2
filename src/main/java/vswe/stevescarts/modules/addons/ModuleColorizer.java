package vswe.stevescarts.modules.addons;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import vswe.stevescarts.api.modules.template.ModuleAddon;
import vswe.stevescarts.client.guis.GuiMinecart;
import vswe.stevescarts.entities.ModularMinecart;
import vswe.stevescarts.helpers.IntArrayData;
import vswe.stevescarts.helpers.Localization;
import vswe.stevescarts.helpers.ResourceHelper;
import vswe.stevescarts.polylib.EntityData;

public class ModuleColorizer extends ModuleAddon {
    private final EntityData<int[]> colors = new EntityData<>(getCart(), new IntArrayData(new int[]{255, 255, 255}));
    private final int markerOffsetX;
    private final int scrollWidth;
    private int markerMoving;

    public ModuleColorizer(ModularMinecart cart) {
        super(cart);
        markerOffsetX = 10;
        scrollWidth = 64;
        markerMoving = -1;
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
    public void drawForeground(GuiGraphicsExtractor GuiGraphicsExtractor, GuiMinecart gui) {
        drawString(GuiGraphicsExtractor, gui, getModuleName(), 8, 6, 4210752);
    }

    @Override
    public int guiWidth() {
        return 125;
    }

    @Override
    public int guiHeight() {
        return 75;
    }

    private int[] getMovableMarker(final int i) {
        return new int[]{markerOffsetX + (int) (scrollWidth * (getColorVal(i) / 255.0f)) - 2, 17 + i * 20, 4, 13};
    }

    private int[] getArea(final int i) {
        return new int[]{markerOffsetX, 20 + i * 20, scrollWidth, 7};
    }

    @Override
    public void drawBackground(GuiGraphicsExtractor GuiGraphicsExtractor, GuiMinecart gui, final int x, final int y) {
        Identifier texture = ResourceHelper.getResource("/gui/color.png");
        for (int i = 0; i < 3; ++i) {
            drawMarker(GuiGraphicsExtractor, texture, gui, x, y, i);
        }
        drawImage(GuiGraphicsExtractor, texture, gui, scrollWidth + 25, 29, 4, 7, 28, 28);
    }

    @Override
    public void drawMouseOver(GuiGraphicsExtractor GuiGraphicsExtractor, GuiMinecart gui, final int x, final int y) {
        final String[] colorNames = {Localization.MODULES.ADDONS.COLOR_RED.translate(), Localization.MODULES.ADDONS.COLOR_GREEN.translate(), Localization.MODULES.ADDONS.COLOR_BLUE.translate()};
        for (int i = 0; i < 3; ++i) {
            drawStringOnMouseOver(GuiGraphicsExtractor, gui, colorNames[i] + ": " + getColorVal(i), x, y, getArea(i));
        }
    }

    private void drawMarker(GuiGraphicsExtractor GuiGraphicsExtractor, Identifier texture, GuiMinecart gui, final int x, final int y, final int id) {
        final float[] colorArea = new float[3];
        final float[] colorMarker = new float[3];
        for (int i = 0; i < 3; ++i) {
            if (i == id) {
                colorArea[i] = 0.7f;
                colorMarker[i] = 1.0f;
            } else {
                colorArea[i] = 0.2f;
                colorMarker[i] = 0.0f;
            }
        }
        drawImage(GuiGraphicsExtractor, texture, gui, getArea(id), 0, 0);
        drawImage(GuiGraphicsExtractor, texture, gui, getMovableMarker(id), 0, 7);
    }

    @Override
    public void mouseClicked(final GuiMinecart gui, final int x, final int y, final int button) {
        if (button == 0) {
            for (int i = 0; i < 3; ++i) {
                if (inRect(x, y, getMovableMarker(i))) {
                    markerMoving = i;
                }
            }
        }
    }

    @Override
    public void mouseMovedOrUp(final GuiMinecart gui, final int x, final int y, final int button) {
        if (markerMoving != -1) {
            int tempColor = (int) ((x - markerOffsetX) / (scrollWidth / 255.0f));
            if (tempColor < 0) {
                tempColor = 0;
            } else if (tempColor > 255) {
                tempColor = 255;
            }
            sendPacket(markerMoving, (byte) tempColor);
        }
        if (button != -1) {
            markerMoving = -1;
        }
    }

    @Override
    public int numberOfPackets() {
        return 3;
    }

    @Override
    protected void receivePacket(final int id, final byte[] data, final Player player) {
        if (id >= 0 && id < 3) {
            setColorVal(id, data[0]);
        }
    }

    public int getColorVal(final int i) {
        if (isPlaceholder()) {
            return 255;
        }
        int tempVal = colors.get()[i];
        if (tempVal < 0) {
            tempVal += 256;
        }
        return tempVal;
    }

    public void setColorVal(final int id, final int val) {
        int[] colors = this.colors.get();
        colors[id] = val;
        this.colors.set(colors);
    }

    private float getColorComponent(final int i) {
        return getColorVal(i) / 255.0f;
    }

    @Override
    public float[] getColor() {
        return new float[]{getColorComponent(0), getColorComponent(1), getColorComponent(2)};
    }

    @Override
    protected void save(ValueOutput output, int id) {
        super.save(output, id);
        output.putByte(generateNBTName("Red", id), (byte) getColorVal(0));
        output.putByte(generateNBTName("Green", id), (byte) getColorVal(1));
        output.putByte(generateNBTName("Blue", id), (byte) getColorVal(2));
    }

    @Override
    protected void load(ValueInput input, int id) {
        super.load(input, id);
        setColorVal(0, input.getByteOr(generateNBTName("Red", id), (byte) 0));
        setColorVal(1, input.getByteOr(generateNBTName("Green", id), (byte) 0));
        setColorVal(2, input.getByteOr(generateNBTName("Blue", id), (byte) 0));
    }
}
