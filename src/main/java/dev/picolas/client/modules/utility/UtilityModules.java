package dev.picolas.client.modules.utility;
import dev.picolas.client.module.*;
import dev.picolas.client.config.Setting;

public class MemoryOptimizerModule extends Module {
    public MemoryOptimizerModule() { super("Memory Optimizer", "Runs GC periodically.", Category.UTILITY); }
}

public class ChunkOptimizerModule extends Module {
    public ChunkOptimizerModule() { super("Chunk Optimizer", "Reduces chunk update lag.", Category.UTILITY); }
}

public class EntityCullingModule extends Module {
    public EntityCullingModule() { super("Entity Culling", "Skips rendering hidden entities.", Category.UTILITY); }
}

public class ToggleSneakModule extends Module {
    public ToggleSneakModule() { super("Toggle Sneak", "Keeps sneak held.", Category.UTILITY); }
}

public class AutoSprintModule extends Module {
    public AutoSprintModule() { super("Auto Sprint", "Sprints in all directions.", Category.UTILITY); }
}

public class CoordinatesModule extends Module {
    public CoordinatesModule() { super("Coordinates", "Shows XYZ on screen.", Category.UTILITY); }
}

public class WaypointsModule extends Module {
    public WaypointsModule() { super("Waypoints", "Set and render waypoints.", Category.UTILITY); }
}
