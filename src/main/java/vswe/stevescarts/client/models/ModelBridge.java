package vswe.stevescarts.client.models;

import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import vswe.stevescarts.api.client.ModelCartbase;
import vswe.stevescarts.api.modules.ModuleBase;
import vswe.stevescarts.helpers.ResourceHelper;
import vswe.stevescarts.modules.workers.ModuleBridge;

public class ModelBridge extends ModelCartbase
{
    private static ResourceLocation normal;
    private static ResourceLocation down;
    private static ResourceLocation up;
    private static ResourceLocation normalWarning;
    private static ResourceLocation downWarning;
    private static ResourceLocation upWarning;
    private ModelPart drillAnchor;

    public ModelBridge()
    {
        //TODO
        //        final ModelRenderer side1 = new ModelRenderer(this, 0, 0);
        //        AddRenderer(side1);
        //        side1.addBox(1.0f, 3.0f, 0.5f, 2, 6, 1, 0.0f);
        //        side1.setPos(-11.5f, -6.0f, 8.0f);
        //        side1.yRot = 1.5707964f;
        //        final ModelRenderer side2 = new ModelRenderer(this, 0, 0);
        //        AddRenderer(side2);
        //        side2.addBox(1.0f, 3.0f, 0.5f, 2, 6, 1, 0.0f);
        //        side2.setPos(-11.5f, -6.0f, -4.0f);
        //        side2.yRot = 1.5707964f;
    }

    @Override
    public ResourceLocation getResource(final ModuleBase module)
    {
        if (module == null)
        {
            return ModelBridge.normal;
        }
        final boolean needBridge = ((ModuleBridge) module).needBridge();
        BlockPos next = ((ModuleBridge) module).getNextblock();
        final int y = next.getY();
        final int yDif = module.getCart().getYTarget() - y;
        if (needBridge)
        {
            if (yDif > 0)
            {
                return ModelBridge.upWarning;
            }
            if (yDif < 0)
            {
                return ModelBridge.downWarning;
            }
            return ModelBridge.normalWarning;
        }
        else
        {
            if (yDif > 0)
            {
                return ModelBridge.up;
            }
            if (yDif < 0)
            {
                return ModelBridge.down;
            }
            return ModelBridge.normal;
        }
    }

    @Override
    protected int getTextureWidth()
    {
        return 8;
    }

    @Override
    protected int getTextureHeight()
    {
        return 8;
    }

    static
    {
        ModelBridge.normal = ResourceHelper.getResource("/models/aiModelNormal.png");
        ModelBridge.down = ResourceHelper.getResource("/models/aiModelDown.png");
        ModelBridge.up = ResourceHelper.getResource("/models/aiModelUp.png");
        ModelBridge.normalWarning = ResourceHelper.getResource("/models/aiModelNormalWarning.png");
        ModelBridge.downWarning = ResourceHelper.getResource("/models/aiModelDownWarning.png");
        ModelBridge.upWarning = ResourceHelper.getResource("/models/aiModelUpWarning.png");
    }
}
