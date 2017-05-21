package com.lidroid.xutils;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;
import com.lidroid.xutils.db.sqlite.CursorUtils;
import com.lidroid.xutils.db.sqlite.CursorUtils.FindCacheSequence;
import com.lidroid.xutils.db.sqlite.DbModelSelector;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.db.sqlite.SqlInfo;
import com.lidroid.xutils.db.sqlite.SqlInfoBuilder;
import com.lidroid.xutils.db.sqlite.WhereBuilder;
import com.lidroid.xutils.db.table.DbModel;
import com.lidroid.xutils.db.table.Id;
import com.lidroid.xutils.db.table.Table;
import com.lidroid.xutils.db.table.TableUtils;
import com.lidroid.xutils.exception.DbException;
import com.lidroid.xutils.util.IOUtils;
import com.lidroid.xutils.util.LogUtils;
import com.supersonicads.sdk.utils.Constants.RequestParameters;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class DbUtils {
    private static HashMap<String, DbUtils> daoMap = new HashMap();
    private boolean allowTransaction = false;
    private DaoConfig daoConfig;
    private SQLiteDatabase database;
    private boolean debug = false;
    private final FindTempCache findTempCache = new FindTempCache();
    private Lock writeLock = new ReentrantLock();
    private volatile boolean writeLocked = false;

    public static class DaoConfig {
        private Context context;
        private String dbDir;
        private String dbName = "xUtils.db";
        private DbUpgradeListener dbUpgradeListener;
        private int dbVersion = 1;

        public DaoConfig(Context context) {
            this.context = context.getApplicationContext();
        }

        public Context getContext() {
            return this.context;
        }

        public String getDbName() {
            return this.dbName;
        }

        public void setDbName(String dbName) {
            if (!TextUtils.isEmpty(dbName)) {
                this.dbName = dbName;
            }
        }

        public int getDbVersion() {
            return this.dbVersion;
        }

        public void setDbVersion(int dbVersion) {
            this.dbVersion = dbVersion;
        }

        public DbUpgradeListener getDbUpgradeListener() {
            return this.dbUpgradeListener;
        }

        public void setDbUpgradeListener(DbUpgradeListener dbUpgradeListener) {
            this.dbUpgradeListener = dbUpgradeListener;
        }

        public String getDbDir() {
            return this.dbDir;
        }

        public void setDbDir(String dbDir) {
            this.dbDir = dbDir;
        }
    }

    public interface DbUpgradeListener {
        void onUpgrade(DbUtils dbUtils, int i, int i2);
    }

    private class FindTempCache {
        private final ConcurrentHashMap<String, Object> cache;
        private long seq;

        private FindTempCache() {
            this.cache = new ConcurrentHashMap();
            this.seq = 0;
        }

        public void put(String sql, Object result) {
            if (sql != null && result != null) {
                this.cache.put(sql, result);
            }
        }

        public Object get(String sql) {
            return this.cache.get(sql);
        }

        public void setSeq(long seq) {
            if (this.seq != seq) {
                this.cache.clear();
                this.seq = seq;
            }
        }
    }

    private DbUtils(DaoConfig config) {
        if (config == null) {
            throw new IllegalArgumentException("daoConfig may not be null");
        }
        this.database = createDatabase(config);
        this.daoConfig = config;
    }

    private static synchronized DbUtils getInstance(DaoConfig daoConfig) {
        DbUtils dao;
        synchronized (DbUtils.class) {
            dao = (DbUtils) daoMap.get(daoConfig.getDbName());
            if (dao == null) {
                dao = new DbUtils(daoConfig);
                daoMap.put(daoConfig.getDbName(), dao);
            } else {
                dao.daoConfig = daoConfig;
            }
            SQLiteDatabase database = dao.database;
            int oldVersion = database.getVersion();
            int newVersion = daoConfig.getDbVersion();
            if (oldVersion != newVersion) {
                if (oldVersion != 0) {
                    DbUpgradeListener upgradeListener = daoConfig.getDbUpgradeListener();
                    if (upgradeListener != null) {
                        upgradeListener.onUpgrade(dao, oldVersion, newVersion);
                    } else {
                        try {
                            dao.dropDb();
                        } catch (DbException e) {
                            LogUtils.e(e.getMessage(), e);
                        }
                    }
                }
                database.setVersion(newVersion);
            }
        }
        return dao;
    }

    public static DbUtils create(Context context) {
        return getInstance(new DaoConfig(context));
    }

    public static DbUtils create(Context context, String dbName) {
        DaoConfig config = new DaoConfig(context);
        config.setDbName(dbName);
        return getInstance(config);
    }

    public static DbUtils create(Context context, String dbDir, String dbName) {
        DaoConfig config = new DaoConfig(context);
        config.setDbDir(dbDir);
        config.setDbName(dbName);
        return getInstance(config);
    }

    public static DbUtils create(Context context, String dbName, int dbVersion, DbUpgradeListener dbUpgradeListener) {
        DaoConfig config = new DaoConfig(context);
        config.setDbName(dbName);
        config.setDbVersion(dbVersion);
        config.setDbUpgradeListener(dbUpgradeListener);
        return getInstance(config);
    }

    public static DbUtils create(Context context, String dbDir, String dbName, int dbVersion, DbUpgradeListener dbUpgradeListener) {
        DaoConfig config = new DaoConfig(context);
        config.setDbDir(dbDir);
        config.setDbName(dbName);
        config.setDbVersion(dbVersion);
        config.setDbUpgradeListener(dbUpgradeListener);
        return getInstance(config);
    }

    public static DbUtils create(DaoConfig daoConfig) {
        return getInstance(daoConfig);
    }

    public DbUtils configDebug(boolean debug) {
        this.debug = debug;
        return this;
    }

    public DbUtils configAllowTransaction(boolean allowTransaction) {
        this.allowTransaction = allowTransaction;
        return this;
    }

    public SQLiteDatabase getDatabase() {
        return this.database;
    }

    public DaoConfig getDaoConfig() {
        return this.daoConfig;
    }

    public void saveOrUpdate(Object entity) throws DbException {
        try {
            beginTransaction();
            createTableIfNotExist(entity.getClass());
            saveOrUpdateWithoutTransaction(entity);
            setTransactionSuccessful();
        } finally {
            endTransaction();
        }
    }

    public void saveOrUpdateAll(List<?> entities) throws DbException {
        if (entities != null && entities.size() != 0) {
            try {
                beginTransaction();
                createTableIfNotExist(entities.get(0).getClass());
                for (Object entity : entities) {
                    saveOrUpdateWithoutTransaction(entity);
                }
                setTransactionSuccessful();
            } finally {
                endTransaction();
            }
        }
    }

    public void replace(Object entity) throws DbException {
        try {
            beginTransaction();
            createTableIfNotExist(entity.getClass());
            execNonQuery(SqlInfoBuilder.buildReplaceSqlInfo(this, entity));
            setTransactionSuccessful();
        } finally {
            endTransaction();
        }
    }

    public void replaceAll(List<?> entities) throws DbException {
        if (entities != null && entities.size() != 0) {
            try {
                beginTransaction();
                createTableIfNotExist(entities.get(0).getClass());
                for (Object entity : entities) {
                    execNonQuery(SqlInfoBuilder.buildReplaceSqlInfo(this, entity));
                }
                setTransactionSuccessful();
            } finally {
                endTransaction();
            }
        }
    }

    public void save(Object entity) throws DbException {
        try {
            beginTransaction();
            createTableIfNotExist(entity.getClass());
            execNonQuery(SqlInfoBuilder.buildInsertSqlInfo(this, entity));
            setTransactionSuccessful();
        } finally {
            endTransaction();
        }
    }

    public void saveAll(List<?> entities) throws DbException {
        if (entities != null && entities.size() != 0) {
            try {
                beginTransaction();
                createTableIfNotExist(entities.get(0).getClass());
                for (Object entity : entities) {
                    execNonQuery(SqlInfoBuilder.buildInsertSqlInfo(this, entity));
                }
                setTransactionSuccessful();
            } finally {
                endTransaction();
            }
        }
    }

    public boolean saveBindingId(Object entity) throws DbException {
        boolean result = false;
        try {
            beginTransaction();
            createTableIfNotExist(entity.getClass());
            result = saveBindingIdWithoutTransaction(entity);
            setTransactionSuccessful();
            return result;
        } finally {
            endTransaction();
        }
    }

    public void saveBindingIdAll(List<?> entities) throws DbException {
        if (entities != null && entities.size() != 0) {
            try {
                beginTransaction();
                createTableIfNotExist(entities.get(0).getClass());
                for (Object entity : entities) {
                    if (!saveBindingIdWithoutTransaction(entity)) {
                        throw new DbException("saveBindingId error, transaction will not commit!");
                    }
                }
                setTransactionSuccessful();
            } finally {
                endTransaction();
            }
        }
    }

    public void deleteById(Class<?> entityType, Object idValue) throws DbException {
        if (tableIsExist(entityType)) {
            try {
                beginTransaction();
                execNonQuery(SqlInfoBuilder.buildDeleteSqlInfo(this, (Class) entityType, idValue));
                setTransactionSuccessful();
            } finally {
                endTransaction();
            }
        }
    }

    public void delete(Object entity) throws DbException {
        if (tableIsExist(entity.getClass())) {
            try {
                beginTransaction();
                execNonQuery(SqlInfoBuilder.buildDeleteSqlInfo(this, entity));
                setTransactionSuccessful();
            } finally {
                endTransaction();
            }
        }
    }

    public void delete(Class<?> entityType, WhereBuilder whereBuilder) throws DbException {
        if (tableIsExist(entityType)) {
            try {
                beginTransaction();
                execNonQuery(SqlInfoBuilder.buildDeleteSqlInfo(this, (Class) entityType, whereBuilder));
                setTransactionSuccessful();
            } finally {
                endTransaction();
            }
        }
    }

    public void deleteAll(List<?> entities) throws DbException {
        if (entities != null && entities.size() != 0 && tableIsExist(entities.get(0).getClass())) {
            try {
                beginTransaction();
                for (Object entity : entities) {
                    execNonQuery(SqlInfoBuilder.buildDeleteSqlInfo(this, entity));
                }
                setTransactionSuccessful();
            } finally {
                endTransaction();
            }
        }
    }

    public void deleteAll(Class<?> entityType) throws DbException {
        delete(entityType, null);
    }

    public void update(Object entity, String... updateColumnNames) throws DbException {
        if (tableIsExist(entity.getClass())) {
            try {
                beginTransaction();
                execNonQuery(SqlInfoBuilder.buildUpdateSqlInfo(this, entity, updateColumnNames));
                setTransactionSuccessful();
            } finally {
                endTransaction();
            }
        }
    }

    public void update(Object entity, WhereBuilder whereBuilder, String... updateColumnNames) throws DbException {
        if (tableIsExist(entity.getClass())) {
            try {
                beginTransaction();
                execNonQuery(SqlInfoBuilder.buildUpdateSqlInfo(this, entity, whereBuilder, updateColumnNames));
                setTransactionSuccessful();
            } finally {
                endTransaction();
            }
        }
    }

    public void updateAll(List<?> entities, String... updateColumnNames) throws DbException {
        if (entities != null && entities.size() != 0 && tableIsExist(entities.get(0).getClass())) {
            try {
                beginTransaction();
                for (Object entity : entities) {
                    execNonQuery(SqlInfoBuilder.buildUpdateSqlInfo(this, entity, updateColumnNames));
                }
                setTransactionSuccessful();
            } finally {
                endTransaction();
            }
        }
    }

    public void updateAll(List<?> entities, WhereBuilder whereBuilder, String... updateColumnNames) throws DbException {
        if (entities != null && entities.size() != 0 && tableIsExist(entities.get(0).getClass())) {
            try {
                beginTransaction();
                for (Object entity : entities) {
                    execNonQuery(SqlInfoBuilder.buildUpdateSqlInfo(this, entity, whereBuilder, updateColumnNames));
                }
                setTransactionSuccessful();
            } finally {
                endTransaction();
            }
        }
    }

    public <T> T findById(Class<T> entityType, Object idValue) throws DbException {
        if (!tableIsExist(entityType)) {
            return null;
        }
        String sql = Selector.from(entityType).where(Table.get(this, entityType).id.getColumnName(), RequestParameters.EQUAL, idValue).limit(1).toString();
        long seq = FindCacheSequence.getSeq();
        this.findTempCache.setSeq(seq);
        T obj = this.findTempCache.get(sql);
        if (obj != null) {
            return obj;
        }
        Cursor cursor = execQuery(sql);
        if (cursor != null) {
            try {
                if (cursor.moveToNext()) {
                    T entity = CursorUtils.getEntity(this, cursor, entityType, seq);
                    this.findTempCache.put(sql, entity);
                    IOUtils.closeQuietly(cursor);
                    return entity;
                }
                IOUtils.closeQuietly(cursor);
            } catch (Throwable th) {
                IOUtils.closeQuietly(cursor);
            }
        }
        return null;
    }

    public <T> T findFirst(Selector selector) throws DbException {
        if (!tableIsExist(selector.getEntityType())) {
            return null;
        }
        String sql = selector.limit(1).toString();
        long seq = FindCacheSequence.getSeq();
        this.findTempCache.setSeq(seq);
        T obj = this.findTempCache.get(sql);
        if (obj != null) {
            return obj;
        }
        Cursor cursor = execQuery(sql);
        if (cursor != null) {
            try {
                if (cursor.moveToNext()) {
                    T entity = CursorUtils.getEntity(this, cursor, selector.getEntityType(), seq);
                    this.findTempCache.put(sql, entity);
                    IOUtils.closeQuietly(cursor);
                    return entity;
                }
                IOUtils.closeQuietly(cursor);
            } catch (Throwable th) {
                IOUtils.closeQuietly(cursor);
            }
        }
        return null;
    }

    public <T> T findFirst(Class<T> entityType) throws DbException {
        return findFirst(Selector.from(entityType));
    }

    public <T> List<T> findAll(Selector selector) throws DbException {
        if (!tableIsExist(selector.getEntityType())) {
            return null;
        }
        String sql = selector.toString();
        long seq = FindCacheSequence.getSeq();
        this.findTempCache.setSeq(seq);
        Object obj = this.findTempCache.get(sql);
        if (obj != null) {
            return (List) obj;
        }
        List<T> result = new ArrayList();
        Cursor cursor = execQuery(sql);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                try {
                    result.add(CursorUtils.getEntity(this, cursor, selector.getEntityType(), seq));
                } catch (Throwable th) {
                    IOUtils.closeQuietly(cursor);
                }
            }
            this.findTempCache.put(sql, result);
            IOUtils.closeQuietly(cursor);
        }
        return result;
    }

    public <T> List<T> findAll(Class<T> entityType) throws DbException {
        return findAll(Selector.from(entityType));
    }

    public DbModel findDbModelFirst(SqlInfo sqlInfo) throws DbException {
        Cursor cursor = execQuery(sqlInfo);
        if (cursor != null) {
            try {
                if (cursor.moveToNext()) {
                    DbModel dbModel = CursorUtils.getDbModel(cursor);
                    IOUtils.closeQuietly(cursor);
                    return dbModel;
                }
                IOUtils.closeQuietly(cursor);
            } catch (Throwable th) {
                IOUtils.closeQuietly(cursor);
            }
        }
        return null;
    }

    public DbModel findDbModelFirst(DbModelSelector selector) throws DbException {
        DbModel dbModel = null;
        if (tableIsExist(selector.getEntityType())) {
            Cursor cursor = execQuery(selector.limit(1).toString());
            if (cursor != null) {
                try {
                    if (cursor.moveToNext()) {
                        dbModel = CursorUtils.getDbModel(cursor);
                        IOUtils.closeQuietly(cursor);
                    } else {
                        IOUtils.closeQuietly(cursor);
                    }
                } catch (Throwable th) {
                    IOUtils.closeQuietly(cursor);
                }
            }
        }
        return dbModel;
    }

    public List<DbModel> findDbModelAll(SqlInfo sqlInfo) throws DbException {
        List<DbModel> dbModelList = new ArrayList();
        Cursor cursor = execQuery(sqlInfo);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                try {
                    dbModelList.add(CursorUtils.getDbModel(cursor));
                } catch (Throwable th) {
                    IOUtils.closeQuietly(cursor);
                }
            }
            IOUtils.closeQuietly(cursor);
        }
        return dbModelList;
    }

    public List<DbModel> findDbModelAll(DbModelSelector selector) throws DbException {
        if (!tableIsExist(selector.getEntityType())) {
            return null;
        }
        List<DbModel> dbModelList = new ArrayList();
        Cursor cursor = execQuery(selector.toString());
        if (cursor == null) {
            return dbModelList;
        }
        while (cursor.moveToNext()) {
            try {
                dbModelList.add(CursorUtils.getDbModel(cursor));
            } catch (Throwable th) {
                IOUtils.closeQuietly(cursor);
            }
        }
        IOUtils.closeQuietly(cursor);
        return dbModelList;
    }

    public long count(Selector selector) throws DbException {
        Class<?> entityType = selector.getEntityType();
        if (!tableIsExist(entityType)) {
            return 0;
        }
        Table table = Table.get(this, entityType);
        return findDbModelFirst(selector.select("count(" + table.id.getColumnName() + ") as count")).getLong("count");
    }

    public long count(Class<?> entityType) throws DbException {
        return count(Selector.from(entityType));
    }

    private SQLiteDatabase createDatabase(DaoConfig config) {
        String dbDir = config.getDbDir();
        if (TextUtils.isEmpty(dbDir)) {
            return config.getContext().openOrCreateDatabase(config.getDbName(), 0, null);
        }
        File dir = new File(dbDir);
        if (dir.exists() || dir.mkdirs()) {
            return SQLiteDatabase.openOrCreateDatabase(new File(dbDir, config.getDbName()), null);
        }
        return null;
    }

    private void saveOrUpdateWithoutTransaction(Object entity) throws DbException {
        Id id = Table.get(this, entity.getClass()).id;
        if (!id.isAutoIncrement()) {
            execNonQuery(SqlInfoBuilder.buildReplaceSqlInfo(this, entity));
        } else if (id.getColumnValue(entity) != null) {
            execNonQuery(SqlInfoBuilder.buildUpdateSqlInfo(this, entity, new String[0]));
        } else {
            saveBindingIdWithoutTransaction(entity);
        }
    }

    private boolean saveBindingIdWithoutTransaction(Object entity) throws DbException {
        Table table = Table.get(this, entity.getClass());
        Id idColumn = table.id;
        if (idColumn.isAutoIncrement()) {
            execNonQuery(SqlInfoBuilder.buildInsertSqlInfo(this, entity));
            long id = getLastAutoIncrementId(table.tableName);
            if (id == -1) {
                return false;
            }
            idColumn.setAutoIncrementId(entity, id);
            return true;
        }
        execNonQuery(SqlInfoBuilder.buildInsertSqlInfo(this, entity));
        return true;
    }

    private long getLastAutoIncrementId(String tableName) throws DbException {
        long id = -1;
        Cursor cursor = execQuery("SELECT seq FROM sqlite_sequence WHERE name='" + tableName + "'");
        if (cursor != null) {
            try {
                if (cursor.moveToNext()) {
                    id = cursor.getLong(0);
                }
                IOUtils.closeQuietly(cursor);
            } catch (Throwable th) {
                IOUtils.closeQuietly(cursor);
            }
        }
        return id;
    }

    public void createTableIfNotExist(Class<?> entityType) throws DbException {
        if (!tableIsExist(entityType)) {
            execNonQuery(SqlInfoBuilder.buildCreateTableSqlInfo(this, entityType));
            String execAfterTableCreated = TableUtils.getExecAfterTableCreated(entityType);
            if (!TextUtils.isEmpty(execAfterTableCreated)) {
                execNonQuery(execAfterTableCreated);
            }
        }
    }

    public boolean tableIsExist(Class<?> entityType) throws DbException {
        Table table = Table.get(this, entityType);
        if (table.isCheckedDatabase()) {
            return true;
        }
        Cursor cursor = execQuery("SELECT COUNT(*) AS c FROM sqlite_master WHERE type='table' AND name='" + table.tableName + "'");
        if (cursor != null) {
            try {
                if (!cursor.moveToNext() || cursor.getInt(0) <= 0) {
                    IOUtils.closeQuietly(cursor);
                } else {
                    table.setCheckedDatabase(true);
                    IOUtils.closeQuietly(cursor);
                    return true;
                }
            } catch (Throwable th) {
                IOUtils.closeQuietly(cursor);
            }
        }
        return false;
    }

    public void dropDb() throws DbException {
        Cursor cursor = execQuery("SELECT name FROM sqlite_master WHERE type='table' AND name<>'sqlite_sequence'");
        if (cursor != null) {
            while (cursor.moveToNext()) {
                try {
                    String tableName = cursor.getString(0);
                    execNonQuery("DROP TABLE " + tableName);
                    Table.remove(this, tableName);
                } catch (Throwable e) {
                    try {
                        DbException dbException = new DbException(e);
                    } catch (Throwable th) {
                        IOUtils.closeQuietly(cursor);
                    }
                }
            }
            IOUtils.closeQuietly(cursor);
        }
    }

    public void dropTable(Class<?> entityType) throws DbException {
        if (tableIsExist(entityType)) {
            execNonQuery("DROP TABLE " + TableUtils.getTableName(entityType));
            Table.remove(this, (Class) entityType);
        }
    }

    public void close() {
        String dbName = this.daoConfig.getDbName();
        if (daoMap.containsKey(dbName)) {
            daoMap.remove(dbName);
            this.database.close();
        }
    }

    private void debugSql(String sql) {
        if (this.debug) {
            LogUtils.d(sql);
        }
    }

    private void beginTransaction() {
        if (this.allowTransaction) {
            this.database.beginTransaction();
            return;
        }
        this.writeLock.lock();
        this.writeLocked = true;
    }

    private void setTransactionSuccessful() {
        if (this.allowTransaction) {
            this.database.setTransactionSuccessful();
        }
    }

    private void endTransaction() {
        if (this.allowTransaction) {
            this.database.endTransaction();
        }
        if (this.writeLocked) {
            this.writeLock.unlock();
            this.writeLocked = false;
        }
    }

    public void execNonQuery(SqlInfo sqlInfo) throws DbException {
        debugSql(sqlInfo.getSql());
        try {
            if (sqlInfo.getBindArgs() != null) {
                this.database.execSQL(sqlInfo.getSql(), sqlInfo.getBindArgsAsArray());
            } else {
                this.database.execSQL(sqlInfo.getSql());
            }
        } catch (Throwable e) {
            DbException dbException = new DbException(e);
        }
    }

    public void execNonQuery(String sql) throws DbException {
        debugSql(sql);
        try {
            this.database.execSQL(sql);
        } catch (Throwable e) {
            DbException dbException = new DbException(e);
        }
    }

    public Cursor execQuery(SqlInfo sqlInfo) throws DbException {
        debugSql(sqlInfo.getSql());
        try {
            return this.database.rawQuery(sqlInfo.getSql(), sqlInfo.getBindArgsAsStrArray());
        } catch (Throwable e) {
            DbException dbException = new DbException(e);
        }
    }

    public Cursor execQuery(String sql) throws DbException {
        debugSql(sql);
        try {
            return this.database.rawQuery(sql, null);
        } catch (Throwable e) {
            DbException dbException = new DbException(e);
        }
    }
}
