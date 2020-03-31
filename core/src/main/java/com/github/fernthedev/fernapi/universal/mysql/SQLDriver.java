package com.github.fernthedev.fernapi.universal.mysql;

import lombok.*;

@Data
@Setter
@ToString
@AllArgsConstructor
public class SQLDriver {

    @NonNull
    private String sql;

    @NonNull
    private String sqlDriver;

    public static SQLDriver MYSQL_DRIVER = new SQLDriver("mysql", "com.mysql.jdbc.Driver");
    public static SQLDriver MARIADB_DRIVER = new SQLDriver("mariadb", org.mariadb.jdbc.Driver.class.getName());
}


