package vswe.stevescarts.client.renders;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.Identifier;
import net.minecraft.world.phys.Vec3;

public final class CartHitchRenderer {
    private static final Identifier CHAIN_TEXTURE = Identifier.withDefaultNamespace("textures/block/iron_chain.png");
    private static final double CART_END_OFFSET = 0.58;
    private static final double CHAIN_HEIGHT = 0.15;
    private static final double CHAIN_HALF_WIDTH = 0.075;
    private static final double CHAIN_SAG = 0.06;
    private static final float FIRST_STRIP_MIN_U = 0.0F;
    private static final float FIRST_STRIP_MAX_U = 3.0F / 16.0F;
    private static final float SECOND_STRIP_MIN_U = 3.0F / 16.0F;
    private static final float SECOND_STRIP_MAX_U = 6.0F / 16.0F;

    private CartHitchRenderer() {
    }

    public static void submit(HitchConnection connection, PoseStack poseStack, SubmitNodeCollector nodeCollector, int light) {
        if (connection.linkedCartOffset().lengthSqr() < 1.0E-6) {
            return;
        }

        nodeCollector.submitCustomGeometry(
                poseStack,
                RenderTypes.entityCutout(CHAIN_TEXTURE),
                (pose, buffer) -> renderChainConnection(pose, buffer, connection, light)
        );
    }

    private static void renderChainConnection(PoseStack.Pose pose, VertexConsumer buffer, HitchConnection connection, int light) {
        Vec3 linkedCartOffset = connection.linkedCartOffset();
        Vec3 towardLinkedCart = linkedCartOffset.normalize();
        Vec3 towardCurrentCart = towardLinkedCart.scale(-1.0);

        Vec3 currentAxis = axisFromYaw(connection.cartYaw());
        Vec3 linkedAxis = axisFromYaw(connection.linkedCartYaw());
        Vec3 currentEndDirection = orientToward(currentAxis, towardLinkedCart);
        Vec3 linkedEndDirection = orientToward(linkedAxis, towardCurrentCart);

        Vec3 currentEnd = currentEndDirection.scale(CART_END_OFFSET).add(0.0, CHAIN_HEIGHT, 0.0);
        Vec3 linkedEnd = linkedCartOffset.add(linkedEndDirection.scale(CART_END_OFFSET)).add(0.0, CHAIN_HEIGHT, 0.0);

        renderChain(pose, buffer, light, currentEnd, linkedEnd);
    }

    private static Vec3 axisFromYaw(float yaw) {
        double radians = Math.toRadians(yaw);
        return new Vec3(Math.cos(radians), 0.0, -Math.sin(radians));
    }

    private static Vec3 orientToward(Vec3 axis, Vec3 targetDirection) {
        return axis.dot(targetDirection) < 0.0 ? axis.scale(-1.0) : axis;
    }

    private static void renderChain(PoseStack.Pose pose, VertexConsumer buffer, int light, Vec3 start, Vec3 end) {
        Vec3 midpoint = start.add(end).scale(0.5).subtract(0.0, CHAIN_SAG, 0.0);
        float midpointV = (float) start.distanceTo(midpoint);
        float endV = midpointV + (float) midpoint.distanceTo(end);
        renderChainSegment(pose, buffer, light, start, midpoint, 0.0F, midpointV);
        renderChainSegment(pose, buffer, light, midpoint, end, midpointV, endV);
    }

    private static void renderChainSegment(PoseStack.Pose pose, VertexConsumer buffer, int light,
                                           Vec3 start, Vec3 end, float startV, float endV) {
        Vec3 difference = end.subtract(start);
        if (difference.lengthSqr() < 1.0E-6) {
            return;
        }

        Vec3 direction = difference.normalize();
        Vec3 across = direction.cross(new Vec3(0.0, 1.0, 0.0));
        if (across.lengthSqr() < 1.0E-6) {
            across = new Vec3(1.0, 0.0, 0.0);
        } else {
            across = across.normalize();
        }
        Vec3 diagonal = across.cross(direction).normalize();
        Vec3 acrossWidth = across.scale(CHAIN_HALF_WIDTH);
        Vec3 diagonalWidth = diagonal.scale(CHAIN_HALF_WIDTH);

        quad(pose, buffer, light,
                start.subtract(acrossWidth), start.add(acrossWidth),
                end.add(acrossWidth), end.subtract(acrossWidth), diagonal,
                FIRST_STRIP_MAX_U, FIRST_STRIP_MIN_U, startV, endV);
        quad(pose, buffer, light,
                start.subtract(diagonalWidth), start.add(diagonalWidth),
                end.add(diagonalWidth), end.subtract(diagonalWidth), across,
                SECOND_STRIP_MAX_U, SECOND_STRIP_MIN_U, startV, endV);
    }

    private static void quad(PoseStack.Pose pose, VertexConsumer buffer, int light,
                             Vec3 a, Vec3 b, Vec3 c, Vec3 d, Vec3 normal,
                             float leftU, float rightU, float startV, float endV) {
        vertex(pose, buffer, light, a, leftU, startV, normal);
        vertex(pose, buffer, light, b, rightU, startV, normal);
        vertex(pose, buffer, light, c, rightU, endV, normal);
        vertex(pose, buffer, light, d, leftU, endV, normal);
    }

    private static void vertex(PoseStack.Pose pose, VertexConsumer buffer, int light, Vec3 position,
                               float u, float v, Vec3 normal) {
        buffer.addVertex(pose, (float) position.x, (float) position.y, (float) position.z)
                .setColor(0xFFFFFFFF)
                .setUv(u, v)
                .setOverlay(OverlayTexture.NO_OVERLAY)
                .setLight(light)
                .setNormal(pose, (float) normal.x, (float) normal.y, (float) normal.z);
    }

    public record HitchConnection(Vec3 linkedCartOffset, float cartYaw, float linkedCartYaw) {
    }
}
