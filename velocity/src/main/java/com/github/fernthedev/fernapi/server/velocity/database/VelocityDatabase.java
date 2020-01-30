package com.github.fernthedev.fernapi.server.velocity.database;

import com.github.fernthedev.fernapi.server.velocity.FernVelocityAPI;
import com.github.fernthedev.fernapi.universal.mysql.DatabaseHandler;
import com.velocitypowered.api.scheduler.ScheduledTask;

import java.util.concurrent.TimeUnit;

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
//        Runnable runnable = () -> {
//            try {
//                openConnectionOnAll();
//
//                for(DatabaseManager databaseManager : databaseManagerMap.values()) {
//                    Statement statement = databaseManager.getConnection().createStatement();
//                }
//            } catch(ClassNotFoundException | SQLException e) {
//                e.printStackTrace();
//            }
//        };
        scheduled = true;
        task = velocity.getServer().getScheduler().buildTask(velocity, getScheduleRunnable()).repeat(scheduleTime, TimeUnit.MINUTES).schedule();
    }

    @Override
    public void stopSchedule() {
        if(task != null) {
            task.cancel();
            scheduled = false;
        }
    }
}
