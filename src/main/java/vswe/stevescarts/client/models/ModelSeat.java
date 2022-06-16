package vswe.stevescarts.client.models;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import vswe.stevescarts.helpers.ResourceHelper;
import vswe.stevescarts.modules.ModuleBase;
import vswe.stevescarts.modules.realtimers.ModuleSeat;

public class ModelSeat extends ModelCartbase
{
    public ModelSeat()
    {
        super(getTexturedModelData().bakeRoot(), ResourceHelper.getResource("/models/chairModel.png"));
    }

    public static LayerDefinition getTexturedModelData()
    {
        MeshDefinition modelData = new MeshDefinition();
        PartDefinition modelPartData = modelData.getRoot();
        modelPartData.addOrReplaceChild("sit", CubeListBuilder.create().texOffs(0, 0)
                .addBox(-4.0f, -2.0f, -2.0f, 8, 4, 4), PartPose.offset(0.0f, 1.0f, 0.0f));
        modelPartData.addOrReplaceChild("back", CubeListBuilder.create().texOffs(0, 8)
                .addBox(-4.0f, -2.0f, -1.0f, 8, 12, 2), PartPose.offset(0.0f, -7.0f, 3.0f));
        return LayerDefinition.create(modelData, 32, 32);
    }

    @Override
    public void applyEffects(final ModuleBase module, PoseStack matrixStack, VertexConsumer rtb, final float yaw, final float pitch, final float roll)
    {
        Entity firstPassenger = module.getCart().getFirstPassenger();
        this.root.yRot = 3.1415926F;
        if (firstPassenger != null) {
            this.root.yRot += (float) Math.toRadians(firstPassenger.getYRot());
        } else {
            this.root.yRot /= 2.0f;
        }
    }
}
