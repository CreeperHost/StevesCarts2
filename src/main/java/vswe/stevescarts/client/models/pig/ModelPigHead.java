package vswe.stevescarts.client.models.pig;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.layers.EquipmentLayerRenderer;
import net.minecraft.client.resources.model.EquipmentClientInfo;
import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.equipment.Equippable;
import vswe.stevescarts.api.client.ModelCartbase;
import vswe.stevescarts.api.modules.ModuleBase;
import vswe.stevescarts.helpers.ResourceHelper;
import vswe.stevescarts.modules.hull.ModulePig;

public class ModelPigHead extends ModelCartbase {
    private static final Identifier UNUSED_TEXTURE = Identifier.withDefaultNamespace("textures/misc/white.png");
    private static final ModelCartbase HELMET_MODEL = new ModelCartbase(getHelmetModelData().bakeRoot(), UNUSED_TEXTURE) {
    };

    private static EquipmentLayerRenderer equipmentRenderer;

    public ModelPigHead() {
        super(getTexturedModelData().bakeRoot(), ResourceHelper.getResourceFromPath("/entity/pig/pig_temperate.png"));
    }

    public static void setEquipmentRenderer(EquipmentLayerRenderer renderer) {
        equipmentRenderer = renderer;
    }

    public static LayerDefinition getTexturedModelData() {
        MeshDefinition modelData = new MeshDefinition();
        PartDefinition modelPartData = modelData.getRoot();

        PartDefinition head = modelPartData.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 0)
                        .addBox(-4.0f, -4.0f, -8.0f, 8, 8, 8),
                PartPose.offsetAndRotation(-9.0f, -5.0f, 0.0f, 0.0f, 1.5707964f, 0.0f));

        head.addOrReplaceChild("box", CubeListBuilder.create().texOffs(16, 16)
                        .addBox(-2.0f, 0.0f, -9.0f, 4, 3, 1),
                PartPose.ZERO);

        return LayerDefinition.create(modelData, 64, 64);
    }

    private static LayerDefinition getHelmetModelData() {
        MeshDefinition modelData = new MeshDefinition();
        modelData.getRoot().addOrReplaceChild("helmet", CubeListBuilder.create().texOffs(0, 0)
                        .addBox(-4.0F, -4.0F, -8.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(1.0F)),
                PartPose.offsetAndRotation(-9.0F, -5.0F, 0.0F, 0.0F, 1.5707964F, 0.0F));
        return LayerDefinition.create(modelData, 64, 32);
    }

    @Override
    public void submitExtraGeometry(ModuleBase module, PoseStack poseStack, SubmitNodeCollector nodeCollector, int light) {
        if (!(module instanceof ModulePig pig) || equipmentRenderer == null) {
            return;
        }

        ItemStack helmet = pig.getHelmet();
        Equippable equippable = helmet.get(DataComponents.EQUIPPABLE);
        if (equippable == null || equippable.slot() != EquipmentSlot.HEAD || equippable.assetId().isEmpty()) {
            return;
        }

        equipmentRenderer.renderLayers(EquipmentClientInfo.LayerType.HUMANOID, equippable.assetId().orElseThrow(),
                HELMET_MODEL, null, helmet, poseStack, nodeCollector, light, 0);
    }
}
