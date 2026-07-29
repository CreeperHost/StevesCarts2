package vswe.stevescarts.modules.addons;

import net.creeperhost.polylib.data.serializable.ByteData;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FallingBlock;
import vswe.stevescarts.api.modules.ModuleBase;
import vswe.stevescarts.api.modules.template.ModuleAddon;
import vswe.stevescarts.entities.ModularMinecart;
import vswe.stevescarts.modules.workers.ModuleLiquidDrainer;
import vswe.stevescarts.modules.workers.tools.ModuleDrill;
import vswe.stevescarts.polylib.EntityData;

public class ModuleLiquidSensors extends ModuleAddon {
    private final EntityData<Byte> sensorInfo = new EntityData<>(getCart(), new ByteData((byte) 1));
    private float sensorRotation;
    private int activetime;
    private int mult;

    public ModuleLiquidSensors(ModularMinecart cart) {
        super(cart);
        activetime = -1;
        mult = 1;
    }

    @Override
    public void update() {
        super.update();
        if (isDrillSpinning()) {
            sensorRotation += 0.05f * mult;
            if ((mult == 1 && sensorRotation > 0.7853981633974483) || (mult == -1 && sensorRotation < -0.7853981633974483)) {
                mult *= -1;
            }
        } else {
            if (sensorRotation != 0.0f) {
                if (sensorRotation > 0.0f) {
                    sensorRotation -= 0.05f;
                    if (sensorRotation < 0.0f) {
                        sensorRotation = 0.0f;
                    }
                } else {
                    sensorRotation += 0.05f;
                    if (sensorRotation > 0.0f) {
                        sensorRotation = 0.0f;
                    }
                }
            }
            if (activetime >= 0) {
                ++activetime;
                if (activetime >= 10) {
                    setLight(1);
                    activetime = -1;
                }
            }
        }
    }

    private void activateLight(final int light) {
        if (getLight() == 3 && light == 2) {
            return;
        }
        setLight(light);
        activetime = 0;
    }

    public void getInfoFromDrill(byte data) {
        final byte light = (byte) (data & 0x3);
        if (light != 1) {
            activateLight(light);
        }
        data &= 0b11111111111111111111111111111100;
        data |= (byte) getLight();
        setSensorInfo(data);
    }

    private void setSensorInfo(byte val) {
        if (isPlaceholder()) {
            return;
        }
        sensorInfo.set(val);
    }

    public int getLight() {
        if (isPlaceholder()) {
            return getSimInfo().getLiquidLight();
        }
        return sensorInfo.get() & 0b11;
    }

    private void setLight(final int val) {
        if (isPlaceholder()) {
            return;
        }
        byte data = sensorInfo.get();
        data &= 0xFFFFFFFC;
        data |= (byte) val;
        setSensorInfo(data);
    }

    protected boolean isDrillSpinning() {
        if (isPlaceholder()) {
            return getSimInfo().getDrillSpinning();
        }
        return (sensorInfo.get() & 0x4) != 0x0;
    }

    public float getSensorRotation() {
        return sensorRotation;
    }

    public boolean isDangerous(final ModuleDrill drill, BlockPos pos, boolean isUp) {
        final Block block = getCart().level().getBlockState(pos).getBlock();
        if (block == Blocks.LAVA) {
            handleLiquid(drill, pos);
            return true;
        }
        if (block == Blocks.WATER) {
            handleLiquid(drill, pos);
            return true;
        }
        if (block != null && isFluid(getCart().level(), pos)) {
            handleLiquid(drill, pos);
            return true;
        }
        final boolean isWater = block == Blocks.WATER || block == Blocks.WATER || block == Blocks.ICE;
        final boolean isLava = block == Blocks.LAVA || block == Blocks.LAVA;
        final boolean isOther = block != null && isFluid(getCart().level(), pos);
        final boolean isLiquid = isWater || isLava || isOther;
        if (!isLiquid) {
            if (isUp) {
                final boolean isFalling = block instanceof FallingBlock;
                if (isFalling) {
                    return isDangerous(drill, pos.offset(0, 1, 0), true) || isDangerous(drill, pos.offset(1, 0, 0), false) || isDangerous(drill, pos.offset(-1, 0, 0), false) || isDangerous(drill, pos.offset(0, 0, 1), false) || isDangerous(drill, pos.offset(0, 0, -1), false);
                }
            }
            return false;
        }
        if (isUp) {
            handleLiquid(drill, pos);
            return true;
        }
        return false;
    }

    private boolean isFluid(Level level, BlockPos pos) {
        return !level.getFluidState(pos).isEmpty();
    }

    private void handleLiquid(final ModuleDrill drill, BlockPos pos) {
        ModuleLiquidDrainer liquiddrainer = null;
        for (final ModuleBase module : getCart().modules()) {
            if (module instanceof ModuleLiquidDrainer) {
                liquiddrainer = (ModuleLiquidDrainer) module;
                break;
            }
        }
        if (liquiddrainer != null) {
            liquiddrainer.handleLiquid(drill, pos);
        }
    }
}
