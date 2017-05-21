package com.lidroid.xutils.db.converter;

import android.database.Cursor;
import android.text.TextUtils;
import com.lidroid.xutils.db.sqlite.ColumnDbType;

public class ShortColumnConverter implements ColumnConverter<Short> {
    public Short getFieldValue(Cursor cursor, int index) {
        return cursor.isNull(index) ? null : Short.valueOf(cursor.getShort(index));
    }

    public Short getFieldValue(String fieldStringValue) {
        if (TextUtils.isEmpty(fieldStringValue)) {
            return null;
        }
        return Short.valueOf(fieldStringValue);
    }

    public Object fieldValue2ColumnValue(Short fieldValue) {
        return fieldValue;
    }

    public ColumnDbType getColumnDbType() {
        return ColumnDbType.INTEGER;
    }
}
