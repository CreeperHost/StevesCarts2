package vswe.stevescarts.computer;

import vswe.stevescarts.client.guis.buttons.ButtonBase;
import vswe.stevescarts.client.guis.buttons.ButtonInfoType;
import vswe.stevescarts.entitys.EntityMinecartModular;
import vswe.stevescarts.modules.ModuleBase;
import vswe.stevescarts.modules.addons.*;
import vswe.stevescarts.modules.realtimers.ModuleDynamite;
import vswe.stevescarts.modules.realtimers.ModuleShooter;
import vswe.stevescarts.modules.realtimers.ModuleShooterAdv;
import vswe.stevescarts.modules.workers.ModuleComputer;
import vswe.stevescarts.modules.workers.ModuleTorch;
import vswe.stevescarts.modules.workers.tools.ModuleDrill;

import java.util.Collection;
import java.util.HashMap;

public class ComputerInfo
{
    private static HashMap<Byte, ComputerInfo> infos;
    private Class<? extends ModuleBase> moduleClass;
    private byte id;
    private String name;
    private int texture;

    public static HashMap<Byte, ComputerInfo> getMap()
    {
        return ComputerInfo.infos;
    }

    public static Collection<ComputerInfo> getList()
    {
        return ComputerInfo.infos.values();
    }

    public static void createButtons(final EntityMinecartModular cart, final ModuleComputer assembly)
    {
        for (final ComputerInfo info : getList())
        {
            if (info.isInfoValid(cart))
            {
                new ButtonInfoType(assembly, ButtonBase.LOCATION.TASK, info.id);
            }
        }
    }

    private static int processColor(final int val)
    {
        if (val == 255)
        {
            return 64;
        }
        return val / 4;
    }

    private static byte clamp(final byte val, final int min, final int max)
    {
        return (byte) Math.max((byte) min, (byte) Math.min(val, (byte) max));
    }

    public ComputerInfo(final int id, final String name, final int texture, final Class<? extends ModuleBase> moduleClass)
    {
        this.moduleClass = moduleClass;
        this.name = name;
        this.id = (byte) id;
        this.texture = texture;
        ComputerInfo.infos.put(this.id, this);
    }

    public boolean isInfoValid(final EntityMinecartModular cart)
    {
        for (final ModuleBase module : cart.getModules())
        {
            if (moduleClass.isAssignableFrom(module.getClass()) && isValid(module))
            {
                return true;
            }
        }
        return false;
    }

    public String getName()
    {
        return name;
    }

    public int getTexture()
    {
        return texture;
    }

    public void getHandler(final EntityMinecartModular cart, final ComputerVar var)
    {
        for (final ModuleBase module : cart.getModules())
        {
            if (moduleClass.isAssignableFrom(module.getClass()) && isValid(module))
            {
                var.setByteValue(get(module));
                break;
            }
        }
    }

    protected int get(final ModuleBase module)
    {
        return 0;
    }

    protected boolean isValid(final ModuleBase module)
    {
        return true;
    }

    static
    {
        ComputerInfo.infos = new HashMap<>();
        new ComputerInfo(1, "Light threshold [0-15]", 84, ModuleTorch.class)
        {
            @Override
            protected int get(final ModuleBase module)
            {
                return ((ModuleTorch) module).getThreshold();
            }
        };
        new ComputerInfo(2, "Light level [0-15]", 85, ModuleTorch.class)
        {
            @Override
            protected int get(final ModuleBase module)
            {
                return ((ModuleTorch) module).getLightLevel();
            }
        };
        new ComputerInfo(3, "Shield [0-1]", 86, ModuleShield.class)
        {
            @Override
            protected int get(final ModuleBase module)
            {
                return ((ModuleShield) module).isActive(0) ? 1 : 0;
            }
        };
        new ComputerInfo(4, "Drill [0-1]", 87, ModuleDrill.class)
        {
            @Override
            protected int get(final ModuleBase module)
            {
                return ((ModuleDrill) module).isActive(0) ? 1 : 0;
            }
        };
        new ComputerInfo(5, "Invisibility core [0-1]", 88, ModuleInvisible.class)
        {
            @Override
            protected int get(final ModuleBase module)
            {
                return ((ModuleInvisible) module).isActive(0) ? 1 : 0;
            }
        };
        new ComputerInfo(6, "Chunk loader [0-1]", 89, ModuleChunkLoader.class)
        {
            @Override
            protected int get(final ModuleBase module)
            {
                return ((ModuleChunkLoader) module).isActive(0) ? 1 : 0;
            }
        };
        new ComputerInfo(7, "Fuse Length [2-127]", 90, ModuleDynamite.class)
        {
            @Override
            protected int get(final ModuleBase module)
            {
                return clamp((byte) ((ModuleDynamite) module).getFuseLength(), 2, 127);
            }
        };
        new ComputerInfo(8, "Active Pipes", 91, ModuleShooter.class)
        {
            @Override
            protected int get(final ModuleBase module)
            {
                return ((ModuleShooter) module).getActivePipes();
            }

            @Override
            protected boolean isValid(final ModuleBase module)
            {
                return !(module instanceof ModuleShooterAdv);
            }
        };
        new ComputerInfo(9, "Selected Target", 92, ModuleShooterAdv.class)
        {
            @Override
            protected int get(final ModuleBase module)
            {
                return ((ModuleShooterAdv) module).selectedOptions();
            }
        };
        new ComputerInfo(10, "Red [0-64]", 93, ModuleColorizer.class)
        {
            @Override
            protected int get(final ModuleBase module)
            {
                return processColor(((ModuleColorizer) module).getColorVal(0));
            }
        };
        new ComputerInfo(11, "Green [0-64]", 94, ModuleColorizer.class)
        {
            @Override
            protected int get(final ModuleBase module)
            {
                return processColor(((ModuleColorizer) module).getColorVal(1));
            }
        };
        new ComputerInfo(12, "Blue [0-64]", 95, ModuleColorizer.class)
        {
            @Override
            protected int get(final ModuleBase module)
            {
                return processColor(((ModuleColorizer) module).getColorVal(2));
            }
        };
        new ComputerInfo(13, "Y target [-128-127]", 96, ModuleHeightControl.class)
        {
            @Override
            protected int get(final ModuleBase module)
            {
                return module.getYTarget() - 128;
            }
        };
        new ComputerInfo(14, "Y level [-128-127]", 97, ModuleHeightControl.class)
        {
            @Override
            protected int get(final ModuleBase module)
            {
                return clamp((byte) (module.getCart().y() - 128.0), -128, 127);
            }
        };
    }
}
