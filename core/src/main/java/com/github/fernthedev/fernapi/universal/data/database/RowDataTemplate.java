package com.github.fernthedev.fernapi.universal.data.database;

import lombok.Getter;
import lombok.Synchronized;
import org.panteleyev.mysqlapi.annotations.Column;
import org.panteleyev.mysqlapi.annotations.PrimaryKey;

import java.lang.reflect.Field;
import java.util.LinkedHashMap;
import java.util.Map;

public class RowDataTemplate<T extends RowData> {

    private final Map<Field, ColumnData> cachedData = new LinkedHashMap<>();
    private final Map<String, ColumnData> cachedDataStr = new LinkedHashMap<>();

    @Getter
    private Field primaryKeyField;

    public RowDataTemplate(Class<T> rowData) {
        RowData.validateKeys(rowData);

        Field[] declaredFields = rowData.getDeclaredFields();

        for (Field field : declaredFields) {
            if (!field.isAnnotationPresent(Column.class)) {
                continue;
            }

            ColumnData columnData;
            try {

                columnData = ColumnData.fromField(field, null);
                addField(field, columnData);

                if (field.isAnnotationPresent(PrimaryKey.class))
                    primaryKeyField = field;

            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }

    }

    @Synchronized
    private void addField(Field field, ColumnData columnData) {
        cachedData.put(field, columnData);
        cachedDataStr.put(field.getAnnotation(Column.class).value(), columnData);
    }

    public String getPrimaryKeySQL() {
        return primaryKeyField.getAnnotation(Column.class).value();
    }

    public ColumnData getColumn(String sqlName) {
        return cachedDataStr.get(sqlName);
    }

    public ColumnData getColumn(Field field) {
        return  cachedData.get(field);
    }

    /**
     * Immutable
     * @return
     */
    public Map<String, ColumnData> getDataStrCopy() {
        return new LinkedHashMap<>(cachedDataStr);
    }

    /**
     * Immutable
     * @return
     */
    public Map<Field, ColumnData> getDataCopy() {
        return new LinkedHashMap<>(cachedData);
    }
}
