package com.github.fernthedev.fernapi.universal.data.database;

import com.github.fernthedev.fernapi.universal.exceptions.database.DatabaseColumNotExistException;
import lombok.Getter;
import lombok.NonNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Contains the full row data of columns.
 * Each column is one data.
 */
public class RowData {

    @Getter
    private List<ColumnData> columnDataList = new ArrayList<>();

    public RowData(@NonNull ColumnData columnData, @NonNull ColumnData... columnDataList) {
        this.columnDataList.add(columnData);
        this.columnDataList.addAll(Arrays.asList(columnDataList));
    }

    public RowData(@NonNull ColumnData object) {
        this.columnDataList.add(object);
    }

    public void addData(ColumnData data) {
        columnDataList.add(data);
    }

    /**
     * Retrieves the value of the designated column in the current row
     * @param columnName Case-sensitive name of column
     * @return The column data.
     */
    public ColumnData getColumn(String columnName) throws DatabaseColumNotExistException {
        for (ColumnData columnData : getColumnDataList()) {
            if(columnData.getColumnName().equals(columnName)) {
                return columnData;
            }
        }
        throw new DatabaseColumNotExistException("The column does not exist. The name is case-sensitive just in case.");
    }
}
