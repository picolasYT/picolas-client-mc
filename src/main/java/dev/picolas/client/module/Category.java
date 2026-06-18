package dev.picolas.client.module;

/** Module categories shown in the ClickGUI sidebar. */
public enum Category {
    RENDER   ("Render",   "render_icon"),
    UTILITY  ("Utility",  "utility_icon"),
    HUD      ("HUD",      "hud_icon"),
    PVP      ("PvP",      "pvp_icon"),
    MOVEMENT ("Movement", "move_icon"),
    SYSTEM   ("System",   "system_icon");

    private final String displayName;
    private final String iconId;

    Category(String displayName, String iconId) {
        this.displayName = displayName;
        this.iconId      = iconId;
    }

    public String getDisplayName() { return displayName; }
    public String getIconId()      { return iconId;      }
}
