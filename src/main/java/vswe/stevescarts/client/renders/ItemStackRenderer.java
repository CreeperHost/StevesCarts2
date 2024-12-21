package vswe.stevescarts.client.renders;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ChestModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.special.ChestSpecialRenderer;
import net.minecraft.client.renderer.special.SpecialModelRenderer;
import net.minecraft.client.resources.model.Material;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.jetbrains.annotations.Nullable;
import vswe.stevescarts.api.client.ModelCartbase;
import vswe.stevescarts.api.modules.ModuleBase;
import vswe.stevescarts.entities.ModularMinecart;
import vswe.stevescarts.init.ModItemData;
import vswe.stevescarts.init.ModItems;

public class ItemStackRenderer implements SpecialModelRenderer<ItemStackRenderer.Data> {
    public static ItemStackRenderer instance;

//    public ItemStackRenderer(BlockEntityRenderDispatcher p_172550_, EntityModelSet p_172551_)
//    {
//        super(p_172550_, p_172551_);
//    }


    @Override
    public void render(@Nullable ItemStackRenderer.Data data, ItemDisplayContext transformType, PoseStack matrixStack, MultiBufferSource iRenderTypeBuffer, int light, int overlay, boolean b) {
        ItemStack stack = data.stack;
        if (stack.getItem() != ModItems.CARTS.get()) {
            return;
        }
        matrixStack.pushPose();
        matrixStack.scale(-1.0f, -1.0f, 1.0f);
        CompoundTag info = ModItemData.getTagCopy(stack);
        if (info.contains("modules")) {
            float lowestMult = 1.0f;
            ModularMinecart cart = new ModularMinecart(Minecraft.getInstance().level, 0, 0, 0, info);

            if (transformType == ItemDisplayContext.GUI) {
                matrixStack.translate(-1, 0, 0);
                matrixStack.scale(lowestMult, lowestMult, lowestMult);
            } else {
                matrixStack.translate(-0.5, -0.5, 0.5);
            }
            matrixStack.mulPose(Axis.ZP.rotationDegrees(180));
            matrixStack.mulPose(Axis.XP.rotationDegrees(180));

            if (cart.modules() != null) {
                for (ModuleBase module : cart.modules()) {
                    if (module.getModels() != null) {
                        for (ModelCartbase model : module.getModels()) {
                            VertexConsumer buffer = iRenderTypeBuffer.getBuffer(model.getRenderType(module));
                            model.applyEffects(module, matrixStack, iRenderTypeBuffer, 0, 0, 0);
                            model.renderToBuffer(matrixStack, buffer, light, overlay, 0xFFFFFFFF);
                        }
                    }
                }
            }
        }
        matrixStack.popPose();
    }

    @Nullable
    @Override
    public Data extractArgument(ItemStack itemStack) {
        return new Data(itemStack);
    }

//    @Override
//    public void renderByItem(ItemStack itemStack, ItemDisplayContext transformType, @NotNull PoseStack matrixStack, @NotNull MultiBufferSource iRenderTypeBuffer, int p_239207_5_, int p_239207_6_)
//    {
//        if (itemStack.getItem() != ModItems.CARTS.get())
//        {
//            super.renderByItem(itemStack, transformType, matrixStack, iRenderTypeBuffer, p_239207_5_, p_239207_6_);
//            return;
//        }
//        matrixStack.pushPose();
//        matrixStack.scale(-1.0f, -1.0f, 1.0f);
//        CompoundTag info = ModItemData.getTagCopy(itemStack);
//        if (info.contains("modules"))
//        {
//            float lowestMult = 1.0f;
//            ModularMinecart cart = new ModularMinecart(Minecraft.getInstance().level, 0, 0, 0, info);
//
//            if (transformType == ItemDisplayContext.GUI)
//            {
//                matrixStack.translate(-1, 0, 0);
//                matrixStack.scale(lowestMult, lowestMult, lowestMult);
//            }
//            else
//            {
//                matrixStack.translate(-0.5, -0.5, 0.5);
//            }
//            matrixStack.mulPose(Axis.ZP.rotationDegrees(180));
//            matrixStack.mulPose(Axis.XP.rotationDegrees(180));
//
//            if (cart.modules() != null)
//            {
//                for (ModuleBase module : cart.modules())
//                {
//                    if (module.getModels() != null)
//                    {
//                        for (ModelCartbase model : module.getModels())
//                        {
//                            VertexConsumer buffer = iRenderTypeBuffer.getBuffer(model.getRenderType(module));
//                            model.applyEffects(module, matrixStack, iRenderTypeBuffer, 0, 0, 0);
//                            model.renderToBuffer(matrixStack, buffer, p_239207_5_, p_239207_6_, 0xFFFFFFFF);
//                        }
//                    }
//                }
//            }
//        }
//        matrixStack.popPose();
//    }

    public static ItemStackRenderer getInstance() {
        if (instance == null) {
            instance = new ItemStackRenderer();
        }
        return instance;
    }

    public static class Data {
        public ItemStack stack;
        public Data(ItemStack stack) {
            this.stack = stack;
        }
    }

    public static record Unbaked() implements SpecialModelRenderer.Unbaked {
        public static final MapCodec<ItemStackRenderer.Unbaked> MAP_CODEC = RecordCodecBuilder.mapCodec((builder) -> {
            return builder.stable(new ItemStackRenderer.Unbaked());
        });

        public MapCodec<ItemStackRenderer.Unbaked> type() {
            return MAP_CODEC;
        }

        public SpecialModelRenderer<?> bake(EntityModelSet p_387681_) {
            return new ItemStackRenderer();
        }
    }
}
