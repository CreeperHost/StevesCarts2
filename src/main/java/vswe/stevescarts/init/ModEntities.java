package vswe.stevescarts.init;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import vswe.stevescarts.Constants;
import vswe.stevescarts.entities.EntityCake;
import vswe.stevescarts.entities.ModularMinecart;

public class ModEntities {
    public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(BuiltInRegistries.ENTITY_TYPE, Constants.MOD_ID);

    public static final DeferredHolder<EntityType<?>, EntityType<ModularMinecart>> MODULAR_CART = ENTITIES.register("modular_cart", () -> EntityType.Builder.<ModularMinecart>of(ModularMinecart::new, MobCategory.MISC).sized(0.98F, 0.7F).passengerAttachments(0.1875F).clientTrackingRange(8).build(ResourceKey.create(Registries.ENTITY_TYPE, Identifier.fromNamespaceAndPath(Constants.MOD_ID, "modular_cart"))));

    public static final DeferredHolder<EntityType<?>, EntityType<EntityCake>> CAKE = ENTITIES.register("cake", () -> EntityType.Builder.<EntityCake>of(EntityCake::new, MobCategory.MISC).sized(0.98F, 0.7F).build(ResourceKey.create(Registries.ENTITY_TYPE, Identifier.fromNamespaceAndPath(Constants.MOD_ID, "cake"))));
}
