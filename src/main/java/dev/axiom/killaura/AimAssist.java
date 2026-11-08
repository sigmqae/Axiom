package dev.axiom.killaura;

import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.glfw.GLFW;

import java.util.Comparator;

public final class AimAssist extends ClientModule {

    private static final double RANGE = 6.0;
    private static final double FOV_DEG = 45.0;
    private static final double SMOOTHING = 6.0;
    private static final double MAX_DEG_PER_SEC = 140.0;
    private static final double DEADZONE_DEG = 0.4;

    public AimAssist() { super("AimAssist", Category.COMBAT, GLFW.GLFW_KEY_G); }

    @Override
    public void onFrame(double dt) {
        ClientPlayerEntity player = mc.player;
        if (player == null || mc.world == null || mc.currentScreen != null) return;
        if (!mc.options.attackKey.isPressed()) return;
        if (!player.isAlive() || player.isSpectator()) return;

        Vec3d eye = player.getEyePos();
        float curYaw = player.getYaw();
        float curPitch = player.getPitch();

        LivingEntity target = mc.world
                .getEntitiesByClass(
                        LivingEntity.class,
                        player.getBoundingBox().expand(RANGE),
                        e -> e instanceof HostileEntity
                                && e.isAlive()
                                && player.squaredDistanceTo(e) <= RANGE * RANGE
                                && player.canSee(e))
                .stream()
                .filter(e -> angularDistance(eye, aimPoint(e), curYaw, curPitch) <= FOV_DEG)
                .min(Comparator.comparingDouble(
                        e -> angularDistance(eye, aimPoint(e), curYaw, curPitch)))
                .orElse(null);

        if (target == null) return;

        Vec3d p = aimPoint(target);
        double dx = p.x - eye.x, dy = p.y - eye.y, dz = p.z - eye.z;
        double flat = Math.sqrt(dx * dx + dz * dz);

        float wantYaw = (float) (Math.toDegrees(Math.atan2(dz, dx)) - 90.0);
        float wantPitch = (float) -Math.toDegrees(Math.atan2(dy, flat));

        float dYaw = MathHelper.wrapDegrees(wantYaw - curYaw);
        float dPitch = wantPitch - curPitch;

        if (Math.hypot(dYaw, dPitch) < DEADZONE_DEG) return;

        double blend = 1.0 - Math.exp(-SMOOTHING * dt);
        double cap = MAX_DEG_PER_SEC * dt;

        float stepYaw = (float) MathHelper.clamp(dYaw * blend, -cap, cap);
        float stepPitch = (float) MathHelper.clamp(dPitch * blend, -cap, cap);

        player.setYaw(curYaw + stepYaw);
        player.setPitch(MathHelper.clamp(curPitch + stepPitch, -90f, 90f));
    }

    private static Vec3d aimPoint(LivingEntity e) {
        return e.getBoundingBox().getCenter();
    }

    private static double angularDistance(Vec3d eye, Vec3d p, float yaw, float pitch) {
        double dx = p.x - eye.x, dy = p.y - eye.y, dz = p.z - eye.z;
        double flat = Math.sqrt(dx * dx + dz * dz);
        float wy = (float) (Math.toDegrees(Math.atan2(dz, dx)) - 90.0);
        float wp = (float) -Math.toDegrees(Math.atan2(dy, flat));
        return Math.hypot(MathHelper.wrapDegrees(wy - yaw), wp - pitch);
    }
}
