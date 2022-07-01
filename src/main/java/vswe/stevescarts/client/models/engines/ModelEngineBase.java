package vswe.stevescarts.client.models.engines;

import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.resources.ResourceLocation;
import vswe.stevescarts.api.client.ModelCartbase;

public abstract class ModelEngineBase extends ModelCartbase
{
    public ModelEngineBase(ModelPart root, ResourceLocation texture)
    {
        super(root, texture);
        root.setPos(10.5f, 0.5f, -0.0f);
        root.yRot = -1.5707964f;
    }
}
