package com.github.fernthedev.fernapi.universal.api;

import co.aikar.commands.CommandIssuer;
import co.aikar.commands.CommandManager;
import com.github.fernthedev.fernapi.universal.Universal;
import com.github.fernthedev.fernapi.universal.data.chat.BaseMessage;
import com.github.fernthedev.fernapi.universal.data.chat.TextMessage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.github.fernthedev.fernapi.universal.api.IFPlayer.VANISH_PERMISSIONS;

public interface FernCommandIssuer extends CommandIssuer {
    /**
     * @param message
     * @deprecated Do not call this, for internal use. Not considered part of the API and may break.
     */
    @Deprecated
    @Override
    default void sendMessageInternal(String message) {
        sendMessage(TextMessage.fromColor(message));
    }

    void sendMessage(BaseMessage message);

    default boolean hasVanishPermission(String... strings) {
        List<String> vanishPermissions = new ArrayList<>(VANISH_PERMISSIONS);

        vanishPermissions.addAll(Arrays.asList(strings));

        return vanishPermissions.parallelStream().anyMatch(this::hasPermission);
    }


    @Override
    default CommandManager getManager() {
        return Universal.getCommandHandler();
    }


}
