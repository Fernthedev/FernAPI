package com.github.fernthedev.fernapi.universal.data.database;

import lombok.NonNull;

public class RowDataTemplate extends RowData {
    public RowDataTemplate(@NonNull ColumnData columnData, @NonNull ColumnData columnData2, @NonNull ColumnData... columnDataList) {
        super(columnData, columnData2, columnDataList);
    }
}
