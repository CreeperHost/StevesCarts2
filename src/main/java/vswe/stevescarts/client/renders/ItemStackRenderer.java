package vswe.stevescarts.client.renders;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.tileentity.ItemStackTileEntityRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.ByteArrayNBT;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.util.text.TranslationTextComponent;
import vswe.stevescarts.client.models.ModelCartbase;
import vswe.stevescarts.entitys.EntityMinecartModular;
import vswe.stevescarts.init.ModItems;
import vswe.stevescarts.modules.ModuleBase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ItemStackRenderer extends ItemStackTileEntityRenderer
{
    @Override
    public void renderByItem(ItemStack itemStack, ItemCameraTransforms.TransformType transformType, MatrixStack matrixStack, IRenderTypeBuffer iRenderTypeBuffer, int p_239207_5_, int p_239207_6_)
    {
        if (itemStack.getItem() != ModItems.CARTS.get())
        {
            super.renderByItem(itemStack, transformType, matrixStack, iRenderTypeBuffer, p_239207_5_, p_239207_6_);
            return;
        }
        matrixStack.pushPose();
        matrixStack.scale(-1.0f, -1.0f, 1.0f);
        CompoundNBT info = itemStack.getTag();
        if (info != null && info.contains("Modules"))
        {
            ByteArrayNBT moduleIDTag = (ByteArrayNBT) info.get("Modules");
            byte[] bytes = moduleIDTag.getAsByteArray();
            HashMap<String, ModelCartbase> models = new HashMap<>();
            List<ModuleBase> moduleBaseList = new ArrayList<>();
            float lowestMult = 1.0f;
            IVertexBuilder ivertexbuilder = iRenderTypeBuffer.getBuffer(RenderType.solid());

            EntityMinecartModular cart = new EntityMinecartModular(Minecraft.getInstance().level, 0, 0, 0, itemStack.getTag(), new TranslationTextComponent(""));

            if (transformType == ItemCameraTransforms.TransformType.GUI)
            {
                matrixStack.translate(-1, 0, 0);
                matrixStack.scale(lowestMult, lowestMult, lowestMult);
            }
            else
            {
                matrixStack.translate(-0.5, -0.5, 0.5);
            }
            matrixStack.mulPose(Vector3f.ZP.rotationDegrees(180));
            matrixStack.mulPose(Vector3f.XP.rotationDegrees(180));

            for (ModuleBase module : cart.getModules())
            {
                if (module.getModels() != null)
                {
                    for (ModelCartbase model : module.getModels())
                    {
                        iRenderTypeBuffer.getBuffer(model.getRenderType(module));
                        model.renderToBuffer(matrixStack, ivertexbuilder, p_239207_5_, p_239207_6_, 1.0F, 1.0F, 1.0F, 1.0F);
                    }
                }
            }
        }
        matrixStack.popPose();
    }
}
