package vswe.stevescarts.modules.data;

import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import vswe.stevescarts.api.StevesCartsAPI;
import vswe.stevescarts.api.modules.ModuleType;
import vswe.stevescarts.client.models.*;
import vswe.stevescarts.entitys.EntityMinecartModular;
import vswe.stevescarts.helpers.Localization;
import vswe.stevescarts.init.ModItems;
import vswe.stevescarts.items.ItemCartModule;
import vswe.stevescarts.modules.ModuleBase;

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

    public static void init()
    {
//        final ModuleDataGroup engineGroup = new ModuleDataGroup(Localization.MODULE_INFO.ENGINE_GROUP);
//        final ModuleDataGroup drillGroup = new ModuleDataGroup(Localization.MODULE_INFO.DRILL_GROUP);
//        final ModuleDataGroup farmerGroup = new ModuleDataGroup(Localization.MODULE_INFO.FARMER_GROUP);
//        final ModuleDataGroup woodcutterGroup = new ModuleDataGroup(Localization.MODULE_INFO.CUTTER_GROUP);
//        final ModuleDataGroup tankGroup = new ModuleDataGroup(Localization.MODULE_INFO.TANK_GROUP);
//        final ModuleDataGroup detectorGroup = new ModuleDataGroup(Localization.MODULE_INFO.ENTITY_GROUP);
//        final ModuleDataGroup shooterGroup = new ModuleDataGroup(Localization.MODULE_INFO.SHOOTER_GROUP);

//        final ModuleData shooter = new ModuleData(28, "Shooter", ModuleShooter.class, 15).addSide(SIDE.TOP);
//        final ModuleData advshooter = new ModuleData(29, "Advanced Shooter", ModuleShooterAdv.class, 50).addSide(SIDE.TOP).addRequirement(detectorGroup);
//        shooterGroup.add(shooter);
//        shooterGroup.add(advshooter);
//
//        final ModuleData animal = new ModuleData(21, "Entity Detector: Animal", ModuleAnimal.class, 1).addParent(advshooter);
//        final ModuleData player = new ModuleData(22, "Entity Detector: Player", ModulePlayer.class, 7).addParent(advshooter);
//        final ModuleData villager = new ModuleData(23, "Entity Detector: Villager", ModuleVillager.class, 1).addParent(advshooter);
//        final ModuleData monster = new ModuleData(24, "Entity Detector: Monster", ModuleMonster.class, 1).addParent(advshooter);
//        final ModuleData bats = new ModuleData(48, "Entity Detector: Bat", ModuleBat.class, 1).addParent(advshooter);
//        detectorGroup.add(animal);
//        detectorGroup.add(player);
//        detectorGroup.add(villager);
//        detectorGroup.add(monster);
//        detectorGroup.add(bats);
//
//        final ModuleData cleaner = new ModuleData(30, "Cleaning Machine", ModuleCleaner.class, 23).addSide(SIDE.CENTER);
//        addNemesis(frontChest, cleaner);
//        new ModuleData(31, "Dynamite Carrier", ModuleDynamite.class, 3).addSide(SIDE.TOP);
//        new ModuleData(32, "Divine Shield", ModuleShield.class, 60);
//        final ModuleData melter = new ModuleData(33, "Melter", ModuleMelter.class, 10);
//        final ModuleData extrememelter = new ModuleData(34, "Extreme Melter", ModuleMelterExtreme.class, 19);
//        addNemesis(melter, extrememelter);
//        new ModuleData(36, "Invisibility Core", ModuleInvisible.class, 21);
//
//        new ModuleData(40, "Note Sequencer", ModuleNote.class, 30).addSides(new SIDE[]{SIDE.RIGHT, SIDE.LEFT});
//        final ModuleData colorizer = new ModuleData(41, "Colorizer", ModuleColorizer.class, 15);
//
//        final ModuleData colorRandomizer = new ModuleData(101, "Color Randomizer", ModuleColorRandomizer.class, 20);
//        addNemesis(colorizer, colorRandomizer);
//
//        new ModuleData(49, "Chunk Loader", ModuleChunkLoader.class, 84);
//
//        new ModuleData(51, "Projectile: Potion", ModulePotion.class, 10).addRequirement(shooterGroup);
//        new ModuleData(52, "Projectile: Fire Charge", ModuleFireball.class, 10).lockByDefault().addRequirement(shooterGroup);
//        new ModuleData(53, "Projectile: Egg", ModuleEgg.class, 10).addRequirement(shooterGroup);
//        final ModuleData snowballshooter = new ModuleData(54, "Projectile: Snowball", ModuleSnowball.class, 10).addRequirement(shooterGroup);
//        final ModuleData cake = new ModuleData(90, "Projectile: Cake", ModuleCake.class, 10).addRequirement(shooterGroup).lock();
//
//        final ModuleData snowgenerator = new ModuleData(55, "Freezer", ModuleSnowCannon.class, 24);
//        addNemesis(snowgenerator, melter);
//        addNemesis(snowgenerator, extrememelter);
//
//        final ModuleData cage = new ModuleData(57, "Cage", ModuleCage.class, 7).addSides(new SIDE[]{SIDE.TOP, SIDE.CENTER});
//        new ModuleData(58, "Crop: Nether Wart", ModuleNetherwart.class, 20).addRequirement(farmerGroup);
//        new ModuleData(59, "Firework display", ModuleFirework.class, 45);
//        final ModuleData internalTank = new ModuleData(63, "Internal SCTank", ModuleInternalTank.class, 37).setAllowDuplicate();
//        final ModuleData sideTank = new ModuleData(64, "Side Tanks", ModuleSideTanks.class, 10).addSides(new SIDE[]{SIDE.RIGHT, SIDE.LEFT});
//        final ModuleData topTank = new ModuleData(65, "Top SCTank", ModuleTopTank.class, 22).addSide(SIDE.TOP);
//        final ModuleData advancedTank = new ModuleData(66, "Advanced SCTank", ModuleAdvancedTank.class, 54).addSides(new SIDE[]{SIDE.CENTER, SIDE.TOP});
//        final ModuleData frontTank = new ModuleData(67, "Front SCTank", ModuleFrontTank.class, 15).addSide(SIDE.FRONT);
//        final ModuleData creativeTank = new ModuleData(72, "Creative SCTank", ModuleCheatTank.class, 1).setAllowDuplicate().addMessage(Localization.MODULE_INFO.OCEAN_MESSAGE);
//        final ModuleData topTankOpen = new ModuleData(73, "Open SCTank", ModuleOpenTank.class, 31).addSide(SIDE.TOP).addMessage(Localization.MODULE_INFO.OPEN_TANK);
//        addNemesis(frontTank, cleaner);
//        tankGroup.add(internalTank).add(sideTank).add(topTank).add(advancedTank).add(frontTank).add(creativeTank).add(topTankOpen);
//
//        new ModuleData(68, "Incinerator", ModuleIncinerator.class, 23).addRequirement(tankGroup).addRequirement(drillGroup);
//
//        addNemesis(thermal0, thermal2);
//
//        final ModuleData cleanerliquid = new ModuleData(71, "Liquid Cleaner", ModuleLiquidDrainer.class, 30).addSide(SIDE.CENTER).addParent(liquidsensors).addRequirement(tankGroup);
//        addNemesis(frontTank, cleanerliquid);
//        addNemesis(frontChest, cleanerliquid);
//
//        new ModuleData(75, "Drill Intelligence", ModuleDrillIntelligence.class, 21).addRequirement(drillGroup);
//        new ModuleData(77, "Power Observer", ModulePowerObserver.class, 12).addRequirement(engineGroup);
//
//        new ModuleData(78, "Steve's Arcade", ModuleArcade.class, 10).addParent(seat);
//
//        final ModuleDataGroup toolGroup = ModuleDataGroup.getCombinedGroup(Localization.MODULE_INFO.TOOL_GROUP, drillGroup, woodcutterGroup);
//        toolGroup.add(farmerGroup);
//        final ModuleDataGroup enchantableGroup = ModuleDataGroup.getCombinedGroup(Localization.MODULE_INFO.TOOL_OR_SHOOTER_GROUP, toolGroup, shooterGroup);
//        new ModuleData(82, "Enchanter", ModuleEnchants.class, 72).addRequirement(enchantableGroup);
//        new ModuleData(83, "Ore Extractor", ModuleOreTracker.class, 80).addRequirement(drillGroup);
//        new ModuleData(85, "Lawn Mower", ModuleFlowerRemover.class, 38).addSides(new SIDE[]{SIDE.RIGHT, SIDE.LEFT});
//        new ModuleData(86, "Milker", ModuleMilker.class, 26).addParent(cage);
//        new ModuleData(87, "Crafter", ModuleCrafter.class, 22).setAllowDuplicate();
//        new ModuleData(89, "Planter Range Extender", ModulePlantSize.class, 20).addRequirement(woodcutterGroup);
//        new ModuleData(91, "Smelter", ModuleSmelter.class, 22).setAllowDuplicate();
//        new ModuleData(92, "Advanced Crafter", ModuleCrafterAdv.class, 42).setAllowDuplicate();
//        new ModuleData(93, "Advanced Smelter", ModuleSmelterAdv.class, 42).setAllowDuplicate();
//        new ModuleData(94, "Information Provider", ModuleLabel.class, 12);
//        new ModuleData(95, "Experience Bank", ModuleExperience.class, 36);
//        new ModuleData(96, "Creative Incinerator", ModuleCreativeIncinerator.class, 1).addRequirement(drillGroup);
//        new ModuleData(97, "Creative Supplies", ModuleCreativeSupplies.class, 1);
//        new ModuleData(99, "Cake Server", ModuleCakeServer.class, 10).addSide(SIDE.TOP).addMessage(Localization.MODULE_INFO.ALPHA_MESSAGE);
//        final ModuleData trickOrTreat = new ModuleData(100, "Trick-or-Treat Cake Server", ModuleCakeServerDynamite.class, 15).addSide(SIDE.TOP);
    }

    ModuleType moduleType;

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

    protected ModuleData lock()
    {
        isLocked = true;
        return this;
    }

    public boolean getEnabledByDefault()
    {
        return !defaultLock;
    }

    protected ModuleData lockByDefault()
    {
        defaultLock = true;
        return this;
    }

    protected ModuleData setAllowDuplicate()
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
        for (int i = 0; i < sides.length; ++i)
        {
            addSide(sides[i]);
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

    protected static void addNemesis(final ModuleData m1, final ModuleData m2)
    {
        m2.addNemesis(m1);
        m1.addNemesis(m2);
    }

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
    protected ModuleData addModel(final String tag, final ModelCartbase model, final boolean placeholder)
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

    public String getCartInfoText(final String name, final byte b)
    {
        return name;
    }

    @Deprecated(forRemoval = true)
    public static NonNullList<ItemStack> getModularItems(@Nonnull ItemStack cart)
    {
        final NonNullList<ItemStack> modules = NonNullList.create();
        if (!cart.isEmpty() && cart.getItem() == ModItems.CARTS.get() && cart.getTag() != null)
        {
            final CompoundTag info = cart.getTag();
            if (info.contains("Modules"))
            {
                final byte[] IDs = info.getByteArray("Modules");
                for (int i = 0; i < IDs.length; ++i)
                {
                    final byte id = IDs[i];
                    //					@Nonnull
                    //					ItemStack module = new ItemStack(ModItems.MODULES.get(), 1);
                    //TODO
                    //					ModItems.MODULES.get().addExtraDataToModule(module, info, i);
                    //					modules.add(module);
                }
            }
        }
        return modules;
    }

    @Deprecated(forRemoval = true)
    public static ItemStack createModularCart(final EntityMinecartModular parentcart)
    {
        @Nonnull ItemStack cart = new ItemStack(ModItems.CARTS.get(), 1);
        final CompoundTag save = new CompoundTag();
        final byte[] moduleIDs = new byte[parentcart.getModules().size()];
        for (int i = 0; i < parentcart.getModules().size(); ++i)
        {
            final ModuleBase module = parentcart.getModules().get(i);
            for (final ModuleData moduledata : StevesCartsAPI.MODULE_REGISTRY.values())
            {
                if (module.getClass() == moduledata.moduleClass)
                {
                    //TODO API CHANGE, NO MORE BYTES
//                    moduleIDs[i] = moduledata.getID();
                    break;
                }
            }
            //TODO
            //			ModItems.MODULES.addExtraDataToModule(save, module, i);
        }
        save.putByteArray("Modules", moduleIDs);
        cart.setTag(save);
        return cart;
    }

    public static ItemStack createModularCartFromItems(final NonNullList<ItemStack> modules)
    {
        ItemStack cart = new ItemStack(ModItems.CARTS.get(), 1);
        ListTag modulesTag = new ListTag();
        for (int i = 0; i < modules.size(); i++)
        {
            CompoundTag moduleTag = new CompoundTag();
            ItemCartModule cartModule = (ItemCartModule) modules.get(i).getItem();
            moduleTag.putString(String.valueOf(i), cartModule.getModuleData().getID().toString());
            modulesTag.add(i, moduleTag);
        }
        cart.getOrCreateTag().put("modules", modulesTag);
        return cart;
    }

    @Deprecated(forRemoval = true)
    public static boolean isItemOfModularType(@Nonnull ItemStack itemstack, final Class<? extends ModuleBase> validClass)
    {
        //		if (itemstack.getItem() == ModItems.MODULES.get()) {
        //TODO
        //			final ModuleData module = ModItems.MODULES.getModuleData(itemstack);
        //			if (module != null && validClass.isAssignableFrom(module.moduleClass)) {
        //				return true;
        //			}
        //		}
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

    public static boolean isValidModuleItem(final int validGroup, @Nonnull ItemStack itemstack)
    {
        if (itemstack.getItem() instanceof ItemCartModule itemCartModule)
        {
            final ModuleData module = itemCartModule.getModuleData();
            return isValidModuleItem(validGroup, module);
        }
        return false;
    }

    //TODO rewrite all of this
    public static boolean isValidModuleItem(final int validGroup, final ModuleData module)
    {
        if (module != null)
        {
            return true;
//            if (validGroup < 0)
//            {
//                for (int i = 0; i < ModuleData.moduleGroups.length; ++i)
//                {
//                    if (ModuleData.moduleGroups[i].isAssignableFrom(module.moduleClass))
//                    {
//                        return false;
//                    }
//                }
//                return true;
//            }
//            if (ModuleData.moduleGroups[validGroup].isAssignableFrom(module.moduleClass))
//            {
//                return true;
//            }
        }
        return false;
    }

    public static boolean isValidModuleCombo(final ModuleDataHull hull, final ArrayList<ModuleData> modules)
    {
        //TODO rewrite all of this
        if(true) return true;

        final int[] max = {1, hull.getEngineMax(), 1, 4, hull.getAddonMax(), 6};
        final int[] current = new int[max.length];
        for (final ModuleData module : modules)
        {
            int id = 5;
            for (int i = 0; i < 5; ++i)
            {
                if (isValidModuleItem(i, module))
                {
                    id = i;
                    break;
                }
            }
            final int[] array = current;
            final int n = id;
            ++array[n];
            if (current[id] > max[id])
            {
                return false;
            }
        }
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
                String sides = "";
                for (int i = 0; i < getRenderingSides().size(); ++i)
                {
                    final SIDE side = getRenderingSides().get(i);
                    if (i == 0)
                    {
                        sides += side.toString();
                    }
                    else if (i == getRenderingSides().size() - 1)
                    {
                        sides = sides + " " + Localization.MODULE_INFO.AND.translate() + " " + side.toString();
                    }
                    else
                    {
                        sides = sides + ", " + side.toString();
                    }
                }
                list.add(Component.literal(ChatFormatting.DARK_AQUA + Localization.MODULE_INFO.OCCUPIED_SIDES.translate(sides, String.valueOf(getRenderingSides().size()))));
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
