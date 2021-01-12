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

    /**
     *
     * @param tableName The name of the table
     * @param rowClass The class of the Row Data
     * @param rowSupplier The supplier should return a new and empty instance of the Row Class.
     *                    This instance should not call {@link RowData#initiateRowData()}
     *                    when constructed. Reflection will be used to
     *                    set the field values accordingly.
     */
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

    /**
     * Internal usage. Sets the rows
     *
     * @param dbRowList
     */
    @Deprecated
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


    /**
     * Retrieves the row based on the primary key.
     *
     * @param key The value of the key
     * @return The row
     */
    public T getRow(String key) {
        return rowDataList.get(key);
    }

    public Map<String, T> getRowDataListCopy() {
        return new HashMap<>(rowDataList);
    }
}
