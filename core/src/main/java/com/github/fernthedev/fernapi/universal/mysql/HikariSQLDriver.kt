package com.github.fernthedev.fernapi.universal.mysql

import org.mariadb.jdbc.Driver
import org.mariadb.jdbc.MariaDbDataSource
import javax.sql.DataSource

class HikariSQLDriver : AbstractSQLDriver {

    @Transient
    var dataSourceClass: String
        private set;

    @Transient
    var preferJDBC = false
        private set;

    fun setPreferJDBC(preferJDBC: Boolean): HikariSQLDriver {
        this.preferJDBC = preferJDBC
        return this
    }

    constructor(sql: String, sqlDriverClassName: String, jdbcUrl: String, dataSourceClass: String) : super(sql, sqlDriverClassName, jdbcUrl) {
        this.dataSourceClass = dataSourceClass
    }

    constructor(sql: String, sqlDriverClassName: String, jdbcUrl: String, dataSourceClass: DataSource) : super(sql, sqlDriverClassName, jdbcUrl) {
        this.dataSourceClass = dataSourceClass.javaClass.name
    }

    companion object {
        @JvmField
        val MARIADB_DRIVER = HikariSQLDriver("mysql", Driver::class.java.name,"jdbc:%sql%://%host%:%port%/%database%", dataSourceClass = MariaDbDataSource()).setPreferJDBC(true).setSqlName("mariadb") as HikariSQLDriver
        @JvmField
        val MYSQL_DRIVER = HikariSQLDriver("mysql", "com.mysql.jdbc.Driver","jdbc:%sql%://%host%:%port%/%database%", dataSourceClass = "com.mysql.jdbc.jdbc2.optional.MysqlDataSource()").setPreferJDBC(true).setSqlName("mysql") as HikariSQLDriver
    }
}