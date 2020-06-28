package com.github.fernthedev.fernapi.universal.mysql;

import com.github.fernthedev.fernapi.universal.Universal;
import com.github.fernthedev.fernapi.universal.data.ScheduleTaskWrapper;
import com.github.fernthedev.fernapi.universal.data.database.DatabaseAuthInfo;
import com.github.fernthedev.fernapi.universal.exceptions.database.DatabaseNotConnectedException;
import com.github.fernthedev.fernapi.universal.exceptions.database.DatabaseNotRegisteredException;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;


public class DatabaseHandler {
    private static final Map<String, AbstractSQLDriver> driverMap = new HashMap<>();
    protected boolean scheduled;
    /**
     * The rate of minutes it takes to reconnect to the sql database.
     */
    @Setter
    @Getter
    protected int scheduleTime = 15;
    protected Map<DatabaseAuthInfo, DatabaseManager> databaseManagerMap = new HashMap<>();
    private ScheduleTaskWrapper<?, ?> task;

    public static final DatabaseHandler instance = new DatabaseHandler();

    public static void registerSQLDriver(AbstractSQLDriver sqlDriver) {
        driverMap.put(sqlDriver.getSqlIdentifierName(), sqlDriver);
    }

    public static AbstractSQLDriver getSqlDriver(String sqlDriver) {
        return driverMap.get(sqlDriver);
    }

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

    protected Class<? extends AbstractSQLDriver> supportedSQL() {
        return JDBC_SQLDriver.class;
    }

    protected void setupSchedule() {
        if(task != null) {
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
        task = Universal.getScheduler().runSchedule(getScheduleRunnable(), 0, scheduleTime, TimeUnit.MINUTES);
    }

    public void stopSchedule() {
        if(task != null) {
            task.cancel();
            scheduled = false;
        }
    }

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
            AbstractSQLDriver abstractSQLDriver = driverMap.get(dataInfo.getMysqlDriver());

            if (abstractSQLDriver == null) throw new IllegalStateException("The sql driver " + dataInfo.getMysqlDriver() + " does not exist or is not registed using DatabaseHandler.registerSQLDriver();" );
            if (!(supportedSQL().isAssignableFrom(abstractSQLDriver.getClass()))) throw new IllegalStateException("The sql driver " + abstractSQLDriver.getSqlIdentifierName() + " must be instance of " + supportedSQL().getName() + " or use a database handler that is supported");

            JDBC_SQLDriver sqlDriver = (JDBC_SQLDriver) abstractSQLDriver;

            Class.forName(sqlDriver.getSqlDriverClassName());
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

    public Connection createConnection(DatabaseAuthInfo dataInfo) throws SQLException, ClassNotFoundException {
        try {
            AbstractSQLDriver abstractSQLDriver = DatabaseHandler.getSqlDriver(dataInfo.getMysqlDriver());

            if (abstractSQLDriver == null) throw new IllegalStateException("The sql driver " + dataInfo.getMysqlDriver() + " does not exist or is not registed using DatabaseHandler.registerSQLDriver();" );
            if (!(supportedSQL().isAssignableFrom(abstractSQLDriver.getClass()))) throw new IllegalStateException("The sql driver " + abstractSQLDriver.getSqlIdentifierName() + " must be instance of " + supportedSQL().getName() + " or use a database handler that is supported");
            JDBC_SQLDriver sqlDriver = (JDBC_SQLDriver) abstractSQLDriver;

            Class.forName(sqlDriver.getSqlDriverClassName());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            System.err.println("jdbc driver unavailable!");
            throw e;
        }

        Connection connection = DriverManager.getConnection(
                dataInfo.getUrlToDB(),
                dataInfo.getUsername(),
                dataInfo.getPassword());

        return connection;
    }
}

