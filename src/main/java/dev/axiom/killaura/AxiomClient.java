package dev.axiom.killaura;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;

public class AxiomClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        ModuleManager.init();

        HudRenderCallback.EVENT.register((ctx, tickDelta) -> {
            MinecraftClient mc = MinecraftClient.getInstance();
            if (mc.player == null || mc.options.hudHidden) return;

            int y = 4;
            int screenW = ctx.getScaledWindowWidth();
            for (ClientModule m : ModuleManager.all()) {
                if (!m.isEnabled()) continue;
                int x = screenW - mc.textRenderer.getWidth(m.name) - 4;
                ctx.drawTextWithShadow(mc.textRenderer, m.name, x, y, 0x55FF55);
                y += 10;
            }
        });
    }
}
