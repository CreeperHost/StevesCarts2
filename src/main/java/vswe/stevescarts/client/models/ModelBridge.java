package vswe.stevescarts.client.models;

import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.Identifier;
import vswe.stevescarts.api.client.ModelCartbase;
import vswe.stevescarts.api.modules.ModuleBase;
import vswe.stevescarts.helpers.ResourceHelper;
import vswe.stevescarts.modules.workers.ModuleBridge;

public class ModelBridge extends ModelCartbase {
    private static final Identifier NORMAL = ResourceHelper.getResource("/models/aiModelNormal.png");
    private static final Identifier DOWN = ResourceHelper.getResource("/models/aiModelDown.png");
    private static final Identifier UP = ResourceHelper.getResource("/models/aiModelUp.png");
    private static final Identifier NORMAL_WARNING = ResourceHelper.getResource("/models/aiModelNormalWarning.png");
    private static final Identifier DOWN_WARNING = ResourceHelper.getResource("/models/aiModelDownWarning.png");
    private static final Identifier UP_WARNING = ResourceHelper.getResource("/models/aiModelUpWarning.png");

    public ModelBridge() {
        super(getTexturedModelData().bakeRoot(), NORMAL);
    }

    public static LayerDefinition getTexturedModelData() {
        MeshDefinition modelData = new MeshDefinition();
        PartDefinition modelPartData = modelData.getRoot();

        CubeListBuilder side = CubeListBuilder.create()
                .texOffs(0, 0)
                .addBox(1.0F, 3.0F, 0.5F, 2.0F, 6.0F, 1.0F);
        modelPartData.addOrReplaceChild("side_left", side,
                PartPose.offsetAndRotation(-11.5F, -6.0F, 8.0F, 0.0F, 1.5707964F, 0.0F));
        modelPartData.addOrReplaceChild("side_right", side,
                PartPose.offsetAndRotation(-11.5F, -6.0F, -4.0F, 0.0F, 1.5707964F, 0.0F));

        return LayerDefinition.create(modelData, 8, 8);
    }

    @Override
    public Identifier getResource(final ModuleBase module) {
        if (!(module instanceof ModuleBridge bridge)) {
            return NORMAL;
        }

        BlockPos next = bridge.getNextblock();
        int yDifference = bridge.getCart().getYTarget() - next.getY();
        if (bridge.needBridge()) {
            return yDifference > 0 ? UP_WARNING : yDifference < 0 ? DOWN_WARNING : NORMAL_WARNING;
        }
        return yDifference > 0 ? UP : yDifference < 0 ? DOWN : NORMAL;
    }
}
