# 🎮 Picolas Client

**v1.0.0-beta | Minecraft 1.21.1 | Fabric**

A premium Minecraft client mod built on Fabric with a glass-style UI, modular
architecture, and 30+ modules across Render, Utility, HUD, and PvP categories.

---

## 📸 UI Preview

- **Sidebar** — category navigation (Render, Utility, HUD, PvP, Movement, System)
- **Module grid** — 2-column card layout with toggles
- **Settings panel** — slides in on the right with sliders, dropdowns, and toggles
- **ArrayList HUD** — top-right active module list
- **HUD elements** — FPS, Ping, XYZ, Clock rendered in-game

---

## ⚡ Modules

### Render
| Module | Description |
|--------|-------------|
| Fullbright | Max gamma — always lit |
| No Fog | Removes all fog |
| Custom Crosshair | Size, gap, dot settings |
| Entity Highlight | Glowing entities |
| Nametags | Player name tags |
| Player ESP | Box / corner / outline ESP |
| Block ESP | Highlight target blocks |
| Distance ESP | Entity distance labels |
| Chams | See through walls |
| Better Visuals | Visual improvements |

### Utility
| Module | Description |
|--------|-------------|
| FPS Boost | Reduce particles, optimize render |
| Memory Optimizer | Periodic GC |
| Chunk Optimizer | Reduce chunk lag |
| Entity Culling | Skip hidden entities |
| Toggle Sprint | Auto-sprint on W |
| Toggle Sneak | Hold sneak |
| Auto Sprint | Sprint in all dirs |
| Coordinates | XYZ on-screen |
| Waypoints | Set / render waypoints |

### HUD
| Module | Description |
|--------|-------------|
| FPS Counter | Colour-coded FPS |
| CPS Counter | Clicks per second |
| Keystrokes | WASD visualizer |
| Armor HUD | Durability display |
| Potion HUD | Active effects |
| Ping Display | Server latency |
| Clock | Real-world time |
| ArrayList | Active module list |

### PvP
| Module | Description |
|--------|-------------|
| Aim Assist | FOV + strength settings |
| Hit Indicator | Flash on hit |
| Combo Counter | Kill combo tracking |
| Attack Cooldown | Visual arc |
| PvP Stats | Kills / damage |

---

## 🔧 Build

### Prerequisites
- **JDK 21** (`java -version` should show 21)
- **Git**

### Steps

```bash
git clone https://github.com/picolas/picolas-client
cd picolas-client

# Linux / macOS
./gradlew build

# Windows
gradlew.bat build
```

Output: `build/libs/picolas-client-1.0.0-beta.jar`

Copy the jar to your `.minecraft/mods/` folder alongside Fabric Loader and Fabric API.

---

## 🎮 Controls

| Key | Action |
|-----|--------|
| **Right Control** | Open ClickGUI |
| **Right Shift** | Toggle Sneak |
| **R** | HUD Editor (planned) |

---

## 🗂 Project Structure

```
src/main/java/dev/picolas/client/
├── PicolasClient.java          ← Entrypoint
├── core/                       ← (future: font renderer, shader manager)
├── event/
│   ├── bus/EventBus.java       ← Annotation-based event system
│   └── events/                 ← TickEvent, RenderHudEvent, RenderWorldEvent
├── module/
│   ├── Module.java             ← Base class
│   ├── Category.java           ← Enum: RENDER, UTILITY, HUD, PVP…
│   └── ModuleManager.java      ← Registration + queries
├── modules/
│   ├── render/                 ← Fullbright, ESP, NoFog…
│   ├── utility/                ← Sprint, FPSBoost…
│   ├── hud/                    ← FPS counter, ArrayList…
│   └── pvp/                    ← AimAssist, HitIndicator…
├── config/
│   ├── Setting.java            ← Typed setting (Bool/Int/Double/Enum)
│   └── ConfigManager.java      ← JSON persistence
├── gui/
│   ├── screens/ClickGuiScreen  ← Main glass UI
│   └── hud/HudManager.java     ← In-game HUD renderer
├── mixin/                      ← Fabric mixins
└── util/ColorUtil.java         ← Palette + blend helpers
```

---

## 🛠 Adding a New Module

1. Create a class extending `Module` in the appropriate package:

```java
public class MyModule extends Module {
    public final Setting<Boolean> mySetting =
        addSetting(new Setting<>("My Option", "Description", true));

    public MyModule() {
        super("My Module", "Does something cool.", Category.UTILITY);
    }

    @Override protected void onEnable()  { /* setup */ }
    @Override protected void onDisable() { /* teardown */ }

    @EventHandler
    public void onTick(TickEvent event) {
        // runs every tick while enabled
    }
}
```

2. Register it in `ModuleManager.registerAll()`:
```java
register(new MyModule());
```

That's it — it will appear in the ClickGUI automatically.

---

## 📄 License

MIT — open source, modify freely.
