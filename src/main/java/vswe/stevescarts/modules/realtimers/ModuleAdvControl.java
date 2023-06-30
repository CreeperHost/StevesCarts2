package vswe.stevescarts.modules.realtimers;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import vswe.stevescarts.api.modules.ModuleBase;
import vswe.stevescarts.api.modules.interfaces.ILeverModule;
import vswe.stevescarts.api.modules.template.ModuleEngine;
import vswe.stevescarts.client.guis.GuiMinecart;
import vswe.stevescarts.entities.EntityMinecartModular;
import vswe.stevescarts.helpers.Localization;
import vswe.stevescarts.helpers.ResourceHelper;

public class ModuleAdvControl extends ModuleBase implements ILeverModule {
    private byte[] engineInformation;
    private int tripPacketTimer;
    private int enginePacketTimer;
    private byte keyinformation;
    private double lastPosX;
    private double lastPosY;
    private double lastPosZ;
    private boolean first;
    private int speedChangeCooldown;
    private boolean lastBackKey;
    private double odo;
    private double trip;
    private int[] buttonRect;
    private EntityDataAccessor<Integer> SPEED;

    public ModuleAdvControl(final EntityMinecartModular cart) {
        super(cart);
        first = true;
        buttonRect = new int[]{15, 20, 24, 12};
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
        return 90;
    }

    @Override
    public int guiHeight() {
        return 35;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void renderOverlay(PoseStack poseStack, Minecraft minecraft) {
        ResourceHelper.bindResource("/gui/drive.png");
        if (engineInformation != null)
        {
            for (int i = 0; i < getCart().getEngines().size(); ++i) {
                drawImage(5, i * 15, 0, 0, 66, 15);
                int upperBarLength = engineInformation[i * 2] & 0x3F;
                int lowerBarLength = engineInformation[i * 2 + 1] & 0x3F;
                ModuleEngine engine = getCart().getEngines().get(i);
                float[] rgb = engine.getGuiBarColor();
                RenderSystem.setShaderColor(rgb[0], rgb[1], rgb[2], 1.0f);
                drawImage(7, i * 15 + 2, 66, 0, upperBarLength, 5);
                drawImage(7, i * 15 + 2 + 6, 66, 6, lowerBarLength, 5);
                RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
                drawImage(5, i * 15, 66 + engine.getPriority() * 7, 11, 7, 15);
            }
        }
        int enginesEndAt = getCart().getEngines().size() * 15;
        drawImage(5, enginesEndAt, 0, 15, 32, 32);
        if (minecraft.options.keyUp.isDown()) {
            drawImage(15, enginesEndAt + 5, 42, 20, 12, 6);
        } else if (minecraft.options.keyLeft.isDown()) {
            drawImage(7, enginesEndAt + 13, 34, 28, 6, 12);
        } else if (minecraft.options.keyRight.isDown()) {
            drawImage(29, enginesEndAt + 13, 56, 28, 6, 12);
        }
        int speedGraphicHeight = getSpeedSetting() * 2;
        drawImage(14, enginesEndAt + 13 + 12 - speedGraphicHeight, 41, 40 - speedGraphicHeight, 14, speedGraphicHeight);
        drawImage(0, 0, 0, 67, 5, 130);
        //TODO this is a y-level indicator that needs to be redesigned for new world heights.
        drawImage(1, 1 + (256 - getCart().y()) / 2, 5, 67, 5, 1);
        drawImage(5, enginesEndAt + 32, 0, 47, 32, 20);
        drawImage(5, enginesEndAt + 52, 0, 47, 32, 20);
        drawImage(5, enginesEndAt + 72, 0, 47, 32, 20);

        //TODO
//        guiGraphics.drawString(minecraft.font, Localization.MODULES.ATTACHMENTS.ODO.translate(), 7, enginesEndAt + 52 + 2, 0x909090);
//        guiGraphics.drawString(minecraft.font, distToString(odo), 7, enginesEndAt + 52 + 11, 0x909090);
//        guiGraphics.drawString(minecraft.font, Localization.MODULES.ATTACHMENTS.TRIP.translate(), 7, enginesEndAt + 52 + 22, 0x909090);
//        guiGraphics.drawString(minecraft.font, distToString(trip), 7, enginesEndAt + 52 + 31, 0x909090);
//
//        drawItem(guiGraphics, new ItemStack(Items.CLOCK), 5, enginesEndAt + 32 + 3);
//        drawItem(guiGraphics, new ItemStack(Items.COMPASS), 21, enginesEndAt + 32 + 3);
    }

    @OnlyIn(Dist.CLIENT)
    public void drawItem(GuiGraphics guiGraphics, ItemStack icon, final int targetX, final int targetY) {
        guiGraphics.renderItem(getClientPlayer(), icon, targetX, targetY, targetX + targetX * guiWidth());
    }

    private String distToString(double dist) {
        int i;
        for (i = 0; dist >= 1000.0; dist /= 1000.0, ++i) {
        }
        int val;
        if (dist >= 100.0) {
            val = 1;
        } else if (dist >= 10.0) {
            val = 10;
        } else {
            val = 100;
        }
        final double d = Math.round(dist * val) / val;
        String s;
        if (d == (int) d) {
            s = String.valueOf((int) d);
        } else {
            s = String.valueOf(d);
        }
        while (s.length() < ((s.indexOf(46) != -1) ? 4 : 3)) {
            if (s.indexOf(46) != -1) {
                s += "0";
            } else {
                s += ".0";
            }
        }
        s += Localization.MODULES.ATTACHMENTS.DISTANCES.translate(String.valueOf(i));
        return s;
    }

    @Override
    public RAILDIRECTION getSpecialRailDirection(BlockPos pos) {
        if (this.isForwardKeyDown()) {
            return RAILDIRECTION.FORWARD;
        }
        if (this.isLeftKeyDown()) {
            return RAILDIRECTION.LEFT;
        }
        if (this.isRightKeyDown()) {
            return RAILDIRECTION.RIGHT;
        }
        return RAILDIRECTION.DEFAULT;
    }

    @Override
    protected void receivePacket(final int id, final byte[] data, final Player player) {
        if (id == 0) {
            engineInformation = data;
        } else if (id == 1) {
            if (getCart().getCartRider() != null && getCart().getCartRider() instanceof Player && getCart().getCartRider() == player) {
                keyinformation = data[0];
            }
        } else if (id == 2) {
            int intOdo = 0;
            int intTrip = 0;
            for (int i = 0; i < 4; ++i) {
                int temp = data[i];
                if (temp < 0) {
                    temp += 256;
                }
                intOdo |= temp << i * 8;
                temp = data[i + 4];
                if (temp < 0) {
                    temp += 256;
                }
                intTrip |= temp << i * 8;
            }
            odo = intOdo;
            trip = intTrip;
        } else if (id == 3) {
            trip = 0.0;
            tripPacketTimer = 0;
        }
    }

    @Override
    public void update() {
        super.update();
        if (!getCart().level().isClientSide && getCart().getCartRider() != null && getCart().getCartRider() instanceof Player) {
            if (enginePacketTimer == 0) {
                sendEnginePacket((Player) getCart().getCartRider());
                enginePacketTimer = 15;
            } else {
                enginePacketTimer--;
            }
            if (tripPacketTimer == 0) {
                sendTripPacket((Player) getCart().getCartRider());
                tripPacketTimer = 500;
            } else {
                tripPacketTimer--;
            }
        } else {
            enginePacketTimer = 0;
            tripPacketTimer = 0;
        }

        if (getCart().level().isClientSide) {
            encodeKeys();
        }
        if (!lastBackKey && isBackKeyDown()) {
            turnback();
        }
        lastBackKey = isBackKeyDown();

        if (!getCart().level().isClientSide) {
            if (speedChangeCooldown == 0) {
                if (!isJumpKeyDown() || !isControlKeyDown()) {
                    if (isJumpKeyDown()) {
                        setSpeedSetting(getSpeedSetting() + 1);
                        speedChangeCooldown = 8;
                    } else if (isControlKeyDown()) {
                        setSpeedSetting(getSpeedSetting() - 1);
                        speedChangeCooldown = 8;
                    } else {
                        speedChangeCooldown = 0;
                    }
                }
            } else {
                --speedChangeCooldown;
            }
            if (isForwardKeyDown() && isLeftKeyDown() && isRightKeyDown() && getCart().getCartRider() != null && getCart().getCartRider() instanceof Player) {
                getCart().getCartRider().startRiding(getCart());
                keyinformation = 0;
            }
        }
        final double x = getCart().x() - lastPosX;
        final double y = getCart().y() - lastPosY;
        final double z = getCart().z() - lastPosZ;
        lastPosX = getCart().x();
        lastPosY = getCart().y();
        lastPosZ = getCart().z();
        final double dist = Math.sqrt(x * x + y * y + z * z);
        if (!first) {
            odo += dist;
            trip += dist;
        } else {
            first = false;
        }
    }

    @Override
    public double getPushFactor() {
        switch (getSpeedSetting()) {
            case 1 -> {
                return 0.01;
            }
            case 2 -> {
                return 0.03;
            }
            case 3 -> {
                return 0.05;
            }
            case 4 -> {
                return 0.07;
            }
            case 5 -> {
                return 0.09;
            }
            case 6 -> {
                return 0.11;
            }
            default -> {
                return super.getPushFactor();
            }
        }
    }

    private void encodeKeys() {
        if (getCart().getCartRider() != null && getCart().getCartRider() instanceof Player && getCart().getCartRider() == getClientPlayer()) {
            final Minecraft minecraft = Minecraft.getInstance();
            final byte oldVal = keyinformation;
            keyinformation = 0;
            keyinformation |= (byte) ((minecraft.options.keyUp.isDown() ? 1 : 0) << 0);
            keyinformation |= (byte) ((minecraft.options.keyLeft.isDown() ? 1 : 0) << 1);
            keyinformation |= (byte) ((minecraft.options.keyRight.isDown() ? 1 : 0) << 2);
            keyinformation |= (byte) ((minecraft.options.keyDown.isDown() ? 1 : 0) << 3);
            keyinformation |= (byte) ((minecraft.options.keyJump.isDown() ? 1 : 0) << 4);
            keyinformation |= (byte) ((minecraft.options.keySprint.isDown() ? 1 : 0) << 5);
            if (oldVal != keyinformation) {
                sendPacket(1, new byte[]{keyinformation});
            }
        }
    }


    private boolean isForwardKeyDown() {
        return (keyinformation & 0x1) != 0x0;
    }

    private boolean isLeftKeyDown() {
        return (keyinformation & 0x2) != 0x0;
    }

    private boolean isRightKeyDown() {
        return (keyinformation & 0x4) != 0x0;
    }

    private boolean isBackKeyDown() {
        return (keyinformation & 0x8) != 0x0;
    }

    private boolean isJumpKeyDown() {
        return (keyinformation & 0x10) != 0x0;
    }

    private boolean isControlKeyDown() {
        return (keyinformation & 0x20) != 0x0;
    }

    private void sendTripPacket(final Player player) {
        final byte[] data = new byte[8];
        final int intOdo = (int) odo;
        final int intTrip = (int) trip;
        for (int i = 0; i < 4; ++i) {
            data[i] = (byte) ((intOdo & 255 << i * 8) >> i * 8);
            data[i + 4] = (byte) ((intTrip & 255 << i * 8) >> i * 8);
        }
        sendPacket(2, data, player);
    }

    private void sendEnginePacket(final Player player) {
        int engineCount = getCart().getEngines().size();
        byte[] data = new byte[engineCount * 2];
        for (int i = 0; i < getCart().getEngines().size(); ++i) {
            ModuleEngine engine = getCart().getEngines().get(i);
            int totalfuel = engine.getTotalFuel();
            int fuelInTopBar = 20000;
            int maxBarLength = 62;
            float percentage = (totalfuel % fuelInTopBar) / (float)fuelInTopBar;
            int upperBarLength = (int) (maxBarLength * percentage);
            int lowerBarLength = totalfuel / fuelInTopBar;
            if (lowerBarLength > maxBarLength) {
                lowerBarLength = maxBarLength;
            }
            data[i * 2] = (byte) (upperBarLength & 0x3F);
            data[i * 2 + 1] = (byte) (lowerBarLength & 0x3F);
        }
        sendPacket(0, data, player);
    }

    @Override
    public int numberOfPackets() {
        return 4;
    }

    private void setSpeedSetting(final int val) {
        if (val < 0 || val > 6) {
            return;
        }
        updateDw(SPEED, val);
    }

    private int getSpeedSetting() {
        if (isPlaceholder()) {
            return 1;
        }
        return getDw(SPEED);
    }

    @Override
    public int numberOfDataWatchers() {
        return 1;
    }

    @Override
    public void initDw() {
        SPEED = createDw(EntityDataSerializers.INT);
        registerDw(SPEED, 0);
    }

    @Override
    public boolean stopEngines() {
        return getSpeedSetting() == 0;
    }

    @Override
    public int getConsumption(final boolean isMoving) {
        if (!isMoving) {
            return super.getConsumption(isMoving);
        }
        switch (getSpeedSetting()) {
            case 4: {
                return 1;
            }
            case 5: {
                return 3;
            }
            case 6: {
                return 5;
            }
            default: {
                return super.getConsumption(isMoving);
            }
        }
    }

    @Override
    public void drawBackground(GuiGraphics guiGraphics, GuiMinecart gui, final int x, final int y) {
        ResourceHelper.bindResource("/gui/advlever.png");
        if (inRect(x, y, buttonRect)) {
            drawImage(guiGraphics, gui, buttonRect, 0, buttonRect[3]);
        } else {
            drawImage(guiGraphics, gui, buttonRect, 0, 0);
        }
    }

    @Override
    public void drawMouseOver(GuiGraphics guiGraphics, GuiMinecart gui, final int x, final int y) {
        drawStringOnMouseOver(guiGraphics, gui, Localization.MODULES.ATTACHMENTS.CONTROL_RESET.translate(), x, y, buttonRect);
    }

    @Override
    public void mouseClicked(final GuiMinecart gui, final int x, final int y, final int button) {
        if (button == 0 && inRect(x, y, buttonRect)) {
            sendPacket(3);
        }
    }

    @Override
    public void drawForeground(GuiGraphics guiGraphics, GuiMinecart gui) {
        drawString(guiGraphics, gui, Localization.MODULES.ATTACHMENTS.CONTROL_SYSTEM.translate(), 8, 6, 4210752);
    }

    @Override
    protected void Save(final CompoundTag tagCompound, final int id) {
        tagCompound.putByte(generateNBTName("Speed", id), (byte) getSpeedSetting());
        tagCompound.putDouble(generateNBTName("ODO", id), odo);
        tagCompound.putDouble(generateNBTName("TRIP", id), trip);
    }

    @Override
    protected void Load(final CompoundTag tagCompound, final int id) {
        setSpeedSetting(tagCompound.getByte(generateNBTName("Speed", id)));
        odo = tagCompound.getDouble(generateNBTName("ODO", id));
        trip = tagCompound.getDouble(generateNBTName("TRIP", id));
    }

    public float getWheelAngle() {
        if (!isForwardKeyDown()) {
            if (isLeftKeyDown()) {
                return 0.3926991f;
            }
            if (isRightKeyDown()) {
                return -0.3926991f;
            }
        }
        return 0.0f;
    }

    @Override
    public float getLeverState() {
        if (isPlaceholder()) {
            return 0.0f;
        }
        return getSpeedSetting() / 6.0f;
    }

    @Override
    public void postUpdate() {
        if (this.getCart().level().isClientSide && this.getCart().getCartRider() != null && this.getCart().getCartRider() instanceof Player && this.getCart().getCartRider() == this.getClientPlayer()) {
            //TODO
            //			KeyBinding.setKeyBindState(Minecraft.getMinecraft().gameSettings.keyBindSprint.getKeyCode(), false);
            //			KeyBinding.setKeyBindState(Minecraft.getMinecraft().gameSettings.keyBindJump.getKeyCode(), false);
        }
    }
}
