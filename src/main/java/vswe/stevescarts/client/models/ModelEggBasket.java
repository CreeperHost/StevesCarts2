package vswe.stevescarts.client.models;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.util.ResourceLocation;
import vswe.stevescarts.helpers.ResourceHelper;
import vswe.stevescarts.modules.ModuleBase;
import vswe.stevescarts.modules.storages.chests.ModuleEggBasket;

public class ModelEggBasket extends ModelCartbase
{
    private static ResourceLocation texture;
    ModelRenderer chesttop;

    @Override
    public ResourceLocation getResource(final ModuleBase module)
    {
        return ModelEggBasket.texture;
    }

    @Override
    protected int getTextureHeight()
    {
        return 128;
    }

    public ModelEggBasket()
    {
        super();
        for (int i = 0; i < 2; ++i)
        {
            final ModelRenderer chestside = new ModelRenderer(this, 0, 13);
            AddRenderer(chestside);
            chestside.addBox(-8.0f, -2.5f, -0.5f, 16, 5, 1, 0.0f);
            chestside.setPos(0.0f, -8.5f, -5.5f + i * 11);
            final ModelRenderer chestfrontback = new ModelRenderer(this, 0, 19);
            AddRenderer(chestfrontback);
            chestfrontback.addBox(-5.0f, -2.5f, -0.5f, 10, 5, 1, 0.0f);
            chestfrontback.setPos(-7.5f + i * 15, -8.5f, 0.0f);
            chestfrontback.yRot = 1.5707964f;
            final ModelRenderer chesthandle = new ModelRenderer(this, 0, 36);
            AddRenderer(chesthandle);
            chesthandle.addBox(-1.0f, -1.5f, -0.5f, 2, 3, 1, 0.0f);
            chesthandle.setPos(0.0f, -12.5f, -5.5f + i * 11);
            final ModelRenderer chesthandlesmall = new ModelRenderer(this, 0, 40);
            AddRenderer(chesthandlesmall);
            chesthandlesmall.addBox(-1.0f, -0.5f, -0.5f, 2, 1, 1, 0.0f);
            chesthandlesmall.setPos(0.0f, -14.5f, -4.5f + i * 9);
        }
        AddRenderer(chesttop = new ModelRenderer(this, 0, 0));
        chesttop.addBox(-7.0f, -5.0f, -0.5f, 14, 10, 1, 0.0f);
        chesttop.setPos(0.0f, -11.5f, 0.0f);
        chesttop.xRot = 1.5707964f;
        chesttop.yRot = 0.1f;
        final ModelRenderer chestbot = new ModelRenderer(this, 0, 25);
        AddRenderer(chestbot);
        chestbot.addBox(-7.0f, -5.0f, -0.5f, 14, 10, 1, 0.0f);
        chestbot.setPos(0.0f, -5.5f, 0.0f);
        chestbot.xRot = 1.5707964f;
        final ModelRenderer chesthandletop = new ModelRenderer(this, 0, 42);
        AddRenderer(chesthandletop);
        chesthandletop.addBox(-1.0f, -4.0f, -0.5f, 2, 8, 1, 0.0f);
        chesthandletop.setPos(0.0f, -15.5f, 0.0f);
        chesthandletop.xRot = 1.5707964f;
        for (int j = 0; j < 12; ++j)
        {
            addEgg(j);
        }
    }

    private void addEgg(final int id)
    {
        final int x = id % 3;
        final int y = id / 3;
        final float xCoord = -3.0f + x * 3.3333333f;
        final float yCoord = -5.0f + y * 3.5f;
        final int textureY = 19 + id * 5;
        final ModelRenderer eggbot = new ModelRenderer(this, 30, textureY);
        AddRenderer(eggbot);
        eggbot.addBox(-1.0f, -0.5f, -1.0f, 2, 1, 2, 0.0f);
        eggbot.setPos(yCoord, -6.5f, xCoord);
        final ModelRenderer eggbase = new ModelRenderer(this, 38, textureY);
        AddRenderer(eggbase);
        eggbase.addBox(-1.5f, -1.0f, -1.5f, 3, 2, 3, 0.0f);
        eggbase.setPos(yCoord, -7.5f, xCoord);
        final ModelRenderer eggmiddle = new ModelRenderer(this, 50, textureY);
        AddRenderer(eggmiddle);
        eggmiddle.addBox(-1.0f, -0.5f, -1.0f, 2, 1, 2, 0.0f);
        eggmiddle.setPos(yCoord, -8.75f, xCoord);
        final ModelRenderer eggtip = new ModelRenderer(this, 58, textureY);
        AddRenderer(eggtip);
        eggtip.addBox(-0.5f, -0.5f, -0.5f, 1, 1, 1, 0.0f);
        eggtip.setPos(yCoord, -9.25f, xCoord);
    }

    @Override
    public void applyEffects(final ModuleBase module, MatrixStack matrixStack, IRenderTypeBuffer rtb, final float yaw, final float pitch, final float roll)
    {
        if (module != null)
        {
            chesttop.yRot = 0.1f + ((ModuleEggBasket) module).getChestAngle();
        }
    }

    static
    {
        ModelEggBasket.texture = ResourceHelper.getResource("/models/chestModelEaster.png");
    }
}
