package vswe.stevescarts.client.guis;

import net.minecraft.client.Minecraft;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.opengl.GL11;

import java.util.LinkedList;

/**
 * Created by brandon3055 on 02/03/2023
 * <p>
 * I guess this can probably be moved to PolyLib. I would do it if polylib didnt break in WorkspaceTool Environment.
 */
public class GuiHelper {
    private static final Logger LOGGER = LogManager.getLogger();
    private static LinkedList<ScissorState> prevStates = new LinkedList<>();
    private static ScissorState currentState = null;

    public static void pushGuiScissor(Minecraft mc, double x, double y, double width, double height, int guiWidth, int guiHeight) {
        double yResScale = (double) mc.getWindow().getScreenHeight() / guiHeight;
        double xResScale = (double) mc.getWindow().getScreenWidth() / guiWidth;
        double scaledWidth = width * xResScale;
        double scaledHeight = height * yResScale;
        int screenX = (int) (x * xResScale);
        int screenY = (int) (mc.getWindow().getScreenHeight() - (y * yResScale) - scaledHeight);
        GuiHelper.pushScissor(mc, screenX, screenY, (int) scaledWidth, (int) scaledHeight);
    }

    public static void pushScissor(int x, int y, int width, int height) {
        pushScissor(Minecraft.getInstance(), x, y, width, height);
    }

    public static void pushScissor(Minecraft mc, int x, int y, int width, int height) {
        int xMax = x + width;
        int yMax = y + height;
        if (currentState == null) {
            prevStates.add(currentState = new ScissorState(false, 0, 0, mc.getWindow().getScreenWidth(), mc.getWindow().getScreenHeight()));
        } else {
            prevStates.add(currentState);
        }
        currentState = new ScissorState(true, x, y, xMax, yMax, currentState).apply();
    }

    public static void popScissor() {
        ScissorState lastState = prevStates.size() > 0 ? prevStates.removeLast() : null;
        if (lastState != null) {
            currentState = lastState.apply();
            if (!currentState.enabled) {
                currentState = null;
            }
        } else {
            LOGGER.error("Attempt to popScissor but Scissor state has not been set");
        }
    }

    private static class ScissorState {
        private boolean enabled;
        private int x;
        private int y;
        private int xMax;
        private int yMax;

        private ScissorState(boolean enabled, int x, int y, int xMax, int yMax, ScissorState prevState) {
            this.enabled = enabled;
            this.x = Math.max(prevState.x, x);
            this.y = Math.max(prevState.y, y);
            this.xMax = Math.min(prevState.xMax, xMax);
            this.yMax = Math.min(prevState.yMax, yMax);
            Minecraft mc = Minecraft.getInstance();
            if (this.x < 0) this.x = 0;
            if (this.y < 0) this.y = 0;
            if (this.xMax > mc.getWindow().getScreenWidth()) this.xMax = mc.getWindow().getScreenWidth();
            if (this.yMax > mc.getWindow().getScreenHeight()) this.yMax = mc.getWindow().getScreenHeight();
            if (this.xMax < this.x) this.xMax = this.x;
            if (this.yMax < this.y) this.yMax = this.y;
        }

        private ScissorState(boolean enabled, int x, int y, int xMax, int yMax) {
            this.enabled = enabled;
            this.x = x;
            this.y = y;
            this.xMax = xMax;
            this.yMax = yMax;
        }

        private ScissorState apply() {
            if (enabled) { GL11.glEnable(GL11.GL_SCISSOR_TEST); } else { GL11.glDisable(GL11.GL_SCISSOR_TEST); }
            GL11.glScissor(x, y, xMax - x, yMax - y);
            return this;
        }
    }
}
