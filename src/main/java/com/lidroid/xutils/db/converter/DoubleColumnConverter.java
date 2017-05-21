package com.lidroid.xutils.db.converter;

import android.database.Cursor;
import android.text.TextUtils;
import com.lidroid.xutils.db.sqlite.ColumnDbType;

public class DoubleColumnConverter implements ColumnConverter<Double> {
    public Double getFieldValue(Cursor cursor, int index) {
        return cursor.isNull(index) ? null : Double.valueOf(cursor.getDouble(index));
    }

    public Double getFieldValue(String fieldStringValue) {
        if (TextUtils.isEmpty(fieldStringValue)) {
            return null;
        }
        return Double.valueOf(fieldStringValue);
    }

    public Object fieldValue2ColumnValue(Double fieldValue) {
        return fieldValue;
    }

    public ColumnDbType getColumnDbType() {
        return ColumnDbType.REAL;
    }
}
