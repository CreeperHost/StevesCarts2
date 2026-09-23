package vswe.stevescarts.modules.realtimers;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import vswe.stevescarts.SCConfig;
import vswe.stevescarts.api.slots.SlotStevesCarts;
import vswe.stevescarts.containers.slots.SlotCakeDynamite;
import vswe.stevescarts.entities.ModularMinecart;

public class ModuleCakeServerDynamite extends ModuleCakeServer {
    private int dynamiteCount;

    public ModuleCakeServerDynamite(ModularMinecart cart) {
        super(cart);
    }

    private int getMaxDynamiteCount() {
        return Math.min(SCConfig.COMMON.maxDynamites.get(), 25);
    }

    @Override
    protected SlotStevesCarts getSlot(final int slotId, final int x, final int y) {
        return new SlotCakeDynamite(getCart(), slotId, 8 + x * 18, 38 + y * 18);
    }

    @Override
    public boolean dropOnDeath() {
        return dynamiteCount == 0;
    }

    @Override
    public void onDeath() {
        if (dynamiteCount > 0) {
            explode();
        }
    }

    private void explode() {
        getCart().level().explode(null, getCart().blockPosition().getX(), getCart().blockPosition().getY(), getCart().blockPosition().getZ(), dynamiteCount * .08f, Level.ExplosionInteraction.NONE);
    }

    @Override
    public void update() {
        super.update();
        if (!getCart().level().isClientSide()) {
            //TODO Restore dynamite loading.
        }
    }

    @Override
    public boolean onInteractFirst(final Player entityplayer) {
        if (dynamiteCount > 0) {
            explode();
            getCart().remove(Entity.RemovalReason.KILLED);
            return true;
        }
        return super.onInteractFirst(entityplayer);
    }
}
