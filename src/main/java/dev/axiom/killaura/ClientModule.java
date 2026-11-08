package dev.axiom.killaura;

import net.minecraft.client.MinecraftClient;

public abstract class ClientModule {
    public final String name;
    public final Category category;
    public final int defaultKey;
    protected final MinecraftClient mc = MinecraftClient.getInstance();
    private boolean enabled;

    protected ClientModule(String name, Category category, int defaultKey) {
        this.name = name;
        this.category = category;
        this.defaultKey = defaultKey;
    }

    public boolean isEnabled() { return enabled; }

    public void toggle() {
        enabled = !enabled;
        if (enabled) onEnable(); else onDisable();
    }

    protected void onEnable() {}
    protected void onDisable() {}
    public void onTick() {}              // 20 Hz
    public void onFrame(double dt) {}    // every rendered frame
}
