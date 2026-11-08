package dev.axiom.killaura;

import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.util.Hand;
import org.lwjgl.glfw.GLFW;

import java.util.Comparator;

public final class KillAura extends ClientModule {

    private static final double RANGE = 3.8;
    private static final double RANGE_SQ = RANGE * RANGE;
    private static final float MIN_CHARGE = 0.95f;

    public KillAura() { super("KillAura", Category.COMBAT, GLFW.GLFW_KEY_R); }

    @Override
    public void onTick() {
        ClientPlayerEntity player = mc.player;
        if (player == null || mc.world == null || mc.interactionManager == null) return;
        if (player.isSpectator() || !player.isAlive()) return;
        if (player.getAttackCooldownProgress(0.5f) < MIN_CHARGE) return;

        LivingEntity target = mc.world
                .getEntitiesByClass(
                        LivingEntity.class,
                        player.getBoundingBox().expand(RANGE),
                        e -> e instanceof HostileEntity
                                && e.isAlive()
                                && player.squaredDistanceTo(e) <= RANGE_SQ
                                && player.canSee(e))
                .stream()
                .min(Comparator.comparingDouble(player::squaredDistanceTo))
                .orElse(null);

        if (target == null) return;

        mc.interactionManager.attackEntity(player, target);
        player.swingHand(Hand.MAIN_HAND);
    }
}
