package com.github.fernthedev.fernapi.server.velocity.database;

import com.github.fernthedev.fernapi.server.velocity.FernVelocityAPI;
import com.github.fernthedev.fernapi.universal.DatabaseManager;
import com.github.fernthedev.fernapi.universal.handlers.DatabaseHandler;
import com.velocitypowered.api.scheduler.ScheduledTask;

import java.sql.SQLException;
import java.sql.Statement;

public class VelocityDatabase extends DatabaseHandler {
    private FernVelocityAPI velocity;
    private static ScheduledTask task;

    public VelocityDatabase(FernVelocityAPI velocity) {
        this.velocity = velocity;
    }

    @Override
    protected void setupSchedule() {
        if(task != null && scheduled) {
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
        task = velocity.getServer().getScheduler().buildTask(velocity, runnable).schedule();
    }

    @Override
    public void stopSchedule() {
        if(task != null) {
            task.cancel();
            scheduled = false;
        }
    }
}
