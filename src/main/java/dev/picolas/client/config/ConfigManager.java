package dev.picolas.client.config;

import com.google.gson.*;
import dev.picolas.client.PicolasClient;
import dev.picolas.client.module.Module;
import net.fabricmc.loader.api.FabricLoader;

import java.io.*;
import java.lang.reflect.Type;
import java.nio.file.*;
import java.util.Map;

/**
 * Reads/writes module state and settings to
 * {@code .minecraft/config/picolas-client/modules.json}.
 */
public class ConfigManager {

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private final Path configDir;
    private final Path modulesFile;

    public ConfigManager() {
        configDir   = FabricLoader.getInstance().getConfigDir().resolve("picolas-client");
        modulesFile = configDir.resolve("modules.json");
        try { Files.createDirectories(configDir); } catch (IOException ignored) {}
    }

    // ── Save ─────────────────────────────────────────────────────────────────

    public void save() {
        JsonObject root = new JsonObject();

        for (Module module : PicolasClient.getInstance().getModuleManager().getModules()) {
            JsonObject moduleJson = new JsonObject();
            moduleJson.addProperty("enabled", module.isEnabled());
            moduleJson.addProperty("keybind", module.getKeybind());

            JsonObject settingsJson = new JsonObject();
            for (Setting<?> setting : module.getSettings()) {
                settingsJson.add(setting.getName(), GSON.toJsonTree(setting.getValue()));
            }
            moduleJson.add("settings", settingsJson);
            root.add(module.getName(), moduleJson);
        }

        try (Writer writer = Files.newBufferedWriter(modulesFile)) {
            GSON.toJson(root, writer);
        } catch (IOException e) {
            PicolasClient.LOGGER.error("Failed to save config", e);
        }
    }

    // ── Load ─────────────────────────────────────────────────────────────────

    public void load() {
        if (!Files.exists(modulesFile)) return;

        try (Reader reader = Files.newBufferedReader(modulesFile)) {
            JsonObject root = GSON.fromJson(reader, JsonObject.class);
            if (root == null) return;

            for (Module module : PicolasClient.getInstance().getModuleManager().getModules()) {
                JsonObject moduleJson = root.getAsJsonObject(module.getName());
                if (moduleJson == null) continue;

                if (moduleJson.has("enabled")) {
                    module.setEnabled(moduleJson.get("enabled").getAsBoolean());
                }
                if (moduleJson.has("keybind")) {
                    module.setKeybind(moduleJson.get("keybind").getAsInt());
                }
                if (moduleJson.has("settings")) {
                    JsonObject settingsJson = moduleJson.getAsJsonObject("settings");
                    applySettings(module, settingsJson);
                }
            }
        } catch (IOException e) {
            PicolasClient.LOGGER.error("Failed to load config", e);
        }
    }

    @SuppressWarnings("unchecked")
    private void applySettings(Module module, JsonObject settingsJson) {
        for (Setting<?> setting : module.getSettings()) {
            JsonElement element = settingsJson.get(setting.getName());
            if (element == null) continue;

            try {
                Object current = setting.getValue();
                if (current instanceof Boolean) {
                    ((Setting<Boolean>) setting).setValue(element.getAsBoolean());
                } else if (current instanceof Integer) {
                    ((Setting<Integer>) setting).setValue(element.getAsInt());
                } else if (current instanceof Double) {
                    ((Setting<Double>) setting).setValue(element.getAsDouble());
                } else if (current instanceof Float) {
                    ((Setting<Float>) setting).setValue(element.getAsFloat());
                } else if (current instanceof String) {
                    ((Setting<String>) setting).setValue(element.getAsString());
                } else if (current instanceof Enum<?>) {
                    Class<? extends Enum> enumClass = ((Enum<?>) current).getDeclaringClass();
                    ((Setting) setting).setValue(Enum.valueOf(enumClass, element.getAsString()));
                }
            } catch (Exception e) {
                PicolasClient.LOGGER.warn("Could not restore setting {}.{}: {}",
                        module.getName(), setting.getName(), e.getMessage());
            }
        }
    }
}
