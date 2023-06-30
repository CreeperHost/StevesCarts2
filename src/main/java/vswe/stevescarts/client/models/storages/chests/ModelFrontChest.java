package vswe.stevescarts.client.models.storages.chests;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.renderer.MultiBufferSource;
import vswe.stevescarts.api.client.ModelCartbase;
import vswe.stevescarts.api.modules.ModuleBase;
import vswe.stevescarts.api.modules.template.ModuleChest;
import vswe.stevescarts.helpers.ResourceHelper;

public class ModelFrontChest extends ModelCartbase
{
    private final ModelPart lid;

    public ModelFrontChest()
    {
        super(getTexturedModelData().bakeRoot(), ResourceHelper.getResource("/models/frontChestModel.png"));
        this.root.setPos(-3.5f, 0.0f, 0.0f);
        this.root.setRotation(0.0f, 1.5707964f, 0.0f);
        this.lid = this.root.getChild("lid");
    }

    public static LayerDefinition getTexturedModelData()
    {
        MeshDefinition modelData = new MeshDefinition();
        PartDefinition modelPartData = modelData.getRoot();
        modelPartData.addOrReplaceChild("base", CubeListBuilder.create().texOffs(0, 11).addBox(7.0f, 3.0f, 4.0f, 14, 6, 8), PartPose.offset(-14.0f, -5.5f, -18.5f));
        modelPartData.addOrReplaceChild("lid", CubeListBuilder.create().texOffs(0, 0).addBox(7.0f, -3.0f, -8.0f, 14, 3, 8), PartPose.offset(-14.0f, -1.5f, -6.5f));
        modelPartData.addOrReplaceChild("lock", CubeListBuilder.create().texOffs(0, 0).addBox(1.0f, 1.5f, 0.5f, 2, 3, 1), PartPose.offset(-2.0f, -4.5f, -16.0f));
        return LayerDefinition.create(modelData, 64, 32);
    }

    @Override
    public void applyEffects(final ModuleBase module, PoseStack matrixStack, MultiBufferSource rtb, final float yaw, final float pitch, final float roll)
    {
        lid.xRot = ((module == null) ? 0.0f : (-((ModuleChest) module).getChestAngle()));
    }
}
