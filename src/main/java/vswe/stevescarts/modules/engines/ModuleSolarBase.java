package vswe.stevescarts.modules.engines;

import net.creeperhost.polylib.data.serializable.BooleanData;
import net.creeperhost.polylib.data.serializable.IntData;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import vswe.stevescarts.api.modules.template.ModuleEngine;
import vswe.stevescarts.client.guis.GuiMinecart;
import vswe.stevescarts.entities.ModularMinecart;
import vswe.stevescarts.helpers.Localization;
import vswe.stevescarts.helpers.ResourceHelper;
import vswe.stevescarts.polylib.EntityData;

import java.util.List;

public abstract class ModuleSolarBase extends ModuleEngine {
    private final EntityData<Integer> light = new EntityData<>(getCart(), new IntData(0));
    private final EntityData<Boolean> upState = new EntityData<>(getCart(), new BooleanData(false));
    protected boolean down;
    private boolean maxLight;
    private int panelCoolDown;
    private boolean setup;

    public ModuleSolarBase(ModularMinecart cart) {
        super(cart);
        down = true;
    }

    @Override
    public boolean hasSlots() {
        return false;
    }

    @Override
    public void update() {
        super.update();
        updateSolarModel();
    }

    @Override
    protected void loadFuel() {
        updateLight();
        updateDataForModel();
        chargeSolar();
    }

    @Override
    public int getTotalFuel() {
        return getFuelLevel();
    }

    @Override
    public float[] getGuiBarColor() {
        return new float[]{1.0f, 1.0f, 0.0f};
    }

    private void updateLight() {
        if (!getCart().level().isBrightOutside() || getCart().level().isRaining()) {
            light.set(0);
        } else {
            if (getCart().level().canSeeSky(getCart().blockPosition())) {
                light.set(15);
            }
        }
    }

    private void updateDataForModel() {
        if (isPlaceholder()) {
            light.set((getSimInfo().getMaxLight() ? 15 : 14));
        }
        maxLight = (light.get() == 15);
        if (!upState.get() && light.get() == 15) {
            light.set(14);
        }
    }

    private void chargeSolar() {
        if (light.get() == 15 && getCart().level().getRandom().nextInt(8) < 4) {
            setFuelLevel(getFuelLevel() + getGenSpeed());
            if (getFuelLevel() > getMaxCapacity()) {
                setFuelLevel(getMaxCapacity());
            }
        }
    }

    public int getLight() {
        return light.get();
    }

    @Override
    public void drawForeground(GuiGraphicsExtractor GuiGraphicsExtractor, GuiMinecart gui) {
        drawString(GuiGraphicsExtractor, gui, Localization.MODULES.ENGINES.SOLAR.translate(), 8, 6, 4210752);
        String strfuel = Localization.MODULES.ENGINES.NO_POWER.translate();
        if (getFuelLevel() > 0) {
            strfuel = "Power: " + getFuelLevel();//Localization.MODULES.ENGINES.POWER.translate(String.valueOf(getFuelLevel()));
        }
        drawString(GuiGraphicsExtractor, gui, strfuel, 8, 42, 4210752);
    }

    @Override
    public void drawBackground(GuiGraphicsExtractor GuiGraphicsExtractor, GuiMinecart gui, final int x, final int y) {
        super.drawBackground(GuiGraphicsExtractor, gui, x, y);
        Identifier texture = ResourceHelper.getResource("/gui/solar.png");
        int lightWidth = light.get() * 3;
        if (light.get() == 15) {
            lightWidth += 2;
        }
        drawImage(GuiGraphicsExtractor, texture, gui, 9, 20, 0, 0, 54, 18);
        drawImage(GuiGraphicsExtractor, texture, gui, 15, 21, 0, 18, lightWidth, 16);
    }

    protected boolean isGoingDown() {
        return down;
    }

    public void updateSolarModel() {
        if (getCart().level().isClientSide()) {
            updateDataForModel();
            if (!setup) {
                boolean tmpUp = upState.get();
                if (tmpUp) {
                    setAnimDone();
                    upState.set(true);
                    down = false;
                }
                setup = true;
            }
        }
        panelCoolDown += (maxLight ? 1 : -1);
        if (down && panelCoolDown < 0) {
            panelCoolDown = 0;
        } else if (!down && panelCoolDown > 0) {
            panelCoolDown = 0;
        } else if (Math.abs(panelCoolDown) > 20) {
            panelCoolDown = 0;
            down = !down;
        }
        upState.set(updatePanels());
    }

    @Override
    public int numberOfGuiData() {
        return 2;
    }

    @Override
    protected void doGuiDataCheck(AbstractContainerMenu con, List<Player> players, boolean isNew) {
        updateGuiData(con, players, isNew, 0, (short) (getFuelLevel() & 0xFFFF));
        updateGuiData(con, players, isNew, 1, (short) (getFuelLevel() >> 16 & 0xFFFF));
    }

    @Override
    public void receiveGuiData(final int id, final short data) {
        if (id == 0) {
            int dataint = data;
            if (dataint < 0) {
                dataint += 65536;
            }
            setFuelLevel((getFuelLevel() & 0xFFFF0000) | dataint);
        } else if (id == 1) {
            setFuelLevel((getFuelLevel() & 0xFFFF) | data << 16);
        }
    }

    protected abstract int getMaxCapacity();

    protected abstract int getGenSpeed();

    protected abstract boolean updatePanels();

    protected abstract void setAnimDone();

    @Override
    protected void save(ValueOutput output, int id) {
        super.save(output, id);
        output.putInt(generateNBTName("Fuel", id), getFuelLevel());
        upState.save(generateNBTName("Up", id), output);
    }

    @Override
    protected void load(ValueInput input, int id) {
        super.load(input, id);
        setFuelLevel(input.getIntOr(generateNBTName("Fuel", id), 0));
        upState.load(generateNBTName("Up", id), input);
        if (upState.get()) {
            setAnimDone();
        }
    }
}
