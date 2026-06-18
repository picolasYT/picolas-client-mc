package dev.picolas.client.modules.utility;

import dev.picolas.client.event.bus.EventHandler;
import dev.picolas.client.event.events.TickEvent;
import dev.picolas.client.module.Category;
import dev.picolas.client.module.Module;

/**
 * Toggle Sprint — holds the sprint key while the player is moving forward.
 */
public class ToggleSprintModule extends Module {

    public ToggleSprintModule() {
        super("Toggle Sprint", "Automatically sprints when moving.", Category.UTILITY);
    }

    @EventHandler
    public void onTick(TickEvent event) {
        if (mc().player == null) return;
        if (mc().player.input.movementForward > 0f) {
            mc().player.setSprinting(true);
        }
    }
}
