package com.github.fernthedev.fernapi.universal.mysql;

import com.github.fernthedev.fernapi.universal.Universal;
import com.github.fernthedev.fernapi.universal.data.database.DatabaseAuthInfo;
import com.github.fernthedev.fernapi.universal.data.database.HikariDatabaseAuthInfo;
import com.github.fernthedev.fernapi.universal.exceptions.database.DatabaseNotConnectedException;
import com.github.fernthedev.fernapi.universal.exceptions.database.DatabaseNotRegisteredException;
import com.zaxxer.hikari.HikariDataSource;
import lombok.Getter;
import lombok.Setter;

import java.sql.Connection;
import java.sql.SQLException;


public class HikariDatabaseHandler extends DatabaseHandler {

    @Setter
    @Getter
    private HikariDataSource hikari;

    private boolean sealed = false;


    public HikariDatabaseHandler(DatabaseAuthInfo databaseAuthInfo) {
        this.hikari = new HikariDataSource();
        setHikariData(databaseAuthInfo);
    }

    @Override
    protected Class<? extends AbstractSQLDriver> supportedSQL() {
        return HikariSQLDriver.class;
    }

    @Override
    public boolean openConnection(DatabaseAuthInfo dataInfo) throws SQLException, ClassNotFoundException {
        DatabaseManager manager = databaseManagerMap.get(dataInfo);

        boolean connected = false;

        if(manager == null) {
            throw new DatabaseNotRegisteredException("The database info was not correctly registered. Call registerDatabase to fix this",new NullPointerException());
        }

        if (!manager.isSetup())
            setHikariData(dataInfo);


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



//            Class.forName(sqlDriver.getDataSourceClass());

//        connection = DriverManager.getConnection(dataInfo.getUrlToDB(), dataInfo.getUsername(), dataInfo.getPassword());

        if (!manager.isSetup()) {
            Universal.getMethods().getLogger().info("Connecting to MySQL now.");
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

    private void setHikariData(DatabaseAuthInfo dataInfo) {

        AbstractSQLDriver abstractSQLDriver = getSqlDriver(dataInfo.getMysqlDriver());

        if (abstractSQLDriver == null)
            throw new IllegalStateException("The sql driver " + dataInfo.getMysqlDriver() + " does not exist or is not registed using DatabaseHandler.registerSQLDriver();");
        if (!(supportedSQL().isAssignableFrom(abstractSQLDriver.getClass())))
            throw new IllegalStateException("The sql driver " + abstractSQLDriver.getSqlIdentifierName() + " must be instance of " + supportedSQL().getName() + " or use a database handler that is supported");

        if (!sealed) {
            HikariSQLDriver sqlDriver = (HikariSQLDriver) abstractSQLDriver;

            if ((dataInfo instanceof HikariDatabaseAuthInfo && (sqlDriver.getPreferJDBC())) || hikari.getDataSource() == null || hikari.getDataSource().getClass().getName().equals(sqlDriver.getDataSourceClass())) {

                boolean useJDBC = false;

                if (dataInfo instanceof HikariDatabaseAuthInfo) {
                    if (sqlDriver.getPreferJDBC()) {
                        hikari.setJdbcUrl(dataInfo.getUrlToDB());
                        useJDBC = true;
                    }
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



            if (dataInfo instanceof HikariDatabaseAuthInfo) {
                HikariDatabaseAuthInfo hikariDatabaseAuthInfo = (HikariDatabaseAuthInfo) dataInfo;

            }


//        hikari.addDataSourceProperty("user", dataInfo.getUsername());
//        hikari.addDataSourceProperty("password", dataInfo.getPassword());
        } else {
            Universal.getLogger().warning("Restart required to apply new SQL driver change. ");
        }
    }

    @Override
    public Connection createConnection(DatabaseAuthInfo dataInfo) throws SQLException {
        if (!sealed) {
            setHikariData(dataInfo);
        }

        sealed = true;

        return hikari.getConnection();
    }

    @Override
    public void closeConnection() {
        super.closeConnection();
        if (hikari != null)
            hikari.close();
    }
}
