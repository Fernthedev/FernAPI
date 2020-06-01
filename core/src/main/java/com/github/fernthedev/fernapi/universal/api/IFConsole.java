package com.github.fernthedev.fernapi.universal.api;

import co.aikar.commands.CommandManager;
import com.github.fernthedev.fernapi.universal.Universal;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public abstract class IFConsole<T> implements FernCommandIssuer {
    protected T commandSender;
    protected UUID uuid;

    public IFConsole(T commandSender) {
        this.commandSender =commandSender;
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
