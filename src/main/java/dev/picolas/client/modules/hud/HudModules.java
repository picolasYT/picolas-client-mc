package dev.picolas.client.modules.hud;
import dev.picolas.client.module.*;
import dev.picolas.client.config.Setting;

public class CPSCounterModule extends Module {
    public CPSCounterModule() { super("CPS Counter", "Shows clicks per second.", Category.HUD); }
}

public class KeystrokesModule extends Module {
    public KeystrokesModule() { super("Keystrokes", "Shows WASD key presses.", Category.HUD); }
}

public class ArmorHudModule extends Module {
    public ArmorHudModule() { super("Armor HUD", "Shows armor durability.", Category.HUD); }
}

public class PotionHudModule extends Module {
    public PotionHudModule() { super("Potion HUD", "Shows active potion effects.", Category.HUD); }
}

public class PingDisplayModule extends Module {
    public PingDisplayModule() { super("Ping Display", "Shows server ping.", Category.HUD); }
}

public class ClockModule extends Module {
    public ClockModule() { super("Clock", "Shows real-world time.", Category.HUD); }
}

public class ArrayListModule extends Module {
    public final Setting<Boolean> showCategory = addSetting(new Setting<>("Show Category", "Show category label", false));
    public ArrayListModule() { super("ArrayList", "Shows active modules.", Category.HUD); }
}
