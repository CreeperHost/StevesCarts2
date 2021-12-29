package vswe.stevescarts.modules.engines;

import vswe.stevescarts.entitys.EntityMinecartModular;

public abstract class ModuleSolarTop extends ModuleSolarBase
{
    private float minVal;
    private float maxVal;
    private float minAngle;
    private float maxAngle;
    private float innerRotation;
    private float movingLevel;

    public ModuleSolarTop(final EntityMinecartModular cart)
    {
        super(cart);
        minVal = -4.0f;
        maxVal = -13.0f;
        minAngle = 0.0f;
        maxAngle = 1.5707964f;
        innerRotation = 0.0f;
        movingLevel = minVal;
    }

    public float getInnerRotation()
    {
        return innerRotation;
    }

    public float getMovingLevel()
    {
        return movingLevel;
    }

    @Override
    protected void setAnimDone()
    {
        innerRotation = maxAngle;
        movingLevel = minVal;
        down = false;
    }

    @Override
    public boolean updatePanels()
    {
        if (movingLevel > minVal)
        {
            movingLevel = minVal;
        }
        if (innerRotation < minAngle)
        {
            innerRotation = minAngle;
        }
        else if (innerRotation > maxAngle)
        {
            innerRotation = maxAngle;
        }
        final float targetAngle = isGoingDown() ? minAngle : maxAngle;
        if (movingLevel > maxVal && innerRotation != targetAngle)
        {
            movingLevel -= 0.2f;
            if (movingLevel <= maxVal)
            {
                movingLevel = maxVal;
            }
        }
        else if (innerRotation != targetAngle)
        {
            innerRotation += (isGoingDown() ? -0.05f : 0.05f);
            if ((!isGoingDown() && innerRotation >= targetAngle) || (isGoingDown() && innerRotation <= targetAngle))
            {
                innerRotation = targetAngle;
            }
        }
        else if (movingLevel < minVal)
        {
            movingLevel += 0.2f;
            if (movingLevel >= minVal)
            {
                movingLevel = minVal;
            }
        }
        return innerRotation == maxAngle;
    }

    protected abstract int getPanelCount();
}
