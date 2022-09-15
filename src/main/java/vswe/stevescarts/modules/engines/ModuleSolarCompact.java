package vswe.stevescarts.modules.engines;

import vswe.stevescarts.SCConfig;
import vswe.stevescarts.entities.EntityMinecartModular;
import vswe.stevescarts.helpers.AnimationRig;
import vswe.stevescarts.helpers.AnimationRigVal;

public class ModuleSolarCompact extends ModuleSolarBase
{
    private AnimationRig rig;
    private AnimationRigVal extraction;
    private AnimationRigVal topbot;
    private AnimationRigVal leftright;
    private AnimationRigVal corner;
    private AnimationRigVal angle;
    private AnimationRigVal extraction2;
    private AnimationRigVal innerextraction;

    public ModuleSolarCompact(final EntityMinecartModular cart)
    {
        super(cart);
        rig = new AnimationRig();
        extraction = new AnimationRigVal(rig, 0.4f, 2.0f, 0.1f);
        topbot = new AnimationRigVal(rig, 0.1f, 4.0f, 0.25f);
        leftright = new AnimationRigVal(rig, 0.01f, 6.0f, 0.2f);
        corner = new AnimationRigVal(rig, 0.1f, 4.0f, 0.25f);
        extraction2 = new AnimationRigVal(rig, 0.0f, 1.8f, 0.1f);
        innerextraction = new AnimationRigVal(rig, 0.4f, 3.0f, 0.2f);
        angle = new AnimationRigVal(rig, 0.0f, 1.5707964f, 0.1f);
        innerextraction.setUpAndDown(angle);
    }

    @Override
    protected int getMaxCapacity()
    {
        return 25000;
    }

    @Override
    protected int getGenSpeed()
    {
        return SCConfig.compact_solar_production.get();
    }

    @Override
    public boolean updatePanels()
    {
        return rig.update(isGoingDown());
    }

    @Override
    protected void setAnimDone()
    {
        rig.setAnimDone();
        down = false;
    }

    public float getExtractionDist()
    {
        return extraction.getVal() + extraction2.getVal();
    }

    public float getTopBotExtractionDist()
    {
        return topbot.getVal();
    }

    public float getLeftRightExtractionDist()
    {
        return leftright.getVal();
    }

    public float getCornerExtractionDist()
    {
        return corner.getVal();
    }

    public float getPanelAngle()
    {
        return angle.getVal();
    }

    public float getInnerExtraction()
    {
        return innerextraction.getVal();
    }
}
