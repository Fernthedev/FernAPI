package com.github.fernthedev.fernapi.universal;

public enum ServerType {
    BUNGEE,
    BUKKIT,
    FORGE;

    @Override
    public String toString() {
        return this.name();
    }
}
