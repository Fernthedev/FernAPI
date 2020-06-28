package com.github.fernthedev.fernapi.universal.data.database;

import com.github.fernthedev.fernapi.universal.mysql.AbstractSQLDriver;
import lombok.Getter;
import lombok.NonNull;

@Getter
public class HikariDatabaseAuthInfo extends DatabaseAuthInfo {


    public HikariDatabaseAuthInfo(@NonNull String username, @NonNull String password, @NonNull String port, @NonNull String urlHost, @NonNull String database) {
        super(username, password, port, urlHost, database);
    }

    public HikariDatabaseAuthInfo(@NonNull String username, @NonNull String password, @NonNull String port, @NonNull String urlHost, @NonNull String database, @NonNull AbstractSQLDriver mysqlDriver) {
        super(username, password, port, urlHost, database, mysqlDriver);
    }
}
