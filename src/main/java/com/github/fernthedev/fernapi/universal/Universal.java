package com.github.fernthedev.fernapi.universal;

import com.github.fernthedev.fernapi.universal.api.IFPlayer;
import com.github.fernthedev.fernapi.universal.data.network.IPMessageHandler;
import com.github.fernthedev.fernapi.universal.exceptions.setup.IncorrectSetupException;
import com.github.fernthedev.fernapi.universal.handlers.*;
import com.github.fernthedev.fernapi.universal.mysql.DatabaseHandler;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import java.util.UUID;


public class Universal {

    private Universal() {}

    private static boolean setup = false;

    private static Universal instance = null;

    @Getter
    @Setter
    private static boolean debug = false;

    private static MethodInterface mi;
    private static IChatHandler ch;
    private static IPMessageHandler mh;
    private static DatabaseHandler db;
    private static CommandHandler comhand;
    private static NetworkHandler nh;
    private static IScheduler sh;

    private static FernAPIPlugin plugin;


    public static Universal getInstance() {
        return instance == null ? instance = new Universal() : instance;
    }

    public void setup(@NonNull MethodInterface methodInterface, FernAPIPlugin aplugin, IChatHandler chatHandler, IPMessageHandler messageHandler, DatabaseHandler databaseHandler, CommandHandler commandHandler, NetworkHandler networkHandler, IScheduler iScheduler) {
        methodInterface.getLogger().info("Registered interface");
        setup = true;
        mi = methodInterface;
        ch = chatHandler;
        mh = messageHandler;
        db = databaseHandler;
        comhand = commandHandler;
        plugin = aplugin;
        nh = networkHandler;
        sh = iScheduler;

    }


    protected static void checkNull() {
        if(mi == null || !setup) {
            try {
                throw new IncorrectSetupException("You have not setup the API correctly. Check this for more info: https://github.com/Fernthedev/FernAPI/blob/master/error/incorrectsetup.md",new NullPointerException());
            } catch (IncorrectSetupException e) {
                e.printStackTrace();
            }
        }
    }



    public static void debug(Object message) {
        if(debug) {
            getMethods().getLogger().info("[DEBUG] " + message);
        }
    }

    /**
     * Shortcut to getMethods()
     * This shortcut might me removed in later versions, attempt to avoid it
     */
    @Deprecated
    public static IFPlayer convertObjectPlayerToFPlayer(Object player) {
        return getMethods().convertPlayerObjectToFPlayer(player);
    }

    /**
     * Shortcut to getMethods()
     * This shortcut might me removed in later versions, attempt to avoid it
     */
    @Deprecated
    public static Object convertFPlayerToPlayer(@NonNull IFPlayer ifPlayer) {
        return getMethods().convertFPlayerToPlayer(ifPlayer);
    }

    /**
     * Shortcut to getMethods()
     * This shortcut might me removed in later versions, attempt to avoid it
     */
    @Deprecated
    public static IFPlayer getPlayerFromName(String name) {
        return getMethods().getPlayerFromName(name);
    }

    /**
     * Shortcut to getMethods()
     * This shortcut might me removed in later versions, attempt to avoid it
     */
    @Deprecated
    public static IFPlayer getPlayerFromUUID(UUID uuid) {
        return getMethods().getPlayerFromUUID(uuid);
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

    public static NetworkHandler getNetworkHandler() {
        checkNull();
        return nh;
    }

    public static CommandHandler getCommandHandler() {
        checkNull();
        return comhand;
    }

    public static IScheduler getScheduler() {
        checkNull();
        return sh;
    }

    public static FernAPIPlugin getPlugin() {
        checkNull();
        return plugin;
    }

    public void onDisable() {
        getDatabaseHandler().stopSchedule();
        getDatabaseHandler().closeConnection();
    }
}
