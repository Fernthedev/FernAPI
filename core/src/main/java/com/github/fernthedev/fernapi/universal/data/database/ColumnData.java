package com.github.fernthedev.fernapi.universal.data.database;

import com.google.errorprone.annotations.Immutable;
import lombok.*;
import org.panteleyev.mysqlapi.annotations.Column;
import org.panteleyev.mysqlapi.annotations.PrimaryKey;

import javax.annotation.Nullable;
import java.io.Serializable;
import java.lang.reflect.Field;

import static org.panteleyev.mysqlapi.DataTypes.*;

/**
 * The column is a piece of data. Immutable
 */
@ToString
@AllArgsConstructor
@Builder(toBuilder = true)
@Immutable
public class ColumnData implements Serializable {

    @Getter
    @NonNull
    private final String columnName;

    /**
     * Will return null if it is SQL NULL
     */
    @Getter
    @Nullable
    private final String value;

    @Getter
    private final String type;

    @Getter
    private final int length;

    @Getter
    private final boolean nullable;

    @Getter
    private final boolean autoIncrement;

    @Getter
    private final boolean primaryKey;


    public static ColumnData fromField(Field field, String value) throws IllegalAccessException {
        if (!field.isAnnotationPresent(Column.class))
            throw new IllegalArgumentException("Field " + field.toString() + " does not contain " + Column.class.toString() + " annotation");

        Column column = field.getAnnotation(Column.class);

        String columnName = column.value();
        Class<?> type = field.getType();
        String mySQLType;

        int length = column.length();

        boolean nullable = column.nullable();
        boolean primaryKey = field.isAnnotationPresent(PrimaryKey.class);
        boolean autoIncrement = primaryKey && field.getAnnotation(PrimaryKey.class).isAutoIncrement();

        String typeName = type.isEnum() ?
                TYPE_ENUM : type.getTypeName();

        // Code snatched from Petr Panteleyev's MySQL annotation API
        switch (typeName) {
            case TYPE_STRING:
                if (column.isJson()) {
                    mySQLType = "JSON";
                } else {
                    mySQLType = "VARCHAR("
                            + column.length()
                            + ")";
                }
                break;
            case TYPE_ENUM:
                mySQLType = "VARCHAR("
                        + column.length()
                        + ")";
                break;
            case TYPE_BOOL:
            case TYPE_BOOLEAN:
                mySQLType = "BOOLEAN";
                break;
            case TYPE_INTEGER:
            case TYPE_INT:
                mySQLType = "INTEGER";
                break;
            case TYPE_LONG:
            case TYPE_LONG_PRIM:
            case TYPE_DATE:
            case TYPE_LOCAL_DATE:
                mySQLType = "BIGINT";
                break;
            case TYPE_BIG_DECIMAL:
                mySQLType = "DECIMAL(" +
                        column.precision() +
                        "," +
                        column.scale() +
                        ")";
                break;
            case TYPE_BYTE_ARRAY:
                mySQLType = "VARBINARY(" +
                        column.length() +
                        ")";
                break;
            case TYPE_UUID:
                if (column.storeUuidAsBinary()) {
                    mySQLType = "BINARY(16)";
                } else {
                    mySQLType = "VARCHAR(36)";
                }
                break;
            default:
                throw new IllegalStateException(BAD_FIELD_TYPE + typeName);
        }




        return new ColumnData(columnName, value, mySQLType, length, nullable, autoIncrement, primaryKey);
    }


}
