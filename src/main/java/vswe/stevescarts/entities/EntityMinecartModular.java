package vswe.stevescarts.entities;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Container;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseRailBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RailBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.RailShape;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.entity.IEntityAdditionalSpawnData;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.NotNull;
import vswe.stevescarts.StevesCarts;
import vswe.stevescarts.api.StevesCartsAPI;
import vswe.stevescarts.blocks.tileentities.TileEntityCartAssembler;
import vswe.stevescarts.api.client.ModelCartbase;
import vswe.stevescarts.containers.ContainerMinecart;
import vswe.stevescarts.helpers.ActivatorOption;
import vswe.stevescarts.helpers.GuiAllocationHelper;
import vswe.stevescarts.helpers.ModuleCountPair;
import vswe.stevescarts.helpers.storages.TransferHandler;
import vswe.stevescarts.init.ModBlocks;
import vswe.stevescarts.init.ModEntities;
import vswe.stevescarts.api.modules.interfaces.IActivatorModule;
import vswe.stevescarts.api.modules.ModuleBase;
import vswe.stevescarts.modules.addons.ModuleCreativeSupplies;
import vswe.stevescarts.api.modules.data.ModuleData;
import vswe.stevescarts.api.modules.template.ModuleEngine;
import vswe.stevescarts.modules.storages.tanks.ModuleTank;
import vswe.stevescarts.modules.workers.CompWorkModule;
import vswe.stevescarts.modules.workers.ModuleWorker;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;

public class EntityMinecartModular extends AbstractMinecart implements Container, IEntityAdditionalSpawnData, IFluidHandler, MenuProvider
{
    public BlockPos disabledPos;
    protected boolean wasDisabled;
    public double pushX;
    public double pushZ;
    public double temppushX;
    public double temppushZ;
    protected boolean engineFlag;
    private int motorRotation;
    public boolean cornerFlip;
    private List<ResourceLocation> moduleLoadingData;
    private RailShape fixedRailDirection;
    private BlockPos fixedRailPos;
    private int wrongRender;
    private boolean oldRender;
    private float lastRenderYaw;
    private double lastMotionX;
    private double lastMotionZ;
    private int workingTime;
    private ModuleWorker workingComponent;
    public TileEntityCartAssembler placeholderAsssembler;
    public boolean isPlaceholder;
    public int keepAlive;
    public static final int MODULAR_SPACE_WIDTH = 443;
    public static final int MODULAR_SPACE_HEIGHT = 168;
    public int modularSpaceHeight;
    public boolean canScrollModules;
    private ArrayList<ModuleCountPair> moduleCounts;
    public static final int[][][] railDirectionCoordinates;
    private ArrayList<ModuleBase> modules;
    private ArrayList<ModuleWorker> workModules;
    private ArrayList<ModuleEngine> engineModules;
    private ArrayList<ModuleTank> tankModules;
    private ModuleCreativeSupplies creativeSupplies;
    public RandomSource random;
    protected Component name;
    private int scrollY;
    @Deprecated(forRemoval = true)
    private int keepSilent;

    private static final EntityDataAccessor<Boolean> IS_BURNING = SynchedEntityData.defineId(EntityMinecartModular.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> IS_DISANABLED = SynchedEntityData.defineId(EntityMinecartModular.class, EntityDataSerializers.BOOLEAN);

    public ArrayList<ModuleBase> getModules()
    {
        return modules;
    }

    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket()
    {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    public boolean hasModule(Class<? extends ModuleBase> module)
    {
        for (ModuleBase moduleBase : getModules())
        {
            if (moduleBase.getClass().equals(module))
            {
                return true;
            }
        }
        return false;
    }

    public boolean hasModule(ModuleData moduleData)
    {
        if(moduleData == null) return false;

        for (ModuleBase module : getModules())
        {
            if(module.getModuleId() == moduleData.getID())
            {
                return true;
            }
        }
        return false;
    }

    public ArrayList<ModuleWorker> getWorkers()
    {
        return workModules;
    }

    public ArrayList<ModuleEngine> getEngines()
    {
        return engineModules;
    }

    public ArrayList<ModuleTank> getModuleTanks()
    {
        return tankModules;
    }

    public ArrayList<ModuleCountPair> getModuleCounts()
    {
        return moduleCounts;
    }

    public EntityMinecartModular(final Level world, final double x, final double y, final double z, final CompoundTag info, final Component name)
    {
        super(ModEntities.MODULAR_CART.get(), world, x, y, z);
        engineFlag = false;
        fixedRailDirection = null;
        random = world.random;
        loadModules(info);
    }

    public EntityMinecartModular(Level world)
    {
        super(ModEntities.MODULAR_CART.get(), world);
        engineFlag = false;
        fixedRailDirection = null;
        random = world.random;
    }

    public EntityMinecartModular(EntityType<EntityMinecartModular> entityType, Level world)
    {
        super(entityType, world);
        engineFlag = false;
        fixedRailDirection = null;
        random = RandomSource.create();
    }

    public EntityMinecartModular(final Level world, final TileEntityCartAssembler assembler, final ArrayList<ResourceLocation> data)
    {
        this(world);
        setPlaceholder(assembler);
        loadPlaceHolderModules(data);
    }

    @Override
    public void defineSynchedData()
    {
        super.defineSynchedData();
        this.entityData.define(IS_BURNING, false);
        this.entityData.define(IS_DISANABLED, false);
    }

    private void loadPlaceHolderModules(final List<ResourceLocation> data)
    {
        if(modules == null)
        {
            modules = new ArrayList<>();
            for (ResourceLocation moduleResourceLocation : data)
            {
                ModuleData moduleData = StevesCartsAPI.MODULE_REGISTRY.get(moduleResourceLocation);
                doLoadModules(moduleData, null);
            }
        }
        else
        {
            modules.clear();
            for (ResourceLocation moduleResourceLocation : data)
            {
                ModuleData moduleData = StevesCartsAPI.MODULE_REGISTRY.get(moduleResourceLocation);
                doLoadModules(moduleData, null);
            }
        }
        initModules();
        moduleLoadingData = data;
//        moduleLoadingData = new ArrayList<>();
//        for (int i = 0; i < data.size(); i++) {
//            ResourceLocation module = data.get(i);
//            CompoundTag tag = new CompoundTag();
//            tag.putString(String.valueOf(i), module.toString());
//            moduleLoadingData.add(tag);
//        }
    }

    private void doLoadModules(ModuleData moduleData, @org.jetbrains.annotations.Nullable CompoundTag data)
    {
        if(moduleData == null) return;
        try
        {
            Class<? extends ModuleBase> moduleClass = moduleData.getModuleClass();
            Constructor<? extends ModuleBase> moduleConstructor = moduleClass.getConstructor(EntityMinecartModular.class);
            ModuleBase module = moduleConstructor.newInstance(this);
            module.setModuleId(moduleData.getID());
            modules.add(module);
            if (data != null && data.contains("data")) {
                module.readExtraData(data.getCompound("data"));
            }
        }
        catch (Exception e)
        {
            StevesCarts.LOGGER.error("Failed to load module with ID " + moduleData.getID() + "! More info below.");
            e.printStackTrace();
        }
    }

    private void loadModules(final CompoundTag info)
    {
        List<CompoundTag> modules = new ArrayList<>();
        if(info == null) return;

        List<ResourceLocation> names = new ArrayList<>();
        ListTag listTag = (ListTag) info.get("modules");
        for (int i = 0; i < listTag.size(); i++) {
            Tag tag = listTag.get(i);
            modules.add((CompoundTag) tag);
            names.add(new ResourceLocation(((CompoundTag) tag).getString(String.valueOf(i))));
        }

        if (!names.isEmpty()){
            moduleLoadingData = names;
        }
        loadModules(modules);
    }

    public void updateSimulationModules(final List<ResourceLocation> data)
    {
        if (!isPlaceholder)
        {
            StevesCarts.LOGGER.error("You're stupid! This is not a placeholder cart.");
        }
        else
        {
            loadPlaceHolderModules(data);
        }
    }

    protected void loadModules(List<CompoundTag> data)
    {
        modules = new ArrayList<>();
        if(data != null) {
            for (int i = 0; i < data.size(); i++) {
                CompoundTag tag = data.get(i);
                ResourceLocation name = new ResourceLocation(tag.getString(String.valueOf(i)));
                doLoadModules(StevesCartsAPI.MODULE_REGISTRY.get(name), tag);
            }
        }
        initModules();
    }

    protected void loadModulesFromNames(List<ResourceLocation> data)
    {
        modules = new ArrayList<>();
        if(data != null) {
            for (ResourceLocation name : data) {
                doLoadModules(StevesCartsAPI.MODULE_REGISTRY.get(name), null);
            }
        }
        initModules();
    }

    private void initModules()
    {
        moduleCounts = new ArrayList<>();
        for (final ModuleBase module : modules)
        {
            final ModuleData data = StevesCartsAPI.MODULE_REGISTRY.get(module.getModuleId());
            boolean found = false;
            for (final ModuleCountPair count : moduleCounts)
            {
                if (count.isContainingData(data))
                {
                    count.increase();
                    found = true;
                    break;
                }
            }
            if (!found)
            {
                moduleCounts.add(new ModuleCountPair(data));
            }
        }
        for (final ModuleBase module : modules)
        {
            module.preInit();
        }
        workModules = new ArrayList<>();
        engineModules = new ArrayList<>();
        tankModules = new ArrayList<>();
        @Deprecated(forRemoval = true)
        final int x = 0;
        @Deprecated(forRemoval = true)
        final int y = 0;
        @Deprecated(forRemoval = true)
        final int maxH = 0;
        int guidata = 0;
        int packets = 0;
        if (level().isClientSide)
        {
            generateModels();
        }
        for (final ModuleBase module2 : modules)
        {
            if (module2 instanceof ModuleWorker)
            {
                workModules.add((ModuleWorker) module2);
            }
            else if (module2 instanceof ModuleEngine)
            {
                engineModules.add((ModuleEngine) module2);
            }
            else if (module2 instanceof ModuleTank)
            {
                tankModules.add((ModuleTank) module2);
            }
            else
            {
                if (!(module2 instanceof ModuleCreativeSupplies))
                {
                    continue;
                }
                creativeSupplies = (ModuleCreativeSupplies) module2;
            }
        }
        final CompWorkModule sorter = new CompWorkModule();
        workModules.sort(sorter);
        if (!isPlaceholder)
        {
            final ArrayList<GuiAllocationHelper> lines = new ArrayList<>();
            int slots = 0;
            for (final ModuleBase module3 : modules)
            {
                if (module3.hasGui())
                {
                    boolean foundLine = false;
                    for (final GuiAllocationHelper line : lines)
                    {
                        if (line.width + module3.guiWidth() <= MODULAR_SPACE_WIDTH)
                        {
                            module3.setX(line.width);
                            line.width += module3.guiWidth();
                            line.maxHeight = Math.max(line.maxHeight, module3.guiHeight());
                            line.modules.add(module3);
                            foundLine = true;
                            break;
                        }
                    }
                    if (!foundLine)
                    {
                        final GuiAllocationHelper line2 = new GuiAllocationHelper();
                        module3.setX(0);
                        line2.width = module3.guiWidth();
                        line2.maxHeight = module3.guiHeight();
                        line2.modules.add(module3);
                        lines.add(line2);
                    }
                    module3.setGuiDataStart(guidata);
                    guidata += module3.numberOfGuiData();
                    if (module3.hasSlots())
                    {
                        slots = module3.generateSlots(slots);
                    }
                }
                if (module3.numberOfDataWatchers() > 0)
                {
                    module3.initDw();
                }
                module3.setPacketStart(packets);
                packets += module3.totalNumberOfPackets();
            }
            int currentY = 0;
            for (final GuiAllocationHelper line3 : lines)
            {
                for (final ModuleBase module4 : line3.modules)
                {
                    module4.setY(currentY);
                }
                currentY += line3.maxHeight;
            }
            if (currentY > MODULAR_SPACE_HEIGHT)
            {
                canScrollModules = true;
            }
            modularSpaceHeight = currentY;
        }
        for (final ModuleBase module5 : modules)
        {
            module5.init();
        }
    }

    @Override
    public void remove(@NotNull RemovalReason removalReason)
    {
        if (level().isClientSide)
        {
            for (int var1 = 0; var1 < getContainerSize(); ++var1)
            {
                setItem(var1, ItemStack.EMPTY);
            }
        }
        super.remove(removalReason);
        if (modules != null)
        {
            for (final ModuleBase module : modules)
            {
                module.onDeath();
            }
        }
        dropChunkLoading();
    }

    @OnlyIn(Dist.CLIENT)
    public void renderOverlay(PoseStack poseStack, Minecraft minecraft)
    {
        if (modules != null)
        {
            for (final ModuleBase module : modules)
            {
                module.renderOverlay(poseStack, minecraft);
            }
        }
    }

    public void updateFuel() {
        final int consumption = getConsumption();
        if (consumption > 0) {
            final ModuleEngine engine = getCurrentEngine();
            if (engine != null) {
                engine.consumeFuel(consumption);
                if (!isPlaceholder && level().isClientSide && hasFuel() && !isDisabled()) {
                    engine.smoke();
                }
            }
        }
        if (hasFuel()) {
            if (!engineFlag) {
                pushX = temppushX;
                pushZ = temppushZ;
            }
        } else if (engineFlag) {
            temppushX = pushX;
            temppushZ = pushZ;
            final double n = 0.0;
            pushZ = n;
            pushX = n;
        }
        setEngineBurning(hasFuel() && !isDisabled());
    }

    public boolean isEngineBurning()
    {
        return entityData.get(IS_BURNING);
    }

    public void setEngineBurning(final boolean on)
    {
        entityData.set(IS_BURNING, on);
    }

    private ModuleEngine getCurrentEngine()
    {
        if (modules == null)
        {
            return null;
        }
        for (final ModuleBase module : modules)
        {
            if (module.stopEngines())
            {
                return null;
            }
        }
        final int consumption = getConsumption(true);
        final ArrayList<ModuleEngine> priority = new ArrayList<>();
        int mostImportant = -1;
        for (final ModuleEngine engine : engineModules)
        {
            if (engine.hasFuel(consumption) && (mostImportant == -1 || mostImportant >= engine.getPriority()))
            {
                if (engine.getPriority() < mostImportant)
                {
                    priority.clear();
                }
                mostImportant = engine.getPriority();
                priority.add(engine);
            }
        }
        if (priority.size() > 0)
        {
            if (motorRotation >= priority.size())
            {
                motorRotation = 0;
            }
            motorRotation = (motorRotation + 1) % priority.size();
            return priority.get(motorRotation);
        }
        return null;
    }

    public int getConsumption()
    {
        return getConsumption(!isDisabled() && isEngineBurning());
    }

    public int getConsumption(final boolean isMoving)
    {
        int consumption = isMoving ? 1 : 0;
        if (modules != null && !isPlaceholder)
        {
            for (final ModuleBase module : modules)
            {
                consumption += module.getConsumption(isMoving);
            }
        }
        return consumption;
    }

    //TODO Forge PR for making Type an extensible enum
    @Override
    public @NotNull Type getMinecartType() {
        return Type.FURNACE;
    }

    @Override
    public @NotNull HitResult pick(double p_19908_, float p_19909_, boolean p_19910_)
    {
        return super.pick(p_19908_, p_19909_, p_19910_);
    }

    @Override
    protected float getEyeHeight(@NotNull Pose pose, @NotNull EntityDimensions entityDimensions)
    {
        return 0.9F;
    }

    @Override
    public double getMyRidingOffset()
    {
        if (modules != null && !getPassengers().isEmpty())
        {
            for (final ModuleBase module : modules)
            {
                final float offset = module.mountedOffset(getPassengers().get(0));
                if (offset != 0.0f)
                {
                    return offset;
                }
            }
        }
        return super.getMyRidingOffset();
    }

    @Override
    @NotNull
    public Item getDropItem()
    {
        return Items.AIR;
    }

    @Nonnull
    public ItemStack getCartItem()
    {
        if (modules != null)
        {
            @Nonnull ItemStack cart = ModuleData.createModularCart(this);
            return cart;
        }
        return ItemStack.EMPTY;
    }

    //Override this to stop it spawning a vanilla minecart
    @Override
    public void destroy(@NotNull DamageSource damageSource)
    {
        this.kill();
        if (this.level().getGameRules().getBoolean(GameRules.RULE_DOENTITYDROPS) && dropOnDeath()) {
            ItemStack itemstack = getCartItem();
            if (this.hasCustomName()) {
                itemstack.setHoverName(this.getCustomName());
            }
            for (int i = 0; i < getContainerSize(); i++)
            {
                ItemStack stack = getItem(i);
                if(!stack.isEmpty())
                {
                    this.spawnAtLocation(stack);
                }
            }
            this.spawnAtLocation(itemstack);
        }
    }

    public boolean dropOnDeath() {
        if (isPlaceholder) {
            return false;
        }
        if (modules != null) {
            for (final ModuleBase module : modules) {
                if (!module.dropOnDeath()) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public float getMaxCartSpeedOnRail()
    {
        float maxSpeed = super.getMaxCartSpeedOnRail();
        if (modules != null)
        {
            for (final ModuleBase module : modules)
            {
                final float tempMax = module.getMaxSpeed();
                if (tempMax < maxSpeed)
                {
                    maxSpeed = tempMax;
                }
            }
        }
        return maxSpeed;
    }

    @Override
    public boolean isPoweredCart()
    {
        return engineModules.size() > 0;
    }

    public int getDefaultDisplayTileData()
    {
        return -1;
    }

    public float[] getColor()
    {
        if (modules != null)
        {
            for (final ModuleBase module : getModules())
            {
                final float[] color = module.getColor();
                if (color[0] != 1.0f || color[1] != 1.0f || color[2] != 1.0f)
                {
                    return color;
                }
            }
        }
        return new float[]{1.0f, 1.0f, 1.0f};
    }

    public int getYTarget()
    {
        if (modules != null)
        {
            for (ModuleBase module : getModules())
            {
                int yTarget = module.getYTarget();
                if (yTarget != -1)
                {
                    return yTarget;
                }
            }
        }
        return (int) y();
    }

    public ModuleBase getInterfaceThief()
    {
        if (modules != null)
        {
            for (final ModuleBase module : getModules())
            {
                if (module.doStealInterface())
                {
                    return module;
                }
            }
        }
        return null;
    }

    @Override
    public boolean hurt(final @NotNull DamageSource dmg, final float par2)
    {
        if (isPlaceholder)
        {
            return false;
        }
        if (modules != null)
        {
            for (final ModuleBase module : getModules())
            {
                if (!module.receiveDamage(dmg, par2))
                {
                    return false;
                }
            }
        }
        return super.hurt(dmg, par2);
    }

    @Override
    public boolean isPushable()
    {
        return true;
    }

    @Override
    public void activateMinecart(final int x, final int y, final int z, final boolean active)
    {
        if (modules != null)
        {
            for (final ModuleBase module : modules)
            {
                module.activatedByRail(x, y, z, active);
            }
        }
    }

    @Override
    public void moveMinecartOnRail(@NotNull BlockPos pos) {
        super.moveMinecartOnRail(pos);
        if (modules != null) {
            for (final ModuleBase module : modules) {
                module.moveMinecartOnRail(pos);
            }
        }
        BlockState blockState = level().getBlockState(pos);
        RailShape railDirection = ((BaseRailBlock) blockState.getBlock()).getRailDirection(blockState, level(), pos, this);
        cornerFlip = ((railDirection == RailShape.SOUTH_EAST || railDirection == RailShape.SOUTH_WEST) && getDeltaMovement().x < 0.0) || ((railDirection == RailShape.NORTH_EAST || railDirection == RailShape.NORTH_WEST) && getDeltaMovement().x > 0.0);

        if (blockState.getBlock() != ModBlocks.ADVANCED_DETECTOR.get() && isDisabled()) {
            releaseCart();
        }
        boolean canBeDisabled = blockState.getBlock() == ModBlocks.ADVANCED_DETECTOR.get();// && (stateBelow.getBlock() != ModBlocks.DETECTOR_UNIT.getBlock() || !DetectorType.getTypeFromSate(stateBelow).canInteractWithCart() || DetectorType.getTypeFromSate(stateBelow).shouldStopCart());
        boolean forceUnDisable = wasDisabled && disabledPos != null && disabledPos.equals(pos);
        if (!forceUnDisable && wasDisabled) {
            wasDisabled = false;
        }
        canBeDisabled = (!forceUnDisable && canBeDisabled);
        if (canBeDisabled && !isDisabled()) {
            setIsDisabled(true);
            if (pushX != 0.0 || pushZ != 0.0) {
                temppushX = pushX;
                temppushZ = pushZ;
                final double n = 0.0;
                pushZ = n;
                pushX = n;
            }
            disabledPos = new BlockPos(pos);
        }
        if (fixedRailPos != null && !fixedRailPos.equals(pos)) {
            fixedRailDirection = null;
            fixedRailPos = new BlockPos(fixedRailPos.getX(), -1, fixedRailPos.getZ());
        }
    }

    public boolean isCorner(BlockPos blockPos)
    {
        if(level().getBlockState(blockPos).getBlock() == Blocks.RAIL)
        {
            RailShape value = level().getBlockState(blockPos).getValue(RailBlock.SHAPE);
            if(value == RailShape.NORTH_EAST) return true;
            if(value == RailShape.NORTH_WEST) return true;
            if(value == RailShape.SOUTH_WEST) return true;
            if(value == RailShape.SOUTH_EAST) return true;
        }

        return false;
    }

    public RailShape getRailDirection(BlockPos pos) {
        ModuleBase.RAILDIRECTION dir = ModuleBase.RAILDIRECTION.DEFAULT;
        for (ModuleBase module : getModules()) {
            dir = module.getSpecialRailDirection(pos);
            if (dir != ModuleBase.RAILDIRECTION.DEFAULT) {
                break;
            }
        }
        if (dir == ModuleBase.RAILDIRECTION.DEFAULT) {
            return null;
        }
        int Yaw = (int) (getYRot() % 180.0f); //TODO Is this the correct rotation?
        if (Yaw < 0) {
            Yaw += 180;
        }
        boolean flag = Yaw >= 45 && Yaw <= 135;
        Vec3 motion = getDeltaMovement();
        if (fixedRailDirection == null) {
            switch (dir) {
                case FORWARD -> {
                    if (flag) {
                        fixedRailDirection = RailShape.NORTH_SOUTH;
                        break;
                    }
                    fixedRailDirection = RailShape.EAST_WEST;
                }
                case LEFT -> {
                    if (flag) {
                        if (motion.z > 0.0) {
                            fixedRailDirection = RailShape.NORTH_EAST;
                            break;
                        }
                        if (motion.z <= 0.0) {
                            fixedRailDirection = RailShape.SOUTH_WEST;
                        }
                    } else {
                        if (motion.x > 0.0) {
                            fixedRailDirection = RailShape.NORTH_WEST;
                            break;
                        }
                        if (motion.x < 0.0) {
                            fixedRailDirection = RailShape.SOUTH_EAST;
                        }
                    }
                }
                case RIGHT -> {
                    if (flag) {
                        if (motion.z > 0.0) {
                            fixedRailDirection = RailShape.NORTH_WEST;
                            break;
                        }
                        if (motion.z <= 0.0) {
                            fixedRailDirection = RailShape.SOUTH_EAST;
                        }
                    } else {
                        if (motion.x > 0.0) {
                            fixedRailDirection = RailShape.SOUTH_WEST;
                            break;
                        }
                        if (motion.x < 0.0) {
                            fixedRailDirection = RailShape.NORTH_EAST;
                        }
                    }
                }
                case NORTH -> {
                    if (flag) {
                        if (motion.z > 0.0) {
                            fixedRailDirection = RailShape.NORTH_SOUTH;
                        }
                    } else {
                        if (motion.x > 0.0) {
                            fixedRailDirection = RailShape.SOUTH_WEST;
                            break;
                        }
                        if (motion.x < 0.0) {
                            fixedRailDirection = RailShape.SOUTH_EAST;
                        }
                    }
                }
                default -> {}
            }
            if (fixedRailDirection == null) {
                return null;
            }
            fixedRailPos = new BlockPos(pos);
        }
        return fixedRailDirection;
    }

    public void resetRailDirection() {
        fixedRailDirection = null;
    }

//    public RailShape getRailDirection(final BlockPos pos)
//    {
//        if (this.lastRailShape != null)
//        {
//            return this.lastRailShape;
//        }
//        return null;
//    }

    public void turnback() {
        pushX *= -1.0;
        pushZ *= -1.0;
        temppushX *= -1.0;
        temppushZ *= -1.0;
        setDeltaMovement(getDeltaMovement().multiply(-1, -1, -1));
    }

    public void releaseCart()
    {
        wasDisabled = true;
        setIsDisabled(false);
        pushX = temppushX;
        pushZ = temppushZ;
    }

    @Override
    public ItemStack getPickResult()
    {
        return getCartItem();
    }

    @Override
    public int getContainerSize()
    {
        int slotCount = 0;
        if (modules != null)
        {
            for (final ModuleBase module : modules)
            {
                slotCount += module.getInventorySize();
            }
        }
        return slotCount;
    }

    @Override
    public boolean isEmpty()
    {
        return false;
    }

    @Override
    public @NotNull ItemStack getItem(int i)
    {
        if (modules != null)
        {
            for (final ModuleBase module : modules)
            {
                if (i < module.getInventorySize())
                {
                    return module.getStack(i);
                }
                i -= module.getInventorySize();
            }
        }
        return ItemStack.EMPTY;
    }

    @Override
    public @NotNull ItemStack removeItem(int i, int p_70298_2_)
    {
        if (!getItem(i).isEmpty())
        {
            @Nonnull ItemStack var2 = getItem(i);
            setItem(i, ItemStack.EMPTY);
            return var2;
        }
        return ItemStack.EMPTY;
    }

    @Override
    public @NotNull ItemStack removeItemNoUpdate(int i)
    {
        return ItemStack.EMPTY;
    }

    @Override
    public void setItem(int i, @NotNull ItemStack item)
    {
        if (modules != null)
        {
            for (final ModuleBase module : modules)
            {
                if (i < module.getInventorySize())
                {
                    module.setStack(i, item);
                    break;
                }
                i -= module.getInventorySize();
            }
        }
    }

    @Override
    public void setChanged()
    {
        if (modules != null)
        {
            for (final ModuleBase module : modules)
            {
                module.onInventoryChanged();
            }
        }
    }

    @Override
    public boolean stillValid(@NotNull Player player)
    {
        return true;
    }

    @Override
    protected void moveAlongTrack(@NotNull BlockPos pos, @NotNull BlockState state) {
        if (!getPassengers().isEmpty()) {
            Entity riddenByEntity = getPassengers().get(0);
            if (riddenByEntity instanceof LivingEntity) {
                final float move = riddenByEntity.moveDist;
                riddenByEntity.moveDist = 0.0f;
                super.moveAlongTrack(pos, state);
                riddenByEntity.moveDist = move;
            } else {
                super.moveAlongTrack(pos, state);
            }
        } else {
            super.moveAlongTrack(pos, state);
        }
        double d2 = pushX * pushX + pushZ * pushZ;
        Vec3 motion = getDeltaMovement();
        if (d2 > 1.0E-4 && motion.x * motion.x + motion.z * motion.z > 0.001) {
            d2 = Math.sqrt(d2);
            pushX /= d2;
            pushZ /= d2;
            if (pushX * motion.x + pushZ * motion.z < 0.0) {
                pushX = 0.0;
                pushZ = 0.0;
            } else {
                pushX = motion.x;
                pushZ = motion.z;
            }
        }
    }

    @Override
    protected void applyNaturalSlowdown() {
        double d0 = pushX * pushX + pushZ * pushZ;
        engineFlag = (d0 > 1.0E-4);

        if (isDisabled()) {
            setDeltaMovement(0, 0, 0);
        } else if (engineFlag) {
            d0 = Math.sqrt(d0);
            this.pushX /= d0;
            this.pushZ /= d0;
            final double d2 = getPushFactor();
            Vec3 vec3 = this.getDeltaMovement().multiply(0.8D, 0.0D, 0.8D).add(this.pushX * d2, 0.0D, this.pushZ * d2);
            if (this.isInWater()) {
                vec3 = vec3.scale(0.1D);
            }
            this.setDeltaMovement(vec3);
        } else {
            this.setDeltaMovement(this.getDeltaMovement().multiply(0.98D, 0.0D, 0.98D));
        }
        super.applyNaturalSlowdown();
    }


    protected double getPushFactor() {
        if (modules != null) {
            for (final ModuleBase module : modules) {
                final double factor = module.getPushFactor();
                if (factor >= 0.0) {
                    return factor;
                }
            }
        }
        return 0.05;
    }

    @Override
    public boolean save(@NotNull CompoundTag tagCompound)
    {
        super.save(tagCompound);
        if(name != null) tagCompound.putString("cartName", name.getString());
        tagCompound.putBoolean("engineFlag", engineFlag);
        tagCompound.putDouble("pushX", pushX);
        tagCompound.putDouble("pushZ", pushZ);
        tagCompound.putDouble("temppushX", temppushX);
        tagCompound.putDouble("temppushZ", temppushZ);
        tagCompound.putShort("workingTime", (short) workingTime);
        writeModulesToNbt(tagCompound);
        if (modules != null)
        {
            for (int i = 0; i < modules.size(); ++i)
            {
                final ModuleBase module = modules.get(i);
                module.writeToNBT(tagCompound, i);
            }
        }
        return true;
    }

    public void writeModulesToNbt(CompoundTag compoundTag)
    {
        ListTag listTag = new ListTag();
        if(modules != null)
        {
            for (int i = 0; i < modules.size(); i++)
            {
                CompoundTag compoundTag1 = new CompoundTag();
                compoundTag1.putString(String.valueOf(i), modules.get(i).getModuleId().toString());
                listTag.add(i, compoundTag1);
            }
            compoundTag.put("modules", listTag);
        }
    }

    @Override
    public void load(final @NotNull CompoundTag tagCompound)
    {
        super.load(tagCompound);
        name = Component.translatable(tagCompound.getString("cartName"));
        engineFlag = tagCompound.getBoolean("engineFlag");
        pushX = tagCompound.getDouble("pushX");
        pushZ = tagCompound.getDouble("pushZ");
        temppushX = tagCompound.getDouble("temppushX");
        temppushZ = tagCompound.getDouble("temppushZ");
        workingTime = tagCompound.getShort("workingTime");
        loadModules(tagCompound);
        if (modules != null)
        {
            for (int i = 0; i < modules.size(); ++i)
            {
                final ModuleBase module = modules.get(i);
                module.readFromNBT(tagCompound, i);
            }
        }
    }


    public boolean isDisabled()
    {
        return entityData.get(IS_DISANABLED);
    }

    public void setIsDisabled(final boolean disabled)
    {
        entityData.set(IS_DISANABLED, disabled);
    }

    @Override
    public void tick()
    {
        flipped = true;
//        int i = Mth.floor(this.getX());
//        int j = Mth.floor(this.getY());
//        int k = Mth.floor(this.getZ());
//        if (this.level.getBlockState(new BlockPos(i, j - 1, k)).is(BlockTags.RAILS))
//        {
//            --j;
//        }

//        BlockPos blockpos = new BlockPos(i, j, k);
//        BlockState blockState = this.level.getBlockState(blockpos);

//        if (blockState.getBlock() instanceof BaseRailBlock)
//        {
//            RailShape railshape = ((BaseRailBlock) blockState.getBlock()).getRailDirection(blockState, this.level, getExactPosition().below(), this);
//            if (blockState.getBlock() != ModBlocks.JUNCTION.get())
//            {
//                lastRailShape = railshape;
//            }
//        }

        onCartUpdate();
        if (level().isClientSide)
        {
            updateSounds();
        }
        super.tick();
    }

    public void onCartUpdate()
    {
        if (modules != null)
        {
            updateFuel();
            for (final ModuleBase module : modules)
            {
                module.update();
            }
            for (final ModuleBase module : modules)
            {
                module.postUpdate();
            }
            work();
            setCurrentCartSpeedCapOnRail(getMaxCartSpeedOnRail());
        }
        if (isPlaceholder && keepAlive++ > 20)
        {
            remove(RemovalReason.KILLED);
            placeholderAsssembler.resetPlaceholder();
        }
    }

    public boolean hasFuel()
    {
        if (isDisabled())
        {
            return false;
        }
        if (modules != null)
        {
            for (final ModuleBase module : modules)
            {
                if (module.stopEngines())
                {
                    return false;
                }
            }
        }
        return hasFuelForModule();
    }

    public boolean hasFuelForModule()
    {
        if (isPlaceholder)
        {
            return true;
        }
        final int consumption = getConsumption(true);
        if (modules != null)
        {
            for (final ModuleBase module : modules)
            {
                if (module.hasFuel(consumption))
                {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public @NotNull InteractionResult interactAt(@NotNull Player player, @NotNull Vec3 vec, @NotNull InteractionHand hand) {
        if (isPlaceholder) {
            return InteractionResult.FAIL;
        }
        if (modules != null && !player.isCrouching()) {
            boolean interupt = false;
            for (final ModuleBase module : modules) {
                if (module.onInteractFirst(player)) {
                    interupt = true;
                }
            }
            if (interupt) {
                return InteractionResult.SUCCESS;
            }
        }
        if (!level().isClientSide) {
            if (!isDisabled() && !hasPassenger(player)) {
                temppushX = getX() - player.getX();
                temppushZ = getZ() - player.getZ();
            }
            if (!isDisabled() && hasFuel() && pushX == 0.0 && pushZ == 0.0) {
                pushX = temppushX;
                pushZ = temppushZ;
            }
            NetworkHooks.openScreen((ServerPlayer) player, this, packetBuffer -> packetBuffer.writeInt(getId()));
        }
        return InteractionResult.SUCCESS;
    }

    @Override
    public @NotNull Component getDisplayName()
    {
        return Component.translatable("entity.minecart");
    }

    @SuppressWarnings("unused")
    public void loadChunks()
    {
    }

    @SuppressWarnings("unused")
    public void loadChunks(final int chunkX, final int chunkZ)
    {
    }

    public void initChunkLoading()
    {
    }

    public void dropChunkLoading()
    {
    }

    public void setWorker(final ModuleWorker worker)
    {
        if (workingComponent != null && worker != null)
        {
            workingComponent.stopWorking();
        }
        if ((workingComponent = worker) == null)
        {
            setWorkingTime(0);
        }
    }

    public ModuleWorker getWorker()
    {
        return workingComponent;
    }

    public void setWorkingTime(final int val)
    {
        workingTime = val;
    }

    private void work()
    {
        if (isPlaceholder)
        {
            return;
        }
        if (!level().isClientSide && hasFuel())
        {
            if (workingTime <= 0)
            {
                final ModuleWorker oldComponent = workingComponent;
                if (workingComponent != null)
                {
                    final boolean result = workingComponent.work();
                    if (workingComponent != null && oldComponent == workingComponent && workingTime <= 0 && !workingComponent.preventAutoShutdown())
                    {
                        workingComponent.stopWorking();
                    }
                    if (result)
                    {
                        work();
                        return;
                    }
                }
                if (workModules != null)
                {
                    for (final ModuleWorker module : workModules)
                    {
                        if (module.work())
                        {
                            return;
                        }
                    }
                }
            }
            else
            {
                --workingTime;
            }
        }
    }

    public void handleActivator(final ActivatorOption option, final boolean isOrange)
    {
        for (final ModuleBase module : modules)
        {
            if (module instanceof final IActivatorModule activator && option.getModule().isAssignableFrom(module.getClass()))
            {
                if (option.shouldActivate(isOrange))
                {
                    activator.doActivate(option.getId());
                }
                else if (option.shouldDeactivate(isOrange))
                {
                    activator.doDeActivate(option.getId());
                }
                else
                {
                    if (!option.shouldToggle())
                    {
                        continue;
                    }
                    if (activator.isActive(option.getId()))
                    {
                        activator.doDeActivate(option.getId());
                    }
                    else
                    {
                        activator.doActivate(option.getId());
                    }
                }
            }
        }
    }

    public boolean getRenderFlippedYaw(float yaw) {
        yaw %= 360.0f;
        if (yaw < 0.0f) {
            yaw += 360.0f;
        }
        Vec3 motion = getDeltaMovement();
        if (!oldRender || Math.abs(yaw - lastRenderYaw) < 90.0f || Math.abs(yaw - lastRenderYaw) > 270.0f || (motion.x > 0.0 && lastMotionX < 0.0) || (motion.z > 0.0 && lastMotionZ < 0.0)
                || (motion.x < 0.0 && lastMotionX > 0.0) || (motion.z < 0.0 && lastMotionZ > 0.0) || wrongRender >= 50) {
            lastMotionX = motion.x;
            lastMotionZ = motion.z;
            lastRenderYaw = yaw;
            oldRender = true;
            wrongRender = 0;
            return false;
        }
        ++wrongRender;
        return true;
    }

    @SuppressWarnings("unused")
    public ArrayList<Component> getLabel()
    {
        final ArrayList<Component> label = new ArrayList<>();
        if (getModules() != null)
        {
            for (final ModuleBase module : getModules())
            {
                module.addToLabel(label);
            }
        }
        return label;
    }

    public int x()
    {
        return Mth.floor(position().x);
    }

    public int y()
    {
        return Mth.floor(position().y);
    }

    public int z()
    {
        return Mth.floor(position().z);
    }

    public BlockPos getExactPosition()
    {
        return blockPosition();
    }

    public void addItemToChest(@Nonnull ItemStack iStack)
    {
        TransferHandler.TransferItem(iStack, this, getCon(null), Slot.class, null, -1);
    }

    public void addItemToChest(@Nonnull ItemStack iStack, final int start, final int end)
    {
        TransferHandler.TransferItem(iStack, this, start, end, getCon(null), Slot.class, null, -1);
    }

    public void addItemToChest(@Nonnull ItemStack iStack, final Class validSlot, final Class invalidSlot)
    {
        TransferHandler.TransferItem(iStack, this, getCon(null), validSlot, invalidSlot, -1);
    }

    public AbstractContainerMenu getCon(final Inventory playerInventory)
    {
        return new ContainerMinecart(0, playerInventory, this, dataAccess);
    }

    public void setPlaceholder(final TileEntityCartAssembler assembler)
    {
        isPlaceholder = true;
        placeholderAsssembler = assembler;
    }

    @Override
    public boolean canBeCollidedWith()
    {
        return !isPlaceholder && super.canBeCollidedWith();
    }

    private void generateModels()
    {
        if (modules != null)
        {
            final ArrayList<String> invalid = new ArrayList<>();
            for (final ModuleBase module : modules)
            {
                final ModuleData data = module.getData();
                if (data.haveRemovedModels())
                {
                    invalid.addAll(data.getRemovedModels());
                }
            }
            for (int i = modules.size() - 1; i >= 0; --i)
            {
                final ModuleBase module = modules.get(i);
                final ModuleData data = module.getData();
                if (data != null &&
                        data.haveModels(isPlaceholder))
                {
                    final ArrayList<ModelCartbase> models = new ArrayList<>();
                    for (final String str : data.getModels(isPlaceholder).keySet())
                    {
                        if (!invalid.contains(str))
                        {
                            models.add(data.getModels(isPlaceholder).get(str));
                            invalid.add(str);
                        }
                    }
                    if (models.size() > 0)
                    {
                        module.setModels(models);
                    }
                }
            }
        }
    }

    @Override
    public void writeSpawnData(final FriendlyByteBuf data)
    {
        if (moduleLoadingData == null) return;
        data.writeByte(moduleLoadingData.size());
        for (final ResourceLocation b : moduleLoadingData)
        {
            data.writeResourceLocation(b);
        }
    }

    @Override
    public void readSpawnData(final FriendlyByteBuf data)
    {
        final byte length = data.readByte();
        List<ResourceLocation> list = new ArrayList<>();
        for (int i = 0; i < length; i++)
        {
            list.add(data.readResourceLocation());
        }
        loadModulesFromNames(list);
    }

    public void setScrollY(final int val)
    {
        if (canScrollModules)
        {
            scrollY = val;
        }
    }

    public int getScrollY()
    {
        if (getInterfaceThief() != null)
        {
            return 0;
        }
        return scrollY;
    }

    public int getRealScrollY()
    {
        return (int) ((modularSpaceHeight - MODULAR_SPACE_HEIGHT) / 198.0f * getScrollY());
    }

    public boolean hasCreativeSupplies()
    {
        return creativeSupplies != null;
    }

    @Override
    public boolean canRiderInteract()
    {
        return true;
    }

    @OnlyIn(Dist.CLIENT)
    public void silent()
    {
        keepSilent = 6;
    }

    @OnlyIn(Dist.CLIENT)
    private void updateSounds() {}

    static
    {
        railDirectionCoordinates = new int[][][]{
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

    public Entity getCartRider()
    {
        return getPassengers().isEmpty() ? null : getPassengers().get(0);
    }

    @Nullable
    @Override
    public LivingEntity getControllingPassenger()
    {
        return null; //Works when returning null, not sure why
    }

    public SynchedEntityData getDataManager()
    {
        return entityData;
    }

    int base = 0;

    public int getNextDataWatcher()
    {
        base++;
        return getDataManager().itemsById.size() + base + 1;
    }

    @Override
    public void clearContent() {}

    @Override
    public int getTanks()
    {
        return getModuleTanks().size();
    }

    @Nonnull
    @Override
    public FluidStack getFluidInTank(int tank)
    {
        return tankModules.get(tank).getFluid();
    }

    @Override
    public int getTankCapacity(int tank)
    {
        return tankModules.get(tank).getCapacity();
    }

    @Override
    public boolean isFluidValid(int tank, @Nonnull FluidStack stack)
    {
        return tankModules.get(tank).isFluidValid(stack);
    }

    @Override
    public int fill(FluidStack resource, FluidAction action)
    {
        int amount = 0;
        if (resource != null && resource.getAmount() > 0)
        {
            final FluidStack fluid = resource.copy();
            for (int i = 0; i < tankModules.size(); ++i)
            {
                final int tempAmount = tankModules.get(i).fill(fluid, action);
                amount += tempAmount;
                fluid.shrink(tempAmount);
                if (fluid.getAmount() <= 0)
                {
                    break;
                }
            }
        }
        return amount;
    }

    private FluidStack drain(final FluidStack resource, int maxDrain, final FluidAction doDrain)
    {
        FluidStack ret = resource;
        if (ret != null)
        {
            ret = ret.copy();
            ret.setAmount(0);
        }
        for (int i = 0; i < tankModules.size(); ++i)
        {
            FluidStack temp = null;
            temp = tankModules.get(i).drain(maxDrain, doDrain);
            if (temp != null && (ret == null || ret.isFluidEqual(temp)))
            {
                if (ret == null)
                {
                    ret = temp;
                }
                else
                {
                    ret.grow(temp.getAmount());
                }
                maxDrain -= temp.getAmount();
                if (maxDrain <= 0)
                {
                    break;
                }
            }
        }
        if (ret != null && ret.getAmount() == 0)
        {
            return null;
        }
        return ret;
    }

    public int drain(final Fluid type, int maxDrain, final FluidAction doDrain)
    {
        int amount = 0;
        if (type != null && maxDrain > 0)
        {
            for (final ModuleTank tank : tankModules)
            {
                final FluidStack drained = tank.drain(maxDrain, doDrain);
                if (!drained.isEmpty() && type.isSame(drained.getFluid()))
                {
                    amount += drained.getAmount();
                    maxDrain -= drained.getAmount();
                    if (doDrain == FluidAction.EXECUTE)
                    {
                        tank.drain(drained.getAmount(), doDrain);
                    }
                    if (maxDrain <= 0)
                    {
                        break;
                    }
                }
            }
        }
        return amount;
    }

    @Nonnull
    @Override
    public FluidStack drain(FluidStack resource, FluidAction action)
    {
        return drain(resource, (!resource.isEmpty()) ? 0 : resource.getAmount(), action);
    }

    @Nonnull
    @Override
    public FluidStack drain(int maxDrain, FluidAction action)
    {
        return drain(maxDrain, action);
    }

    @Override
    public void removeVehicle()
    {
        if (!this.level().isClientSide)
        {
            if (!isRemoved())
            {
                ItemEntity cartItem = new ItemEntity(this.level(), this.getExactPosition().getX(), this.getExactPosition().getY(), this.getExactPosition().getZ(), getCartItem());
                this.level().addFreshEntity(cartItem);
            }
        }
        super.removeVehicle();
    }

    protected final SimpleContainerData dataAccess = new SimpleContainerData(0)
    {
        public int get(int id)
        {
            throw new IllegalArgumentException("Invalid index: " + id);
        }

        public void set(int p_221477_1_, int p_221477_2_)
        {
            throw new IllegalStateException("Cannot set values through IIntArray");
        }

        public int getCount()
        {
            return 0;
        }
    };

    AbstractContainerMenu container = null;

    @org.jetbrains.annotations.Nullable
    @Override
    public AbstractContainerMenu createMenu(int id, @NotNull Inventory inventory, @NotNull Player player)
    {
        this.container = new ContainerMinecart(id, inventory, this, dataAccess);
        return container;
    }
}
