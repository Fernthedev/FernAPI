package com.github.fernthedev.fernapi.universal.api;

import lombok.Getter;
import net.kyori.adventure.audience.Audience;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public abstract class IFConsole<T> implements FernCommandIssuer {
    protected T commandSender;
    protected UUID uuid;

    @Getter
    protected Audience audience;


    public IFConsole(T commandSender, Audience audience) {
        this.commandSender = commandSender;
        this.audience = audience;
        uuid = UUID.randomUUID();
    }

    @NotNull
    @Override
    public UUID getUniqueId() {
        return uuid;
    }


    @Override
    public T getIssuer() {
        return commandSender;
    }

    /**
     * Is this issue a player, or server/console sender
     *
     * @return
     */
    @Override
    public boolean isPlayer() {
        return false;
    }

    @Override
    public String toString() {
        return "IFConsole{" +
                "commandSender=" + commandSender +
                ", uuid=" + uuid +
                '}';
    }
}
