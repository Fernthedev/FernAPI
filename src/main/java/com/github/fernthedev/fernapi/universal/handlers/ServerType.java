package com.github.fernthedev.fernapi.universal.handlers;

public enum ServerType {
    BUNGEE,
    BUKKIT,
    SPONGE;

    @Override
    public String toString() {
        return this.name();
    }
}
