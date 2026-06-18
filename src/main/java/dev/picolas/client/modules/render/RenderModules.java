package dev.picolas.client.modules.render;
import dev.picolas.client.module.*;
import dev.picolas.client.config.Setting;

// ── EntityHighlight ──────────────────────────────────────────────────────────
class EntityHighlightModule extends Module {
    public EntityHighlightModule() { super("Entity Highlight", "Highlights entities.", Category.RENDER); }
}

// ── Nametags ─────────────────────────────────────────────────────────────────
class NametagsModule extends Module {
    public final Setting<Double> scale = addSetting(new Setting<>("Scale", "Tag size", 1.0, 0.5, 3.0));
    public NametagsModule() { super("Nametags", "Shows player nametags.", Category.RENDER); }
}

// ── Block ESP ────────────────────────────────────────────────────────────────
class BlockESPModule extends Module {
    public BlockESPModule() { super("Block ESP", "Highlights target blocks.", Category.RENDER); }
}

// ── Distance ESP ─────────────────────────────────────────────────────────────
class DistanceESPModule extends Module {
    public DistanceESPModule() { super("Distance ESP", "Shows distance to entities.", Category.RENDER); }
}

// ── Chams ────────────────────────────────────────────────────────────────────
class ChamsModule extends Module {
    public ChamsModule() { super("Chams", "See players through walls with color.", Category.RENDER); }
}

// ── Better Visuals ───────────────────────────────────────────────────────────
class BetterVisualsModule extends Module {
    public BetterVisualsModule() { super("Better Visuals", "Improves visual effects.", Category.RENDER); }
}
