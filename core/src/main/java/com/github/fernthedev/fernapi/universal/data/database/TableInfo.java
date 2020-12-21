package com.github.fernthedev.fernapi.universal.data.database;

import co.aikar.idb.DbRow;
import com.github.fernthedev.fernapi.universal.exceptions.database.DatabaseException;
import com.github.fernthedev.fernapi.universal.mysql.DatabaseListener;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@ToString
public class TableInfo<T extends RowData> {

    @Getter
    private final Class<T> rowClass;


    protected Map<String, T> rowDataList = new HashMap<>();

    @Getter
    private final String tableName;

    @Getter
    private final RowDataTemplate<T> rowDataTemplate;

    public TableInfo(String tableName, @NonNull Class<T> rowClass) {
        this.tableName = tableName;
        this.rowClass = rowClass;
        this.rowDataTemplate = new RowDataTemplate<>(rowClass);
    }


    /**
     * Shortcut
     * @param databaseListener
     * @return {@link DatabaseListener#getTable(String, Class)}
     */
    public CompletableFuture<TableInfo<T>> loadFromDB(DatabaseListener databaseListener) throws DatabaseException {
        return databaseListener.getTableRows(tableName, rowClass).thenApplyAsync(rows -> {
            setRows(rows);

            return this;
        });
    }

    public void setRows(List<DbRow> dbRowList) {
        rowDataList.clear();
        dbRowList.forEach(this::addRow);
    }

    /**
     * Don't use. It is only used in the creation of tables
     * To add to table, use {@link DatabaseListener#insertIntoTable(TableInfo, RowData)}
     *
     * @deprecated Internal
     */
    @Deprecated
    public void addRow(DbRow dbRow) {
        try {
            T row = rowClass.getConstructor(DbRow.class).newInstance(dbRow);
            rowDataList.put(rowDataTemplate.getPrimaryKeySQL(), row);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new IllegalStateException(e);
        }
    }

    /**
     * Don't use. It is only used in the creation of tables
     * To add to table, use {@link DatabaseListener#insertIntoTable(TableInfo, RowData)}
     * @deprecated Internal
     */
    public void addRow(T rowData) {
        rowDataList.put(rowDataTemplate.getPrimaryKeySQL(), rowData);
    }

    /**
     * Don't use. It is only used in the creation of tables
     * To add to table, use {@link DatabaseListener#removeRowIfColumnContainsValue(TableInfo, String, String)}
     * @deprecated Internal
     */
    @Deprecated
    public void removeTableInfo(RowData rowData) {
        rowDataList.remove(rowData);
    }

    public T getRow(String key) {
        return rowDataList.get(key);
    }

    public Map<String, T> getRowDataListCopy() {
        return new HashMap<>(rowDataList);
    }
}
