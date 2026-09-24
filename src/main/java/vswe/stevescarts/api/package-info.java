/**
 * Supported extension points for Steve's Carts add-ons.
 * <p>
 * Register modules with {@link vswe.stevescarts.api.StevesCartsAPI#registerModule(
 * vswe.stevescarts.api.modules.data.ModuleData, java.util.function.Supplier)}. New modules should
 * use the factory-based {@link vswe.stevescarts.api.modules.data.ModuleData} constructor instead of
 * relying on reflection. Module items can use
 * {@link vswe.stevescarts.items.ItemCartModule} or implement
 * {@link vswe.stevescarts.api.IModuleItem}.
 * <p>
 * Connected-cart operations are exposed through
 * {@link vswe.stevescarts.api.carts.CartTrain}. Graph methods returning cart objects are server-side;
 * client code should use its synchronized entity-ID methods. Link and unlink notifications are in
 * {@link vswe.stevescarts.api.events.CartEvents}.
 */
package vswe.stevescarts.api;
