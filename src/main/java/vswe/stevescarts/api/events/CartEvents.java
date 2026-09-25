package vswe.stevescarts.api.events;

import net.neoforged.bus.api.ICancellableEvent;
import net.neoforged.neoforge.event.entity.EntityEvent;
import net.minecraft.world.entity.vehicle.minecart.AbstractMinecart;
import vswe.stevescarts.entities.ModularMinecart;

public final class CartEvents {
    private CartEvents() {
    }

    //Called when a Modular cart is trying to be removed from the world
    public static class CartRemovedEvent extends EntityEvent implements ICancellableEvent {
        public CartRemovedEvent(ModularMinecart modularMinecart) {
            super(modularMinecart);
        }
    }

    /** Fired before two carts are linked. Cancelling prevents the link. */
    public static class CartLinkEvent extends EntityEvent implements ICancellableEvent {
        private final AbstractMinecart otherCart;

        public CartLinkEvent(AbstractMinecart cart, AbstractMinecart otherCart) {
            super(cart);
            this.otherCart = otherCart;
        }

        public AbstractMinecart getCart() {
            return (AbstractMinecart) getEntity();
        }

        public AbstractMinecart getOtherCart() {
            return otherCart;
        }
    }

    /** Fired after a direct link between two carts has been removed. */
    public static class CartUnlinkEvent extends EntityEvent {
        private final AbstractMinecart otherCart;

        public CartUnlinkEvent(AbstractMinecart cart, AbstractMinecart otherCart) {
            super(cart);
            this.otherCart = otherCart;
        }

        public AbstractMinecart getCart() {
            return (AbstractMinecart) getEntity();
        }

        public AbstractMinecart getOtherCart() {
            return otherCart;
        }
    }
}
