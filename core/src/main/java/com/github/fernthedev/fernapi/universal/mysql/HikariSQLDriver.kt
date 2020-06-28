package com.github.fernthedev.fernapi.universal.mysql

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
        val MARIADB_DRIVER = HikariSQLDriver(sql = "mariadb_hikari", sqlDriverClassName = JDBC_SQLDriver.MARIADB_DRIVER.sqlDriverClassName, jdbcUrl = JDBC_SQLDriver.MARIADB_DRIVER.jdbcUrl, dataSourceClass = MariaDbDataSource()).setPreferJDBC(true).setSqlName(JDBC_SQLDriver.MYSQL_DRIVER.sqlName) as HikariSQLDriver
        @JvmField
        val MYSQL_DRIVER = HikariSQLDriver(sql ="mysql_hikari", sqlDriverClassName = JDBC_SQLDriver.MYSQL_DRIVER.sqlDriverClassName,jdbcUrl =  JDBC_SQLDriver.MYSQL_DRIVER.jdbcUrl, dataSourceClass = "com.mysql.jdbc.jdbc2.optional.MysqlDataSource()").setPreferJDBC(true).setSqlName(JDBC_SQLDriver.MYSQL_DRIVER.sqlName) as HikariSQLDriver
    }
}