package vswe.stevescarts.helpers;

import net.minecraftforge.common.MinecraftForge;

public class MinecartSoundMuter
{
    public MinecartSoundMuter()
    {
        MinecraftForge.EVENT_BUS.register(this);
    }

    //TODO
    //	@SubscribeEvent
    //	public void soundPlay(final PlaySoundEvent event) {
    //		final ISound sound = event.getSound();
    //		if (sound instanceof MovingSoundMinecartRiding) {
    //			final MovingSoundMinecartRiding cartSound = (MovingSoundMinecartRiding) sound;
    //			final EntityMinecart cart = ReflectionHelper.getPrivateValue(MovingSoundMinecartRiding.class, cartSound, 1);
    //			if (cart instanceof EntityMinecartModular) {
    //				final EntityMinecartModular modular = (EntityMinecartModular) cart;
    //				modular.setSound(cartSound, true);
    //			}
    //		} else if (sound instanceof MovingSoundMinecart) {
    //			final MovingSoundMinecart cartSound2 = (MovingSoundMinecart) sound;
    //			final EntityMinecart cart = ReflectionHelper.getPrivateValue(MovingSoundMinecart.class, cartSound2, 0);
    //			if (cart instanceof EntityMinecartModular) {
    //				final EntityMinecartModular modular = (EntityMinecartModular) cart;
    //				modular.setSound(cartSound2, false);
    //			}
    //		}
    //	}
}
