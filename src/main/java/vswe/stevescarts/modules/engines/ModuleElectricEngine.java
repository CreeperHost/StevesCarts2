//package vswe.stevescarts.modules.engines;
//
//import com.mojang.blaze3d.matrix.MatrixStack;
//import dev.ftb.mods.ftbic.util.EnergyItemHandler;
//import net.minecraft.network.datasync.DataParameter;
//import net.minecraft.network.datasync.DataSerializers;
//import net.neoforged.api.distmarker.Dist;
//import net.neoforged.api.distmarker.OnlyIn;
//import vswe.stevescarts.client.guis.GuiMinecart;
//import vswe.stevescarts.compat.ftbic.SlotElectricEngine;
//import vswe.stevescarts.api.slots.SlotBase;
//import vswe.stevescarts.entitys.EntityMinecartModular;
//import vswe.stevescarts.helpers.Localization;
//
//public class ModuleElectricEngine extends ModuleEngine
//{
//    private DataParameter<Integer> PRIORITY;
//    private int maxStored = 50000;
//
//    public ModuleElectricEngine(EntityMinecartModular cart) {
//        super(cart);
//    }
//
//    @Override
//    protected DataParameter<Integer> getPriorityDw() {
//        return PRIORITY;
//    }
//
//    @Override
//    public void initDw() {
//        PRIORITY = createDw(DataSerializers.INT);
//        super.initDw();
//    }
//
//    @Override
//    protected void loadFuel()
//    {
//        final int consumption = getCart().getConsumption(true) * 2;
//        if (getFuelLevel() <= consumption)
//        {
//            int i = 0;
//            while (i < getInventorySize())
//            {
//                if (getFuelLevel() < maxStored)
//                {
//                    if (getStack(i).isEmpty())
//                    {
//                        break;
//                    }
//                    if(getStack(i).getItem() instanceof EnergyItemHandler)
//                    {
//                        EnergyItemHandler energyItemHandler = (EnergyItemHandler) getStack(i).getItem();
//                        if(energyItemHandler.canExtractEnergy())
//                        {
//                            double amount = energyItemHandler.extractEnergy(getStack(i), 100, false);
//                            setFuelLevel((getFuelLevel() + (int) amount) * 5);
//                        }
//                    }
//                    break;
//                }
//                else
//                {
//                    ++i;
//                }
//            }
//        }
//    }
//
//    @Override
//    public int getTotalFuel()
//    {
//        int total = getFuelLevel();
//        total += getFuelFromBatteries();
//        return total;
//    }
//
//    public double getFuelFromBatteries()
//    {
//        double totalfuel = 0;
//
//        for (int i = 0; i < getInventorySize(); ++i)
//        {
//            if (!getStack(i).isEmpty())
//            {
//                if(getStack(i).getItem() instanceof EnergyItemHandler)
//                {
//                    EnergyItemHandler energyItemHandler = (EnergyItemHandler) getStack(i).getItem();
//                    totalfuel += energyItemHandler.getEnergy(getStack(i));
//                }
//            }
//        }
//        return totalfuel;
//    }
//
//    @Override
//    public float[] getGuiBarColor() {
//        return new float[]{0.0f, 0.0f, 0.0f};
//    }
//
//    @Override
//    protected SlotBase getSlot(final int slotId, final int x, final int y)
//    {
//        return new SlotElectricEngine(getCart(), slotId, 8 + x * 18, 23 + 18 * y);
//    }
//
//    @OnlyIn(Dist.CLIENT)
//    @Override
//    public void drawForeground(MatrixStack matrixStack, GuiMinecart gui)
//    {
//        drawString(matrixStack, gui, "Electric Engine", 8, 6, 4210752);
//        String strfuel = Localization.MODULES.ENGINES.NO_FUEL.translate();
//        if (getFuelLevel() > 0)
//        {
//            strfuel = "Fuel: " + getFuelLevel();
//        }
//        drawString(matrixStack, gui, strfuel, 8, 48, 4210752);
//    }
//}
