package com.lidroid.xutils.db.sqlite;

import com.lidroid.xutils.db.table.TableUtils;
import java.util.ArrayList;
import java.util.List;

public class Selector {
    protected Class<?> entityType;
    protected int limit = 0;
    protected int offset = 0;
    protected List<OrderBy> orderByList;
    protected String tableName;
    protected WhereBuilder whereBuilder;

    protected class OrderBy {
        private String columnName;
        private boolean desc;

        public OrderBy(String columnName) {
            this.columnName = columnName;
        }

        public OrderBy(String columnName, boolean desc) {
            this.columnName = columnName;
            this.desc = desc;
        }

        public String toString() {
            return this.columnName + (this.desc ? " DESC" : " ASC");
        }
    }

    private Selector(Class<?> entityType) {
        this.entityType = entityType;
        this.tableName = TableUtils.getTableName(entityType);
    }

    public static Selector from(Class<?> entityType) {
        return new Selector(entityType);
    }

    public Selector where(WhereBuilder whereBuilder) {
        this.whereBuilder = whereBuilder;
        return this;
    }

    public Selector where(String columnName, String op, Object value) {
        this.whereBuilder = WhereBuilder.b(columnName, op, value);
        return this;
    }

    public Selector and(String columnName, String op, Object value) {
        this.whereBuilder.and(columnName, op, value);
        return this;
    }

    public Selector and(WhereBuilder where) {
        this.whereBuilder.expr("AND (" + where.toString() + ")");
        return this;
    }

    public Selector or(String columnName, String op, Object value) {
        this.whereBuilder.or(columnName, op, value);
        return this;
    }

    public Selector or(WhereBuilder where) {
        this.whereBuilder.expr("OR (" + where.toString() + ")");
        return this;
    }

    public Selector expr(String expr) {
        if (this.whereBuilder == null) {
            this.whereBuilder = WhereBuilder.b();
        }
        this.whereBuilder.expr(expr);
        return this;
    }

    public Selector expr(String columnName, String op, Object value) {
        if (this.whereBuilder == null) {
            this.whereBuilder = WhereBuilder.b();
        }
        this.whereBuilder.expr(columnName, op, value);
        return this;
    }

    public DbModelSelector groupBy(String columnName) {
        return new DbModelSelector(this, columnName);
    }

    public DbModelSelector select(String... columnExpressions) {
        return new DbModelSelector(this, columnExpressions);
    }

    public Selector orderBy(String columnName) {
        if (this.orderByList == null) {
            this.orderByList = new ArrayList(2);
        }
        this.orderByList.add(new OrderBy(columnName));
        return this;
    }

    public Selector orderBy(String columnName, boolean desc) {
        if (this.orderByList == null) {
            this.orderByList = new ArrayList(2);
        }
        this.orderByList.add(new OrderBy(columnName, desc));
        return this;
    }

    public Selector limit(int limit) {
        this.limit = limit;
        return this;
    }

    public Selector offset(int offset) {
        this.offset = offset;
        return this;
    }

    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append("SELECT ");
        result.append("*");
        result.append(" FROM ").append(this.tableName);
        if (this.whereBuilder != null && this.whereBuilder.getWhereItemSize() > 0) {
            result.append(" WHERE ").append(this.whereBuilder.toString());
        }
        if (this.orderByList != null) {
            for (int i = 0; i < this.orderByList.size(); i++) {
                result.append(" ORDER BY ").append(((OrderBy) this.orderByList.get(i)).toString());
            }
        }
        if (this.limit > 0) {
            result.append(" LIMIT ").append(this.limit);
            result.append(" OFFSET ").append(this.offset);
        }
        return result.toString();
    }

    public Class<?> getEntityType() {
        return this.entityType;
    }
}
