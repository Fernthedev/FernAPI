package com.github.fernthedev.fernapi.universal.data.database;

import com.github.fernthedev.fernapi.universal.mysql.SQLDriver;
import lombok.*;

@RequiredArgsConstructor
@Getter
@Data
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
    protected SQLDriver mysqlDatabaseType = SQLDriver.MARIADB_DRIVER;


    private DatabaseAuthInfo() {}


    public String getUrlToDB() {
        return "jdbc:%sql%://%host%:%port%/%database%".replaceAll("%host%", urlHost).replaceAll("%port%", port).replaceAll("%database%", database).replaceAll("%sql%", mysqlDatabaseType.getSql());
    }



}
