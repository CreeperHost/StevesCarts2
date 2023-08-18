package vswe.stevescarts.modules.addons;

import net.minecraft.core.BlockPos;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FallingBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.fluids.IFluidBlock;
import vswe.stevescarts.api.modules.ModuleBase;
import vswe.stevescarts.api.modules.template.ModuleAddon;
import vswe.stevescarts.entities.EntityMinecartModular;
import vswe.stevescarts.modules.workers.ModuleLiquidDrainer;
import vswe.stevescarts.modules.workers.tools.ModuleDrill;

public class ModuleLiquidSensors extends ModuleAddon
{
    private float sensorRotation;
    private int activetime;
    private int mult;
    private EntityDataAccessor<Byte> SENSOR_INFO;

    public ModuleLiquidSensors(final EntityMinecartModular cart)
    {
        super(cart);
        activetime = -1;
        mult = 1;
    }

    @Override
    public void update()
    {
        super.update();
        if (isDrillSpinning())
        {
            sensorRotation += 0.05f * mult;
            if ((mult == 1 && sensorRotation > 0.7853981633974483) || (mult == -1 && sensorRotation < -0.7853981633974483))
            {
                mult *= -1;
            }
        }
        else
        {
            if (sensorRotation != 0.0f)
            {
                if (sensorRotation > 0.0f)
                {
                    sensorRotation -= 0.05f;
                    if (sensorRotation < 0.0f)
                    {
                        sensorRotation = 0.0f;
                    }
                }
                else
                {
                    sensorRotation += 0.05f;
                    if (sensorRotation > 0.0f)
                    {
                        sensorRotation = 0.0f;
                    }
                }
            }
            if (activetime >= 0)
            {
                ++activetime;
                if (activetime >= 10)
                {
                    setLight(1);
                    activetime = -1;
                }
            }
        }
    }

    @Override
    public int numberOfDataWatchers()
    {
        return 1;
    }

    @Override
    public void initDw()
    {
        SENSOR_INFO = createDw(EntityDataSerializers.BYTE);
        registerDw(SENSOR_INFO, (byte) 1);
    }

    private void activateLight(final int light)
    {
        if (getLight() == 3 && light == 2)
        {
            return;
        }
        setLight(light);
        activetime = 0;
    }

    public void getInfoFromDrill(byte data)
    {
        final byte light = (byte) (data & 0x3);
        if (light != 1)
        {
            activateLight(light);
        }
        data &= 0b11111111111111111111111111111100;
        data |= (byte) getLight();
        setSensorInfo(data);
    }

    private void setLight(final int val)
    {
        if (isPlaceholder())
        {
            return;
        }
        byte data = getDw(SENSOR_INFO);
        data &= 0xFFFFFFFC;
        data |= (byte) val;
        setSensorInfo(data);
    }

    private void setSensorInfo(byte val)
    {
        if (isPlaceholder())
        {
            return;
        }
        updateDw(SENSOR_INFO, val);
    }

    public int getLight()
    {
        if (isPlaceholder())
        {
            return getSimInfo().getLiquidLight();
        }
        return getDw(SENSOR_INFO) & 0b11;
    }

    protected boolean isDrillSpinning()
    {
        if (isPlaceholder())
        {
            return getSimInfo().getDrillSpinning();
        }
        return (getDw(SENSOR_INFO) & 0x4) != 0x0;
    }

    public float getSensorRotation()
    {
        return sensorRotation;
    }

    public boolean isDangerous(final ModuleDrill drill, BlockPos pos, boolean isUp)
    {
        final Block block = getCart().level().getBlockState(pos).getBlock();
        if (block == Blocks.LAVA)
        {
            handleLiquid(drill, pos);
            return true;
        }
        if (block == Blocks.WATER)
        {
            handleLiquid(drill, pos);
            return true;
        }
        if (block != null && block instanceof IFluidBlock)
        {
            handleLiquid(drill, pos);
            return true;
        }
        final boolean isWater = block == Blocks.WATER || block == Blocks.WATER || block == Blocks.ICE;
        final boolean isLava = block == Blocks.LAVA || block == Blocks.LAVA;
        final boolean isOther = block != null && block instanceof IFluidBlock;
        final boolean isLiquid = isWater || isLava || isOther;
        if (!isLiquid)
        {
            if (isUp)
            {
                final boolean isFalling = block instanceof FallingBlock;
                if (isFalling)
                {
                    return isDangerous(drill, pos.offset(0, 1, 0), true) || isDangerous(drill, pos.offset(1, 0, 0), false) || isDangerous(drill, pos.offset(-1, 0, 0), false) || isDangerous(drill, pos.offset(0, 0, 1), false) || isDangerous(drill, pos.offset(0, 0, -1), false);
                }
            }
            return false;
        }
        if (isUp)
        {
            handleLiquid(drill, pos);
            return true;
        }
        BlockState state = getCart().level().getBlockState(pos);
        //TODO
        //		int m = state.getBlock().getMetaFromState(state);
        //		if ((m & 0x8) == 0x8) {
        //			if (block.getBlockFaceShape(getCart().world, state, pos.down(), EnumFacing.UP) == BlockFaceShape.SOLID) {
        //				handleLiquid(drill, pos);
        //				return true;
        //			}
        //			return false;
        //		}
        //		else {
        //			if (isWater && (m & 0x7) == 0x7) {
        //				return false;
        //			}
        //			if (isLava && (m & 0x7) == 0x7 && !getCart().world.provider.isSkyColored()) {
        //				return false;
        //			}
        //			if (isLava && (m & 0x7) == 0x6) {
        //				return false;
        //			}
        //			handleLiquid(drill, pos);
        //			return true;
        //		}
        return false;
    }

    private void handleLiquid(final ModuleDrill drill, BlockPos pos)
    {
        ModuleLiquidDrainer liquiddrainer = null;
        for (final ModuleBase module : getCart().getModules())
        {
            if (module instanceof ModuleLiquidDrainer)
            {
                liquiddrainer = (ModuleLiquidDrainer) module;
                break;
            }
        }
        if (liquiddrainer != null)
        {
            liquiddrainer.handleLiquid(drill, pos);
        }
    }
}
