package dev.picolas.client.module;

import dev.picolas.client.PicolasClient;
import dev.picolas.client.config.Setting;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.List;

/**
 * Base class for every Picolas Client module.
 *
 * <p>Subclasses override {@link #onEnable()}, {@link #onDisable()}, and any
 * event-handler methods annotated with {@code @EventHandler}.
 */
public abstract class Module {

    // ── Metadata ────────────────────────────────────────────────────────────

    private final String   name;
    private final String   description;
    private final Category category;

    // ── State ───────────────────────────────────────────────────────────────

    private boolean enabled    = false;
    private boolean registered = false; // subscribed to event bus?
    private int     keybind    = GLFW.GLFW_KEY_UNKNOWN;

    // ── Settings ────────────────────────────────────────────────────────────

    protected final List<Setting<?>> settings = new ArrayList<>();

    // ── Constructor ─────────────────────────────────────────────────────────

    protected Module(String name, String description, Category category) {
        this.name        = name;
        this.description = description;
        this.category    = category;
    }

    // ── Lifecycle ────────────────────────────────────────────────────────────

    /** Called once when the module is first registered by the manager. */
    public void init() {}

    /** Called when toggled ON. Override to set up state. */
    protected void onEnable()  {}

    /** Called when toggled OFF. Override to tear down state. */
    protected void onDisable() {}

    // ── Toggle ───────────────────────────────────────────────────────────────

    public void setEnabled(boolean enabled) {
        if (this.enabled == enabled) return;
        this.enabled = enabled;

        if (enabled) {
            if (!registered) {
                PicolasClient.getInstance().getEventBus().subscribe(this);
                registered = true;
            }
            onEnable();
        } else {
            PicolasClient.getInstance().getEventBus().unsubscribe(this);
            registered = false;
            onDisable();
        }
    }

    public void toggle() {
        setEnabled(!enabled);
    }

    // ── Settings helpers ─────────────────────────────────────────────────────

    protected <T> Setting<T> addSetting(Setting<T> setting) {
        settings.add(setting);
        return setting;
    }

    // ── Getters ──────────────────────────────────────────────────────────────

    public String   getName()        { return name;        }
    public String   getDescription() { return description; }
    public Category getCategory()    { return category;    }
    public boolean  isEnabled()      { return enabled;     }
    public int      getKeybind()     { return keybind;     }
    public List<Setting<?>> getSettings() { return settings; }

    public void setKeybind(int key) { this.keybind = key; }

    /** Convenience: the vanilla MinecraftClient singleton. */
    protected MinecraftClient mc() { return MinecraftClient.getInstance(); }
}
