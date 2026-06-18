package dev.picolas.client.modules.hud;

import dev.picolas.client.config.Setting;
import dev.picolas.client.event.bus.EventHandler;
import dev.picolas.client.event.events.RenderHudEvent;
import dev.picolas.client.module.Category;
import dev.picolas.client.module.Module;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;

// ─────────────────────────────────────────────────────────────────────────────
// FPS Counter
// ─────────────────────────────────────────────────────────────────────────────
public class FPSCounterModule extends Module {
    public final Setting<Integer> x = addSetting(new Setting<>("X", "X position", 4, 0, 1920));
    public final Setting<Integer> y = addSetting(new Setting<>("Y", "Y position", 4, 0, 1080));

    public FPSCounterModule() { super("FPS Counter", "Shows current FPS.", Category.HUD); }
}
