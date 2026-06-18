package dev.picolas.client.event.events;

import net.minecraft.client.gui.DrawContext;

/** Fired during the 2-D HUD render pass (after vanilla HUD). */
public class RenderHudEvent {
    private final DrawContext context;
    private final float tickDelta;

    public RenderHudEvent(DrawContext context, float tickDelta) {
        this.context   = context;
        this.tickDelta = tickDelta;
    }

    public DrawContext getContext()   { return context;   }
    public float        getTickDelta() { return tickDelta; }
}
