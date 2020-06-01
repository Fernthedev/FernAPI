package com.github.fernthedev.fernapi.universal.util;

import co.aikar.locales.MessageKey;
import co.aikar.locales.MessageKeyProvider;

import java.util.Locale;

public enum UniversalMinecraftMessageKeys implements MessageKeyProvider {
    USERNAME_TOO_SHORT,
    IS_NOT_A_VALID_NAME,
    MULTIPLE_PLAYERS_MATCH,
    NO_PLAYER_FOUND_SERVER,
    PLAYER_IS_VANISHED_CONFIRM,
    NO_PLAYER_FOUND_OFFLINE,
    NO_PLAYER_FOUND;

    private final MessageKey key = MessageKey.of("acf-minecraft." + this.name().toLowerCase(Locale.ENGLISH));

    public MessageKey getMessageKey() {
        return key;
    }

}
