package vswe.stevescarts.helpers;

import net.minecraft.entity.AgeableEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.merchant.villager.VillagerEntity;
import net.minecraft.entity.monster.*;
import net.minecraft.entity.monster.piglin.PiglinEntity;
import net.minecraft.entity.passive.*;
import net.minecraft.entity.player.PlayerEntity;
import vswe.stevescarts.entitys.EntityMinecartModular;
import vswe.stevescarts.modules.IActivatorModule;
import vswe.stevescarts.modules.ISuppliesModule;
import vswe.stevescarts.modules.ModuleBase;
import vswe.stevescarts.modules.addons.ModuleChunkLoader;
import vswe.stevescarts.modules.addons.ModuleInvisible;
import vswe.stevescarts.modules.addons.ModulePowerObserver;
import vswe.stevescarts.modules.addons.ModuleShield;
import vswe.stevescarts.modules.realtimers.ModuleCage;
import vswe.stevescarts.modules.realtimers.ModuleCakeServer;
import vswe.stevescarts.modules.realtimers.ModuleShooter;
import vswe.stevescarts.modules.storages.chests.ModuleChest;
import vswe.stevescarts.modules.storages.tanks.ModuleTank;
import vswe.stevescarts.modules.workers.ModuleBridge;
import vswe.stevescarts.modules.workers.ModuleFertilizer;
import vswe.stevescarts.modules.workers.ModuleRailer;
import vswe.stevescarts.modules.workers.ModuleTorch;
import vswe.stevescarts.modules.workers.tools.ModuleDrill;
import vswe.stevescarts.modules.workers.tools.ModuleFarmer;
import vswe.stevescarts.modules.workers.tools.ModuleWoodcutter;

import java.util.Collection;
import java.util.HashMap;

public class ModuleState
{
    private static HashMap<Byte, ModuleState> states;
    private Class<? extends ModuleBase> moduleClass;
    private Localization.GUI.DETECTOR name;
    private byte id;
    private STATETYPE type;

    public static HashMap<Byte, ModuleState> getStates()
    {
        return ModuleState.states;
    }

    public static Collection<ModuleState> getStateList()
    {
        return ModuleState.states.values();
    }

    public ModuleState(final int id, final Class<? extends ModuleBase> moduleClass, final Localization.GUI.DETECTOR name, final STATETYPE type)
    {
        this.moduleClass = moduleClass;
        this.name = name;
        this.id = (byte) id;
        this.type = type;
        ModuleState.states.put(this.id, this);
    }

    public boolean evaluate(final EntityMinecartModular cart)
    {
        switch (type)
        {
            case SUPPLY:
            {
                for (final ModuleBase module : cart.getModules())
                {
                    if (isModuleOfCorrectType(module) && module instanceof ISuppliesModule)
                    {
                        return ((ISuppliesModule) module).haveSupplies();
                    }
                }
                break;
            }
            case ACTIVATION:
            {
                for (final ModuleBase module : cart.getModules())
                {
                    if (isModuleOfCorrectType(module) && module instanceof IActivatorModule)
                    {
                        return ((IActivatorModule) module).isActive(0);
                    }
                }
                break;
            }
            case INVENTORY:
            {
                if (this instanceof ModuleStateInv)
                {
                    boolean hasModule = false;
                    for (final ModuleBase module2 : cart.getModules())
                    {
                        if (isModuleOfCorrectType(module2))
                        {
                            final ModuleChest chest = (ModuleChest) module2;
                            if (((ModuleStateInv) this).full && !chest.isCompletelyFilled())
                            {
                                return false;
                            }
                            if (!((ModuleStateInv) this).full && !chest.isCompletelyEmpty())
                            {
                                return false;
                            }
                            hasModule = true;
                        }
                    }
                    return hasModule;
                }
                break;
            }
            case PASSENGER:
            {
                if (!cart.getPassengers().isEmpty())
                {
                    Entity passenger = cart.getPassengers().get(0);
                    return ((ModuleStatePassenger) this).passengerClass.isAssignableFrom(passenger.getClass()) && ((ModuleStatePassenger) this).isPassengerValid(passenger);
                }
                break;
            }
            case POWER:
            {
                for (final ModuleBase module2 : cart.getModules())
                {
                    if (isModuleOfCorrectType(module2))
                    {
                        return ((ModulePowerObserver) module2).isAreaActive(((ModuleStatePower) this).areaId);
                    }
                }
                break;
            }
            case TANK:
            {
                if (this instanceof ModuleStateTank)
                {
                    boolean hasModule2 = false;
                    for (final ModuleBase module3 : cart.getModules())
                    {
                        if (isModuleOfCorrectType(module3))
                        {
                            final ModuleTank tank = (ModuleTank) module3;
                            boolean result;
                            if (((ModuleStateTank) this).full)
                            {
                                result = tank.isCompletelyFilled();
                            }
                            else
                            {
                                result = tank.isCompletelyEmpty();
                            }
                            if (result == ((ModuleStateTank) this).individual)
                            {
                                return result;
                            }
                            hasModule2 = !((ModuleStateTank) this).individual;
                        }
                    }
                    return hasModule2;
                }
                break;
            }
        }
        return false;
    }

    private boolean isModuleOfCorrectType(final ModuleBase module)
    {
        return moduleClass.isAssignableFrom(module.getClass());
    }

    public String getName()
    {
        return name.translate();
    }

    public byte getID()
    {
        return id;
    }

    static
    {
        ModuleState.states = new HashMap<>();
        new ModuleState(0, ModuleRailer.class, Localization.GUI.DETECTOR.RAIL, STATETYPE.SUPPLY);
        new ModuleState(1, ModuleTorch.class, Localization.GUI.DETECTOR.TORCH, STATETYPE.SUPPLY);
        new ModuleState(2, ModuleWoodcutter.class, Localization.GUI.DETECTOR.SAPLING, STATETYPE.SUPPLY);
        new ModuleState(3, ModuleFarmer.class, Localization.GUI.DETECTOR.SEED, STATETYPE.SUPPLY);
        new ModuleState(5, ModuleBridge.class, Localization.GUI.DETECTOR.BRIDGE, STATETYPE.SUPPLY);
        new ModuleState(40, ModuleShooter.class, Localization.GUI.DETECTOR.PROJECTILE, STATETYPE.SUPPLY);
        new ModuleState(41, ModuleFertilizer.class, Localization.GUI.DETECTOR.FERTILIZING, STATETYPE.SUPPLY);
        new ModuleState(49, ModuleCakeServer.class, Localization.GUI.DETECTOR.CAKE, STATETYPE.SUPPLY);
        new ModuleState(6, ModuleShield.class, Localization.GUI.DETECTOR.SHIELD, STATETYPE.ACTIVATION);
        new ModuleState(7, ModuleChunkLoader.class, Localization.GUI.DETECTOR.CHUNK, STATETYPE.ACTIVATION);
        new ModuleState(8, ModuleInvisible.class, Localization.GUI.DETECTOR.INVISIBILITY, STATETYPE.ACTIVATION);
        new ModuleState(9, ModuleDrill.class, Localization.GUI.DETECTOR.DRILL, STATETYPE.ACTIVATION);
        new ModuleState(12, ModuleCage.class, Localization.GUI.DETECTOR.CAGE, STATETYPE.ACTIVATION);
        new ModuleStateInv(10, Localization.GUI.DETECTOR.STORAGE_FULL, true);
        new ModuleStateInv(11, Localization.GUI.DETECTOR.STORAGE_EMPTY, false);
        new ModuleStatePassenger(13, Localization.GUI.DETECTOR.PASSENGER, LivingEntity.class);
        new ModuleStatePassenger(14, Localization.GUI.DETECTOR.ANIMAL, AnimalEntity.class);
        new ModuleStatePassenger(15, Localization.GUI.DETECTOR.TAMEABLE, TameableEntity.class);
        new ModuleStatePassenger(16, Localization.GUI.DETECTOR.BREEDABLE, AgeableEntity.class);
        new ModuleStatePassenger(17, Localization.GUI.DETECTOR.HOSTILE, IMob.class);
        new ModuleStatePassenger(18, Localization.GUI.DETECTOR.CREEPER, CreeperEntity.class);
        new ModuleStatePassenger(19, Localization.GUI.DETECTOR.SKELETON, SkeletonEntity.class);
        new ModuleStatePassenger(20, Localization.GUI.DETECTOR.SPIDER, SpiderEntity.class);
        new ModuleStatePassenger(21, Localization.GUI.DETECTOR.ZOMBIE, ZombieEntity.class);
        new ModuleStatePassenger(22, Localization.GUI.DETECTOR.PIG_MAN, PiglinEntity.class);
        new ModuleStatePassenger(23, Localization.GUI.DETECTOR.SILVERFISH, SilverfishEntity.class);
        new ModuleStatePassenger(24, Localization.GUI.DETECTOR.BLAZE, BlazeEntity.class);
        new ModuleStatePassenger(25, Localization.GUI.DETECTOR.BAT, BatEntity.class);
        new ModuleStatePassenger(26, Localization.GUI.DETECTOR.WITCH, WitchEntity.class);
        new ModuleStatePassenger(27, Localization.GUI.DETECTOR.PIG, PiglinEntity.class);
        new ModuleStatePassenger(28, Localization.GUI.DETECTOR.SHEEP, SheepEntity.class);
        new ModuleStatePassenger(29, Localization.GUI.DETECTOR.COW, CowEntity.class);
        new ModuleStatePassenger(30, Localization.GUI.DETECTOR.MOOSHROOM, MooshroomEntity.class);
        new ModuleStatePassenger(31, Localization.GUI.DETECTOR.CHICKEN, ChickenEntity.class);
        new ModuleStatePassenger(32, Localization.GUI.DETECTOR.WOLF, WolfEntity.class);
        //		new ModuleStatePassenger(33, Localization.GUI.DETECTOR.SNOW_GOLEM, EntitySnowman.class);
        new ModuleStatePassenger(34, Localization.GUI.DETECTOR.OCELOT, OcelotEntity.class);
        new ModuleStatePassenger(35, Localization.GUI.DETECTOR.VILLAGER, VillagerEntity.class);
        new ModuleStatePassenger(36, Localization.GUI.DETECTOR.PLAYER, PlayerEntity.class);
        new ModuleStatePassenger(37, Localization.GUI.DETECTOR.ZOMBIE, ZombieEntity.class)
        {
            @Override
            public boolean isPassengerValid(final Entity passenger)
            {
                return passenger instanceof ZombieVillagerEntity;
            }
        };
        new ModuleStatePassenger(38, Localization.GUI.DETECTOR.CHILD, AgeableEntity.class)
        {
            @Override
            public boolean isPassengerValid(final Entity passenger)
            {
                return ((AgeableEntity) passenger).getAge() > 0;
            }
        };
        new ModuleStatePassenger(39, Localization.GUI.DETECTOR.TAMED, TameableEntity.class)
        {
            @Override
            public boolean isPassengerValid(final Entity passenger)
            {
                return ((TameableEntity) passenger).isTame();
            }
        };
        new ModuleStatePower(42, Localization.GUI.DETECTOR.POWER_RED, 0);
        new ModuleStatePower(43, Localization.GUI.DETECTOR.POWER_BLUE, 1);
        new ModuleStatePower(44, Localization.GUI.DETECTOR.POWER_GREEN, 2);
        new ModuleStatePower(45, Localization.GUI.DETECTOR.POWER_YELLOW, 3);
        new ModuleStateTank(46, Localization.GUI.DETECTOR.TANKS_FULL, true, false);
        new ModuleStateTank(47, Localization.GUI.DETECTOR.TANKS_EMPTY, false, false);
        new ModuleStateTank(48, Localization.GUI.DETECTOR.TANK_EMPTY, false, true);
    }

    private static class ModuleStateInv extends ModuleState
    {
        private boolean full;

        public ModuleStateInv(final int id, final Localization.GUI.DETECTOR name, final boolean full)
        {
            super(id, ModuleChest.class, name, STATETYPE.INVENTORY);
            this.full = full;
        }
    }

    private static class ModuleStateTank extends ModuleState
    {
        private boolean full;
        private boolean individual;

        public ModuleStateTank(final int id, final Localization.GUI.DETECTOR name, final boolean full, final boolean individual)
        {
            super(id, ModuleTank.class, name, STATETYPE.TANK);
            this.full = full;
            this.individual = individual;
        }
    }

    private static class ModuleStatePassenger extends ModuleState
    {
        private Class passengerClass;

        public ModuleStatePassenger(final int id, final Localization.GUI.DETECTOR name, final Class passengerClass)
        {
            super(id, null, name, STATETYPE.PASSENGER);
            this.passengerClass = passengerClass;
        }

        public boolean isPassengerValid(final Entity passenger)
        {
            return passengerClass.isAssignableFrom(passenger.getClass());
        }
    }

    private static class ModuleStatePower extends ModuleState
    {
        private int areaId;

        public ModuleStatePower(final int id, final Localization.GUI.DETECTOR name, final int areaId)
        {
            super(id, ModulePowerObserver.class, name, STATETYPE.POWER);
            this.areaId = areaId;
        }
    }

    public enum STATETYPE
    {
        SUPPLY, ACTIVATION, INVENTORY, PASSENGER, POWER, TANK
    }
}
