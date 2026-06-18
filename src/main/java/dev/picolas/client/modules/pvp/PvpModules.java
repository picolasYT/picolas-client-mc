package dev.picolas.client.modules.pvp;
import dev.picolas.client.module.*;
import dev.picolas.client.config.Setting;

public class AimAssistModule extends Module {
    public final Setting<Double>  fov      = addSetting(new Setting<>("FOV",      "Aim assist field of view", 90.0, 10.0, 180.0));
    public final Setting<Double>  strength = addSetting(new Setting<>("Strength", "How strong the assist is",  0.5,  0.0,   1.0));
    public final Setting<Boolean> players  = addSetting(new Setting<>("Players",  "Target players",            true));
    public AimAssistModule() { super("Aim Assist", "Gently assists aim toward targets.", Category.PVP); }
}

public class HitIndicatorModule extends Module {
    public HitIndicatorModule() { super("Hit Indicator", "Flash on hit registration.", Category.PVP); }
}

public class ComboCounterModule extends Module {
    public ComboCounterModule() { super("Combo Counter", "Tracks hit combos.", Category.PVP); }
}

public class AttackCooldownModule extends Module {
    public AttackCooldownModule() { super("Attack Cooldown", "Displays attack cooldown arc.", Category.PVP); }
}

public class PvpStatsModule extends Module {
    public PvpStatsModule() { super("PvP Stats", "Shows kills and damage dealt.", Category.PVP); }
}
