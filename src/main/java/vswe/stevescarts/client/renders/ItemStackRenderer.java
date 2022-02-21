package vswe.stevescarts.client.renders;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Vector3f;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.nbt.ByteArrayTag;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.item.ItemStack;
import vswe.stevescarts.client.models.ModelCartbase;
import vswe.stevescarts.entitys.EntityMinecartModular;
import vswe.stevescarts.init.ModItems;
import vswe.stevescarts.modules.ModuleBase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ItemStackRenderer extends BlockEntityWithoutLevelRenderer
{
    public ItemStackRenderer(BlockEntityRenderDispatcher p_172550_, EntityModelSet p_172551_) {
        super(p_172550_, p_172551_);
    }

    @Override
    public void renderByItem(ItemStack itemStack, ItemTransforms.TransformType transformType, PoseStack matrixStack, MultiBufferSource iRenderTypeBuffer, int p_239207_5_, int p_239207_6_)
    {
        if (itemStack.getItem() != ModItems.CARTS.get())
        {
            super.renderByItem(itemStack, transformType, matrixStack, iRenderTypeBuffer, p_239207_5_, p_239207_6_);
            return;
        }
        matrixStack.pushPose();
        matrixStack.scale(-1.0f, -1.0f, 1.0f);
        CompoundTag info = itemStack.getTag();
        if (info != null && info.contains("Modules"))
        {
            ByteArrayTag moduleIDTag = (ByteArrayTag) info.get("Modules");
            byte[] bytes = moduleIDTag.getAsByteArray();
            HashMap<String, ModelCartbase> models = new HashMap<>();
            List<ModuleBase> moduleBaseList = new ArrayList<>();
            float lowestMult = 1.0f;
            VertexConsumer ivertexbuilder = iRenderTypeBuffer.getBuffer(RenderType.solid());

            EntityMinecartModular cart = new EntityMinecartModular(Minecraft.getInstance().level, 0, 0, 0, itemStack.getTag(), new TextComponent(""));

            if (transformType == ItemTransforms.TransformType.GUI)
            {
                matrixStack.translate(-1, 0, 0);
                matrixStack.scale(lowestMult, lowestMult, lowestMult);
            }
            else
            {
                matrixStack.translate(0.25, -0.25, 0.25);
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
