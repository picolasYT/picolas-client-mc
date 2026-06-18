package dev.picolas.client.util;

/**
 * Color utilities for the Picolas Client palette.
 */
public final class ColorUtil {

    // Core palette
    public static final int DARK_BG     = 0xFF080810;
    public static final int NEON_PURPLE = 0xFF8A2BE2;
    public static final int ELEC_BLUE   = 0xFF00BFFF;
    public static final int WHITE       = 0xFFFFFFFF;
    public static final int GRAY        = 0xFF888899;

    private ColorUtil() {}

    /** Blend two ARGB colors at ratio {@code t} (0 = a, 1 = b). */
    public static int blend(int a, int b, float t) {
        int ar = (a >> 16) & 0xFF, ag = (a >> 8) & 0xFF, ab = a & 0xFF, aa = (a >> 24) & 0xFF;
        int br = (b >> 16) & 0xFF, bg = (b >> 8) & 0xFF, bb = b & 0xFF, ba = (b >> 24) & 0xFF;
        int r = (int)(ar + (br - ar) * t);
        int g = (int)(ag + (bg - ag) * t);
        int bl2 = (int)(ab + (bb - ab) * t);
        int alp = (int)(aa + (ba - aa) * t);
        return (alp << 24) | (r << 16) | (g << 8) | bl2;
    }

    /** ARGB from individual components (0-255 each). */
    public static int argb(int a, int r, int g, int b) {
        return (a << 24) | (r << 16) | (g << 8) | b;
    }

    /** Pulse a color's alpha over time for a glowing effect. */
    public static int pulse(int color, long time, float speed) {
        float t   = (float)(Math.sin(time * speed * 0.001) * 0.5 + 0.5);
        int alpha = (int)(((color >> 24) & 0xFF) * t);
        return (color & 0x00FFFFFF) | (alpha << 24);
    }
}
