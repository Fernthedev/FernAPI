package com.github.fernthedev.fernapi.server.bungee.database;

import com.github.fernthedev.fernapi.server.bungee.FernBungeeAPI;
import com.github.fernthedev.fernapi.universal.mysql.DatabaseManager;
import com.github.fernthedev.fernapi.universal.mysql.DatabaseHandler;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.scheduler.ScheduledTask;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.concurrent.TimeUnit;

public class BungeeDatabase extends DatabaseHandler {
    private FernBungeeAPI bungee;
    private ScheduledTask task;

    public BungeeDatabase(FernBungeeAPI bungee) {
        this.bungee = bungee;
    }

    @Override
    protected void setupSchedule() {
        if(task != null) {
            task.cancel();
            scheduled = false;
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
        task = ProxyServer.getInstance().getScheduler().schedule(bungee, runnable, 0, scheduleTime, TimeUnit.MINUTES);
    }

    @Override
    public void stopSchedule() {
        if(task != null) {
            task.cancel();
            scheduled = false;
        }
    }
}
