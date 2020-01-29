package com.github.fernthedev.fernapi.universal;

import com.github.fernthedev.fernapi.universal.api.ILocale;
import com.github.fernthedev.fernapi.universal.api.Locale;
import com.github.fernthedev.fernapi.universal.api.PluginData;
import com.github.fernthedev.fernapi.universal.api.URLGit;
import com.github.fernthedev.fernapi.universal.data.network.IPMessageHandler;
import com.github.fernthedev.fernapi.universal.exceptions.FernRuntimeException;
import com.github.fernthedev.fernapi.universal.exceptions.setup.IncorrectSetupException;
import com.github.fernthedev.fernapi.universal.handlers.*;
import com.github.fernthedev.fernapi.universal.mysql.DatabaseHandler;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

/**
 * Holds most of the core api
 */
public class Universal {

    private Universal() {}

    private static boolean setup = false;

    private static Universal instance = null;

    @Getter
    @Setter
    private static ILocale locale = new Locale();

    @Getter
    @Setter
    private static boolean debug = false;

    private static MethodInterface<?> mi;
    private static IChatHandler<?> ch;
    private static IPMessageHandler mh;
    private static DatabaseHandler db;
    private static CommandHandler comhand;
    private static NetworkHandler nh;
    private static IScheduler<?,?> sh;

    private static FernAPIPlugin plugin;

    @Getter
    private static PluginData<?> pluginData;


    public static Universal getInstance() {
        return instance == null ? instance = new Universal() : instance;
    }

    public void setup(@NonNull MethodInterface<?> methodInterface, FernAPIPlugin aplugin,
                      IChatHandler<?> chatHandler, IPMessageHandler messageHandler, DatabaseHandler databaseHandler,
                      CommandHandler commandHandler, NetworkHandler networkHandler, IScheduler<?,?> iScheduler,
                      PluginData<?> pluginData
    ) {
        if (setup) throw new FernRuntimeException("The interface has already been registered.");


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
        Universal.pluginData = pluginData;
    }


    protected static void checkNull() {
        if(mi == null || !setup) {
            try {
                throw new IncorrectSetupException("You have not setup the API correctly. Check this for more info: " + URLGit.INCORRECT_SETUP, new NullPointerException());
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

    public static MethodInterface<?> getMethods() {
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
