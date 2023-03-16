package vswe.stevescarts.client.models;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.renderer.MultiBufferSource;
import vswe.stevescarts.api.client.ModelCartbase;
import vswe.stevescarts.api.modules.ModuleBase;
import vswe.stevescarts.helpers.ResourceHelper;
import vswe.stevescarts.modules.realtimers.ModuleSeat;
import vswe.stevescarts.modules.realtimers.ModuleShooterAdv;

public class ModelSniperRifle extends ModelCartbase {


	public ModelSniperRifle() {
		super(buildModel(), ResourceHelper.getResource("/models/gunModel.png"));
	}

	public static ModelPart buildModel() {
		MeshDefinition modelData = new MeshDefinition();
		PartDefinition modelPartData = modelData.getRoot();

		PartDefinition gun = modelPartData.addOrReplaceChild("sniper", CubeListBuilder.create()
						.texOffs(0, 16)
						.addBox(-1.5f + 2.5F, -2.5f, -1.5f, 7, 3, 3),
				PartPose.offsetAndRotation(0, -9.0f, 0.0f, 0, 0, 0));

		return gun.bake(32, 8);
	}

		@Override
	public void applyEffects(final ModuleBase module, PoseStack matrixStack, MultiBufferSource rtb, final float yaw, final float pitch, final float roll) {
		//This does not perfectly match the original behavior. But its close enough. I dont feel like spending hours screwing with the new model system.
		root.zRot = ((module == null) ? 0.0f : ((ModuleShooterAdv) module).getPipeRotation(0) * 0.75F);
		root.yRot = root.zRot != 0 ? 0 : ((module == null) ? 0.0f : ((float)Math.PI + ((ModuleShooterAdv) module).getRifleDirection() + ((yaw + 180) * -0.0174532F)));
	}
}