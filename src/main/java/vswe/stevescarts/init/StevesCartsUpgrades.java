package vswe.stevescarts.init;

import net.minecraft.resources.Identifier;
import vswe.stevescarts.Constants;
import vswe.stevescarts.api.StevesCartsAPI;
import vswe.stevescarts.api.upgrades.AssemblerUpgrade;
import vswe.stevescarts.api.upgrades.BaseUpgradeEffect;
import vswe.stevescarts.upgrades.*;

public final class StevesCartsUpgrades {
    public static AssemblerUpgrade BATTERIES;
    public static AssemblerUpgrade POWER_CRYSTAL;
    public static AssemblerUpgrade MODULE_KNOWLEDGE;
    public static AssemblerUpgrade INDUSTRIAL_ESPIONAGE;
    public static AssemblerUpgrade EXPERIENCED_ASSEMBLER;
    public static AssemblerUpgrade NEW_ERA;
    public static AssemblerUpgrade COTWO_FRIENDLY;
    public static AssemblerUpgrade GENERIC_ENGINE;
    public static AssemblerUpgrade MODULE_INPUT;
    public static AssemblerUpgrade PRODUCTION_LINE;
    public static AssemblerUpgrade CART_DEPLOYER;
    public static AssemblerUpgrade CART_MODIFIER;
    public static AssemblerUpgrade CART_CRANE;
    public static AssemblerUpgrade REDSTONE_CONTROL;
    public static AssemblerUpgrade CREATIVE_MODE;
    public static AssemblerUpgrade QUICK_DEMOLISHER;
    public static AssemblerUpgrade ENTROPY;
    public static AssemblerUpgrade MANAGER_BRIDGE;
    public static AssemblerUpgrade THERMAL_ENGINE;
    public static AssemblerUpgrade SOLAR_PANEL;

    private StevesCartsUpgrades() {
    }

    public static void init() {
        BATTERIES = register("upgrade_batteries", 0, new FuelCapacity(5000), new Recharger(40));
        POWER_CRYSTAL = register("upgrade_power_crystal", 1, new FuelCapacity(15000), new Recharger(150));
        MODULE_KNOWLEDGE = register("upgrade_module_knowledge", 2, new TimeFlat(-750), new TimeFlatCart(-5000), new WorkEfficiency(-0.01F));
        INDUSTRIAL_ESPIONAGE = register("upgrade_industrial_espionage", 3, new TimeFlat(-2500), new TimeFlatCart(-14000), new WorkEfficiency(-0.01F));
        EXPERIENCED_ASSEMBLER = register("upgrade_experienced_assembler", 4, new WorkEfficiency(0.1F), new FuelCost(0.3F));
        NEW_ERA = register("upgrade_new_era", 5, new WorkEfficiency(1.0F), new FuelCost(30.0F));
        COTWO_FRIENDLY = register("upgrade_cotwo_friendly", 6, new FuelCost(-0.15F));
        GENERIC_ENGINE = register("upgrade_generic_engine", 7, new CombustionFuel(), new FuelCost(0.05F));
        MODULE_INPUT = register("upgrade_module_input", 8, new InputChest(7, 3));
        PRODUCTION_LINE = register("upgrade_production_line", 9, new Blueprint());
        CART_DEPLOYER = register("upgrade_cart_deployer", 10, new Deployer());
        CART_MODIFIER = register("upgrade_cart_modifier", 11, new Disassemble());
        CART_CRANE = register("upgrade_cart_crane", 12, new Transposer());
        REDSTONE_CONTROL = register("upgrade_redstone_control", 13, new Redstone());
        CREATIVE_MODE = register("upgrade_creative_mode", 14, new WorkEfficiency(10000.0F), new FuelCost(-1.0F));
        QUICK_DEMOLISHER = register("upgrade_quick_demolisher", 15, new TimeFlatRemoved(-8000));
        ENTROPY = register("upgrade_entropy", 16, new TimeFlatRemoved(-32000), new TimeFlat(3000));
        MANAGER_BRIDGE = register("upgrade_manager_bridge", 17, new Manager(), new TimeFlatCart(200));
        THERMAL_ENGINE = register("upgrade_thermal_engine", 18, new ThermalFuel(), new FuelCost(0.05F));
        SOLAR_PANEL = register("upgrade_solar_panel", 19, new Solar());
    }

    private static AssemblerUpgrade register(String path, int legacyId, BaseUpgradeEffect... effects) {
        AssemblerUpgrade.Builder builder = AssemblerUpgrade.builder(
                Identifier.fromNamespaceAndPath(Constants.MOD_ID, path)).legacyId(legacyId);
        for (BaseUpgradeEffect effect : effects) {
            builder.addEffect(effect);
        }
        return StevesCartsAPI.registerAssemblerUpgrade(builder.build());
    }
}
