package vswe.stevescarts.client.guis.buttons;

import net.minecraft.world.entity.player.Player;
import vswe.stevescarts.computer.ComputerProg;
import vswe.stevescarts.modules.workers.ModuleComputer;

public class ButtonProgramAdd extends ButtonAssembly
{
    public ButtonProgramAdd(final ModuleComputer module, final LOCATION loc)
    {
        super(module, loc);
    }

    @Override
    public String toString()
    {
        return "Add new program";
    }

    @Override
    public boolean isVisible()
    {
        return super.isVisible();
    }

    @Override
    public boolean isEnabled()
    {
        return true;
    }

    @Override
    public void onServerClick(final Player player, final int mousebutton, final boolean ctrlKey, final boolean shiftKey)
    {
        ((ModuleComputer) module).setCurrentProg(new ComputerProg((ModuleComputer) module));
    }
}
