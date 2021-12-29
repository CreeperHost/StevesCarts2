package vswe.stevescarts.compat.ftbic;

import dev.ftb.mods.ftbic.util.EnergyHandler;
import dev.ftb.mods.ftbic.util.EnergyTier;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.IIntArray;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import vswe.stevescarts.blocks.tileentities.TileEntityManager;
import vswe.stevescarts.helpers.storages.TransferManager;

import javax.annotation.Nullable;

public class TileIndustrialManager extends TileEntityManager implements INamedContainerProvider, EnergyHandler
{
    double energy;
    double energyAdded;

    protected final IIntArray dataAccess = new IIntArray()
    {
        public int get(int id)
        {
            switch (id)
            {
                case 0:
                    return (int) energy;
                default:
                    throw new IllegalArgumentException("Invalid index: " + id);
            }
        }

        public void set(int p_221477_1_, int p_221477_2_)
        {
            throw new IllegalStateException("Cannot set values through IIntArray");
        }

        public int getCount()
        {
            return 1;
        }
    };

    public TileIndustrialManager()
    {
        super(CompatFtbic.INDUSTRIAL_MANAGER_TILE.get());
    }

    @Override
    protected boolean isTargetValid(TransferManager p0) {
        return false;
    }

    @Override
    protected boolean doTransfer(TransferManager p0) {
        return false;
    }

    @Override
    public int getAmountCount() {
        return 0;
    }

    @Override
    public boolean stillValid(PlayerEntity player) {
        return true;
    }

    @Override
    public int getContainerSize() {
        return 0;
    }

    @Override
    public ITextComponent getDisplayName() {
        return new StringTextComponent("industrial_manager");
    }

    @Nullable
    @Override
    public Container createMenu(int id, PlayerInventory playerInventory, PlayerEntity playerEntity) {
        return new ContainerIndustrialManager(id, playerInventory, this, this.dataAccess);
    }

    @Override
    public double getEnergyCapacity() {
        return 50000;
    }

    @Override
    public double getEnergy() {
        return energy;
    }

    @Override
    public void setEnergyRaw(double v) {
        this.energy = v;
    }

    @Override
    public double insertEnergy(double maxInsert, boolean simulate)
    {
        if (!simulate)
        {
            if(energy < getEnergyCapacity())
            {
                this.energy += maxInsert;
            }
        }
        return maxInsert;
    }

    @Override
    public EnergyTier getInputEnergyTier() {
        return EnergyTier.EV;
    }

    @Override
    public void tick()
    {
    }

    @Override
    public CompoundNBT save(CompoundNBT nbttagcompound)
    {
        super.save(nbttagcompound);
        nbttagcompound.putDouble("energy", energy);
        return nbttagcompound;
    }

    @Override
    public void load(BlockState blockState, CompoundNBT nbttagcompound)
    {
        super.load(blockState, nbttagcompound);
        energy = nbttagcompound.getDouble("energy");
    }
}
