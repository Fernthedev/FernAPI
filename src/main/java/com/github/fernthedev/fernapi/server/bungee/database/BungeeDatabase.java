package com.github.fernthedev.fernapi.server.bungee.database;

import com.github.fernthedev.fernapi.server.bungee.FernBungeeAPI;
import com.github.fernthedev.fernapi.universal.DatabaseManager;
import com.github.fernthedev.fernapi.universal.handlers.DatabaseHandler;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.scheduler.TaskScheduler;

import java.sql.SQLException;
import java.sql.Statement;

public class BungeeDatabase extends DatabaseHandler {
    private static TaskScheduler scheduler;

    private FernBungeeAPI bungee;

    public BungeeDatabase(FernBungeeAPI bungee) {
        this.bungee = bungee;
    }

    @Override
    protected void setupSchedule() {
        if(scheduler != null && scheduled) {
            scheduler.cancel(bungee);
            scheduled =false;
        }
        Runnable runnable = () -> {
            try {
                openConnectionOnAll();

                for(DatabaseManager databaseManager : databaseManagerMap.values()) {
                    Statement statement = databaseManager.getConnection().createStatement();
                }
            } catch(ClassNotFoundException | SQLException e) {
                e.printStackTrace();
            }
        };
        scheduled = true;
        ProxyServer.getInstance().getScheduler().runAsync(bungee,runnable);
    }

    @Override
    public void stopSchedule() {
        if(scheduler != null) {
            scheduler.cancel(bungee);
            scheduled = false;
        }
    }
}
