package com.lidroid.xutils.db.converter;

import android.database.Cursor;
import android.text.TextUtils;
import com.lidroid.xutils.db.sqlite.ColumnDbType;

public class FloatColumnConverter implements ColumnConverter<Float> {
    public Float getFieldValue(Cursor cursor, int index) {
        return cursor.isNull(index) ? null : Float.valueOf(cursor.getFloat(index));
    }

    public Float getFieldValue(String fieldStringValue) {
        if (TextUtils.isEmpty(fieldStringValue)) {
            return null;
        }
        return Float.valueOf(fieldStringValue);
    }

    public Object fieldValue2ColumnValue(Float fieldValue) {
        return fieldValue;
    }

    public ColumnDbType getColumnDbType() {
        return ColumnDbType.REAL;
    }
}
