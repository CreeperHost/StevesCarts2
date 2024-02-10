package vswe.stevescarts.client.models;

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
import vswe.stevescarts.modules.realtimers.ModuleCakeServer;

public class ModelCake extends ModelCartbase {

    private final ModelPart[] cakes = new ModelPart[6];

    public ModelCake() {
        super(null, ResourceHelper.getResource("/models/cakeModel.png"));
        makeModel();
    }

    public void makeModel() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        for (int cake = 0; cake < 6; ++cake) {
            int slices = 6 - cake;
            partdefinition.addOrReplaceChild("cake" + cake, CubeListBuilder.create()
                            .texOffs(0, 22 * (6 - slices))
                            .addBox(-7.0f, -4.0f, -7.0f, 2 * slices + ((slices == 6) ? 2 : 1), 8, 14),
                    PartPose.offset(0.0f, -9.0f, 0.0f)
            );
        }

        root = LayerDefinition.create(meshdefinition, 64, 256).bakeRoot();
        for (int slices = 0; slices < 6; ++slices) {
            cakes[slices] = root.getChild("cake" + slices);
        }
    }

    @Override
    public void applyEffects(ModuleBase module, PoseStack matrixStack, MultiBufferSource rtb, float yaw, float pitch, float roll) {
        int count;
        if (module != null) {
            count = ((ModuleCakeServer) module).getRenderSliceCount();
        } else {
            count = 6;
        }
        for (int i = 0; i < cakes.length; ++i) {
            cakes[i].visible = 6 - i == count;
        }
        super.applyEffects(module, matrixStack, rtb, yaw, pitch, roll);
    }
}
