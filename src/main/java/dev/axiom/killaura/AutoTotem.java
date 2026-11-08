package dev.axiom.killaura;

import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.item.Items;
import net.minecraft.screen.slot.SlotActionType;

public final class AutoTotem extends ClientModule {

    private static final float HEALTH_THRESHOLD = 8.0f;
    private int cooldown;

    public AutoTotem() { super("AutoTotem", Category.COMBAT, -1); }

    @Override
    public void onTick() {
        ClientPlayerEntity p = mc.player;
        if (p == null || mc.interactionManager == null || mc.currentScreen != null) return;
        if (cooldown > 0) { cooldown--; return; }
        if (p.getHealth() > HEALTH_THRESHOLD) return;
        if (p.getOffHandStack().isOf(Items.TOTEM_OF_UNDYING)) return;

        for (int i = 0; i < 36; i++) {
            if (!p.getInventory().getStack(i).isOf(Items.TOTEM_OF_UNDYING)) continue;
            int slot = i < 9 ? 36 + i : i;   // hotbar maps to 36-44 in the player screen handler
            mc.interactionManager.clickSlot(
                    p.playerScreenHandler.syncId, slot, 40, SlotActionType.SWAP, p);
            cooldown = 5;
            return;
        }
    }
}
