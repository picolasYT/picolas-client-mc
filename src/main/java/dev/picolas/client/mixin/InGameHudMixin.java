package dev.picolas.client.mixin;

import dev.picolas.client.PicolasClient;
import dev.picolas.client.event.events.RenderHudEvent;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InGameHud.class)
public class InGameHudMixin {

    @Inject(method = "render", at = @At("TAIL"))
    private void onRender(DrawContext context, float tickDelta, CallbackInfo ci) {
        PicolasClient.getInstance().getEventBus()
                .post(new RenderHudEvent(context, tickDelta));
    }
}
