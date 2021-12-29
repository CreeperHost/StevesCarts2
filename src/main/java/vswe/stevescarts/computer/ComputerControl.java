package vswe.stevescarts.computer;

import vswe.stevescarts.client.guis.buttons.ButtonBase;
import vswe.stevescarts.client.guis.buttons.ButtonControlType;
import vswe.stevescarts.entitys.EntityMinecartModular;
import vswe.stevescarts.modules.ModuleBase;
import vswe.stevescarts.modules.addons.*;
import vswe.stevescarts.modules.realtimers.ModuleDynamite;
import vswe.stevescarts.modules.realtimers.ModuleFirework;
import vswe.stevescarts.modules.realtimers.ModuleShooter;
import vswe.stevescarts.modules.realtimers.ModuleShooterAdv;
import vswe.stevescarts.modules.workers.ModuleComputer;
import vswe.stevescarts.modules.workers.ModuleTorch;
import vswe.stevescarts.modules.workers.tools.ModuleDrill;

import java.util.Collection;
import java.util.HashMap;

public class ComputerControl
{
    private static HashMap<Byte, ComputerControl> controls;
    private Class<? extends ModuleBase> moduleClass;
    private byte id;
    private String name;
    private int texture;

    public static HashMap<Byte, ComputerControl> getMap()
    {
        return ComputerControl.controls;
    }

    public static Collection<ComputerControl> getList()
    {
        return ComputerControl.controls.values();
    }

    public static void createButtons(final EntityMinecartModular cart, final ModuleComputer assembly)
    {
        for (final ComputerControl control : getList())
        {
            if (control.isControlValid(cart))
            {
                new ButtonControlType(assembly, ButtonBase.LOCATION.TASK, control.id);
            }
        }
    }

    private byte clamp(final byte val, final int min, final int max)
    {
        return (byte) Math.max((byte) min, (byte) Math.min(val, (byte) max));
    }

    public ComputerControl(final int id, final String name, final int texture, final Class<? extends ModuleBase> moduleClass)
    {
        this.moduleClass = moduleClass;
        this.name = name;
        this.id = (byte) id;
        this.texture = texture;
        ComputerControl.controls.put(this.id, this);
    }

    public boolean isControlValid(final EntityMinecartModular cart)
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

    public void runHandler(final EntityMinecartModular cart, final byte val)
    {
        for (final ModuleBase module : cart.getModules())
        {
            if (moduleClass.isAssignableFrom(module.getClass()) && isValid(module))
            {
                run(module, clamp(val, (byte) getIntegerMin(), (byte) getIntegerMax()));
                break;
            }
        }
    }

    public int getIntegerMin()
    {
        if (isBoolean())
        {
            return 0;
        }
        return getMin();
    }

    public int getIntegerMax()
    {
        if (isBoolean())
        {
            return 1;
        }
        return getMax();
    }

    public boolean useIntegerOfSize(final int size)
    {
        return !isBoolean() || size <= 1;
    }

    protected boolean isBoolean()
    {
        return false;
    }

    protected boolean isActivator()
    {
        return false;
    }

    protected void run(final ModuleBase module, final byte val)
    {
    }

    protected int getMin()
    {
        return -127;
    }

    protected int getMax()
    {
        return 128;
    }

    protected boolean isValid(final ModuleBase module)
    {
        return true;
    }

    static
    {
        ComputerControl.controls = new HashMap<>();
        new ComputerControl(1, "Light threshold [0-15]", 69, ModuleTorch.class)
        {
            @Override
            protected void run(final ModuleBase module, final byte val)
            {
                ((ModuleTorch) module).setThreshold(val);
            }

            @Override
            protected int getMin()
            {
                return 0;
            }

            @Override
            protected int getMax()
            {
                return 15;
            }
        };
        new ComputerControl(2, "Shield", 70, ModuleShield.class)
        {
            @Override
            protected void run(final ModuleBase module, final byte val)
            {
                ((ModuleShield) module).setShieldStatus(val != 0);
            }

            @Override
            protected boolean isBoolean()
            {
                return true;
            }
        };
        new ComputerControl(3, "Drill", 71, ModuleDrill.class)
        {
            @Override
            protected void run(final ModuleBase module, final byte val)
            {
                ((ModuleDrill) module).setDrillEnabled(val != 0);
            }

            @Override
            protected boolean isBoolean()
            {
                return true;
            }
        };
        new ComputerControl(4, "Invisibility Core", 72, ModuleInvisible.class)
        {
            @Override
            protected void run(final ModuleBase module, final byte val)
            {
                ((ModuleInvisible) module).setIsVisible(val == 0);
            }

            @Override
            protected boolean isBoolean()
            {
                return true;
            }
        };
        new ComputerControl(5, "Chunk loader", 73, ModuleChunkLoader.class)
        {
            @Override
            protected void run(final ModuleBase module, final byte val)
            {
                ((ModuleChunkLoader) module).setChunkLoading(val != 0);
            }

            @Override
            protected boolean isBoolean()
            {
                return true;
            }
        };
        new ComputerControl(6, "Fuse length [2-127]", 74, ModuleDynamite.class)
        {
            @Override
            protected void run(final ModuleBase module, final byte val)
            {
                ((ModuleDynamite) module).setFuseLength(val);
            }

            @Override
            protected int getMin()
            {
                return 2;
            }
        };
        new ComputerControl(7, "Prime", 75, ModuleDynamite.class)
        {
            @Override
            protected void run(final ModuleBase module, final byte val)
            {
                ((ModuleDynamite) module).prime();
            }

            @Override
            protected boolean isActivator()
            {
                return true;
            }
        };
        new ComputerControl(8, "Active pipes", 76, ModuleShooter.class)
        {
            @Override
            protected void run(final ModuleBase module, final byte val)
            {
                ((ModuleShooter) module).setActivePipes(val);
            }

            @Override
            protected boolean isValid(final ModuleBase module)
            {
                return !(module instanceof ModuleShooterAdv);
            }
        };
        new ComputerControl(9, "Selected targets", 77, ModuleShooterAdv.class)
        {
            @Override
            protected void run(final ModuleBase module, final byte val)
            {
                ((ModuleShooterAdv) module).setOptions(val);
            }
        };
        new ComputerControl(10, "Fire", 78, ModuleFirework.class)
        {
            @Override
            protected void run(final ModuleBase module, final byte val)
            {
                ((ModuleFirework) module).fire();
            }

            @Override
            protected boolean isActivator()
            {
                return true;
            }
        };
        new ComputerControl(11, "Red [0-64]", 79, ModuleColorizer.class)
        {
            @Override
            protected void run(final ModuleBase module, final byte val)
            {
                ((ModuleColorizer) module).setColorVal(0, (byte) Math.min(val * 4, 255));
            }

            @Override
            protected int getMin()
            {
                return 0;
            }

            @Override
            protected int getMax()
            {
                return 64;
            }
        };
        new ComputerControl(12, "Green [0-64]", 80, ModuleColorizer.class)
        {
            @Override
            protected void run(final ModuleBase module, final byte val)
            {
                ((ModuleColorizer) module).setColorVal(1, (byte) Math.min(val * 4, 255));
            }

            @Override
            protected int getMin()
            {
                return 0;
            }

            @Override
            protected int getMax()
            {
                return 64;
            }
        };
        new ComputerControl(13, "Blue [0-64]", 81, ModuleColorizer.class)
        {
            @Override
            protected void run(final ModuleBase module, final byte val)
            {
                ((ModuleColorizer) module).setColorVal(2, (byte) Math.min(val * 4, 255));
            }

            @Override
            protected int getMin()
            {
                return 0;
            }

            @Override
            protected int getMax()
            {
                return 64;
            }
        };
        new ComputerControl(14, "Y target [-128-127]", 85, ModuleHeightControl.class)
        {
            @Override
            protected void run(final ModuleBase module, final byte val)
            {
                ((ModuleHeightControl) module).setYTarget(val + 128);
            }
        };
    }
}
