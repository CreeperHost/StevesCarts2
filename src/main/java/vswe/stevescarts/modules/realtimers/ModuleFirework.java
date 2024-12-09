package vswe.stevescarts.modules.realtimers;

import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.projectile.FireworkRocketEntity;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.FireworkExplosion;
import net.minecraft.world.item.component.FireworkExplosion.Shape;
import net.minecraft.world.item.component.Fireworks;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.common.Tags;
import vswe.stevescarts.api.modules.ModuleBase;
import vswe.stevescarts.api.slots.SlotStevesCarts;
import vswe.stevescarts.client.guis.GuiMinecart;
import vswe.stevescarts.containers.slots.SlotFirework;
import vswe.stevescarts.entities.ModularMinecart;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public class ModuleFirework extends ModuleBase {
    private int fireCooldown;

    public ModuleFirework(ModularMinecart cart) {
        super(cart);
    }

    @Override
    public void update() {
        if (fireCooldown > 0) {
            --fireCooldown;
        }
    }

    @Override
    public void activatedByRail(final int x, final int y, final int z, final boolean active) {
        if (active && fireCooldown == 0 && getCart().hasFuel()) {
            fire();
            fireCooldown = 20;
        }
    }

    @Override
    public boolean hasGui() {
        return true;
    }

    @Override
    protected SlotStevesCarts getSlot(final int slotId, final int x, final int y) {
        return new SlotFirework(getCart(), slotId, 8 + x * 18, 16 + y * 18);
    }

    @OnlyIn (Dist.CLIENT)
    @Override
    public void drawForeground(GuiGraphics guiGraphics, GuiMinecart gui) {
        drawString(guiGraphics, gui, getModuleName(), 8, 6, 4210752);
    }

    @Override
    public int guiWidth() {
        return 15 + getInventoryWidth() * 18;
    }

    @Override
    public int guiHeight() {
        return 20 + getInventoryHeight() * 18;
    }

    @Override
    protected int getInventoryWidth() {
        return 8;
    }

    @Override
    protected int getInventoryHeight() {
        return 3;
    }

    public void fire() {
        if (getCart().level().isClientSide) {
            return;
        }
        @Nonnull ItemStack firework = getFirework();
        if (!firework.isEmpty()) {
            launchFirework(firework);
        }
    }

    @Nonnull
    private ItemStack getFirework() {
        boolean hasGunpowder = false;
        boolean hasPaper = false;

        boolean canMakeCharge = false;
        for (int i = 0; i < getInventorySize(); ++i) {
            ItemStack stack = getStack(i);
            if (stack.isEmpty()) continue;
            if (stack.is(Tags.Items.DYES) || stack.is(Items.FIREWORK_STAR)){
                canMakeCharge = true;
                break;
            }
        }
        if (!canMakeCharge) return ItemStack.EMPTY;

        for (int i = 0; i < getInventorySize(); ++i) {
            ItemStack item = getStack(i);
            if (!item.isEmpty()) {
                if (item.getItem() == Items.FIREWORK_ROCKET) {
                    ItemStack firework = item.copy();
                    firework.setCount(1);
                    removeItemStack(item, firework.getCount(), i);
                    return firework;
                }
                if (item.getItem() == Items.PAPER) {
                    hasPaper = true;
                } else if (item.getItem() == Items.GUNPOWDER) {
                    hasGunpowder = true;
                }
            }
        }
        if (hasPaper && hasGunpowder) {
            int maxGunpowder = getCart().getRandom().nextInt(3) + 1;
            int countGunpowder = 0;
            boolean removedPaper = false;
            for (int j = 0; j < getInventorySize(); ++j) {
                ItemStack item2 = getStack(j);
                if (!item2.isEmpty()) {
                    if (item2.getItem() == Items.PAPER && !removedPaper) {
                        removeItemStack(item2, 1, j);
                        removedPaper = true;
                    } else if (item2.getItem() == Items.GUNPOWDER && countGunpowder < maxGunpowder) {
                        while (item2.getCount() > 0 && countGunpowder < maxGunpowder) {
                            ++countGunpowder;
                            removeItemStack(item2, 1, j);
                        }
                    }
                }
            }
            int chargeCount;
            for (chargeCount = 1; chargeCount < 7 && getCart().getRandom().nextInt(3 + chargeCount / 3) == 0; ++chargeCount);
            List<FireworkExplosion> explosions = new ArrayList<>();
            for (int k = 0; k < chargeCount; ++k) {
                ItemStack charge = getCharge();
                if (charge.isEmpty()) {
                    break;
                }
                if (charge.has(DataComponents.FIREWORK_EXPLOSION)) {
                    explosions.add(charge.get(DataComponents.FIREWORK_EXPLOSION));
                }
            }
            ItemStack firework = new ItemStack(Items.FIREWORK_ROCKET);
            firework.set(DataComponents.FIREWORKS, new Fireworks(countGunpowder, explosions));
            return firework;
        }
        return ItemStack.EMPTY;
    }

    /*
    Shape
    - Default:      Small Ball
    - Fire Charge:  Large Ball
    - Gold Nugget:  Star
    - Head:         Creeper
    - Feather:      Burst

    Effects
    - Glowstone Dust:   Twinkle
    - Diamond:          Trail
    */

    private ItemStack getCharge() {
        List<Integer> starSlots = new ArrayList<>();

        for (int i = 0; i < getInventorySize(); ++i) {
            ItemStack item = getStack(i);
            if (!item.isEmpty() && item.getItem() == Items.FIREWORK_STAR) {
                starSlots.add(i);
            }
        }
        RandomSource random = getCart().getRandom();
        if (!starSlots.isEmpty()) {
            int slot = starSlots.get(random.nextInt(starSlots.size()));
            ItemStack item = getStack(slot);
            ItemStack charge = item.copy();
            charge.setCount(1);
            removeItemStack(item, charge.getCount(), slot);
            return charge;
        }

        Shape shape = Shape.SMALL_BALL;
        IntList colors = new IntArrayList();
        IntList fadeColors = new IntArrayList();
        boolean hasTrail = false;
        boolean hasTwinkle = false;

        boolean attemptTrail = random.nextInt(16) == 0;
        boolean attemptTwinkle = random.nextInt(8) == 0;
        boolean attemptModifier = random.nextInt(4) == 0;
        Shape wantedShape = Shape.byId(1 + random.nextInt(4));

        boolean removedGunpowder = false;
        for (int j = 0; j < getInventorySize(); ++j) {
            ItemStack item = getStack(j);
            if (item.isEmpty()) continue;
            if (item.getItem() == Items.GUNPOWDER && !removedGunpowder) {
                removeItemStack(item, 1, j);
                removedGunpowder = true;
                break;
            }
        }

        if (!removedGunpowder) {
            return ItemStack.EMPTY;
        }

        boolean removedModifier = false;
        boolean removedDiamond = false;
        boolean removedGlow = false;
        for (int j = 0; j < getInventorySize(); ++j) {
            ItemStack item = getStack(j);
            if (item.isEmpty()) continue;
            if (item.getItem() == Items.GLOWSTONE_DUST && attemptTwinkle && !removedGlow) {
                removeItemStack(item, 1, j);
                removedGlow = true;
                hasTwinkle = true;
            } else if (item.getItem() == Items.DIAMOND && attemptTrail && !removedDiamond) {
                removeItemStack(item, 1, j);
                removedDiamond = true;
                hasTrail = true;
            } else if (attemptModifier && !removedModifier && ((item.getItem() == Items.FIRE_CHARGE && wantedShape == Shape.LARGE_BALL) || (item.getItem() == Items.GOLD_NUGGET && wantedShape == Shape.STAR) || (item.is(ItemTags.SKULLS) && wantedShape == Shape.CREEPER) || (item.getItem() == Items.FEATHER && wantedShape == Shape.BURST))) {
                removeItemStack(item, 1, j);
                removedModifier = true;
                shape = wantedShape;
            }
        }
        generateColors(colors, (shape != Shape.BURST) ? 7 : 8);
        if (colors.isEmpty()) {
            return ItemStack.EMPTY;
        }
        if (getCart().getRandom().nextInt(4) == 0) {
            generateColors(fadeColors, 8);
        }

        FireworkExplosion explosion = new FireworkExplosion(shape, colors, fadeColors, hasTrail, hasTwinkle);
        ItemStack charge = new ItemStack(Items.FIREWORK_STAR);
        charge.set(DataComponents.FIREWORK_EXPLOSION, explosion);
        return charge;
    }

    private void generateColors(IntList colours, int maxColorCount) {
        int[] maxColors = new int[16];
        int[] currentColors = new int[16];
        for (int i = 0; i < getInventorySize(); ++i) {
            ItemStack stack = getStack(i);
            if (stack.isEmpty() || !stack.is(Tags.Items.DYES)) continue;
            DyeColor colour = DyeColor.getColor(stack);
            if (colour == null) continue;
            maxColors[colour.getId()] += stack.getCount();
        }

        int colorCount;
        for (colorCount = getCart().getRandom().nextInt(2) + 1; colorCount <= maxColorCount - 2 && getCart().getRandom().nextInt(2) == 0; colorCount += 2);

        ArrayList<Integer> availableColours = new ArrayList<>();
        for (int colourId = 0; colourId < 16; ++colourId) {
            if (maxColors[colourId] > 0) {
                availableColours.add(colourId);
            }
        }
        if (availableColours.isEmpty()) return;

        while (colorCount > 0 && !availableColours.isEmpty()) {
            int randomPick = getCart().getRandom().nextInt(availableColours.size());
            int colorId = availableColours.get(randomPick);
            ++currentColors[colorId];
            if (--maxColors[colorId] <= 0) {
                availableColours.remove(randomPick);
            }
            colours.add(DyeColor.byId(colorId).getFireworkColor());
            --colorCount;
        }

        for (int k = 0; k < getInventorySize(); ++k) {
            ItemStack stack = getStack(k);
            if (stack.isEmpty() || !stack.is(Tags.Items.DYES)) continue;
            DyeColor colour = DyeColor.getColor(stack);
            if (colour == null) continue;

            int used = currentColors[colour.getId()];
            if (used > 0) {
                used = Math.min(used, stack.getCount());
                removeItemStack(stack, used, k);
                currentColors[colour.getId()] -= used;
            }
        }
    }

    private void removeItemStack(@Nonnull ItemStack item, int count, int slot) {
        if (!getCart().hasCreativeSupplies()) {
            item.shrink(count);
            if (item.getCount() <= 0) {
                setStack(slot, ItemStack.EMPTY);
            }
        }
    }

    private void launchFirework(@Nonnull ItemStack firework) {
        FireworkRocketEntity rocket = new FireworkRocketEntity(getCart().level(), getCart().blockPosition().getX(), getCart().blockPosition().getY() + 1.0, getCart().blockPosition().getZ(), firework);
        getCart().level().addFreshEntity(rocket);
    }
}
