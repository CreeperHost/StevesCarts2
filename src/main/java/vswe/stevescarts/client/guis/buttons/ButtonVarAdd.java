package vswe.stevescarts.client.guis.buttons;

import net.minecraft.world.entity.player.Player;
import vswe.stevescarts.computer.ComputerVar;
import vswe.stevescarts.modules.workers.ModuleComputer;

public class ButtonVarAdd extends ButtonAssembly
{
    public ButtonVarAdd(final ModuleComputer module, final LOCATION loc)
    {
        super(module, loc);
    }

    @Override
    public String toString()
    {
        return "Add Variable";
    }

    @Override
    public boolean isVisible()
    {
        return super.isVisible();
    }

    @Override
    public int texture()
    {
        return 25;
    }

    @Override
    public boolean isEnabled()
    {
        return ((ModuleComputer) module).getCurrentProg() != null;
    }

    @Override
    public void onServerClick(final Player player, final int mousebutton, final boolean ctrlKey, final boolean shiftKey)
    {
        if (((ModuleComputer) module).getCurrentProg() != null)
        {
            final ComputerVar var = new ComputerVar((ModuleComputer) module);
            var.setEditing(true);
            ((ModuleComputer) module).getCurrentProg().getVars().add(var);
        }
    }
}
