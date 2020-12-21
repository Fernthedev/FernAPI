package com.github.fernthedev.fernapi.universal.mysql;

import lombok.NonNull;

import java.util.Objects;

public abstract class AbstractSQLDriver {

    @NonNull
    private String sqlIdentifierName;

    public AbstractSQLDriver(@NonNull String sqlIdentifierName, @NonNull String sqlDriverClassName, @NonNull String jdbcUrl) {
        this.sqlIdentifierName = sqlIdentifierName;
        this.sqlDriverClassName = sqlDriverClassName;
        this.jdbcUrl = jdbcUrl;
        this.sqlName = sqlIdentifierName;
    }

    private String sqlName;

    public AbstractSQLDriver setSqlName(String sqlName) {
        this.sqlName = sqlName;
        return this;
    }

    @NonNull
    private String sqlDriverClassName;

    @NonNull
    private String jdbcUrl;

    @NonNull
    public String getSqlIdentifierName() {
        return this.sqlIdentifierName;
    }

    @NonNull
    public String getJdbcUrl() {
        return this.jdbcUrl;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AbstractSQLDriver)) return false;
        AbstractSQLDriver that = (AbstractSQLDriver) o;
        return sqlIdentifierName.equals(that.sqlIdentifierName) && sqlName.equals(that.sqlName) && sqlDriverClassName.equals(that.sqlDriverClassName) && jdbcUrl.equals(that.jdbcUrl);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sqlIdentifierName, sqlName, sqlDriverClassName, jdbcUrl);
    }

    public void setSqlIdentifierName(@NonNull String sqlIdentifierName) {
        this.sqlIdentifierName = sqlIdentifierName;
    }

    public void setSqlDriverClassName( @NonNull String sqlDriverClassName) {
        this.sqlDriverClassName = sqlDriverClassName;
    }

    public void setJdbcUrl(@NonNull String jdbcUrl) {
        this.jdbcUrl = jdbcUrl;
    }

    public String toString() {
        return "AbstractSQLDriver(sqlIdentifierName=" + this.getSqlIdentifierName() + ", sqlName=" + this.getSqlName() + ", sqlDriverClassName=" + this.getSqlDriverClassName() + ", jdbcUrl=" + this.getJdbcUrl() + ")";
    }

    public String getSqlName() {
        return this.sqlName;
    }

    @NonNull
    public String getSqlDriverClassName() {
        return this.sqlDriverClassName;
    }
}
