package vswe.stevescarts.client.models;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.resources.ResourceLocation;
import vswe.stevescarts.api.client.ModelCartbase;
import vswe.stevescarts.api.modules.interfaces.ILeverModule;
import vswe.stevescarts.api.modules.ModuleBase;

public class ModelLever extends ModelCartbase
{
    ModelPart lever;

    public ModelLever(final ResourceLocation resource)
    {
        super(getTexturedModelData().bakeRoot(), resource);
        lever = getRoot().getChild("base").getChild("lever");
    }

    public static LayerDefinition getTexturedModelData()
    {
        MeshDefinition modelData = new MeshDefinition();
        PartDefinition modelPartData = modelData.getRoot();
        PartDefinition base = modelPartData.addOrReplaceChild("base", CubeListBuilder.create().texOffs(0, 0)
                .addBox(-2.5f, -1.5f, -0.5f, 5, 3, 1),
                PartPose.offset(0.0f, 2.0f, 8.5f));

        PartDefinition lever = base.addOrReplaceChild("lever", CubeListBuilder.create().texOffs(0, 4)
                .addBox(-0.5f, -12.0f, -0.5f, 1, 11, 1),
                PartPose.ZERO);

        lever.addOrReplaceChild("handle", CubeListBuilder.create().texOffs(4, 4).
                addBox(-1.0f, -13.0f, -1.0f, 2, 2, 2),
                PartPose.ZERO);

        return LayerDefinition.create(modelData, 32, 32);
    }

    @Override
    public void applyEffects(final ModuleBase module, PoseStack matrixStack, MultiBufferSource rtb, final float yaw, final float pitch, final float roll)
    {
        lever.zRot = ((module == null) ? 0.0f : (0.3926991f - ((ILeverModule) module).getLeverState() * 3.1415927f / 4.0f));
    }
}
