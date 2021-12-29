package vswe.stevescarts.helpers;

import vswe.stevescarts.modules.ModuleBase;

import java.util.ArrayList;

public class GuiAllocationHelper
{
    public int width;
    public int maxHeight;
    public ArrayList<ModuleBase> modules;

    public GuiAllocationHelper()
    {
        modules = new ArrayList<>();
    }
}
