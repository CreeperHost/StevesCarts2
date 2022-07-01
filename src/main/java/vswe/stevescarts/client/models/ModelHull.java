package vswe.stevescarts.client.models;

import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.resources.ResourceLocation;
import vswe.stevescarts.api.client.ModelCartbase;

public class ModelHull extends ModelCartbase
{
    public ModelHull(ResourceLocation texture)
    {
        super(createBodyLayer().bakeRoot(), texture);
    }

    public static LayerDefinition createBodyLayer()
    {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        partdefinition.addOrReplaceChild("bottom", CubeListBuilder.create().texOffs(0, 0)
                .addBox(-10.0F, -8.0F, -1.0F, 20.0F, 16.0F, 2.0F),
                PartPose.offsetAndRotation(0.0F, 4.0F, 0.0F, ((float)Math.PI / 2F), 0.0F, 0.0F));

        partdefinition.addOrReplaceChild("front", CubeListBuilder.create().texOffs(0, 18)
                .addBox(-8.0F, -9.0F, -1.0F, 16.0F, 8.0F, 2.0F),
                PartPose.offsetAndRotation(-9.0F, 4.0F, 0.0F, 0.0F, ((float)Math.PI * 1.5F), 0.0F));

        partdefinition.addOrReplaceChild("back", CubeListBuilder.create().texOffs(0, 18)
                .addBox(-8.0F, -9.0F, -1.0F, 16.0F, 8.0F, 2.0F),
                PartPose.offsetAndRotation(9.0F, 4.0F, 0.0F, 0.0F, ((float)Math.PI / 2F), 0.0F));

        partdefinition.addOrReplaceChild("left", CubeListBuilder.create().texOffs(0, 18)
                .addBox(-8.0F, -9.0F, -1.0F, 16.0F, 8.0F, 2.0F),
                PartPose.offsetAndRotation(0.0F, 4.0F, -7.0F, 0.0F, (float)Math.PI, 0.0F));

        partdefinition.addOrReplaceChild("right", CubeListBuilder.create().texOffs(0, 18)
                .addBox(-8.0F, -9.0F, -1.0F, 16.0F, 8.0F, 2.0F),
                PartPose.offset(0.0F, 4.0F, 7.0F));

        return LayerDefinition.create(meshdefinition, 64, 32);
    }
}
