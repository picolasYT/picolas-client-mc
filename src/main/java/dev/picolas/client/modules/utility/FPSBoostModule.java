package dev.picolas.client.modules.utility;

import dev.picolas.client.config.Setting;
import dev.picolas.client.event.bus.EventHandler;
import dev.picolas.client.event.events.TickEvent;
import dev.picolas.client.module.Category;
import dev.picolas.client.module.Module;
import net.minecraft.client.MinecraftClient;

// ── FPS Boost ────────────────────────────────────────────────────────────────
public class FPSBoostModule extends Module {
    public final Setting<Boolean> reduceParticles = addSetting(new Setting<>("Reduce Particles", "Lower particle count", true));
    public final Setting<Boolean> optimizeRender  = addSetting(new Setting<>("Optimize Render",  "Skip off-screen renders", true));
    public FPSBoostModule() { super("FPS Boost", "Optimizes the game for better FPS.", Category.UTILITY); }
}
