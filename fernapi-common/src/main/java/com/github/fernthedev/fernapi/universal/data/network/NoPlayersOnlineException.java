package com.github.fernthedev.fernapi.universal.data.network;

public class NoPlayersOnlineException extends Exception {

    public NoPlayersOnlineException(String reason) {
        super("No players online. The reason players are needed: \n" + reason);
    }

    public NoPlayersOnlineException() {
        super("No Players Online");
    }

}
