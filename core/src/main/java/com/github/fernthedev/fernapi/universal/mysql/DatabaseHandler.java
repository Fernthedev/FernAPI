package com.github.fernthedev.fernapi.universal.mysql;

import com.github.fernthedev.fernapi.universal.Universal;
import com.github.fernthedev.fernapi.universal.data.database.DatabaseAuthInfo;
import com.github.fernthedev.fernapi.universal.exceptions.database.DatabaseNotConnectedException;
import com.github.fernthedev.fernapi.universal.exceptions.database.DatabaseNotRegisteredException;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import java.sql.Connection;
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

    protected Map<DatabaseAuthInfo, DatabaseManager> databaseManagerMap = new HashMap<>();

    protected Runnable getScheduleRunnable() {
        return () -> {
            try {
                openConnectionOnAll();

                for (DatabaseManager databaseManager : databaseManagerMap.values()) {
                    databaseManager.getConnection().createStatement();
                }
            } catch (ClassNotFoundException | SQLException e) {
                e.printStackTrace();
            }
        };
    }

    protected abstract void setupSchedule();

    protected void openConnectionOnAll() throws SQLException, ClassNotFoundException {
        for(DatabaseAuthInfo databaseManager : databaseManagerMap.keySet()) {
            openConnection(databaseManager);
        }
    }



    public boolean openConnection(DatabaseAuthInfo dataInfo) throws SQLException, ClassNotFoundException {
        DatabaseManager manager = databaseManagerMap.get(dataInfo);

        boolean connected = false;

        if(manager == null) {
            throw new DatabaseNotRegisteredException("The database info was not correctly registered. Call registerDatabase to fix this",new NullPointerException());
        }
        Connection connection;
        try {
            connection = manager.getConnection();

            if (connection != null && !connection.isClosed()) {
                return true;
            }
        } catch (DatabaseNotConnectedException ignored) {}

//        try { //We use a try catch to avoid errors, hopefully we don't get any.
//            Class.forName("com.mysql.jdbc.Driver"); //this accesses Driver in jdbc.
//        } catch (ClassNotFoundException e) {
//            e.printStackTrace();
//            System.err.println("jdbc driver unavailable!");
//            throw e;
//        }

        try {
            Class.forName(dataInfo.getMysqlDatabaseType().getSqlDriver());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            Universal.getMethods().getLogger().severe("jdbc driver unavailable!");
            throw e;
        }

        if (!manager.isSetup()) {
            Universal.getMethods().getLogger().info("Connecting to MySQL now.");
        }

//        connection = DriverManager.getConnection(dataInfo.getUrlToDB(), dataInfo.getUsername(), dataInfo.getPassword());

        if (!manager.isSetup()) {
            manager.setFirstConnect(true);
            manager.createConnection(dataInfo);

            try {
                connected = manager.isConnected();
            } catch (DatabaseNotConnectedException ignored) {}

            manager.onConnectAttempt(connected);
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
    public void registerDatabase(@NonNull DatabaseAuthInfo databaseAuthInfo, @NonNull DatabaseManager databaseManager) {
        if (!databaseManager.isSetup()) {
            Universal.getMethods().getLogger().info("Setting database connection");
        }

        databaseManagerMap.put(databaseAuthInfo,databaseManager);
    }


    public void closeConnection() {
        for(DatabaseManager databaseManager : databaseManagerMap.values()) {
            Connection connection = null;
            try {
                connection = databaseManager.getConnection();
            } catch (DatabaseNotConnectedException ignored) { } catch (SQLException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) { }
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

