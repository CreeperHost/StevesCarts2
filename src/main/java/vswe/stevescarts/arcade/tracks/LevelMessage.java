package vswe.stevescarts.arcade.tracks;

import net.minecraft.network.chat.Component;

public class LevelMessage {
    private final int x;
    private final int y;
    private final int w;
    private final String message;
    private int isRunning;
    private int isStill;
    private int isDone;

    public LevelMessage(final int x, final int y, final int w, final String message) {
        this.x = x;
        this.y = y;
        this.w = w;
        this.message = message;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getW() {
        return w;
    }

    public String getMessage() {
        return Component.translatable(message).getString();
    }

    public LevelMessage setMustBeRunning() {
        isRunning = 1;
        return this;
    }

    public LevelMessage setMustNotBeRunning() {
        isRunning = -1;
        return this;
    }

    public LevelMessage setMustBeStill() {
        isStill = 1;
        return this;
    }

    public LevelMessage setMustNotBeStill() {
        isStill = -1;
        return this;
    }

    public LevelMessage setMustBeDone() {
        isDone = 1;
        return this;
    }

    public LevelMessage setMustNotBeDone() {
        isDone = -1;
        return this;
    }

    public boolean isVisible(final boolean isRunning, final boolean isStill, final boolean isDone) {
        return (this.isRunning == 0 || this.isRunning > 0 == isRunning) && (this.isStill == 0 || this.isStill > 0 == isStill) && (this.isDone == 0 || this.isDone > 0 == isDone);
    }
}
