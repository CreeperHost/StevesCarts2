package vswe.stevescarts.helpers;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ambient.Bat;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.chicken.Chicken;
import net.minecraft.world.entity.animal.cow.Cow;
import net.minecraft.world.entity.animal.cow.MushroomCow;
import net.minecraft.world.entity.animal.feline.Ocelot;
import net.minecraft.world.entity.animal.golem.SnowGolem;
import net.minecraft.world.entity.animal.pig.Pig;
import net.minecraft.world.entity.animal.sheep.Sheep;
import net.minecraft.world.entity.animal.wolf.Wolf;
import net.minecraft.world.entity.monster.Blaze;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.monster.Silverfish;
import net.minecraft.world.entity.monster.Witch;
import net.minecraft.world.entity.monster.skeleton.Skeleton;
import net.minecraft.world.entity.monster.spider.Spider;
import net.minecraft.world.entity.monster.zombie.Zombie;
import net.minecraft.world.entity.monster.zombie.ZombieVillager;
import net.minecraft.world.entity.monster.zombie.ZombifiedPiglin;
import net.minecraft.world.entity.npc.villager.Villager;
import net.minecraft.world.entity.player.Player;
import vswe.stevescarts.api.modules.ModuleBase;
import vswe.stevescarts.api.modules.interfaces.IActivatorModule;
import vswe.stevescarts.api.modules.interfaces.ISuppliesModule;
import vswe.stevescarts.api.modules.template.ModuleChest;
import vswe.stevescarts.entities.ModularMinecart;
import vswe.stevescarts.modules.addons.ModuleChunkLoader;
import vswe.stevescarts.modules.addons.ModuleInvisible;
import vswe.stevescarts.modules.addons.ModulePowerObserver;
import vswe.stevescarts.modules.addons.ModuleShield;
import vswe.stevescarts.modules.realtimers.ModuleCage;
import vswe.stevescarts.modules.realtimers.ModuleCakeServer;
import vswe.stevescarts.modules.realtimers.ModuleShooter;
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

public class ModuleState {
    private static HashMap<Byte, ModuleState> states;

    static {
        ModuleState.states = new HashMap<>();
        new ModuleState(0, ModuleRailer.class, "gui.stevescarts.stateRails", STATETYPE.SUPPLY);
        new ModuleState(1, ModuleTorch.class, "gui.stevescarts.stateTorches", STATETYPE.SUPPLY);
        new ModuleState(2, ModuleWoodcutter.class, "gui.stevescarts.stateSaplings", STATETYPE.SUPPLY);
        new ModuleState(3, ModuleFarmer.class, "gui.stevescarts.sateSeeds", STATETYPE.SUPPLY);
        new ModuleState(5, ModuleBridge.class, "gui.stevescarts.stateBridge", STATETYPE.SUPPLY);
        new ModuleState(40, ModuleShooter.class, "gui.stevescarts.stateProjectiles", STATETYPE.SUPPLY);
        new ModuleState(41, ModuleFertilizer.class, "gui.stevescarts.stateFertilizing", STATETYPE.SUPPLY);
        new ModuleState(49, ModuleCakeServer.class, "gui.stevescarts.stateCake", STATETYPE.SUPPLY);
        new ModuleState(6, ModuleShield.class, "gui.stevescarts.stateShield", STATETYPE.ACTIVATION);
        new ModuleState(7, ModuleChunkLoader.class, "gui.stevescarts.stateChunk", STATETYPE.ACTIVATION);
        new ModuleState(8, ModuleInvisible.class, "gui.stevescarts.stateInvisibility", STATETYPE.ACTIVATION);
        new ModuleState(9, ModuleDrill.class, "gui.stevescarts.stateDrill", STATETYPE.ACTIVATION);
        new ModuleState(12, ModuleCage.class, "gui.stevescarts.stateCage", STATETYPE.ACTIVATION);
        new ModuleStateInv(10, "gui.stevescarts.stateStorageFull", true);
        new ModuleStateInv(11, "gui.stevescarts.stateStorageEmpty", false);
        new ModuleStatePassenger(13, "gui.stevescarts.statePassenger", LivingEntity.class);
        new ModuleStatePassenger(14, "gui.stevescarts.stateAnimal", Animal.class);
        new ModuleStatePassenger(15, "gui.stevescarts.stateTameable", TamableAnimal.class);
        new ModuleStatePassenger(16, "gui.stevescarts.stateBreedable", AgeableMob.class);
        new ModuleStatePassenger(17, "gui.stevescarts.stateHostile", Enemy.class);
        new ModuleStatePassenger(18, "gui.stevescarts.stateCreeper", Creeper.class);
        new ModuleStatePassenger(19, "gui.stevescarts.stateSkeleton", Skeleton.class);
        new ModuleStatePassenger(20, "gui.stevescarts.stateSpider", Spider.class);
        new ModuleStatePassenger(21, "gui.stevescarts.stateZombie", Zombie.class);
        new ModuleStatePassenger(22, "gui.stevescarts.stateZombiePigMan", ZombifiedPiglin.class);
        new ModuleStatePassenger(23, "gui.stevescarts.stateSilverFish", Silverfish.class);
        new ModuleStatePassenger(24, "gui.stevescarts.stateBlaze", Blaze.class);
        new ModuleStatePassenger(25, "gui.stevescarts.stateBat", Bat.class);
        new ModuleStatePassenger(26, "gui.stevescarts.stateWitch", Witch.class);
        new ModuleStatePassenger(27, "gui.stevescarts.statePig", Pig.class);
        new ModuleStatePassenger(28, "gui.stevescarts.stateSheep", Sheep.class);
        new ModuleStatePassenger(29, "gui.stevescarts.stateCow", Cow.class);
        new ModuleStatePassenger(30, "gui.stevescarts.stateMooshroom", MushroomCow.class);
        new ModuleStatePassenger(31, "gui.stevescarts.stateChicken", Chicken.class);
        new ModuleStatePassenger(32, "gui.stevescarts.stateWolf", Wolf.class);
        new ModuleStatePassenger(33, "gui.stevescarts.stateSnowGolem", SnowGolem.class);
        new ModuleStatePassenger(34, "gui.stevescarts.stateOcelot", Ocelot.class);
        new ModuleStatePassenger(35, "gui.stevescarts.stateVillager", Villager.class);
        new ModuleStatePassenger(36, "gui.stevescarts.statePlayer", Player.class);
        new ModuleStatePassenger(37, "gui.stevescarts.stateZombieVillager", Zombie.class) {
            @Override
            public boolean isPassengerValid(final Entity passenger) {
                return passenger instanceof ZombieVillager;
            }
        };
        new ModuleStatePassenger(38, "gui.stevescarts.stateChild", AgeableMob.class) {
            @Override
            public boolean isPassengerValid(final Entity passenger) {
                return ((AgeableMob) passenger).isBaby();
            }
        };
        new ModuleStatePassenger(39, "gui.stevescarts.stateTamed", TamableAnimal.class) {
            @Override
            public boolean isPassengerValid(final Entity passenger) {
                return ((TamableAnimal) passenger).isTame();
            }
        };
        new ModuleStatePower(42, "gui.stevescarts.statePowerRed", 0);
        new ModuleStatePower(43, "gui.stevescarts.statePowerBlue", 1);
        new ModuleStatePower(44, "gui.stevescarts.statePowerGreen", 2);
        new ModuleStatePower(45, "gui.stevescarts.statePowerYellow", 3);
        new ModuleStateTank(46, "gui.stevescarts.stateTanksFull", true, false);
        new ModuleStateTank(47, "gui.stevescarts.stateTanksEmpty", false, false);
        new ModuleStateTank(48, "gui.stevescarts.stateTankEmpty", false, true);
    }

    private final Class<? extends ModuleBase> moduleClass;
    private final String name;
    private final byte id;
    private final STATETYPE type;

    public ModuleState(final int id, final Class<? extends ModuleBase> moduleClass, final String name, final STATETYPE type) {
        this.moduleClass = moduleClass;
        this.name = name;
        this.id = (byte) id;
        this.type = type;
        ModuleState.states.put(this.id, this);
    }

    public static HashMap<Byte, ModuleState> getStates() {
        return ModuleState.states;
    }

    public static Collection<ModuleState> getStateList() {
        return ModuleState.states.values();
    }

    public boolean evaluate(ModularMinecart cart) {
        switch (type) {
            case SUPPLY: {
                for (final ModuleBase module : cart.modules()) {
                    if (isModuleOfCorrectType(module) && module instanceof ISuppliesModule) {
                        return ((ISuppliesModule) module).haveSupplies();
                    }
                }
                break;
            }
            case ACTIVATION: {
                for (final ModuleBase module : cart.modules()) {
                    if (isModuleOfCorrectType(module) && module instanceof IActivatorModule) {
                        return ((IActivatorModule) module).isActive(0);
                    }
                }
                break;
            }
            case INVENTORY: {
                if (this instanceof ModuleStateInv) {
                    boolean hasModule = false;
                    for (final ModuleBase module2 : cart.modules()) {
                        if (isModuleOfCorrectType(module2)) {
                            final ModuleChest chest = (ModuleChest) module2;
                            if (((ModuleStateInv) this).full && !chest.isCompletelyFilled()) {
                                return false;
                            }
                            if (!((ModuleStateInv) this).full && !chest.isCompletelyEmpty()) {
                                return false;
                            }
                            hasModule = true;
                        }
                    }
                    return hasModule;
                }
                break;
            }
            case PASSENGER: {
                if (!cart.getPassengers().isEmpty()) {
                    Entity passenger = cart.getPassengers().get(0);
                    return ((ModuleStatePassenger) this).passengerClass.isAssignableFrom(passenger.getClass()) && ((ModuleStatePassenger) this).isPassengerValid(passenger);
                }
                break;
            }
            case POWER: {
                for (final ModuleBase module2 : cart.modules()) {
                    if (isModuleOfCorrectType(module2)) {
                        return ((ModulePowerObserver) module2).isAreaActive(((ModuleStatePower) this).areaId);
                    }
                }
                break;
            }
            case TANK: {
                if (this instanceof ModuleStateTank) {
                    boolean hasModule2 = false;
                    for (final ModuleBase module3 : cart.modules()) {
                        if (isModuleOfCorrectType(module3)) {
                            final ModuleTank tank = (ModuleTank) module3;
                            boolean result;
                            if (((ModuleStateTank) this).full) {
                                result = tank.isCompletelyFilled();
                            } else {
                                result = tank.isCompletelyEmpty();
                            }
                            if (result == ((ModuleStateTank) this).individual) {
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

    private boolean isModuleOfCorrectType(final ModuleBase module) {
        return moduleClass.isAssignableFrom(module.getClass());
    }

    public String getName() {
        return Component.translatable(name).getString();
    }

    public byte getID() {
        return id;
    }

    public enum STATETYPE {
        SUPPLY, ACTIVATION, INVENTORY, PASSENGER, POWER, TANK
    }

    private static class ModuleStateInv extends ModuleState {
        private final boolean full;

        public ModuleStateInv(final int id, final String name, final boolean full) {
            super(id, ModuleChest.class, name, STATETYPE.INVENTORY);
            this.full = full;
        }
    }

    private static class ModuleStateTank extends ModuleState {
        private final boolean full;
        private final boolean individual;

        public ModuleStateTank(final int id, final String name, final boolean full, final boolean individual) {
            super(id, ModuleTank.class, name, STATETYPE.TANK);
            this.full = full;
            this.individual = individual;
        }
    }

    private static class ModuleStatePassenger extends ModuleState {
        private final Class passengerClass;

        public ModuleStatePassenger(final int id, final String name, final Class passengerClass) {
            super(id, null, name, STATETYPE.PASSENGER);
            this.passengerClass = passengerClass;
        }

        public boolean isPassengerValid(final Entity passenger) {
            return passengerClass.isAssignableFrom(passenger.getClass());
        }
    }

    private static class ModuleStatePower extends ModuleState {
        private final int areaId;

        public ModuleStatePower(final int id, final String name, final int areaId) {
            super(id, ModulePowerObserver.class, name, STATETYPE.POWER);
            this.areaId = areaId;
        }
    }
}
