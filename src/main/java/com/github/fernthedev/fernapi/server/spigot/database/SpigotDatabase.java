package com.github.fernthedev.fernapi.server.spigot.database;

import com.github.fernthedev.fernapi.server.spigot.FernSpigotAPI;
import com.github.fernthedev.fernapi.universal.DatabaseManager;
import com.github.fernthedev.fernapi.universal.handlers.DatabaseHandler;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.sql.SQLException;
import java.sql.Statement;

public class SpigotDatabase extends DatabaseHandler {
    private static boolean scheduled;

    private static BukkitTask task;

    private FernSpigotAPI spigot;

    public SpigotDatabase(FernSpigotAPI fernSpigotAPI) {
        this.spigot = fernSpigotAPI;
    }

    @Override
    protected void setupSchedule() {
        if (task != null && scheduled) {
            task.cancel();
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
        task = runnable.runTaskAsynchronously(spigot);
    }

    @Override
    public void stopSchedule() {
        scheduled = false;

        if (task != null) {
            task.cancel();
        }
    }
}
