package com.github.fernthedev.fernapi.universal;

import com.github.fernthedev.fernapi.universal.data.network.IPMessageHandler;
import com.github.fernthedev.fernapi.universal.handlers.IChatHandler;
import com.github.fernthedev.fernapi.universal.handlers.IFPlayer;
import com.github.fernthedev.fernapi.universal.handlers.MethodInterface;
import lombok.NonNull;

public class Universal {

    private Universal() {}

    private static Universal instance = null;

    private static boolean debug = false;

    private static MethodInterface mi;
    private static IChatHandler ch;
    private static IPMessageHandler mh;


    public static Universal getInstance() {
        return instance == null ? instance = new Universal() : instance;
    }

    public void setup(MethodInterface methodInterface, IChatHandler chatHandler, IPMessageHandler messageHandler) {
        methodInterface.getLogger().info("Registered interface");
        mi = methodInterface;
        ch = chatHandler;
        mh = messageHandler;
    }

    public static void setDebug(boolean debug) {
        Universal.debug = debug;
    }

    public static void debug(Object message) {
        mi.getLogger().info("[DEBUG] " + message);
    }

    public static IFPlayer convertObjectPlayerToFPlayer(Object player) {
        return mi.convertPlayerObjectToFPlayer(player);
    }

    public static Object convertFPlayerToPlayer(@NonNull IFPlayer ifPlayer) {
        return mi.convertFPlayerToPlayer(ifPlayer);
    }

    public static MethodInterface getMethods() {
        return mi;
    }

    public static IChatHandler getChatHandler() {
        return ch;
    }

    public static IPMessageHandler getMessageHandler() {
        return mh;
    }
}
