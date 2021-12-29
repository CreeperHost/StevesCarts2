package vswe.stevescarts.client.models;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.model.ModelRenderer;
import vswe.stevescarts.client.models.realtimers.ModelGun;
import vswe.stevescarts.modules.ModuleBase;
import vswe.stevescarts.modules.realtimers.ModuleShooterAdv;

public class ModelSniperRifle extends ModelGun
{
    ModelRenderer anchor;
    ModelRenderer gun;

    public ModelSniperRifle()
    {
        AddRenderer(anchor = new ModelRenderer(this));
        gun = createGun(anchor);
    }

    @Override
    public void applyEffects(final ModuleBase module, MatrixStack matrixStack, IRenderTypeBuffer rtb, final float yaw, final float pitch, final float roll)
    {
        gun.zRot = ((module == null) ? 0.0f : ((ModuleShooterAdv) module).getPipeRotation(0));
        anchor.yRot = ((module == null) ? 0.0f : ((float) Math.PI + ((ModuleShooterAdv) module).getRifleDirection() + yaw));
    }
}
