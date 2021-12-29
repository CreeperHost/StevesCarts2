package vswe.stevescarts.entitys;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.block.AbstractRailBlock;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.MinecartTickableSound;
import net.minecraft.entity.*;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.item.minecart.ContainerMinecartEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.ByteArrayNBT;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.state.properties.RailShape;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.extensions.IForgeEntityMinecart;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;
import net.minecraftforge.fml.network.NetworkHooks;
import vswe.stevescarts.blocks.tileentities.TileEntityCartAssembler;
import vswe.stevescarts.client.models.ModelCartbase;
import vswe.stevescarts.containers.ContainerMinecart;
import vswe.stevescarts.helpers.ActivatorOption;
import vswe.stevescarts.helpers.CartVersion;
import vswe.stevescarts.helpers.GuiAllocationHelper;
import vswe.stevescarts.helpers.ModuleCountPair;
import vswe.stevescarts.helpers.storages.TransferHandler;
import vswe.stevescarts.init.ModBlocks;
import vswe.stevescarts.init.ModEntities;
import vswe.stevescarts.init.ModItems;
import vswe.stevescarts.modules.IActivatorModule;
import vswe.stevescarts.modules.ModuleBase;
import vswe.stevescarts.modules.addons.ModuleCreativeSupplies;
import vswe.stevescarts.modules.data.ModuleData;
import vswe.stevescarts.modules.engines.ModuleEngine;
import vswe.stevescarts.modules.storages.tanks.ModuleTank;
import vswe.stevescarts.modules.workers.CompWorkModule;
import vswe.stevescarts.modules.workers.ModuleWorker;
import vswe.stevescarts.modules.workers.tools.ModuleTool;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Random;
import java.util.function.Consumer;

;

public class EntityMinecartModular extends ContainerMinecartEntity implements IForgeEntityMinecart, IInventory, IEntityAdditionalSpawnData, IFluidHandler
{

    public BlockPos disabledPos;
    protected boolean wasDisabled;
    //	public double pushX;
    //	public double pushZ;
    public double temppushX;
    public double temppushZ;
    protected boolean engineFlag;
    private int motorRotation;
    private byte[] moduleLoadingData;
    //TODO
    //	private ForgeChunkManager.Ticket cartTicket;

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
    public Random random;
    protected ITextComponent name;
    public byte cartVersion;
    private int scrollY;
    //TODO
    @OnlyIn(Dist.CLIENT)
    private MinecartTickableSound sound;
    //	private MovingSound soundRiding;
    private int keepSilent;

    private static final DataParameter<Boolean> IS_BURNING = EntityDataManager.defineId(EntityMinecartModular.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> IS_DISANABLED = EntityDataManager.defineId(EntityMinecartModular.class, DataSerializers.BOOLEAN);

    public ArrayList<ModuleBase> getModules()
    {
        return modules;
    }

    @Override
    public IPacket<?> getAddEntityPacket()
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

    public EntityMinecartModular(final World world, final double x, final double y, final double z, final CompoundNBT info, final ITextComponent name)
    {
        super(ModEntities.MODULAR_CART.get(), x, y, z, world);
        engineFlag = false;
        random = new Random();
        cartVersion = info.getByte("CartVersion");
        loadModules(info);
        this.name = name;
        for (int i = 0; i < modules.size(); ++i)
        {
            if (modules.get(i).hasExtraData() && info.contains("Data" + i))
            {
                modules.get(i).setExtraData(info.getByte("Data" + i));
            }
        }
    }

    public EntityMinecartModular(World world)
    {
        super(ModEntities.MODULAR_CART.get(), world);
        engineFlag = false;
        random = new Random();
    }

    public EntityMinecartModular(EntityType<EntityMinecartModular> entityType, World world)
    {
        super(entityType, world);
        engineFlag = false;
        random = new Random();
    }

    public EntityMinecartModular(final World world, final TileEntityCartAssembler assembler, final byte[] data)
    {
        this(world);
        setPlaceholder(assembler);
        loadPlaceholderModules(data);
    }

    @Override
    public void defineSynchedData()
    {
        super.defineSynchedData();
        this.entityData.define(IS_BURNING, false);
        this.entityData.define(IS_DISANABLED, false);
    }

    @Deprecated
    private void overrideDatawatcher()
    {
        //TODO AT
        //		entityData = new EntityDataManagerLockable(this);
    }

    private void loadPlaceholderModules(final byte[] data)
    {
        if (modules == null)
        {
            modules = new ArrayList<>();
            doLoadModules(data);
        }
        else
        {
            final ArrayList<Byte> modulesToAdd = new ArrayList<>();
            final ArrayList<Byte> oldModules = new ArrayList<>();
            for (int i = 0; i < moduleLoadingData.length; ++i)
            {
                oldModules.add(moduleLoadingData[i]);
            }
            for (int i = 0; i < data.length; ++i)
            {
                boolean found = false;
                for (int j = 0; j < oldModules.size(); ++j)
                {
                    if (data[i] == oldModules.get(j))
                    {
                        found = true;
                        oldModules.remove(j);
                        break;
                    }
                }
                if (!found)
                {
                    modulesToAdd.add(data[i]);
                }
            }
            for (final byte id : oldModules)
            {
                for (int k = 0; k < modules.size(); ++k)
                {
                    if (id == modules.get(k).getModuleId())
                    {
                        modules.remove(k);
                        break;
                    }
                }
            }
            final byte[] newModuleData = new byte[modulesToAdd.size()];
            for (int l = 0; l < modulesToAdd.size(); ++l)
            {
                newModuleData[l] = modulesToAdd.get(l);
            }
            doLoadModules(newModuleData);
        }
        initModules();
        moduleLoadingData = data;
    }

    private void loadModules(final CompoundNBT info)
    {
        final ByteArrayNBT moduleIDTag = (ByteArrayNBT) info.get("Modules");
        if (moduleIDTag == null)
        {
            return;
        }
        if (level.isClientSide)
        {
            moduleLoadingData = moduleIDTag.getAsByteArray();
        }
        else
        {
            moduleLoadingData = CartVersion.updateCart(this, moduleIDTag.getAsByteArray());
        }
        loadModules(moduleLoadingData);
    }

    public void updateSimulationModules(final byte[] bytes)
    {
        if (!isPlaceholder)
        {
            System.out.println("You're stupid! This is not a placeholder cart.");
        }
        else
        {
            loadPlaceholderModules(bytes);
        }
    }

    protected void loadModules(final byte[] bytes)
    {
        modules = new ArrayList<>();
        doLoadModules(bytes);
        initModules();
    }

    private void doLoadModules(final byte[] bytes)
    {
        for (final byte id : bytes)
        {
            try
            {
                final Class<? extends ModuleBase> moduleClass = ModuleData.getList().get(id).getModuleClass();
                final Constructor moduleConstructor = moduleClass.getConstructor(EntityMinecartModular.class);
                final Object moduleObject = moduleConstructor.newInstance(this);
                final ModuleBase module = (ModuleBase) moduleObject;
                module.setModuleId(id);
                modules.add(module);
            } catch (Exception e)
            {
                System.out.println("Failed to load module with ID " + id + "! More info below.");
                e.printStackTrace();
            }
        }
    }

    private void initModules()
    {
        moduleCounts = new ArrayList<>();
        for (final ModuleBase module : modules)
        {
            final ModuleData data = ModuleData.getList().get(module.getModuleId());
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
        final int x = 0;
        final int y = 0;
        final int maxH = 0;
        int guidata = 0;
        int packets = 0;
        if (level.isClientSide)
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
                            final GuiAllocationHelper guiAllocationHelper = line;
                            guiAllocationHelper.width += module3.guiWidth();
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
    public void remove()
    {
        if (level.isClientSide)
        {
            for (int var1 = 0; var1 < getContainerSize(); ++var1)
            {
                setItem(var1, ItemStack.EMPTY);
            }
        }
        super.remove();
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
    public void renderOverlay(MatrixStack matrixStack, Minecraft minecraft)
    {
        if (modules != null)
        {
            for (final ModuleBase module : modules)
            {
                module.renderOverlay(matrixStack, minecraft);
            }
        }
    }

    @Override
    public void onAddedToWorld()
    {
        if (level.isClientSide && !(entityData instanceof EntityDataManagerLockable))
        {
            overrideDatawatcher();
        }
        super.onAddedToWorld();
    }

    public void updateFuel()
    {
        final int consumption = getConsumption();
        if (consumption > 0)
        {
            final ModuleEngine engine = getCurrentEngine();
            if (engine != null)
            {
                engine.consumeFuel(consumption);
                if (!isPlaceholder && level.isClientSide && hasFuel() && !isDisabled())
                {
                    engine.smoke();
                }
            }
        }
        if (hasFuel())
        {
            if (!isDisabled())
            {
                if (!engineFlag)
                {
                    if (temppushX != 0.0 && temppushZ != 0.0)
                    {
                        setDeltaMovement(temppushX, 0, temppushZ);
                    }
                    else
                    {
                        this.setDeltaMovement(getMaxCartSpeedOnRail(), 0, getMaxCartSpeedOnRail());
                    }
                    engineFlag = true;
                }
                if(getDeltaMovement().x == 0 && getDeltaMovement().z == 0)
                {
                    this.setDeltaMovement(getMaxCartSpeedOnRail(), 0, getMaxCartSpeedOnRail());
                }
            }
            setEngineBurning(hasFuel() && !isDisabled());
        }
        else
        {
            engineFlag = false;
            this.setDeltaMovement(0, 0, 0);
        }
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

    @Override
    public Type getMinecartType()
    {
        return null;
    }

    @Override
    protected float getEyeHeight(Pose p_213316_1_, EntitySize p_213316_2_)
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
    public void destroy(DamageSource p_94095_1_)
    {
        this.remove();
    }

    public boolean dropOnDeath()
    {
        if (isPlaceholder)
        {
            return false;
        }
        if (modules != null)
        {
            for (final ModuleBase module : modules)
            {
                if (!module.dropOnDeath())
                {
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
    public boolean hurt(final DamageSource dmg, final float par2)
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
    public void moveMinecartOnRail(BlockPos pos)
    {
        super.moveMinecartOnRail(pos);
        if (modules != null)
        {
            for (final ModuleBase module : modules)
            {
                module.moveMinecartOnRail(pos);
            }
        }
        BlockState blockState = level.getBlockState(pos);
        RailShape railDirection = ((AbstractRailBlock) blockState.getBlock()).getRailDirection(blockState, level, pos, this);

        if (blockState.getBlock() != ModBlocks.ADVANCED_DETECTOR.get().getBlock() && isDisabled())
        {
            releaseCart();
        }
        boolean canBeDisabled = blockState.getBlock() == ModBlocks.ADVANCED_DETECTOR.get().getBlock();
        //&& (stateBelow.getBlock() != ModBlocks.DETECTOR_UNIT.get().getBlock()); //TODO //|| !DetectorType.getTypeFromSate(stateBelow).canInteractWithCart() || DetectorType.getTypeFromSate(stateBelow).shouldStopCart());
        final boolean forceUnDisable = wasDisabled && disabledPos != null && disabledPos.equals(pos);
        if (!forceUnDisable && wasDisabled)
        {
            wasDisabled = false;
        }
        canBeDisabled = (!forceUnDisable && canBeDisabled);
        if (canBeDisabled && !isDisabled())
        {
            setIsDisabled(true);
            if (getDeltaMovement().x != 0.0 || getDeltaMovement().z != 0.0)
            {
                temppushX = getDeltaMovement().x;
                temppushZ = getDeltaMovement().z;
                final double n = 0.0;
                setDeltaMovement(n, n, n);
            }
            disabledPos = new BlockPos(pos);
        }

        if (!isDisabled())
        {
            temppushX = getDeltaMovement().x;
            temppushZ = getDeltaMovement().z;
        }
    }

    public RailShape getRailDirection(final BlockPos pos)
    {
        if (this.lastRailShape != null)
        {
            return this.lastRailShape;
        }
        return null;
    }

    public void turnback()
    {
        double x = getDeltaMovement().x;
        double z = getDeltaMovement().z;
        setDeltaMovement(x *= -1.0, 0, z *= -1.0);
        temppushX *= -1.0;
        temppushZ *= -1.0;
    }

    public void releaseCart()
    {
        wasDisabled = true;
        setIsDisabled(false);
        setDeltaMovement(temppushX, 0, temppushZ);
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
    public ItemStack getItem(int i)
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
    public ItemStack removeItem(int i, int p_70298_2_)
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
    public ItemStack removeItemNoUpdate(int p_70304_1_)
    {
        return ItemStack.EMPTY;
    }

    @Override
    public void setItem(int i, ItemStack item)
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
    public boolean stillValid(PlayerEntity p_70300_1_)
    {
        return true;
    }

    RailShape lastRailShape;

    @Override
    protected void moveAlongTrack(BlockPos pos, BlockState state)
    {
        if (!getPassengers().isEmpty())
        {
            Entity riddenByEntity = getPassengers().get(0);
            if (riddenByEntity instanceof LivingEntity)
            {
                final float move = ((LivingEntity) riddenByEntity).moveDist;
                ((LivingEntity) riddenByEntity).moveDist = 0.0f;
                super.moveAlongTrack(pos, state);
                ((LivingEntity) riddenByEntity).moveDist = move;
            }
            else
            {
                super.moveAlongTrack(pos, state);
            }
        }
        else
        {
            super.moveAlongTrack(pos, state);
        }
    }

    @Override
    protected void applyNaturalSlowdown()
    {
        if (!hasFuel()) super.applyNaturalSlowdown();
    }

    protected double getPushFactor()
    {
        if (modules != null)
        {
            for (final ModuleBase module : modules)
            {
                final double factor = module.getPushFactor();
                if (factor >= 0.0)
                {
                    return factor;
                }
            }
        }
        return 0.05;
    }

    @Override
    public boolean save(CompoundNBT tagCompound)
    {
        super.save(tagCompound);
        tagCompound.putString("cartName", name.getString());
        tagCompound.putBoolean("engineFlag", engineFlag);
        tagCompound.putDouble("pushX", getDeltaMovement().x);
        tagCompound.putDouble("pushZ", getDeltaMovement().z);
        tagCompound.putDouble("temppushX", temppushX);
        tagCompound.putDouble("temppushZ", temppushZ);
        tagCompound.putShort("workingTime", (short) workingTime);
        tagCompound.putByteArray("Modules", moduleLoadingData);
        tagCompound.putByte("CartVersion", cartVersion);
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

    @Override
    public void load(final CompoundNBT tagCompound)
    {
        super.load(tagCompound);
        name = new TranslationTextComponent(tagCompound.getString("cartName"));
        engineFlag = tagCompound.getBoolean("engineFlag");
        double pushX = tagCompound.getDouble("pushX");
        double pushZ = tagCompound.getDouble("pushZ");
        setDeltaMovement(pushX, 0, pushZ);
        temppushX = tagCompound.getDouble("temppushX");
        temppushZ = tagCompound.getDouble("temppushZ");
        workingTime = tagCompound.getShort("workingTime");
        cartVersion = tagCompound.getByte("CartVersion");

        final int oldVersion = cartVersion;
        loadModules(tagCompound);
        if (modules != null)
        {
            for (int i = 0; i < modules.size(); ++i)
            {
                final ModuleBase module = modules.get(i);
                module.readFromNBT(tagCompound, i);
            }
        }
        if (oldVersion < 2)
        {
            int newSlot = -1;
            int slotCount = 0;
            for (final ModuleBase module2 : modules)
            {
                if (module2 instanceof ModuleTool)
                {
                    newSlot = slotCount;
                    break;
                }
                slotCount += module2.getInventorySize();
            }
            if (newSlot != -1)
            {
                @Nonnull ItemStack lastitem = ItemStack.EMPTY;
                for (int j = newSlot; j < getContainerSize(); ++j)
                {
                    @Nonnull ItemStack thisitem = getItem(j);
                    setItem(j, lastitem);
                    lastitem = thisitem;
                }
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
        int i = MathHelper.floor(this.getX());
        int j = MathHelper.floor(this.getY());
        int k = MathHelper.floor(this.getZ());
        if (this.level.getBlockState(new BlockPos(i, j - 1, k)).is(BlockTags.RAILS))
        {
            --j;
        }

        BlockPos blockpos = new BlockPos(i, j, k);
        BlockState blockState = this.level.getBlockState(blockpos);

        if (blockState.getBlock() instanceof AbstractRailBlock)
        {
            RailShape railshape = ((AbstractRailBlock) blockState.getBlock()).getRailDirection(blockState, this.level, getExactPosition().below(), this);
            if (railshape != null && blockState.getBlock() != ModBlocks.JUNCTION.get())
            {
                lastRailShape = railshape;
            }
        }

        onCartUpdate();
        if (level.isClientSide)
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
            remove();
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
    public ActionResultType interactAt(PlayerEntity entityplayer, Vector3d vec, Hand hand)
    {
        if (isPlaceholder)
        {
            return ActionResultType.FAIL;
        }
        if (modules != null && !entityplayer.isCrouching())
        {
            boolean interupt = false;
            for (final ModuleBase module : modules)
            {
                if (module.onInteractFirst(entityplayer))
                {
                    interupt = true;
                }
            }
            if (interupt)
            {
                return ActionResultType.SUCCESS;
            }
        }
        if (!level.isClientSide)
        {
            NetworkHooks.openGui((ServerPlayerEntity) entityplayer, (INamedContainerProvider) this, new Consumer<PacketBuffer>()
            {
                @Override
                public void accept(PacketBuffer packetBuffer)
                {
                    packetBuffer.writeInt(getId());
                }
            });
        }
        return ActionResultType.SUCCESS;
    }

    @Override
    public ITextComponent getDisplayName()
    {
        return new TranslationTextComponent("entity.minecart");
    }

    public void loadChunks()
    {
        //TODO
        //		loadChunks(cartTicket, x() >> 4, z() >> 4);
    }

    public void loadChunks(final int chunkX, final int chunkZ)
    {
        //TODO
        //		loadChunks(cartTicket, chunkX, chunkZ);
    }

    //	public void loadChunks(final ForgeChunkManager.Ticket ticket) {
    //		loadChunks(ticket, x() >> 4, z() >> 4);
    //	}
    //
    //	public void loadChunks(final ForgeChunkManager.Ticket ticket, final int chunkX, final int chunkZ) {
    //		if (world.isRemote || ticket == null)
    //			return;
    //		if (cartTicket == null)
    //			cartTicket = ticket;
    //		Set<ChunkPos> loadedChunks = ticket.getChunkList();
    //
    //		ArrayList<ChunkPos> newChunks = new ArrayList<>();
    //		for (int i = -1; i <= 1; ++i)
    //			for (int j = -1; j <= 1; ++j)
    //				newChunks.add(new ChunkPos(chunkX + i, chunkZ + j));
    //
    //		for (ChunkPos pos: loadedChunks)
    //			if (!newChunks.contains(pos))
    //				ForgeChunkManager.unforceChunk(cartTicket, pos);
    //		for (ChunkPos pos: newChunks)
    //			if (!loadedChunks.contains(pos))
    //				ForgeChunkManager.forceChunk(cartTicket, pos);
    //	}

    public void initChunkLoading()
    {
        //		if (level.isClientSide || cartTicket != null) {
        //			return;
        //		}
        //		cartTicket = ForgeChunkManager.requestTicket(StevesCarts.instance, world, ForgeChunkManager.Type.ENTITY);
        //		if (cartTicket != null) {
        //			cartTicket.bindEntity(this);
        //			cartTicket.setChunkListDepth(9);
        //			loadChunks();
        //		}
    }

    public void dropChunkLoading()
    {
        //		if (world.isRemote) {
        //			return;
        //		}
        //		if (cartTicket != null) {
        //			ForgeChunkManager.releaseTicket(cartTicket);
        //			cartTicket = null;
        //		}
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
        if (!level.isClientSide && hasFuel())
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
            if (module instanceof IActivatorModule && option.getModule().isAssignableFrom(module.getClass()))
            {
                final IActivatorModule iactivator = (IActivatorModule) module;
                if (option.shouldActivate(isOrange))
                {
                    iactivator.doActivate(option.getId());
                }
                else if (option.shouldDeactivate(isOrange))
                {
                    iactivator.doDeActivate(option.getId());
                }
                else
                {
                    if (!option.shouldToggle())
                    {
                        continue;
                    }
                    if (iactivator.isActive(option.getId()))
                    {
                        iactivator.doDeActivate(option.getId());
                    }
                    else
                    {
                        iactivator.doActivate(option.getId());
                    }
                }
            }
        }
    }

    public ArrayList<String> getLabel()
    {
        final ArrayList<String> label = new ArrayList<>();
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
        return MathHelper.floor(position().x);
    }

    public int y()
    {
        return MathHelper.floor(position().y);
    }

    public int z()
    {
        return MathHelper.floor(position().x);
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

    public Container getCon(final PlayerInventory playerInventory)
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
                    for (final String remove : data.getRemovedModels())
                    {
                        invalid.add(remove);
                    }
                }
            }
            for (int i = modules.size() - 1; i >= 0; --i)
            {
                final ModuleBase module = modules.get(i);
                final ModuleData data = module.getData();
                if (data != null && data.haveModels(isPlaceholder))
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
    public void writeSpawnData(final PacketBuffer data)
    {
        if (moduleLoadingData == null) return;
        data.writeByte(moduleLoadingData.length);
        for (final byte b : moduleLoadingData)
        {
            data.writeByte(b);
        }
        data.writeByte(name.toString().getBytes().length);
        for (final byte b : name.toString().getBytes())
        {
            data.writeByte(b);
        }
    }

    @Override
    public void readSpawnData(final PacketBuffer data)
    {
        final byte length = data.readByte();
        final byte[] bytes = new byte[length];
        data.readBytes(bytes);
        loadModules(bytes);
        //TODO
        //		final int nameLength = data.readByte();
        //		final byte[] nameBytes = new byte[nameLength];
        //		for (int i = 0; i < nameLength; ++i) {
        //			nameBytes[i] = data.readByte();
        //		}
        //		name = new TranslationTextComponent(new String(nameBytes));
        if (getDataManager() instanceof EntityDataManagerLockable)
        {
            ((EntityDataManagerLockable) getDataManager()).release();
        }
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

    //TODO sounds
    @OnlyIn(Dist.CLIENT)
    public void setSound(final MinecartTickableSound sound, final boolean riding)
    {
        //		if (riding) {
        //			soundRiding = sound;
        //		} else {
        this.sound = sound;
        //		}
    }

    @OnlyIn(Dist.CLIENT)
    public void silent()
    {
        keepSilent = 6;
    }

    @OnlyIn(Dist.CLIENT)
    private void updateSounds()
    {
        if (keepSilent > 1)
        {
            --keepSilent;
            stopSound(sound);
            //			stopSound(soundRiding);
            sound = null;
            //			soundRiding = null;
        }
        else if (keepSilent == 1)
        {
            keepSilent = 0;
            Minecraft.getInstance().getSoundManager().play(new MinecartTickableSound(this));
            //			Minecraft.getMinecraft().getSoundHandler().playSound(new MovingSoundMinecartRiding(Minecraft.getMinecraft().player, this));
        }
    }

    @OnlyIn(Dist.CLIENT)
    private void stopSound(final MinecartTickableSound sound)
    {
        if (sound != null)
        {
            Minecraft.getInstance().getSoundManager().stop(sound);
        }
    }

    static
    {
        railDirectionCoordinates = new int[][][]{{{0, 0, -1}, {0, 0, 1}}, {{-1, 0, 0}, {1, 0, 0}}, {{-1, -1, 0}, {1, 0, 0}}, {{-1, 0, 0}, {1, -1, 0}}, {{0, 0, -1}, {0, -1, 1}}, {{0, -1, -1}, {0, 0, 1}}, {{0, 0, 1}, {1, 0, 0}}, {{0, 0, 1}, {-1, 0, 0}}, {{0, 0, -1}, {-1, 0, 0}}, {{0, 0, -1}, {1, 0, 0}}};
    }

    public Entity getCartRider()
    {
        return getPassengers().isEmpty() ? null : getPassengers().get(0);
    }

    @Nullable
    @Override
    public Entity getControllingPassenger()
    {
        return null; //Works when returning null, not sure why
    }

    public EntityDataManager getDataManager()
    {
        return entityData;
    }

    int base = 0;

    public int getNextDataWatcher()
    {
        base++;
        return getDataManager().getAll().size() + base + 1;
    }

    @Override
    public void clearContent()
    {
    }

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
                final FluidStack fluidStack = fluid;
                fluidStack.shrink(tempAmount);
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
                    final FluidStack fluidStack = ret;
                    fluidStack.grow(temp.getAmount());
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
                    continue;
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
    public void remove(boolean keepData)
    {
        if (!this.level.isClientSide)
        {
            if (!removed)
            {
                ItemEntity cartItem = new ItemEntity(this.level, this.getExactPosition().getX(), this.getExactPosition().getY(), this.getExactPosition().getZ(), getCartItem());
                this.level.addFreshEntity(cartItem);
            }
        }
        super.remove(keepData);
    }

    protected final IIntArray dataAccess = new IIntArray()
    {
        public int get(int id)
        {
            switch (id)
            {
                default:
                    throw new IllegalArgumentException("Invalid index: " + id);
            }
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

    Container container = null;

    @Override
    protected Container createMenu(int id, PlayerInventory playerInventory)
    {
        Container container = new ContainerMinecart(id, playerInventory, this, dataAccess);
        this.container = container;
        return container;
    }
}
