package vswe.stevescarts.modules.realtimers;

import com.mojang.blaze3d.platform.InputConstants;
import net.creeperhost.polylib.data.serializable.BooleanData;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Input;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.Vec3;
import vswe.stevescarts.api.modules.ModuleBase;
import vswe.stevescarts.client.guis.GuiMinecart;
import vswe.stevescarts.entities.ModularMinecart;
import vswe.stevescarts.helpers.ResourceHelper;
import vswe.stevescarts.polylib.EntityData;

public class ModuleRocket extends ModuleBase {
    private static final double FLIGHT_SPEED = 0.35D;
    private static final double VERTICAL_SPEED = 0.3D;
    private static final double HORIZONTAL_ACCELERATION = 0.22D;
    private static final double VERTICAL_ACCELERATION = 0.3D;
    private static final double STOP_EPSILON = 1.0E-3D;
    private static final int FLIGHT_CONSUMPTION = 4;

    private final EntityData<Boolean> flying = new EntityData<>(getCart(), new BooleanData(false));
    private final EntityData<Boolean> flightEnabled = new EntityData<>(getCart(), new BooleanData(true));
    private final int[] buttonRect;

    public ModuleRocket(ModularMinecart cart) {
        super(cart);
        buttonRect = new int[]{20, 20, 24, 12};
    }

    @Override
    public void update() {
        if (isPlaceholder() || getCart().level().isClientSide()) {
            return;
        }

        boolean hasFlightFuel = isFlying() ? getCart().isEngineBurning() : getCart().hasFuelForModule();
        boolean canFly = isFlightEnabled()
                && getCart().getCartRider() instanceof ServerPlayer
                && getCart().hasModule(ModuleAdvControl.class)
                && !getCart().isDisabled()
                && hasFlightFuel;
        setFlying(canFly);
        if (!isFlying()) {
            return;
        }

        ServerPlayer player = (ServerPlayer) getCart().getCartRider();
        updateFlight(player, player.getLastClientInput());
    }

    private void updateFlight(ServerPlayer player, Input input) {
        double forwardInput = (input.forward() ? 1.0D : 0.0D) - (input.backward() ? 1.0D : 0.0D);
        double strafeInput = (input.right() ? 1.0D : 0.0D) - (input.left() ? 1.0D : 0.0D);
        double verticalInput = (input.jump() ? 1.0D : 0.0D) - (input.sprint() ? 1.0D : 0.0D);

        float yaw = player.getYRot();
        double yawRadians = yaw * Mth.DEG_TO_RAD;
        Vec3 forward = new Vec3(-Mth.sin((float) yawRadians), 0.0D, Mth.cos((float) yawRadians));
        Vec3 right = new Vec3(-forward.z(), 0.0D, forward.x());
        Vec3 horizontal = forward.scale(forwardInput).add(right.scale(strafeInput));
        if (horizontal.lengthSqr() > 1.0D) {
            horizontal = horizontal.normalize();
        }

        Vec3 targetMovement = horizontal.scale(FLIGHT_SPEED).add(0.0D, verticalInput * VERTICAL_SPEED, 0.0D);
        Vec3 movement = getCart().getDeltaMovement();
        Vec3 updatedMovement = new Vec3(
                Mth.lerp(HORIZONTAL_ACCELERATION, movement.x(), targetMovement.x()),
                Mth.lerp(VERTICAL_ACCELERATION, movement.y(), targetMovement.y()),
                Mth.lerp(HORIZONTAL_ACCELERATION, movement.z(), targetMovement.z())
        );
        if (Math.abs(updatedMovement.x()) < STOP_EPSILON) {
            updatedMovement = new Vec3(0.0D, updatedMovement.y(), updatedMovement.z());
        }
        if (Math.abs(updatedMovement.y()) < STOP_EPSILON) {
            updatedMovement = new Vec3(updatedMovement.x(), 0.0D, updatedMovement.z());
        }
        if (Math.abs(updatedMovement.z()) < STOP_EPSILON) {
            updatedMovement = new Vec3(updatedMovement.x(), updatedMovement.y(), 0.0D);
        }

        getCart().setYRot(270.0F - yaw);
        getCart().setXRot(0.0F);
        getCart().setDeltaMovement(updatedMovement);
        player.setYBodyRot(yaw);
    }

    private void setFlying(boolean flying) {
        this.flying.set(flying);
        getCart().setNoGravity(flying);
    }

    public boolean isFlying() {
        return flying.get();
    }

    public boolean isFlightEnabled() {
        return flightEnabled.get();
    }

    private void setFlightEnabled(boolean enabled) {
        flightEnabled.set(enabled);
        if (!enabled) {
            setFlying(false);
        }
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
        return 70;
    }

    @Override
    public int guiHeight() {
        return 35;
    }

    @Override
    public void drawForeground(GuiGraphicsExtractor graphics, GuiMinecart gui) {
        drawString(graphics, gui, getModuleName(), 8, 6, 4210752);
    }

    @Override
    public void drawBackground(GuiGraphicsExtractor graphics, GuiMinecart gui, int x, int y) {
        Identifier texture = ResourceHelper.getResource("/gui/lever.png");
        drawImage(graphics, texture, gui, buttonRect, 0, inRect(x, y, buttonRect) ? buttonRect[3] : 0);
        int imageId = isFlightEnabled() ? 1 : 2;
        int sourceY = buttonRect[3] * 2 + imageId * (buttonRect[3] - 2);
        drawImage(graphics, texture, gui, buttonRect[0] + 1, buttonRect[1] + 1, 0, sourceY,
                buttonRect[2] - 2, buttonRect[3] - 2);
    }

    @Override
    public void drawMouseOver(GuiGraphicsExtractor graphics, GuiMinecart gui, int x, int y) {
        String action = isFlightEnabled() ? "deactivate" : "activate";
        drawStringOnMouseOver(graphics, gui,
                Component.translatable("modules.addons.stevescarts.rocketToggle." + action).getString(),
                x, y, buttonRect);
    }

    @Override
    public void mouseClicked(GuiMinecart gui, int x, int y, int button) {
        if (button == InputConstants.MOUSE_BUTTON_LEFT && inRect(x, y, buttonRect)) {
            sendPacket(0);
        }
    }

    @Override
    protected void receivePacket(int id, byte[] data, Player player) {
        if (id == 0) {
            setFlightEnabled(!isFlightEnabled());
        }
    }

    @Override
    public int numberOfPackets() {
        return 1;
    }

    @Override
    public boolean controlsCartMovement() {
        return isFlying();
    }

    @Override
    public int getConsumption(boolean isMoving) {
        return isFlying() ? FLIGHT_CONSUMPTION : super.getConsumption(isMoving);
    }

    @Override
    protected void save(ValueOutput output, int id) {
        super.save(output, id);
        output.putBoolean(generateNBTName("RocketEnabled", id), isFlightEnabled());
    }

    @Override
    protected void load(ValueInput input, int id) {
        super.load(input, id);
        setFlightEnabled(input.getBooleanOr(generateNBTName("RocketEnabled", id), true));
    }
}
