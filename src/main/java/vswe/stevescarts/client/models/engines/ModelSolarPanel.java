package vswe.stevescarts.client.models.engines;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.model.ModelRenderer;
import vswe.stevescarts.client.models.ModelCartbase;
import vswe.stevescarts.modules.ModuleBase;
import vswe.stevescarts.modules.engines.ModuleSolarTop;

public abstract class ModelSolarPanel extends ModelCartbase
{
    ModelRenderer moving;

    protected ModelRenderer createMovingHolder(final int x, final int y)
    {
        final ModelRenderer moving = new ModelRenderer(this, x, y);
        AddRenderer(this.moving = moving);
        return moving;
    }

    @Override
    public void applyEffects(final ModuleBase module, MatrixStack matrixStack, IRenderTypeBuffer rtb, final float yaw, final float pitch, final float roll)
    {
        moving.y = ((module == null) ? -4.0f : ((ModuleSolarTop) module).getMovingLevel());
    }
}
