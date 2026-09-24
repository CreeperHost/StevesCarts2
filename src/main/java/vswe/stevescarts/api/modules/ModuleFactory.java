package vswe.stevescarts.api.modules;

import vswe.stevescarts.entities.ModularMinecart;

/**
 * Creates a module instance for a cart.
 *
 * @param <T> module implementation type
 */
@FunctionalInterface
public interface ModuleFactory<T extends ModuleBase> {
    T create(ModularMinecart cart);
}
