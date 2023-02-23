package vswe.stevescarts.client.models.engines;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.renderer.MultiBufferSource;
import vswe.stevescarts.api.client.ModelCartbase;
import vswe.stevescarts.helpers.ResourceHelper;
import vswe.stevescarts.api.modules.ModuleBase;
import vswe.stevescarts.modules.engines.ModuleSolarTop;

public class ModelSolarPanelHeads extends ModelCartbase
{
    private final ModelPart[] panels;

    public ModelSolarPanelHeads(int count)
    {
        super(getTexturedModelData(count).bakeRoot(), ResourceHelper.getResource("/models/panelModelActive.png"));
        this.panels = new ModelPart[count];
        for (int i = 0; i < count; i++)
        {
            this.panels[i] = this.getRoot().getChild("panel" + i);
        }
    }

    public static LayerDefinition getTexturedModelData(int count)
    {
        MeshDefinition modelData = new MeshDefinition();
        PartDefinition modelPartData = modelData.getRoot();
        for (int i = 0; i < count; i++)
        {
            float rotation;
            float f;
            switch (i)
            {
                case 0 -> {
                    rotation = 0.0f;
                    f = -1.5f;
                }
                case 1 -> {
                    rotation = 3.1415927f;
                    f = -1.5f;
                }
                case 2 -> {
                    rotation = 4.712389f;
                    f = -6.0f;
                }
                case 3 -> {
                    rotation = 1.5707964f;
                    f = -6.0f;
                }
                default -> throw new IllegalArgumentException("Invalid index: " + i);
            }
            modelPartData.addOrReplaceChild("panel" + i, CubeListBuilder.create().texOffs(0, 0).addBox(-6.0f, 0.0f, -2.0f, 12, 13, 2), PartPose.offsetAndRotation((float) (Math.sin(rotation) * f), -5.0f, (float) (Math.cos(rotation) * f), 0.0f, rotation, 0.0f));
        }
        return LayerDefinition.create(modelData, 32, 16);
    }

    @Override
    public void applyEffects(final ModuleBase module, PoseStack matrixStack, MultiBufferSource rtb, final float yaw, final float pitch, final float roll)
    {
        super.applyEffects(module, matrixStack, rtb, yaw, pitch, roll);
        for (final ModelPart panel : panels)
        {
            panel.xRot = ((module == null) ? 0.0f : (-((ModuleSolarTop) module).getInnerRotation()));
            panel.y = ((module == null) ? -4.0f : ((ModuleSolarTop) module).getMovingLevel());
        }
    }
}
