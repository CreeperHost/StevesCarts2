package vswe.stevescarts.client.models.engines;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.resources.ResourceLocation;
import vswe.stevescarts.client.models.ModelCartbase;
import vswe.stevescarts.helpers.ResourceHelper;
import vswe.stevescarts.modules.ModuleBase;
import vswe.stevescarts.modules.engines.ModuleSolarCompact;

public class ModelCompactSolarPanel extends ModelCartbase
{
    private static ResourceLocation texture;
    private static ResourceLocation texture2;
    ModelPart[][] models;

    @Override
    public ResourceLocation getResource(final ModuleBase module)
    {
        if (module != null && ((ModuleSolarCompact) module).getLight() == 15)
        {
            return ModelCompactSolarPanel.texture;
        }
        return ModelCompactSolarPanel.texture2;
    }

    @Override
    protected int getTextureWidth()
    {
        return 64;
    }

    @Override
    protected int getTextureHeight()
    {
        return 32;
    }

    public ModelCompactSolarPanel()
    {
        (models = new ModelPart[2][])[0] = createSide(false);
        models[1] = createSide(true);
    }

    private ModelPart[] createSide(final boolean opposite)
    {
        final ModelPart anchor = new ModelPart(this, 0, 0);
        AddRenderer(anchor);
        if (opposite)
        {
            anchor.yRot = 3.1415927f;
        }
        final ModelRenderer base = new ModelRenderer(this, 0, 0);
        anchor.addChild(base);
        fixSize(base);
        base.addBox(-7.0f, -6.0f, -1.5f, 14, 6, 3, 0.0f);
        base.setPos(0.0f, 2.0f, -9.0f);
        final ModelRenderer panelarminner = new ModelRenderer(this, 34, 0);
        anchor.addChild(panelarminner);
        fixSize(panelarminner);
        panelarminner.addBox(-1.0f, -1.0f, -2.0f, 2, 2, 4, 0.0f);
        panelarminner.setPos(0.0f, -1.0f, 0.0f);
        final ModelRenderer panelarmouter = new ModelRenderer(this, 34, 0);
        panelarminner.addChild(panelarmouter);
        fixSize(panelarmouter);
        panelarmouter.addBox(-1.0f, -1.0f, -3.0f, 2, 2, 4, 0.0f);
        panelarmouter.setPos(0.001f, 0.001f, 0.001f);
        final ModelRenderer panelBase = new ModelRenderer(this, 0, 9);
        panelarmouter.addChild(panelBase);
        fixSize(panelBase);
        panelBase.addBox(-5.5f, -2.0f, -1.0f, 11, 4, 2, 0.0f);
        panelBase.setPos(0.0f, 0.0f, -2.8f);
        final ModelRenderer panelTop = createPanel(panelBase, 10, 4, -0.497f, 0, 15);
        final ModelRenderer panelBot = createPanel(panelBase, 10, 4, -0.494f, 22, 15);
        final ModelRenderer panelLeft = createPanel(panelBase, 6, 4, -0.491f, 0, 20);
        final ModelRenderer panelRight = createPanel(panelBase, 6, 4, -0.488f, 14, 20);
        final ModelRenderer panelTopLeft = createPanel(panelLeft, 6, 4, 0.002f, 0, 25);
        final ModelRenderer panelBotLeft = createPanel(panelLeft, 6, 4, 0.001f, 28, 25);
        final ModelRenderer panelTopRight = createPanel(panelRight, 6, 4, 0.002f, 14, 25);
        final ModelRenderer panelBotRight = createPanel(panelRight, 6, 4, 0.001f, 42, 25);
        return new ModelRenderer[]{panelBase, panelTop, panelBot, panelLeft, panelRight, panelTopLeft, panelTopRight, panelBotLeft, panelBotRight, panelarmouter, panelarminner};
    }

    private ModelRenderer createPanel(final ModelRenderer parent, final int width, final int height, final float offset, final int textureOffsetX, final int textureOffsetY)
    {
        final ModelRenderer panel = new ModelRenderer(this, textureOffsetX, textureOffsetY);
        parent.addChild(panel);
        fixSize(panel);
        panel.addBox(-width / 2, -height / 2, -0.5f, width, height, 1, 0.0f);
        panel.setPos(0.0f, 0.0f, offset);
        return panel;
    }

    @Override
    public void applyEffects(final ModuleBase module, PoseStack matrixStack, VertexConsumer rtb, final float yaw, final float pitch, final float roll)
    {
        if (module == null)
        {
            for (int i = 0; i < 2; ++i)
            {
                final ModelPart[] models = this.models[i];
                models[9].z = 0.6f;
                models[10].z = -8.1f;
                models[1].y = -0.1f;
                models[2].y = 0.1f;
                models[3].x = -2.01f;
                models[4].x = 2.01f;
                final ModelPart modelRenderer = models[5];
                final ModelPart modelRenderer2 = models[6];
                final float n = -0.1f;
                modelRenderer2.y = n;
                modelRenderer.y = n;
                final ModelPart modelRenderer3 = models[7];
                final ModelPart modelRenderer4 = models[8];
                final float n2 = 0.1f;
                modelRenderer4.y = n2;
                modelRenderer3.y = n2;
                models[9].xRot = 0.0f;
            }
        }
        else
        {
            final ModuleSolarCompact solar = (ModuleSolarCompact) module;
            for (int j = 0; j < 2; ++j)
            {
                final ModelPart[] models2 = models[j];
                models2[9].z = 1.0f - solar.getExtractionDist();
                models2[10].z = -7.7f - solar.getInnerExtraction();
                models2[1].y = -solar.getTopBotExtractionDist();
                models2[2].y = solar.getTopBotExtractionDist();
                models2[3].x = -2.0f - solar.getLeftRightExtractionDist();
                models2[4].x = 2.0f + solar.getLeftRightExtractionDist();
                final ModelPart modelRenderer5 = models2[5];
                final ModelPart modelRenderer6 = models2[6];
                final float n3 = -solar.getCornerExtractionDist();
                modelRenderer6.y = n3;
                modelRenderer5.y = n3;
                final ModelPart modelRenderer7 = models2[7];
                final ModelPart modelRenderer8 = models2[8];
                final float cornerExtractionDist = solar.getCornerExtractionDist();
                modelRenderer8.y = cornerExtractionDist;
                modelRenderer7.y = cornerExtractionDist;
                models2[9].xRot = -solar.getPanelAngle();
            }
        }
    }

    static
    {
        ModelCompactSolarPanel.texture = ResourceHelper.getResource("/models/panelModelSideActive.png");
        ModelCompactSolarPanel.texture2 = ResourceHelper.getResource("/models/panelModelSideIdle.png");
    }
}
