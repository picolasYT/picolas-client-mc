package dev.picolas.client.event.events;

import net.minecraft.client.util.math.MatrixStack;

/** Fired during the 3-D world render pass. */
public class RenderWorldEvent {
    private final MatrixStack matrices;
    private final float tickDelta;

    public RenderWorldEvent(MatrixStack matrices, float tickDelta) {
        this.matrices  = matrices;
        this.tickDelta = tickDelta;
    }

    public MatrixStack getMatrices() { return matrices;  }
    public float       getTickDelta() { return tickDelta; }
}
