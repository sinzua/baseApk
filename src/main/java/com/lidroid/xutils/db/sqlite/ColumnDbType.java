package com.lidroid.xutils.db.sqlite;

public enum ColumnDbType {
    INTEGER("INTEGER"),
    REAL("REAL"),
    TEXT("TEXT"),
    BLOB("BLOB");
    
    private String value;

    private ColumnDbType(String value) {
        this.value = value;
    }

    public String toString() {
        return this.value;
    }
}
