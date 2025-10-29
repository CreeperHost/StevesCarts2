package vswe.stevescarts.client.renders;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.special.SpecialModelRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.ProblemReporter;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.TagValueInput;
import net.minecraft.world.level.storage.ValueInput;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;
import vswe.stevescarts.api.client.ModelCartbase;
import vswe.stevescarts.api.modules.ModuleBase;
import vswe.stevescarts.entities.ModularMinecart;
import vswe.stevescarts.init.ModItemData;
import vswe.stevescarts.init.ModItems;

import java.util.Set;

public class ItemStackRenderer implements SpecialModelRenderer<ItemStackRenderer.Data> {

    @Override
    public void submit(@Nullable ItemStackRenderer.Data data, ItemDisplayContext transformType, PoseStack matrixStack, SubmitNodeCollector nodeCollector, int light, int overlay, boolean b, int i2) {
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
//                matrixStack.scale(lowestMult, lowestMult, lowestMult);
            } else {
                matrixStack.translate(-0.5, -0.5, 0.5);
            }
            matrixStack.mulPose(Axis.ZP.rotationDegrees(180));
            matrixStack.mulPose(Axis.XP.rotationDegrees(180));

            if (cart.modules() != null) {
                for (ModuleBase module : cart.modules()) {
                    if (module.getModels() != null) {
                        for (ModelCartbase model : module.getModels()) {
                            model.applyEffects(module, matrixStack, 0, 0, 0);
                            nodeCollector.submitModel(model, null, matrixStack, model.getRenderType(module), 240, OverlayTexture.NO_OVERLAY, 0, null);
                        }
                    }
                }
            }
        }
        matrixStack.popPose();
    }

    @Override
    public void getExtents(Set<Vector3f> set) {
        set.add(new Vector3f());
    }

    @Nullable
    @Override
    public Data extractArgument(ItemStack itemStack) {
        return new Data(itemStack);
    }

    public static class Data {
        public ItemStack stack;
        public Data(ItemStack stack) {
            this.stack = stack;
        }
    }

//    public static record Unbaked() implements SpecialModelRenderer.Unbaked {
//        public static final MapCodec<ItemStackRenderer.Unbaked> MAP_CODEC = RecordCodecBuilder.mapCodec((builder) -> {
//            return builder.stable(new ItemStackRenderer.Unbaked());
//        });
//
//        public MapCodec<ItemStackRenderer.Unbaked> type() {
//            return MAP_CODEC;
//        }
//
//        public SpecialModelRenderer<?> bake(EntityModelSet p_387681_) {
//            return new ItemStackRenderer();
//        }
//    }
}
