package vswe.stevescarts.client.models.engines;

import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.util.ResourceLocation;
import vswe.stevescarts.helpers.ResourceHelper;
import vswe.stevescarts.modules.ModuleBase;

public class ModelSolarPanelBase extends ModelSolarPanel
{
    private static ResourceLocation texture;

    @Override
    public ResourceLocation getResource(final ModuleBase module)
    {
        return ModelSolarPanelBase.texture;
    }

    @Override
    protected int getTextureWidth()
    {
        return 32;
    }

    @Override
    protected int getTextureHeight()
    {
        return 32;
    }

    public ModelSolarPanelBase()
    {
        final ModelRenderer base = new ModelRenderer(this, 0, 0);
        AddRenderer(base);
        base.addBox(-1.0f, -5.0f, -1.0f, 2, 10, 2, 0.0f);
        base.setPos(0.0f, -4.5f, 0.0f);
        final ModelRenderer moving = createMovingHolder(8, 0);
        moving.addBox(-2.0f, -3.5f, -2.0f, 4, 7, 4, 0.0f);
        final ModelRenderer top = new ModelRenderer(this, 0, 12);
        fixSize(top);
        moving.addChild(top);
        top.addBox(-6.0f, -1.5f, -2.0f, 12, 3, 4, 0.0f);
        top.setPos(0.0f, -5.0f, 0.0f);
    }

    static
    {
        ModelSolarPanelBase.texture = ResourceHelper.getResource("/models/panelModelBase.png");
    }
}
