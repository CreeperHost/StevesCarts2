package vswe.stevescarts.init;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import vswe.stevescarts.Constants;
import vswe.stevescarts.entitys.EntityCake;
import vswe.stevescarts.entitys.EntityMinecartModular;

public class ModEntities
{
    public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, Constants.MOD_ID);

    public static final RegistryObject<EntityType<EntityMinecartModular>> MODULAR_CART = ENTITIES.register("modular_cart", () -> EntityType.Builder.<EntityMinecartModular>of(EntityMinecartModular::new, MobCategory.MISC).sized(0.98F, 0.7F).clientTrackingRange(8).setCustomClientFactory((spawnEntity, world) -> new EntityMinecartModular(world)).build("modular_cart"));

    public static final RegistryObject<EntityType<EntityCake>> CAKE = ENTITIES.register("cake", () -> EntityType.Builder.<EntityCake>of(EntityCake::new, MobCategory.MISC).sized(0.98F, 0.7F).build("cake"));
}
