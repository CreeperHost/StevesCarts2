package vswe.stevescarts.client.models;

import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import vswe.stevescarts.api.client.ModelCartbase;
import vswe.stevescarts.helpers.ResourceHelper;

public class ModelCleaner extends ModelCartbase {

    public ModelCleaner() {
        super(null, ResourceHelper.getResource("/models/cleanerModel.png"));
        makeModel();
    }

    public void makeModel() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition box = partdefinition.addOrReplaceChild("box", CubeListBuilder.create()
                        .texOffs(0, 0)
                        .addBox(-4.0f, -3.0f, -4.0f, 8, 6, 8),
                PartPose.offset(4.0f, -0.0f, -0.0f)
        );

        for (int i = 0; i < 2; ++i) {
            PartDefinition sidetube = partdefinition.addOrReplaceChild("sidetube" + i, CubeListBuilder.create()
                            .texOffs(0, 14)
                            .addBox(-2.0f, -2.0f, -1.0f, 4, 4, 2),
                    PartPose.offset(4.0f, -0.0f, -5.0f * (i * 2 - 1))
            );
        }

        PartDefinition tube = partdefinition.addOrReplaceChild("tube", CubeListBuilder.create()
                        .texOffs(0, 14)
                        .addBox(-2.0f, -2.0f, -1.0f, 4, 4, 2),
                PartPose.offsetAndRotation(-1.0f, 0.0f, 0.0f, 0, 1.5707964f, 0)
        );

        for (int j = 0; j < 2; ++j) {
            PartDefinition endtube = partdefinition.addOrReplaceChild("endtube" + j, CubeListBuilder.create()
                            .texOffs(0, 14)
                            .addBox(-2.0f, -2.0f, -1.0f, 4, 4, 2),
                    PartPose.offsetAndRotation(-7.0f, -0.0f, -3.0f * (j * 2 - 1), 0, 1.5707964f, 0)
            );
        }

        PartDefinition connectiontube = partdefinition.addOrReplaceChild("connectiontube", CubeListBuilder.create()
                        .texOffs(0, 20)
                        .addBox(-5.0f, -5.0f, -1.0f, 10, 4, 4),
                PartPose.offsetAndRotation(-5.0f, 3.0f, 0.0f, 0, 1.5707964f, 0)
        );

        for (int j = 0; j < 2; ++j) {
            PartDefinition externaltube = partdefinition.addOrReplaceChild("externaltube" + j, CubeListBuilder.create()
                            .texOffs(0, 14)
                            .addBox(-2.0f, -2.0f, -1.0f, 4, 4, 2),
                    PartPose.offsetAndRotation(-10.95f, -0.0f, -3.05f * (j * 2 - 1), 0, 1.5707964f, 0)
            );
        }

        root = LayerDefinition.create(meshdefinition, 32, 32).bakeRoot();
    }
}