package com.github.fernthedev.fernapi.universal.data.database;

import co.aikar.idb.DbRow;
import com.github.fernthedev.fernapi.universal.mysql.DatabaseListener;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

@ToString
public class TableInfo<T extends RowData> {

    @Getter
    private final Class<T> rowClass;
    private final Supplier<@NonNull T> rowSupplier;


    protected Map<String, T> rowDataList = new HashMap<>();

    @Getter
    private final String tableName;

    @Getter
    private final RowDataTemplate<T> rowDataTemplate;

    public TableInfo(String tableName, @NonNull Class<T> rowClass, Supplier<@NonNull T> rowSupplier) {
        this.tableName = tableName;
        this.rowClass = rowClass;
        this.rowDataTemplate = new RowDataTemplate<>(rowClass);
        this.rowSupplier = rowSupplier;
    }


    /**
     * Retrieves the rows of the table
     * and updates the table accordingly.
     *
     * @param databaseListener
     * @return
     */
    public CompletableFuture<TableInfo<T>> loadFromDB(DatabaseListener databaseListener) {
        return databaseListener.getTableRows(tableName).thenApplyAsync(rows -> {
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
     */
    private void addRow(DbRow dbRow) {
        @NonNull T row = rowSupplier.get();

        row.initiateRowData(dbRow);
        rowDataList.put(row.getColumn(rowDataTemplate.getPrimaryKeySQL()).getValue(), row);
    }



    public T getRow(String key) {
        return rowDataList.get(key);
    }

    public Map<String, T> getRowDataListCopy() {
        return new HashMap<>(rowDataList);
    }
}
