package com.github.fernthedev.fernapi.universal.handlers;

import com.github.fernthedev.fernapi.universal.DatabaseManager;
import com.github.fernthedev.fernapi.universal.Universal;
import com.github.fernthedev.fernapi.universal.data.database.DatabaseInfo;
import com.github.fernthedev.fernapi.universal.exceptions.database.DatabaseNotRegisteredException;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;


public abstract class DatabaseHandler {
    protected boolean scheduled;

    /**
     * The rate of minutes it takes to reconnect to the sql database.
     */
    @Setter
    @Getter
    protected int scheduleTime = 15;

    protected Map<DatabaseInfo,DatabaseManager> databaseManagerMap = new HashMap<>();



    protected abstract void setupSchedule();

    protected void openConnectionOnAll() throws SQLException, ClassNotFoundException {
        for(DatabaseInfo databaseManager : databaseManagerMap.keySet()) {
            openConnection(databaseManager);
        }
    }



    public boolean openConnection(DatabaseInfo dataInfo) throws SQLException, ClassNotFoundException {
        DatabaseManager manager = databaseManagerMap.get(dataInfo);

        boolean connected = false;

        if(manager == null) {
            throw new DatabaseNotRegisteredException("The database info was not correctly registered. Call registerDatabase to fix this",new NullPointerException());
        }
        Connection connection = manager.getConnection();

        if (connection != null && !connection.isClosed()) {
            return true;
        }

        try { //We use a try catch to avoid errors, hopefully we don't get any.
            Class.forName("com.mysql.jdbc.Driver"); //this accesses Driver in jdbc.
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            System.err.println("jdbc driver unavailable!");
            throw e;
        }

        if (!manager.isSetup()) {
            Universal.getMethods().getLogger().info("Connecting to MySQL now.");
        }

        connection = DriverManager.getConnection(dataInfo.getUrlToDB(), dataInfo.getUsername(), dataInfo.getPassword());

        if (!manager.isSetup()) {
            connected = !connection.isClosed();

            manager.onConnectAttempt(connected);
            manager.setConnection(connection);

            Universal.getMethods().getLogger().info("Connected successfully");
            manager.setSetup(true);

            setupSchedule();
        }

        return connected;
    }

    public abstract void stopSchedule();


    public DatabaseHandler() {
    }

    /**]
     * Registers the databaseManager for calling events.
     * @param databaseManager The manager
     */
    public void registerDatabase(@NonNull DatabaseInfo databaseInfo, @NonNull DatabaseManager databaseManager) {
        if (!databaseManager.isSetup()) {
            Universal.getMethods().getLogger().info("Setting database connection");
        }

        databaseManagerMap.put(databaseInfo,databaseManager);
    }


    public void closeConnection() {
        for(DatabaseManager databaseManager : databaseManagerMap.values()) {
            Connection connection = databaseManager.getConnection();
            // invoke on disable.
            try { //using a try catch to catch connection errors (like wrong sql password...)
                if (connection != null && !connection.isClosed()) { //checking if connection isn't null to
                    //avoid receiving a nullpointer
                    connection.close(); //closing the connection field variable.
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}

