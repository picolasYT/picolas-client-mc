package dev.picolas.client.modules.render;

import dev.picolas.client.config.Setting;
import dev.picolas.client.module.Category;
import dev.picolas.client.module.Module;

public class CustomCrosshairModule extends Module {
    public final Setting<Integer> size  = addSetting(new Setting<>("Size", "Crosshair size", 5, 1, 20));
    public final Setting<Integer> gap   = addSetting(new Setting<>("Gap", "Center gap", 2, 0, 10));
    public final Setting<Boolean> dot   = addSetting(new Setting<>("Center Dot", "Show center dot", false));

    public CustomCrosshairModule() { super("Custom Crosshair", "Edit your crosshair.", Category.RENDER); }
}
