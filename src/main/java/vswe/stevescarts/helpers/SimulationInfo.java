package vswe.stevescarts.helpers;

import vswe.stevescarts.Constants;
import vswe.stevescarts.modules.addons.ModuleBrake;
import vswe.stevescarts.modules.addons.ModuleInvisible;
import vswe.stevescarts.modules.addons.ModuleLiquidSensors;
import vswe.stevescarts.modules.addons.ModuleShield;
import vswe.stevescarts.modules.engines.ModuleSolarBase;
import vswe.stevescarts.modules.realtimers.ModuleDynamite;
import vswe.stevescarts.modules.realtimers.ModuleShooter;
import vswe.stevescarts.modules.realtimers.ModuleShooterAdv;
import vswe.stevescarts.modules.storages.chests.ModuleChest;
import vswe.stevescarts.modules.storages.chests.ModuleInternalStorage;
import vswe.stevescarts.modules.workers.ModuleBridge;
import vswe.stevescarts.modules.workers.ModuleHydrater;
import vswe.stevescarts.modules.workers.ModuleRailer;
import vswe.stevescarts.modules.workers.ModuleTorch;
import vswe.stevescarts.modules.workers.tools.ModuleDrill;
import vswe.stevescarts.modules.workers.tools.ModuleFarmer;
import vswe.stevescarts.modules.workers.tools.ModuleWoodcutter;

import java.util.ArrayList;

public class SimulationInfo
{
    private ArrayList<DropDownMenuItem> items;
    private DropDownMenuItem itemBOOLChest;
    private DropDownMenuItem itemBOOLInvis;
    private DropDownMenuItem itemBOOLBrake;
    private DropDownMenuItem itemBOOLDrill;
    private DropDownMenuItem itemBOOLLight;
    private DropDownMenuItem itemBOOLBridge;
    private DropDownMenuItem itemBOOLFarm;
    private DropDownMenuItem itemBOOLCut;
    private DropDownMenuItem itemBOOLExplode;
    private DropDownMenuItem itemBOOLShield;
    private DropDownMenuItem itemINTLiquid;
    private DropDownMenuItem itemINTWater;
    private DropDownMenuItem itemINTFuse;
    private DropDownMenuItem itemINTRail;
    private DropDownMenuItem itemINTExplosion;
    private DropDownMenuItem itemMULTIBOOLTorch;
    private DropDownMenuItem itemMULTIBOOLPipes1;
    private DropDownMenuItem itemMULTIBOOLPipes2;
    private DropDownMenuItem itemBOOLPipe;
    private DropDownMenuItem itemINTBackground;
    public int fuse;

    public boolean getShieldActive()
    {
        return itemBOOLShield.getBOOL();
    }

    public boolean getChestActive()
    {
        return itemBOOLChest.getBOOL();
    }

    public boolean getInvisActive()
    {
        return itemBOOLInvis.getBOOL();
    }

    public boolean getBrakeActive()
    {
        return itemBOOLBrake.getBOOL();
    }

    public boolean getDrillSpinning()
    {
        return itemBOOLDrill.getBOOL();
    }

    public boolean getMaxLight()
    {
        return itemBOOLLight.getBOOL();
    }

    public boolean getNeedBridge()
    {
        return itemBOOLBridge.getBOOL();
    }

    public boolean getIsFarming()
    {
        return itemBOOLFarm.getBOOL();
    }

    public boolean getIsCutting()
    {
        return itemBOOLCut.getBOOL();
    }

    public boolean getIsPipeActive()
    {
        return itemBOOLPipe.getBOOL();
    }

    public boolean getShouldExplode()
    {
        return itemBOOLExplode.getBOOL();
    }

    public int getLiquidLight()
    {
        return itemINTLiquid.getINT();
    }

    public int getFuseLength()
    {
        return itemINTFuse.getINT() * 2;
    }

    public int getWaterLevel()
    {
        return itemINTWater.getINT();
    }

    public int getRailCount()
    {
        return itemINTRail.getINT();
    }

    public byte getTorchInfo()
    {
        return itemMULTIBOOLTorch.getMULTIBOOL();
    }

    public byte getActivePipes()
    {
        return (byte) (itemMULTIBOOLPipes1.getMULTIBOOL() << 4 | itemMULTIBOOLPipes2.getMULTIBOOL());
    }

    public int getBackground()
    {
        return itemINTBackground.getINT();
    }

    public float getExplosionSize()
    {
        return itemINTExplosion.getINT() * 2;
    }

    public ArrayList<DropDownMenuItem> getList()
    {
        return items;
    }

    public SimulationInfo()
    {
        items = new ArrayList<>();
        itemBOOLChest = new DropDownMenuItem("Chest", 0, DropDownMenuItem.VALUETYPE.BOOL, ModuleChest.class, ModuleInternalStorage.class);
        itemBOOLInvis = new DropDownMenuItem("Invisible", 1, DropDownMenuItem.VALUETYPE.BOOL, ModuleInvisible.class);
        itemBOOLBrake = new DropDownMenuItem("Brake", 2, DropDownMenuItem.VALUETYPE.BOOL, ModuleBrake.class);
        itemBOOLDrill = new DropDownMenuItem("Drill", 3, DropDownMenuItem.VALUETYPE.BOOL, ModuleDrill.class);
        itemBOOLLight = new DropDownMenuItem("Light", 4, DropDownMenuItem.VALUETYPE.BOOL, ModuleSolarBase.class);
        itemBOOLBridge = new DropDownMenuItem("Bridge", 5, DropDownMenuItem.VALUETYPE.BOOL, ModuleBridge.class);
        itemBOOLFarm = new DropDownMenuItem("Farm", 6, DropDownMenuItem.VALUETYPE.BOOL, ModuleFarmer.class);
        itemBOOLCut = new DropDownMenuItem("Cutting", 7, DropDownMenuItem.VALUETYPE.BOOL, ModuleWoodcutter.class);
        (itemINTLiquid = new DropDownMenuItem("Liquid", 8, DropDownMenuItem.VALUETYPE.INT, ModuleLiquidSensors.class)).setINTLimit(1, 3);
        (itemINTWater = new DropDownMenuItem("Water", 9, DropDownMenuItem.VALUETYPE.INT, ModuleHydrater.class)).setINTLimit(0, 4);
        (itemINTFuse = new DropDownMenuItem("Fuse", 10, DropDownMenuItem.VALUETYPE.INT, ModuleDynamite.class)).setINTLimit(1, 75);
        itemINTFuse.setINT(35);
        (itemINTRail = new DropDownMenuItem("Rails", 11, DropDownMenuItem.VALUETYPE.INT, ModuleRailer.class)).setINTLimit(0, 6);
        (itemINTExplosion = new DropDownMenuItem("Explosives", 12, DropDownMenuItem.VALUETYPE.INT, ModuleDynamite.class)).setINTLimit(4, 54);
        itemBOOLExplode = new DropDownMenuItem("Explode", 13, DropDownMenuItem.VALUETYPE.BOOL, ModuleDynamite.class);
        (itemBOOLShield = new DropDownMenuItem("Shield", 14, DropDownMenuItem.VALUETYPE.BOOL, ModuleShield.class)).setBOOL(true);
        (itemMULTIBOOLTorch = new DropDownMenuItem("Torches", 15, DropDownMenuItem.VALUETYPE.MULTIBOOL, ModuleTorch.class)).setMULTIBOOLCount(3);
        (itemMULTIBOOLPipes1 = new DropDownMenuItem("Pipes", 16, DropDownMenuItem.VALUETYPE.MULTIBOOL, ModuleShooter.class, ModuleShooterAdv.class)).setMULTIBOOLCount(4);
        (itemMULTIBOOLPipes2 = new DropDownMenuItem("Pipes", 16, DropDownMenuItem.VALUETYPE.MULTIBOOL, ModuleShooter.class, ModuleShooterAdv.class)).setMULTIBOOLCount(4);
        itemBOOLPipe = new DropDownMenuItem("Pipe", 17, DropDownMenuItem.VALUETYPE.BOOL, ModuleShooterAdv.class);
        (itemINTBackground = new DropDownMenuItem("Background", 18, DropDownMenuItem.VALUETYPE.INT, null)).setINTLimit(Constants.hasGreenScreen ? 0 : 1, 3);
        itemINTBackground.setINT(1);
        if (Constants.hasGreenScreen)
        {
            items.add(itemINTBackground);
        }
        items.add(itemBOOLChest);
        items.add(itemBOOLInvis);
        items.add(itemBOOLBrake);
        items.add(itemBOOLDrill);
        items.add(itemBOOLLight);
        items.add(itemBOOLBridge);
        items.add(itemBOOLFarm);
        items.add(itemBOOLCut);
        items.add(itemINTLiquid);
        items.add(itemINTFuse);
        items.add(itemINTRail);
        items.add(itemINTExplosion);
        items.add(itemBOOLExplode);
        items.add(itemBOOLShield);
        items.add(itemMULTIBOOLTorch);
        items.add(itemMULTIBOOLPipes1);
        items.add(itemMULTIBOOLPipes2);
        items.add(itemBOOLPipe);
    }
}
