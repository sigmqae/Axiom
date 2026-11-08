package dev.axiom.killaura;

public enum Category {
    COMBAT("Combat"),
    MOVEMENT("Movement"),
    RENDER("Render"),
    PLAYER("Player");

    public final String label;

    Category(String label) { this.label = label; }
}
