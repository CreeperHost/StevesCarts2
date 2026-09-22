package vswe.stevescarts.client.renders;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.block.FluidModel;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.Mth;
import net.minecraft.world.level.material.FluidState;
import net.neoforged.neoforge.fluids.FluidStack;
import vswe.stevescarts.modules.storages.tanks.ModuleTank;

public final class FluidTankRenderer {
    private FluidTankRenderer() {
    }

    public static void submit(ModuleTank tank, PoseStack poseStack, SubmitNodeCollector nodeCollector, int light,
                              float minX, float topY, float minZ, float maxX, float bottomY, float maxZ) {
        FluidStack fluidStack = tank.getFluid();
        if (fluidStack.isEmpty()) {
            return;
        }

        float fill = Mth.clamp(tank.getFluidRenderHeight(), 0.0F, 1.0F);
        if (fill <= 0.0F) {
            return;
        }

        FluidState fluidState = fluidStack.getFluid().defaultFluidState();
        FluidModel fluidModel = Minecraft.getInstance().getModelManager().getFluidStateModelSet().get(fluidState);
        TextureAtlasSprite sprite = fluidModel.stillMaterial().sprite();
        if (sprite == null) {
            return;
        }

        var tintSource = fluidModel.fluidTintSource();
        int color = tintSource == null ? 0xFFFFFFFF : tintSource.color(fluidState);
        float fluidTop = Mth.lerp(fill, bottomY, topY);

        nodeCollector.submitCustomGeometry(
                poseStack,
                RenderTypes.entityTranslucent(sprite.atlasLocation()),
                (pose, buffer) -> renderCuboid(pose, buffer, sprite, color, light, minX, fluidTop, minZ, maxX, bottomY, maxZ)
        );
    }

    private static void renderCuboid(PoseStack.Pose pose, VertexConsumer buffer, TextureAtlasSprite sprite, int color, int light,
                                     float minX, float minY, float minZ, float maxX, float maxY, float maxZ) {
        float u0 = sprite.getU0();
        float u1 = sprite.getU1();
        float v0 = sprite.getV0();
        float v1 = sprite.getV1();

        quad(pose, buffer, color, light, minX, minY, minZ, u0, v0, minX, minY, maxZ, u0, v1, maxX, minY, maxZ, u1, v1, maxX, minY, minZ, u1, v0, 0, -1, 0);
        quad(pose, buffer, color, light, minX, maxY, maxZ, u0, v1, minX, maxY, minZ, u0, v0, maxX, maxY, minZ, u1, v0, maxX, maxY, maxZ, u1, v1, 0, 1, 0);
        quad(pose, buffer, color, light, maxX, minY, minZ, u0, v0, maxX, maxY, minZ, u0, v1, minX, maxY, minZ, u1, v1, minX, minY, minZ, u1, v0, 0, 0, -1);
        quad(pose, buffer, color, light, minX, minY, maxZ, u0, v0, minX, maxY, maxZ, u0, v1, maxX, maxY, maxZ, u1, v1, maxX, minY, maxZ, u1, v0, 0, 0, 1);
        quad(pose, buffer, color, light, minX, minY, minZ, u0, v0, minX, maxY, minZ, u0, v1, minX, maxY, maxZ, u1, v1, minX, minY, maxZ, u1, v0, -1, 0, 0);
        quad(pose, buffer, color, light, maxX, minY, maxZ, u0, v0, maxX, maxY, maxZ, u0, v1, maxX, maxY, minZ, u1, v1, maxX, minY, minZ, u1, v0, 1, 0, 0);
    }

    private static void quad(PoseStack.Pose pose, VertexConsumer buffer, int color, int light,
                             float x0, float y0, float z0, float u0, float v0,
                             float x1, float y1, float z1, float u1, float v1,
                             float x2, float y2, float z2, float u2, float v2,
                             float x3, float y3, float z3, float u3, float v3,
                             float normalX, float normalY, float normalZ) {
        vertex(pose, buffer, color, light, x0, y0, z0, u0, v0, normalX, normalY, normalZ);
        vertex(pose, buffer, color, light, x1, y1, z1, u1, v1, normalX, normalY, normalZ);
        vertex(pose, buffer, color, light, x2, y2, z2, u2, v2, normalX, normalY, normalZ);
        vertex(pose, buffer, color, light, x3, y3, z3, u3, v3, normalX, normalY, normalZ);
    }

    private static void vertex(PoseStack.Pose pose, VertexConsumer buffer, int color, int light,
                               float x, float y, float z, float u, float v,
                               float normalX, float normalY, float normalZ) {
        buffer.addVertex(pose, x, y, z)
                .setColor(color)
                .setUv(u, v)
                .setOverlay(OverlayTexture.NO_OVERLAY)
                .setLight(light)
                .setNormal(pose, normalX, normalY, normalZ);
    }
}
