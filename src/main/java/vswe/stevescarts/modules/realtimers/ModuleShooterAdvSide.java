package vswe.stevescarts.modules.realtimers;

import vswe.stevescarts.entities.ModularMinecart;
import vswe.stevescarts.helpers.AnimationRig;
import vswe.stevescarts.helpers.AnimationRigVal;

public class ModuleShooterAdvSide extends ModuleShooterAdv {
    private final AnimationRig rig;
    private final AnimationRigVal handlePos;
    private final AnimationRigVal basePos;
    private final AnimationRigVal handleRot;
    private final AnimationRigVal gunRot;
    private final AnimationRigVal backPos;
    private final AnimationRigVal backRot;
    private final AnimationRigVal attacherRot;
    private final AnimationRigVal stabalizerOut;
    private final AnimationRigVal stabalizerDown;
    private final AnimationRigVal standOut;
    private final AnimationRigVal standUp;
    private final AnimationRigVal standSlide;
    private final AnimationRigVal armBasePos;
    private final AnimationRigVal armPos;
    private final AnimationRigVal armRot;
    private final AnimationRigVal missilePos;
    private final AnimationRigVal missileRot;
    private final AnimationRigVal armBasePos2;
    private final AnimationRigVal armPos2;
    private final AnimationRigVal armRot2;

    public ModuleShooterAdvSide(ModularMinecart cart) {
        super(cart);
        rig = new AnimationRig();
        handlePos = new AnimationRigVal(rig, 8.55f, 9.4f, 0.0f);
        basePos = new AnimationRigVal(rig, 1.05f, 4.0f, 0.05f);
        handleRot = new AnimationRigVal(rig, 3.1415927f, 4.712389f, 0.075f);
        gunRot = new AnimationRigVal(rig, 0.0f, -1.5707964f, 0.0f);
        backPos = new AnimationRigVal(rig, 4.5f, -3.0f, 0.3f);
        backRot = new AnimationRigVal(rig, 0.0f, -1.5707964f, 0.2f);
        attacherRot = new AnimationRigVal(rig, 0.0f, -3.1415927f, 0.2f);
        stabalizerOut = new AnimationRigVal(rig, 0.001f, 0.8f, 0.1f);
        stabalizerDown = new AnimationRigVal(rig, 0.0f, -2.0f, 0.1f);
        standOut = new AnimationRigVal(rig, 0.001f, 0.8f, 0.1f);
        standUp = new AnimationRigVal(rig, 0.0f, 2.0f, 0.1f);
        standSlide = new AnimationRigVal(rig, 0.0f, 0.25f, 0.01f);
        armBasePos = new AnimationRigVal(rig, 0.5f, 10.0f, 0.3f);
        armPos = new AnimationRigVal(rig, -2.25f, 2.5f, 0.0f);
        armRot = new AnimationRigVal(rig, 0.0f, 1.5707964f, 0.2f);
        missilePos = new AnimationRigVal(rig, 0.0f, 3.0f, 0.1f);
        missileRot = new AnimationRigVal(rig, 0.0f, -0.2f, 0.0f);
        armRot2 = new AnimationRigVal(rig, 0.0f, 1.5707964f, 0.2f);
        armBasePos2 = new AnimationRigVal(rig, 0.0f, 9.5f, 0.3f);
        armPos2 = new AnimationRigVal(rig, 0.0f, 5.0f, 0.0f);
        handlePos.setUpAndDown(basePos);
        handlePos.setSpeedToSync(basePos, false);
        handleRot.setUpAndDown(gunRot);
        gunRot.setSpeedToSync(handleRot, true);
        armPos.setSpeedToSync(armBasePos, false);
        armBasePos.setUpAndDown(armPos);
        missilePos.setUpAndDown(missileRot);
        missileRot.setSpeedToSync(missilePos, true);
        armPos2.setSpeedToSync(armBasePos2, false);
        armBasePos2.setUpAndDown(armPos2);
    }

    @Override
    public void update() {
        super.update();
        rig.update(!isPipeActive(0));
    }

    public float getHandlePos(final int mult) {
        return handlePos.getVal() * mult;
    }

    public float getBasePos(final int mult) {
        return basePos.getVal() * mult;
    }

    public float getHandleRot(final int mult) {
        return handleRot.getVal();
    }

    public float getGunRot(final int mult) {
        return gunRot.getVal();
    }

    public float getBackPos(final int mult) {
        return backPos.getVal();
    }

    public float getBackRot(final int mult) {
        return backRot.getVal() * mult;
    }

    public float getAttacherRot(final int mult) {
        return attacherRot.getVal() * mult;
    }

    public float getStabalizerOut(final int mult) {
        return stabalizerOut.getVal() * mult;
    }

    public float getStabalizerDown(final int mult) {
        return stabalizerDown.getVal();
    }

    public float getStandOut(final int mult, final int i, final int j) {
        return standOut.getVal() * j + mult * i * 0.5f + 0.003f;
    }

    public float getStandUp(final int mult, final int i, final int j) {
        return standUp.getVal() - standSlide.getVal() * (i * 2 - 1) * j * mult;
    }

    public float getArmBasePos(final int mult, final boolean fake) {
        return armBasePos.getVal() - (fake ? 0.0f : armBasePos2.getVal());
    }

    public float getArmRot(final int mult, final boolean fake) {
        return (armRot.getVal() - (fake ? 0.0f : armRot2.getVal())) * mult;
    }

    public float getArmPos(final int mult, final boolean fake) {
        return armPos.getVal() - (fake ? 0.0f : armPos2.getVal());
    }

    public float getMissilePos(final int mult) {
        return missilePos.getVal();
    }

    public float getMissileRot(final int mult) {
        return missileRot.getVal() * mult;
    }
}
