package com.github.fernthedev.fernapi.universal.data.database;

import com.github.fernthedev.fernapi.universal.mysql.AbstractSQLDriver;
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



}
