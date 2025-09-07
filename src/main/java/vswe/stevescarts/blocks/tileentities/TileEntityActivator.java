package vswe.stevescarts.blocks.tileentities;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.jetbrains.annotations.NotNull;
import vswe.stevescarts.containers.ContainerActivator;
import vswe.stevescarts.entities.ModularMinecart;
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

    @org.jetbrains.annotations.Nullable
    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider provider) {
        return saveWithoutMetadata(provider);
    }

    @Override
    protected void loadAdditional(ValueInput input) {
        super.loadAdditional(input);
        for (ActivatorOption option : options) {
            option.setOption(input.getByteOr(option.getName(), (byte) 0));
        }
    }

    @Override
    protected void saveAdditional(ValueOutput output) {
        super.saveAdditional(output);
        for (ActivatorOption option : options) {
            output.putByte(option.getName(), (byte) option.getOption());
        }
    }

    public void receivePacket(final int id, final byte[] data, @org.jetbrains.annotations.Nullable ServerPlayer sender)
    {
        if (id == 0)
        {
            final boolean leftClick = (data[0] & 0x1) == 0x0;
            final int optionId = (data[0] & 0xFFFFFFFE) >> 1;
            if (optionId >= 0 && optionId < options.size())
            {
                options.get(optionId).changeOption(leftClick);
                setChanged();
            }
        }
    }

    public void handleCart(ModularMinecart cart, final boolean isOrange)
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
    public @NotNull Component getDisplayName()
    {
        return Component.translatable("container.activator");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int id, @NotNull Inventory playerInventory, @NotNull Player playerEntity)
    {
        return new ContainerActivator(id, playerInventory, this, new SimpleContainerData(0));
    }
}
