package vswe.stevescarts.init;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import vswe.stevescarts.Constants;
import vswe.stevescarts.entities.EntityCake;
import vswe.stevescarts.entities.EntityMinecartModular;

public class ModEntities
{
    public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(BuiltInRegistries.ENTITY_TYPE, Constants.MOD_ID);

    public static final DeferredHolder<EntityType<?>, EntityType<EntityMinecartModular>> MODULAR_CART = ENTITIES.register("modular_cart", () -> EntityType.Builder.<EntityMinecartModular>of(EntityMinecartModular::new, MobCategory.MISC).sized(0.98F, 0.7F).clientTrackingRange(8).build("modular_cart"));

    public static final DeferredHolder<EntityType<?>, EntityType<EntityCake>> CAKE = ENTITIES.register("cake", () -> EntityType.Builder.<EntityCake>of(EntityCake::new, MobCategory.MISC).sized(0.98F, 0.7F).build("cake"));
}
