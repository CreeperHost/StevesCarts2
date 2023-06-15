package vswe.stevescarts.modules.realtimers;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.commands.CommandSource;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.Blocks;
import vswe.stevescarts.client.guis.GuiMinecart;
import vswe.stevescarts.entities.EntityMinecartModular;
import vswe.stevescarts.helpers.ResourceHelper;
import vswe.stevescarts.api.modules.ModuleBase;

public abstract class ModuleCommand extends ModuleBase implements CommandSource
{
    private String command;
    private int[] textbox;

    public ModuleCommand(final EntityMinecartModular cart)
    {
        super(cart);
        command = "say HI";
        textbox = new int[]{10, 10, 145, 90};
    }

    @Override
    public void drawForeground(GuiGraphics guiGraphics, GuiMinecart gui)
    {
        //TODO
        //		final List lines = Minecraft.getInstance().font.width(command, textbox[2] - 4);
        //		for (int i = 0; i < lines.size(); ++i) {
        //			final String line = lines.get(i).toString();
        //			drawString(matrixStack, gui, line, textbox[0] + 2, textbox[1] + 2 + i * 8, 16777215);
        //		}
    }

    @Override
    public boolean hasGui()
    {
        return true;
    }

    @Override
    public boolean hasSlots()
    {
        return false;
    }

    @Override
    public void drawBackground(GuiGraphics guiGraphics, GuiMinecart gui, final int x, final int y)
    {
        ResourceHelper.bindResource("/gui/command.png");
        drawImage(guiGraphics, gui, textbox, 0, 0);
    }

    public void keyPress(final char character, final int extraInformation)
    {
        if (extraInformation == 14)
        {
            if (command.length() > 0)
            {
                command = command.substring(0, command.length() - 1);
            }
        }
        else
        {
            command += Character.toString(character);
        }
    }

    public String getCommandSenderName()
    {
        return "@";
    }

    public void sendChatToPlayer(final String var1)
    {
    }


    //	@Override
    //	public boolean canUseCommand(final int var1, final String var2) {
    //		return var1 <= 2;
    //	}

    public String translateString(final String var1, final Object... var2)
    {
        return var1;
    }

    public BlockPos getPlayerCoordinates()
    {
        return getCart().blockPosition();
    }

    private void executeCommand()
    {
        if (!getCart().level().isClientSide)
        {
            //TODO
        }
    }

    @Override
    public void moveMinecartOnRail(BlockPos pos)
    {
        if (getCart().level().getBlockState(pos).getBlock() == Blocks.DETECTOR_RAIL)
        {
            executeCommand();
        }
    }

    @Override
    protected void Save(final CompoundTag tagCompound, final int id)
    {
        tagCompound.putString(generateNBTName("Command", id), command);
    }

    @Override
    protected void Load(final CompoundTag tagCompound, final int id)
    {
        command = tagCompound.getString(generateNBTName("Command", id));
    }
}
