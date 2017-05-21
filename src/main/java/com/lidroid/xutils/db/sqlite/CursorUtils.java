package com.lidroid.xutils.db.sqlite;

import android.database.Cursor;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.db.table.Column;
import com.lidroid.xutils.db.table.DbModel;
import com.lidroid.xutils.db.table.Finder;
import com.lidroid.xutils.db.table.Id;
import com.lidroid.xutils.db.table.Table;
import com.lidroid.xutils.util.LogUtils;
import java.util.concurrent.ConcurrentHashMap;

public class CursorUtils {

    private static class EntityTempCache {
        private static final ConcurrentHashMap<String, Object> cache = new ConcurrentHashMap();
        private static long seq = 0;

        private EntityTempCache() {
        }

        public static <T> void put(Class<T> entityType, Object idValue, Object entity) {
            cache.put(entityType.getName() + "#" + idValue, entity);
        }

        public static <T> T get(Class<T> entityType, Object idValue) {
            return cache.get(entityType.getName() + "#" + idValue);
        }

        public static void setSeq(long seq) {
            if (seq != seq) {
                cache.clear();
                seq = seq;
            }
        }
    }

    public static class FindCacheSequence {
        private static final String FINDER_LAZY_LOADER_CLASS_NAME = FinderLazyLoader.class.getName();
        private static final String FOREIGN_LAZY_LOADER_CLASS_NAME = ForeignLazyLoader.class.getName();
        private static long seq = 0;

        private FindCacheSequence() {
        }

        public static long getSeq() {
            String findMethodCaller = Thread.currentThread().getStackTrace()[4].getClassName();
            if (!(findMethodCaller.equals(FOREIGN_LAZY_LOADER_CLASS_NAME) || findMethodCaller.equals(FINDER_LAZY_LOADER_CLASS_NAME))) {
                seq++;
            }
            return seq;
        }
    }

    public static <T> T getEntity(DbUtils db, Cursor cursor, Class<T> entityType, long findCacheSequence) {
        if (db == null || cursor == null) {
            return null;
        }
        EntityTempCache.setSeq(findCacheSequence);
        try {
            Table table = Table.get(db, entityType);
            Id id = table.id;
            String idColumnName = id.getColumnName();
            int idIndex = id.getIndex();
            if (idIndex < 0) {
                idIndex = cursor.getColumnIndex(idColumnName);
            }
            Object idValue = id.getColumnConverter().getFieldValue(cursor, idIndex);
            T entity = EntityTempCache.get(entityType, idValue);
            if (entity != null) {
                return entity;
            }
            entity = entityType.newInstance();
            id.setValue2Entity(entity, cursor, idIndex);
            EntityTempCache.put(entityType, idValue, entity);
            int columnCount = cursor.getColumnCount();
            for (int i = 0; i < columnCount; i++) {
                Column column = (Column) table.columnMap.get(cursor.getColumnName(i));
                if (column != null) {
                    column.setValue2Entity(entity, cursor, i);
                }
            }
            for (Finder finder : table.finderMap.values()) {
                finder.setValue2Entity(entity, null, 0);
            }
            return entity;
        } catch (Throwable e) {
            LogUtils.e(e.getMessage(), e);
            return null;
        }
    }

    public static DbModel getDbModel(Cursor cursor) {
        DbModel result = null;
        if (cursor != null) {
            result = new DbModel();
            int columnCount = cursor.getColumnCount();
            for (int i = 0; i < columnCount; i++) {
                result.add(cursor.getColumnName(i), cursor.getString(i));
            }
        }
        return result;
    }
}
