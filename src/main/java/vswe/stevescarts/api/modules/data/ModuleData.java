package vswe.stevescarts.api.modules.data;

import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import vswe.stevescarts.api.IModuleItem;
import vswe.stevescarts.api.StevesCartsAPI;
import vswe.stevescarts.api.client.ModelCartbase;
import vswe.stevescarts.api.modules.ModuleType;
import vswe.stevescarts.entities.EntityMinecartModular;
import vswe.stevescarts.helpers.Localization;
import vswe.stevescarts.init.ModItems;
import vswe.stevescarts.api.modules.ModuleBase;
import vswe.stevescarts.items.ItemCartModule;

import javax.annotation.Nonnull;
import java.util.*;

public class ModuleData
{
    private final ResourceLocation id;
    private final Class<? extends ModuleBase> moduleClass;
    private final String name;
    private final int modularCost;
    private ArrayList<SIDE> renderingSides;
    private boolean allowDuplicate;
    private ArrayList<ModuleData> nemesis;
    private ArrayList<ModuleDataGroup> requirement;
    private ModuleData parent;
    private boolean isLocked;
    private boolean defaultLock;
    private ArrayList<Localization.MODULE_INFO> message;
    @OnlyIn(Dist.CLIENT)
    private HashMap<String, ModelCartbase> models;
    @OnlyIn(Dist.CLIENT)
    private HashMap<String, ModelCartbase> modelsPlaceholder;
    private ArrayList<String> removedModels;
    private float modelMult;
    private boolean useExtraData;
    private byte extraDataDefaultValue;
    private static final int MAX_MESSAGE_ROW_LENGTH = 30;

    private final ModuleType moduleType;

    public ModuleData(final ResourceLocation id, final String name, final Class<? extends ModuleBase> moduleClass, ModuleType moduleType, final int modularCost)
    {
        this.nemesis = null;
        this.requirement = null;
        this.parent = null;
        this.modelMult = 0.75f;
        this.id = id;
        this.moduleClass = moduleClass;
        this.name = name;
        this.modularCost = modularCost;
        this.moduleType = moduleType;
    }

    public Class<? extends ModuleBase> getModuleClass()
    {
        return moduleClass;
    }

    public ModuleType getModuleType()
    {
        return moduleType;
    }

    @Deprecated(forRemoval = true)
    public boolean getIsValid()
    {
        return true;
    }

    @Deprecated(forRemoval = true)
    public boolean getIsLocked()
    {
        return isLocked;
    }

    @SuppressWarnings("unused")
    protected ModuleData lock()
    {
        isLocked = true;
        return this;
    }

    @SuppressWarnings("unused")
    public boolean getEnabledByDefault()
    {
        return !defaultLock;
    }

    @SuppressWarnings("unused")
    protected ModuleData lockByDefault()
    {
        defaultLock = true;
        return this;
    }

    public ModuleData setAllowDuplicate()
    {
        allowDuplicate = true;
        return this;
    }

    protected boolean getAllowDuplicate()
    {
        return allowDuplicate;
    }

    public ModuleData addSide(final SIDE side)
    {
        if (renderingSides == null)
        {
            renderingSides = new ArrayList<>();
        }
        renderingSides.add(side);
        if (side == SIDE.TOP)
        {
            removeModel("Rails");
        }
        return this;
    }

    @SuppressWarnings("unused")
    public ModuleData useExtraData(final byte defaultValue)
    {
        extraDataDefaultValue = defaultValue;
        useExtraData = true;
        return this;
    }

    public boolean isUsingExtraData()
    {
        return useExtraData;
    }

    public byte getDefaultExtraData()
    {
        return extraDataDefaultValue;
    }

    public ArrayList<SIDE> getRenderingSides()
    {
        return renderingSides;
    }

    public ModuleData addSides(final SIDE[] sides)
    {
        for (SIDE side : sides)
        {
            addSide(side);
        }
        return this;
    }

    public ModuleData addParent(final ModuleData parent)
    {
        this.parent = parent;
        return this;
    }

    public ModuleData addMessage(final Localization.MODULE_INFO s)
    {
        if (message == null)
        {
            message = new ArrayList<>();
        }
        message.add(s);
        return this;
    }

    protected void addNemesis(final ModuleData nemesis)
    {
        if (this.nemesis == null)
        {
            this.nemesis = new ArrayList<>();
        }
        this.nemesis.add(nemesis);
    }

    public ModuleData addRequirement(final ModuleDataGroup requirement)
    {
        if (this.requirement == null)
        {
            this.requirement = new ArrayList<>();
        }
        this.requirement.add(requirement);
        return this;
    }

    @SuppressWarnings("unused")
    protected static void addNemesis(final ModuleData m1, final ModuleData m2)
    {
        m2.addNemesis(m1);
        m1.addNemesis(m2);
    }

    @SuppressWarnings("unused")
    @OnlyIn(Dist.CLIENT)
    public float getModelMult()
    {
        return modelMult;
    }

    @OnlyIn(Dist.CLIENT)
    public ModuleData setModelMult(final float val)
    {
        modelMult = val;
        return this;
    }

    @OnlyIn(Dist.CLIENT)
    public ModuleData addModel(final String tag, final ModelCartbase model)
    {
        addModel(tag, model, false);
        addModel(tag, model, true);
        return this;
    }

    @OnlyIn(Dist.CLIENT)
    public ModuleData addModel(final String tag, final ModelCartbase model, final boolean placeholder)
    {
        if (placeholder)
        {
            if (modelsPlaceholder == null)
            {
                modelsPlaceholder = new HashMap<>();
            }
            modelsPlaceholder.put(tag, model);
        }
        else
        {
            if (models == null)
            {
                models = new HashMap<>();
            }
            models.put(tag, model);
        }
        return this;
    }

    @OnlyIn(Dist.CLIENT)
    public HashMap<String, ModelCartbase> getModels(final boolean placeholder)
    {
        if (placeholder)
        {
            return modelsPlaceholder;
        }
        return models;
    }

    @OnlyIn(Dist.CLIENT)
    public boolean haveModels(final boolean placeholder)
    {
        if (placeholder)
        {
            return modelsPlaceholder != null;
        }
        return models != null;
    }

    public ModuleData removeModel(final String tag)
    {
        if (removedModels == null)
        {
            removedModels = new ArrayList<>();
        }
        if (!removedModels.contains(tag))
        {
            removedModels.add(tag);
        }
        return this;
    }

    public ArrayList<String> getRemovedModels()
    {
        return removedModels;
    }

    public boolean haveRemovedModels()
    {
        return removedModels != null;
    }

    public String getDisplayName()
    {
        return name;
    }

    public String getName()
    {
        return "module_" + getRawName();
    }

    public ResourceLocation getID()
    {
        return id;
    }

    public int getCost()
    {
        return modularCost;
    }

    protected ModuleData getParent()
    {
        return parent;
    }

    protected ArrayList<ModuleData> getNemesis()
    {
        return nemesis;
    }

    protected ArrayList<ModuleDataGroup> getRequirement()
    {
        return requirement;
    }

    public String getModuleInfoText(final byte b)
    {
        return null;
    }

    public String getCartInfoText(final String name, CompoundTag extraData)
    {
        return name;
    }

    public static NonNullList<ItemStack> getModularItems(@Nonnull ItemStack cart) {
        NonNullList<ItemStack> modules = NonNullList.create();
        if (!cart.isEmpty() && cart.getItem() == ModItems.CARTS.get() && cart.getTag() != null) {
            CompoundTag info = cart.getTag();
            if (info.contains("modules")) {
                int i = 0;
                for (Tag tag : info.getList("modules", 10)) {
                    CompoundTag moduleTag = (CompoundTag) tag;
                    //If this ever explodes, then someone please slap whoever decided to use the arbitrary index of the module used as the key for the id field. WTF...
                    String regName = moduleTag.getString(String.valueOf(i));
                    ModuleData data = StevesCartsAPI.MODULE_REGISTRY.get(new ResourceLocation(regName));
                    ItemCartModule item = (ItemCartModule) ModItems.MODULES.get(data).get();
                    ItemStack module = new ItemStack(item);
                    if (moduleTag.contains("data")) {
                        module.addTagElement("data", moduleTag.getCompound("data"));
                    }
                    modules.add(module);
                    i++;
                }
            }
        }
        return modules;
    }

    public static ItemStack createModularCart(final EntityMinecartModular parentcart) {
        ItemStack cart = new ItemStack(ModItems.CARTS.get(), 1);
        ListTag modulesTag = new ListTag();
        for (int i = 0; i < parentcart.getModules().size(); i++) {
            CompoundTag moduleTag = new CompoundTag();
            ModuleBase module = parentcart.getModules().get(i);
            moduleTag.putString(String.valueOf(i), module.getModuleId().toString());
            if (module.hasExtraData()) {
                moduleTag.put("data", module.writeExtraData());
            }
            modulesTag.add(i, moduleTag);
        }
        cart.getOrCreateTag().put("modules", modulesTag);
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

            if (moduleStack.hasTag() && moduleStack.getOrCreateTag().contains("data")) {
                moduleTag.put("data", moduleStack.getOrCreateTag().getCompound("data"));
            }

            modulesTag.add(i, moduleTag);
        }
        cart.getOrCreateTag().put("modules", modulesTag);
        return cart;
    }

    public static boolean isItemOfModularType(@Nonnull ItemStack itemstack, final Class<? extends ModuleBase> validClass)
    {
        if(itemstack.getItem() instanceof ItemCartModule itemCartModule)
        {
            final ModuleData moduleData = itemCartModule.getModuleData();
            return moduleData != null && validClass.isAssignableFrom(moduleData.moduleClass);
        }
        return false;
    }

    @Nonnull
    public ItemStack getItemStack()
    {
        ItemStack stack = new ItemStack(Items.AIR);
        if (ModItems.MODULES.get(this) != null)
        {
            stack = new ItemStack(ModItems.MODULES.get(this).get());
        }
        return stack;
    }

    public static boolean isValidModuleItem(final ModuleType moduleType, @Nonnull ItemStack itemstack)
    {
        if (itemstack.getItem() instanceof IModuleItem itemCartModule)
        {
            final ModuleData module = itemCartModule.getModuleData();
            return isValidModuleItem(moduleType, module);
        }
        return false;
    }

    //TODO rewrite all of this
    public static boolean isValidModuleItem(final ModuleType moduleType, final ModuleData module)
    {
        if (module != null)
        {
            return module.getModuleType() == moduleType;
        }
        return false;
    }

    public static boolean isValidModuleCombo(final ModuleDataHull hull, final ArrayList<ModuleData> modules)
    {
        //TODO rewrite all of this

//        final int[] max = {1, hull.getEngineMax(), 1, 4, hull.getAddonMax(), 6};
//        final int[] current = new int[max.length];
//        for (final ModuleData module : modules)
//        {
//            int id = 5;
//            for (int i = 0; i < 5; ++i)
//            {
//                if (isValidModuleItem(i, module))
//                {
//                    id = i;
//                    break;
//                }
//            }
//            final int[] array = current;
//            final int n = id;
//            ++array[n];
//            if (current[id] > max[id])
//            {
//                return false;
//            }
//        }
        return true;
    }

    @OnlyIn(Dist.CLIENT)
    public void addExtraMessage(final List<Component> list)
    {
        if (message != null)
        {
            list.add(Component.literal(""));
            for (final Localization.MODULE_INFO m : message)
            {
                final String str = m.translate();
                if (str.length() <= MAX_MESSAGE_ROW_LENGTH)
                {
                    addExtraMessage(list, str);
                }
                else
                {
                    final String[] words = str.split(" ");
                    String row = "";
                    for (final String word : words)
                    {
                        final String next = (row + " " + word).trim();
                        if (next.length() <= MAX_MESSAGE_ROW_LENGTH)
                        {
                            row = next;
                        }
                        else
                        {
                            addExtraMessage(list, row);
                            row = word;
                        }
                    }
                    addExtraMessage(list, row);
                }
            }
        }
    }

    @OnlyIn(Dist.CLIENT)
    private void addExtraMessage(final List<Component> list, final String str)
    {
        list.add(Component.literal(ChatFormatting.DARK_GRAY + (ChatFormatting.ITALIC + str + ChatFormatting.RESET)));
    }

    @OnlyIn(Dist.CLIENT)
    public final void addInformation(final List<Component> list, final CompoundTag compound)
    {
        list.add(Component.literal(ChatFormatting.GRAY + Localization.MODULE_INFO.MODULAR_COST.translate() + ": " + modularCost));
        if (compound != null && compound.contains("Data"))
        {
            final String extradatainfo = getModuleInfoText(compound.getByte("Data"));
            if (extradatainfo != null)
            {
                list.add(Component.literal(ChatFormatting.WHITE + extradatainfo));
            }
        }
        if (Screen.hasShiftDown())
        {
            if (getRenderingSides() == null || getRenderingSides().size() == 0)
            {
                list.add(Component.literal(ChatFormatting.DARK_AQUA + Localization.MODULE_INFO.NO_SIDES.translate()));
            }
            else
            {
                StringBuilder sides = new StringBuilder();
                for (int i = 0; i < getRenderingSides().size(); ++i)
                {
                    final SIDE side = getRenderingSides().get(i);
                    if (i == 0)
                    {
                        sides.append(side.toString());
                    }
                    else if (i == getRenderingSides().size() - 1)
                    {
                        sides.append(" ").append(Localization.MODULE_INFO.AND.translate()).append(" ").append(side.toString());
                    }
                    else
                    {
                        sides.append(", ").append(side.toString());
                    }
                }
                list.add(Component.literal(ChatFormatting.DARK_AQUA + Localization.MODULE_INFO.OCCUPIED_SIDES.translate(sides.toString(), String.valueOf(getRenderingSides().size()))));
            }
            if (getNemesis() != null && getNemesis().size() != 0)
            {
                if (getRenderingSides() == null || getRenderingSides().size() == 0)
                {
                    list.add(Component.literal(ChatFormatting.RED + Localization.MODULE_INFO.CONFLICT_HOWEVER.translate() + ":"));
                }
                else
                {
                    list.add(Component.literal(ChatFormatting.RED + Localization.MODULE_INFO.CONFLICT_ALSO.translate() + ":"));
                }
                for (final ModuleData module : getNemesis())
                {
                    list.add(Component.literal(ChatFormatting.RED + module.getName()));
                }
            }
            if (parent != null)
            {
                list.add(Component.literal(ChatFormatting.YELLOW + Localization.MODULE_INFO.REQUIREMENT.translate() + " " + parent.getName()));
            }
            if (getRequirement() != null && getRequirement().size() != 0)
            {
                for (final ModuleDataGroup group : getRequirement())
                {
                    list.add(Component.literal(ChatFormatting.YELLOW + Localization.MODULE_INFO.REQUIREMENT.translate() + " " + group.getCountName() + " " + group.getName()));
                }
            }
            if (getAllowDuplicate())
            {
                list.add(Component.literal(ChatFormatting.GREEN + Localization.MODULE_INFO.DUPLICATES.translate()));
            }
        }
        else
        {
            list.add(Component.literal(ChatFormatting.DARK_AQUA + Localization.MODULE_INFO.SHIFT_FOR_MORE.translate("SHIFT")));
        }
        list.add(Component.literal(ChatFormatting.BLUE + "Module Type: " + ChatFormatting.WHITE + moduleType.name()));
        addExtraMessage(list);
    }

    public static String checkForErrors(final ModuleDataHull hull, final ArrayList<ModuleData> modules)
    {
        if (getTotalCost(modules) > hull.getCapacity())
        {
            return Localization.MODULE_INFO.CAPACITY_ERROR.translate();
        }
        if (!isValidModuleCombo(hull, modules))
        {
            return Localization.MODULE_INFO.COMBINATION_ERROR.translate();
        }
        for (int i = 0; i < modules.size(); ++i)
        {
            final ModuleData mod1 = modules.get(i);
            if (mod1.getCost() > hull.getComplexityMax())
            {
                return Localization.MODULE_INFO.COMPLEXITY_ERROR.translate(mod1.getName());
            }
            if (mod1.getParent() != null && !modules.contains(mod1.getParent()))
            {
                return Localization.MODULE_INFO.PARENT_ERROR.translate(mod1.getName(), mod1.getParent().getName());
            }
            if (mod1.getNemesis() != null)
            {
                for (final ModuleData nemesis : mod1.getNemesis())
                {
                    if (modules.contains(nemesis))
                    {
                        return Localization.MODULE_INFO.NEMESIS_ERROR.translate(mod1.getName(), nemesis.getName());
                    }
                }
            }
            if (mod1.getRequirement() != null)
            {
                for (final ModuleDataGroup group : mod1.getRequirement())
                {
                    int count = 0;
                    for (final ModuleData mod2 : group.getModules())
                    {
                        for (final ModuleData mod3 : modules)
                        {
                            if (mod2.equals(mod3))
                            {
                                ++count;
                            }
                        }
                    }
                    if (count < group.getCount())
                    {
                        return Localization.MODULE_INFO.PARENT_ERROR.translate(mod1.getName(), group.getCountName() + " " + group.getName());
                    }
                }
            }
            for (int j = i + 1; j < modules.size(); ++j)
            {
                final ModuleData mod4 = modules.get(j);
                if (mod1 == mod4)
                {
                    if (!mod1.getAllowDuplicate())
                    {
                        return Localization.MODULE_INFO.DUPLICATE_ERROR.translate(mod1.getName());
                    }
                }
                else if (mod1.getRenderingSides() != null && mod4.getRenderingSides() != null)
                {
                    SIDE clash = SIDE.NONE;
                    for (final SIDE side1 : mod1.getRenderingSides())
                    {
                        for (final SIDE side2 : mod4.getRenderingSides())
                        {
                            if (side1 == side2)
                            {
                                clash = side1;
                                break;
                            }
                        }
                        if (clash != SIDE.NONE)
                        {
                            break;
                        }
                    }
                    if (clash != SIDE.NONE)
                    {
                        return Localization.MODULE_INFO.CLASH_ERROR.translate(mod1.getName(), mod4.getName(), clash.toString());
                    }
                }
            }
        }
        return null;
    }

    public static int getTotalCost(final ArrayList<ModuleData> modules)
    {
        int currentCost = 0;
        for (final ModuleData module : modules)
        {
            currentCost += module.getCost();
        }
        return currentCost;
    }

    private static long calculateCombinations()
    {
        long combinations = 0L;
        final ArrayList<ModuleData> potential = new ArrayList<>();
        for (final ModuleData module : StevesCartsAPI.MODULE_REGISTRY.values())
        {
            if (!(module instanceof ModuleDataHull))
            {
                potential.add(module);
            }
        }
        for (final ModuleData module : StevesCartsAPI.MODULE_REGISTRY.values())
        {
            if (module instanceof ModuleDataHull)
            {
                final ArrayList<ModuleData> modules = new ArrayList<>();
                combinations += populateHull((ModuleDataHull) module, modules, (ArrayList<ModuleData>) potential.clone(), 0);
                System.out.println("Hull added: " + combinations);
            }
        }
        return combinations;
    }

    private static long populateHull(final ModuleDataHull hull, final ArrayList<ModuleData> attached, final ArrayList<ModuleData> potential, final int depth)
    {
        if (checkForErrors(hull, attached) != null)
        {
            return 0L;
        }
        long combinations = 1L;
        final Iterator itt = potential.iterator();
        while (itt.hasNext())
        {
            final ModuleData module = (ModuleData) itt.next();
            final ArrayList<ModuleData> attachedCopy = (ArrayList<ModuleData>) attached.clone();
            attachedCopy.add(module);
            final ArrayList<ModuleData> potentialCopy = (ArrayList<ModuleData>) potential.clone();
            itt.remove();
            combinations += populateHull(hull, attachedCopy, potentialCopy, depth + 1);
            if (depth < 3)
            {
                System.out.println("Modular state[" + depth + "]: " + combinations);
            }
        }
        return combinations;
    }

    public String getRawName()
    {
        return name.replace(":", "").replace("'", "").replace(" ", "_").replace("-", "_").toLowerCase();
    }

    public enum SIDE
    {
        NONE(Localization.MODULE_INFO.SIDE_NONE), TOP(Localization.MODULE_INFO.SIDE_TOP), CENTER(Localization.MODULE_INFO.SIDE_CENTER), BOTTOM(Localization.MODULE_INFO.SIDE_BOTTOM), BACK(Localization.MODULE_INFO.SIDE_BACK), LEFT(Localization.MODULE_INFO.SIDE_LEFT), RIGHT(Localization.MODULE_INFO.SIDE_RIGHT), FRONT(Localization.MODULE_INFO.SIDE_FRONT);

        private final Localization.MODULE_INFO name;

        SIDE(final Localization.MODULE_INFO name)
        {
            this.name = name;
        }

        @Override
        public String toString()
        {
            return name.translate();
        }
    }
}
