package vswe.stevescarts.client.models.storages.chests;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.resources.ResourceLocation;
import vswe.stevescarts.client.models.ModelCartbase;
import vswe.stevescarts.helpers.ResourceHelper;
import vswe.stevescarts.modules.ModuleBase;

public class ModelExtractingChests extends ModelCartbase
{
    private static ResourceLocation texture;
    ModelPart lid1;
    ModelPart lid2;
    ModelPart base1;
    ModelPart base2;

    @Override
    public ResourceLocation getResource(final ModuleBase module)
    {
        return ModelExtractingChests.texture;
    }

    public ModelExtractingChests()
    {
        super();
        ModelPart[] temp = AddChest(false);
//        base1 = temp[0];
//        lid1 = temp[1];
//        temp = AddChest(true);
//        base2 = temp[0];
//        lid2 = temp[1];
    }

    private ModelPart[] AddChest(final boolean opposite)
    {
        //TODO
//        final ModelRenderer chestAnchor = new ModelRenderer(this);
//        AddRenderer(chestAnchor);
//        if (opposite)
//        {
//            chestAnchor.yRot = 3.1415927f;
//        }
//        final ModelRenderer base = new ModelRenderer(this, 0, 17);
//        fixSize(base);
//        chestAnchor.addChild(base);
//        base.addBox(8.0f, 3.0f, 2.0f, 16, 6, 14, 0.0f);
//        base.setPos(-16.0f, -5.5f, -14.0f);
//        final ModelRenderer lid = new ModelRenderer(this, 0, 0);
//        fixSize(lid);
//        chestAnchor.addChild(lid);
//        lid.addBox(8.0f, -3.0f, -14.0f, 16, 3, 14, 0.0f);
//        lid.setPos(-16.0f, -1.5f, 2.0f);
//        final ModelRenderer lock = new ModelRenderer(this, 0, 37);
//        fixSize(lock);
//        lid.addChild(lock);
//        lock.addBox(1.0f, 1.5f, 0.5f, 2, 3, 1, 0.0f);
//        lock.setPos(14.0f, -3.0f, -15.5f);
//        return new ModelRenderer[]{base, lid};
        return null;
    }

    @Override
    public void applyEffects(final ModuleBase module, PoseStack matrixStack, VertexConsumer rtb, final float yaw, final float pitch, final float roll)
    {
        //		if (module == null) {
        //			lid1.rotateAngleX = 0.0f;
        //			lid2.rotateAngleX = 0.0f;
        //			lid1.rotationPointZ = 2.0f;
        //			lid2.rotationPointZ = 2.0f;
        //			base1.rotationPointZ = -14.0f;
        //			base2.rotationPointZ = -14.0f;
        //		} else {
        //			final ModuleExtractingChests chest = (ModuleExtractingChests) module;
        //			lid1.rotateAngleX = -chest.getChestAngle();
        //			lid2.rotateAngleX = -chest.getChestAngle();
        //			lid1.rotationPointZ = chest.getChestOffset() + 16.0f;
        //			lid2.rotationPointZ = chest.getChestOffset() + 16.0f;
        //			base1.rotationPointZ = chest.getChestOffset();
        //			base2.rotationPointZ = chest.getChestOffset();
        //		}
    }

    static
    {
        ModelExtractingChests.texture = ResourceHelper.getResource("/models/codeSideChestsModel.png");
    }
}
