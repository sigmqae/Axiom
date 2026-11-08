package dev.axiom.killaura;

public final class AutoRespawn extends ClientModule {

    public AutoRespawn() { super("AutoRespawn", Category.PLAYER, -1); }

    @Override
    public void onTick() {
        if (mc.player != null && mc.player.isDead()) mc.player.requestRespawn();
    }
}
