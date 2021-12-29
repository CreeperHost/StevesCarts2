package vswe.stevescarts.helpers;

import vswe.stevescarts.client.guis.buttons.ButtonBase;

import java.util.Comparator;

public class ButtonComparator implements Comparator
{
    public static final ButtonComparator INSTANCE = new ButtonComparator();

    private ButtonComparator()
    {
    }

    @Override
    public int compare(final Object obj1, final Object obj2)
    {
        final ButtonBase button1 = (ButtonBase) obj1;
        final ButtonBase button2 = (ButtonBase) obj2;
        if (!button1.isVisible() && !button2.isVisible())
        {
            return 0;
        }
        if (!button1.isVisible())
        {
            return 1;
        }
        if (!button2.isVisible())
        {
            return -1;
        }
        final int location1 = button1.getLocationID();
        final int location2 = button2.getLocationID();
        if (location1 != location2)
        {
            return (location1 < location2) ? -1 : 1;
        }
        final int id1 = button1.getIdInModule();
        final int id2 = button1.getIdInModule();
        if (id1 == id2)
        {
            return 0;
        }
        return (id1 < id2) ? -1 : 1;
    }
}
