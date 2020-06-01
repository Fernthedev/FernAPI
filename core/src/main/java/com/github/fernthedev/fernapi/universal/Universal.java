package com.github.fernthedev.fernapi.universal;

import co.aikar.commands.CommandManager;
import com.github.fernthedev.fernapi.universal.api.*;
import com.github.fernthedev.fernapi.universal.data.network.IPMessageHandler;
import com.github.fernthedev.fernapi.universal.exceptions.FernRuntimeException;
import com.github.fernthedev.fernapi.universal.exceptions.setup.IncorrectSetupException;
import com.github.fernthedev.fernapi.universal.handlers.*;
import com.github.fernthedev.fernapi.universal.mysql.DatabaseHandler;
import com.github.fernthedev.fernapi.universal.util.UniversalContextResolvers;
import com.github.fernthedev.fernapi.universal.util.VersionUtil;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import java.util.Locale;
import java.util.logging.Logger;

/**
 * Holds most of the core api
 */
public class Universal {

    private Universal() {}

    private static boolean setup = false;

    private static Universal instance = null;

    @Getter
    @Setter
    @NonNull
    private static ILocale locale = new Locale_EN_US();

    @Getter
    @Setter
    private static boolean debug = false;

    private static MethodInterface<?, ?> mi;
    private static IChatHandler<?> ch;
    private static IPMessageHandler mh;
    private static DatabaseHandler db;
    private static CommandManager comhand;
    private static NetworkHandler<? extends Object> nh;
    private static IScheduler<?,?> sh;

    private static FernAPIPlugin plugin;

    @Getter
    private static PluginData<?> pluginData;


    public static Universal getInstance() {
        return instance == null ? instance = new Universal() : instance;
    }

    public static Logger getLogger() {
        return getMethods().getLogger();
    }

    public void setup(@NonNull MethodInterface<?, ?> methodInterface, FernAPIPlugin aplugin,
                      IChatHandler<?> chatHandler, IPMessageHandler messageHandler, DatabaseHandler databaseHandler,
                      CommandManager commandHandler, NetworkHandler<? extends Object> networkHandler, IScheduler<?,?> iScheduler,
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

        comhand.getLocales().addMessageBundle("acf-fernapi", Locale.ENGLISH);

        // register the context
        comhand.getCommandContexts().registerIssuerOnlyContext(FernCommandIssuer.class, new UniversalContextResolvers.FernCommandIssuerContextResolver());
        comhand.getCommandContexts().registerIssuerOnlyContext(IFConsole.class, new UniversalContextResolvers.IFConsoleIssuerContextResolver());
        comhand.getCommandContexts().registerIssuerAwareContext(IFPlayer.class, new UniversalContextResolvers.SenderIFPlayerContextResolver());


        comhand.getCommandContexts().registerContext(OfflineFPlayer.class, new UniversalContextResolvers.SingularIFPlayerContextResolver());
        comhand.getCommandContexts().registerContext(IFPlayer[].class, new UniversalContextResolvers.OnlineIFPlayerArrayCommandResolver());



//        comhand.getCommandCompletions().registerAsyncCompletion("fernPlayers", context ->
//                mi.getPlayers().parallelStream()
//                        .filter(player -> !context.getIssuer().isPlayer() ||
//                                IFPlayer.canSee(
//                                        Universal.getMethods()
//                                                .convertCommandSenderToAPISender(context.getIssuer().getIssuer()),
//                                        player)
//                        ).map(IFPlayer::getName)
//                        .collect(Collectors.toList()));

        comhand.getCommandCompletions().setDefaultCompletion("players", IFPlayer.class, IFPlayer[].class, OfflineFPlayer.class, FernCommandIssuer.class);

        getMethods().getLogger().info("Registered FernAPI " + getMethods().getServerType().toString() + " using version " + VersionUtil.getVersionData());


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



    public static void debug(String message) {
        if(debug) {
            getMethods().getLogger().info("[DEBUG] " + message);
        }
    }

    public static MethodInterface<?, ?> getMethods() {
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

    public static NetworkHandler<? extends Object> getNetworkHandler() {
        checkNull();
        return nh;
    }

    public static CommandManager getCommandHandler() {
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
