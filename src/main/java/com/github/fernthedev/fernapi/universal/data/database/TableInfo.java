package com.github.fernthedev.fernapi.universal.data.database;

import com.github.fernthedev.fernapi.universal.DatabaseManager;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

public class TableInfo {

    @Getter
    protected List<RowData> rowDataList = new ArrayList<>();

    @Getter
    private String tableName;

    public TableInfo(String tableName) {
        this.tableName = tableName;
    }

    @Deprecated
    /**
     * Don't use. It is only for creating tables
     */
    public void addTableInfo(RowData rowData) {
        rowDataList.add(rowData);
    }

    @Deprecated
    /**
     * Don't use. It is only for creating tables
     */
    public void removeTableInfo(RowData rowData) {
        rowDataList.remove(rowData);
    }

    /**
     * Shortcut
     * @param databaseManager
     * @return {@link DatabaseManager#getTable(String)}
     */
    public TableInfo getFromDatabase(DatabaseManager databaseManager) {
        rowDataList = databaseManager.getTable(tableName).getRowDataList();
        return this;
    }
}
