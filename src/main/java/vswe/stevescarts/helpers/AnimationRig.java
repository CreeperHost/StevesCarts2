package vswe.stevescarts.helpers;

import java.util.ArrayList;

public class AnimationRig
{
    private ArrayList<AnimationRigVal> rigs;

    public AnimationRig()
    {
        rigs = new ArrayList<>();
    }

    public void setAnimDone()
    {
        for (AnimationRigVal rig : rigs)
        {
            rig.setAnimDone();
        }
    }

    public boolean update(final boolean goDown)
    {
        if (goDown)
        {
            for (int i = rigs.size() - 1; i >= 0; --i)
            {
                if (rigs.get(i).update(true))
                {
                    return false;
                }
            }
            return false;
        }
        for (int i = 0; i < rigs.size(); ++i)
        {
            if (rigs.get(i).update(false))
            {
                return false;
            }
        }
        return true;
    }

    public void addVal(final AnimationRigVal val)
    {
        rigs.add(val);
    }
}
