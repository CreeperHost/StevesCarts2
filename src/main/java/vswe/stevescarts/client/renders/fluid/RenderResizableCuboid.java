package vswe.stevescarts.client.renders.fluid;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.Direction;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.math.vector.Vector3f;

import java.util.Arrays;

public class RenderResizableCuboid
{
    public static final RenderResizableCuboid INSTANCE = new RenderResizableCuboid();
    private static final Vector3f VEC_ZERO = new Vector3f(0, 0, 0);
    private static final int U_MIN = 0;
    private static final int U_MAX = 1;
    private static final int V_MIN = 2;
    private static final int V_MAX = 3;
    protected EntityRendererManager manager = Minecraft.getInstance().getEntityRenderDispatcher();

    private static Vector3f withValue(Vector3f vector, Direction.Axis axis, float value) {
        if (axis == Direction.Axis.X) {
            return new Vector3f(value, vector.y(), vector.z());
        }
        else if (axis == Direction.Axis.Y) {
            return new Vector3f(vector.x(), value, vector.z());
        }
        else if (axis == Direction.Axis.Z) {
            return new Vector3f(vector.x(), vector.y(), value);
        }
        throw new RuntimeException("Was given a null axis! That was probably not intentional, consider this a bug! (Vector = " + vector + ")");
    }

    public static double getValue(Vector3d vector, Direction.Axis axis) {
        if (axis == Direction.Axis.X) {
            return vector.x;
        }
        else if (axis == Direction.Axis.Y) {
            return vector.y;
        }
        else if (axis == Direction.Axis.Z) {
            return vector.z;
        }
        throw new RuntimeException("Was given a null axis! That was probably not intentional, consider this a bug! (Vector = " + vector + ")");
    }

    /**
     * model 3d cube is the fluid
     */
    public void renderCube(Model3D cube, MatrixStack matrix, IVertexBuilder buffer, int argb, int light) {
        float red = RenderUtils.getRed(argb);
        float green = RenderUtils.getGreen(argb);
        float blue = RenderUtils.getBlue(argb);
        float alpha = RenderUtils.getAlpha(argb);
        Vector3d size = new Vector3d(cube.sizeX(), cube.sizeY(), cube.sizeZ());
        matrix.pushPose();
        matrix.translate(cube.minX, cube.minY, cube.minZ);
        net.minecraft.util.math.vector.Matrix4f matrix4f = matrix.last().pose();
        for (Direction face : Direction.values()) {
            if (cube.shouldSideRender(face)) {
                int ordinal = face.ordinal();
                TextureAtlasSprite sprite = cube.textures[ordinal];
                if (sprite != null) {
                    Direction.Axis u = face.getAxis() == Direction.Axis.X ? Direction.Axis.Z : Direction.Axis.X;
                    Direction.Axis v = face.getAxis() == Direction.Axis.Y ? Direction.Axis.Z : Direction.Axis.Y;
                    float other = face.getAxisDirection() == Direction.AxisDirection.POSITIVE ? (float) getValue(size, face.getAxis()) : 0;
                    //Swap the face if this is positive: the renderer returns indexes that ALWAYS are for the negative face, so light it properly this way
                    face = face.getAxisDirection() == Direction.AxisDirection.NEGATIVE ? face : face.getOpposite();
                    Direction opposite = face.getOpposite();
                    float minU = sprite.getU0();
                    float maxU = sprite.getU1();
                    //Flip the v
                    float minV = sprite.getV1();
                    float maxV = sprite.getV0();
                    double sizeU = getValue(size, u);
                    double sizeV = getValue(size, v);
                    // Look into this more, as it makes tiling of multiple objects not render properly if they don't fit the full texture.
                    // Example: Mechanical pipes rendering water or lava, makes it relatively easy to see the texture artifacts
                    for (int uIndex = 0; uIndex < sizeU; uIndex++) {
                        float[] baseUV = new float[] { minU, maxU, minV, maxV };
                        double addU = 1;
                        // If the size of the texture is greater than the cuboid goes on for then make sure the texture positions are lowered
                        if (uIndex + addU > sizeU) {
                            addU = sizeU - uIndex;
                            baseUV[U_MAX] = baseUV[U_MIN] + (baseUV[U_MAX] - baseUV[U_MIN]) * (float) addU;
                        }
                        for (int vIndex = 0; vIndex < sizeV; vIndex++) {
                            float[] uv = Arrays.copyOf(baseUV, 4);
                            double addV = 1;
                            if (vIndex + addV > sizeV) {
                                addV = sizeV - vIndex;
                                uv[V_MAX] = uv[V_MIN] + (uv[V_MAX] - uv[V_MIN]) * (float) addV;
                            }
                            float[] xyz = new float[] { uIndex, (float) (uIndex + addU), vIndex, (float) (vIndex + addV) };
                            renderPoint(matrix4f, buffer, face, u, v, other, uv, xyz, true, false, red, green, blue, alpha, light);
                            renderPoint(matrix4f, buffer, face, u, v, other, uv, xyz, true, true, red, green, blue, alpha, light);
                            renderPoint(matrix4f, buffer, face, u, v, other, uv, xyz, false, true, red, green, blue, alpha, light);
                            renderPoint(matrix4f, buffer, face, u, v, other, uv, xyz, false, false, red, green, blue, alpha, light);
                            renderPoint(matrix4f, buffer, opposite, u, v, other, uv, xyz, false, false, red, green, blue, alpha, light);
                            renderPoint(matrix4f, buffer, opposite, u, v, other, uv, xyz, false, true, red, green, blue, alpha, light);
                            renderPoint(matrix4f, buffer, opposite, u, v, other, uv, xyz, true, true, red, green, blue, alpha, light);
                            renderPoint(matrix4f, buffer, opposite, u, v, other, uv, xyz, true, false, red, green, blue, alpha, light);
                        }
                    }
                }
            }
        }
        matrix.popPose();
    }

    private void renderPoint(Matrix4f matrix4f, IVertexBuilder buffer, Direction face, Direction.Axis u, Direction.Axis v, float other, float[] uv, float[] xyz, boolean minU, boolean minV,
                             float red, float green, float blue, float alpha, int light) {
        int uFinal = minU ? U_MIN : U_MAX;
        int vFinal = minV ? V_MIN : V_MAX;
        Vector3f vertex = withValue(VEC_ZERO, u, xyz[uFinal]);
        vertex = withValue(vertex, v, xyz[vFinal]);
        vertex = withValue(vertex, face.getAxis(), other);
        buffer.vertex(matrix4f, vertex.x(), vertex.y(), vertex.z()).color(red, green, blue, alpha).uv(uv[uFinal], uv[vFinal]).uv2(light).endVertex();
    }
}
