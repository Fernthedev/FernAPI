package com.github.fernthedev.fernapi.server.spigot.database;

import com.github.fernthedev.fernapi.server.spigot.FernSpigotAPI;
import com.github.fernthedev.fernapi.universal.DatabaseManager;
import com.github.fernthedev.fernapi.universal.data.database.DatabaseInfo;
import com.github.fernthedev.fernapi.universal.handlers.DatabaseHandler;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitScheduler;

import java.sql.SQLException;
import java.sql.Statement;

public class SpigotDatabase extends DatabaseHandler {
    private static BukkitScheduler scheduler;
    private static DatabaseInfo data;
    private static boolean scheduled;

    private FernSpigotAPI spigot;

    public SpigotDatabase(FernSpigotAPI fernSpigotAPI) {
        this.spigot = fernSpigotAPI;
    }

    @Override
    protected void setupSchedule() {
        if (scheduler != null && scheduled) {
            scheduler.cancelTasks(spigot);
            scheduled = false;
        }

        BukkitRunnable runnable = new BukkitRunnable() {
            @Override
            public void run() {
                try {
                    openConnectionOnAll();

                    for(DatabaseManager databaseManager : databaseManagerMap.values()) {
                        Statement statement = databaseManager.getConnection().createStatement();
                    }
                } catch (ClassNotFoundException | SQLException e) {
                    e.printStackTrace();
                }
            }
        };
        scheduled = true;
        runnable.runTaskAsynchronously(spigot);
    }

    @Override
    public void stopSchedule() {
        scheduled = false;

        if (scheduler != null) {
            scheduler.cancelTasks(spigot);
        }
    }
}
