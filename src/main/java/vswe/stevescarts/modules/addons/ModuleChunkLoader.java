package vswe.stevescarts.modules.addons;

import net.creeperhost.polylib.data.serializable.BooleanData;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import vswe.stevescarts.api.modules.interfaces.IActivatorModule;
import vswe.stevescarts.api.modules.template.ModuleAddon;
import vswe.stevescarts.client.guis.GuiMinecart;
import vswe.stevescarts.entities.ModularMinecart;
import vswe.stevescarts.helpers.ResourceHelper;
import vswe.stevescarts.polylib.EntityData;

public class ModuleChunkLoader extends ModuleAddon implements IActivatorModule {
    private final EntityData<Boolean> loadingChunk = new EntityData<>(getCart(), new BooleanData(false));
    private boolean rdyToInit;
    private final int[] buttonRect;

    public ModuleChunkLoader(ModularMinecart cart) {
        super(cart);
        buttonRect = new int[]{20, 20, 24, 12};
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
        return 80;
    }

    @Override
    public int guiHeight() {
        return 35;
    }

    @Override
    public void drawForeground(GuiGraphicsExtractor GuiGraphicsExtractor, GuiMinecart gui) {
        drawString(GuiGraphicsExtractor, gui, "Chunk Loader", 8, 6, 4210752);
    }

    @Override
    public void update() {
        super.update();
        if (!rdyToInit) {
            rdyToInit = true;
        }
        if (isLoadingChunk()) {
            if (!getCart().hasFuelForModule() && !getCart().level().isClientSide()) {
                setChunkLoading(false);
                return;
            }
            getCart().loadChunks();
        }
    }

    public void setChunkLoading(final boolean val) {
        if (!isPlaceholder()) {
            loadingChunk.set(val);
            if (!getCart().level().isClientSide() && rdyToInit) {
                if (val) {
                    getCart().initChunkLoading();
                } else {
                    getCart().dropChunkLoading();
                }
            }
        }
    }

    private boolean isLoadingChunk() {
        return loadingChunk.get();
    }

    @Override
    public void drawBackground(GuiGraphicsExtractor GuiGraphicsExtractor, GuiMinecart gui, final int x, final int y) {
        Identifier texture = ResourceHelper.getResource("/gui/chunk.png");
        final int imageID = isLoadingChunk() ? 1 : 0;
        int borderID = 0;
        if (inRect(x, y, buttonRect)) {
            borderID = 1;
        }
        drawImage(GuiGraphicsExtractor, texture, gui, buttonRect, 0, buttonRect[3] * borderID);
        final int srcY = buttonRect[3] * 2 + imageID * (buttonRect[3] - 2);
        drawImage(GuiGraphicsExtractor, texture, gui, buttonRect[0] + 1, buttonRect[1] + 1, 0, srcY, buttonRect[2] - 2, buttonRect[3] - 2);
    }

    @Override
    public void drawMouseOver(GuiGraphicsExtractor GuiGraphicsExtractor, GuiMinecart gui, final int x, final int y) {
        drawStringOnMouseOver(GuiGraphicsExtractor, gui, getStateName(), x, y, buttonRect);
    }

    private String getStateName() {
        if (!isLoadingChunk()) {
            return "Activate chunk loading";
        }
        return "Deactivate chunk loading";
    }

    @Override
    public void mouseClicked(final GuiMinecart gui, final int x, final int y, final int button) {
        if (button == 0 && inRect(x, y, buttonRect)) {
            sendPacket(0);
        }
    }

    @Override
    protected void receivePacket(final int id, final byte[] data, final Player player) {
        if (id == 0) {
            setChunkLoading(!isLoadingChunk());
        }
    }

    @Override
    public int numberOfPackets() {
        return 1;
    }

    @Override
    public int getConsumption(final boolean isMoving) {
        return isLoadingChunk() ? 5 : super.getConsumption(isMoving);
    }

    @Override
    protected void save(ValueOutput output, int id) {
        super.save(output, id);
        output.putBoolean(generateNBTName("ChunkLoading", id), isLoadingChunk());
    }

    @Override
    protected void load(ValueInput input, int id) {
        super.load(input, id);
        setChunkLoading(input.getBooleanOr(generateNBTName("ChunkLoading", id), false));
    }

    @Override
    public void doActivate(final int id) {
        setChunkLoading(true);
    }

    @Override
    public void doDeActivate(final int id) {
        setChunkLoading(false);
    }

    @Override
    public boolean isActive(final int id) {
        return isLoadingChunk();
    }
}
