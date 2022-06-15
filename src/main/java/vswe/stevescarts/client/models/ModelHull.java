package vswe.stevescarts.client.models;

import net.minecraft.client.model.MinecartModel;
import net.minecraft.resources.ResourceLocation;

public class ModelHull extends ModelCartbase
{
    public ModelHull(ResourceLocation texture)
    {
        super(MinecartModel.createBodyLayer().bakeRoot(), new ResourceLocation("textures/entity/minecart.png"));
    }
}
