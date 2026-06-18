package dev.picolas.client.mixin;

import dev.picolas.client.PicolasClient;
import dev.picolas.client.module.Module;
import net.minecraft.client.render.GameRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameRenderer.class)
public class GameRendererMixin {

    /**
     * Cancel fog setup when NoFog is active.
     */
    @Inject(method = "renderWorld", at = @At("HEAD"))
    private void onRenderWorld(float tickDelta, long limitTime, CallbackInfo ci) {
        // Post world render event so modules can hook in
        // (Full render event needs MatrixStack — handled via WorldRendererMixin)
    }
}
