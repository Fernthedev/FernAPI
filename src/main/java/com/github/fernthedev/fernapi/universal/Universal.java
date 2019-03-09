package com.github.fernthedev.fernapi.universal;

import com.github.fernthedev.fernapi.universal.data.network.IPMessageHandler;
import com.github.fernthedev.fernapi.universal.exceptions.setup.IncorrectSetupException;
import com.github.fernthedev.fernapi.universal.handlers.*;
import lombok.NonNull;

public class Universal {

    private Universal() {}

    private static boolean setup = false;

    private static Universal instance = null;

    private static boolean debug = false;

    private static MethodInterface mi;
    private static IChatHandler ch;
    private static IPMessageHandler mh;
    private static DatabaseHandler db;
    private static CommandHandler comhand;

    private static FernAPIPlugin plugin;


    public static Universal getInstance() {
        return instance == null ? instance = new Universal() : instance;
    }

    public void setup(@NonNull MethodInterface methodInterface,FernAPIPlugin aplugin, IChatHandler chatHandler, IPMessageHandler messageHandler, DatabaseHandler databaseHandler,CommandHandler commandHandler) {
        methodInterface.getLogger().info("Registered interface");
        setup = true;
        mi = methodInterface;
        ch = chatHandler;
        mh = messageHandler;
        db = databaseHandler;
        comhand = commandHandler;
        plugin = aplugin;

    }


    public static void checkNull() {
        if(mi == null || !setup) {
            try {
                throw new IncorrectSetupException("You have not setup the API correctly. Check this for more info: https://github.com/Fernthedev/FernAPI/blob/master/error/incorrectsetup.md",new NullPointerException());
            } catch (IncorrectSetupException e) {
                e.printStackTrace();
            }
        }
    }

    public static void setDebug(boolean debug) {
        Universal.debug = debug;
    }

    public static void debug(Object message) {
        if(debug) {
            getMethods().getLogger().info("[DEBUG] " + message);
        }
    }

    public static IFPlayer convertObjectPlayerToFPlayer(Object player) {
        return getMethods().convertPlayerObjectToFPlayer(player);
    }

    public static Object convertFPlayerToPlayer(@NonNull IFPlayer ifPlayer) {
        return getMethods().convertFPlayerToPlayer(ifPlayer);
    }

    public static MethodInterface getMethods() {
        checkNull();
        return mi;
    }

    public static IChatHandler getChatHandler() {
        checkNull();
        return ch;
    }

    public static IPMessageHandler getMessageHandler() {
        checkNull();
        return mh;
    }

    public static DatabaseHandler getDatabaseHandler() {
        checkNull();
        return db;
    }

    public static CommandHandler getCommandHandler() {
        checkNull();
        return comhand;
    }

    public static FernAPIPlugin getPlugin() {
        checkNull();
        return plugin;
    }
}