package vswe.stevescarts.client.models;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.geom.ModelPart;
import vswe.stevescarts.client.models.realtimers.ModelGun;
import vswe.stevescarts.modules.ModuleBase;
import vswe.stevescarts.modules.realtimers.ModuleShooterAdv;

public class ModelSniperRifle extends ModelGun
{
    ModelPart anchor;
    ModelPart gun;

    public ModelSniperRifle()
    {
        //TODO
        //        AddRenderer(anchor = new ModelRenderer(this));
        //        gun = createGun(anchor);
    }

    @Override
    public void applyEffects(final ModuleBase module, PoseStack matrixStack, VertexConsumer rtb, final float yaw, final float pitch, final float roll)
    {
        gun.zRot = ((module == null) ? 0.0f : ((ModuleShooterAdv) module).getPipeRotation(0));
        anchor.yRot = ((module == null) ? 0.0f : ((float) Math.PI + ((ModuleShooterAdv) module).getRifleDirection() + yaw));
    }
}
