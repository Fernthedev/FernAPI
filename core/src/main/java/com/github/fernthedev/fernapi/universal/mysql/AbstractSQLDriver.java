package com.github.fernthedev.fernapi.universal.mysql;

import lombok.NonNull;

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

    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof AbstractSQLDriver)) return false;
        final AbstractSQLDriver other = (AbstractSQLDriver) o;
        if (!other.canEqual((Object) this)) return false;
        final Object this$sqlIdentifierName = this.getSqlIdentifierName();
        final Object other$sqlIdentifierName = other.getSqlIdentifierName();
        if (this$sqlIdentifierName == null ? other$sqlIdentifierName != null : !this$sqlIdentifierName.equals(other$sqlIdentifierName))
            return false;
        final Object this$sqlName = this.getSqlName();
        final Object other$sqlName = other.getSqlName();
        if (this$sqlName == null ? other$sqlName != null : !this$sqlName.equals(other$sqlName)) return false;
        final Object this$sqlDriverClassName = this.getSqlDriverClassName();
        final Object other$sqlDriverClassName = other.getSqlDriverClassName();
        if (this$sqlDriverClassName == null ? other$sqlDriverClassName != null : !this$sqlDriverClassName.equals(other$sqlDriverClassName))
            return false;
        final Object this$jdbcUrl = this.getJdbcUrl();
        final Object other$jdbcUrl = other.getJdbcUrl();
        if (this$jdbcUrl == null ? other$jdbcUrl != null : !this$jdbcUrl.equals(other$jdbcUrl)) return false;
        return true;
    }

    protected boolean canEqual(final Object other) {
        return other instanceof AbstractSQLDriver;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $sqlIdentifierName = this.getSqlIdentifierName();
        result = result * PRIME + ($sqlIdentifierName == null ? 43 : $sqlIdentifierName.hashCode());
        final Object $sqlName = this.getSqlName();
        result = result * PRIME + ($sqlName == null ? 43 : $sqlName.hashCode());
        final Object $sqlDriverClassName = this.getSqlDriverClassName();
        result = result * PRIME + ($sqlDriverClassName == null ? 43 : $sqlDriverClassName.hashCode());
        final Object $jdbcUrl = this.getJdbcUrl();
        result = result * PRIME + ($jdbcUrl == null ? 43 : $jdbcUrl.hashCode());
        return result;
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
