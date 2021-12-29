package vswe.stevescarts.client.models.storages.chests;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.util.ResourceLocation;
import vswe.stevescarts.client.models.ModelCartbase;
import vswe.stevescarts.helpers.ResourceHelper;
import vswe.stevescarts.modules.ModuleBase;
import vswe.stevescarts.modules.storages.chests.ModuleChest;

public class ModelFrontChest extends ModelCartbase
{
    private static ResourceLocation texture;
    ModelRenderer lid;

    @Override
    public ResourceLocation getResource(final ModuleBase module)
    {
        return ModelFrontChest.texture;
    }

    @Override
    protected int getTextureHeight()
    {
        return 32;
    }

    public ModelFrontChest()
    {
        super();
        lid = AddChest();
    }

    private ModelRenderer AddChest()
    {
        final ModelRenderer chestAnchor = new ModelRenderer(this);
        AddRenderer(chestAnchor);
        chestAnchor.yRot = 1.5707964f;
        chestAnchor.setPos(-3.5f, 0.0f, 0.0f);
        final ModelRenderer base = new ModelRenderer(this, 0, 11);
        fixSize(base);
        chestAnchor.addChild(base);
        base.addBox(7.0f, 3.0f, 4.0f, 14, 6, 8, 0.0f);
        base.setPos(-14.0f, -5.5f, -18.5f);
        final ModelRenderer lid = new ModelRenderer(this, 0, 0);
        fixSize(lid);
        chestAnchor.addChild(lid);
        lid.addBox(7.0f, -3.0f, -8.0f, 14, 3, 8, 0.0f);
        lid.setPos(-14.0f, -1.5f, -6.5f);
        final ModelRenderer lock = new ModelRenderer(this, 0, 25);
        fixSize(lock);
        lid.addChild(lock);
        lock.addBox(1.0f, 1.5f, 0.5f, 2, 3, 1, 0.0f);
        lock.setPos(12.0f, -3.0f, -9.5f);
        return lid;
    }

    @Override
    public void applyEffects(final ModuleBase module, MatrixStack matrixStack, IRenderTypeBuffer rtb, final float yaw, final float pitch, final float roll)
    {
        lid.xRot = ((module == null) ? 0.0f : (-((ModuleChest) module).getChestAngle()));
    }

    static
    {
        ModelFrontChest.texture = ResourceHelper.getResource("/models/frontChestModel.png");
    }
}
