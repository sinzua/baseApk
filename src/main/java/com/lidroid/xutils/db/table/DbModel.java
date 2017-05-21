package com.lidroid.xutils.db.table;

import android.text.TextUtils;
import java.util.Date;
import java.util.HashMap;

public class DbModel {
    private HashMap<String, String> dataMap = new HashMap();

    public String getString(String columnName) {
        return (String) this.dataMap.get(columnName);
    }

    public int getInt(String columnName) {
        return Integer.valueOf((String) this.dataMap.get(columnName)).intValue();
    }

    public boolean getBoolean(String columnName) {
        String value = (String) this.dataMap.get(columnName);
        if (value != null) {
            return value.length() == 1 ? "1".equals(value) : Boolean.valueOf(value).booleanValue();
        } else {
            return false;
        }
    }

    public double getDouble(String columnName) {
        return Double.valueOf((String) this.dataMap.get(columnName)).doubleValue();
    }

    public float getFloat(String columnName) {
        return Float.valueOf((String) this.dataMap.get(columnName)).floatValue();
    }

    public long getLong(String columnName) {
        return Long.valueOf((String) this.dataMap.get(columnName)).longValue();
    }

    public Date getDate(String columnName) {
        return new Date(Long.valueOf((String) this.dataMap.get(columnName)).longValue());
    }

    public java.sql.Date getSqlDate(String columnName) {
        return new java.sql.Date(Long.valueOf((String) this.dataMap.get(columnName)).longValue());
    }

    public void add(String columnName, String valueStr) {
        this.dataMap.put(columnName, valueStr);
    }

    public HashMap<String, String> getDataMap() {
        return this.dataMap;
    }

    public boolean isEmpty(String columnName) {
        return TextUtils.isEmpty((CharSequence) this.dataMap.get(columnName));
    }
}
