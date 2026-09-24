package vswe.stevescarts.entities;

import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.minecart.AbstractMinecart;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.RailShape;
import net.minecraft.world.level.gamerules.GameRules;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.entity.IEntityWithComplexSpawn;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Math;
import vswe.stevescarts.api.StevesCartsAPI;
import vswe.stevescarts.api.carts.CartLinkResult;
import vswe.stevescarts.api.carts.CartTrain;
import vswe.stevescarts.api.events.CartEvents;
import vswe.stevescarts.api.modules.ModuleBase;
import vswe.stevescarts.api.modules.data.ModuleData;
import vswe.stevescarts.api.modules.template.ModuleEngine;
import vswe.stevescarts.api.modules.template.ModuleWorker;
import vswe.stevescarts.blocks.tileentities.TileEntityCartAssembler;
import vswe.stevescarts.containers.ContainerMinecart;
import vswe.stevescarts.helpers.ModuleCountPair;
import vswe.stevescarts.init.ModBlocks;
import vswe.stevescarts.init.ModEntities;
import vswe.stevescarts.items.CartLinking;
import vswe.stevescarts.modules.realtimers.ModuleSeat;
import vswe.stevescarts.modules.storages.tanks.ModuleTank;
import vswe.stevescarts.polylib.DataEntity;
import vswe.stevescarts.polylib.EntityData;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.ArrayDeque;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

/**
 * Created by brandon3055 on 07/12/2024
 */
public class ModularMinecart extends AbstractMinecart implements IEntityWithComplexSpawn, MenuProvider, DataEntity, IModularCart, CartTrain {
    private static final double STOP_CENTER_EPSILON = 1.0 / 64.0;
    private static final double STOP_CENTER_SPEED = 0.125;
    private static final double LINK_DISTANCE = 1.6;
    private static final double LINK_CORRECTION = 0.35;
    private static final double MAX_LINK_CORRECTION_SPEED = 0.12;
    private static final double MAX_LINK_IMPULSE = 0.2;
    private static final int LINK_CONSTRAINT_ITERATIONS = 8;
    public static final int MODULAR_SPACE_WIDTH = 443;
    public static final int MODULAR_SPACE_HEIGHT = 168;
    public static final int[][][] railDirectionCoordinates = new int[][][]{
            {{0, 0, -1}, {0, 0, 1}},
            {{-1, 0, 0}, {1, 0, 0}},
            {{-1, -1, 0}, {1, 0, 0}},
            {{-1, 0, 0}, {1, -1, 0}},
            {{0, 0, -1}, {0, -1, 1}},
            {{0, -1, -1}, {0, 0, 1}},
            {{0, 0, 1}, {1, 0, 0}},
            {{0, 0, 1}, {-1, 0, 0}},
            {{0, 0, -1}, {-1, 0, 0}},
            {{0, 0, -1}, {1, 0, 0}}};
    private static final EntityDataAccessor<Boolean> IS_BURNING = SynchedEntityData.defineId(ModularMinecart.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> IS_DISANABLED = SynchedEntityData.defineId(ModularMinecart.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Integer> LINKED_CART_0 = SynchedEntityData.defineId(ModularMinecart.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> LINKED_CART_1 = SynchedEntityData.defineId(ModularMinecart.class, EntityDataSerializers.INT);
    private static final List<EntityDataAccessor<Integer>> TRAIN_CART_IDS = List.of(
            SynchedEntityData.defineId(ModularMinecart.class, EntityDataSerializers.INT),
            SynchedEntityData.defineId(ModularMinecart.class, EntityDataSerializers.INT),
            SynchedEntityData.defineId(ModularMinecart.class, EntityDataSerializers.INT),
            SynchedEntityData.defineId(ModularMinecart.class, EntityDataSerializers.INT),
            SynchedEntityData.defineId(ModularMinecart.class, EntityDataSerializers.INT),
            SynchedEntityData.defineId(ModularMinecart.class, EntityDataSerializers.INT),
            SynchedEntityData.defineId(ModularMinecart.class, EntityDataSerializers.INT),
            SynchedEntityData.defineId(ModularMinecart.class, EntityDataSerializers.INT)
    );
    protected final List<ChunkPos> forcedChunks = new ArrayList<>();
    private final ArrayList<ModuleCountPair> moduleCounts = new ArrayList<>();
    private final ArrayList<ModuleBase> modules = new ArrayList<>();
    private final ArrayList<ModuleWorker> workModules = new ArrayList<>();
    private final ArrayList<ModuleEngine> engineModules = new ArrayList<>();
    private final ArrayList<ModuleTank> tankModules = new ArrayList<>();
    private final List<EntityData<?>> entityDataList = new ArrayList<>();
    private final List<UUID> linkedCartIds = new ArrayList<>(MAX_DIRECT_LINKS);
    private long lastLinkUpdateGameTime = Long.MIN_VALUE;
    public boolean canScrollModules;
    public int modularSpaceHeight;
    protected TileEntityCartAssembler placeholderAsssembler;
    protected List<Identifier> moduleLoadingData;
    protected ModuleWorker workingComponent;
    protected boolean hasCreativeSupplies;
    protected boolean isPlaceholder;
    protected boolean wasDisabled;
    protected BlockPos disabledPos;
    protected boolean fullStop = false;
    protected int workingTime;
    protected int motorRotation;
    protected int keepAlive;
    /**
     * When the cart needs to stop to perform a cart function, the current velocity is stored in this field,
     * So that it can be restored after the operation is complete.
     */
    protected Vec3 preStopVelocity = null;
    //These two fields are used to control what happens on entering a junction track.
    protected RailShape fixedRailDirection;
    protected BlockPos fixedRailPos;
    protected Component name; //TODO, Is this actually used?
    //Client/Gui fields
    protected int scrollY;
    private float rotationOffset;
    private float playerRotationOffset;

    public ModularMinecart(Level level, double x, double y, double z) {
        super(ModEntities.MODULAR_CART.get(), level, x, y, z);
        behavior = new ModularMinecartBehavior(this);
    }

    protected ModularMinecart(Level level) {
        this(ModEntities.MODULAR_CART.get(), level);
    }

    public ModularMinecart(Level world, TileEntityCartAssembler assembler, ArrayList<Identifier> data) {
        this(world);
        // Placeholder carts are rendered in the assembler without ever being added to
        // the level. Since 26.3, entities do not receive an ID until they are added, but
        // entity render-state extraction requires one.
        setId(Integer.MIN_VALUE);
        setPlaceholder(assembler);
        loadPlaceHolderModules(data);
    }

    // Old Module Load

    public ModularMinecart(Level world, double x, double y, double z, CompoundTag data) {
        this(world, x, y, z);
        _oldLoadModules(data);
    }

    public ModularMinecart(EntityType<?> entityType, Level level) {
        super(entityType, level);
        behavior = new ModularMinecartBehavior(this);
    }

    // ===============

    public static Optional<BlockPos> readBlockPos(CompoundTag tag, String key) {
        int[] aint = tag.getIntArray(key).orElseGet(() -> new int[0]);
        return aint.length == 3 ? Optional.of(new BlockPos(aint[0], aint[1], aint[2])) : Optional.empty();
    }

    //=== Cart Stuff ===//

    private void _oldLoadModules(CompoundTag info) {
        List<CompoundTag> modules = new ArrayList<>();
        if (info == null) return;

        List<Identifier> names = new ArrayList<>();
        ListTag listTag = (ListTag) info.get("modules");
        for (int i = 0; i < listTag.size(); i++) {
            Tag tag = listTag.get(i);
            modules.add((CompoundTag) tag);
            names.add(Identifier.parse(((CompoundTag) tag).getStringOr(String.valueOf(i), "")));
        }

        if (!names.isEmpty()) {
            getCart().moduleLoadingData = names;
        }
        _oldLoadModules(modules);
    }

    private void _oldLoadModules(List<CompoundTag> data) {
        modules().clear();
        if (data != null) {
            for (int i = 0; i < data.size(); i++) {
                CompoundTag tag = data.get(i);
                Identifier name = Identifier.parse(tag.getStringOr(String.valueOf(i), ""));
                doLoadModules(StevesCartsAPI.getModule(name), tag);
            }
        }
        initModules();
    }

    @Override
    public void tick() {
        if (!level().isClientSide()) {
            entityDataList.forEach(EntityData::detectAndSend);
            if (isDisabled() && disabledPos != null) {
                if (!fullStop) {
                    double xOffset = (disabledPos.getX() + 0.5) - position().x;
                    double zOffset = (disabledPos.getZ() + 0.5) - position().z;
                    Vec3 offset = new Vec3(xOffset, 0, zOffset);
                    double distance = offset.horizontalDistance();
                    if (distance <= STOP_CENTER_EPSILON) {
                        move(MoverType.SELF, offset);
                        fullStop = true;
                    } else {
                        move(MoverType.SELF, offset.scale(Math.min(STOP_CENTER_SPEED, distance) / distance));
                    }
                }
            } else {
                fullStop = false;
            }
        }
        flipped = true;
        onCartUpdate();
        if (level().isClientSide()) {
            updateSounds();
        }

        double lastYRot = this.getYRot();
        Vec3 lastPos = this.position();
        super.tick();
        if (this.level() instanceof ServerLevel serverLevel) {
            updateCartLinks(serverLevel);
        }
        if (this.level().isClientSide() && lastPos.distanceTo(this.position()) > 0.01) {
            this.rotationOffset += (float) ((this.getYRot() - lastYRot) % 360.0);
            this.rotationOffset %= 360.0F;
        }
        if (this.level().isClientSide() && getPassengers().stream().noneMatch(Player.class::isInstance)) {
            this.playerRotationOffset = this.rotationOffset;
        }
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.@NotNull Builder builder) {
        super.defineSynchedData(builder);
        builder.define(IS_BURNING, false);
        builder.define(IS_DISANABLED, false);
        builder.define(LINKED_CART_0, -1);
        builder.define(LINKED_CART_1, -1);
        for (EntityDataAccessor<Integer> trainCartId : TRAIN_CART_IDS) {
            builder.define(trainCartId, -1);
        }
    }

    @Override
    public ItemStack getPickResult() {
        return getCartItem();
    }

    @Override
    protected Item getDropItem() {
        return Items.AIR;
    }

    @Nonnull
    public ItemStack getCartItem() {
        return ModuleData.createModularCart(this);
    }

    @Override
    public boolean canBeCollidedWith(@Nullable Entity entity) {
        return !isPlaceholder() && super.canBeCollidedWith(entity);
    }

    @Override
    public void push(Entity entity) {
        if (entity instanceof ModularMinecart other && isLinkedTo(other)) {
            return;
        }
        super.push(entity);
    }

    @Override
    public boolean canRiderInteract() {
        return true;
    }

    public Entity getCartRider() {
        return getPassengers().isEmpty() ? null : getPassengers().get(0);
    }

    @Nullable
    @Override
    public LivingEntity getControllingPassenger() {
        return null; //Works when returning null, not sure why //TODO, Look into this
    }

    //Override this to stop it spawning a vanilla minecart
    @Override
    protected void destroy(ServerLevel level, DamageSource damageSource) {
        int returnedChains = unlinkAllCarts();
        this.kill(level);
        if (!level.getGameRules().get(GameRules.ENTITY_DROPS)) {
            return;
        }
        if (returnedChains > 0) {
            this.spawnAtLocation(level, new ItemStack(Items.IRON_CHAIN, returnedChains));
        }
        if (dropOnDeath()) {
            ItemStack itemstack = getCartItem();
            if (this.hasCustomName()) {
                itemstack.set(DataComponents.CUSTOM_NAME, getCustomName());
            }
            for (int i = 0; i < getContainerSize(); i++) {
                ItemStack stack = getItem(i);
                if (!stack.isEmpty()) {
                    this.spawnAtLocation(level, stack);
                }
            }
            this.spawnAtLocation(level, itemstack);
        }
    }

    @Override
    public boolean hurtServer(ServerLevel serverLevel, DamageSource dmg, float damage) {
        if (isPlaceholder()) {
            return false;
        }
        for (ModuleBase module : modules()) {
            if (!module.receiveDamage(dmg, damage)) {
                return false;
            }
        }
        return super.hurtServer(serverLevel, dmg, damage);
    }

    @Override
    public void activateMinecart(ServerLevel level, int x, int y, int z, boolean active) {
        for (ModuleBase module : modules()) {
            module.activatedByRail(x, y, z, active);
        }
    }

    public boolean isDisabled() {
        return entityData.get(IS_DISANABLED);
    }

    public void setIsDisabled(final boolean disabled) {
        entityData.set(IS_DISANABLED, disabled);
    }

    public boolean isEngineBurning() {
        return entityData.get(IS_BURNING);
    }

    //=== Cart Motion Handling ===//

    public void setEngineBurning(final boolean on) {
        entityData.set(IS_BURNING, on);
    }

    @Override
    public @NotNull Component getDisplayName() {
        return Component.translatable("entity.minecraft.minecart");
    }

    /**
     * Returns the carts velocity vector, or what the pre-stop velocity vector if that cart is stopped.
     *
     */
    public Vec3 getEffectiveVelocity() {
        float rotation = behavior.getYRot();
        float pushRad = (rotation + 90) * 0.017453292F;
        float sin = Math.sin(pushRad);
        float cos = Math.cosFromSin(sin, pushRad);
        return new Vec3(sin, 0, cos);
    }

    @Override
    protected Vec3 applyNaturalSlowdown(Vec3 initialVelocity) {
        if (isDisabled()) {
            return Vec3.ZERO;
        }
        //If engine is burning and we are not stopped, then apply engine power.
        if (isEngineBurning() && preStopVelocity == null) {
            double pushFactor = getPushFactor();
            Vec3 velocity = getEffectiveVelocity();
            initialVelocity = initialVelocity.multiply(0.8D, 0.0D, 0.8D).add(velocity.multiply(pushFactor, 0, pushFactor));
            if (isInWater()) {
                initialVelocity = initialVelocity.scale(0.1);
            }
        }
        return super.applyNaturalSlowdown(initialVelocity);
    }

    public void handleMoveAlongTrack(BlockPos pos, BlockState state) {
        for (ModuleBase module : modules()) {
            module.moveMinecartOnRail(pos, state);
        }

        if (state.getBlock() != ModBlocks.ADVANCED_DETECTOR.get() && isDisabled()) {
            releaseCart();
        }
        boolean canBeDisabled = state.getBlock() == ModBlocks.ADVANCED_DETECTOR.get();// && (stateBelow.getBlock() != ModBlocks.DETECTOR_UNIT.getBlock() || !DetectorType.getTypeFromSate(stateBelow).canInteractWithCart() || DetectorType.getTypeFromSate(stateBelow).shouldStopCart());
        boolean forceUnDisable = wasDisabled && disabledPos != null && disabledPos.equals(pos);
        if (!forceUnDisable && wasDisabled) {
            wasDisabled = false;
        }
        canBeDisabled = (!forceUnDisable && canBeDisabled);
        if (canBeDisabled && !isDisabled()) {
            preStopVelocity = getDeltaMovement();
            disabledPos = pos.immutable();
            fullStop = false;
            setIsDisabled(true);
        }
        if (fixedRailPos != null && !fixedRailPos.equals(pos)) {
            fixedRailDirection = null;
            fixedRailPos = new BlockPos(fixedRailPos.getX(), -1, fixedRailPos.getZ());
        }
    }

    public void turnback() {
        behavior.setYRot((behavior.getYRot() + 180) % 360);
        setDeltaMovement(getDeltaMovement().multiply(-1, -1, -1));
    }

    public void releaseCart() {
        wasDisabled = true;
        setIsDisabled(false);
        fullStop = false;
        if (preStopVelocity != null) {
            behavior.setDeltaMovement(preStopVelocity);
        }
        preStopVelocity = null;
    }

    @Override
    protected void comeOffTrack(ServerLevel level) {
        super.comeOffTrack(level);
    }

    @Override
    protected double getMaxSpeed(ServerLevel level) {
        return super.getMaxSpeed(level);
    }

    //=== Modules ===//

    @Override
    protected double makeStepAlongTrack(BlockPos pos, RailShape shape, double distance) {
        return super.makeStepAlongTrack(pos, shape, distance);
    }

    @Override
    protected void moveAlongTrack(ServerLevel level) {
        super.moveAlongTrack(level);
    }

    @Override
    public ModularMinecart getCart() {
        return this;
    }

    @Override
    public ArrayList<ModuleBase> modules() {
        return modules;
    }

    @Override
    public ArrayList<ModuleWorker> workers() {
        return workModules;
    }

    @Override
    public ArrayList<ModuleEngine> engines() {
        return engineModules;
    }

    @Override
    public ArrayList<ModuleTank> moduleTanks() {
        return tankModules;
    }

    @Override
    public ArrayList<ModuleCountPair> moduleCounts() {
        return moduleCounts;
    }

    @Override
    public boolean isPlaceholder() {
        return isPlaceholder;
    }

    @Override
    public void setPlaceholder(TileEntityCartAssembler assembler) {
        isPlaceholder = true;
        placeholderAsssembler = assembler;
    }

    @Override
    public ModuleWorker getWorker() {
        return workingComponent;
    }

    //=== Interact ===//

    @Override
    public void setWorker(ModuleWorker worker) {
        if (workingComponent != null && worker != null) {
            workingComponent.stopWorking();
        }
        if ((workingComponent = worker) == null) {
            setWorkingTime(0);
        }
    }

    //=== Data ===//

    @Override
    public void setWorkingTime(int val) {
        workingTime = val;
    }



    @Override
    public @NotNull InteractionResult interact(@NotNull Player player, @NotNull InteractionHand hand, @NotNull Vec3 vec) {
        if (isPlaceholder()) {
            return InteractionResult.FAIL;
        }
        ItemStack heldItem = player.getItemInHand(hand);
        if (heldItem.is(Items.IRON_CHAIN)) {
            if (player instanceof ServerPlayer serverPlayer) {
                return CartLinking.interactWithCart(heldItem, serverPlayer, this);
            }
            return InteractionResult.SUCCESS;
        }
        if (heldItem.is(Items.SHEARS)) {
            if (player instanceof ServerPlayer serverPlayer) {
                return CartLinking.cutCartLinks(heldItem, serverPlayer, this, hand);
            }
            return InteractionResult.SUCCESS;
        }
        if (!player.isCrouching()) {
            boolean interupt = false;
            for (ModuleBase module : modules()) {
                if (module.onInteractFirst(player)) {
                    interupt = true;
                }
            }
            if (interupt) {
                return InteractionResult.SUCCESS;
            }
        }
        if (player instanceof ServerPlayer) {
            player.openMenu(this, packetBuffer -> packetBuffer.writeInt(getId()));
        }
        return InteractionResult.SUCCESS;
    }

    @Override
    public boolean shouldTriggerClientSideContainerClosingOnOpen() {
        return false;
    }

    @Override
    public void remove(@NotNull Entity.RemovalReason removalReason) {
        CartEvents.CartRemovedEvent event = new CartEvents.CartRemovedEvent(this);
        NeoForge.EVENT_BUS.post(event);
        if (event.isCanceled()) return;

        if (level().isClientSide()) {
            for (int var1 = 0; var1 < getContainerSize(); ++var1) {
                setItem(var1, ItemStack.EMPTY);
            }
        }
        super.remove(removalReason);
        for (ModuleBase module : modules) {
            module.onDeath();
        }
        dropChunkLoading();
    }

    @Override
    public void removeVehicle() {
        if (!level().isClientSide()) {
            if (!isRemoved()) {
                level().addFreshEntity(new ItemEntity(level(), blockPosition().getX() + 0.5, blockPosition().getY() + 0.5, blockPosition().getZ() + 0.5, getCartItem()));
            }
        }
        super.removeVehicle();
    }

    @Override
    public void writeSpawnData(RegistryFriendlyByteBuf data) {
        if (moduleLoadingData == null) {
            data.writeByte(0);
            return;
        }
        data.writeByte(moduleLoadingData.size());
        for (Identifier b : moduleLoadingData) {
            data.writeIdentifier(b);
        }
        entityDataList.forEach(e -> e.toBytes(data));
        data.writeBoolean(isEngineBurning());
        data.writeBoolean(isDisabled());
    }

    @Override
    public void readSpawnData(RegistryFriendlyByteBuf data) {
        byte length = data.readByte();
        if (length == 0) {
            return;
        }
        List<Identifier> list = new ArrayList<>();
        for (int i = 0; i < length; i++) {
            list.add(data.readIdentifier());
        }
        loadModulesFromNames(list);
        entityDataList.forEach(e -> e.fromBytes(data));
        setEngineBurning(data.readBoolean());
        setIsDisabled(data.readBoolean());
    }

    @Override
    protected void addAdditionalSaveData(ValueOutput output) {
        super.addAdditionalSaveData(output);
        if (name != null) output.putString("cartName", name.getString());
        output.putBoolean("engine_burning", isEngineBurning());
        output.putBoolean("disabled", isDisabled());
        output.putBoolean("stopped", preStopVelocity != null);
        if (preStopVelocity != null) {
            output.putDouble("preStopVelX", preStopVelocity.x());
            output.putDouble("preStopVelY", preStopVelocity.y());
            output.putDouble("preStopVelZ", preStopVelocity.z());
        }
        output.putShort("workingTime", (short) workingTime);
        if (disabledPos != null) {
            output.store("disabled_pos", BlockPos.CODEC, disabledPos);
        }
        for (int i = 0; i < linkedCartIds.size(); i++) {
            output.putString("linked_cart_" + i, linkedCartIds.get(i).toString());
        }

        ValueOutput list = output.child("modules");
        list.putInt("count", modules.size());
        for (int i = 0; i < modules.size(); i++) {
            ModuleBase module = modules.get(i);
            list.putString(String.valueOf(i), modules.get(i).getModuleId().toString());
            module.writeToNBT(list, i);
        }
    }

    @Override
    protected void readAdditionalSaveData(ValueInput input) {
        super.readAdditionalSaveData(input);
        name = Component.translatable(input.getStringOr("cartName", ""));
        setEngineBurning(input.getBooleanOr("engine_burning", false));
        setIsDisabled(input.getBooleanOr("disabled", false));
        if (input.getBooleanOr("stopped", false)) {
            preStopVelocity = new Vec3(input.getDoubleOr("preStopVelX", 0), input.getDoubleOr("preStopVelY", 0), input.getDoubleOr("preStopVelZ", 0));
        } else {
            preStopVelocity = null;
        }
        workingTime = input.getShortOr("workingTime", (short) 0);
        disabledPos = input.read("disabled_pos", BlockPos.CODEC).orElse(null);
        linkedCartIds.clear();
        for (int i = 0; i < MAX_DIRECT_LINKS; i++) {
            String id = input.getStringOr("linked_cart_" + i, "");
            if (!id.isEmpty()) {
                try {
                    linkedCartIds.add(UUID.fromString(id));
                } catch (IllegalArgumentException ignored) {
                }
            }
        }

        loadModules(input);
    }

    @Override
    public CartLinkResult linkCart(ModularMinecart other) {
        if (other == null || other == this || !isAlive() || !other.isAlive()) {
            return CartLinkResult.INVALID_CART;
        }
        if (level() != other.level()) {
            return CartLinkResult.DIFFERENT_LEVEL;
        }
        if (!(level() instanceof ServerLevel)) {
            return CartLinkResult.INVALID_CART;
        }
        if (isLinkedTo(other) || isConnectedTo(other)) {
            return CartLinkResult.ALREADY_CONNECTED;
        }
        if (linkedCartIds.size() >= MAX_DIRECT_LINKS || other.linkedCartIds.size() >= MAX_DIRECT_LINKS) {
            return CartLinkResult.NO_FREE_LINK;
        }
        if (getTrainSize() + other.getTrainSize() > MAX_TRAIN_CARTS) {
            return CartLinkResult.TRAIN_FULL;
        }

        CartEvents.CartLinkEvent event = new CartEvents.CartLinkEvent(this, other);
        NeoForge.EVENT_BUS.post(event);
        if (event.isCanceled()) {
            return CartLinkResult.CANCELLED;
        }

        linkedCartIds.add(other.getUUID());
        other.linkedCartIds.add(getUUID());
        return CartLinkResult.LINKED;
    }

    @Override
    public boolean unlinkCart(ModularMinecart other) {
        if (other == null || other == this || level() != other.level() || level().isClientSide()) {
            return false;
        }
        boolean removed = linkedCartIds.remove(other.getUUID());
        boolean removedOther = other.linkedCartIds.remove(getUUID());
        if (removed || removedOther) {
            clearSyncedCartLinks();
            other.clearSyncedCartLinks();
            NeoForge.EVENT_BUS.post(new CartEvents.CartUnlinkEvent(this, other));
            return true;
        }
        return false;
    }

    @Override
    public int unlinkAllCarts() {
        int linkCount = linkedCartIds.size();
        if (level() instanceof ServerLevel serverLevel) {
            for (UUID linkedId : List.copyOf(linkedCartIds)) {
                Entity entity = serverLevel.getEntity(linkedId);
                if (entity instanceof ModularMinecart linkedCart) {
                    unlinkCart(linkedCart);
                } else {
                    linkedCartIds.remove(linkedId);
                }
            }
        }
        linkedCartIds.clear();
        clearSyncedCartLinks();
        return linkCount;
    }

    private void clearSyncedCartLinks() {
        entityData.set(LINKED_CART_0, -1);
        entityData.set(LINKED_CART_1, -1);
        for (EntityDataAccessor<Integer> trainCartId : TRAIN_CART_IDS) {
            entityData.set(trainCartId, -1);
        }
    }

    public int getLinkedCartEntityId(int index) {
        if (index < 0 || index >= MAX_DIRECT_LINKS) {
            throw new IndexOutOfBoundsException("A cart only has " + MAX_DIRECT_LINKS + " direct link slots");
        }
        return entityData.get(index == 0 ? LINKED_CART_0 : LINKED_CART_1);
    }

    @Override
    public List<Integer> getLinkedCartEntityIds() {
        List<Integer> ids = new ArrayList<>(MAX_DIRECT_LINKS);
        for (int i = 0; i < MAX_DIRECT_LINKS; i++) {
            int id = getLinkedCartEntityId(i);
            if (id >= 0) {
                ids.add(id);
            }
        }
        return List.copyOf(ids);
    }

    @Override
    public List<Integer> getTrainCartEntityIds() {
        List<Integer> ids = new ArrayList<>(MAX_TRAIN_CARTS);
        for (EntityDataAccessor<Integer> trainCartId : TRAIN_CART_IDS) {
            int id = entityData.get(trainCartId);
            if (id >= 0) {
                ids.add(id);
            }
        }
        return List.copyOf(ids);
    }

    @Override
    public List<UUID> getLinkedCartIds() {
        return List.copyOf(linkedCartIds);
    }

    @Override
    public List<ModularMinecart> getDirectlyLinkedCarts() {
        if (!(level() instanceof ServerLevel serverLevel)) {
            return List.of();
        }
        return linkedCartIds.stream()
                .map(serverLevel::getEntity)
                .filter(ModularMinecart.class::isInstance)
                .map(ModularMinecart.class::cast)
                .filter(Entity::isAlive)
                .toList();
    }

    @Override
    public List<ModularMinecart> getConnectedCarts() {
        if (!(level() instanceof ServerLevel serverLevel)) {
            return List.of();
        }
        return List.copyOf(getConnectedCarts(serverLevel));
    }

    @Override
    public List<ModularMinecart> getTrainCarts() {
        if (level().isClientSide()) {
            return List.of();
        }
        List<ModularMinecart> carts = new ArrayList<>(getConnectedCarts());
        carts.add(this);
        carts.sort(Comparator.comparingInt(Entity::getId));
        return List.copyOf(carts);
    }

    @Override
    public int getTrainSize() {
        return level().isClientSide() ? java.lang.Math.max(1, getTrainCartEntityIds().size()) : getConnectedCarts().size() + 1;
    }

    @Override
    public boolean isLinkedTo(ModularMinecart other) {
        return other != null && linkedCartIds.contains(other.getUUID());
    }

    @Override
    public boolean isConnectedTo(ModularMinecart target) {
        if (target == null) {
            return false;
        }
        if (!(level() instanceof ServerLevel serverLevel)) {
            return false;
        }
        return getConnectedCarts(serverLevel).contains(target);
    }

    private List<ModularMinecart> getConnectedCarts(ServerLevel serverLevel) {
        List<ModularMinecart> connectedCarts = new ArrayList<>();
        Set<UUID> visited = new HashSet<>();
        ArrayDeque<UUID> pending = new ArrayDeque<>(linkedCartIds);
        visited.add(getUUID());
        while (!pending.isEmpty()) {
            UUID id = pending.removeFirst();
            if (!visited.add(id)) {
                continue;
            }
            Entity entity = serverLevel.getEntity(id);
            if (entity instanceof ModularMinecart linkedCart) {
                connectedCarts.add(linkedCart);
                pending.addAll(linkedCart.linkedCartIds);
            }
        }
        return connectedCarts;
    }

    private void updateCartLinks(ServerLevel serverLevel) {
        int[] linkedEntityIds = {-1, -1};
        List<UUID> links = List.copyOf(linkedCartIds);
        for (int i = 0; i < links.size(); i++) {
            UUID linkedId = links.get(i);
            Entity entity = serverLevel.getEntity(linkedId);
            if (!(entity instanceof ModularMinecart linkedCart) || !linkedCart.isAlive()) {
                continue;
            }
            linkedEntityIds[i] = linkedCart.getId();
            if (!linkedCart.linkedCartIds.contains(getUUID())) {
                if (linkedCart.linkedCartIds.size() >= MAX_DIRECT_LINKS) {
                    linkedCartIds.remove(linkedId);
                    continue;
                }
                linkedCart.linkedCartIds.add(getUUID());
            }
        }
        entityData.set(LINKED_CART_0, linkedEntityIds[0]);
        entityData.set(LINKED_CART_1, linkedEntityIds[1]);
        List<ModularMinecart> trainCarts = getTrainCarts();
        lastLinkUpdateGameTime = serverLevel.getGameTime();
        for (ModularMinecart trainCart : trainCarts) {
            for (int i = 0; i < TRAIN_CART_IDS.size(); i++) {
                trainCart.entityData.set(TRAIN_CART_IDS.get(i), i < trainCarts.size() ? trainCarts.get(i).getId() : -1);
            }
        }
        if (trainCarts.stream().allMatch(cart -> cart.lastLinkUpdateGameTime == serverLevel.getGameTime())) {
            applyTrainLinkConstraints(serverLevel, trainCarts);
        }
    }

    private static void applyTrainLinkConstraints(ServerLevel serverLevel, List<ModularMinecart> trainCarts) {
        for (int iteration = 0; iteration < LINK_CONSTRAINT_ITERATIONS; iteration++) {
            for (ModularMinecart cart : trainCarts) {
                for (UUID linkedId : cart.linkedCartIds) {
                    Entity entity = serverLevel.getEntity(linkedId);
                    if (entity instanceof ModularMinecart linkedCart && cart.getId() < linkedCart.getId()) {
                        cart.applyLinkConstraint(linkedCart);
                    }
                }
            }
        }
    }

    private void applyLinkConstraint(ModularMinecart linkedCart) {
        Vec3 offset = linkedCart.position().subtract(position());
        double distance = offset.length();
        if (distance < 1.0E-4) {
            return;
        }
        Vec3 direction = offset.scale(1.0 / distance);
        double relativeSpeed = linkedCart.getDeltaMovement().subtract(getDeltaMovement()).dot(direction);
        double correctionSpeed = java.lang.Math.clamp(
                (distance - LINK_DISTANCE) * LINK_CORRECTION,
                -MAX_LINK_CORRECTION_SPEED,
                MAX_LINK_CORRECTION_SPEED
        );
        double impulseStrength = java.lang.Math.clamp(
                (relativeSpeed + correctionSpeed) * 0.5,
                -MAX_LINK_IMPULSE,
                MAX_LINK_IMPULSE
        );
        Vec3 impulse = direction.scale(impulseStrength);
        setDeltaMovement(getDeltaMovement().add(impulse));
        linkedCart.setDeltaMovement(linkedCart.getDeltaMovement().subtract(impulse));
    }

    public void loadModules(ValueInput input) {
        List<Identifier> names = new ArrayList<>();

        //Data Migration
        ValueInput.ValueInputList migration = input.childrenListOrEmpty("modules");
        if (!migration.isEmpty()) {
            int i = 0;
            for (ValueInput in : migration) {
                String name = in.getStringOr(String.valueOf(i), "");
                if (name.isEmpty()) continue;
                Identifier moduleName = Identifier.parse(name);
                names.add(moduleName);
                i++;
            }
        }

        ValueInput list = input.childOrEmpty("modules");
        int count = list.getIntOr("count", 0);
        for (int i = 0; i < count; i++) {
            String name = list.getStringOr(String.valueOf(i), "");
            if (name.isEmpty()) continue;
            Identifier moduleName = Identifier.parse(name);
            names.add(moduleName);
        }

        if (!names.isEmpty()) {
            getCart().moduleLoadingData = names;
        }

        modules().clear();
        for (Identifier name : names) {
            doLoadModules(StevesCartsAPI.getModule(name), null);
        }
        initModules();

        for (int i = 0; i < modules.size(); ++i) {
            ModuleBase module = modules.get(i);
            module.readFromNBT(list, i);
        }
    }

    @Override
    public List<EntityData<?>> getEntityDataList() {
        return entityDataList;
    }


    //=== Random / Util stuff ===

    @Override
    public void registerEntityData(EntityData<?> data) {
        entityDataList.add(data);
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int id, Inventory inventory, Player player) {
        return new ContainerMinecart(id, inventory, this);
    }

    @Override
    protected void positionRider(Entity passenger, Entity.MoveFunction moveFunction) {
        super.positionRider(passenger, moveFunction);
        if (this.level().isClientSide()
                && passenger instanceof Player player
                && player.shouldRotateWithMinecart()
                && modules().stream().anyMatch(ModuleSeat.class::isInstance)) {
            float yRot = (float) Mth.rotLerp(0.5, this.playerRotationOffset, this.rotationOffset);
            player.setYRot(player.getYRot() - (yRot - this.playerRotationOffset));
            this.playerRotationOffset = yRot;
        }
    }

}
