package com.lidroid.xutils.db.converter;

import android.database.Cursor;
import com.lidroid.xutils.db.sqlite.ColumnDbType;

public class StringColumnConverter implements ColumnConverter<String> {
    public String getFieldValue(Cursor cursor, int index) {
        return cursor.isNull(index) ? null : cursor.getString(index);
    }

    public String getFieldValue(String fieldStringValue) {
        return fieldStringValue;
    }

    public Object fieldValue2ColumnValue(String fieldValue) {
        return fieldValue;
    }

    public ColumnDbType getColumnDbType() {
        return ColumnDbType.TEXT;
    }
}
