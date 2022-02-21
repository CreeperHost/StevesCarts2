package vswe.stevescarts.client.guis.buttons;

import net.minecraft.world.entity.player.Player;
import vswe.stevescarts.computer.ComputerProg;
import vswe.stevescarts.modules.workers.ModuleComputer;

public class ButtonProgramStart extends ButtonAssembly
{
    public ButtonProgramStart(final ModuleComputer module, final LOCATION loc)
    {
        super(module, loc);
    }

    @Override
    public String toString()
    {
        return "Start Program";
    }

    @Override
    public boolean isVisible()
    {
        return super.isVisible();
    }

    @Override
    public boolean isEnabled()
    {
        return ((ModuleComputer) module).getCurrentProg() != null;
    }

    @Override
    public void onServerClick(final Player player, final int mousebutton, final boolean ctrlKey, final boolean shiftKey)
    {
        final ComputerProg program = ((ModuleComputer) module).getCurrentProg();
        if (program != null)
        {
            program.start();
        }
    }
}
