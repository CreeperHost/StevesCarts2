package vswe.stevescarts.api.modules.data;

import net.minecraft.ChatFormatting;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import vswe.stevescarts.api.IModuleItem;
import vswe.stevescarts.api.StevesCartsAPI;
import vswe.stevescarts.api.client.ModelCartbase;
import vswe.stevescarts.api.modules.ModuleBase;
import vswe.stevescarts.api.modules.ModuleFactory;
import vswe.stevescarts.api.modules.ModuleType;
import vswe.stevescarts.entities.ModularMinecart;
import vswe.stevescarts.init.ModItems;
import vswe.stevescarts.init.ModItemData;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Locale;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class ModuleData {
    private static final int MAX_MESSAGE_ROW_LENGTH = 30;
    private final Identifier id;
    private final Class<? extends ModuleBase> moduleClass;
    private final ModuleFactory<? extends ModuleBase> moduleFactory;
    private final String name;
    private final int modularCost;
    private final ModuleType moduleType;
    private ArrayList<SIDE> renderingSides;
    private boolean allowDuplicate;
    private ArrayList<ModuleData> nemesis;
    private ArrayList<ModuleDataGroup> requirement;
    private ModuleData parent;
    private boolean isLocked;
    private boolean defaultLock;
    private ArrayList<String> message;
    private HashMap<String, ModelCartbase> models;
    private HashMap<String, ModelCartbase> modelsPlaceholder;
    private ArrayList<String> removedModels;
    private float modelMult;
    private boolean useExtraData;
    private byte extraDataDefaultValue;
    private String translationKey;
    private Supplier<? extends Item> itemSupplier;

    public ModuleData(final Identifier id, final String name, final Class<? extends ModuleBase> moduleClass, ModuleType moduleType, final int modularCost) {
        this(id, name, moduleClass, cart -> {
            try {
                return moduleClass.getConstructor(ModularMinecart.class).newInstance(cart);
            } catch (ReflectiveOperationException exception) {
                throw new IllegalStateException("Failed to create module " + id
                        + ". Supply a ModuleFactory or a public constructor accepting ModularMinecart.", exception);
            }
        }, moduleType, modularCost);
    }

    public ModuleData(final Identifier id, final String name, final Class<? extends ModuleBase> moduleClass,
                      ModuleFactory<? extends ModuleBase> moduleFactory, ModuleType moduleType, final int modularCost) {
        this.nemesis = new ArrayList<>();
        this.requirement = new ArrayList<>();
        this.parent = null;
        this.modelMult = 0.75f;
        this.id = Objects.requireNonNull(id, "id");
        this.moduleClass = Objects.requireNonNull(moduleClass, "moduleClass");
        this.moduleFactory = Objects.requireNonNull(moduleFactory, "moduleFactory");
        this.name = Objects.requireNonNull(name, "name");
        this.modularCost = modularCost;
        this.moduleType = Objects.requireNonNull(moduleType, "moduleType");
        this.translationKey = "item." + id.getNamespace() + "." + id.getPath();
    }

    public static void addNemesis(final ModuleData first, final ModuleData second) {
        first.conflictsWith(second);
    }

    public static NonNullList<ItemStack> getModularItems(@Nonnull ItemStack cart) {
        NonNullList<ItemStack> modules = NonNullList.create();
        if (!cart.isEmpty() && cart.getItem() == ModItems.CARTS.get() && ModItemData.hasTag(cart)) {
            CompoundTag info = ModItemData.getTagCopy(cart);
            if (info.contains("modules")) {
                int i = 0;
                for (Tag tag : info.getListOrEmpty("modules")) {
                    CompoundTag moduleTag = (CompoundTag) tag;
                    //If this ever explodes, then someone please slap whoever decided to use the arbitrary index of the module used as the key for the id field. WTF...
                    String regName = moduleTag.getStringOr(String.valueOf(i), "");
                    ModuleData data = StevesCartsAPI.getModule(Identifier.parse(regName));
                    if (data == null) {
                        i++;
                        continue;
                    }
                    ItemStack module = data.getItemStack();
                    if (module.isEmpty()) {
                        i++;
                        continue;
                    }
                    if (moduleTag.contains("data")) {
                        ModItemData.modifyTag(module, t -> t.put("data", moduleTag.getCompoundOrEmpty("data")));
                    }
                    modules.add(module);
                    i++;
                }
            }
        }
        return modules;
    }

    public static ItemStack createModularCart(ModularMinecart parentcart) {
        ItemStack cart = new ItemStack(ModItems.CARTS.get(), 1);
        ListTag modulesTag = new ListTag();
        for (int i = 0; i < parentcart.modules().size(); i++) {
            CompoundTag moduleTag = new CompoundTag();
            ModuleBase module = parentcart.modules().get(i);
            moduleTag.putString(String.valueOf(i), module.getModuleId().toString());
            if (module.hasExtraData()) {
                moduleTag.put("data", module.writeExtraData());
            }
            modulesTag.add(i, moduleTag);
        }
        CompoundTag tag = ModItemData.getTagCopy(cart);
        tag.put("modules", modulesTag);
        ModItemData.setTag(cart, tag);
        return cart;
    }

    public static ItemStack createModularCartFromItems(final NonNullList<ItemStack> modules) {
        ItemStack cart = new ItemStack(ModItems.CARTS.get(), 1);
        ListTag modulesTag = new ListTag();
        for (int i = 0; i < modules.size(); i++) {
            CompoundTag moduleTag = new CompoundTag();
            ItemStack moduleStack = modules.get(i);
            IModuleItem cartModule = (IModuleItem) moduleStack.getItem();
            moduleTag.putString(String.valueOf(i), cartModule.getModuleData().getID().toString());

            CompoundTag tag = ModItemData.getTagCopy(moduleStack);
            if (tag.contains("data")) {
                moduleTag.put("data", tag.getCompoundOrEmpty("data"));
            }

            modulesTag.add(i, moduleTag);
        }
        CompoundTag tag = ModItemData.getTagCopy(cart);
        tag.put("modules", modulesTag);
        ModItemData.setTag(cart, tag);
        return cart;
    }

    public static boolean isItemOfModularType(@Nonnull ItemStack itemstack, final Class<? extends ModuleBase> validClass) {
        if (itemstack.getItem() instanceof IModuleItem iModuleItem) {
            final ModuleData moduleData = iModuleItem.getModuleData();
            return moduleData != null && validClass.isAssignableFrom(moduleData.moduleClass);
        }
        return false;
    }

    public static boolean isValidModuleItem(final ModuleType moduleType, @Nonnull ItemStack itemstack) {
        if (itemstack.getItem() instanceof IModuleItem itemCartModule) {
            final ModuleData module = itemCartModule.getModuleData();
            return isValidModuleItem(moduleType, module);
        }
        return false;
    }

    //TODO rewrite all of this
    public static boolean isValidModuleItem(final ModuleType moduleType, final ModuleData module) {
        if (module != null) {
            return module.getModuleType() == moduleType;
        }
        return false;
    }

    public static boolean isValidModuleCombo(final ModuleDataHull hull, final ArrayList<ModuleData> modules) {
        if (hull == null || modules == null) {
            return false;
        }

        EnumMap<ModuleType, Integer> counts = new EnumMap<>(ModuleType.class);
        for (ModuleData module : modules) {
            if (module == null) {
                return false;
            }

            ModuleType type = module.getModuleType();
            int maximum = switch (type) {
                case HULL -> 1;
                case ENGINE -> hull.getEngineMax();
                case TOOL -> 1;
                case ATTACHMENT -> 6;
                case STORAGE -> 4;
                case ADDON -> hull.getAddonMax();
                case NONE -> 0;
            };
            int count = counts.merge(type, 1, Integer::sum);
            if (count > maximum) {
                return false;
            }
        }
        return true;
    }

    public static String checkForErrors(final ModuleDataHull hull, final ArrayList<ModuleData> modules) {
        if (getTotalCost(modules) > hull.getCapacity()) {
            return Component.translatable("info.stevescarts.capacityOverloadError").getString();
        }
        if (!isValidModuleCombo(hull, modules)) {
            return Component.translatable("info.stevescarts.impossibleCombinationError").getString();
        }
        for (int i = 0; i < modules.size(); ++i) {
            final ModuleData mod1 = modules.get(i);
            if (mod1.getCost() > hull.getComplexityMax()) {
                return Component.translatable("info.stevescarts.complexityOverloadError", mod1.getName()).getString();
            }
            if (mod1.getParent() != null && !modules.contains(mod1.getParent())) {
                return Component.translatable("info.stevescarts.missingParentError", mod1.getName(), mod1.getParent().getName()).getString();
            }
            if (mod1.getNemesis() != null) {
                for (final ModuleData nemesis : mod1.getNemesis()) {
                    if (modules.contains(nemesis)) {
                        return Component.translatable("info.stevescarts.presentNemesisError", mod1.getName(), nemesis.getName()).getString();
                    }
                }
            }
            if (mod1.getRequirement() != null) {
                for (final ModuleDataGroup group : mod1.getRequirement()) {
                    int count = 0;
                    for (final ModuleData mod2 : group.getModules()) {
                        for (final ModuleData mod3 : modules) {
                            if (mod2.equals(mod3)) {
                                ++count;
                            }
                        }
                    }
                    if (count < group.getCount()) {
                        return Component.translatable("info.stevescarts.missingParentError", mod1.getName(), group.getCountName() + " " + group.getName()).getString();
                    }
                }
            }
            for (int j = i + 1; j < modules.size(); ++j) {
                final ModuleData mod4 = modules.get(j);
                if (mod1 == mod4) {
                    if (!mod1.getAllowDuplicate()) {
                        return Component.translatable("info.stevescarts.presentDuplicateError", mod1.getName()).getString();
                    }
                } else if (mod1.getRenderingSides() != null && mod4.getRenderingSides() != null) {
                    SIDE clash = SIDE.NONE;
                    for (final SIDE side1 : mod1.getRenderingSides()) {
                        for (final SIDE side2 : mod4.getRenderingSides()) {
                            if (side1 == side2) {
                                clash = side1;
                                break;
                            }
                        }
                        if (clash != SIDE.NONE) {
                            break;
                        }
                    }
                    if (clash != SIDE.NONE) {
                        return Component.translatable("info.stevescarts.sideClashError", mod1.getName(), mod4.getName(), clash.toString()).getString();
                    }
                }
            }
        }
        return null;
    }

    public static int getTotalCost(final ArrayList<ModuleData> modules) {
        int currentCost = 0;
        for (final ModuleData module : modules) {
            currentCost += module.getCost();
        }
        return currentCost;
    }

    public Class<? extends ModuleBase> getModuleClass() {
        return moduleClass;
    }

    public ModuleBase createModule(ModularMinecart cart) {
        ModuleBase module = moduleFactory.create(cart);
        if (module == null) {
            throw new IllegalStateException("Module factory for " + id + " returned null");
        }
        if (!moduleClass.isInstance(module)) {
            throw new IllegalStateException("Module factory for " + id + " returned "
                    + module.getClass().getName() + " instead of " + moduleClass.getName());
        }
        return module;
    }

    public ModuleType getModuleType() {
        return moduleType;
    }

    @Deprecated(forRemoval = true)
    public boolean getIsValid() {
        return true;
    }

    @Deprecated(forRemoval = true)
    public boolean getIsLocked() {
        return isLocked;
    }

    @SuppressWarnings("unused")
    public ModuleData lock() {
        isLocked = true;
        return this;
    }

    @SuppressWarnings("unused")
    public boolean getEnabledByDefault() {
        return !defaultLock;
    }

    @SuppressWarnings("unused")
    public ModuleData lockByDefault() {
        defaultLock = true;
        return this;
    }

    public ModuleData setAllowDuplicate() {
        allowDuplicate = true;
        return this;
    }

    public boolean getAllowDuplicate() {
        return allowDuplicate;
    }

    public ModuleData addSide(final SIDE side) {
        if (renderingSides == null) {
            renderingSides = new ArrayList<>();
        }
        renderingSides.add(side);
        if (side == SIDE.TOP) {
            removeModel("Rails");
        }
        return this;
    }

    @SuppressWarnings("unused")
    public ModuleData useExtraData(final byte defaultValue) {
        extraDataDefaultValue = defaultValue;
        useExtraData = true;
        return this;
    }

    public boolean isUsingExtraData() {
        return useExtraData;
    }

    public byte getDefaultExtraData() {
        return extraDataDefaultValue;
    }

    public ArrayList<SIDE> getRenderingSides() {
        return renderingSides;
    }

    public ModuleData addSides(final SIDE[] sides) {
        for (SIDE side : sides) {
            addSide(side);
        }
        return this;
    }

    public ModuleData addParent(final ModuleData parent) {
        this.parent = parent;
        return this;
    }

    public ModuleData addMessage(final String s) {
        if (message == null) {
            message = new ArrayList<>();
        }
        message.add(s);
        return this;
    }

    public ModuleData conflictsWith(final ModuleData other) {
        Objects.requireNonNull(other, "other");
        if (other == this) {
            throw new IllegalArgumentException("A module cannot conflict with itself");
        }
        if (!nemesis.contains(other)) {
            nemesis.add(other);
        }
        if (!other.nemesis.contains(this)) {
            other.nemesis.add(this);
        }
        return this;
    }

    public ModuleData addRequirement(final ModuleDataGroup requirement) {
        if (this.requirement == null) {
            this.requirement = new ArrayList<>();
        }
        this.requirement.add(requirement);
        return this;
    }

    @SuppressWarnings("unused")
    public float getModelMult() {
        return modelMult;
    }

    public ModuleData setModelMult(final float val) {
        modelMult = val;
        return this;
    }

    public ModuleData addModel(final String tag, final ModelCartbase model) {
        addModel(tag, model, false);
        addModel(tag, model, true);
        return this;
    }

    public ModuleData addModel(final String tag, final ModelCartbase model, final boolean placeholder) {
        if (placeholder) {
            if (modelsPlaceholder == null) {
                modelsPlaceholder = new HashMap<>();
            }
            modelsPlaceholder.put(tag, model);
        } else {
            if (models == null) {
                models = new HashMap<>();
            }
            models.put(tag, model);
        }
        return this;
    }

    public HashMap<String, ModelCartbase> getModels(final boolean placeholder) {
        if (placeholder) {
            return modelsPlaceholder;
        }
        return models;
    }

    public boolean haveModels(final boolean placeholder) {
        if (placeholder) {
            return modelsPlaceholder != null;
        }
        return models != null;
    }

    public ModuleData removeModel(final String tag) {
        if (removedModels == null) {
            removedModels = new ArrayList<>();
        }
        if (!removedModels.contains(tag)) {
            removedModels.add(tag);
        }
        return this;
    }

    public ArrayList<String> getRemovedModels() {
        return removedModels;
    }

    public boolean haveRemovedModels() {
        return removedModels != null;
    }

    public String getDisplayName() {
        return getDisplayNameComponent().getString();
    }

    public String getName() {
        return "module_" + getRawName();
    }

    public Identifier getID() {
        return id;
    }

    public int getCost() {
        return modularCost;
    }

    @Nullable
    public ModuleData getParent() {
        return parent;
    }

    public java.util.List<ModuleData> getNemesis() {
        return java.util.List.copyOf(nemesis);
    }

    public java.util.List<ModuleDataGroup> getRequirement() {
        return java.util.List.copyOf(requirement);
    }

    public String getModuleInfoText(final byte b) {
        return null;
    }

    public String getCartInfoText(final String name, CompoundTag extraData) {
        return name;
    }

    @Nonnull
    public ItemStack getItemStack() {
        return itemSupplier == null ? ItemStack.EMPTY : new ItemStack(itemSupplier.get());
    }

    public ModuleData setItem(Supplier<? extends Item> itemSupplier) {
        this.itemSupplier = Objects.requireNonNull(itemSupplier, "itemSupplier");
        return this;
    }

    public boolean hasItem() {
        return itemSupplier != null;
    }

    public String getTranslationKey() {
        return translationKey;
    }

    public ModuleData setTranslationKey(String translationKey) {
        this.translationKey = Objects.requireNonNull(translationKey, "translationKey");
        return this;
    }

    public Component getDisplayNameComponent() {
        return Component.translatableWithFallback(translationKey, name);
    }

    public void addExtraMessage(Consumer<Component> consumer) {
        if (message != null) {
            consumer.accept(Component.literal(""));
            for (final String m : message) {
                final String str = Component.translatable(m).getString();
                if (str.length() <= MAX_MESSAGE_ROW_LENGTH) {
                    addExtraMessage(consumer, str);
                } else {
                    final String[] words = str.split(" ");
                    String row = "";
                    for (final String word : words) {
                        final String next = (row + " " + word).trim();
                        if (next.length() <= MAX_MESSAGE_ROW_LENGTH) {
                            row = next;
                        } else {
                            addExtraMessage(consumer, row);
                            row = word;
                        }
                    }
                    addExtraMessage(consumer, row);
                }
            }
        }
    }

    private void addExtraMessage(Consumer<Component> consumer, final String str) {
        consumer.accept(Component.literal(ChatFormatting.DARK_GRAY + (ChatFormatting.ITALIC + str + ChatFormatting.RESET)));
    }

    public final void addInformation(Consumer<Component> consumer, final CompoundTag compound, TooltipFlag tooltipFlag) {
        consumer.accept(Component.literal(ChatFormatting.GRAY + Component.translatable("info.stevescarts.modularCost").getString() + ": " + modularCost));
        if (compound != null && compound.contains("Data")) {
            final String extradatainfo = getModuleInfoText(compound.getByteOr("Data", (byte) 0));
            if (extradatainfo != null) {
                consumer.accept(Component.literal(ChatFormatting.WHITE + extradatainfo));
            }
        }
        if (tooltipFlag.hasShiftDown()) {
            if (getRenderingSides() == null || getRenderingSides().size() == 0) {
                consumer.accept(Component.literal(ChatFormatting.DARK_AQUA + Component.translatable("info.stevescarts.noSides").getString()));
            } else {
                StringBuilder sides = new StringBuilder();
                for (int i = 0; i < getRenderingSides().size(); ++i) {
                    final SIDE side = getRenderingSides().get(i);
                    if (i == 0) {
                        sides.append(side.toString());
                    } else if (i == getRenderingSides().size() - 1) {
                        sides.append(" ").append(Component.translatable("info.stevescarts.sidesAnd").getString()).append(" ").append(side.toString());
                    } else {
                        sides.append(", ").append(side.toString());
                    }
                }
                consumer.accept(Component.literal(ChatFormatting.DARK_AQUA + Component.translatable("info.stevescarts.occupiedSides." + (getRenderingSides().size() == 1 ? "singular" : "plural"), sides.toString()).getString()));
            }
            if (getNemesis() != null && getNemesis().size() != 0) {
                if (getRenderingSides() == null || getRenderingSides().size() == 0) {
                    consumer.accept(Component.literal(ChatFormatting.RED + Component.translatable("info.stevescarts.moduleConflictHowever").getString() + ":"));
                } else {
                    consumer.accept(Component.literal(ChatFormatting.RED + Component.translatable("info.stevescarts.moduleConflictAlso").getString() + ":"));
                }
                for (final ModuleData module : getNemesis()) {
                    consumer.accept(Component.literal(ChatFormatting.RED + module.getName()));
                }
            }
            if (parent != null) {
                consumer.accept(Component.literal(ChatFormatting.YELLOW + Component.translatable("info.stevescarts.moduleRequirement").getString() + " " + parent.getName()));
            }
            if (getRequirement() != null && getRequirement().size() != 0) {
                for (final ModuleDataGroup group : getRequirement()) {
                    consumer.accept(Component.literal(ChatFormatting.YELLOW + Component.translatable("info.stevescarts.moduleRequirement").getString() + " " + group.getCountName() + " " + group.getName()));
                }
            }
            if (getAllowDuplicate()) {
                consumer.accept(Component.literal(ChatFormatting.GREEN + Component.translatable("info.stevescarts.allowDuplicates").getString()));
            }
        } else {
            consumer.accept(Component.literal(ChatFormatting.DARK_AQUA + Component.translatable("info.stevescarts.shiftForMore", "SHIFT").getString()));
        }
        consumer.accept(Component.literal(ChatFormatting.BLUE + "Module Type: " + ChatFormatting.WHITE + moduleType.name()));
        addExtraMessage(consumer);
    }

    public String getRawName() {
        return name.replace(":", "").replace("'", "").replace(" ", "_").replace("-", "_").toLowerCase(Locale.ROOT);
    }

    public enum SIDE {
        NONE("info.stevescarts.cartSideNone"), TOP("info.stevescarts.cartSideTop"), CENTER("info.stevescarts.cartSideCenter"), BOTTOM("info.stevescarts.cartSideBottom"), BACK("info.stevescarts.cartSideBack"), LEFT("info.stevescarts.cartSideLeft"), RIGHT("info.stevescarts.cartSideRight"), FRONT("info.stevescarts.cartSideFront");

        private final String name;

        SIDE(final String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return Component.translatable(name).getString();
        }
    }
}
