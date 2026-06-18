package dev.picolas.client.modules.render;

import dev.picolas.client.module.Category;
import dev.picolas.client.module.Module;

/**
 * Fullbright — sets gamma to maximum so the world is always fully lit.
 * On disable, gamma is restored to its previous value.
 */
public class FullbrightModule extends Module {

    private double savedGamma = 1.0;

    public FullbrightModule() {
        super("Fullbright", "Makes the world fully bright.", Category.RENDER);
    }

    @Override
    protected void onEnable() {
        savedGamma = mc().options.getGamma().getValue();
        mc().options.getGamma().setValue(16.0); // vanilla max is 1.0; >1 = fullbright
    }

    @Override
    protected void onDisable() {
        mc().options.getGamma().setValue(savedGamma);
    }
}
