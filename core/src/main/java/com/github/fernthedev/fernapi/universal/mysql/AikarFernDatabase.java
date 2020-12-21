package com.github.fernthedev.fernapi.universal.mysql;

import co.aikar.idb.*;
import com.github.fernthedev.fernapi.universal.Universal;
import com.github.fernthedev.fernapi.universal.handlers.FernAPIPlugin;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AikarFernDatabase {
    private static final List<Database> databases = new ArrayList<>();

    public static PooledDatabaseOptions getRecommendedOptions(FernAPIPlugin plugin, @NonNull String user, @NonNull String pass, @NonNull String db, @NonNull String hostAndPort) {
        DatabaseOptions options = DatabaseOptions
                .builder()
                .poolName(plugin.getPluginData().getName() + " DB")
                .logger(Logger.getLogger(Universal.getLogger().getName()))
                .mysql(user, pass, db, hostAndPort)
                .build();
        PooledDatabaseOptions poolOptions = PooledDatabaseOptions
                .builder()
                .options(options)
                .build();
        return poolOptions;
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
            Universal.getScheduler().runAsync(database::close);
        }
    }
}
