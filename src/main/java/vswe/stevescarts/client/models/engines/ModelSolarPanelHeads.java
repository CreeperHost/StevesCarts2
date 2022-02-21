package vswe.stevescarts.client.models.engines;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.resources.ResourceLocation;
import vswe.stevescarts.helpers.ResourceHelper;
import vswe.stevescarts.modules.ModuleBase;
import vswe.stevescarts.modules.engines.ModuleSolarTop;

import java.util.ArrayList;

public class ModelSolarPanelHeads extends ModelSolarPanel
{
    private static ResourceLocation texture;
    private static ResourceLocation texture2;
    ArrayList<ModelPart> panels;

    @Override
    public ResourceLocation getResource(final ModuleBase module)
    {
        if (module != null && ((ModuleSolarTop) module).getLight() == 15)
        {
            return ModelSolarPanelHeads.texture;
        }
        return ModelSolarPanelHeads.texture2;
    }

    @Override
    protected int getTextureWidth()
    {
        return 32;
    }

    @Override
    protected int getTextureHeight()
    {
        return 16;
    }

    public ModelSolarPanelHeads(final int panelCount)
    {
        panels = new ArrayList<>();
        //TODO
//        final ModelPart moving = createMovingHolder(0, 0);
        for (int i = 0; i < panelCount; ++i)
        {
            createPanel(moving, i);
        }
    }

    private void createPanel(final ModelPart base, final int index)
    {
        float rotation = 0.0f;
        float f = 0.0f;
        switch (index)
        {
            case 0:
            {
                rotation = 0.0f;
                f = -1.5f;
                break;
            }
            case 1:
            {
                rotation = 3.1415927f;
                f = -1.5f;
                break;
            }
            case 2:
            {
                rotation = 4.712389f;
                f = -6.0f;
                break;
            }
            case 3:
            {
                rotation = 1.5707964f;
                f = -6.0f;
                break;
            }
            default:
            {
                return;
            }
        }
        createPanel(base, rotation, f);
    }

    private void createPanel(final ModelPart base, final float rotation, final float f)
    {
        //TODO
//        final ModelRenderer panel = new ModelRenderer(this, 0, 0);
//        fixSize(panel);
//        base.addChild(panel);
//        panel.addBox(-6.0f, 0.0f, -2.0f, 12, 13, 2, 0.0f);
//        panel.setPos((float) Math.sin(rotation) * f, -5.0f, (float) Math.cos(rotation) * f);
//        panel.yRot = rotation;
//        panels.add(panel);
    }

    @Override
    public void applyEffects(final ModuleBase module, PoseStack matrixStack, VertexConsumer rtb, final float yaw, final float pitch, final float roll)
    {
        super.applyEffects(module, matrixStack, rtb, yaw, pitch, roll);
        for (final ModelPart panel : panels)
        {
            panel.xRot = ((module == null) ? 0.0f : (-((ModuleSolarTop) module).getInnerRotation()));
        }
    }

    static
    {
        ModelSolarPanelHeads.texture = ResourceHelper.getResource("/models/panelModelActive.png");
        ModelSolarPanelHeads.texture2 = ResourceHelper.getResource("/models/panelModelIdle.png");
    }
}
