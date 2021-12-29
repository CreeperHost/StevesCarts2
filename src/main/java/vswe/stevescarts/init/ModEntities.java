package vswe.stevescarts.init;

import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import vswe.stevescarts.Constants;
import vswe.stevescarts.entitys.EntityCake;
import vswe.stevescarts.entitys.EntityMinecartModular;

public class ModEntities
{
    public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(ForgeRegistries.ENTITIES, Constants.MOD_ID);

    public static final RegistryObject<EntityType<EntityMinecartModular>> MODULAR_CART = ENTITIES.register("modular_cart", () -> EntityType.Builder.<EntityMinecartModular>of(EntityMinecartModular::new, EntityClassification.MISC).sized(0.98F, 0.7F).clientTrackingRange(8).setCustomClientFactory((spawnEntity, world) -> new EntityMinecartModular(world)).build("modular_cart"));

    public static final RegistryObject<EntityType<EntityCake>> CAKE = ENTITIES.register("cake", () -> EntityType.Builder.<EntityCake>of(EntityCake::new, EntityClassification.MISC).sized(0.98F, 0.7F).build("cake"));
}
