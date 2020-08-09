package com.github.fernthedev.fernapi.universal.mysql;

import com.github.fernthedev.fernapi.universal.Universal;
import com.github.fernthedev.fernapi.universal.data.ScheduleTaskWrapper;
import com.github.fernthedev.fernapi.universal.data.database.DatabaseAuthInfo;
import com.github.fernthedev.fernapi.universal.exceptions.database.DatabaseNotConnectedException;
import com.github.fernthedev.fernapi.universal.exceptions.database.DatabaseNotRegisteredException;
import com.zaxxer.hikari.HikariDataSource;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;


public class HikariDatabaseHandler {

    public static final HikariDatabaseHandler instance = new HikariDatabaseHandler();
    private static final Map<String, AbstractSQLDriver> driverMap = new HashMap<>();
    protected boolean scheduled;

    /**
     * The rate of minutes it takes to reconnect to the sql database.
     */
    @Setter
    @Getter
    protected int scheduleTime = 15;
    protected Map<DatabaseAuthInfo, DatabaseListener> databaseManagerMap = new HashMap<>();
    private ScheduleTaskWrapper<?, ?> task;

    @Setter
    @Getter
    private HikariDataSource hikari = new HikariDataSource();
    private boolean sealed = false;

    public HikariDatabaseHandler() {
        this.hikari = new HikariDataSource();
    }

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

                for (DatabaseListener databaseListener : databaseManagerMap.values()) {
                    databaseListener.getConnection().createStatement();
                }
            } catch (ClassNotFoundException | SQLException e) {
                e.printStackTrace();
            }
        };
    }

    protected void setupSchedule() {
        if(task != null) {
            task.cancel();
            scheduled = false;
        }

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
        DatabaseListener listener = databaseManagerMap.get(dataInfo);

        boolean connected = false;

        if(listener == null) {
            throw new DatabaseNotRegisteredException("The database info was not correctly registered. Call registerDatabase to fix this",new NullPointerException());
        }

        if (!listener.isSetup())
            setHikariData(dataInfo);


        Connection connection;
        try {
            connection = listener.getConnection();

            if (connection != null && !connection.isClosed()) {
                return true;
            }
        } catch (DatabaseNotConnectedException ignored) {}

        if (!listener.isSetup()) {
            Universal.getMethods().getLogger().info("Connecting to MySQL now.");
            listener.setFirstConnect(true);
            listener.createConnection(dataInfo);

            try {
                connected = listener.isConnected();
            } catch (DatabaseNotConnectedException ignored) {}

            listener.onConnectAttempt(connected);
            listener.setSetup(true);


            setupSchedule();
        }

        return connected;
    }

    /**]
     * Registers the databaseListener for calling events.
     * @param databaseListener The manager
     */
    public void registerDatabase(@NonNull DatabaseAuthInfo databaseAuthInfo, @NonNull DatabaseListener databaseListener) {
        if (!databaseListener.isSetup()) {
            Universal.getMethods().getLogger().info("Setting database connection");
        }

        databaseManagerMap.put(databaseAuthInfo, databaseListener);
    }

    protected Class<? extends AbstractSQLDriver> supportedSQL() {
        return HikariSQLDriver.class;
    }



    private void setHikariData(DatabaseAuthInfo dataInfo) {

        AbstractSQLDriver abstractSQLDriver = getSqlDriver(dataInfo.getMysqlDriver());

        if (abstractSQLDriver == null)
            throw new IllegalStateException("The sql driver " + dataInfo.getMysqlDriver() + " does not exist or is not registed using DatabaseHandler.registerSQLDriver();");
        if (!(supportedSQL().isAssignableFrom(abstractSQLDriver.getClass())))
            throw new IllegalStateException("The sql driver " + abstractSQLDriver.getSqlIdentifierName() + " must be instance of " + supportedSQL().getName() + " or use a database handler that is supported");

        if (!sealed) {
            HikariSQLDriver sqlDriver = (HikariSQLDriver) abstractSQLDriver;

            if (sqlDriver.getPreferJDBC() || hikari.getDataSource() == null || hikari.getDataSource().getClass().getName().equals(sqlDriver.getDataSourceClass())) {

                boolean useJDBC = false;

                if (sqlDriver.getPreferJDBC()) {
                    hikari.setJdbcUrl(dataInfo.getUrlToDB());
                    useJDBC = true;
                }

                if (!useJDBC) {
//                    hikari.setDataSource(sqlDriver.getDataSourceClass());
                    hikari.setDataSourceClassName(sqlDriver.getClass().getName());
                }


            }


            hikari.setPassword(dataInfo.getPassword());
            hikari.setUsername(dataInfo.getUsername());

//        hikari.setDataSourceClassName(sqlDriver.getDataSourceClass());
            hikari.addDataSourceProperty("serverName", dataInfo.getUrlHost());
            hikari.addDataSourceProperty("port", dataInfo.getPort());
            hikari.addDataSourceProperty("databaseName", dataInfo.getDatabase());

            Universal.debug("Database name: " + hikari.getDataSourceProperties().getProperty("databaseName"));

            hikari.addDataSourceProperty("cachePrepStmts", dataInfo.isCachePrepStmts());
            hikari.addDataSourceProperty("prepStmtCacheSize", dataInfo.getPrepStmtCacheSize());
            hikari.addDataSourceProperty("prepStmtCacheSqlLimit", dataInfo.getPrepStmtCacheSqlLimit());
            hikari.addDataSourceProperty("useServerPrepStmts", dataInfo.isUseServerPrepStmts());

            hikari.addDataSourceProperty("useLocalSessionState", true);
            hikari.addDataSourceProperty("rewriteBatchedStatements", true);
            hikari.addDataSourceProperty("cacheResultSetMetadata", true);
            hikari.addDataSourceProperty("cacheServerConfiguration", true);
            hikari.addDataSourceProperty("elideSetAutoCommits", true);
            hikari.addDataSourceProperty("maintainTimeStats", false);


//        hikari.addDataSourceProperty("user", dataInfo.getUsername());
//        hikari.addDataSourceProperty("password", dataInfo.getPassword());
        } else {
            Universal.getLogger().warning("Restart required to apply new SQL driver change. ");
        }
    }

    public Connection createConnection(DatabaseAuthInfo dataInfo) throws SQLException {
        if (!sealed) {
            setHikariData(dataInfo);
        }

        sealed = true;

        return hikari.getConnection();
    }

    public void closeConnection() {
        for(DatabaseListener databaseListener : databaseManagerMap.values()) {
            Connection connection = null;
            try {
                connection = databaseListener.getConnection();
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
        if (hikari != null)
            hikari.close();
    }
}
