package com.lidroid.xutils.db.converter;

import android.database.Cursor;
import android.text.TextUtils;
import com.lidroid.xutils.db.sqlite.ColumnDbType;

public class LongColumnConverter implements ColumnConverter<Long> {
    public Long getFieldValue(Cursor cursor, int index) {
        return cursor.isNull(index) ? null : Long.valueOf(cursor.getLong(index));
    }

    public Long getFieldValue(String fieldStringValue) {
        if (TextUtils.isEmpty(fieldStringValue)) {
            return null;
        }
        return Long.valueOf(fieldStringValue);
    }

    public Object fieldValue2ColumnValue(Long fieldValue) {
        return fieldValue;
    }

    public ColumnDbType getColumnDbType() {
        return ColumnDbType.INTEGER;
    }
}
