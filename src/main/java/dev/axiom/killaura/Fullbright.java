package dev.axiom.killaura;

import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;

public final class Fullbright extends ClientModule {

    public Fullbright() { super("Fullbright", Category.RENDER, -1); }

    @Override
    public void onTick() {
        ClientPlayerEntity p = mc.player;
        if (p == null) return;
        StatusEffectInstance cur = p.getStatusEffect(StatusEffects.NIGHT_VISION);
        // refresh above 300 ticks remaining so the vanilla low-duration flicker never starts
        if (cur == null || cur.getDuration() < 300) {
            p.addStatusEffect(new StatusEffectInstance(
                    StatusEffects.NIGHT_VISION, 1000, 0, false, false, false));
        }
    }

    @Override
    protected void onDisable() {
        if (mc.player != null) mc.player.removeStatusEffect(StatusEffects.NIGHT_VISION);
    }
}
