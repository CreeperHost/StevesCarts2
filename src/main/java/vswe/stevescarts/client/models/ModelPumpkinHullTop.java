package vswe.stevescarts.client.models;

import net.minecraft.resources.ResourceLocation;
import vswe.stevescarts.api.modules.ModuleBase;

public class ModelPumpkinHullTop extends ModelHullTop
{
    private ResourceLocation resourceactive;
    private ResourceLocation resourceidle;

    @Override
    public ResourceLocation getResource(final ModuleBase module)
    {
        return (module == null || isActive(module)) ? resourceactive : resourceidle;
    }

    public ModelPumpkinHullTop(final ResourceLocation resourceactive, final ResourceLocation resourceidle)
    {
        super(resourceactive);
        this.resourceactive = resourceactive;
        this.resourceidle = resourceidle;
    }

    private boolean isActive(final ModuleBase module)
    {
        return false;
    }
}
