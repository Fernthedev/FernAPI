package com.github.fernthedev.fernapi.universal.data.database;

import lombok.Getter;
import org.panteleyev.mysqlapi.annotations.Column;
import org.panteleyev.mysqlapi.annotations.PrimaryKey;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class RowDataTemplate<T extends RowData> {

    private final Map<Field, ColumnData> cachedData = new HashMap<>();
    private final Map<String, ColumnData> cachedDataStr = new HashMap<>();

    @Getter
    private Field primaryKeyField;

    public RowDataTemplate(Class<T> rowData) {
        RowData.validateKeys(rowData);

        Arrays.stream(getClass().getDeclaredFields())
                .filter(field -> field.isAnnotationPresent(Column.class))
                .forEach(field -> {
                    ColumnData columnData;
                    try {

                        columnData = ColumnData.fromField(field, null);
                        addField(field, columnData);

                        if (field.isAnnotationPresent(PrimaryKey.class))
                            primaryKeyField = field;

                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                });
    }

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
        return cachedData.get(field);
    }

    /**
     * Immutable
     * @return
     */
    public Map<String, ColumnData> getDataCopy() {
        return new HashMap<>(cachedDataStr);
    }
}
