package vswe.stevescarts.client.models.engines;

import net.minecraft.client.renderer.model.ModelRenderer;
import vswe.stevescarts.client.models.ModelCartbase;

public abstract class ModelEngineBase extends ModelCartbase
{
    protected ModelRenderer anchor;

    public ModelEngineBase()
    {
        AddRenderer(anchor = new ModelRenderer(this, 0, 0));
        anchor.setPos(10.5f, 0.5f, -0.0f);
        anchor.yRot = -1.5707964f;
    }
}
