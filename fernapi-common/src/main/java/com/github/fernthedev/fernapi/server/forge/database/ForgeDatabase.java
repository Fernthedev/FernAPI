package com.github.fernthedev.fernapi.server.forge.database;

import com.github.fernthedev.fernapi.server.forge.FernForgeAPI;
import com.github.fernthedev.fernapi.universal.DatabaseManager;
import com.github.fernthedev.fernapi.universal.handlers.DatabaseHandler;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.Timer;

public class ForgeDatabase extends DatabaseHandler {

    private FernForgeAPI forge;


    private static Timer sqlTimer = new Timer();


    public ForgeDatabase(FernForgeAPI forge) {
        this.forge = forge;
    }

    private boolean running;

    @Override
    protected void setupSchedule() {
        scheduled = true;
        running = true;
        if(sqlTimer == null) {
            sqlTimer = new Timer();
        }

        sqlTimer.cancel();
        sqlTimer.purge();

        new Thread(() -> {
            while(running) {
                try {
                    openConnectionOnAll();

                    for(DatabaseManager databaseManager : databaseManagerMap.values()) {
                        Statement statement = databaseManager.getConnection().createStatement();
                    }
                } catch (ClassNotFoundException | SQLException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    @Override
    public void stopSchedule() {
        scheduled = false;
        running = false;

        if(sqlTimer != null) {
            sqlTimer.cancel();
            sqlTimer.purge();
        }


    }
}
