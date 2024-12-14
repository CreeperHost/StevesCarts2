package vswe.stevescarts.entities;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.vehicle.NewMinecartBehavior;
import net.minecraft.world.level.block.BaseRailBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.PoweredRailBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.RailShape;
import net.minecraft.world.phys.Vec3;
import vswe.stevescarts.api.modules.ModuleBase;
import vswe.stevescarts.blocks.BlockRailAdvDetector;

public class ModularMinecartBehavior extends NewMinecartBehavior {

    private final ModularMinecart minecart;

    public ModularMinecartBehavior(ModularMinecart minecart) {
        super(minecart);
        this.minecart = minecart;
    }

    @Override
    public void tick() {
        super.tick();
    }

    @Override
    public void moveAlongTrack(ServerLevel serverLevel) {
        for(TrackIteration newminecartbehavior$trackiteration = new TrackIteration(); newminecartbehavior$trackiteration.shouldIterate() && this.minecart.isAlive(); newminecartbehavior$trackiteration.firstIteration = false) {
            Vec3 vec3 = this.getDeltaMovement();
            BlockPos blockpos = this.minecart.getCurrentBlockPosOrRailBelow();
            BlockState blockstate = this.level().getBlockState(blockpos);
            boolean flag = BaseRailBlock.isRail(blockstate);
            if (this.minecart.isOnRails() != flag) {
                this.minecart.setOnRails(flag);
                this.adjustToRails(blockpos, blockstate, false);
            }

            Vec3 vec31;
            if (flag) {
                this.minecart.resetFallDistance();
                this.minecart.setOldPosAndRot();
                Block block = blockstate.getBlock();
                if (block instanceof PoweredRailBlock) {
                    PoweredRailBlock poweredRail = (PoweredRailBlock)block;
                    if (poweredRail.isActivatorRail()) {
                        this.minecart.activateMinecart(blockpos.getX(), blockpos.getY(), blockpos.getZ(), (Boolean)blockstate.getValue(PoweredRailBlock.POWERED));
                    }
                }

                if (block instanceof BlockRailAdvDetector advDetector) {
                    advDetector.onMinecartPassSC(blockstate, serverLevel, blockpos, minecart);
                }

                minecart.handleMoveAlongTrack(blockpos, blockstate);

                RailShape railshape = ((BaseRailBlock)blockstate.getBlock()).getRailDirection(blockstate, this.level(), blockpos, this.minecart);
                vec31 = this.calculateTrackSpeed(serverLevel, vec3.horizontal(), newminecartbehavior$trackiteration, blockpos, blockstate, railshape);
                if (newminecartbehavior$trackiteration.firstIteration) {
                    newminecartbehavior$trackiteration.movementLeft = vec31.horizontalDistance();
                } else {
                    newminecartbehavior$trackiteration.movementLeft += vec31.horizontalDistance() - vec3.horizontalDistance();
                }

                this.setDeltaMovement(vec31);
                newminecartbehavior$trackiteration.movementLeft = this.minecart.makeStepAlongTrack(blockpos, railshape, newminecartbehavior$trackiteration.movementLeft);
            } else {
                this.minecart.comeOffTrack(serverLevel);
                newminecartbehavior$trackiteration.movementLeft = 0.0;
            }

            Vec3 vec32 = this.position();
            vec31 = vec32.subtract(this.minecart.oldPosition());
            double d0 = vec31.length();
            if (d0 > 9.999999747378752E-6) {
                if (!(vec31.horizontalDistanceSqr() > 9.999999747378752E-6)) {
                    if (!this.minecart.isOnRails()) {
                        this.setXRot(this.minecart.onGround() ? 0.0F : Mth.rotLerp(0.2F, this.getXRot(), 0.0F));
                    }
                } else {
                    float f = 180.0F - (float)(Math.atan2(vec31.z, vec31.x) * 180.0 / Math.PI);
                    float f1 = this.minecart.onGround() && !this.minecart.isOnRails() ? 0.0F : 90.0F - (float)(Math.atan2(vec31.horizontalDistance(), vec31.y) * 180.0 / Math.PI);
                    f += this.minecart.isFlipped() ? 180.0F : 0.0F;
                    f1 *= this.minecart.isFlipped() ? -1.0F : 1.0F;
                    this.setRotation(f, f1);
                }

                this.lerpSteps.add(new MinecartStep(vec32, this.getDeltaMovement(), this.getYRot(), this.getXRot(), (float)Math.min(d0, this.getMaxSpeed(serverLevel))));
            } else if (vec3.horizontalDistanceSqr() > 0.0) {
                this.lerpSteps.add(new MinecartStep(vec32, this.getDeltaMovement(), this.getYRot(), this.getXRot(), 1.0F));
            }

            if (d0 > 9.999999747378752E-6 || newminecartbehavior$trackiteration.firstIteration) {
                this.minecart.applyEffectsFromBlocks();
                this.minecart.applyEffectsFromBlocks();
            }
        }
    }

    @Override //TODO, THis may need tweaking, The previous default "getMaxCartSpeedOnRail" value was 1.2, So things may need to be tweaked.
    public double getMaxSpeed(ServerLevel level) {
        double maxSpeed = this.minecart.isInWater() ? 0.2 : 0.4;
        return Math.min(maxSpeed, minecart.modules().stream().mapToDouble(ModuleBase::getMaxSpeed).min().orElse(maxSpeed));
    }
}