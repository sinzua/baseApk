package com.lidroid.xutils.db.table;

import android.text.TextUtils;
import com.lidroid.xutils.DbUtils;
import java.util.HashMap;
import java.util.Map.Entry;

public class Table {
    private static final HashMap<String, Table> tableMap = new HashMap();
    private boolean checkedDatabase;
    public final HashMap<String, Column> columnMap;
    public final DbUtils db;
    public final HashMap<String, Finder> finderMap = new HashMap();
    public final Id id;
    public final String tableName;

    private Table(DbUtils db, Class<?> entityType) {
        this.db = db;
        this.tableName = TableUtils.getTableName(entityType);
        this.id = TableUtils.getId(entityType);
        this.columnMap = TableUtils.getColumnMap(entityType);
        for (Column column : this.columnMap.values()) {
            column.setTable(this);
            if (column instanceof Finder) {
                this.finderMap.put(column.getColumnName(), (Finder) column);
            }
        }
    }

    public static synchronized Table get(DbUtils db, Class<?> entityType) {
        Table table;
        synchronized (Table.class) {
            String tableKey = new StringBuilder(String.valueOf(db.getDaoConfig().getDbName())).append("#").append(entityType.getName()).toString();
            table = (Table) tableMap.get(tableKey);
            if (table == null) {
                table = new Table(db, entityType);
                tableMap.put(tableKey, table);
            }
        }
        return table;
    }

    public static synchronized void remove(DbUtils db, Class<?> entityType) {
        synchronized (Table.class) {
            tableMap.remove(new StringBuilder(String.valueOf(db.getDaoConfig().getDbName())).append("#").append(entityType.getName()).toString());
        }
    }

    public static synchronized void remove(DbUtils db, String tableName) {
        synchronized (Table.class) {
            if (tableMap.size() > 0) {
                String key = null;
                for (Entry<String, Table> entry : tableMap.entrySet()) {
                    Table table = (Table) entry.getValue();
                    if (table != null && table.tableName.equals(tableName)) {
                        key = (String) entry.getKey();
                        if (key.startsWith(new StringBuilder(String.valueOf(db.getDaoConfig().getDbName())).append("#").toString())) {
                            break;
                        }
                    }
                }
                if (TextUtils.isEmpty(key)) {
                    tableMap.remove(key);
                }
            }
        }
    }

    public boolean isCheckedDatabase() {
        return this.checkedDatabase;
    }

    public void setCheckedDatabase(boolean checkedDatabase) {
        this.checkedDatabase = checkedDatabase;
    }
}
