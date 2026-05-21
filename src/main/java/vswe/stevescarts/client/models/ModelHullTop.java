package vswe.stevescarts.client.models;

import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.resources.Identifier;
import vswe.stevescarts.api.client.ModelCartbase;
import vswe.stevescarts.api.modules.ModuleBase;

public class ModelHullTop extends ModelCartbase {
    public ModelHullTop(Identifier resourceLocation) {
        super(getTexturedModelData().bakeRoot(), resourceLocation);
    }

    public static LayerDefinition getTexturedModelData() {
        MeshDefinition modelData = new MeshDefinition();
        PartDefinition modelPartData = modelData.getRoot();
        modelPartData.addOrReplaceChild("top", CubeListBuilder.create()
                        .texOffs(0, 0)
                        .addBox(-8.0f, -6.0f, -1.0f, 16, 12, 2),
                PartPose.offsetAndRotation(0.0f, -4.0f, 0.0f, -1.5707964f, 0.0f, 0.0f)
        );
        return LayerDefinition.create(modelData, 64, 16);
    }

    @Override
    public RenderType getRenderType(ModuleBase moduleBase) {
        return RenderTypes.entityCutout(getTexture());
    }
}
