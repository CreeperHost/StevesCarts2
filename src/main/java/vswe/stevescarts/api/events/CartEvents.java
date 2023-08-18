package vswe.stevescarts.api.events;

import net.minecraftforge.event.entity.EntityEvent;
import vswe.stevescarts.entities.EntityMinecartModular;

public class CartEvents
{
    //Called when a Modular cart is trying to be removed from the world
    public static class CartRemovedEvent extends EntityEvent
    {
        public CartRemovedEvent(EntityMinecartModular entityMinecartModular)
        {
            super(entityMinecartModular);
        }
    }
}
