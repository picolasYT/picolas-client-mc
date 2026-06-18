package dev.picolas.client.module;

import dev.picolas.client.modules.render.*;
import dev.picolas.client.modules.utility.*;
import dev.picolas.client.modules.hud.*;
import dev.picolas.client.modules.pvp.*;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Holds and manages every registered module.
 */
public class ModuleManager {

    private final List<Module> modules = new ArrayList<>();

    /** Register all built-in modules. */
    public void registerAll() {

        // ── Render ──────────────────────────────────────────────────────────
        register(new FullbrightModule());
        register(new NoFogModule());
        register(new CustomCrosshairModule());
        register(new EntityHighlightModule());
        register(new NametagsModule());
        register(new PlayerESPModule());
        register(new BlockESPModule());
        register(new DistanceESPModule());
        register(new ChamsModule());
        register(new BetterVisualsModule());

        // ── Utility ─────────────────────────────────────────────────────────
        register(new FPSBoostModule());
        register(new MemoryOptimizerModule());
        register(new ChunkOptimizerModule());
        register(new EntityCullingModule());
        register(new ToggleSprintModule());
        register(new ToggleSneakModule());
        register(new AutoSprintModule());
        register(new CoordinatesModule());
        register(new WaypointsModule());

        // ── HUD ─────────────────────────────────────────────────────────────
        register(new FPSCounterModule());
        register(new CPSCounterModule());
        register(new KeystrokesModule());
        register(new ArmorHudModule());
        register(new PotionHudModule());
        register(new PingDisplayModule());
        register(new ClockModule());
        register(new ArrayListModule());

        // ── PvP ─────────────────────────────────────────────────────────────
        register(new AimAssistModule());
        register(new HitIndicatorModule());
        register(new ComboCounterModule());
        register(new AttackCooldownModule());
        register(new PvpStatsModule());
    }

    private void register(Module module) {
        module.init();
        modules.add(module);
    }

    // ── Queries ──────────────────────────────────────────────────────────────

    public List<Module> getModules() {
        return Collections.unmodifiableList(modules);
    }

    public List<Module> getModulesByCategory(Category category) {
        return modules.stream()
                      .filter(m -> m.getCategory() == category)
                      .collect(Collectors.toList());
    }

    public Optional<Module> getModuleByName(String name) {
        return modules.stream()
                      .filter(m -> m.getName().equalsIgnoreCase(name))
                      .findFirst();
    }

    public List<Module> getActiveModules() {
        return modules.stream()
                      .filter(Module::isEnabled)
                      .collect(Collectors.toList());
    }
}
