package dev.axiom.killaura;

import net.minecraft.client.network.ClientPlayerEntity;

public final class Fly extends ClientModule {

    private static final float FLY_SPEED = 0.08f;   // vanilla default is 0.05

    public Fly() { super("Fly", Category.MOVEMENT, -1); }

    @Override
    public void onTick() {
        ClientPlayerEntity p = mc.player;
        if (p == null) return;
        p.getAbilities().allowFlying = true;
        p.getAbilities().setFlySpeed(FLY_SPEED);
    }

    @Override
    protected void onDisable() {
        ClientPlayerEntity p = mc.player;
        if (p == null) return;
        p.getAbilities().setFlySpeed(0.05f);
        if (!p.isCreative() && !p.isSpectator()) {
            p.getAbilities().allowFlying = false;
            p.getAbilities().flying = false;
        }
    }
}
