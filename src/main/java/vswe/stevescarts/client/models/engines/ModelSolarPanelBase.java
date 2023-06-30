package vswe.stevescarts.client.models.engines;

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
import vswe.stevescarts.helpers.ResourceHelper;
import vswe.stevescarts.modules.engines.ModuleSolarTop;

public class ModelSolarPanelBase extends ModelCartbase
{
    private final ModelPart moving;
    private final ModelPart top;

    public ModelSolarPanelBase()
    {
        super(getTexturedModelData().bakeRoot(), ResourceHelper.getResource("/models/panelModelBase.png"));
        this.moving = this.getRoot().getChild("moving");
        this.top = this.getRoot().getChild("top");
    }

    public static LayerDefinition getTexturedModelData()
    {
        MeshDefinition modelData = new MeshDefinition();
        PartDefinition modelPartData = modelData.getRoot();
        modelPartData.addOrReplaceChild("base", CubeListBuilder.create().texOffs(0, 0).addBox(-1.0f, -5.0f, -1.0f, 2, 10, 2), PartPose.offset(0.0f, -4.5f, 0.0f));
        modelPartData.addOrReplaceChild("moving", CubeListBuilder.create().texOffs(8, 0).addBox(-2.0f, -3.5f, -2.0f, 4, 7, 4), PartPose.offset(0.0f, 0.0f, 0.0f));
        modelPartData.addOrReplaceChild("top", CubeListBuilder.create().texOffs(0, 12).addBox(-6.0f, -1.5f, -2.0f, 12, 3, 4), PartPose.offset(0.0f, -5.0f, 0.0f));
        return LayerDefinition.create(modelData, 32, 32);
    }

    @Override
    public void applyEffects(final ModuleBase module, PoseStack matrixStack, MultiBufferSource rtb, final float yaw, final float pitch, final float roll)
    {
        super.applyEffects(module, matrixStack, rtb, yaw, pitch, roll);
        moving.y = ((module == null) ? -4.0f : ((ModuleSolarTop) module).getMovingLevel());
    }
}
