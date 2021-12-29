package vswe.stevescarts.upgrades;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.inventory.container.Slot;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fluids.FluidStack;
import vswe.stevescarts.blocks.tileentities.TileEntityUpgrade;
import vswe.stevescarts.client.guis.GuiUpgrade;
import vswe.stevescarts.containers.slots.SlotLiquidOutput;
import vswe.stevescarts.containers.slots.SlotLiquidUpgradeInput;
import vswe.stevescarts.helpers.ResourceHelper;
import vswe.stevescarts.helpers.storages.SCTank;

public abstract class TankEffect extends InventoryEffect
{
    private static final int tankInterfaceX = 35;
    private static final int tankInterfaceY = 20;
    private static ResourceLocation texture;

    public abstract int getTankSize();

    @Override
    public Class<? extends Slot> getSlot(final int id)
    {
        return SlotLiquidOutput.class;
    }

    @Override
    public Slot createSlot(final TileEntityUpgrade upgrade, final int id)
    {
        if (id == 0)
        {
            return new SlotLiquidUpgradeInput(upgrade, upgrade.tank, 16, id, getSlotX(id), getSlotY(id));
        }
        return super.createSlot(upgrade, id);
    }

    @Override
    public int getInventorySize()
    {
        return 2;
    }

    @Override
    public int getSlotX(final int id)
    {
        return 8;
    }

    @Override
    public int getSlotY(final int id)
    {
        return 24 * (id + 1);
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void drawBackground(MatrixStack matrixStack, TileEntityUpgrade upgrade, final GuiUpgrade gui, final int x, final int y)
    {
        if (TankEffect.texture == null)
        {
            TankEffect.texture = ResourceHelper.getResource("/gui/tank.png");
        }
        upgrade.tank.drawFluid(matrixStack, gui, tankInterfaceX, tankInterfaceY);
        ResourceHelper.bindResource(TankEffect.texture);
        gui.blit(matrixStack, gui.getGuiLeft() + tankInterfaceX, gui.getGuiTop() + tankInterfaceY, 0, 0, 36, 51);
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void drawMouseOver(MatrixStack matrixStack, TileEntityUpgrade upgrade, final GuiUpgrade gui, final int x, final int y)
    {
        drawMouseOver(matrixStack, gui, upgrade.tank.getMouseOver(), x, y, new int[]{tankInterfaceX, tankInterfaceX, 36, 51});
    }

    @Override
    public void init(final TileEntityUpgrade upgrade)
    {
        upgrade.tank = new SCTank(upgrade, getTankSize(), 0);
        upgrade.getCompound().putByte("Tick", (byte) 0);
    }

    @Override
    public void update(final TileEntityUpgrade upgrade)
    {
        super.update(upgrade);
        upgrade.getCompound().putByte("Tick", (byte) (upgrade.getCompound().getByte("Tick") - 1));
        if (upgrade.getCompound().getByte("Tick") <= 0)
        {
            upgrade.getCompound().putByte("Tick", (byte) 5);
            if (!upgrade.getLevel().isClientSide && slots != null && slots.size() >= 2)
            {
                upgrade.tank.containerTransfer();
            }
        }
    }

    @Override
    public void load(final TileEntityUpgrade upgrade, final CompoundNBT compound)
    {
        if (compound.getByte("Exists") != 0)
        {
            upgrade.tank.setFluid(FluidStack.loadFluidStackFromNBT(compound));
        }
        else
        {
            upgrade.tank.setFluid(null);
        }
    }

    @Override
    public void save(final TileEntityUpgrade upgrade, final CompoundNBT compound)
    {
        if (upgrade.tank.getFluid() == null)
        {
            compound.putByte("Exists", (byte) 0);
        }
        else
        {
            compound.putByte("Exists", (byte) 1);
            upgrade.tank.getFluid().writeToNBT(compound);
        }
    }
}
