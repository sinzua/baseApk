package com.lidroid.xutils.db.converter;

import android.database.Cursor;
import android.text.TextUtils;
import com.lidroid.xutils.db.sqlite.ColumnDbType;

public class IntegerColumnConverter implements ColumnConverter<Integer> {
    public Integer getFieldValue(Cursor cursor, int index) {
        return cursor.isNull(index) ? null : Integer.valueOf(cursor.getInt(index));
    }

    public Integer getFieldValue(String fieldStringValue) {
        if (TextUtils.isEmpty(fieldStringValue)) {
            return null;
        }
        return Integer.valueOf(fieldStringValue);
    }

    public Object fieldValue2ColumnValue(Integer fieldValue) {
        return fieldValue;
    }

    public ColumnDbType getColumnDbType() {
        return ColumnDbType.INTEGER;
    }
}
