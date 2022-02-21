package vswe.stevescarts.client.models;

import net.minecraft.resources.ResourceLocation;
import vswe.stevescarts.modules.ModuleBase;

public class ModelPumpkinHull extends ModelHull
{
    private ResourceLocation resourceactive;
    private ResourceLocation resourceidle;

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
        //TODO
        //		final long time = module.getCart().level.getWorldInfo().getWorldTime() % 24000L;
        //		return time >= 12000L && time <= 18000L;
        return false;
    }
}
