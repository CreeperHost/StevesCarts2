package vswe.stevescarts.modules.workers.tools;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;
import vswe.stevescarts.SCConfig;
import vswe.stevescarts.entities.EntityMinecartModular;

public class ModuleWoodcutterNetherite extends ModuleWoodcutter
{
    public ModuleWoodcutterNetherite(final EntityMinecartModular cart)
    {
        super(cart);
    }

    @Override
    public int getPercentageDropChance()
    {
        return 90;
    }

    @Override
    public int getMaxDurability()
    {
        return SCConfig.woodcutterDurabilityNetherite.get();
    }

    @Override
    public ResourceLocation getRepairItem() {
        String item = SCConfig.woodcutterRepairNetherite.get();
        return item.isEmpty() ? null : new ResourceLocation(item);
    }

    @Override
    public int getRepairItemUnits(@NotNull ItemStack stack) {
        ResourceLocation name = ForgeRegistries.ITEMS.getKey(stack.getItem());
        if (name != null && name.equals(getRepairItem())) {
            return SCConfig.woodcutterRepairAmountNetherite.get();
        }
        return 0;
    }

    @Override
    public int getRepairSpeed()
    {
        return 250;
    }
}
