package com.github.fernthedev.fernapi.universal.handlers;

public enum ServerType {
    BUNGEE,
    BUKKIT,
    VELOCITY,
    SPONGE,
    OTHER;

    public boolean isProxy() {
        return this == BUNGEE || this == VELOCITY;
    }

    @Override
    public String toString() {
        return this.name();
    }
}
