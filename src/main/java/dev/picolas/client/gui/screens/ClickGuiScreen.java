package dev.picolas.client.gui.screens;

import dev.picolas.client.PicolasClient;
import dev.picolas.client.config.Setting;
import dev.picolas.client.module.Category;
import dev.picolas.client.module.Module;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

import java.util.List;

/**
 * Picolas Client — ClickGUI Screen
 *
 * Layout (mirrors the reference image):
 *   ┌─────────┬──────────────────────┬──────────────────┐
 *   │ Header  (full width)                               │
 *   ├─────────┼──────────────────────┬──────────────────┤
 *   │ Sidebar │  Module grid         │  Settings panel  │
 *   │ (cats.) │  (2 columns)         │  (right)         │
 *   └─────────┴──────────────────────┴──────────────────┘
 *
 * All coordinates are in scaled screen-space (DrawContext handles scaling).
 */
public class ClickGuiScreen extends Screen {

    // ── Palette ──────────────────────────────────────────────────────────────
    private static final int BG_DARK      = 0xF0080810; // near-black
    private static final int BG_PANEL     = 0xE0100018; // slightly lighter
    private static final int BG_CARD      = 0xCC14001F; // module card
    private static final int BG_CARD_SEL  = 0xCC200035; // selected card
    private static final int ACCENT_PUR   = 0xFF8A2BE2; // neon purple
    private static final int ACCENT_BLUE  = 0xFF00BFFF; // electric blue
    private static final int WHITE        = 0xFFFFFFFF;
    private static final int GRAY         = 0xFF888899;
    private static final int TOGGLE_ON    = 0xFF8A2BE2;
    private static final int TOGGLE_OFF   = 0xFF333344;

    // ── Layout constants ─────────────────────────────────────────────────────
    private static final int SIDEBAR_W   = 140;
    private static final int SETTINGS_W  = 200;
    private static final int HEADER_H    = 50;
    private static final int CAT_H       = 40;
    private static final int CARD_W      = 190;
    private static final int CARD_H      = 58;
    private static final int CARD_GAP    = 6;
    private static final int PADDING     = 12;

    // ── State ────────────────────────────────────────────────────────────────
    private Category selectedCategory = Category.RENDER;
    private Module   selectedModule   = null;

    // Animation (0-1 fade-in)
    private float alpha = 0f;

    public ClickGuiScreen() {
        super(Text.literal("Picolas Client"));
    }

    // ── Lifecycle ─────────────────────────────────────────────────────────────

    @Override
    public boolean shouldPause() { return false; }

    @Override
    public boolean shouldCloseOnEsc() { return true; }

    @Override
    public void tick() {
        super.tick();
        // Fade in over ~10 ticks
        if (alpha < 1f) alpha = Math.min(1f, alpha + 0.1f);
    }

    // ── Render ────────────────────────────────────────────────────────────────

    @Override
    public void render(DrawContext ctx, int mouseX, int mouseY, float delta) {
        // Dim world background
        renderBackground(ctx, mouseX, mouseY, delta);

        int sw = width;
        int sh = height;

        // The whole GUI is centered and capped in size
        int guiW = Math.min(sw - 40, 900);
        int guiH = Math.min(sh - 40, 600);
        int gx   = (sw - guiW) / 2;
        int gy   = (sh - guiH) / 2;

        // Main window background
        drawRoundRect(ctx, gx, gy, guiW, guiH, 12, BG_DARK);

        // Header
        renderHeader(ctx, gx, gy, guiW);

        // Sidebar
        renderSidebar(ctx, gx, gy + HEADER_H, SIDEBAR_W, guiH - HEADER_H);

        // Module grid
        int gridX = gx + SIDEBAR_W;
        int gridW = selectedModule != null ? guiW - SIDEBAR_W - SETTINGS_W : guiW - SIDEBAR_W;
        renderModuleGrid(ctx, gridX, gy + HEADER_H, gridW, guiH - HEADER_H, mouseX, mouseY);

        // Settings panel (only when a module is selected)
        if (selectedModule != null) {
            renderSettingsPanel(ctx, gx + SIDEBAR_W + gridW, gy + HEADER_H, SETTINGS_W, guiH - HEADER_H);
        }

        // Outer glow border
        drawBorder(ctx, gx, gy, guiW, guiH, 2, ACCENT_PUR);

        super.render(ctx, mouseX, mouseY, delta);
    }

    // ── Sub-renders ───────────────────────────────────────────────────────────

    private void renderHeader(DrawContext ctx, int x, int y, int w) {
        // Header bg
        ctx.fill(x, y, x + w, y + HEADER_H, BG_PANEL);
        // Bottom separator line (purple)
        ctx.fill(x, y + HEADER_H - 1, x + w, y + HEADER_H, ACCENT_PUR);

        // Logo "P" icon placeholder (purple square)
        ctx.fill(x + 12, y + 12, x + 36, y + 36, ACCENT_PUR);
        ctx.drawTextWithShadow(textRenderer, "P", x + 19, y + 18, WHITE);

        // Title: "PICOLAS" white, "CLIENT" purple
        ctx.drawTextWithShadow(textRenderer, "PICOLAS", x + 44, y + 13, WHITE);
        ctx.drawTextWithShadow(textRenderer, "CLIENT",  x + 44 + textRenderer.getWidth("PICOLAS "), y + 13, ACCENT_PUR);
        // Sub-line version
        ctx.drawTextWithShadow(textRenderer, "v1.0.0-beta | 1.21.1", x + 44, y + 27, GRAY);

        // Close hint (right side)
        ctx.drawTextWithShadow(textRenderer, "ESC to close", x + w - textRenderer.getWidth("ESC to close") - 12, y + 19, GRAY);
    }

    private void renderSidebar(DrawContext ctx, int x, int y, int w, int h) {
        ctx.fill(x, y, x + w, y + h, BG_PANEL);
        // Right separator
        ctx.fill(x + w - 1, y, x + w, y + h, 0xFF1A0030);

        int catY = y + 10;
        for (Category cat : Category.values()) {
            boolean selected = cat == selectedCategory;
            int catBg = selected ? ACCENT_PUR : 0x00000000;
            // Highlight bar
            if (selected) {
                ctx.fill(x + 6, catY, x + w - 6, catY + CAT_H - 2, 0x33FFFFFF);
                ctx.fill(x + 6, catY, x + 10, catY + CAT_H - 2, ACCENT_PUR); // left accent
            }
            ctx.drawTextWithShadow(textRenderer, cat.getDisplayName(), x + 18, catY + (CAT_H - 8) / 2, selected ? WHITE : GRAY);
            catY += CAT_H;
        }
    }

    private void renderModuleGrid(DrawContext ctx, int x, int y, int w, int h, int mx, int my) {
        // Panel label
        ctx.fill(x, y, x + w, y + h, BG_DARK);
        ctx.drawTextWithShadow(textRenderer, "■ " + selectedCategory.getDisplayName().toUpperCase() + " MODULES",
                x + PADDING, y + 10, ACCENT_PUR);

        List<Module> modules = PicolasClient.getInstance()
                .getModuleManager().getModulesByCategory(selectedCategory);

        int cols = 2;
        int startX = x + PADDING;
        int startY = y + 32;

        for (int i = 0; i < modules.size(); i++) {
            Module mod    = modules.get(i);
            int col       = i % cols;
            int row       = i / cols;
            int cx        = startX + col * (CARD_W + CARD_GAP);
            int cy        = startY + row * (CARD_H + CARD_GAP);

            boolean hover = mx >= cx && mx <= cx + CARD_W && my >= cy && my <= cy + CARD_H;
            boolean selec = mod == selectedModule;

            // Card background
            int cardBg = selec ? BG_CARD_SEL : (hover ? 0xCC1A0028 : BG_CARD);
            drawRoundRect(ctx, cx, cy, CARD_W, CARD_H, 6, cardBg);
            if (selec) drawBorder(ctx, cx, cy, CARD_W, CARD_H, 1, ACCENT_PUR);

            // Module name
            ctx.drawTextWithShadow(textRenderer, mod.getName(), cx + 36, cy + 10, WHITE);
            // Description
            ctx.drawTextWithShadow(textRenderer, mod.getDescription(), cx + 36, cy + 24, GRAY);

            // Toggle
            drawToggle(ctx, cx + CARD_W - 38, cy + (CARD_H - 12) / 2, mod.isEnabled());
        }
    }

    private void renderSettingsPanel(DrawContext ctx, int x, int y, int w, int h) {
        if (selectedModule == null) return;
        ctx.fill(x, y, x + w, y + h, BG_PANEL);
        ctx.fill(x, y, x + 1, y + h, ACCENT_PUR); // left border

        // Header
        ctx.drawTextWithShadow(textRenderer, "● " + selectedModule.getName().toUpperCase(), x + 12, y + 12, ACCENT_PUR);
        ctx.fill(x, y + 28, x + w, y + 29, 0xFF1A0030);

        int sy = y + 38;
        // Enabled toggle
        ctx.drawTextWithShadow(textRenderer, "Enabled", x + 12, sy + 2, WHITE);
        drawToggle(ctx, x + w - 38, sy, selectedModule.isEnabled());
        sy += 22;

        // Settings
        for (Setting<?> setting : selectedModule.getSettings()) {
            if (sy + 22 > y + h - 10) break;
            ctx.drawTextWithShadow(textRenderer, setting.getName(), x + 12, sy + 2, WHITE);

            if (setting.isBoolean()) {
                boolean val = (Boolean) setting.getValue();
                drawToggle(ctx, x + w - 38, sy, val);
            } else if (setting.isNumeric()) {
                // Slider
                double min  = ((Number) setting.getMinValue()).doubleValue();
                double max  = ((Number) setting.getMaxValue()).doubleValue();
                double val  = ((Number) setting.getValue()).doubleValue();
                double pct  = (val - min) / (max - min);
                int    sw2  = w - 24;
                int    slY  = sy + 14;
                ctx.fill(x + 12, slY, x + 12 + sw2, slY + 4, TOGGLE_OFF);
                ctx.fill(x + 12, slY, x + 12 + (int)(sw2 * pct), slY + 4, ACCENT_PUR);
                // Value label
                String valStr = setting.getValue() instanceof Double
                        ? String.format("%.1f", val) : String.valueOf((int) val);
                ctx.drawTextWithShadow(textRenderer, valStr, x + w - textRenderer.getWidth(valStr) - 12, sy + 2, ACCENT_BLUE);
                sy += 10;
            } else if (setting.isEnum()) {
                ctx.drawTextWithShadow(textRenderer, setting.getValue().toString(), x + w - textRenderer.getWidth(setting.getValue().toString()) - 12, sy + 2, ACCENT_BLUE);
            }

            sy += 22;
        }
    }

    // ── Mouse interaction ─────────────────────────────────────────────────────

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        int sw = width;
        int sh = height;
        int guiW = Math.min(sw - 40, 900);
        int guiH = Math.min(sh - 40, 600);
        int gx   = (sw - guiW) / 2;
        int gy   = (sh - guiH) / 2;

        int mx = (int) mouseX;
        int my = (int) mouseY;

        // Sidebar category click
        int sideX = gx;
        int sideY = gy + HEADER_H;
        int catY  = sideY + 10;
        for (Category cat : Category.values()) {
            if (mx >= sideX + 6 && mx <= sideX + SIDEBAR_W - 6
                    && my >= catY && my <= catY + CAT_H - 2) {
                selectedCategory = cat;
                selectedModule   = null;
                return true;
            }
            catY += CAT_H;
        }

        // Module card click
        List<Module> modules = PicolasClient.getInstance()
                .getModuleManager().getModulesByCategory(selectedCategory);

        int gridX  = gx + SIDEBAR_W;
        int startX = gridX + PADDING;
        int startY = gy + HEADER_H + 32;

        for (int i = 0; i < modules.size(); i++) {
            Module mod = modules.get(i);
            int col    = i % 2;
            int row    = i / 2;
            int cx     = startX + col * (CARD_W + CARD_GAP);
            int cy     = startY + row * (CARD_H + CARD_GAP);

            if (mx >= cx && mx <= cx + CARD_W && my >= cy && my <= cy + CARD_H) {
                if (button == 0) {
                    // Left click: toggle + show settings
                    mod.toggle();
                    selectedModule = (selectedModule == mod) ? null : mod;
                    PicolasClient.getInstance().getConfigManager().save();
                } else if (button == 1) {
                    // Right click: open settings
                    selectedModule = (selectedModule == mod) ? null : mod;
                }
                return true;
            }
        }

        // Settings panel toggle clicks
        if (selectedModule != null) {
            int settX = gx + SIDEBAR_W + (guiW - SIDEBAR_W - SETTINGS_W);
            if (mx >= settX) {
                handleSettingsClick(mx, my, settX, gy + HEADER_H);
            }
        }

        return super.mouseClicked(mouseX, mouseY, button);
    }

    private void handleSettingsClick(int mx, int my, int x, int y) {
        // Enabled toggle
        int tw = 28; int th = 14;
        int toggleX = x + SETTINGS_W - 38;
        int sy      = y + 38;

        if (mx >= toggleX && mx <= toggleX + tw && my >= sy && my <= sy + th) {
            selectedModule.toggle();
            PicolasClient.getInstance().getConfigManager().save();
        }
        sy += 22;

        for (Setting<?> setting : selectedModule.getSettings()) {
            if (setting.isBoolean()) {
                if (mx >= toggleX && mx <= toggleX + tw && my >= sy && my <= sy + th) {
                    ((Setting<Boolean>) setting).setValue(!(Boolean) setting.getValue());
                }
            }
            sy += 22;
            if (setting.isNumeric()) sy += 10;
        }
    }

    // ── Drawing helpers ───────────────────────────────────────────────────────

    /**
     * Draws a filled rectangle with rounded corners (approximated via fill calls).
     * For a true rounded rect you would use a shader; this is a reasonable fallback.
     */
    private void drawRoundRect(DrawContext ctx, int x, int y, int w, int h, int r, int color) {
        ctx.fill(x + r, y,     x + w - r, y + h,     color);
        ctx.fill(x,     y + r, x + r,     y + h - r, color);
        ctx.fill(x + w - r, y + r, x + w, y + h - r, color);
    }

    private void drawBorder(DrawContext ctx, int x, int y, int w, int h, int thickness, int color) {
        ctx.fill(x, y, x + w, y + thickness, color);
        ctx.fill(x, y + h - thickness, x + w, y + h, color);
        ctx.fill(x, y, x + thickness, y + h, color);
        ctx.fill(x + w - thickness, y, x + w, y + h, color);
    }

    private void drawToggle(DrawContext ctx, int x, int y, boolean on) {
        int tw = 28; int th = 14;
        int bg = on ? TOGGLE_ON : TOGGLE_OFF;
        ctx.fill(x, y, x + tw, y + th, bg);
        // Knob
        int kx = on ? x + tw - th + 2 : x + 2;
        ctx.fill(kx, y + 2, kx + th - 4, y + th - 2, WHITE);
    }
}
