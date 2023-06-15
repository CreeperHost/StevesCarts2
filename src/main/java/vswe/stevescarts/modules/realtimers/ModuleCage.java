package vswe.stevescarts.modules.realtimers;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ambient.Bat;
import net.minecraft.world.entity.animal.FlyingAnimal;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.animal.WaterAnimal;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.entity.boss.wither.WitherBoss;
import net.minecraft.world.entity.monster.*;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import vswe.stevescarts.client.guis.GuiMinecart;
import vswe.stevescarts.entities.EntityMinecartModular;
import vswe.stevescarts.helpers.Localization;
import vswe.stevescarts.helpers.ResourceHelper;
import vswe.stevescarts.api.modules.interfaces.IActivatorModule;
import vswe.stevescarts.api.modules.ModuleBase;

import java.util.Comparator;
import java.util.List;

public class ModuleCage extends ModuleBase implements IActivatorModule
{
    private int[] autoRect;
    private int[] manualRect;
    private EntityNearestTarget sorter;
    private int cooldown;
    private boolean disablePickup;

    public ModuleCage(final EntityMinecartModular cart)
    {
        super(cart);
        autoRect = new int[]{15, 20, 24, 12};
        manualRect = new int[]{autoRect[0] + autoRect[2] + 5, autoRect[1], autoRect[2], autoRect[3]};
        sorter = new EntityNearestTarget(getCart());
        cooldown = 0;
    }

    @Override
    public boolean hasSlots()
    {
        return false;
    }

    @Override
    public boolean hasGui()
    {
        return true;
    }

    @Override
    public int guiWidth()
    {
        return 80;
    }

    @Override
    public int guiHeight()
    {
        return 35;
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void drawForeground(GuiGraphics guiGraphics, GuiMinecart gui)
    {
        drawString(guiGraphics, gui, getModuleName(), 8, 6, 4210752);
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void drawBackground(GuiGraphics guiGraphics, final GuiMinecart gui, final int x, final int y)
    {
        ResourceHelper.bindResource("/gui/cage.png");
        drawButton(guiGraphics, gui, x, y, autoRect, disablePickup ? 2 : 3);
        drawButton(guiGraphics, gui, x, y, manualRect, isCageEmpty() ? 0 : 1);
    }

    private void drawButton(GuiGraphics guiGraphics, GuiMinecart gui, final int x, final int y, final int[] coords, final int imageID)
    {
        if (inRect(x, y, coords))
        {
            drawImage(guiGraphics, gui, coords, 0, coords[3]);
        }
        else
        {
            drawImage(guiGraphics, gui, coords, 0, 0);
        }
        final int srcY = coords[3] * 2 + imageID * (coords[3] - 2);
        drawImage(guiGraphics, gui, coords[0] + 1, coords[1] + 1, 0, srcY, coords[2] - 2, coords[3] - 2);
    }

    @Override
    public void drawMouseOver(GuiGraphics guiGraphics, GuiMinecart gui, final int x, final int y)
    {
        drawStringOnMouseOver(guiGraphics, gui, Localization.MODULES.ATTACHMENTS.CAGE_AUTO.translate(disablePickup ? "0" : "1"), x, y, autoRect);
        drawStringOnMouseOver(guiGraphics, gui, Localization.MODULES.ATTACHMENTS.CAGE.translate(isCageEmpty() ? "0" : "1"), x, y, manualRect);
    }

    private boolean isCageEmpty()
    {
        return getCart().getCartRider() == null;
    }

    @Override
    public void mouseClicked(final GuiMinecart gui, final int x, final int y, final int button)
    {
        if (button == 0)
        {
            if (inRect(x, y, autoRect))
            {
                sendPacket(0);
            }
            else if (inRect(x, y, manualRect))
            {
                sendPacket(1);
            }
        }
    }

    @Override
    protected void receivePacket(final int id, final byte[] data, final Player player)
    {
        if (id == 0)
        {
            disablePickup = !disablePickup;
        }
        else if (id == 1)
        {
            if (!isCageEmpty())
            {
                manualDrop();
            }
            else
            {
                manualPickUp();
            }
        }
    }

    @Override
    public int numberOfPackets()
    {
        return 2;
    }

    @Override
    public void update()
    {
        super.update();
        if (cooldown > 0)
        {
            --cooldown;
        }
        else if (!disablePickup)
        {
            pickUpCreature(2);
            cooldown = 20;
        }
    }

    private void manualDrop()
    {
        if (!isCageEmpty())
        {
            getCart().ejectPassengers();
            cooldown = 20;
        }
    }

    private void manualPickUp()
    {
        pickUpCreature(5);
    }

    private void pickUpCreature(final int searchDistance)
    {
        if (getCart().level().isClientSide || !isCageEmpty())
        {
            return;
        }
        final List<LivingEntity> entities = getCart().level().getEntitiesOfClass(LivingEntity.class, getCart().getBoundingBox().inflate(searchDistance, 4.0, searchDistance));
        entities.sort(sorter);
        for (LivingEntity target : entities)
        {
            //TODO this is horrid, Maybe datatag??
            if (!(target instanceof Player) && !(target instanceof IronGolem) && !(target instanceof EnderDragon) && !(target instanceof Slime) && !(target instanceof WaterAnimal) && !(target instanceof WitherBoss) && !(target instanceof EnderMan) && (!(target instanceof Spider) || target instanceof CaveSpider) && !(target instanceof Giant) && !(target instanceof FlyingAnimal))
            {
                if (target.getPassengers().isEmpty())
                {
                    target.startRiding(getCart());
                    return;
                }
            }
        }
    }

    @Override
    public float mountedOffset(final Entity rider)
    {
        if (rider instanceof Bat)
        {
            return 0.5f;
        }
        if (rider instanceof Zombie || rider instanceof Skeleton)
        {
            return -0.75f;
        }
        return super.mountedOffset(rider);
    }

    @Override
    public int numberOfGuiData()
    {
        return 1;
    }

    @Override
    protected void checkGuiData(final Object[] info)
    {
        updateGuiData(info, 0, (byte) (disablePickup ? 1 : 0));
    }

    @Override
    public void receiveGuiData(final int id, final short data)
    {
        if (id == 0)
        {
            disablePickup = (data != 0);
        }
    }

    @Override
    protected void Save(final CompoundTag tagCompound, final int id)
    {
        tagCompound.putBoolean(generateNBTName("disablePickup", id), disablePickup);
    }

    @Override
    protected void Load(final CompoundTag tagCompound, final int id)
    {
        disablePickup = tagCompound.getBoolean(generateNBTName("disablePickup", id));
    }

    @Override
    public boolean isActive(final int id)
    {
        if (id == 0)
        {
            return !disablePickup;
        }
        return !isCageEmpty();
    }

    @Override
    public void doActivate(final int id)
    {
        if (id == 0)
        {
            disablePickup = false;
        }
        else
        {
            manualPickUp();
        }
    }

    @Override
    public void doDeActivate(final int id)
    {
        if (id == 0)
        {
            disablePickup = true;
        }
        else
        {
            manualDrop();
        }
    }

    private static class EntityNearestTarget implements Comparator<LivingEntity>
    {
        private Entity entity;

        public EntityNearestTarget(Entity entity)
        {
            this.entity = entity;
        }

        public int compareDistanceSq(Entity entity1, Entity entity2)
        {
            final double distance1 = entity.distanceTo(entity1);
            final double distance2 = entity.distanceTo(entity2);
            return Double.compare(distance1, distance2);
        }

        @Override
        public int compare(LivingEntity o1, LivingEntity o2)
        {
            return compareDistanceSq(o1, o2);
        }
    }
}
