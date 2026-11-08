package dev.axiom.killaura;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;

public final class ClickGuiScreen extends Screen {

    private static Category selected = Category.COMBAT;

    public ClickGuiScreen() {
        super(Text.literal("Axiom Client"));
    }

    @Override
    protected void init() {
        Category[] cats = Category.values();
        int tabW = 70, gap = 4;
        int total = cats.length * (tabW + gap) - gap;
        int x0 = (width - total) / 2;

        for (int i = 0; i < cats.length; i++) {
            Category c = cats[i];
            String text = (c == selected ? "> " : "") + c.label;
            addDrawableChild(ButtonWidget.builder(Text.literal(text), b -> {
                selected = c;
                clearAndInit();
            }).dimensions(x0 + i * (tabW + gap), 28, tabW, 22).build());
        }

        int w = 160, y = 62;
        for (ClientModule m : ModuleManager.in(selected)) {
            addDrawableChild(ButtonWidget.builder(label(m), b -> {
                m.toggle();
                b.setMessage(label(m));
            }).dimensions((width - w) / 2, y, w, 24).build());
            y += 28;
        }

        addDrawableChild(ButtonWidget.builder(Text.literal("Close"), b -> close())
                .dimensions(width / 2 - 40, height - 30, 80, 20).build());
    }

    private static Text label(ClientModule m) {
        return Text.literal(m.name + ": " + (m.isEnabled() ? "\u00a7aON" : "\u00a7cOFF"));
    }

    @Override
    public void render(DrawContext ctx, int mouseX, int mouseY, float delta) {
        renderBackground(ctx);
        ctx.drawCenteredTextWithShadow(textRenderer, title, width / 2, 10, 0xFFFFFF);
        super.render(ctx, mouseX, mouseY, delta);
    }

    @Override
    public boolean shouldPause() { return false; }
}
