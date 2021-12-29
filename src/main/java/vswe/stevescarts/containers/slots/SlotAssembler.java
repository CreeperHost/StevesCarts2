package vswe.stevescarts.containers.slots;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import vswe.stevescarts.blocks.tileentities.TileEntityCartAssembler;
import vswe.stevescarts.items.ItemCarts;
import vswe.stevescarts.modules.data.ModuleData;

import javax.annotation.Nonnull;

public class SlotAssembler extends Slot
{
    private int groupID;
    private int xPos;
    private int yPos;
    private TileEntityCartAssembler assembler;
    private int openingAnimation;
    private int id;
    private boolean isValid;
    private boolean useLarge;
    private boolean reloadOnUpdate;

    public SlotAssembler(final TileEntityCartAssembler assembler, final int i, final int j, final int k, final int groupID, final boolean useLarge, final int id)
    {
        super(assembler, i, j, k);
        this.assembler = assembler;
        this.useLarge = useLarge;
        this.groupID = groupID;
        xPos = j;
        yPos = k;
        isValid = true;
        this.id = id;
    }

    private void invalidationCheck() {
        x = -3000;
        y = -3000;
        if (openingAnimation > 8) {
            openingAnimation = 8;
        }
    }

    public void update()
    {
        if (!assembler.getLevel().isClientSide) {
            if (!isValid() && hasItem()) {
                assembler.puke(getItem());
                set(ItemStack.EMPTY);
            }
        } else if (isValid()) {
            if (openingAnimation == 8) {
                x = getX();
                y = getY();
                ++openingAnimation;
            } else if (openingAnimation < 8) {
                ++openingAnimation;
            }
        } else if (openingAnimation > 0) {
            --openingAnimation;
        } else {
            openingAnimation = id * -3;
        }
    }

    @Override
    public void setChanged()
    {
        super.setChanged();
        if (shouldUpdatePlaceholder()) {
            assembler.updatePlaceholder();
        } else {
            assembler.isErrorListOutdated = true;
        }
    }

    public boolean useLargeInterface()
    {
        return useLarge;
    }

    @Override
    public boolean mayPlace(@Nonnull ItemStack itemstack)
    {
        return isValid && ModuleData.isValidModuleItem(groupID, itemstack);
    }

    public void invalidate()
    {
        isValid = false;
        invalidationCheck();
    }

    @Override
    public int getMaxStackSize()
    {
        return 1;
    }

    public void validate()
    {
        isValid = true;
    }

    public boolean isValid()
    {
        return isValid;
    }

    public int getAnimationTick()
    {
        return openingAnimation;
    }

    public int getX()
    {
        return xPos;
    }

    public int getY()
    {
        return yPos;
    }

    public TileEntityCartAssembler getAssembler()
    {
        return assembler;
    }

    public boolean shouldUpdatePlaceholder()
    {
        return true;
    }

    @Override
    public boolean mayPickup(PlayerEntity player)
    {
        if (getItem().getItem() instanceof ItemCarts)
        {
            if (assembler.getIsAssembling()) return false;
        }
        return !getItem().isEmpty() && (!getItem().hasTag() || !getItem().getTag().contains(TileEntityCartAssembler.MODIFY_STATUS) || getItem().getTag().getInt(TileEntityCartAssembler.MODIFY_STATUS) > 0);
    }
}
