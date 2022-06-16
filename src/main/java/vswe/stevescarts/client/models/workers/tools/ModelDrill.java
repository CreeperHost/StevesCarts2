package vswe.stevescarts.client.models.workers.tools;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.resources.ResourceLocation;
import vswe.stevescarts.client.models.ModelCartbase;
import vswe.stevescarts.modules.ModuleBase;

public class ModelDrill extends ModelCartbase
{
    public ModelDrill(final ResourceLocation resource)
    {
        super(getTexturedModelData().bakeRoot(), resource);
        this.root.setRotation(0.0f, 4.712389f, 0.0f);
    }

    public static LayerDefinition getTexturedModelData()
    {
        MeshDefinition modelData = new MeshDefinition();
        PartDefinition modelPartData = modelData.getRoot();

        for (int i = 0; i < 6; ++i)
        {
            modelPartData.addOrReplaceChild("drill" + i, CubeListBuilder.create().texOffs(0, 0)
                            .addBox(-3.0f + i * 0.5f, -3.0f + i * 0.5f, i, 6 - i, 6 - i, 1),
                    PartPose.offset(0.0f, 0.0f, 11.0f));
        }
        return LayerDefinition.create(modelData, 32, 32);
    }

    @Override
    public void applyEffects(final ModuleBase module, PoseStack matrixStack, VertexConsumer rtb, final float yaw, final float pitch, final float roll)
    {
        //		for (final Object drill : drillAnchor.childModels) {
        //			((ModelRenderer) drill).rotateAngleZ = ((module == null) ? 0.0f : ((ModuleDrill) module).getDrillRotation());
        //		}
    }
}
