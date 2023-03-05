package vswe.stevescarts.client.models;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.creeperhost.polylib.client.render.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.renderer.MultiBufferSource;
import org.jetbrains.annotations.NotNull;
import vswe.stevescarts.api.client.ModelCartbase;
import vswe.stevescarts.api.modules.ModuleBase;
import vswe.stevescarts.helpers.ResourceHelper;
import vswe.stevescarts.modules.addons.ModuleShield;

public class ModelShield extends ModelCartbase {
    private ModelPart[] shields;
    private boolean enabled = false;
    private ModuleBase module;

    public ModelShield() {
        super(null, ResourceHelper.getResource("/models/shieldModel.png"));
        buildModels();
    }

    public void buildModels() {
        MeshDefinition modelData = new MeshDefinition();
        PartDefinition modelPartData = modelData.getRoot();

        shields = new ModelPart[4 * 5];

        for (int i = 0; i < shields.length; i++) {
            shields[i] = modelPartData.addOrReplaceChild("shield" + i, CubeListBuilder.create().texOffs(0, 0)
                                    .addBox(-1.0f, -1.0f, -1.0f, 2, 2, 2),
                            PartPose.offset(0.0f, 0.0f, 0.0f))
                    .bake(8, 4);
        }
    }

    @Override
    public void renderToBuffer(@NotNull PoseStack poseStack, @NotNull VertexConsumer vertexConsumers, int light, int overlay, float red, float green, float blue, float alpha) {
        if (!enabled) return;
        for (ModelPart shield : shields) {
            shield.render(poseStack, vertexConsumers, light, overlay, red, green, blue, alpha);
        }
    }

    @Override
    public void applyEffects(ModuleBase module, PoseStack matrixStack, MultiBufferSource rtb, float yaw, float pitch, float roll) {
        float shieldDistance = (module == null) ? 18.0f : ((ModuleShield) module).getShieldDistance();
        enabled = module == null || ((ModuleShield) module).hasShield();
        if (!enabled) return;
        float shieldAngle = (module == null) ? 0.0f : interpolate(((ModuleShield) module).getShieldAngle(), ((ModuleShield) module).getShieldAngle(), Minecraft.getInstance().getPartialTick());

        for (int i = 0; i < shields.length; i++) {
            ModelPart part = shields[i];
            int yPos = i / 5;
            double angularPos = ((i % 5) / 5D) * Math.PI * 2;
            angularPos += shieldAngle + (6.2831855f * (yPos / 4F));

            part.y = ((float) Math.sin(shieldAngle / 5) * 3.0f) + ((shieldDistance / 18.0f) * 5 * -yPos);
            part.x = (float) Math.sin(angularPos) * shieldDistance;
            part.z = (float) Math.cos(angularPos) * shieldDistance;
        }
    }

    public static float interpolate(float a, float b, float d) {
        return a + (b - a) * d;
    }
}
