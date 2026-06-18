package dev.picolas.client.modules.render;

import dev.picolas.client.module.Category;
import dev.picolas.client.module.Module;

/**
 * NoFog — disables all fog rendering.
 * The actual fog removal is handled by {@link dev.picolas.client.mixin.GameRendererMixin}.
 */
public class NoFogModule extends Module {

    public NoFogModule() {
        super("No Fog", "Removes fog from the world.", Category.RENDER);
    }
}
