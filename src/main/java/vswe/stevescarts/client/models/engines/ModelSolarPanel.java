package vswe.stevescarts.client.models.engines;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.geom.ModelPart;
import vswe.stevescarts.client.models.ModelCartbase;
import vswe.stevescarts.modules.ModuleBase;
import vswe.stevescarts.modules.engines.ModuleSolarTop;

@Deprecated(forRemoval = true)
public abstract class ModelSolarPanel extends ModelCartbase
{
    ModelPart moving;

    //TODO
//    protected ModelPart createMovingHolder(final int x, final int y)
//    {
//        final ModelRenderer moving = new ModelRenderer(this, x, y);
//        AddRenderer(this.moving = moving);
//        return moving;
//    }

    @Override
    public void applyEffects(final ModuleBase module, PoseStack matrixStack, VertexConsumer rtb, final float yaw, final float pitch, final float roll)
    {
//        moving.y = ((module == null) ? -4.0f : ((ModuleSolarTop) module).getMovingLevel());
    }
}
