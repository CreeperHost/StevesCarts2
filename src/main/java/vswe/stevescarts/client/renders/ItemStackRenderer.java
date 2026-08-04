package vswe.stevescarts.client.renders;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.special.SpecialModelRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import org.joml.Vector3f;
import org.joml.Vector3fc;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import vswe.stevescarts.api.client.ModelCartbase;
import vswe.stevescarts.api.modules.ModuleBase;
import vswe.stevescarts.api.modules.template.ModuleHull;
import vswe.stevescarts.entities.ModularMinecart;
import vswe.stevescarts.init.ModItemData;
import vswe.stevescarts.init.ModItems;
import vswe.stevescarts.init.StevesCartsModules;
import vswe.stevescarts.modules.hull.ModuleReinforced;

import java.util.function.Consumer;

public class ItemStackRenderer implements SpecialModelRenderer<ItemStackRenderer.Data> {

    @Override
    public void submit(ItemStackRenderer.@Nullable Data data, @NonNull PoseStack matrixStack, @NonNull SubmitNodeCollector nodeCollector, int light, int overlay, boolean b, int outlineColor) {
        ItemStack stack = data.stack;
        if (stack.getItem() != ModItems.CARTS.get()) {
            return;
        }
        matrixStack.pushPose();
        matrixStack.scale(-1.0f, -1.0f, 1.0f);

        /*if (transformType == ItemDisplayContext.GUI) {
            matrixStack.translate(-1, 0, 0);
//                matrixStack.scale(lowestMult, lowestMult, lowestMult);
        } else {
            matrixStack.translate(-0.5, -0.5, 0.5);
        }*/
        matrixStack.mulPose(Axis.ZP.rotationDegrees(180));
        matrixStack.mulPose(Axis.XP.rotationDegrees(180));

        CompoundTag info = ModItemData.getTagCopy(stack);
        if (info.contains("modules")) {
            float lowestMult = 1.0f;
            ModularMinecart cart = new ModularMinecart(Minecraft.getInstance().level, 0, 0, 0, info);

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
        } else {
            //Fallback Render
            ModuleHull hull = new ModuleReinforced(null);
            for (ModelCartbase model : StevesCartsModules.REINFORCED_HULL.getModels(true).values()) {
                model.applyEffects(hull, matrixStack, 0, 0, 0);
                nodeCollector.submitModel(model, null, matrixStack, model.getRenderType(hull), 240, OverlayTexture.NO_OVERLAY, 0, null);
            }
        }
        matrixStack.popPose();
    }

    @Override
    public void getExtents(Consumer<Vector3fc> consumer) {
        consumer.accept(new Vector3f());
    }


    @Nullable
    @Override
    public Data extractArgument(@NonNull ItemStack itemStack) {
        return new Data(itemStack);
    }

    public static class Data {
        public ItemStack stack;

        public Data(ItemStack stack) {
            this.stack = stack;
        }
    }

    public record Unbaked() implements SpecialModelRenderer.Unbaked<Data> {
        public static final MapCodec<ItemStackRenderer.Unbaked> MAP_CODEC = RecordCodecBuilder.mapCodec((builder) -> {
            return builder.stable(new ItemStackRenderer.Unbaked());
        });

        @Override
        public SpecialModelRenderer<Data> bake(@NonNull BakingContext bakingContext) {
            return new ItemStackRenderer();
        }

        public @NonNull MapCodec<ItemStackRenderer.Unbaked> type() {
            return MAP_CODEC;
        }
    }
}
