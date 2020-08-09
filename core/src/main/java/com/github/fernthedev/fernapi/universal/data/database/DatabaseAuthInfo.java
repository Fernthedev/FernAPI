package com.github.fernthedev.fernapi.universal.data.database;

import com.github.fernthedev.fernapi.universal.mysql.AbstractSQLDriver;
import com.github.fernthedev.fernapi.universal.mysql.HikariDatabaseHandler;
import com.github.fernthedev.fernapi.universal.mysql.HikariSQLDriver;
import lombok.*;

@RequiredArgsConstructor
@Getter
@Data
@ToString
public class DatabaseAuthInfo {
    //DataBase vars.
    @NonNull
    protected String username; //Enter in your db username

    @NonNull
    protected String password; //Enter your password for the db

    @NonNull
    protected String port;

    @NonNull
    protected String urlHost;

    @NonNull
    protected String database;

    @Setter
    protected boolean cachePrepStmts = true;

    @Setter
    protected int prepStmtCacheSize = 250;

    @Setter
    protected int prepStmtCacheSqlLimit = 2048;

    @Setter
    protected boolean useServerPrepStmts = true;

    /**
     * Set if necessary
     */
    @Setter
    @NonNull
    protected String mysqlDriver = HikariSQLDriver.MARIADB_DRIVER.getSqlIdentifierName();

    public DatabaseAuthInfo(@NonNull String username, @NonNull String password, @NonNull String port, @NonNull String urlHost, @NonNull String database, @NonNull AbstractSQLDriver mysqlDriver) {
        this.username = username;
        this.password = password;
        this.port = port;
        this.urlHost = urlHost;
        this.database = database;
        this.mysqlDriver = mysqlDriver.getSqlIdentifierName();
    }

    private DatabaseAuthInfo() {}


    public String getUrlToDB() {
        AbstractSQLDriver sqlDriver = HikariDatabaseHandler.getSqlDriver(mysqlDriver);
        if (sqlDriver == null) throw new IllegalStateException("Sql Driver " + mysqlDriver + " could not be found");

        String jdbcUrl = sqlDriver.getJdbcUrl();

        if (jdbcUrl == null) throw new IllegalStateException("JDBC Url for " + sqlDriver.getSqlIdentifierName() + " cannot be found");

//        Universal.debug("This DB:" + toString());

        return jdbcUrl
                .replace("%host%", urlHost)
                .replace("%port%", port)
                .replace("%database%", database)
                .replace("%sql%", sqlDriver.getSqlName());
    }



}
