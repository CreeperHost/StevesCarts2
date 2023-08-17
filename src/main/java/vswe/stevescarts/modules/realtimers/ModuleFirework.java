package vswe.stevescarts.modules.realtimers;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.entity.projectile.FireworkRocketEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import vswe.stevescarts.api.modules.ModuleBase;
import vswe.stevescarts.client.guis.GuiMinecart;
import vswe.stevescarts.containers.slots.SlotBase;
import vswe.stevescarts.containers.slots.SlotFirework;
import vswe.stevescarts.entities.EntityMinecartModular;

import javax.annotation.Nonnull;
import java.util.ArrayList;

public class ModuleFirework extends ModuleBase
{
    private int fireCooldown;

    public ModuleFirework(final EntityMinecartModular cart)
    {
        super(cart);
    }

    @Override
    public void update()
    {
        if (fireCooldown > 0)
        {
            --fireCooldown;
        }
    }

    @Override
    public void activatedByRail(final int x, final int y, final int z, final boolean active)
    {
        if (active && fireCooldown == 0 && getCart().hasFuel())
        {
            fire();
            fireCooldown = 20;
        }
    }

    @Override
    public boolean hasGui()
    {
        return true;
    }

    @Override
    protected SlotBase getSlot(final int slotId, final int x, final int y)
    {
        return new SlotFirework(getCart(), slotId, 8 + x * 18, 16 + y * 18);
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void drawForeground(GuiGraphics guiGraphics, GuiMinecart gui)
    {
        drawString(guiGraphics, gui, getModuleName(), 8, 6, 4210752);
    }

    @Override
    public int guiWidth()
    {
        return 15 + getInventoryWidth() * 18;
    }

    @Override
    public int guiHeight()
    {
        return 20 + getInventoryHeight() * 18;
    }

    @Override
    protected int getInventoryWidth()
    {
        return 8;
    }

    @Override
    protected int getInventoryHeight()
    {
        return 3;
    }

    public void fire()
    {
        if (getCart().level().isClientSide)
        {
            return;
        }
        @Nonnull ItemStack firework = getFirework();
        if (!firework.isEmpty())
        {
            launchFirework(firework);
        }
    }

    @Nonnull
    private ItemStack getFirework()
    {
        boolean hasGunpowder = false;
        boolean hasPaper = false;
        for (int i = 0; i < getInventorySize(); ++i)
        {
            @Nonnull ItemStack item = getStack(i);
            if (!item.isEmpty())
            {
                if (item.getItem() == Items.FIREWORK_ROCKET)
                {
                    @Nonnull ItemStack firework = item.copy();
                    firework.setCount(1);
                    removeItemStack(item, firework.getCount(), i);
                    return firework;
                }
                if (item.getItem() == Items.PAPER)
                {
                    hasPaper = true;
                }
                else if (item.getItem() == Items.GUNPOWDER)
                {
                    hasGunpowder = true;
                }
            }
        }
        if (hasPaper && hasGunpowder)
        {
            @Nonnull ItemStack firework2 = new ItemStack(Items.FIREWORK_ROCKET);
            final int maxGunpowder = getCart().random.nextInt(3) + 1;
            int countGunpowder = 0;
            boolean removedPaper = false;
            for (int j = 0; j < getInventorySize(); ++j)
            {
                @Nonnull ItemStack item2 = getStack(j);
                if (!item2.isEmpty())
                {
                    if (item2.getItem() == Items.PAPER && !removedPaper)
                    {
                        removeItemStack(item2, 1, j);
                        removedPaper = true;
                    }
                    else if (item2.getItem() == Items.GUNPOWDER && countGunpowder < maxGunpowder)
                    {
                        while (item2.getCount() > 0 && countGunpowder < maxGunpowder)
                        {
                            ++countGunpowder;
                            removeItemStack(item2, 1, j);
                        }
                    }
                }
            }
            int chargeCount;
            for (chargeCount = 1; chargeCount < 7 && getCart().random.nextInt(3 + chargeCount / 3) == 0; ++chargeCount)
            {
            }
            final CompoundTag itemstackNBT = new CompoundTag();
            final CompoundTag fireworksNBT = new CompoundTag();
            final ListTag explosionsNBT = new ListTag();
            for (int k = 0; k < chargeCount; ++k)
            {
                @Nonnull ItemStack charge = getCharge();
                if (charge.isEmpty())
                {
                    break;
                }
                if (charge.hasTag() && charge.getTag().contains("Explosion"))
                {
                    explosionsNBT.add(charge.getTag().get("Explosion"));
                }
            }
            fireworksNBT.put("Explosions", explosionsNBT);
            fireworksNBT.putByte("Flight", (byte) countGunpowder);
            itemstackNBT.put("Fireworks", fireworksNBT);
            firework2.setTag(itemstackNBT);
            return firework2;
        }
        return ItemStack.EMPTY;
    }

    @Nonnull
    private ItemStack getCharge()
    {
        for (int i = 0; i < getInventorySize(); ++i)
        {
            @Nonnull ItemStack item = getStack(i);
            if (!item.isEmpty() && item.getItem() == Items.FIREWORK_STAR)
            {
                @Nonnull ItemStack charge = item.copy();
                charge.setCount(1);
                removeItemStack(item, charge.getCount(), i);
                return charge;
            }
        }
        @Nonnull ItemStack charge2 = new ItemStack(Items.FIREWORK_STAR);
        final CompoundTag itemNBT = new CompoundTag();
        final CompoundTag explosionNBT = new CompoundTag();
        byte type = 0;
        boolean removedGunpowder = false;
        final boolean canHasTrail = getCart().random.nextInt(16) == 0;
        final boolean canHasFlicker = getCart().random.nextInt(8) == 0;
        final boolean canHasModifier = getCart().random.nextInt(4) == 0;
        final byte modifierType = (byte) (getCart().random.nextInt(4) + 1);
        boolean removedModifier = false;
        boolean removedDiamond = false;
        boolean removedGlow = false;
        for (int j = 0; j < getInventorySize(); ++j)
        {
            @Nonnull ItemStack item2 = getStack(j);
            if (item2 != null)
            {
                if (item2.getItem() == Items.GUNPOWDER && !removedGunpowder)
                {
                    removeItemStack(item2, 1, j);
                    removedGunpowder = true;
                }
                else if (item2.getItem() == Items.GLOWSTONE_DUST && canHasFlicker && !removedGlow)
                {
                    removeItemStack(item2, 1, j);
                    removedGlow = true;
                    explosionNBT.putBoolean("Flicker", true);
                }
                else if (item2.getItem() == Items.DIAMOND && canHasTrail && !removedDiamond)
                {
                    removeItemStack(item2, 1, j);
                    removedDiamond = true;
                    explosionNBT.putBoolean("Trail", true);
                }
                else if (canHasModifier && !removedModifier && ((item2.getItem() == Items.FIREWORK_STAR && modifierType == 1) || (item2.getItem() == Items.GOLD_NUGGET && modifierType == 2) || (item2.getItem() == Items.SKELETON_SKULL && modifierType == 3) || (item2.getItem() == Items.FEATHER && modifierType == 4)))
                {
                    removeItemStack(item2, 1, j);
                    removedModifier = true;
                    type = modifierType;
                }
            }
        }
        final int[] colors = generateColors((type != 0) ? 7 : 8);
        if (colors == null)
        {
            return null;
        }
        explosionNBT.putIntArray("Colors", colors);
        if (getCart().random.nextInt(4) == 0)
        {
            final int[] fade = generateColors(8);
            if (fade != null)
            {
                explosionNBT.putIntArray("FadeColors", fade);
            }
        }
        explosionNBT.putByte("Type", type);
        itemNBT.put("Explosion", explosionNBT);
        charge2.setTag(itemNBT);
        return charge2;
    }

    private int[] generateColors(final int maxColorCount)
    {
        final int[] maxColors = new int[16];
        final int[] currentColors = new int[16];
        for (int i = 0; i < getInventorySize(); ++i)
        {
            @Nonnull ItemStack item = getStack(i);
            if (!item.isEmpty() && item.getItem() == Items.BLACK_DYE)
            {
                //TODO
                //				final int[] array = maxColors;
                //				final int itemDamage = item.getItemDamage();
                //				array[itemDamage] += item.getCount();
            }
        }
        int colorCount;
        for (colorCount = getCart().random.nextInt(2) + 1; colorCount <= maxColorCount - 2 && getCart().random.nextInt(2) == 0; colorCount += 2)
        {
        }
        final ArrayList<Integer> colorPointers = new ArrayList<>();
        for (int j = 0; j < 16; ++j)
        {
            if (maxColors[j] > 0)
            {
                colorPointers.add(j);
            }
        }
        if (colorPointers.size() == 0)
        {
            return null;
        }
        final ArrayList<Integer> usedColors = new ArrayList<>();
        while (colorCount > 0 && colorPointers.size() > 0)
        {
            final int pointerId = getCart().random.nextInt(colorPointers.size());
            final int colorId = colorPointers.get(pointerId);
            final int[] array2 = currentColors;
            final int n = colorId;
            ++array2[n];
            final int[] array3 = maxColors;
            final int n2 = colorId;
            if (--array3[n2] <= 0)
            {
                colorPointers.remove(pointerId);
            }
            usedColors.add(colorId);
            --colorCount;
        }
        final int[] colors = new int[usedColors.size()];
        for (int k = 0; k < colors.length; ++k)
        {
//            			colors[k] = ItemDye.DYE_COLORS[usedColors.get(k)];
        }
        //		for (int k = 0; k < getInventorySize(); ++k) {
        //			@Nonnull
        //			ItemStack item2 = getStack(k);
        //			if (!item2.isEmpty() && item2.getItem() == Items.DYE && currentColors[item2.getItemDamage()] > 0) {
        //				final int count = Math.min(currentColors[item2.getItemDamage()], item2.getCount());
        //				final int[] array4 = currentColors;
        //				final int itemDamage2 = item2.getItemDamage();
        //				array4[itemDamage2] -= count;
        //			}
        //		}
        return colors;
    }

    private void removeItemStack(@Nonnull ItemStack item, final int count, final int id)
    {
        if (!getCart().hasCreativeSupplies())
        {
            item.shrink(count);
            if (item.getCount() <= 0)
            {
                setStack(id, ItemStack.EMPTY);
            }
        }
    }

    private void launchFirework(@Nonnull ItemStack firework)
    {
        final FireworkRocketEntity rocket = new FireworkRocketEntity(getCart().level(), getCart().blockPosition().getX(), getCart().blockPosition().getY() + 1.0, getCart().blockPosition().getZ(), firework);

        getCart().level().addFreshEntity(rocket);
    }
}
