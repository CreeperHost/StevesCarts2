package vswe.stevescarts.entities;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Container;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.block.state.properties.RailShape;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import org.jetbrains.annotations.NotNull;
import vswe.stevescarts.StevesCarts;
import vswe.stevescarts.api.StevesCartsAPI;
import vswe.stevescarts.api.client.ModelCartbase;
import vswe.stevescarts.api.modules.ModuleBase;
import vswe.stevescarts.api.modules.data.ModuleData;
import vswe.stevescarts.api.modules.interfaces.IActivatorModule;
import vswe.stevescarts.api.modules.template.ModuleEngine;
import vswe.stevescarts.api.modules.template.ModuleWorker;
import vswe.stevescarts.blocks.tileentities.TileEntityCartAssembler;
import vswe.stevescarts.containers.ContainerMinecart;
import vswe.stevescarts.helpers.ActivatorOption;
import vswe.stevescarts.helpers.ForceChunkHelper;
import vswe.stevescarts.helpers.GuiAllocationHelper;
import vswe.stevescarts.helpers.ModuleCountPair;
import vswe.stevescarts.helpers.storages.TransferHandler;
import vswe.stevescarts.modules.addons.ModuleCreativeSupplies;
import vswe.stevescarts.modules.storages.tanks.ModuleTank;
import vswe.stevescarts.modules.workers.CompWorkModule;

import javax.annotation.Nonnull;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

/**
 * The modular cart entity class was way too crowded,
 * So a lot of the modular stuff has been moved here to clean things up a bit.
 * <p>
 * Created by brandon3055 on 09/12/2024
 */
public interface IModularCart extends Container, IFluidHandler {

    ModularMinecart getCart();

    @Deprecated //Not sure if we need to keep these,
    default int x() {
        return (int) Math.floor(getCart().position().x);
    }

    @Deprecated //Not sure if we need to keep these,
    default int y() {
        return (int) Math.floor(getCart().position().y);
    }

    @Deprecated //Not sure if we need to keep these,
    default int z() {
        return (int) Math.floor(getCart().position().z);
    }

    //=== Module Methods ===//

    ArrayList<ModuleBase> modules();

    ArrayList<ModuleWorker> workers();

    ArrayList<ModuleEngine> engines();

    ArrayList<ModuleTank> moduleTanks();

    ArrayList<ModuleCountPair> moduleCounts();

    boolean isPlaceholder();

    void setPlaceholder(TileEntityCartAssembler assembler);

    default boolean dropOnDeath() {
        return !isPlaceholder() && modules().stream().allMatch(ModuleBase::dropOnDeath);
    }

    //TODO, This is not used anywhere, which means its probably bart of a broken feature...
    default float[] getColor() {
        for (ModuleBase module : modules()) {
            float[] color = module.getColor();
            if (color[0] != 1.0f || color[1] != 1.0f || color[2] != 1.0f) {
                return color;
            }
        }
        return new float[]{1.0f, 1.0f, 1.0f};
    }

    default int getYTarget() {
        for (ModuleBase module : modules()) {
            int yTarget = module.getYTarget();
            if (yTarget != Integer.MIN_VALUE) {
                return yTarget;
            }
        }
        return y();
    }

    default ModuleBase getInterfaceThief() {
        return modules().stream()
                .filter(ModuleBase::doStealInterface)
                .findFirst()
                .orElse(null);
    }

    default double getPushFactor() {
        for (ModuleBase module : modules()) {
            double factor = module.getPushFactor();
            if (factor >= 0.0) {
                return factor;
            }
        }
        return 0.05;
    }

    default boolean hasFuel() {
        if (getCart().isDisabled()) {
            return false;
        }
        for (ModuleBase module : modules()) {
            if (module.stopEngines()) {
                return false;
            }
        }
        return hasFuelForModule();
    }

    default boolean hasFuelForModule() {
        if (isPlaceholder()) {
            return true;
        }
        int consumption = getConsumption(true);
        for (ModuleBase module : modules()) {
            if (module.hasFuel(consumption)) {
                return true;
            }
        }
        return false;
    }

    default int getConsumption() {
        return getConsumption(!getCart().isDisabled() && getCart().isEngineBurning());
    }

    default int getConsumption(boolean isMoving) {
        int consumption = isMoving ? 1 : 0;
        if (!isPlaceholder()) {
            for (ModuleBase module : modules()) {
                consumption += module.getConsumption(isMoving);
            }
        }
        return consumption;
    }

    private ModuleEngine getCurrentEngine() {
        for (ModuleBase module : modules()) {
            if (module.stopEngines()) {
                return null;
            }
        }
        int consumption = getConsumption(true);
        ArrayList<ModuleEngine> priority = new ArrayList<>();
        int mostImportant = -1;
        for (ModuleEngine engine : engines()) {
            if (engine.hasFuel(consumption) && (mostImportant == -1 || mostImportant >= engine.getPriority())) {
                if (engine.getPriority() < mostImportant) {
                    priority.clear();
                }
                mostImportant = engine.getPriority();
                priority.add(engine);
            }
        }
        if (!priority.isEmpty()) {
            if (getCart().motorRotation >= priority.size()) {
                getCart().motorRotation = 0;
            }
            getCart().motorRotation = (getCart().motorRotation + 1) % priority.size();
            return priority.get(getCart().motorRotation);
        }
        return null;
    }

    default ArrayList<Component> getLabel() {
        ArrayList<Component> label = new ArrayList<>();
        for (ModuleBase module : modules()) {
            module.addToLabel(label);
        }
        return label;
    }

    default boolean hasCreativeSupplies() {
        return getCart().hasCreativeSupplies;
    }

    default TileEntityCartAssembler getPlaceholderAsssembler() {
        return getCart().placeholderAsssembler;
    }

    default BlockPos getDisabledPos() {
        return getCart().disabledPos;
    }

    //=== Cart Functions / Logic ===//

    default void onCartUpdate() {
        updateFuel();
        for (ModuleBase module : modules()) {
            module.update();
        }
        for (ModuleBase module : modules()) {
            module.postUpdate();
        }
        work();
        if (isPlaceholder() && getCart().keepAlive++ > 20) {
            getCart().remove(Entity.RemovalReason.KILLED);
            getCart().placeholderAsssembler.resetPlaceholder();
        }
    }

    default void updateFuel() {
        if (!getCart().level().isClientSide()) {
            getCart().setEngineBurning(hasFuel() && !getCart().isDisabled());
        }

        int consumption = getConsumption();
        if (consumption > 0) {
            ModuleEngine engine = getCurrentEngine();
            if (engine != null) {
                engine.consumeFuel(consumption);
                if (!isPlaceholder() && getCart().level().isClientSide && getCart().isEngineBurning()) {
                    engine.smoke();
                }
            }
        }
    }

    default void handleActivator(ActivatorOption option, boolean isOrange) {
        for (ModuleBase module : modules()) {
            if (module instanceof IActivatorModule activator && option.getModule().isAssignableFrom(module.getClass())) {
                if (option.shouldActivate(isOrange)) {
                    activator.doActivate(option.getId());
                } else if (option.shouldDeactivate(isOrange)) {
                    activator.doDeActivate(option.getId());
                } else {
                    if (!option.shouldToggle()) {
                        continue;
                    }
                    if (activator.isActive(option.getId())) {
                        activator.doDeActivate(option.getId());
                    } else {
                        activator.doActivate(option.getId());
                    }
                }
            }
        }
    }

    //Used to control the direction the cart goes when it hits a junction track
    default RailShape getRailDirection(BlockPos pos) {
        ModuleBase.RAILDIRECTION dir = ModuleBase.RAILDIRECTION.DEFAULT;
        for (ModuleBase module : modules()) {
            dir = module.getSpecialRailDirection(pos);
            if (dir != ModuleBase.RAILDIRECTION.DEFAULT) {
                break;
            }
        }
        if (dir == ModuleBase.RAILDIRECTION.DEFAULT) {
            return null;
        }
        int Yaw = (int) (getCart().getYRot() % 180.0f);
        if (Yaw < 0) {
            Yaw += 180;
        }
        boolean flag = Yaw >= 45 && Yaw <= 135;
        Vec3 motion = getCart().getDeltaMovement();
        if (getCart().fixedRailDirection == null) {
            switch (dir) {
                case FORWARD -> {
                    if (flag) {
                        getCart().fixedRailDirection = RailShape.NORTH_SOUTH;
                        break;
                    }
                    getCart().fixedRailDirection = RailShape.EAST_WEST;
                }
                case LEFT -> {
                    if (flag) {
                        if (motion.z > 0.0) {
                            getCart().fixedRailDirection = RailShape.NORTH_EAST;
                            break;
                        }
                        if (motion.z <= 0.0) {
                            getCart().fixedRailDirection = RailShape.SOUTH_WEST;
                        }
                    } else {
                        if (motion.x > 0.0) {
                            getCart().fixedRailDirection = RailShape.NORTH_WEST;
                            break;
                        }
                        if (motion.x < 0.0) {
                            getCart().fixedRailDirection = RailShape.SOUTH_EAST;
                        }
                    }
                }
                case RIGHT -> {
                    if (flag) {
                        if (motion.z > 0.0) {
                            getCart().fixedRailDirection = RailShape.NORTH_WEST;
                            break;
                        }
                        if (motion.z <= 0.0) {
                            getCart().fixedRailDirection = RailShape.SOUTH_EAST;
                        }
                    } else {
                        if (motion.x > 0.0) {
                            getCart().fixedRailDirection = RailShape.SOUTH_WEST;
                            break;
                        }
                        if (motion.x < 0.0) {
                            getCart().fixedRailDirection = RailShape.NORTH_EAST;
                        }
                    }
                }
                case NORTH -> {
                    if (flag) {
                        if (motion.z > 0.0) {
                            getCart().fixedRailDirection = RailShape.NORTH_SOUTH;
                        }
                    } else {
                        if (motion.x > 0.0) {
                            getCart().fixedRailDirection = RailShape.SOUTH_WEST;
                            break;
                        }
                        if (motion.x < 0.0) {
                            getCart().fixedRailDirection = RailShape.SOUTH_EAST;
                        }
                    }
                }
                default -> {
                }
            }
            if (getCart().fixedRailDirection == null) {
                return null;
            }
            getCart().fixedRailPos = new BlockPos(pos);
        }
        return getCart().fixedRailDirection;
    }

    //=== Work Functions ===//

    private void work() {
        if (isPlaceholder()) {
            return;
        }
        if (!getCart().level().isClientSide && hasFuel()) {
            if (getCart().workingTime <= 0) {
                ModuleWorker oldComponent = getCart().workingComponent;
                if (getCart().workingComponent != null) {
                    boolean result = getCart().workingComponent.work();
                    if (getCart().workingComponent != null && oldComponent == getCart().workingComponent && getCart().workingTime <= 0 && !getCart().workingComponent.preventAutoShutdown()) {
                        getCart().workingComponent.stopWorking();
                    }
                    if (result) {
                        work();
                        return;
                    }
                }

                for (ModuleWorker module : workers()) {
                    if (module.work()) {
                        return;
                    }
                }

            } else {
                --getCart().workingTime;
            }
        }
    }

    void setWorker(ModuleWorker worker);

    ModuleWorker getWorker();

    void setWorkingTime(int val);

    //=== Chunk Loading ===//

    default void loadChunks() {
        updateTicket(true);
    }

    default void initChunkLoading() {
        updateTicket(true);
    }

    default void dropChunkLoading() {
        updateTicket(false);
    }

    default void updateTicket(boolean loadingEnabled) {
        if (!(getCart().level() instanceof ServerLevel level)) {
            return;
        }
        List<ChunkPos> removed = new ArrayList<>();

        //If disabled then just un-force all chunks.
        if (!loadingEnabled) {
            getCart().forcedChunks.forEach(pos -> {
                if (ForceChunkHelper.CONTROLLER.forceChunk(level, getCart(), pos.x, pos.z, false, true)) {
                    removed.add(pos);
                }
            });
            getCart().forcedChunks.removeAll(removed);
            return;
        }

        //Copy the loaded list then remove any already loaded chunks from the copy and load any new chunks.
        List<ChunkPos> forcedCopy = new ArrayList<>(getCart().forcedChunks);
        Stream<ChunkPos> loadArea = ChunkPos.rangeClosed(getCart().chunkPosition(), 1);
        loadArea.forEach(pos -> {
            if (forcedCopy.contains(pos)) {
                forcedCopy.remove(pos);
            } else if (ForceChunkHelper.CONTROLLER.forceChunk(level, getCart(), pos.x, pos.z, true, true)) {
                getCart().forcedChunks.add(pos);
            }
        });

        //The copy should now only contain chunks that are out of range, so unload them.
        forcedCopy.forEach(pos -> {
            if (ForceChunkHelper.CONTROLLER.forceChunk(level, getCart(), pos.x, pos.z, false, true)) {
                removed.add(pos);
            }
        });
        getCart().forcedChunks.removeAll(removed);
    }

    //=== Module Loading ===//

    default void initModules() {
        moduleCounts().clear();
        for (ModuleBase module : modules()) {
            ModuleData data = StevesCartsAPI.MODULE_REGISTRY.get(module.getModuleId());
            boolean found = false;
            for (ModuleCountPair count : moduleCounts()) {
                if (count.isContainingData(data)) {
                    count.increase();
                    found = true;
                    break;
                }
            }
            if (!found) {
                moduleCounts().add(new ModuleCountPair(data));
            }
        }
        for (ModuleBase module : modules()) {
            module.preInit();
        }
        workers().clear();
        engines().clear();
        moduleTanks().clear();
        int guidata = 0;
        int packets = 0;
        if (getCart().level().isClientSide) {
            generateModels();
        }
        for (ModuleBase module2 : modules()) {
            switch (module2) {
                case ModuleWorker moduleWorker -> workers().add(moduleWorker);
                case ModuleEngine moduleEngine -> engines().add(moduleEngine);
                case ModuleTank moduleTank -> moduleTanks().add(moduleTank);
                case null, default -> {
                    if (!(module2 instanceof ModuleCreativeSupplies)) {
                        continue;
                    }
                    getCart().hasCreativeSupplies = true;
                }
            }
        }
        CompWorkModule sorter = new CompWorkModule();
        workers().sort(sorter);
        if (!isPlaceholder()) {
            ArrayList<GuiAllocationHelper> lines = new ArrayList<>();
            int slots = 0;
            for (ModuleBase module3 : modules()) {
                if (module3.hasGui()) {
                    boolean foundLine = false;
                    for (GuiAllocationHelper line : lines) {
                        if (line.width + module3.guiWidth() <= ModularMinecart.MODULAR_SPACE_WIDTH) {
                            module3.setX(line.width);
                            line.width += module3.guiWidth();
                            line.maxHeight = Math.max(line.maxHeight, module3.guiHeight());
                            line.modules.add(module3);
                            foundLine = true;
                            break;
                        }
                    }
                    if (!foundLine) {
                        GuiAllocationHelper line2 = new GuiAllocationHelper();
                        module3.setX(0);
                        line2.width = module3.guiWidth();
                        line2.maxHeight = module3.guiHeight();
                        line2.modules.add(module3);
                        lines.add(line2);
                    }
                    module3.setGuiDataStart(guidata);
                    guidata += module3.numberOfGuiData();
                    if (module3.hasSlots()) {
                        slots = module3.generateSlots(slots);
                    }
                }
                module3.setPacketStart(packets);
                packets += module3.totalNumberOfPackets();
            }
            int currentY = 0;
            for (GuiAllocationHelper line3 : lines) {
                for (ModuleBase module4 : line3.modules) {
                    module4.setY(currentY);
                }
                currentY += line3.maxHeight;
            }
            if (currentY > ModularMinecart.MODULAR_SPACE_HEIGHT) {
                getCart().canScrollModules = true;
            }
            getCart().modularSpaceHeight = currentY;
        }
        for (ModuleBase module5 : modules()) {
            module5.init();
        }
    }

    default void loadPlaceHolderModules(List<ResourceLocation> data) {
        modules().clear();
        for (ResourceLocation moduleResourceLocation : data) {
            ModuleData moduleData = StevesCartsAPI.MODULE_REGISTRY.get(moduleResourceLocation);
            doLoadModules(moduleData, null);
        }

        initModules();
        getCart().moduleLoadingData = data;
    }

    default void doLoadModules(ModuleData moduleData, @org.jetbrains.annotations.Nullable CompoundTag data) {
        if (moduleData == null) return;
        try {
            Class<? extends ModuleBase> moduleClass = moduleData.getModuleClass();
            Constructor<? extends ModuleBase> moduleConstructor = moduleClass.getConstructor(ModularMinecart.class);
            ModuleBase module = moduleConstructor.newInstance(this);
            module.setModuleId(moduleData.getID());
            modules().add(module);
            if (data != null && data.contains("data")) {
                module.readExtraData(data.getCompound("data"));
            }
        } catch (Exception e) {
            StevesCarts.LOGGER.error("Failed to load module with ID " + moduleData.getID() + "! More info below.");
            e.printStackTrace();
        }
    }

    default void loadModules(CompoundTag info) {
        List<CompoundTag> modules = new ArrayList<>();
        if (info == null) return;

        List<ResourceLocation> names = new ArrayList<>();
        ListTag listTag = (ListTag) info.get("modules");
        for (int i = 0; i < listTag.size(); i++) {
            Tag tag = listTag.get(i);
            modules.add((CompoundTag) tag);
            names.add(ResourceLocation.parse(((CompoundTag) tag).getString(String.valueOf(i))));
        }

        if (!names.isEmpty()) {
            getCart().moduleLoadingData = names;
        }
        loadModules(modules);
    }

    default void updateSimulationModules(List<ResourceLocation> data) {
        if (!isPlaceholder()) {
            StevesCarts.LOGGER.error("You're stupid! This is not a placeholder cart.");
        } else {
            loadPlaceHolderModules(data);
        }
    }

    default void loadModules(List<CompoundTag> data) {
        modules().clear();
        if (data != null) {
            for (int i = 0; i < data.size(); i++) {
                CompoundTag tag = data.get(i);
                ResourceLocation name = ResourceLocation.parse(tag.getString(String.valueOf(i)));
                doLoadModules(StevesCartsAPI.MODULE_REGISTRY.get(name), tag);
            }
        }
        initModules();
    }

    default void loadModulesFromNames(List<ResourceLocation> data) {
        modules().clear();
        if (data != null) {
            for (ResourceLocation name : data) {
                doLoadModules(StevesCartsAPI.MODULE_REGISTRY.get(name), null);
            }
        }
        initModules();
    }

    //=== Container ===//

    @Override
    default int getContainerSize() {
        int slotCount = 0;
        for (ModuleBase module : modules()) {
            slotCount += module.getInventorySize();
        }
        return slotCount;
    }

    @Override
    default boolean isEmpty() {
        return false;
    }

    @Override
    default @NotNull ItemStack getItem(int i) {
        for (ModuleBase module : modules()) {
            if (i < module.getInventorySize()) {
                return module.getStack(i);
            }
            i -= module.getInventorySize();
        }
        return ItemStack.EMPTY;
    }

    @Override
    default @NotNull ItemStack removeItem(int i, int p_70298_2_) {
        if (!getItem(i).isEmpty()) {
            @Nonnull ItemStack var2 = getItem(i);
            setItem(i, ItemStack.EMPTY);
            return var2;
        }
        return ItemStack.EMPTY;
    }

    @Override
    default @NotNull ItemStack removeItemNoUpdate(int i) {
        return ItemStack.EMPTY;
    }

    @Override
    default void setItem(int i, @NotNull ItemStack item) {
        for (ModuleBase module : modules()) {
            if (i < module.getInventorySize()) {
                module.setStack(i, item);
                break;
            }
            i -= module.getInventorySize();
        }
    }

    @Override
    default void setChanged() {
        for (ModuleBase module : modules()) {
            module.onInventoryChanged();
        }
    }

    @Override
    default boolean stillValid(Player player) {
        return true;
    }

    default void addItemToChest(@Nonnull ItemStack iStack) {
        TransferHandler.TransferItem(iStack, this, getCon(null), Slot.class, null, -1);
    }

    default void addItemToChest(@Nonnull ItemStack iStack, int start, int end) {
        TransferHandler.TransferItem(iStack, this, start, end, getCon(null), Slot.class, null, -1);
    }

    default void addItemToChest(@Nonnull ItemStack iStack, Class validSlot, Class invalidSlot) {
        TransferHandler.TransferItem(iStack, this, getCon(null), validSlot, invalidSlot, -1);
    }

    default AbstractContainerMenu getCon(Inventory playerInventory) {
        return new ContainerMinecart(0, playerInventory, getCart());
    }

    //=== Fluid ===//

    @Override
    default void clearContent() {
    }

    @Override
    default int getTanks() {
        return moduleTanks().size();
    }

    @Nonnull
    @Override
    default FluidStack getFluidInTank(int tank) {
        return moduleTanks().get(tank).getFluid();
    }

    @Override
    default int getTankCapacity(int tank) {
        return moduleTanks().get(tank).getCapacity();
    }

    @Override
    default boolean isFluidValid(int tank, @Nonnull FluidStack stack) {
        return moduleTanks().get(tank).isFluidValid(stack);
    }

    @Override
    default int fill(FluidStack resource, FluidAction action) {
        int amount = 0;
        if (resource != null && resource.getAmount() > 0) {
            FluidStack fluid = resource.copy();
            for (int i = 0; i < moduleTanks().size(); ++i) {
                int tempAmount = moduleTanks().get(i).fill(fluid, action);
                amount += tempAmount;
                fluid.shrink(tempAmount);
                if (fluid.getAmount() <= 0) {
                    break;
                }
            }
        }
        return amount;
    }

    default FluidStack drain(FluidStack resource, int maxDrain, FluidAction doDrain) {
        FluidStack ret = resource;
        if (ret != null) {
            ret = ret.copy();
            ret.setAmount(0);
        }
        for (int i = 0; i < moduleTanks().size(); ++i) {
            FluidStack temp = null;
            temp = moduleTanks().get(i).drain(maxDrain, doDrain);
            if (temp != null && (ret == null || FluidStack.isSameFluidSameComponents(ret, temp))) {
                if (ret == null) {
                    ret = temp;
                } else {
                    ret.grow(temp.getAmount());
                }
                maxDrain -= temp.getAmount();
                if (maxDrain <= 0) {
                    break;
                }
            }
        }
        if (ret != null && ret.getAmount() == 0) {
            return null;
        }
        return ret;
    }

    default int drain(Fluid type, int maxDrain, FluidAction doDrain) {
        int amount = 0;
        if (type != null && maxDrain > 0) {
            for (ModuleTank tank : moduleTanks()) {
                FluidStack drained = tank.drain(maxDrain, doDrain);
                if (!drained.isEmpty() && type.isSame(drained.getFluid())) {
                    amount += drained.getAmount();
                    maxDrain -= drained.getAmount();
                    if (doDrain == FluidAction.EXECUTE) {
                        tank.drain(drained.getAmount(), doDrain);
                    }
                    if (maxDrain <= 0) {
                        break;
                    }
                }
            }
        }
        return amount;
    }

    @Nonnull
    @Override
    default FluidStack drain(FluidStack resource, FluidAction action) {
        return drain(resource, (!resource.isEmpty()) ? 0 : resource.getAmount(), action);
    }

    @Nonnull
    @Override
    default FluidStack drain(int maxDrain, FluidAction action) {
        return drain(maxDrain, action);
    }

    //=== Client / GUI Stuff ===//

    @OnlyIn (Dist.CLIENT)
    private void generateModels() {
        ArrayList<String> invalid = new ArrayList<>();
        for (ModuleBase module : modules()) {
            ModuleData data = module.getData();
            if (data.haveRemovedModels()) {
                invalid.addAll(data.getRemovedModels());
            }
        }
        for (int i = modules().size() - 1; i >= 0; --i) {
            ModuleBase module = modules().get(i);
            ModuleData data = module.getData();
            if (data != null && data.haveModels(isPlaceholder())) {
                ArrayList<ModelCartbase> models = new ArrayList<>();
                for (String str : data.getModels(isPlaceholder()).keySet()) {
                    if (!invalid.contains(str)) {
                        models.add(data.getModels(isPlaceholder()).get(str));
                        invalid.add(str);
                    }
                }
                if (!models.isEmpty()) {
                    module.setModels(models);
                }
            }
        }
    }

    @OnlyIn (Dist.CLIENT) //TODO, Why does this exist? What ism mit supposed to do?
    default void updateSounds() {
    }

    default void setScrollY(int val) {
        if (getCart().canScrollModules) {
            getCart().scrollY = val;
        }
    }

    default int getScrollY() {
        if (getInterfaceThief() != null) {
            return 0;
        }
        return getCart().scrollY;
    }

    default int getRealScrollY() {
        return (int) ((getCart().modularSpaceHeight - ModularMinecart.MODULAR_SPACE_HEIGHT) / 198.0f * getScrollY());
    }

    @OnlyIn (Dist.CLIENT)
    default void renderOverlay(GuiGraphics render, float partialTicks) {
        for (ModuleBase module : modules()) {
            module.renderOverlay(render, partialTicks);
        }
    }

}
