package vswe.stevescarts.client.models.workers.tools;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.resources.ResourceLocation;
import vswe.stevescarts.client.models.ModelCartbase;
import vswe.stevescarts.modules.ModuleBase;

public class ModelDrill extends ModelCartbase
{
    private ModelPart drillAnchor;
    private ResourceLocation resource;

    @Override
    public ResourceLocation getResource(final ModuleBase module)
    {
        return resource;
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

    public ModelDrill(final ResourceLocation resource)
    {
        super();
        this.resource = resource;
        //TODO
//        AddRenderer(drillAnchor = new ModelRenderer(this));
//        drillAnchor.yRot = 4.712389f;
//        int srcY = 0;
//        for (int i = 0; i < 6; ++i)
//        {
//            final ModelRenderer drill = fixSize(new ModelRenderer(this, 0, srcY));
//            drillAnchor.addChild(drill);
//            drill.addBox(-3.0f + i * 0.5f, -3.0f + i * 0.5f, i, 6 - i, 6 - i, 1, 0.0f);
//            drill.setPos(0.0f, 0.0f, 11.0f);
//            srcY += 7 - i;
//        }
    }

    @Override
    public void applyEffects(final ModuleBase module, PoseStack matrixStack, VertexConsumer rtb, final float yaw, final float pitch, final float roll)
    {
        //		for (final Object drill : drillAnchor.childModels) {
        //			((ModelRenderer) drill).rotateAngleZ = ((module == null) ? 0.0f : ((ModuleDrill) module).getDrillRotation());
        //		}
    }
}
