package vswe.stevescarts.client.models;

import net.minecraft.resources.ResourceLocation;
import vswe.stevescarts.api.modules.ModuleBase;

public class ModelPumpkinHull extends ModelHull
{
    private final ResourceLocation resourceactive;
    private final ResourceLocation resourceidle;

    @Override
    public ResourceLocation getResource(final ModuleBase module)
    {
        return (module == null || isActive(module)) ? resourceactive : resourceidle;
    }

    public ModelPumpkinHull(final ResourceLocation resourceactive, final ResourceLocation resourceidle)
    {
        super(resourceactive);
        this.resourceactive = resourceactive;
        this.resourceidle = resourceidle;
    }

    private boolean isActive(final ModuleBase module)
    {
        final long time = module.getCart().level.getLevelData().getGameTime() % 24000L;
        return time >= 12000L && time <= 18000L;
    }
}
