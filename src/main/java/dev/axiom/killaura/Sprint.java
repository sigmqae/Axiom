package dev.axiom.killaura;

import net.minecraft.client.network.ClientPlayerEntity;

public final class Sprint extends ClientModule {

    public Sprint() { super("Sprint", Category.MOVEMENT, -1); }

    @Override
    public void onTick() {
        ClientPlayerEntity p = mc.player;
        if (p == null) return;
        if (p.input.movementForward > 0
                && !p.isSneaking()
                && !p.horizontalCollision
                && p.getHungerManager().getFoodLevel() > 6) {
            p.setSprinting(true);
        }
    }
}
