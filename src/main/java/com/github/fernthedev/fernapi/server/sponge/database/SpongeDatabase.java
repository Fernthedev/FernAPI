package com.github.fernthedev.fernapi.server.sponge.database;

import com.github.fernthedev.fernapi.server.sponge.FernSpongeAPI;
import com.github.fernthedev.fernapi.universal.DatabaseManager;
import com.github.fernthedev.fernapi.universal.data.database.DatabaseInfo;
import com.github.fernthedev.fernapi.universal.handlers.DatabaseHandler;
import org.spongepowered.api.scheduler.Task;

import java.sql.SQLException;
import java.sql.Statement;

public class SpongeDatabase extends DatabaseHandler {
    private static DatabaseInfo data;
    private static boolean scheduled;

    private Task refreshTask;

    private FernSpongeAPI spigot;

    public SpongeDatabase(FernSpongeAPI fernSpigotAPI) {
        this.spigot = fernSpigotAPI;
    }

    @Override
    protected void setupSchedule() {
        Task.Builder scheduler = Task.builder();
        if (scheduled && refreshTask != null) {
            refreshTask.cancel();
            scheduled = false;
        }

        Runnable runnable = () -> {
            try {
                openConnectionOnAll();

                for(DatabaseManager databaseManager : databaseManagerMap.values()) {
                    Statement statement = databaseManager.getConnection().createStatement();
                }
            } catch (ClassNotFoundException | SQLException e) {
                e.printStackTrace();
            }
        };

        scheduled = true;
        scheduler.execute(runnable).async().submit(spigot);
    }

    @Override
    public void stopSchedule() {
        scheduled = false;

        if(refreshTask != null) {
            refreshTask.cancel();
        }
    }
}