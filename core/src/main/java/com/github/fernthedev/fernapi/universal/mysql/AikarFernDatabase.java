package com.github.fernthedev.fernapi.universal.mysql;

import co.aikar.idb.*;
import com.github.fernthedev.fernapi.universal.Universal;
import com.github.fernthedev.fernapi.universal.data.database.DatabaseAuthInfo;
import com.github.fernthedev.fernapi.universal.handlers.FernAPIPlugin;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AikarFernDatabase {
    private static final List<Database> databases = new ArrayList<>();

    public static PooledDatabaseOptions getRecommendedOptions(FernAPIPlugin plugin, @NonNull String user, @NonNull String pass, @NonNull String db, @NonNull String hostAndPort) {
        boolean optimizations = true;

        try {
            Class.forName("org.mariadb.jdbc.MariaDbDataSource");
            optimizations = false;
            Universal.debug("Disabling optimizations since MariaDB is used. Should this be done?");
        } catch (ClassNotFoundException e) {
            Universal.debug("Enabling optimizations");
        }

        DatabaseOptions options = DatabaseOptions
                .builder()
                .poolName(plugin.getPluginData().getName() + " DB")
                .logger(Universal.getLogger())
                .mysql(user, pass, db, hostAndPort)
                .useOptimizations(optimizations)
                .build();
        return PooledDatabaseOptions
                .builder()
                .options(options)
                .build();
    }
    public static Database createHikariDatabase(FernAPIPlugin plugin, DatabaseAuthInfo databaseAuthInfo) {
        return createHikariDatabase(getRecommendedOptions(plugin, databaseAuthInfo.getUsername(), databaseAuthInfo.getPassword(), databaseAuthInfo.getDatabase(), databaseAuthInfo.getUrlHost() + ":" + databaseAuthInfo.getPort()));
    }

    public static Database createHikariDatabase(FernAPIPlugin plugin, @NonNull String user, @NonNull String pass, @NonNull String db, @NonNull String hostAndPort) {
        return createHikariDatabase(getRecommendedOptions(plugin, user, pass, db, hostAndPort));
    }

    public static Database createHikariDatabase(PooledDatabaseOptions options) {
        return createHikariDatabase(options, true);
    }

    public static Database createHikariDatabase(PooledDatabaseOptions options, boolean setGlobal) {
        HikariPooledDatabase db = new HikariPooledDatabase(options);
        if (setGlobal) {
            DB.setGlobalDatabase(db);
        }
        databases.add(db);

        return db;
    }
    
    public static void shutdownDatabases() {
        for (Database database : databases) {
            new Thread(database::close).start();
        }
    }
}
