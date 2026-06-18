package dev.picolas.client.mixin;

import dev.picolas.client.PicolasClient;
import dev.picolas.client.event.events.RenderWorldEvent;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.util.math.MatrixStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Fires {@link RenderWorldEvent} after vanilla world rendering completes.
 *
 * NOTE: WorldRenderer#render's exact parameter list has changed between
 * Minecraft versions. If the build fails with a "method not found" mixin
 * error, open the deobfuscated WorldRenderer class for 1.21.1 and match
 * this @Inject's method signature to it exactly (or switch to method =
 * "render" with no descriptor and let Mixin select by name if there is
 * only one render(...) overload).
 */
@Mixin(WorldRenderer.class)
public class WorldRendererMixin {

    @Inject(method = "render", at = @At("RETURN"))
    private void picolas$onRenderEnd(RenderTickCounter tickCounter, boolean renderBlockOutline,
                                     CallbackInfo ci) {
        MatrixStack matrices = new MatrixStack();
        PicolasClient.getInstance().getEventBus()
                .post(new RenderWorldEvent(matrices, tickCounter.getTickDelta(true)));
    }
}
