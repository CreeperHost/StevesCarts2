package vswe.stevescarts.api.events;

import net.neoforged.bus.api.ICancellableEvent;
import net.neoforged.neoforge.event.entity.EntityEvent;
import vswe.stevescarts.entities.EntityMinecartModular;

public class CartEvents
{
    //Called when a Modular cart is trying to be removed from the world
    public static class CartRemovedEvent extends EntityEvent implements ICancellableEvent
    {
        public CartRemovedEvent(EntityMinecartModular entityMinecartModular)
        {
            super(entityMinecartModular);
        }
    }
}
