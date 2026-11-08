package dev.axiom.killaura;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public final class ModuleManager {

    private static final List<ClientModule> MODULES = new ArrayList<>();
    private static final Map<ClientModule, KeyBinding> KEYS = new LinkedHashMap<>();
    private static KeyBinding menuKey;
    private static long lastNanos = System.nanoTime();

    private ModuleManager() {}

    public static List<ClientModule> all() { return MODULES; }

    public static List<ClientModule> in(Category c) {
        List<ClientModule> out = new ArrayList<>();
        for (ClientModule m : MODULES) if (m.category == c) out.add(m);
        return out;
    }

    public static void init() {
        // Combat
        register(new KillAura());
        register(new AimAssist());
        register(new AutoTotem());
        // Movement
        register(new Sprint());
        register(new Fly());
        // Render
        register(new Fullbright());
        register(new MobGlow());
        // Player
        register(new AutoRespawn());

        menuKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.killaura.menu",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_P,
                "category.killaura"));

        ClientTickEvents.END_CLIENT_TICK.register(mc -> {
            if (mc.player == null || mc.world == null) return;

            while (menuKey.wasPressed()) {
                if (mc.currentScreen == null) mc.setScreen(new ClickGuiScreen());
            }

            KEYS.forEach((module, key) -> {
                while (key.wasPressed()) {
                    module.toggle();
                    mc.player.sendMessage(Text.literal(
                            module.name + ": " + (module.isEnabled() ? "ON" : "OFF")), true);
                }
            });

            for (ClientModule m : MODULES) if (m.isEnabled()) m.onTick();
        });

        WorldRenderEvents.START.register(ctx -> {
            long now = System.nanoTime();
            double dt = Math.min((now - lastNanos) / 1_000_000_000.0, 0.1);
            lastNanos = now;

            if (MinecraftClient.getInstance().player == null) return;
            for (ClientModule m : MODULES) if (m.isEnabled()) m.onFrame(dt);
        });
    }

    private static void register(ClientModule m) {
        MODULES.add(m);
        KEYS.put(m, KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.killaura." + m.name.toLowerCase(),
                InputUtil.Type.KEYSYM,
                m.defaultKey,
                "category.killaura")));
    }
}
