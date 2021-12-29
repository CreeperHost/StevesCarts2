package vswe.stevescarts.upgrades;

import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import vswe.stevescarts.blocks.tileentities.TileEntityUpgrade;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

public class AssemblerUpgrade
{
    private static HashMap<Byte, AssemblerUpgrade> upgrades;
    private byte id;
    private int sideTexture;
    private String name;
    private ArrayList<BaseEffect> effects;
    private String icon;

    public static HashMap<Byte, AssemblerUpgrade> getUpgrades()
    {
        return AssemblerUpgrade.upgrades;
    }

    public static Collection<AssemblerUpgrade> getUpgradesList()
    {
        return AssemblerUpgrade.upgrades.values();
    }

    public static AssemblerUpgrade getUpgrade(final int id)
    {
        return AssemblerUpgrade.upgrades.get((byte) id);
    }

    public static void init()
    {
        new AssemblerUpgrade(0, "Batteries").addEffect(new FuelCapacity(5000)).addEffect(new Recharger(40));
        new AssemblerUpgrade(1, "Power Crystal").addEffect(new FuelCapacity(15000)).addEffect(new Recharger(150));
        new AssemblerUpgrade(2, "Module knowledge").addEffect(new TimeFlat(-750)).addEffect(new TimeFlatCart(-5000)).addEffect(new WorkEfficiency(-0.01f));
        new AssemblerUpgrade(3, "Industrial espionage").addEffect(new TimeFlat(-2500)).addEffect(new TimeFlatCart(-14000)).addEffect(new WorkEfficiency(-0.01f));
        new AssemblerUpgrade(4, "Experienced assembler").addEffect(new WorkEfficiency(0.1f)).addEffect(new FuelCost(0.3f));
        new AssemblerUpgrade(5, "New Era").addEffect(new WorkEfficiency(1.0f)).addEffect(new FuelCost(30.0f));
        new AssemblerUpgrade(6, "CO2 friendly").addEffect(new FuelCost(-0.15f));
        new AssemblerUpgrade(7, "Generic engine").addEffect(new CombustionFuel()).addEffect(new FuelCost(0.05f));
        new AssemblerUpgrade(8, "Module input", 1).addEffect(new InputChest(7, 3));
        new AssemblerUpgrade(9, "Production line").addEffect(new Blueprint());
        new AssemblerUpgrade(10, "Cart Deployer").addEffect(new Deployer());
        new AssemblerUpgrade(11, "Cart Modifier").addEffect(new Disassemble());
        new AssemblerUpgrade(12, "Cart Crane").addEffect(new Transposer());
        new AssemblerUpgrade(13, "Redstone Control").addEffect(new Redstone());
        new AssemblerUpgrade(14, "Creative Mode").addEffect(new WorkEfficiency(10000.0f)).addEffect(new FuelCost(-1.0f));
        new AssemblerUpgrade(15, "Quick Demolisher").addEffect(new TimeFlatRemoved(-8000));
        new AssemblerUpgrade(16, "Entropy").addEffect(new TimeFlatRemoved(-32000)).addEffect(new TimeFlat(3000));
        new AssemblerUpgrade(17, "Manager Bridge").addEffect(new Manager()).addEffect(new TimeFlatCart(200));
        new AssemblerUpgrade(18, "Thermal Engine").addEffect(new ThermalFuel()).addEffect(new FuelCost(0.05f));
        new AssemblerUpgrade(19, "Solar Panel").addEffect(new Solar());
    }

    public AssemblerUpgrade(final int id, final String name)
    {
        this(id, name, 0);
    }

    public AssemblerUpgrade(final int id, final String name, final int sideTexture)
    {
        this.id = (byte) id;
        this.sideTexture = sideTexture;
        this.name = name;
        effects = new ArrayList<>();
        AssemblerUpgrade.upgrades.put(this.id, this);
    }

    public byte getId()
    {
        return id;
    }

    public String getName()
    {
        return I18n.get("block.stevescarts.upgrade_" + getRawName());
    }

    public AssemblerUpgrade addEffect(final BaseEffect effect)
    {
        effects.add(effect);
        return this;
    }

    @Nonnull
    protected ItemStack getItemStack()
    {
        return getItemStack(1);
    }

    @Nonnull
    protected ItemStack getItemStack(final int count)
    {
        return ItemStack.EMPTY; //new ItemStack(ModBlocks.UPGRADE.get().getBlock(), count);
    }

    public ArrayList<BaseEffect> getEffects()
    {
        return effects;
    }

    public boolean useStandardInterface()
    {
        return getInterfaceEffect() == null;
    }

    public int getInventorySize()
    {
        final InventoryEffect inv = getInventoryEffect();
        if (inv != null)
        {
            return inv.getInventorySize();
        }
        return 0;
    }

    public InterfaceEffect getInterfaceEffect()
    {
        for (final BaseEffect effect : effects)
        {
            if (effect instanceof InterfaceEffect)
            {
                return (InterfaceEffect) effect;
            }
        }
        return null;
    }

    public InventoryEffect getInventoryEffect()
    {
        for (final BaseEffect effect : effects)
        {
            if (effect instanceof InventoryEffect)
            {
                return (InventoryEffect) effect;
            }
        }
        return null;
    }

    public TankEffect getTankEffect()
    {
        for (final BaseEffect effect : effects)
        {
            if (effect instanceof TankEffect)
            {
                return (TankEffect) effect;
            }
        }
        return null;
    }

    public void init(final TileEntityUpgrade upgrade)
    {
        for (final BaseEffect effect : effects)
        {
            effect.init(upgrade);
        }
    }

    public void load(final TileEntityUpgrade upgrade, final CompoundNBT compound)
    {
        for (final BaseEffect effect : effects)
        {
            effect.load(upgrade, compound);
        }
    }

    public void save(final TileEntityUpgrade upgrade, final CompoundNBT compound)
    {
        for (final BaseEffect effect : effects)
        {
            effect.save(upgrade, compound);
        }
    }

    public void update(final TileEntityUpgrade upgrade)
    {
        for (final BaseEffect effect : effects)
        {
            effect.update(upgrade);
        }
    }

    public void removed(final TileEntityUpgrade upgrade)
    {
        for (final BaseEffect effect : effects)
        {
            effect.removed(upgrade);
        }
    }

    public String getRawName()
    {
        return name.replace(":", "").replace(" ", "_").toLowerCase();
    }

    public String getIcon()
    {
        return icon;
    }

    public void setIcon(String icon)
    {
        this.icon = icon;
    }

    static
    {
        AssemblerUpgrade.upgrades = new HashMap<>();
    }
}
