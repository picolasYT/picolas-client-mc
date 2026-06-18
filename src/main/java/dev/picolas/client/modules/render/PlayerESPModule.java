package dev.picolas.client.modules.render;

import dev.picolas.client.config.Setting;
import dev.picolas.client.event.bus.EventHandler;
import dev.picolas.client.event.events.RenderWorldEvent;
import dev.picolas.client.module.Category;
import dev.picolas.client.module.Module;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Box;

/**
 * Player ESP — draws boxes (or corner brackets) around nearby players.
 */
public class PlayerESPModule extends Module {

    public enum Mode { BOX, CORNER, OUTLINE }

    private final Setting<Mode>    mode           = addSetting(new Setting<>("Mode", "ESP render mode", Mode.BOX));
    private final Setting<Boolean> throughWalls   = addSetting(new Setting<>("Through Walls", "Show through walls", true));
    private final Setting<Boolean> showHealth     = addSetting(new Setting<>("Show Health", "Show player health", true));
    private final Setting<Boolean> showArmor      = addSetting(new Setting<>("Show Armor", "Show armor value", true));
    private final Setting<Boolean> showDistance   = addSetting(new Setting<>("Show Distance", "Show distance", true));
    private final Setting<Double>  maxDistance    = addSetting(new Setting<>("Max Distance", "Max render distance", 100.0, 10.0, 512.0));
    private final Setting<Boolean> renderFriendly = addSetting(new Setting<>("Render Friendlies", "Render team members", false));

    // ESP color — stored as ARGB int
    private int espColor = 0xFF8A2BE2; // neon purple by default

    public PlayerESPModule() {
        super("Player ESP", "Shows players with ESP boxes.", Category.RENDER);
    }

    @EventHandler
    public void onRenderWorld(RenderWorldEvent event) {
        if (mc().world == null || mc().player == null) return;

        MatrixStack matrices = event.getMatrices();

        for (PlayerEntity player : mc().world.getPlayers()) {
            if (player == mc().player) continue;

            double dist = mc().player.distanceTo(player);
            if (dist > maxDistance.getValue()) continue;

            renderESP(matrices, player);
        }
    }

    private void renderESP(MatrixStack matrices, PlayerEntity player) {
        // Camera offset for accurate world-space rendering
        double cx = mc().getEntityRenderDispatcher().camera.getPos().x;
        double cy = mc().getEntityRenderDispatcher().camera.getPos().y;
        double cz = mc().getEntityRenderDispatcher().camera.getPos().z;

        Box box = player.getBoundingBox().expand(0.1).offset(
                -cx + player.getX() - player.getX(),
                -cy + player.getY() - player.getY(),
                -cz + player.getZ() - player.getZ()
        );

        // Actual offset from camera
        double x = player.getX() - cx;
        double y = player.getY() - cy;
        double z = player.getZ() - cz;

        matrices.push();
        matrices.translate(x, y, z);

        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder buffer    = tessellator.begin(VertexFormat.DrawMode.DEBUG_LINES,
                                                    VertexFormats.POSITION_COLOR);

        float r = ((espColor >> 16) & 0xFF) / 255f;
        float g = ((espColor >> 8)  & 0xFF) / 255f;
        float b = ( espColor        & 0xFF) / 255f;
        float a = ((espColor >> 24) & 0xFF) / 255f;

        // Draw box wireframe
        double hw = player.getWidth()  / 2.0 + 0.1;
        double hh = player.getHeight() + 0.1;

        drawBox(buffer, matrices, -hw, 0, -hw, hw, hh, hw, r, g, b, a);

        matrices.pop();
    }

    private void drawBox(BufferBuilder buffer, MatrixStack matrices,
                         double x1, double y1, double z1,
                         double x2, double y2, double z2,
                         float r, float g, float b, float a) {
        var entry = matrices.peek().getPositionMatrix();
        // Bottom face
        buffer.vertex(entry, (float)x1, (float)y1, (float)z1).color(r,g,b,a);
        buffer.vertex(entry, (float)x2, (float)y1, (float)z1).color(r,g,b,a);
        buffer.vertex(entry, (float)x2, (float)y1, (float)z1).color(r,g,b,a);
        buffer.vertex(entry, (float)x2, (float)y1, (float)z2).color(r,g,b,a);
        buffer.vertex(entry, (float)x2, (float)y1, (float)z2).color(r,g,b,a);
        buffer.vertex(entry, (float)x1, (float)y1, (float)z2).color(r,g,b,a);
        buffer.vertex(entry, (float)x1, (float)y1, (float)z2).color(r,g,b,a);
        buffer.vertex(entry, (float)x1, (float)y1, (float)z1).color(r,g,b,a);
        // Top face
        buffer.vertex(entry, (float)x1, (float)y2, (float)z1).color(r,g,b,a);
        buffer.vertex(entry, (float)x2, (float)y2, (float)z1).color(r,g,b,a);
        buffer.vertex(entry, (float)x2, (float)y2, (float)z1).color(r,g,b,a);
        buffer.vertex(entry, (float)x2, (float)y2, (float)z2).color(r,g,b,a);
        buffer.vertex(entry, (float)x2, (float)y2, (float)z2).color(r,g,b,a);
        buffer.vertex(entry, (float)x1, (float)y2, (float)z2).color(r,g,b,a);
        buffer.vertex(entry, (float)x1, (float)y2, (float)z2).color(r,g,b,a);
        buffer.vertex(entry, (float)x1, (float)y2, (float)z1).color(r,g,b,a);
        // Verticals
        buffer.vertex(entry, (float)x1, (float)y1, (float)z1).color(r,g,b,a);
        buffer.vertex(entry, (float)x1, (float)y2, (float)z1).color(r,g,b,a);
        buffer.vertex(entry, (float)x2, (float)y1, (float)z1).color(r,g,b,a);
        buffer.vertex(entry, (float)x2, (float)y2, (float)z1).color(r,g,b,a);
        buffer.vertex(entry, (float)x2, (float)y1, (float)z2).color(r,g,b,a);
        buffer.vertex(entry, (float)x2, (float)y2, (float)z2).color(r,g,b,a);
        buffer.vertex(entry, (float)x1, (float)y1, (float)z2).color(r,g,b,a);
        buffer.vertex(entry, (float)x1, (float)y2, (float)z2).color(r,g,b,a);
    }

    public int  getEspColor()          { return espColor;  }
    public void setEspColor(int color) { espColor = color; }
}
