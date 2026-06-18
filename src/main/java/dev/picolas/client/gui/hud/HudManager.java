package dev.picolas.client.gui.hud;

import dev.picolas.client.PicolasClient;
import dev.picolas.client.event.bus.EventHandler;
import dev.picolas.client.event.events.RenderHudEvent;
import dev.picolas.client.module.Module;
import dev.picolas.client.modules.hud.*;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/**
 * Renders all active HUD elements during the 2-D overlay pass.
 * Registered as a global event listener (always active, not toggled per module).
 */
public class HudManager {

    private static final int WHITE     = 0xFFFFFFFF;
    private static final int GRAY      = 0xFF888899;
    private static final int ACCENT    = 0xFF8A2BE2;
    private static final int ACCENT_BL = 0xFF00BFFF;
    private static final int GREEN     = 0xFF55FF55;
    private static final int RED       = 0xFFFF5555;

    public HudManager() {
        PicolasClient.getInstance().getEventBus().subscribe(this);
    }

    @EventHandler
    public void onRenderHud(RenderHudEvent event) {
        MinecraftClient mc = MinecraftClient.getInstance();
        if (mc.player == null || mc.world == null) return;

        renderHud(event.getContext(), mc);
    }

    private void renderHud(DrawContext ctx, MinecraftClient mc) {
        var mgr = PicolasClient.getInstance().getModuleManager();

        // FPS Counter
        if (isEnabled("FPS Counter")) {
            int fps = mc.getCurrentFps();
            int fpsColor = fps >= 60 ? GREEN : (fps >= 30 ? 0xFFFFFF55 : RED);
            ctx.drawTextWithShadow(mc.textRenderer, "FPS: ", 4, 4, WHITE);
            ctx.drawTextWithShadow(mc.textRenderer, String.valueOf(fps), 4 + mc.textRenderer.getWidth("FPS: "), 4, fpsColor);
        }

        // Ping
        if (isEnabled("Ping Display")) {
            var entry = mc.player.networkHandler != null
                    ? mc.player.networkHandler.getPlayerListEntry(mc.player.getUuid()) : null;
            int ping = entry != null ? entry.getLatency() : 0;
            int pingColor = ping < 50 ? GREEN : (ping < 150 ? 0xFFFFFF55 : RED);
            ctx.drawTextWithShadow(mc.textRenderer, "Ping: ", 4, 16, WHITE);
            ctx.drawTextWithShadow(mc.textRenderer, ping + "ms", 4 + mc.textRenderer.getWidth("Ping: "), 16, pingColor);
        }

        // Coordinates
        if (isEnabled("Coordinates")) {
            int px = (int) mc.player.getX();
            int py = (int) mc.player.getY();
            int pz = (int) mc.player.getZ();
            ctx.drawTextWithShadow(mc.textRenderer,
                    String.format("XYZ: %d, %d, %d", px, py, pz), 4, 28, WHITE);
        }

        // Clock
        if (isEnabled("Clock")) {
            String time = LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"));
            ctx.drawTextWithShadow(mc.textRenderer, time, 4, 40, ACCENT_BL);
        }

        // ArrayList (top-right)
        if (isEnabled("ArrayList")) {
            var active = mgr.getActiveModules();
            int ay = 4;
            for (Module mod : active) {
                if (mod.getName().equals("ArrayList")) continue;
                String name = mod.getName();
                int nameW   = mc.textRenderer.getWidth(name);
                int ax      = ctx.getScaledWindowWidth() - nameW - 6;
                ctx.fill(ax - 2, ay - 1, ax + nameW + 2, ay + 9, 0x55000000);
                ctx.drawTextWithShadow(mc.textRenderer, name, ax, ay, ACCENT);
                ay += 11;
            }
        }
    }

    private boolean isEnabled(String moduleName) {
        return PicolasClient.getInstance().getModuleManager()
                .getModuleByName(moduleName)
                .map(Module::isEnabled)
                .orElse(false);
    }
}
