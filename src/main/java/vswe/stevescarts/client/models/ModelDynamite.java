package vswe.stevescarts.client.models;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import vswe.stevescarts.api.client.ModelCartbase;
import vswe.stevescarts.api.modules.ModuleBase;
import vswe.stevescarts.modules.realtimers.ModuleDynamite;

public class ModelDynamite extends ModelCartbase {
    private ModelPart[] dynamites = new ModelPart[54];
    private float[] yPos = new float[54];
    private float sizemult;


    public ModelDynamite(final ResourceLocation resource) {
        super(null, resource);
        buildModels();
    }

    public void buildModels() {
        createDynamite(0.0f, 0.0f, 0.0f, 0);
        createDynamite(-1.0f, 0.0f, 0.0f, 3);
        createDynamite(1.0f, 0.0f, 0.0f, 4);
        createDynamite(-2.0f, 0.0f, 0.0f, 18);
        createDynamite(2.0f, 0.0f, 0.0f, 19);
        createDynamite(-0.5f, 1.0f, 0.0f, 9);
        createDynamite(0.5f, 1.0f, 0.0f, 10);
        createDynamite(-1.5f, 1.0f, 0.0f, 24);
        createDynamite(1.5f, 1.0f, 0.0f, 25);
        createDynamite(0.0f, 2.0f, 0.0f, 15);
        createDynamite(-1.0f, 2.0f, 0.0f, 30);
        createDynamite(1.0f, 2.0f, 0.0f, 31);
        createDynamite(-3.0f, 0.0f, 0.0f, 36);
        createDynamite(3.0f, 0.0f, 0.0f, 37);
        createDynamite(-2.5f, 1.0f, 0.0f, 42);
        createDynamite(2.5f, 1.0f, 0.0f, 43);
        createDynamite(-2.0f, 2.0f, 0.0f, 48);
        createDynamite(2.0f, 2.0f, 0.0f, 49);
        createDynamite(0.0f, 0.0f, -1.0f, 1);
        createDynamite(-1.0f, 0.0f, -1.0f, 5);
        createDynamite(1.0f, 0.0f, -1.0f, 7);
        createDynamite(-2.0f, 0.0f, -1.0f, 20);
        createDynamite(2.0f, 0.0f, -1.0f, 22);
        createDynamite(-0.5f, 1.0f, -1.0f, 11);
        createDynamite(0.5f, 1.0f, -1.0f, 13);
        createDynamite(-1.5f, 1.0f, -1.0f, 26);
        createDynamite(1.5f, 1.0f, -1.0f, 28);
        createDynamite(0.0f, 2.0f, -1.0f, 16);
        createDynamite(-1.0f, 2.0f, -1.0f, 32);
        createDynamite(1.0f, 2.0f, -1.0f, 34);
        createDynamite(-3.0f, 0.0f, -1.0f, 38);
        createDynamite(3.0f, 0.0f, -1.0f, 40);
        createDynamite(-2.5f, 1.0f, -1.0f, 44);
        createDynamite(2.5f, 1.0f, -1.0f, 46);
        createDynamite(-2.0f, 2.0f, -1.0f, 50);
        createDynamite(2.0f, 2.0f, -1.0f, 52);
        createDynamite(0.0f, 0.0f, 1.0f, 2);
        createDynamite(-1.0f, 0.0f, 1.0f, 8);
        createDynamite(1.0f, 0.0f, 1.0f, 6);
        createDynamite(-2.0f, 0.0f, 1.0f, 21);
        createDynamite(2.0f, 0.0f, 1.0f, 23);
        createDynamite(-0.5f, 1.0f, 1.0f, 14);
        createDynamite(0.5f, 1.0f, 1.0f, 12);
        createDynamite(-1.5f, 1.0f, 1.0f, 29);
        createDynamite(1.5f, 1.0f, 1.0f, 27);
        createDynamite(0.0f, 2.0f, 1.0f, 17);
        createDynamite(-1.0f, 2.0f, 1.0f, 35);
        createDynamite(1.0f, 2.0f, 1.0f, 33);
        createDynamite(-3.0f, 0.0f, 1.0f, 41);
        createDynamite(3.0f, 0.0f, 1.0f, 39);
        createDynamite(-2.5f, 1.0f, 1.0f, 47);
        createDynamite(2.5f, 1.0f, 1.0f, 45);
        createDynamite(-2.0f, 2.0f, 1.0f, 53);
        createDynamite(2.0f, 2.0f, 1.0f, 51);
    }

    private void createDynamite(float x, float y, float z, int index) {
        MeshDefinition modelData = new MeshDefinition();
        PartDefinition modelPartData = modelData.getRoot();

        PartDefinition anchor = modelPartData.addOrReplaceChild("anchor_" + index, CubeListBuilder.create(),
                PartPose.offset(0f, 0f, 0f));

        PartDefinition dynamite = anchor.addOrReplaceChild("dynamite_" + index, CubeListBuilder.create()
                        .addBox(-8.0f, -4.0f, -4.0f, 16, 8, 8),
                PartPose.offsetAndRotation(x * 10.0f, y * -8.0f, z * 18.0f, 0, 1.5707964f, 0));

        dynamites[index] = dynamite.bake(64, 64);
        yPos[index] = y * -8.0f;
    }

    @Override
    public void renderToBuffer(@NotNull PoseStack poseStack, @NotNull VertexConsumer vertexConsumers, int light, int overlay, float red, float green, float blue, float alpha) {
        poseStack.pushPose();
        poseStack.scale(sizemult * 0.25F, sizemult * 0.25F, sizemult * 0.25F);

        for (ModelPart dynamite : dynamites) {
            if (!dynamite.visible) continue;
            dynamite.render(poseStack, vertexConsumers, light, OverlayTexture.pack(OverlayTexture.u((sizemult -1) * 2), 10), red, green, blue, alpha);
        }

        poseStack.popPose();
    }

    @Override
    public void applyEffects(final ModuleBase module, PoseStack matrixStack, MultiBufferSource rtb, final float yaw, final float pitch, final float roll) {
        if (module == null) {
            sizemult = 0.5f + 1.0f;
            for (ModelPart dynamite : dynamites) {
                dynamite.visible = true;
            }
        } else {
            float fusemult = (float) Math.abs(Math.sin(((ModuleDynamite) module).getFuse() / (float) ((ModuleDynamite) module).getFuseLength() * 3.141592653589793 * 6.0));
            sizemult = fusemult * 0.5f + 1.0f;
            float size = ((ModuleDynamite) module).explosionSize();
            float max = 44.0f;
            float perModel = max / dynamites.length;
            for (int j = 0; j < dynamites.length; ++j) {
                dynamites[j].visible = !(j * perModel >= size);
                dynamites[j].y = yPos[j] + (-24.0f / sizemult);
            }
        }
    }
}
