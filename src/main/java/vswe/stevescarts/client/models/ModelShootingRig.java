package vswe.stevescarts.client.models;

import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import vswe.stevescarts.api.client.ModelCartbase;
import vswe.stevescarts.helpers.ResourceHelper;

public class ModelShootingRig extends ModelCartbase {

	public ModelShootingRig() {
		super(buildModels(), ResourceHelper.getResource("/models/rigModel.png"));
	}

	public static ModelPart buildModels() {
		MeshDefinition modelData = new MeshDefinition();
		PartDefinition modelPartData = modelData.getRoot();

		PartDefinition base = modelPartData.addOrReplaceChild("base", CubeListBuilder.create()
				.addBox(-7.0f, -0.5f, -3.0f, 14, 1, 6),
				PartPose.offsetAndRotation(0.0f, -5.5f, -0.0f, 0, 1.5707964f, 0));

		base.addOrReplaceChild("pillar", CubeListBuilder.create()
						.texOffs(0, 7)
				.addBox(-2.0f, -2.5f, -2.0f, 4, 5, 4),
				PartPose.offset(0.0f, -8.0f + 5.5f, -0.0f));

		base.addOrReplaceChild("top", CubeListBuilder.create()
						.texOffs(16, 7)
						.addBox(-3.0f, -1.0f, -3.0f, 6, 2, 6),
				PartPose.offset(0.0f, -11.0f + 5.5f, -0.0f));

		return base.bake(64, 16);
	}
}