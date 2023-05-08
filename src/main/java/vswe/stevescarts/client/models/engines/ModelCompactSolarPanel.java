package vswe.stevescarts.client.models.engines;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import vswe.stevescarts.api.client.ModelCartbase;
import vswe.stevescarts.api.modules.ModuleBase;
import vswe.stevescarts.helpers.ResourceHelper;
import vswe.stevescarts.modules.engines.ModuleSolarCompact;

import java.util.function.Consumer;

public class ModelCompactSolarPanel extends ModelCartbase {
    private static ResourceLocation texture = ResourceHelper.getResource("/models/panelModelSideActive.png");
    private static ResourceLocation texture2 = ResourceHelper.getResource("/models/panelModelSideIdle.png");
    private ModelPart modelLeft;
    private ModelPart modelRight;
    private ModelPart[][] modelParts;

    public ModelCompactSolarPanel() {
        super(null, texture);
        generateModels();
    }

    @Override
    public RenderType getRenderType(ModuleBase module) {
        if (module != null && ((ModuleSolarCompact) module).getLight() == 15) {
            return RenderType.entitySolid(texture);
        }
        return RenderType.entitySolid(texture2);
    }

    private void generateModels() {
        modelParts = new ModelPart[2][];
        modelParts[0] = createSide(false, modelPart -> modelLeft = modelPart);
        modelParts[1] = createSide(true, modelPart -> modelRight = modelPart);
    }

    private ModelPart[] createSide(boolean reverse, Consumer<ModelPart> buildModel) {
        MeshDefinition modelData = new MeshDefinition();
        PartDefinition modelPartData = modelData.getRoot();

        PartDefinition anchor = modelPartData.addOrReplaceChild("anchor", CubeListBuilder.create(), PartPose.rotation(0, reverse ? 3.1415927f : 0, 0));
        anchor.addOrReplaceChild("base", CubeListBuilder.create()
                        .addBox(-7.0f, -6.0f, -1.5f, 14, 6, 3),
                PartPose.offset(0.0f, 2.0f, -9.0f)
        );

        PartDefinition panelArmInner = anchor.addOrReplaceChild("panelArmInner", CubeListBuilder.create()
                        .texOffs(34, 0)
                        .addBox(-1.0f, -1.0f, -2.0f, 2, 2, 4),
                PartPose.offset(0.0f, -1.0f, 0.0f)
        );

        PartDefinition panelArmOuter = panelArmInner.addOrReplaceChild("panelArmOuter", CubeListBuilder.create()
                        .texOffs(34, 0)
                        .addBox(-1.0f, -1.0f, -3.0f, 2, 2, 4),
                PartPose.offset(0.001f, 0.001f, 0.001f)
        );

        PartDefinition panelBase = panelArmOuter.addOrReplaceChild("panelBase", CubeListBuilder.create()
                        .texOffs(0, 9)
                        .addBox(-5.5f, -2.0f, -1.0f, 11, 4, 2),
                PartPose.offset(0.0f, 0.0f, -2.8f)
        );

        createPanel(panelBase, "panelTop", 10, 4, -0.497f, 0, 15);
        createPanel(panelBase, "panelBot", 10, 4, -0.494f, 22, 15);
        PartDefinition panelLeft = createPanel(panelBase, "panelLeft", 6, 4, -0.491f, 0, 20);
        PartDefinition panelRight = createPanel(panelBase, "panelRight", 6, 4, -0.488f, 14, 20);
        createPanel(panelLeft, "panelTopLeft", 6, 4, 0.002f, 0, 25);
        createPanel(panelLeft, "panelBotLeft", 6, 4, 0.001f, 28, 25);
        createPanel(panelRight, "panelTopRight", 6, 4, 0.002f, 14, 25);
        createPanel(panelRight, "panelBotRight", 6, 4, 0.001f, 42, 25);

        ModelPart anchorPart = anchor.bake(64, 32);
        buildModel.accept(anchorPart);

        ModelPart armInnerPart = anchorPart.getChild("panelArmInner");
        ModelPart armOuterPart = armInnerPart.getChild("panelArmOuter");
        ModelPart panelBasePart = armOuterPart.getChild("panelBase");

        ModelPart panelLeftPart = panelBasePart.getChild("panelLeft");
        ModelPart panelRightPart = panelBasePart.getChild("panelRight");

        return new ModelPart[]{
                panelBasePart,
                panelBasePart.getChild("panelTop"),
                panelBasePart.getChild("panelBot"),
                panelLeftPart,
                panelRightPart,
                panelLeftPart.getChild("panelTopLeft"),
                panelRightPart.getChild("panelTopRight"),
                panelLeftPart.getChild("panelBotLeft"),
                panelRightPart.getChild("panelBotRight"),
                armOuterPart,
                armInnerPart};
    }

    private PartDefinition createPanel(PartDefinition parent, String name, int width, int height, float offset, int textureOffsetX, int textureOffsetY) {
        return parent.addOrReplaceChild(name, CubeListBuilder.create()
                        .texOffs(textureOffsetX, textureOffsetY)
                        .addBox(-width / 2, -height / 2, -0.5f, width, height, 1),
                PartPose.offset(0.0f, 0.0f, offset));
    }

    @Override
    public void renderToBuffer(@NotNull PoseStack poseStack, @NotNull VertexConsumer vertexConsumers, int light, int overlay, float red, float green, float blue, float alpha) {
        modelLeft.render(poseStack, vertexConsumers, light, overlay, red, green, blue, alpha);
        modelRight.render(poseStack, vertexConsumers, light, overlay, red, green, blue, alpha);
    }

    @Override
    public void applyEffects(ModuleBase module, PoseStack matrixStack, MultiBufferSource rtb, float yaw, float pitch, float roll) {
        generateModels();
        if (module == null) {
            for (int i = 0; i < 2; ++i) {
                ModelPart[] models = modelParts[i];
                models[9].z = 0.6f;
                models[10].z = -8.1f;
                models[1].y = -0.1f;
                models[2].y = 0.1f;
                models[3].x = -2.01f;
                models[4].x = 2.01f;
                ModelPart modelRenderer = models[5];
                ModelPart modelRenderer2 = models[6];
                float n = -0.1f;
                modelRenderer2.y = n;
                modelRenderer.y = n;
                ModelPart modelRenderer3 = models[7];
                ModelPart modelRenderer4 = models[8];
                float n2 = 0.1f;
                modelRenderer4.y = n2;
                modelRenderer3.y = n2;
                models[9].xRot = 0.0f;
            }
        } else {
            ModuleSolarCompact solar = (ModuleSolarCompact) module;
            for (int j = 0; j < 2; ++j) {
                ModelPart[] models2 = modelParts[j];
                models2[9].z = 1.0f - solar.getExtractionDist();
                models2[10].z = -7.7f - solar.getInnerExtraction();
                models2[1].y = -solar.getTopBotExtractionDist();
                models2[2].y = solar.getTopBotExtractionDist();
                models2[3].x = -2.0f - solar.getLeftRightExtractionDist();
                models2[4].x = 2.0f + solar.getLeftRightExtractionDist();
                ModelPart modelRenderer5 = models2[5];
                ModelPart modelRenderer6 = models2[6];
                float n3 = -solar.getCornerExtractionDist();
                modelRenderer6.y = n3;
                modelRenderer5.y = n3;
                ModelPart modelRenderer7 = models2[7];
                ModelPart modelRenderer8 = models2[8];
                float cornerExtractionDist = solar.getCornerExtractionDist();
                modelRenderer8.y = cornerExtractionDist;
                modelRenderer7.y = cornerExtractionDist;
                models2[9].xRot = -solar.getPanelAngle();
            }
        }
    }
}
