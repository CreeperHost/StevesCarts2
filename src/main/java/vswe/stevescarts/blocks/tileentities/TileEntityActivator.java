package vswe.stevescarts.blocks.tileentities;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.level.block.state.BlockState;
import vswe.stevescarts.containers.ContainerActivator;
import vswe.stevescarts.entitys.EntityMinecartModular;
import vswe.stevescarts.helpers.ActivatorOption;
import vswe.stevescarts.helpers.Localization;
import vswe.stevescarts.init.ModBlocks;
import vswe.stevescarts.modules.addons.ModuleChunkLoader;
import vswe.stevescarts.modules.addons.ModuleInvisible;
import vswe.stevescarts.modules.addons.ModuleShield;
import vswe.stevescarts.modules.realtimers.ModuleCage;
import vswe.stevescarts.modules.workers.ModuleRemover;
import vswe.stevescarts.modules.workers.tools.ModuleDrill;

import javax.annotation.Nullable;
import java.util.ArrayList;

public class TileEntityActivator extends TileEntityBase implements MenuProvider
{
    private ArrayList<ActivatorOption> options;

    public TileEntityActivator(BlockPos blockPos, BlockState blockState)
    {
        super(ModBlocks.MODULE_TOGGLER_TILE.get(), blockPos, blockState);
        loadOptions();
    }

    private void loadOptions()
    {
        (options = new ArrayList<>()).add(new ActivatorOption(Localization.GUI.TOGGLER.OPTION_DRILL, ModuleDrill.class));
        options.add(new ActivatorOption(Localization.GUI.TOGGLER.OPTION_SHIELD, ModuleShield.class));
        options.add(new ActivatorOption(Localization.GUI.TOGGLER.OPTION_INVISIBILITY, ModuleInvisible.class));
        options.add(new ActivatorOption(Localization.GUI.TOGGLER.OPTION_CHUNK, ModuleChunkLoader.class));
        options.add(new ActivatorOption(Localization.GUI.TOGGLER.OPTION_CAGE_AUTO, ModuleCage.class, 0));
        options.add(new ActivatorOption(Localization.GUI.TOGGLER.OPTION_CAGE, ModuleCage.class, 1));
        options.add(new ActivatorOption(Localization.GUI.TOGGLER.OPTION_REMOVER, ModuleRemover.class));
    }

    public ArrayList<ActivatorOption> getOptions()
    {
        return options;
    }

    @Override
    public void load(CompoundTag nbttagcompound)
    {
        super.load(nbttagcompound);
        for (final ActivatorOption option : options)
        {
            option.setOption(nbttagcompound.getByte(option.getName()));
        }
    }


    @Override
    public void saveAdditional(CompoundTag compoundNBT)
    {
        super.saveAdditional(compoundNBT);
        for (final ActivatorOption option : options)
        {
            compoundNBT.putByte(option.getName(), (byte) option.getOption());
        }
    }

    public void receivePacket(final int id, final byte[] data, final Player player)
    {
        if (id == 0)
        {
            final boolean leftClick = (data[0] & 0x1) == 0x0;
            final int optionId = (data[0] & 0xFFFFFFFE) >> 1;
            if (optionId >= 0 && optionId < options.size())
            {
                options.get(optionId).changeOption(leftClick);
            }
        }
    }

    public void handleCart(final EntityMinecartModular cart, final boolean isOrange)
    {
        for (final ActivatorOption option : options)
        {
            if (!option.isDisabled())
            {
                cart.handleActivator(option, isOrange);
            }
        }
    }

    @Override
    public Component getDisplayName()
    {
        return Component.translatable("container.activator");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int id, Inventory playerInventory, Player playerEntity)
    {
        return new ContainerActivator(id, playerInventory, this, new SimpleContainerData(0));
    }
}
