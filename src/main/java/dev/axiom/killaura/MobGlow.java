package dev.axiom.killaura;

import net.minecraft.entity.Entity;
import net.minecraft.entity.mob.HostileEntity;

public final class MobGlow extends ClientModule {

    public MobGlow() {
        super("MobGlow", Category.RENDER, -1);
    }

    @Override
    public void onTick() {
        if (mc.world == null) return;

        for (Entity entity : mc.world.getEntities()) {
            if (entity instanceof HostileEntity && !entity.isGlowing()) {
                entity.setGlowing(true);
            }
        }
    }

    @Override
    protected void onDisable() {
        if (mc.world == null) return;

        for (Entity entity : mc.world.getEntities()) {
            if (entity instanceof HostileEntity) {
                entity.setGlowing(false);
            }
        }
    }
}
