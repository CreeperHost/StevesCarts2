package vswe.stevescarts.api.carts;

/** Result of attempting to connect two modular carts. */
public enum CartLinkResult {
    LINKED,
    ALREADY_CONNECTED,
    NO_FREE_LINK,
    TRAIN_FULL,
    INVALID_CART,
    DIFFERENT_LEVEL,
    CANCELLED
}
