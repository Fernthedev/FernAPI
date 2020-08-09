package com.github.fernthedev.fernapi.universal.data.database;

import com.github.fernthedev.fernapi.universal.exceptions.database.DatabaseException;
import com.github.fernthedev.fernapi.universal.mysql.DatabaseListener;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@ToString
public class TableInfo {

    @Getter
    protected List<RowData> rowDataList = new ArrayList<>();

    @Getter
    private String tableName;

    /**
     * Is only used as a row template required for creating the table.
     */
    @Getter
    @Setter
    protected RowDataTemplate rowDataTemplate;

    public TableInfo(String tableName, RowDataTemplate rowDataTemplate) {
        this.tableName = tableName;
        this.rowDataTemplate = rowDataTemplate;
    }



    /**
     * Don't use. It is only used in the creation of tables
     * To add to table, use {@link DatabaseListener#insertIntoTable(TableInfo, RowData)}
     */
    @Deprecated
    public void addTableInfo(RowData rowData) {
        rowDataList.add(rowData);
    }


    /**
     * Don't use. It is only used in the creation of tables
     * To add to table, use {@link DatabaseListener#removeRowIfColumnContainsValue(TableInfo, String, String)}
     */
    @Deprecated
    public void removeTableInfo(RowData rowData) {
        rowDataList.remove(rowData);
    }

    /**
     * Shortcut
     * @param databaseListener
     * @return {@link DatabaseListener#getTable(String, RowDataTemplate)}
     */
    public TableInfo getFromDatabase(DatabaseListener databaseListener) throws DatabaseException {
        rowDataList = databaseListener.getTable(tableName, rowDataTemplate).getRowDataList();
        return this;
    }
}
