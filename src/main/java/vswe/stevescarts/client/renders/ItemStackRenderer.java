package vswe.stevescarts.client.renders;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Vector3f;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.client.renderer.item.ItemPropertyFunction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import vswe.stevescarts.api.client.ModelCartbase;
import vswe.stevescarts.entities.EntityMinecartModular;
import vswe.stevescarts.init.ModItems;
import vswe.stevescarts.api.modules.ModuleBase;

public class ItemStackRenderer extends BlockEntityWithoutLevelRenderer implements ItemPropertyFunction
{
    public static ItemStackRenderer instance;

    public ItemStackRenderer(BlockEntityRenderDispatcher p_172550_, EntityModelSet p_172551_)
    {
        super(p_172550_, p_172551_);
    }

    @Override
    public void renderByItem(ItemStack itemStack, ItemTransforms.@NotNull TransformType transformType, @NotNull PoseStack matrixStack, @NotNull MultiBufferSource iRenderTypeBuffer, int p_239207_5_, int p_239207_6_)
    {
        if (itemStack.getItem() != ModItems.CARTS.get())
        {
            super.renderByItem(itemStack, transformType, matrixStack, iRenderTypeBuffer, p_239207_5_, p_239207_6_);
            return;
        }
        matrixStack.pushPose();
        matrixStack.scale(-1.0f, -1.0f, 1.0f);
        CompoundTag info = itemStack.getTag();
        if (info != null && info.contains("modules"))
        {
            float lowestMult = 1.0f;
            VertexConsumer ivertexbuilder = iRenderTypeBuffer.getBuffer(RenderType.solid());

            EntityMinecartModular cart = new EntityMinecartModular(Minecraft.getInstance().level, 0, 0, 0, itemStack.getTag(), Component.literal(""));

            if (transformType == ItemTransforms.TransformType.GUI)
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

            if (cart.getModules() != null)
            {
                for (ModuleBase module : cart.getModules())
                {
                    if (module.getModels() != null)
                    {
                        for (ModelCartbase model : module.getModels())
                        {
                            iRenderTypeBuffer.getBuffer(model.getRenderType(module));
                            model.applyEffects(module, matrixStack, iRenderTypeBuffer, 0, 0, 0);
                            model.renderToBuffer(matrixStack, ivertexbuilder, p_239207_5_, p_239207_6_, 1.0F, 1.0F, 1.0F, 1.0F);
                        }
                    }
                }
            }
        }
        matrixStack.popPose();
    }

    public static ItemStackRenderer getInstance()
    {
        if (instance == null)
        {
            instance = new ItemStackRenderer(Minecraft.getInstance().getBlockEntityRenderDispatcher(), Minecraft.getInstance().getEntityModels());
        }
        return instance;
    }

    @Override
    public float call(@NotNull ItemStack p_174676_, @Nullable ClientLevel p_174677_, @Nullable LivingEntity p_174678_, int p_174679_)
    {
        return 1F;
    }
}
