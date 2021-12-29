package vswe.stevescarts.helpers;

public class AnimationRigVal
{
    private float val;
    private float min;
    private float max;
    private float speed;
    private AnimationRigVal down;
    private AnimationRigVal up;

    public AnimationRigVal(final AnimationRig rig, final float min, final float max, final float speed)
    {
        this.min = min;
        this.max = max;
        this.speed = speed;
        val = this.min;
        rig.addVal(this);
    }

    public void setUp(final AnimationRigVal up)
    {
        this.up = up;
    }

    public void setDown(final AnimationRigVal down)
    {
        this.down = down;
    }

    public void setUpAndDown(final AnimationRigVal up)
    {
        setUp(up);
        up.setDown(this);
    }

    public float getVal()
    {
        return val;
    }

    public void setAnimDone()
    {
        val = max;
    }

    public boolean update(final boolean goDown)
    {
        final float target = goDown ? min : max;
        if (target == val)
        {
            return false;
        }
        if (val < target)
        {
            val += speed;
            if (val > target)
            {
                val = target;
            }
        }
        else if (val > target)
        {
            val -= speed;
            if (val < target)
            {
                val = target;
            }
        }
        if (goDown)
        {
            if (down != null)
            {
                down.update(true);
            }
        }
        else if (up != null)
        {
            up.update(false);
        }
        return true;
    }

    public void setSpeedToSync(final AnimationRigVal syncTo, final boolean invert)
    {
        speed = (max - min) / ((syncTo.max - syncTo.min) / syncTo.speed);
        if (invert)
        {
            speed *= -1.0f;
        }
    }
}
