package vswe.stevescarts.api;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import vswe.stevescarts.Constants;

/** Public data-driven hooks for configuring Steve's Carts behavior. */
public final class StevesCartsTags {
    private StevesCartsTags() {
    }

    public static final class EntityTypes {
        /** Entity types that the cage module cannot pick up. */
        public static final TagKey<EntityType<?>> CAGE_BLACKLIST = TagKey.create(
                Registries.ENTITY_TYPE,
                Identifier.fromNamespaceAndPath(Constants.MOD_ID, "cage_blacklist")
        );

        private EntityTypes() {
        }
    }
}
