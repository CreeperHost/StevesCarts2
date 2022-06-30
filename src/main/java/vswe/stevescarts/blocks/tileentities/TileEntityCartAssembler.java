package vswe.stevescarts.blocks.tileentities;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.Connection;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import vswe.stevescarts.Constants;
import vswe.stevescarts.SCConfig;
import vswe.stevescarts.blocks.BlockCartAssembler;
import vswe.stevescarts.containers.ContainerCartAssembler;
import vswe.stevescarts.containers.ContainerUpgrade;
import vswe.stevescarts.containers.slots.SlotAssembler;
import vswe.stevescarts.containers.slots.SlotAssemblerFuel;
import vswe.stevescarts.containers.slots.SlotHull;
import vswe.stevescarts.containers.slots.SlotOutput;
import vswe.stevescarts.entitys.EntityMinecartModular;
import vswe.stevescarts.helpers.*;
import vswe.stevescarts.helpers.storages.TransferHandler;
import vswe.stevescarts.init.ModBlocks;
import vswe.stevescarts.init.ModItems;
import vswe.stevescarts.items.ItemCartModule;
import vswe.stevescarts.items.ItemCarts;
import vswe.stevescarts.modules.data.ModuleData;
import vswe.stevescarts.modules.data.ModuleDataHull;
import vswe.stevescarts.polylib.NBTHelper;
import vswe.stevescarts.upgrades.*;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;

public class TileEntityCartAssembler extends TileEntityBase implements WorldlyContainer, MenuProvider
{
    private int maxAssemblingTime;
    private float currentAssemblingTime;
    private int fuelCheckTimer;
    @Nonnull
    protected ItemStack outputItem = ItemStack.EMPTY;
    protected NonNullList<ItemStack> spareModules;
    private boolean isAssembling;
    public boolean isErrorListOutdated;
    private ArrayList<TitleBox> titleBoxes;
    private ArrayList<DropDownMenuItem> dropDownItems;
    private SimulationInfo info;
    private boolean shouldSpin;
    private EntityMinecartModular placeholder;
    private float yaw;
    private float roll;
    private boolean rolldown;
    private ArrayList<SlotAssembler> slots;
    private ArrayList<SlotAssembler> engineSlots;
    private ArrayList<SlotAssembler> addonSlots;
    private ArrayList<SlotAssembler> chestSlots;
    private ArrayList<SlotAssembler> funcSlots;
    private SlotHull hullSlot;
    private SlotAssembler toolSlot;
    private SlotOutput outputSlot;
    private SlotAssemblerFuel fuelSlot;
    private final int[] topbotSlots;
    private final int[] sideSlots;
    @Nonnull
    private ItemStack lastHull = ItemStack.EMPTY;
    private float fuelLevel;
    private ArrayList<TileEntityUpgrade> upgrades;
    public boolean isDead;
    private boolean loaded;
    NonNullList<ItemStack> inventoryStacks;
    protected final SimpleContainerData dataAccess = new SimpleContainerData(0)
    {
        public int get(int p_221476_1_)
        {
            switch (p_221476_1_)
            {
                case 0:
                    return getShortFromInt(true, maxAssemblingTime);
                case 1:
                    return getShortFromInt(false, maxAssemblingTime);
                case 2:
                    return getShortFromInt(true, getAssemblingTime());
                case 3:
                    return getShortFromInt(false, getAssemblingTime());
                case 4:
                    return (short) (isAssembling ? 1 : 0);
                case 5:
                    return getShortFromInt(true, getFuelLevel());
                case 6:
                    return getShortFromInt(false, getFuelLevel());
                default:
                    throw new IllegalArgumentException("Invalid index: " + p_221476_1_);
            }
        }

        public void set(int p_221477_1_, int p_221477_2_)
        {
            throw new IllegalStateException("Cannot set values through IIntArray");
        }

        public int getCount()
        {
            return 7;
        }
    };

    public static final String MODIFY_STATUS = "ModifyStatus";

    public TileEntityCartAssembler(BlockPos blockPos, BlockState blockState)
    {
        super(ModBlocks.CART_ASSEMBLER_TILE.get(), blockPos, blockState);
        currentAssemblingTime = -1.0f;
        shouldSpin = true;
        yaw = 0.0f;
        roll = 0.0f;
        rolldown = false;
        upgrades = new ArrayList<>();
        spareModules = NonNullList.create();
        dropDownItems = new ArrayList<>();
        slots = new ArrayList<>();
        engineSlots = new ArrayList<>();
        addonSlots = new ArrayList<>();
        chestSlots = new ArrayList<>();
        funcSlots = new ArrayList<>();
        titleBoxes = new ArrayList<>();
        int slotID = 0;
        hullSlot = new SlotHull(this, slotID++, 18, 25);
        slots.add(hullSlot);
        final TitleBox engineBox = new TitleBox(0, 65, 16225309);
        final TitleBox toolBox = new TitleBox(1, 100, 6696337);
        final TitleBox attachBox = new TitleBox(2, 135, 23423);
        final TitleBox storageBox = new TitleBox(3, 170, 10357518);
        final TitleBox addonBox = new TitleBox(4, 205, 22566);
        final TitleBox infoBox = new TitleBox(5, 375, 30, 13417984);
        titleBoxes.add(engineBox);
        titleBoxes.add(toolBox);
        titleBoxes.add(attachBox);
        titleBoxes.add(storageBox);
        titleBoxes.add(addonBox);
        titleBoxes.add(infoBox);
        for (int i = 0; i < 5; ++i)
        {
            final SlotAssembler slot = new SlotAssembler(this, slotID++, engineBox.getX() + 2 + 18 * i, engineBox.getY(), 1, false, i);
            slot.invalidate();
            slots.add(slot);
            engineSlots.add(slot);
        }
        toolSlot = new SlotAssembler(this, slotID++, toolBox.getX() + 2, toolBox.getY(), 2, false, 0);
        slots.add(toolSlot);
        toolSlot.invalidate();
        for (int i = 0; i < 6; ++i)
        {
            final SlotAssembler slot = new SlotAssembler(this, slotID++, attachBox.getX() + 2 + 18 * i, attachBox.getY(), -1, false, i);
            slot.invalidate();
            slots.add(slot);
            funcSlots.add(slot);
        }
        for (int i = 0; i < 4; ++i)
        {
            final SlotAssembler slot = new SlotAssembler(this, slotID++, storageBox.getX() + 2 + 18 * i, storageBox.getY(), 3, false, i);
            slot.invalidate();
            slots.add(slot);
            chestSlots.add(slot);
        }
        for (int i = 0; i < 12; ++i)
        {
            final SlotAssembler slot = new SlotAssembler(this, slotID++, addonBox.getX() + 2 + 18 * (i % 6), addonBox.getY() + 18 * (i / 6), 4, false, i);
            slot.invalidate();
            slots.add(slot);
            addonSlots.add(slot);
        }
        fuelSlot = new SlotAssemblerFuel(this, slotID++, 395, 220);
        slots.add(fuelSlot);
        outputSlot = new SlotOutput(this, slotID++, 450, 220);
        outputSlot.invalidate();
        slots.add(outputSlot);
        info = new SimulationInfo();
        inventoryStacks = NonNullList.withSize(slots.size(), ItemStack.EMPTY);
        topbotSlots = new int[]{getContainerSize() - nonModularSlots()};
        sideSlots = new int[]{getContainerSize() - nonModularSlots() + 1};
    }

    public void clearUpgrades()
    {
        upgrades.clear();
    }

    public void addUpgrade(final TileEntityUpgrade upgrade)
    {
        upgrades.add(upgrade);
    }

    public void removeUpgrade(final TileEntityUpgrade upgrade)
    {
        upgrades.remove(upgrade);
    }

    public ArrayList<TileEntityUpgrade> getUpgradeTiles()
    {
        return upgrades;
    }

    public ArrayList<AssemblerUpgrade> getUpgrades()
    {
        final ArrayList<AssemblerUpgrade> lst = new ArrayList<>();
        for (final TileEntityUpgrade tile : upgrades)
        {
            lst.add(tile.getUpgrade());
        }
        return lst;
    }

    public ArrayList<BaseEffect> getEffects()
    {
        final ArrayList<BaseEffect> lst = new ArrayList<>();
        for (final TileEntityUpgrade tile : upgrades)
        {
            final AssemblerUpgrade upgrade = tile.getUpgrade();
            if (upgrade != null)
            {
                for (final BaseEffect effect : upgrade.getEffects())
                {
                    lst.add(effect);
                }
            }
        }
        return lst;
    }

    public SimulationInfo getSimulationInfo()
    {
        return info;
    }

    public ArrayList<DropDownMenuItem> getDropDown()
    {
        return dropDownItems;
    }

    public ArrayList<TitleBox> getTitleBoxes()
    {
        return titleBoxes;
    }

    public static int getRemovedSize()
    {
        return -1;
    }

    public static int getKeepSize()
    {
        return 0;
    }

    public ArrayList<SlotAssembler> getSlots()
    {
        return slots;
    }

    public ArrayList<SlotAssembler> getEngines()
    {
        return engineSlots;
    }

    public ArrayList<SlotAssembler> getChests()
    {
        return chestSlots;
    }

    public ArrayList<SlotAssembler> getAddons()
    {
        return addonSlots;
    }

    public ArrayList<SlotAssembler> getFuncs()
    {
        return funcSlots;
    }

    public SlotAssembler getToolSlot()
    {
        return toolSlot;
    }

    public int getMaxAssemblingTime()
    {
        return maxAssemblingTime;
    }

    public int getAssemblingTime()
    {
        return (int) currentAssemblingTime;
    }

    private void setAssemblingTime(final int val)
    {
        currentAssemblingTime = val;
    }

    public boolean getIsAssembling()
    {
        return isAssembling;
    }

    public void doAssemble()
    {
        if (!hasErrors())
        {
            maxAssemblingTime = generateAssemblingTime();
            createCartFromModules();
            isAssembling = true;
            for (final TileEntityUpgrade tile : getUpgradeTiles())
            {
                if (tile.getUpgrade() != null)
                {
                    for (final BaseEffect effect : tile.getUpgrade().getEffects())
                    {
                        if (effect instanceof Disassemble)
                        {
                            @Nonnull ItemStack oldcart = tile.getItem(0);
                            if (!oldcart.isEmpty() && !outputItem.isEmpty() && oldcart.getItem() instanceof ItemCarts && outputItem.getItem() instanceof ItemCarts && oldcart.hasCustomHoverName())
                            {
                                outputItem.setHoverName(oldcart.getDisplayName());
                            }
                            tile.setItem(0, ItemStack.EMPTY);
                        }
                    }
                }
            }
        }
    }

    public void receivePacket(final int id, final byte[] data, final ServerPlayer player)
    {
        if (id == 0)
        {
            doAssemble();
        }
        else if (id == 1)
        {
            final int slotId = data[0];
            if (slotId >= 1 && slotId < getSlots().size())
            {
                final SlotAssembler slot = getSlots().get(slotId);
                if (!slot.getItem().isEmpty())
                {
                    CompoundTag comp = getOrCreateCompound(slot.getItem());
                    if (comp.getInt(MODIFY_STATUS) == getKeepSize())
                    {
                        comp.putInt(MODIFY_STATUS, getRemovedSize());
                    }
                    else
                    {
                        comp.putInt(MODIFY_STATUS, getKeepSize());
                    }
                }
            }
        }
    }

    public static CompoundTag getOrCreateCompound(ItemStack stack)
    {
        if (!stack.hasTag() && !stack.isEmpty()) stack.setTag(new CompoundTag());
        return stack.getTag();
    }

    public static int getSlotStatus(ItemStack stack)
    {
        if (stack.hasTag() && stack.getTag().contains(TileEntityCartAssembler.MODIFY_STATUS, NBTHelper.INT.getId()))
            return stack.getTag().getInt(TileEntityCartAssembler.MODIFY_STATUS);
        return 1;
    }

    public static ItemStack removeModify(ItemStack stack)
    {
        if (stack.hasTag() && stack.getTag().contains(MODIFY_STATUS, NBTHelper.INT.getId()))
        {
            stack.getTag().remove(MODIFY_STATUS);
            if (stack.getTag().size() <= 0) stack.setTag(null);
        }
        return stack;
    }

    public void onUpgradeUpdate()
    {
    }

    public int generateAssemblingTime()
    {
        if (SCConfig.disableTimedCrafting.get())
        {
            return 1;
        }

        return generateAssemblingTime(getModules(true, new int[]{getKeepSize(), getRemovedSize()}), getModules(true, new int[]{getKeepSize(), 1}));
    }

    private int generateAssemblingTime(final ArrayList<ModuleData> modules, final ArrayList<ModuleData> removed)
    {
        int timeRequired = 100;
        for (final ModuleData module : modules)
        {
            timeRequired += getAssemblingTime(module, false);
        }
        for (final ModuleData module : removed)
        {
            timeRequired += getAssemblingTime(module, true);
        }
        for (final BaseEffect effect : getEffects())
        {
            if (effect instanceof TimeFlatCart)
            {
                timeRequired += ((TimeFlatCart) effect).getTicks();
            }
        }
        return Math.max(0, timeRequired);
    }

    private int getAssemblingTime(final ModuleData module, final boolean isRemoved)
    {
        int time = (int) (5.0 * Math.pow(module.getCost(), 2.2));
        time += getTimeDecreased(isRemoved);
        return Math.max(0, time);
    }

    @Nonnull
    public ItemStack getCartFromModules(final boolean isSimulated)
    {
        final NonNullList<ItemStack> items = NonNullList.create();
        for (int i = 0; i < getContainerSize() - nonModularSlots(); ++i)
        {
            @Nonnull ItemStack item = getItem(i);
            if (!item.isEmpty())
            {
                if (getSlotStatus(item) != getRemovedSize())
                {
                    items.add(item);
                }
                else if (!isSimulated)
                {
                    @Nonnull ItemStack spare = item.copy();
                    spare.setCount(1);
                    spareModules.add(spare);
                }
            }
        }
        if (items.size() == 1)
        {
            return removeModify(items.get(0));
        }
        return ModuleData.createModularCartFromItems(items);
    }

    private void createCartFromModules()
    {
        spareModules.clear();
        outputItem = getCartFromModules(false);
        if (!outputItem.isEmpty())
        {
            for (int i = 0; i < getContainerSize() - nonModularSlots(); ++i)
            {
                setItem(i, ItemStack.EMPTY);
            }
        }
        else
        {
            spareModules.clear();
        }
    }

    public ArrayList<ModuleData> getNonHullModules()
    {
        return getModules(false);
    }

    public ArrayList<ModuleData> getModules(final boolean includeHull)
    {
        return getModules(includeHull, new int[]{getRemovedSize()});
    }

    public ArrayList<ModuleData> getModules(final boolean includeHull, final int[] invalid)
    {
        final ArrayList<ModuleData> modules = new ArrayList<>();
        for (int i = includeHull ? 0 : 1; i < getContainerSize() - nonModularSlots(); ++i)
        {
            @Nonnull ItemStack item = getItem(i);
            if (!item.isEmpty())
            {
                boolean validSize = true;
                for (int j = 0; j < invalid.length; ++j)
                {
                    if (invalid[j] == getSlotStatus(item) || (invalid[j] > 0 && getSlotStatus(item) > 0))
                    {
                        validSize = false;
                        break;
                    }
                }
                if (validSize)
                {
                    if (item.getItem() instanceof ItemCartModule itemCartModule)
                    {
                        final ModuleData module = itemCartModule.getModuleData();
                        if (module != null)
                        {
                            modules.add(module);
                        }
                    }
                }
            }
        }
        return modules;
    }

    public ModuleDataHull getHullModule()
    {
        if (!getItem(0).isEmpty())
        {
            ItemStack stack = getItem(0);
            if (stack.getItem() instanceof ItemCartModule itemCartModule)
            {
                final ModuleData hulldata = itemCartModule.getModuleData();
                if (hulldata instanceof ModuleDataHull)
                {
                    return (ModuleDataHull) hulldata;
                }
            }
        }
        return null;
    }

    private boolean hasErrors()
    {
        return getErrors().size() > 0;
    }

    public ArrayList<String> getErrors()
    {
        final ArrayList<String> errors = new ArrayList<>();
        if (hullSlot.getItem().isEmpty())
        {
            errors.add(Localization.GUI.ASSEMBLER.HULL_ERROR.translate());
        }
        else
        {
            ItemCartModule itemCartModule = (ItemCartModule) getItem(0).getItem();
            final ModuleData hulldata = itemCartModule.getModuleData();
            if (hulldata == null || !(hulldata instanceof ModuleDataHull))
            {
                errors.add(Localization.GUI.ASSEMBLER.INVALID_HULL_SHORT.translate());
            }
            else
            {
                if (isAssembling)
                {
                    errors.add(Localization.GUI.ASSEMBLER.BUSY.translate());
                }
                else if (outputSlot != null && !outputSlot.getItem().isEmpty())
                {
                    errors.add(Localization.GUI.ASSEMBLER.DEPARTURE_BAY.translate());
                }
                final ArrayList<ModuleData> modules = new ArrayList<>();
                for (int i = 0; i < getContainerSize() - nonModularSlots(); ++i)
                {
                    if (!getItem(i).isEmpty())
                    {
                        ItemCartModule itemCartModule1 = (ItemCartModule) getItem(i).getItem();
                        final ModuleData data = itemCartModule1.getModuleData();
                        if (data != null)
                        {
                            modules.add(data);
                        }
                    }
                }
                final String error = ModuleData.checkForErrors((ModuleDataHull) hulldata, modules);
                if (error != null)
                {
                    errors.add(error);
                }
            }
        }
        return errors;
    }

    public int getTotalCost()
    {
        final ArrayList<ModuleData> modules = new ArrayList<>();
        for (int i = 0; i < getContainerSize() - nonModularSlots(); ++i)
        {
            if (!getItem(i).isEmpty() && getItem(i).getItem() instanceof ItemCartModule itemCartModule)
            {
                final ModuleData data = itemCartModule.getModuleData();
                if (data != null)
                {
                    modules.add(data);
                }
            }
        }
        return ModuleData.getTotalCost(modules);
    }

    private void invalidateAll()
    {
        for (int i = 0; i < getEngines().size(); ++i)
        {
            getEngines().get(i).invalidate();
        }
        for (int i = 0; i < getAddons().size(); ++i)
        {
            getAddons().get(i).invalidate();
        }
        for (int i = 0; i < getChests().size(); ++i)
        {
            getChests().get(i).invalidate();
        }
        for (int i = 0; i < getFuncs().size(); ++i)
        {
            getFuncs().get(i).invalidate();
        }
        getToolSlot().invalidate();
    }

    private void validateAll()
    {
        if (hullSlot == null)
        {
            return;
        }
        final ArrayList<SlotAssembler> slots = getValidSlotFromHullItem(hullSlot.getItem());
        if (slots != null)
        {
            for (final SlotAssembler slot : slots)
            {
                slot.validate();
            }
        }
    }

    public ArrayList<SlotAssembler> getValidSlotFromHullItem(@Nonnull ItemStack hullitem)
    {
        if (!hullitem.isEmpty())
        {
            if (hullitem.getItem() instanceof ItemCartModule itemCartModule)
            {
                ModuleData moduleData = itemCartModule.getModuleData();
                if (moduleData != null && moduleData instanceof ModuleDataHull moduleDataHull)
                {
                    return getValidSlotFromHull(moduleDataHull);
                }
            }
        }
        return null;
    }

    private ArrayList<SlotAssembler> getValidSlotFromHull(final ModuleDataHull hull)
    {
        final ArrayList<SlotAssembler> slots = new ArrayList<>();
        for (int i = 0; i < hull.getEngineMax(); ++i)
        {
            slots.add(getEngines().get(i));
        }
        for (int i = 0; i < hull.getAddonMax(); ++i)
        {
            slots.add(getAddons().get(i));
        }
        for (int i = 0; i < getChests().size(); ++i)
        {
            slots.add(getChests().get(i));
        }
        for (int i = 0; i < getFuncs().size(); ++i)
        {
            slots.add(getFuncs().get(i));
        }
        slots.add(getToolSlot());
        return slots;
    }

    public int getMaxFuelLevel()
    {
        int capacity = 4000;
        for (final BaseEffect effect : getEffects())
        {
            if (effect instanceof FuelCapacity)
            {
                capacity += ((FuelCapacity) effect).getFuelCapacity();
            }
        }
        if (capacity > 200000)
        {
            capacity = 200000;
        }
        else if (capacity < 1)
        {
            capacity = 1;
        }
        return capacity;
    }

    public boolean isCombustionFuelValid()
    {
        for (final BaseEffect effect : getEffects())
        {
            if (effect instanceof CombustionFuel)
            {
                return true;
            }
        }
        return false;
    }

    public int getFuelLevel()
    {
        return (int) fuelLevel;
    }

    public void setFuelLevel(final int val)
    {
        fuelLevel = val;
    }

    private int getTimeDecreased(final boolean isRemoved)
    {
        int timeDecr = 0;
        for (final BaseEffect effect : getEffects())
        {
            if (effect instanceof TimeFlat && !(effect instanceof TimeFlatRemoved))
            {
                timeDecr += ((TimeFlat) effect).getTicks();
            }
        }
        if (isRemoved)
        {
            for (final BaseEffect effect : getEffects())
            {
                if (effect instanceof TimeFlatRemoved)
                {
                    timeDecr += ((TimeFlat) effect).getTicks();
                }
            }
        }
        return timeDecr;
    }

    private float getFuelCost()
    {
        float cost = 1.0f;
        for (final BaseEffect effect : getEffects())
        {
            if (effect instanceof FuelCost)
            {
                cost += ((FuelCost) effect).getCost();
            }
        }
        return cost;
    }

    public float getEfficiency()
    {
        float efficiency = 1.0f;
        for (final BaseEffect effect : getEffects())
        {
            if (effect instanceof WorkEfficiency)
            {
                efficiency += ((WorkEfficiency) effect).getEfficiency();
            }
        }
        return efficiency;
    }

    private void deployCart()
    {
    }

    private void deploySpares()
    {
        for (final TileEntityUpgrade tile : getUpgradeTiles())
        {
            if (tile.getUpgrade() != null)
            {
                for (final BaseEffect effect : tile.getUpgrade().getEffects())
                {
                    if (effect instanceof Disassemble)
                    {
                        for (@Nonnull ItemStack item : spareModules)
                        {
                            item = removeModify(item);
                            TransferHandler.TransferItem(item, tile, new ContainerUpgrade(0, null, tile, new SimpleContainerData(0)), 1);
                            if (item.getCount() > 0)
                            {
                                puke(item);
                            }
                        }
                    }
                }
            }
        }
    }

    public void puke(@Nonnull ItemStack item)
    {
        if(level == null) return;

        final ItemEntity entityitem = new ItemEntity(level, getBlockPos().getX(), getBlockPos().getY() + 0.25, getBlockPos().getZ(), item);
        level.addFreshEntity(entityitem);
    }

    @Override
    public void tick()
    {
        if(level == null) return;
        if (!loaded)
        {
            ((BlockCartAssembler) ModBlocks.CART_ASSEMBLER.get()).updateMultiBlock(level, getBlockPos());
            loaded = true;
        }
        if (!isAssembling && outputSlot != null && !outputSlot.getItem().isEmpty())
        {
            @Nonnull ItemStack itemInSlot = outputSlot.getItem();
            if (itemInSlot.getItem() == ModItems.CARTS.get())
            {
                final CompoundTag info = itemInSlot.getTag();
                if (info != null && info.contains("maxTime"))
                {
                    @Nonnull ItemStack newItem = new ItemStack(ModItems.CARTS.get());
                    final CompoundTag save = new CompoundTag();
                    save.putByteArray("Modules", info.getByteArray("Modules"));
                    newItem.setTag(save);
                    final int modulecount = info.getByteArray("Modules").length;
                    maxAssemblingTime = info.getInt("maxTime");
                    setAssemblingTime(info.getInt("currentTime"));
                    spareModules.clear();
                    if (itemInSlot.hasCustomHoverName())
                    {
                        newItem.setHoverName(itemInSlot.getDisplayName());
                    }
                    isAssembling = true;
                    outputItem = newItem;
                    outputSlot.set(ItemStack.EMPTY);
                }
            }
        }
        if (getFuelLevel() > getMaxFuelLevel())
        {
            setFuelLevel(getMaxFuelLevel());
        }
        if (isAssembling && outputSlot != null && getFuelLevel() >= getFuelCost())
        {
            currentAssemblingTime += getEfficiency();
            fuelLevel -= getFuelCost();
            if (getFuelLevel() <= 0)
            {
                setFuelLevel(0);
            }
            if (getAssemblingTime() >= maxAssemblingTime)
            {
                isAssembling = false;
                setAssemblingTime(0);
                if (!outputItem.isEmpty())
                {
                    setItem(outputSlot.getSlotIndex(), outputItem);
                }
                if (!level.isClientSide)
                {
                    deployCart();
                    outputItem = ItemStack.EMPTY;
                    deploySpares();
                    spareModules.clear();
                }
            }
        }
        if (!level.isClientSide() && fuelCheckTimer-- <= 0 && fuelSlot != null && !fuelSlot.getItem().isEmpty() && getFuelLevel() < getMaxFuelLevel())
        {
            final int fuel = fuelSlot.getFuelLevel(fuelSlot.getItem());
            if (fuel > 0 && getFuelLevel() + fuel <= getMaxFuelLevel())
            {
                setFuelLevel(getFuelLevel() + fuel);
                if (fuelSlot.getItem().getItem().hasContainerItem(fuelSlot.getItem()))
                {
                    fuelSlot.set(fuelSlot.getItem().getContainerItem());
                }
                else
                {
                    @Nonnull ItemStack stack = fuelSlot.getItem();
                    stack.shrink(1);
                }
                if (fuelSlot.getItem().getCount() <= 0)
                {
                    fuelSlot.set(ItemStack.EMPTY);
                }
            }
            else
            {
                fuelCheckTimer = 20;
            }
        }
        updateSlots();
        handlePlaceholder();
    }

    public void updateSlots()
    {
        if (hullSlot != null)
        {
            if (!lastHull.isEmpty() && hullSlot.getItem().isEmpty())
            {
                invalidateAll();
            }
            else if (lastHull.isEmpty() && !hullSlot.getItem().isEmpty())
            {
                validateAll();
            }
            else if (lastHull != hullSlot.getItem())
            {
                invalidateAll();
                validateAll();
            }
            lastHull = hullSlot.getItem();
        }
        for (final SlotAssembler slot : slots)
        {
            slot.setChanged();
        }
    }

    public void resetPlaceholder()
    {
        placeholder = null;
    }

    public EntityMinecartModular getPlaceholder()
    {
        return placeholder;
    }

    public float getYaw()
    {
        return yaw;
    }

    public float getRoll()
    {
        return roll;
    }

    public void setYaw(final float val)
    {
        yaw = val;
    }

    public void setRoll(final float val)
    {
        roll = val;
    }

    public void setSpinning(final boolean val)
    {
        shouldSpin = val;
    }

    public boolean shouldSpin()
    {
        return shouldSpin;
    }

    public int nonModularSlots()
    {
        return 2;
    }

    private void handlePlaceholder()
    {
        if(level == null) return;

        if (level.isClientSide)
        {
            if (placeholder == null)
            {
                return;
            }
            if (!Constants.freezeCartSimulation)
            {
                final int minRoll = -5;
                final int maxRoll = 25;
                if (shouldSpin)
                {
                    yaw += 2.0f;
                    roll %= 360.0f;
                    if (!rolldown)
                    {
                        if (roll < minRoll - 3)
                        {
                            roll += 5.0f;
                        }
                        else
                        {
                            roll += 0.2f;
                        }
                        if (roll > maxRoll)
                        {
                            rolldown = true;
                        }
                    }
                    else
                    {
                        if (roll > maxRoll + 3)
                        {
                            roll -= 5.0f;
                        }
                        else
                        {
                            roll -= 0.2f;
                        }
                        if (roll < minRoll)
                        {
                            rolldown = false;
                        }
                    }
                }
            }
            placeholder.onCartUpdate();
            if (placeholder == null)
            {
                return;
            }
            placeholder.updateFuel();
        }
    }

    @Override
    public int @NotNull [] getSlotsForFace(@NotNull Direction direction)
    {
        return new int[0];
    }

    @Override
    public boolean canPlaceItemThroughFace(int id, @NotNull ItemStack itemStack, @Nullable Direction direction)
    {
        return false;
    }

    @Override
    public boolean canTakeItemThroughFace(int id, @NotNull ItemStack itemStack, @NotNull Direction direction)
    {
        return false;
    }

    @Override
    public @NotNull Component getDisplayName()
    {
        return Component.literal("tile.cart.assembler");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int id, @NotNull Inventory playerInventory, @NotNull Player player)
    {
        return new ContainerCartAssembler(id, playerInventory, this, this.dataAccess);
    }

    public void createPlaceholder()
    {
        if (placeholder == null)
        {
            placeholder = new EntityMinecartModular(level, this, getModularInfo());
            updateRenderMenu();
            isErrorListOutdated = true;
        }
    }

    public void updatePlaceholder()
    {
        if (placeholder != null)
        {
            placeholder.updateSimulationModules(getModularInfo());
            updateRenderMenu();
            isErrorListOutdated = true;
        }
    }

    private void updateRenderMenu()
    {
        final ArrayList<DropDownMenuItem> list = info.getList();
        dropDownItems.clear();
        for (final DropDownMenuItem item : list)
        {
            if (item.getModuleClass() == null)
            {
                dropDownItems.add(item);
            }
            else
            {
                for (int i = 0; i < getContainerSize() - nonModularSlots(); ++i)
                {
                    if (!getItem(i).isEmpty() && ModuleData.isItemOfModularType(getItem(i), item.getModuleClass()) && (item.getExcludedClass() == null || !ModuleData.isItemOfModularType(getItem(i), item.getExcludedClass())))
                    {
                        dropDownItems.add(item);
                        break;
                    }
                }
            }
        }
    }

    private ArrayList<ResourceLocation> getModularInfo()
    {
        final ArrayList<ResourceLocation> datalist = new ArrayList<>();
        for (int i = 0; i < getContainerSize() - nonModularSlots(); ++i)
        {
            if (!getItem(i).isEmpty())
            {
                if (getItem(i).getItem() instanceof ItemCartModule itemCartModule)
                {
                    final ModuleData data = itemCartModule.getModuleData();
                    if (data != null)
                    {
                        datalist.add(data.getID());
                    }
                }
            }
        }
        return datalist;
    }

    public boolean getIsDisassembling()
    {
        for (int i = 0; i < getContainerSize() - nonModularSlots(); ++i)
        {
            if (!getItem(i).isEmpty() && getSlotStatus(getItem(i)) <= 0)
            {
                return true;
            }
        }
        return false;
    }

    @Override
    public int getContainerSize()
    {
        return inventoryStacks.size();
    }

    @Override
    public boolean isEmpty()
    {
        return false;
    }

    @Override
    public @NotNull ItemStack getItem(int i)
    {
        return i >= 0 && i < this.inventoryStacks.size() ? (ItemStack) this.inventoryStacks.get(i) : ItemStack.EMPTY;
    }

    @Override
    public @NotNull ItemStack removeItem(int i, int j)
    {
        ItemStack itemStack = ContainerHelper.removeItem(this.inventoryStacks, i, j);
        if (!itemStack.isEmpty()) this.setChanged();
        return itemStack;
    }

    @Override
    public @NotNull ItemStack removeItemNoUpdate(int i)
    {
        ItemStack itemStack = (ItemStack) this.inventoryStacks.get(i);
        if (itemStack.isEmpty())
        {
            return ItemStack.EMPTY;
        }
        else
        {
            this.inventoryStacks.set(i, ItemStack.EMPTY);
            return itemStack;
        }
    }

    @Override
    public void setItem(int i, @NotNull ItemStack itemStack)
    {
        this.inventoryStacks.set(i, itemStack);
        this.setChanged();
    }

    @Override
    public boolean stillValid(@NotNull Player player)
    {
        return true;
    }

    @Override
    public void clearContent()
    {
        inventoryStacks.clear();
        this.setChanged();
    }

    @Override
    public void load(@NotNull CompoundTag tagCompound)
    {
        super.load(tagCompound);
        final ListTag items = tagCompound.getList("Items", NBTHelper.COMPOUND.getId());
        for (int i = 0; i < items.size(); ++i)
        {
            final CompoundTag item = items.getCompound(i);
            final int slot = item.getByte("Slot") & 0xFF;
            ItemStack iStack = ItemStack.of(item);
            if (slot >= 0 && slot < getContainerSize())
            {
                setItem(slot, iStack);
            }
        }
        final ListTag spares = tagCompound.getList("Spares", NBTHelper.COMPOUND.getId());
        spareModules.clear();
        for (int j = 0; j < spares.size(); ++j)
        {
            final CompoundTag item2 = spares.getCompound(j);
            ItemStack iStack = ItemStack.of(item2);
            spareModules.add(iStack);
        }
        final CompoundTag outputTag = (CompoundTag) tagCompound.get("Output");
        if (outputTag != null)
        {
            outputItem = ItemStack.of(outputTag);
        }
        if (tagCompound.contains("Fuel"))
        {
            setFuelLevel(tagCompound.getShort("Fuel"));
        }
        else
        {
            setFuelLevel(tagCompound.getInt("IntFuel"));
        }
        maxAssemblingTime = tagCompound.getInt("maxTime");
        setAssemblingTime(tagCompound.getInt("currentTime"));
        isAssembling = tagCompound.getBoolean("isAssembling");
    }


    @Override
    public void saveAdditional(final @NotNull CompoundTag tagCompound)
    {
        super.saveAdditional(tagCompound);
        final ListTag items = new ListTag();
        for (int i = 0; i < getContainerSize(); ++i)
        {
            ItemStack iStack = getItem(i);
            if (!iStack.isEmpty())
            {
                final CompoundTag item = new CompoundTag();
                item.putByte("Slot", (byte) i);
                iStack.save(item);
                items.add(item);
            }
        }
        tagCompound.put("Items", items);
        final ListTag spares = new ListTag();
        for (int j = 0; j < spareModules.size(); ++j)
        {
            ItemStack iStack2 = spareModules.get(j);
            if (!iStack2.isEmpty())
            {
                final CompoundTag item2 = new CompoundTag();
                iStack2.save(item2);
                spares.add(item2);
            }
        }
        tagCompound.put("Spares", spares);
        if (!outputItem.isEmpty())
        {
            final CompoundTag outputTag = new CompoundTag();
            outputItem.save(outputTag);
            tagCompound.put("Output", outputTag);
        }
        tagCompound.putInt("IntFuel", getFuelLevel());
        tagCompound.putInt("maxTime", maxAssemblingTime);
        tagCompound.putInt("currentTime", getAssemblingTime());
        tagCompound.putBoolean("isAssembling", isAssembling);
    }

    @Override
    public void handleUpdateTag(CompoundTag tag)
    {
        load(tag);
    }

    @Override
    public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt)
    {
        load(pkt.getTag());
    }

    public CompoundTag getOutputInfo()
    {
        if (outputItem.isEmpty())
        {
            return null;
        }
        if (!outputItem.hasTag())
        {
            return null;
        }
        return outputItem.getTag();
    }

    public void increaseFuel(final int val)
    {
        fuelLevel += val;
        if (fuelLevel > getMaxFuelLevel())
        {
            fuelLevel = getMaxFuelLevel();
        }
    }
}
