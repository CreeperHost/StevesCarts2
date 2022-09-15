package vswe.stevescarts.modules.realtimers;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Arrow;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ArrowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import vswe.stevescarts.client.guis.GuiMinecart;
import vswe.stevescarts.containers.slots.SlotArrow;
import vswe.stevescarts.containers.slots.SlotBase;
import vswe.stevescarts.entities.EntityMinecartModular;
import vswe.stevescarts.helpers.EnchantmentInfo;
import vswe.stevescarts.helpers.Localization;
import vswe.stevescarts.helpers.ResourceHelper;
import vswe.stevescarts.api.modules.interfaces.ISuppliesModule;
import vswe.stevescarts.api.modules.ModuleBase;
import vswe.stevescarts.modules.addons.ModuleEnchants;
import vswe.stevescarts.modules.addons.projectiles.ModuleProjectile;

import javax.annotation.Nonnull;
import java.util.ArrayList;

public class ModuleShooter extends ModuleBase implements ISuppliesModule
{
    private ArrayList<ModuleProjectile> projectiles;
    private ModuleEnchants enchanter;
    private int pipeSelectionX;
    private int pipeSelectionY;
    private int intervalSelectionX;
    private int intervalSelectionY;
    private int[] intervalSelection;
    private int[] intervalDragArea;
    private int currentCooldownState;
    private int dragState;
    private final ArrayList<Integer> pipes;
    private final float[] pipeRotations;
    private final int[] AInterval;
    private int arrowTick;
    private int arrowInterval;
    private EntityDataAccessor<Byte> ACTIVE_PIPE;

    public ModuleShooter(final EntityMinecartModular cart)
    {
        super(cart);
        dragState = -1;
        AInterval = new int[]{1, 3, 5, 7, 10, 13, 17, 21, 27, 35, 44, 55, 70, 95, 130, 175, 220, 275, 340, 420, 520, 650};
        arrowInterval = 5;
        generatePipes(pipes = new ArrayList<>());
        pipeRotations = new float[pipes.size()];
        generateInterfaceRegions();
    }

    @Override
    public void init()
    {
        super.init();
        projectiles = new ArrayList<>();
        for (final ModuleBase module : getCart().getModules())
        {
            if (module instanceof ModuleProjectile)
            {
                projectiles.add((ModuleProjectile) module);
            }
            else if (module instanceof ModuleEnchants)
            {
                enchanter = (ModuleEnchants) module;
                enchanter.addType(EnchantmentInfo.ENCHANTMENT_TYPE.SHOOTER);
            }
        }
    }

    @Override
    protected int getInventoryHeight()
    {
        return 2;
    }

    @Override
    protected SlotBase getSlot(final int slotId, final int x, final int y)
    {
        return new SlotArrow(getCart(), this, slotId, 8 + x * 18, 23 + y * 18);
    }

    @Override
    public boolean hasGui()
    {
        return true;
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void drawForeground(PoseStack matrixStack, GuiMinecart gui)
    {
        drawString(matrixStack, gui, Localization.MODULES.ATTACHMENTS.SHOOTER.translate(), 8, 6, 0x404040);
        final int delay = AInterval[arrowInterval];
        final double freq = 20.0 / (delay + 1);
        String s = String.valueOf((int) (freq * 1000.0) / 1000.0);
        drawString(matrixStack, gui, Localization.MODULES.ATTACHMENTS.FREQUENCY.translate() + ":", intervalDragArea[0] + intervalDragArea[2] + 5, 15, 0x404040);
        drawString(matrixStack, gui, s, intervalDragArea[0] + intervalDragArea[2] + 5, 23, 0x404040);
        s = String.valueOf(delay / 20.0 + Localization.MODULES.ATTACHMENTS.SECONDS.translate(new String[0]));
        drawString(matrixStack, gui, Localization.MODULES.ATTACHMENTS.DELAY.translate() + ":", intervalDragArea[0] + intervalDragArea[2] + 5, 35, 0x404040);
        drawString(matrixStack, gui, s, intervalDragArea[0] + intervalDragArea[2] + 5, 43, 0x404040);
    }

    @Override
    public int guiWidth()
    {
        return super.guiWidth() + guiExtraWidth();
    }

    protected int guiExtraWidth()
    {
        return 112;
    }

    @Override
    public int guiHeight()
    {
        return Math.max(super.guiHeight(), guiRequiredHeight());
    }

    protected int guiRequiredHeight()
    {
        return 67;
    }

    protected void generateInterfaceRegions()
    {
        pipeSelectionX = guiWidth() - 110;
        pipeSelectionY = (guiHeight() - 12 - 26) / 2 + 12;
        intervalSelectionX = pipeSelectionX + 26 + 8;
        intervalSelectionY = 10;
        intervalSelection = new int[]{intervalSelectionX, intervalSelectionY, 14, 53};
        intervalDragArea = new int[]{intervalSelectionX - 4, intervalSelectionY, 40, 53};
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void drawBackground(PoseStack matrixStack, GuiMinecart gui, final int x, final int y)
    {
        ResourceHelper.bindResource("/gui/shooter.png");
        drawImage(matrixStack, gui, pipeSelectionX + 9, pipeSelectionY + 9 - 1, 0, 104, 8, 9);
        for (int i = 0; i < pipes.size(); ++i)
        {
            final int pipe = pipes.get(i);
            final int pipeX = pipe % 3;
            final int pipeY = pipe / 3;
            final boolean active = isPipeActive(i);
            final boolean selected = inRect(x, y, getRectForPipe(pipe)) || (currentCooldownState == 0 && active);
            int srcX = pipeX * 9;
            if (!active)
            {
                srcX += 26;
            }
            int srcY = pipeY * 9;
            if (selected)
            {
                srcY += 26;
            }
            drawImage(matrixStack, gui, getRectForPipe(pipe), srcX, srcY);
        }
        drawImage(matrixStack, gui, intervalSelection, 42, 52);
        final int size = (int) (arrowInterval / AInterval.length * 4.0f);
        int targetX = intervalSelectionX + 7;
        final int targetY = intervalSelectionY + arrowInterval * 2;
        int srcX2 = 0;
        final int srcY2 = 52 + size * 13;
        drawImage(matrixStack, gui, targetX, targetY, srcX2, srcY2, 25, 13);
        srcX2 += 25;
        targetX += 7;
        drawImage(matrixStack, gui, targetX, targetY + 1, srcX2, srcY2 + 1, 1, 11);
        drawImage(matrixStack, gui, targetX + 1, targetY + 2, srcX2 + 1, srcY2 + 2, 1, 9);
        drawImage(matrixStack, gui, targetX + 1, targetY + 1, srcX2 + 1, srcY2 + 1, Math.min(currentCooldownState, 15), 2);
        drawImage(matrixStack, gui, targetX + 15, targetY + 1, srcX2 + 15, srcY2 + 1, 2, Math.max(Math.min(currentCooldownState, 25) - 15, 0));
        final int len = Math.max(Math.min(currentCooldownState, 41) - 25, 0);
        drawImage(matrixStack, gui, targetX + 1 + (16 - len), targetY + 10, srcX2 + 1 + (16 - len), srcY2 + 10, len, 2);
    }

    private int getCurrentCooldownState()
    {
        final double perc = arrowTick / AInterval[arrowInterval];
        return currentCooldownState = (int) (41.0 * perc);
    }

    private int[] getRectForPipe(final int pipe)
    {
        return new int[]{pipeSelectionX + pipe % 3 * 9, pipeSelectionY + pipe / 3 * 9, 8, 8};
    }

    @Override
    public void mouseClicked(final GuiMinecart gui, final int x, final int y, final int button)
    {
        if (button == 0)
        {
            if (inRect(x, y, intervalDragArea))
            {
                dragState = y - (intervalSelectionY + arrowInterval * 2);
            }
            else
            {
                for (int i = 0; i < pipes.size(); ++i)
                {
                    if (inRect(x, y, getRectForPipe(pipes.get(i))))
                    {
                        sendPacket(0, (byte) i);
                        break;
                    }
                }
            }
        }
    }

    @Override
    public void mouseMovedOrUp(final GuiMinecart gui, final int x, final int y, final int button)
    {
        if (button != -1)
        {
            dragState = -1;
        }
        else if (dragState != -1)
        {
            final int interval = (y + getCart().getRealScrollY() - intervalSelectionY - dragState) / 2;
            if (interval != arrowInterval && interval >= 0 && interval < AInterval.length)
            {
                sendPacket(1, (byte) interval);
            }
        }
    }

    @Override
    protected void receivePacket(final int id, final byte[] data, final Player player)
    {
        //		if (id == 0) {
        byte info = getActivePipes();
        info ^= (byte) (1 << data[0]);
        setActivePipes(info);
        //		} else if (id == 1) {
        //			byte info = data[0];
        //			if (info < 0) {
        //				info = 0;
        //			} else if (info >= AInterval.length) {
        //				info = (byte) (AInterval.length - 1);
        //			}
        //			arrowInterval = info;
        //			arrowTick = AInterval[info];
        //		}
    }

    @Override
    public int numberOfPackets()
    {
        return 2;
    }

    @Override
    public int numberOfGuiData()
    {
        return 2;
    }

    @Override
    protected void checkGuiData(final Object[] info)
    {
        updateGuiData(info, 0, (short) currentCooldownState);
        updateGuiData(info, 1, (short) arrowInterval);
    }

    @Override
    public void receiveGuiData(final int id, final short data)
    {
        if (id == 0)
        {
            currentCooldownState = data;
        }
        else if (id == 1)
        {
            arrowInterval = data;
        }
    }

    @Override
    public void update()
    {
        if (!getCart().level.isClientSide)
        {
            if (arrowTick > 0)
            {
                --arrowTick;
            }
            else
            {
                Shoot();
            }
        }
        else
        {
            rotatePipes(false);
        }
    }

    //pipes that this module have
    //0 (Forward Left)		, 1 (Forward)		, 2 (Forward Right)
    //3 (Left),				, 4 (Invalid)		, 5 (Right)
    //6 (Back Left)			, 7 (Back)			, 8 (Back Right)
    protected void generatePipes(final ArrayList<Integer> list)
    {
        for (int i = 0; i < 9; ++i)
        {
            if (i != 4)
            {
                list.add(i);
            }
        }
    }

    protected boolean hasProjectileItem()
    {
        return !getProjectileItem(false).isEmpty();
    }

    @Nonnull
    protected ItemStack getProjectileItem(boolean flag)
    {
        if (flag && enchanter != null && enchanter.useInfinity())
        {
            flag = false;
        }
        for (int i = 0; i < getInventorySize(); ++i)
        {
            if (!getStack(i).isEmpty() && isValidProjectileItem(getStack(i)))
            {
                @Nonnull ItemStack projectile = getStack(i).copy();
                projectile.setCount(1);
                if (flag && !getCart().hasCreativeSupplies())
                {
                    @Nonnull ItemStack stack = getStack(i);
                    stack.shrink(1);
                    if (getStack(i).getCount() == 0)
                    {
                        setStack(i, ItemStack.EMPTY);
                    }
                }
                return projectile;
            }
        }
        return ItemStack.EMPTY;
    }

    //TODO
    protected void Shoot()
    {
        setTimeToNext(AInterval[arrowInterval]);
        if (!getCart().hasFuel())
        {
            return;
        }
        boolean hasShot = false;
        for (int i = 0; i < pipes.size(); ++i)
        {
            if (isPipeActive(i))
            {
                final int pipe = pipes.get(i);
                if (!hasProjectileItem())
                {
                    break;
                }
                int x = pipe % 3 - 1;
                int y = pipe / 3 - 1;
                if (getCart().getDeltaMovement().x > 0.0)
                {
                    y *= -1;
                    x *= -1;
                }
                else if (getCart().getDeltaMovement().z < 0)
                {
                }
                else if (getCart().x() < 0)
                {
                    final int temp = -x;
                    x = y;
                    y = temp;
                }
                else if (getCart().getDeltaMovement().x > 0.0)
                {
                    final int temp = x;
                    x = -y;
                    y = temp;
                }
                final Entity projectile = new Arrow(getCart().level, getCart().blockPosition().getX() + x * 1.5, getCart().blockPosition().getY() + 0.75F, getCart().blockPosition().getZ() + y * 1.5);//getProjectile(null, getProjectileItem(true));
                //				projectile.setPos(getCart().blockPosition().getX() + x * 1.5, getCart().blockPosition().getY() + 0.75F, getCart().blockPosition().getZ() + y * 1.5);
                setHeading(projectile, x, 0.10000000149011612D, y, 1.6f, 12.0f);
                setProjectileDamage(projectile);
                setProjectileOnFire(projectile);
                setProjectileKnockback(projectile);
                getCart().level.addFreshEntity(projectile);
                hasShot = true;
                damageEnchant();
            }
        }
        if (hasShot)
        {
            getCart().level.levelEvent(1002, getCart().blockPosition(), 0);
        }
    }

    protected void damageEnchant()
    {
        if (enchanter != null)
        {
            enchanter.damageEnchant(EnchantmentInfo.ENCHANTMENT_TYPE.SHOOTER, 1);
        }
    }

    protected void setProjectileOnFire(final Entity projectile)
    {
        if (enchanter != null && enchanter.useFlame())
        {
            projectile.setSecondsOnFire(100);
        }
    }

    protected void setProjectileDamage(final Entity projectile)
    {
        if (enchanter != null && projectile instanceof Arrow)
        {
            final int power = enchanter.getPowerLevel();
            if (power > 0)
            {
                final Arrow arrow = (Arrow) projectile;
                arrow.setBaseDamage(arrow.getBaseDamage() + power * 0.5 + 0.5);
            }
        }
    }

    protected void setProjectileKnockback(final Entity projectile)
    {
        if (enchanter != null && projectile instanceof Arrow)
        {
            final int punch = enchanter.getPunchLevel();
            if (punch > 0)
            {
                final Arrow arrow = (Arrow) projectile;
                arrow.setKnockback(punch);
            }
        }
    }

    protected void setHeading(final Entity projectile, final double motionX, final double motionY, final double motionZ, final float motionMult, final float motionNoise)
    {
        if (projectile instanceof Projectile)
        {
            ((Projectile) projectile).shoot(motionX, motionY, motionZ, motionMult, motionNoise);
        }
    }

    protected Entity getProjectile(final Entity target, @Nonnull ItemStack item)
    {
        for (final ModuleProjectile module : projectiles)
        {
            if (module.isValidProjectile(item))
            {
                return module.createProjectile(target, item);
            }
        }
        return null;
    }

    public boolean isValidProjectileItem(@Nonnull ItemStack item)
    {
        for (final ModuleProjectile module : projectiles)
        {
            if (module.isValidProjectile(item))
            {
                return true;
            }
        }
        return item.getItem() instanceof ArrowItem;
    }

    protected void setTimeToNext(final int val)
    {
        arrowTick = val;
    }

    private void rotatePipes(boolean isNew)
    {
        final float minRotation = 0.0f;
        final float maxRotation = (float) Math.PI / 4;
        final float speed = 0.15f;

        for (int i = 0; i < pipes.size(); ++i)
        {
            boolean isActive = isPipeActive(i);
            if (isNew && isActive)
            {
                pipeRotations[i] = minRotation;
            }
            else if (isNew && !isActive)
            {
                pipeRotations[i] = maxRotation;
            }
            else if (isActive && pipeRotations[i] > minRotation)
            {
                pipeRotations[i] -= speed;
                if (this.pipeRotations[i] < minRotation)
                {
                    this.pipeRotations[i] = minRotation;
                }
            }
            else if (!isActive && pipeRotations[i] < maxRotation)
            {
                pipeRotations[i] += speed;
                if (pipeRotations[i] > maxRotation)
                {
                    pipeRotations[i] = maxRotation;
                }
            }
        }
    }

    @Override
    public int numberOfDataWatchers()
    {
        return 1;
    }

    @Override
    public void initDw()
    {
        ACTIVE_PIPE = createDw(EntityDataSerializers.BYTE);
        registerDw(ACTIVE_PIPE, (byte) 0);
    }

    public void setActivePipes(final byte val)
    {
        updateDw(ACTIVE_PIPE, val);
    }

    public byte getActivePipes()
    {
        if (isPlaceholder())
        {
            return getSimInfo().getActivePipes();
        }
        return getDw(ACTIVE_PIPE);
    }

    protected boolean isPipeActive(final int id)
    {
        return (getActivePipes() & 1 << id) != 0x0;
    }

    public int getPipeCount()
    {
        return pipes.size();
    }

    public float getPipeRotation(final int id)
    {
        return pipeRotations[id];
    }

    @Override
    protected void Save(final CompoundTag tagCompound, final int id)
    {
        tagCompound.putByte(generateNBTName("Pipes", id), getActivePipes());
        tagCompound.putByte(generateNBTName("Interval", id), (byte) arrowInterval);
        saveTick(tagCompound, id);
    }

    @Override
    protected void Load(final CompoundTag tagCompound, final int id)
    {
        setActivePipes(tagCompound.getByte(generateNBTName("Pipes", id)));
        arrowInterval = tagCompound.getByte(generateNBTName("Interval", id));
        loadTick(tagCompound, id);
    }

    protected void saveTick(final CompoundTag tagCompound, final int id)
    {
        tagCompound.putByte(generateNBTName("Tick", id), (byte) arrowTick);
    }

    protected void loadTick(final CompoundTag tagCompound, final int id)
    {
        arrowTick = tagCompound.getByte(generateNBTName("Tick", id));
    }

    @Override
    public boolean haveSupplies()
    {
        return hasProjectileItem();
    }
}
