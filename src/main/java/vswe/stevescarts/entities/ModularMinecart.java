package vswe.stevescarts.entities;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.*;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
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
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.RailShape;
import net.minecraft.world.level.storage.TagValueOutput;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.entity.IEntityWithComplexSpawn;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Math;
import vswe.stevescarts.StevesCarts;
import vswe.stevescarts.api.StevesCartsAPI;
import vswe.stevescarts.api.events.CartEvents;
import vswe.stevescarts.api.modules.ModuleBase;
import vswe.stevescarts.api.modules.data.ModuleData;
import vswe.stevescarts.api.modules.template.ModuleEngine;
import vswe.stevescarts.api.modules.template.ModuleWorker;
import vswe.stevescarts.blocks.tileentities.TileEntityCartAssembler;
import vswe.stevescarts.containers.ContainerMinecart;
import vswe.stevescarts.helpers.Localization;
import vswe.stevescarts.helpers.ModuleCountPair;
import vswe.stevescarts.init.ModBlocks;
import vswe.stevescarts.init.ModEntities;
import vswe.stevescarts.init.ModItemData;
import vswe.stevescarts.modules.storages.tanks.ModuleTank;
import vswe.stevescarts.polylib.DataEntity;
import vswe.stevescarts.polylib.EntityData;
import vswe.stevescarts.polylib.NBTHelper;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Created by brandon3055 on 07/12/2024
 */
public class ModularMinecart extends AbstractMinecart implements IEntityWithComplexSpawn, MenuProvider, DataEntity, IModularCart {
    public static final int MODULAR_SPACE_WIDTH = 443;
    public static final int MODULAR_SPACE_HEIGHT = 168;

    private static final EntityDataAccessor<Boolean> IS_BURNING = SynchedEntityData.defineId(ModularMinecart.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> IS_DISANABLED = SynchedEntityData.defineId(ModularMinecart.class, EntityDataSerializers.BOOLEAN);

    private final ArrayList<ModuleCountPair> moduleCounts = new ArrayList<>();
    private final ArrayList<ModuleBase> modules = new ArrayList<>();
    private final ArrayList<ModuleWorker> workModules = new ArrayList<>();
    private final ArrayList<ModuleEngine> engineModules = new ArrayList<>();
    private final ArrayList<ModuleTank> tankModules = new ArrayList<>();
    private final List<EntityData<?>> entityDataList = new ArrayList<>();

    protected final List<ChunkPos> forcedChunks = new ArrayList<>();

    protected TileEntityCartAssembler placeholderAsssembler;
    protected List<ResourceLocation> moduleLoadingData;
    protected ModuleWorker workingComponent;
    protected boolean hasCreativeSupplies;
    protected boolean isPlaceholder;
    protected boolean wasDisabled;
    protected BlockPos disabledPos;
    protected boolean fullStop = false;
    protected int workingTime;
    protected int motorRotation;
    protected int keepAlive;

    private float rotationOffset;
    private float playerRotationOffset;

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
    public boolean canScrollModules;
    public int modularSpaceHeight;

    public ModularMinecart(Level level, double x, double y, double z) {
        super(ModEntities.MODULAR_CART.get(), level, x, y, z);
        behavior = new ModularMinecartBehavior(this);
    }

    protected ModularMinecart(Level level) {
        this(ModEntities.MODULAR_CART.get(), level);
    }

    public ModularMinecart(Level world, TileEntityCartAssembler assembler, ArrayList<ResourceLocation> data) {
        this(world);
        setPlaceholder(assembler);
        loadPlaceHolderModules(data);
    }

    public ModularMinecart(Level world, double x, double y, double z, CompoundTag data) {
        this(world, x, y, z);
        _oldLoadModules(data);
    }

    // Old Module Load

    private void _oldLoadModules(CompoundTag info) {
        List<CompoundTag> modules = new ArrayList<>();
        if (info == null) return;

        List<ResourceLocation> names = new ArrayList<>();
        ListTag listTag = (ListTag) info.get("modules");
        for (int i = 0; i < listTag.size(); i++) {
            Tag tag = listTag.get(i);
            modules.add((CompoundTag) tag);
            names.add(ResourceLocation.parse(((CompoundTag) tag).getStringOr(String.valueOf(i), "")));
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
                ResourceLocation name = ResourceLocation.parse(tag.getStringOr(String.valueOf(i), ""));
                doLoadModules(StevesCartsAPI.MODULE_REGISTRY.get(name), tag);
            }
        }
        initModules();
    }

    // ===============

    public ModularMinecart(EntityType<?> entityType, Level level) {
        super(entityType, level);
        behavior = new ModularMinecartBehavior(this);
    }

    //=== Cart Stuff ===//

    @Override
    public void tick() {
        if (!level().isClientSide) {
            entityDataList.forEach(EntityData::detectAndSend);
            if (isDisabled() && disabledPos != null) {
                if (!fullStop) {
                    double xOffset = (disabledPos.getX() + 0.5) - position().x;
                    double zOffset = (disabledPos.getZ() + 0.5) - position().z;
                    move(MoverType.SELF, new Vec3(xOffset * 0.25, 0, zOffset * 0.25));
                    if (xOffset < 0.05 && zOffset < 0.05) {
                        fullStop = true;
                    }
                }
            } else {
                fullStop = false;
            }
        }
        flipped = true;
        onCartUpdate();
        if (level().isClientSide) {
            updateSounds();
        }

        double lastYRot = this.getYRot();
        Vec3 lastPos = this.position();
        super.tick();
        if (this.level().isClientSide && lastPos.distanceTo(this.position()) > 0.01) {
            this.rotationOffset += (float) ((this.getYRot() - lastYRot) % 360.0);
            this.rotationOffset %= 360.0F;
        }
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.@NotNull Builder builder) {
        super.defineSynchedData(builder);
        builder.define(IS_BURNING, false);
        builder.define(IS_DISANABLED, false);
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
        this.kill(level);
        if (level.getGameRules().getBoolean(GameRules.RULE_DOENTITYDROPS) && dropOnDeath()) {
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
    public void activateMinecart(int x, int y, int z, boolean active) {
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

    public void setEngineBurning(final boolean on) {
        entityData.set(IS_BURNING, on);
    }

    @Override
    public @NotNull Component getDisplayName() {
        return Localization.translate("entity.minecraft.minecart");
    }

    //=== Cart Motion Handling ===//

    /**
     * Returns the carts velocity vector, or what the pre-stop velocity vector if that cart is stopped.
     * */
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
            //TODO, want to figure out some smarter stopping logic that ensures cart stops in middle of track.
            if (initialVelocity.length() > 0.1) { //Allows pushing, kinda, (needs work)
                initialVelocity = initialVelocity.multiply(0, 0, 0);
            }
            return super.applyNaturalSlowdown(initialVelocity.multiply(0.8, 0.8, 0.8));
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
            preStopVelocity = getDeltaMovement().add(0); //<- Would be nice if there was a .copy function on Vec3...
            disabledPos = new BlockPos(pos);
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
        behavior.setDeltaMovement(preStopVelocity);
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

    @Override
    protected double makeStepAlongTrack(BlockPos pos, RailShape shape, double distance) {
        return super.makeStepAlongTrack(pos, shape, distance);
    }

    @Override
    protected void moveAlongTrack(ServerLevel level) {
        super.moveAlongTrack(level);
    }

    //=== Modules ===//

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
    public void setWorker(ModuleWorker worker) {
        if (workingComponent != null && worker != null) {
            workingComponent.stopWorking();
        }
        if ((workingComponent = worker) == null) {
            setWorkingTime(0);
        }
    }

    @Override
    public ModuleWorker getWorker() {
        return workingComponent;
    }

    @Override
    public void setWorkingTime(int val) {
        workingTime = val;
    }

    //=== Interact ===//

    @Override
    public @NotNull InteractionResult interactAt(@NotNull Player player, @NotNull Vec3 vec, @NotNull InteractionHand hand) {
        if (isPlaceholder()) {
            return InteractionResult.FAIL;
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

    //=== Data ===//

    @Override
    public void remove(@NotNull RemovalReason removalReason) {
        CartEvents.CartRemovedEvent event = new CartEvents.CartRemovedEvent(this);
        NeoForge.EVENT_BUS.post(event);
        if (event.isCanceled()) return;

        if (level().isClientSide) {
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
        if (!level().isClientSide) {
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
        for (ResourceLocation b : moduleLoadingData) {
            data.writeResourceLocation(b);
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
        List<ResourceLocation> list = new ArrayList<>();
        for (int i = 0; i < length; i++) {
            list.add(data.readResourceLocation());
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
        name = Localization.translate(input.getStringOr("cartName", ""));
        setEngineBurning(input.getBooleanOr("engine_burning", false));
        setIsDisabled(input.getBooleanOr("disabled", false));
        if (input.getBooleanOr("stopped", false)) {
            preStopVelocity = new Vec3(input.getDoubleOr("preStopVelX", 0), input.getDoubleOr("preStopVelY", 0), input.getDoubleOr("preStopVelZ", 0));
        } else {
            preStopVelocity = null;
        }
        workingTime = input.getShortOr("workingTime", (short) 0);
        disabledPos = input.read("disabled_pos", BlockPos.CODEC).orElse(null);

        loadModules(input);
    }

    public void loadModules(ValueInput input) {
        List<ResourceLocation> names = new ArrayList<>();

        //Data Migration
        ValueInput.ValueInputList migration = input.childrenListOrEmpty("modules");
        if (!migration.isEmpty()) {
            int i = 0;
            for (ValueInput in : migration) {
                String name = in.getStringOr(String.valueOf(i), "");
                if (name.isEmpty()) continue;
                ResourceLocation moduleName = ResourceLocation.parse(name);
                names.add(moduleName);
                i++;
            }
        }

        ValueInput list = input.childOrEmpty("modules");
        int count = list.getIntOr("count", 0);
        for (int i = 0; i < count; i++) {
            String name = list.getStringOr(String.valueOf(i), "");
            if (name.isEmpty()) continue;
            ResourceLocation moduleName = ResourceLocation.parse(name);
            names.add(moduleName);
        }

        if (!names.isEmpty()) {
            getCart().moduleLoadingData = names;
        }

        modules().clear();
        for (ResourceLocation name : names) {
            doLoadModules(StevesCartsAPI.MODULE_REGISTRY.get(name), null);
        }
        initModules();

        for (int i = 0; i < modules.size(); ++i) {
            ModuleBase module = modules.get(i);
            module.readFromNBT(list, i);
        }
    }

    public static Optional<BlockPos> readBlockPos(CompoundTag tag, String key) {
        int[] aint = tag.getIntArray(key).orElseGet(() -> new int[0]);
        return aint.length == 3 ? Optional.of(new BlockPos(aint[0], aint[1], aint[2])) : Optional.empty();
    }


    @Override
    public List<EntityData<?>> getEntityDataList() {
        return entityDataList;
    }

    @Override
    public void registerEntityData(EntityData<?> data) {
        entityDataList.add(data);
    }


    //=== Random / Util stuff ===

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int id, Inventory inventory, Player player) {
        return new ContainerMinecart(id, inventory, this);
    }

    @Override
    protected void positionRider(Entity passenger, Entity.MoveFunction moveFunction) {
        super.positionRider(passenger, moveFunction);
        if (this.level().isClientSide && passenger instanceof Player player) {
            if (player.shouldRotateWithMinecart() /* && useExperimentalMovement(this.level())*/) {
                //TODO, Figure out if we want this, and get seat rendering working properly.
//            if (true /*player.shouldRotateWithMinecart() && useExperimentalMovement(this.level())*/) {
//                float f = (float) Mth.rotLerp(0.5, (double)this.playerRotationOffset, (double)this.rotationOffset);
//                player.setYRot(player.getYRot() - (f - this.playerRotationOffset));
//                this.playerRotationOffset = f;
            }
        }

    }

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

}
