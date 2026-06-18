package dev.picolas.client.event.events;

import net.minecraft.client.util.math.MatrixStack;

/** Fired once per client tick (end of tick). */
public class TickEvent {
    private static final TickEvent INSTANCE = new TickEvent();
    public static TickEvent get() { return INSTANCE; }
}
