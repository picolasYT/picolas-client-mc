package dev.picolas.client;

import dev.picolas.client.config.ConfigManager;
import dev.picolas.client.event.bus.EventBus;
import dev.picolas.client.module.ModuleManager;
import dev.picolas.client.gui.hud.HudManager;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Picolas Client — Main entrypoint.
 * Initializes all core systems in dependency order.
 */
public class PicolasClient implements ClientModInitializer {

    public static final String MOD_ID   = "picolas-client";
    public static final String MOD_NAME = "Picolas Client";
    public static final String VERSION  = "1.0.0-beta";
    public static final Logger LOGGER   = LoggerFactory.getLogger(MOD_NAME);

    // Singleton
    private static PicolasClient instance;

    // Core systems
    private EventBus      eventBus;
    private ModuleManager moduleManager;
    private ConfigManager configManager;
    private HudManager    hudManager;

    // ClickGUI keybind (Right Control by default)
    private KeyBinding guiKeyBinding;

    @Override
    public void onInitializeClient() {
        instance = this;
        LOGGER.info("Initializing {} v{}", MOD_NAME, VERSION);

        // 1. Event bus must be first
        eventBus = new EventBus();

        // 2. Config before modules so modules can restore saved state
        configManager = new ConfigManager();

        // 3. Modules
        moduleManager = new ModuleManager();
        moduleManager.registerAll();

        // 4. HUD
        hudManager = new HudManager();

        // 5. Load config (applies to modules)
        configManager.load();

        // 6. Register keybinds
        registerKeybinds();

        LOGGER.info("{} loaded — {} modules active", MOD_NAME, moduleManager.getModules().size());
    }

    // ── Keybinds ────────────────────────────────────────────────────────────

    private void registerKeybinds() {
        guiKeyBinding = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.picolas-client.open_gui",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_RIGHT_CONTROL,
                "category.picolas-client"
        ));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (guiKeyBinding.wasPressed() && client.currentScreen == null) {
                client.setScreen(new dev.picolas.client.gui.screens.ClickGuiScreen());
            }
        });
    }

    // ── Getters ─────────────────────────────────────────────────────────────

    public static PicolasClient getInstance()  { return instance;       }
    public EventBus      getEventBus()         { return eventBus;       }
    public ModuleManager getModuleManager()    { return moduleManager;  }
    public ConfigManager getConfigManager()    { return configManager;  }
    public HudManager    getHudManager()       { return hudManager;     }
}
