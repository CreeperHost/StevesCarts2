package vswe.stevescarts.api.carts;

import vswe.stevescarts.entities.ModularMinecart;

import java.util.List;
import java.util.UUID;

/**
 * Public access to the modular-cart train graph.
 * <p>
 * Cart object lists are server-side views because UUID-to-entity lookup requires a
 * {@code ServerLevel}. Client code should use the synced entity-id lists instead.
 */
public interface CartTrain {
    int MAX_DIRECT_LINKS = 2;
    int MAX_TRAIN_CARTS = 8;

    CartLinkResult linkCart(ModularMinecart other);

    boolean unlinkCart(ModularMinecart other);

    int unlinkAllCarts();

    boolean isLinkedTo(ModularMinecart other);

    boolean isConnectedTo(ModularMinecart other);

    List<UUID> getLinkedCartIds();

    List<ModularMinecart> getDirectlyLinkedCarts();

    List<ModularMinecart> getConnectedCarts();

    List<ModularMinecart> getTrainCarts();

    int getTrainSize();

    List<Integer> getLinkedCartEntityIds();

    List<Integer> getTrainCartEntityIds();
}
