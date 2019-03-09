package com.github.fernthedev.fernapi.universal.data.database;

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

    public void addTableInfo(RowData rowData) {
        rowDataList.add(rowData);
    }
}
