package vswe.stevescarts.modules.realtimers;

import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import vswe.stevescarts.entitys.EntityMinecartModular;
import vswe.stevescarts.api.modules.ModuleBase;

public class ModuleRocket extends ModuleBase
{
    private boolean flying;
    private int landDirX;
    private int landDirZ;
    private double flyX;
    private double flyZ;
    private float yaw;
    private boolean isLanding;
    private double landY;
    private double groundY;
    private EntityDataAccessor<Integer> UNKNOWN;

    public ModuleRocket(final EntityMinecartModular cart)
    {
        super(cart);
    }

    @Override
    public void update()
    {
        if (isPlaceholder())
        {
            return;
        }
        //TODO
        //		if (getCart().level.isClientSide) {
        //			if (!flying && getDw(UNKNOWN) != 0) {
        //				takeOff();
        //			} else if (!isLanding && getDw(UNKNOWN) > 1) {
        //				land();
        //			} else if (flying && isLanding && getDw(UNKNOWN) == 0) {
        //				done();
        //			}
        //		}
        //		if (flying) {
        //			getCart().motionX = (isLanding ? (landDirX * 0.05f) : 0.0);
        //			getCart().motionY = (isLanding ? 0.0 : 0.1);
        //			getCart().motionZ = (isLanding ? (landDirZ * 0.05f) : 0.0);
        //			if (!isLanding || landDirX == 0) {
        //				getCart().posX = flyX;
        //			} else {
        //				final EntityMinecartModular cart = getCart();
        //				cart.posX += getCart().motionX;
        //			}
        //			if (!isLanding || landDirZ == 0) {
        //				getCart().posZ = flyZ;
        //			} else {
        //				final EntityMinecartModular cart2 = getCart();
        //				cart2.posZ += getCart().motionZ;
        //			}
        //			getCart().rotationYaw = yaw;
        //			getCart().rotationPitch = 0.0f;
        //			BlockPos pos = getCart().getPosition();
        //			if (isLanding) {
        //				getCart().posY = landY;
        //				if (BlockRailBase.isRailBlock(getCart().world, pos)) {
        //					done();
        //					updateDw(UNKNOWN, 0);
        //				}
        //			}
        //			if (!isLanding && getCart().posY - groundY > 2.0 && BlockRailBase.isRailBlock(getCart().world, pos.add(landDirX, 0, landDirZ))) {
        //				land();
        //				updateDw(UNKNOWN, 2);
        //			}
        //		}
    }

    @Override
    public void activatedByRail(final int x, final int y, final int z, final boolean active)
    {
        if (active)
        {
            takeOff();
            updateDw(UNKNOWN, 1);
        }
    }

    private void takeOff()
    {
        //TODO
        //		flying = true;
        //		getCart().setCanUseRail(false);
        //		flyX = getCart().posX;
        //		flyZ = getCart().posZ;
        //		yaw = getCart().rotationYaw;
        //		groundY = getCart().posY;
        //		if (Math.abs(getCart().motionX) > Math.abs(getCart().motionZ)) {
        //			landDirX = ((getCart().motionX > 0.0) ? 1 : -1);
        //		} else {
        //			landDirZ = ((getCart().motionZ > 0.0) ? 1 : -1);
        //		}
    }

    @Override
    public int numberOfDataWatchers()
    {
        return 1;
    }

    @Override
    public void initDw()
    {
        UNKNOWN = createDw(EntityDataSerializers.INT);
        registerDw(UNKNOWN, 0);
    }

    private void land()
    {
        isLanding = true;
        landY = getCart().y();
        getCart().setCanUseRail(true);
    }

    private void done()
    {
        flying = false;
        isLanding = false;
        landDirX = 0;
        landDirZ = 0;
    }
}
