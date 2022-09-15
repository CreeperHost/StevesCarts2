package vswe.stevescarts.modules.realtimers;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import vswe.stevescarts.client.guis.GuiMinecart;
import vswe.stevescarts.entities.EntityMinecartModular;
import vswe.stevescarts.helpers.Localization;
import vswe.stevescarts.helpers.ResourceHelper;
import vswe.stevescarts.api.modules.ModuleBase;
import vswe.stevescarts.modules.addons.mobdetectors.ModuleMobdetector;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class ModuleShooterAdv extends ModuleShooter
{
    private ArrayList<ModuleMobdetector> detectors;
    private EntityNearestTarget sorter;
    private float detectorAngle;
    private EntityDataAccessor<Byte> OPTION;
    private EntityDataAccessor<Byte> RIFLE_DIRECTION;

    public ModuleShooterAdv(final EntityMinecartModular cart)
    {
        super(cart);
        sorter = new EntityNearestTarget(getCart());
    }

    @Override
    public void preInit()
    {
        super.preInit();
        detectors = new ArrayList<>();
        for (final ModuleBase module : getCart().getModules())
        {
            if (module instanceof ModuleMobdetector)
            {
                detectors.add((ModuleMobdetector) module);
            }
        }
    }

    @Override
    protected void generatePipes(final ArrayList<Integer> list)
    {
        list.add(1);
    }

    @Override
    protected int guiExtraWidth()
    {
        return 100;
    }

    @Override
    protected int guiRequiredHeight()
    {
        return 10 + 10 * detectors.size();
    }

    private int[] getSelectionBox(final int id)
    {
        return new int[]{90, id * 10 + (guiHeight() - 10 * detectors.size()) / 2, 8, 8};
    }

    @Override
    protected void generateInterfaceRegions()
    {
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void drawForeground(PoseStack matrixStack, GuiMinecart gui)
    {
        drawString(matrixStack, gui, Localization.MODULES.ATTACHMENTS.SHOOTER.translate(), 8, 6, 4210752);
        for (int i = 0; i < detectors.size(); ++i)
        {
            final int[] box = getSelectionBox(i);
            drawString(matrixStack, gui, detectors.get(i).getName(), box[0] + 12, box[1], 4210752);
        }
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void drawBackground(PoseStack matrixStack, GuiMinecart gui, final int x, final int y)
    {
        ResourceHelper.bindResource("/gui/mobdetector.png");
        for (int i = 0; i < detectors.size(); ++i)
        {
            final int srcX = isOptionActive(i) ? 0 : 8;
            final int srcY = inRect(x, y, getSelectionBox(i)) ? 8 : 0;
            drawImage(matrixStack, gui, getSelectionBox(i), srcX, srcY);
        }
    }

    @Override
    public void mouseClicked(final GuiMinecart gui, final int x, final int y, final int button)
    {
        if (button == 0)
        {
            for (int i = 0; i < detectors.size(); ++i)
            {
                if (inRect(x, y, getSelectionBox(i)))
                {
                    sendPacket(0, (byte) i);
                    break;
                }
            }
        }
    }

    @Override
    public void mouseMovedOrUp(final GuiMinecart gui, final int x, final int y, final int button)
    {
    }

    @Override
    public int numberOfGuiData()
    {
        return 0;
    }

    @Override
    protected void checkGuiData(final Object[] info)
    {
    }

    @Override
    protected void Shoot()
    {
        setTimeToNext(15);
        if (!getCart().hasFuel())
        {
            return;
        }
        final Entity target = getTarget();
        if (target != null)
        {
            if (hasProjectileItem())
            {
                shootAtTarget(target);
            }
            else
            {
                getCart().level.levelEvent(1001, getCart().blockPosition(), 0);
            }
        }
    }

    private void shootAtTarget(final Entity target)
    {
        final Entity projectile = getProjectile(target, getProjectileItem(true));
        double posY = getCart().getY() + (double) getCart().getEyeHeight() - 0.10000000149011612D;

        double disX = target.blockPosition().getY() - getCart().getX();
        double disY = target.blockPosition().getX() + (double) target.getEyeHeight() - 0.699999988079071D - posY;
        double disZ = target.blockPosition().getZ() - getCart().getZ();

        final double dis = Math.sqrt(disX * disX + disZ * disZ);

        if (dis >= 1.0E-7)
        {
            final float theta = (float) (Math.atan2(disZ, disX) * 180.0D / Math.PI) - 90.0f;
            final float phi = (float) (-(Math.atan2(disY, dis) * 180.0D / Math.PI));

            setRifleDirection((float) Math.atan2(disZ, disX));

            final double disPX = disX / dis;
            final double disPZ = disZ / dis;

            projectile.setPos(getCart().getX() + disPX * 1.5, projectile.getY(), getCart().getZ() + disPZ * 1.5);

            //projectile.setRenderYawOffset(0.0f);
            float disD5 = (float) dis * 0.2f;
            setHeading(projectile, disX, disY + (double) disD5, disZ, 1.6f, 0.0f);
        }
        BlockPos pos = getCart().blockPosition();
        getCart().level.playSound(null, pos.getX(), pos.getY(), pos.getZ(), SoundEvents.ARROW_HIT, SoundSource.NEUTRAL, 1.0f, 1.0f / (getCart().random.nextFloat() * 0.4f + 0.8f));

        setProjectileDamage(projectile);
        setProjectileOnFire(projectile);
        setProjectileKnockback(projectile);

        getCart().level.addFreshEntity(projectile);

        damageEnchant();
    }

    protected int getTargetDistance()
    {
        return 16;
    }

    private Entity getTarget()
    {
        final List<Entity> entities = getCart().level.getEntitiesOfClass(Entity.class, getCart().getBoundingBox().inflate(getTargetDistance(), 4.0, getTargetDistance()));
        entities.sort(sorter);

        for (final Entity target : entities)
        {
            if (target != getCart() && canSee(target))
            {
                for (int i = 0; i < detectors.size(); ++i)
                {
                    if (isOptionActive(i))
                    {
                        final ModuleMobdetector detector = detectors.get(i);
                        if (detector.isValidTarget(target))
                        {
                            return target;
                        }
                    }
                }
            }
        }
        return null;
    }

    private boolean canSee(final Entity target)
    {
        return false;
        //TODO
        //		return target != null && getCart().level.raryTraceBlocks(new Vec3d(getCart().posX, getCart().posY + getCart().getEyeHeight(), getCart().posZ),
        //				new Vec3d(target.posX, target.posY + target.getEyeHeight(), target.posZ)) == null;
    }

    @Override
    protected void receivePacket(final int id, final byte[] data, final Player player)
    {
        if (id == 0)
        {
            switchOption(data[0]);
        }
    }

    @Override
    public int numberOfPackets()
    {
        return 1;
    }

    @Override
    public int numberOfDataWatchers()
    {
        return 2;
    }

    @Override
    public void initDw()
    {
        OPTION = createDw(EntityDataSerializers.BYTE);
        RIFLE_DIRECTION = createDw(EntityDataSerializers.BYTE);
        registerDw(OPTION, (byte) 0);
        registerDw(RIFLE_DIRECTION, (byte) 0);
    }

    private void switchOption(final int id)
    {
        byte val = getDw(OPTION);
        val ^= (byte) (1 << id);
        updateDw(OPTION, val);
    }

    public void setOptions(final byte val)
    {
        updateDw(OPTION, val);
    }

    public byte selectedOptions()
    {
        return getDw(OPTION);
    }

    private boolean isOptionActive(final int id)
    {
        return (selectedOptions() & 1 << id) != 0x0;
    }

    @Override
    protected boolean isPipeActive(final int id)
    {
        if (isPlaceholder())
        {
            return getSimInfo().getIsPipeActive();
        }
        return selectedOptions() != 0;
    }

    public float getDetectorAngle()
    {
        return detectorAngle;
    }

    @Override
    public void update()
    {
        super.update();
        if (isPipeActive(0))
        {
            detectorAngle = (float) ((detectorAngle + 0.1f) % (Math.PI * 2));
        }
    }

    private void setRifleDirection(float val)
    {
        val /= 2 * (float) Math.PI;
        val *= 256.0f;
        val %= 256.0f;
        if (val < 0)
        {
            val += 256.0f;
        }
        updateDw(RIFLE_DIRECTION, (byte) val);
    }

    public float getRifleDirection()
    {
        float val;
        if (isPlaceholder())
        {
            val = 0.0f;
        }
        else
        {
            val = getDw(RIFLE_DIRECTION);
        }
        val /= 256.0f;
        val *= (float) Math.PI * 2;
        return val;
    }

    @Override
    protected void Save(final CompoundTag tagCompound, final int id)
    {
        tagCompound.putByte(generateNBTName("Options", id), selectedOptions());
        saveTick(tagCompound, id);
    }

    @Override
    protected void Load(final CompoundTag tagCompound, final int id)
    {
        setOptions(tagCompound.getByte(generateNBTName("Options", id)));
        loadTick(tagCompound, id);
    }

    private static class EntityNearestTarget implements Comparator<Entity>
    {
        private Entity entity;

        public EntityNearestTarget(final Entity entity)
        {
            this.entity = entity;
        }

        public int compareDistanceSq(final Entity entity1, final Entity entity2)
        {
            final double distance1 = entity.distanceTo(entity1);
            final double distance2 = entity.distanceTo(entity2);
            return (distance1 < distance2) ? -1 : ((distance1 > distance2) ? 1 : 0);
        }

        @Override
        public int compare(Entity o1, Entity o2)
        {
            return compareDistanceSq(o1, o2);
        }
    }
}
