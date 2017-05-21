package com.nativex.monetization.manager;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import com.google.gson.Gson;
import com.nativex.common.Log;
import com.nativex.monetization.business.CacheFile;
import com.nativex.monetization.business.DownloadMap;
import com.nativex.monetization.database.CacheConstants;
import com.nativex.monetization.database.CacheConstants.CACHE;
import com.nativex.monetization.database.CacheConstants.CACHE_UTILS;
import com.nativex.monetization.database.CacheConstants.DOWNLOAD_IDS;
import com.nativex.monetization.database.CacheDatabase;
import com.nativex.monetization.enums.FileStatus;
import com.supersonicads.sdk.utils.Constants.RequestParameters;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CacheDBManager {
    private static SQLiteDatabase cacheDB;
    private static CacheDatabase cacheDBCreator;
    private static CacheDBManager instance;
    private static final Object singletonLock = new Object();

    public static CacheDBManager getInstance() {
        if (instance == null) {
            synchronized (singletonLock) {
                if (instance == null) {
                    instance = new CacheDBManager();
                }
            }
        }
        if (cacheDBCreator == null) {
            try {
                open();
            } catch (SQLException e) {
                Log.v("Exception in opening caching database access.");
            }
        }
        return instance;
    }

    private CacheDBManager() {
        open();
    }

    private static void open() throws SQLException {
        if (MonetizationSharedDataManager.getContext() != null) {
            cacheDBCreator = new CacheDatabase(MonetizationSharedDataManager.getContext());
            cacheDB = cacheDBCreator.getWritableDatabase();
        }
    }

    public static void release() {
        if (cacheDBCreator != null) {
            cacheDBCreator.close();
        }
        if (cacheDB != null) {
            cacheDB.close();
        }
        cacheDBCreator = null;
        cacheDB = null;
        instance = null;
    }

    public void addCache(CacheFile cacheFile) throws SQLException {
        if (cacheDB != null) {
            ContentValues values = new ContentValues();
            values.put("OfferId", Long.valueOf(cacheFile.getOfferId()));
            values.put(CACHE.FILE_STATUS, cacheFile.getFileStatus().getId());
            values.put("ExpirationDateUTC", Long.toString(cacheFile.getExpiryTime()));
            values.put("FileSize", Long.toString(cacheFile.getFileSize()));
            values.put("RelativeUrl", cacheFile.getRelativeUrl());
            values.put("MD5", cacheFile.getMD5());
            values.put("Ext", cacheFile.getExt());
            values.put("CDN", cacheFile.getCDN());
            cacheDB.insertOrThrow(CacheConstants.CACHE_TABLE, null, values);
        }
    }

    public void updateCacheExpiryTime(String md5, long expirationTime) {
        if (cacheDB != null) {
            String[] args = new String[]{md5};
            ContentValues values = new ContentValues();
            values.put("ExpirationDateUTC", Long.toString(expirationTime));
            cacheDB.update(CacheConstants.CACHE_TABLE, values, "MD5=?", args);
        }
    }

    public void updateFileStatusWithMD5(String MD5, FileStatus status) {
        if (cacheDB != null) {
            String[] args = new String[]{MD5};
            ContentValues values = new ContentValues();
            values.put(CACHE.FILE_STATUS, status.getId());
            cacheDB.update(CacheConstants.CACHE_TABLE, values, "MD5=?", args);
        }
    }

    public void updateFileStatusWithMD5IfNotInUse(String MD5, FileStatus status) {
        if (cacheDB != null) {
            String WHERE = "MD5=?" + " AND FileStatus != ?";
            String[] args = new String[]{MD5, FileStatus.STATUS_INUSE.getId()};
            ContentValues values = new ContentValues();
            values.put(CACHE.FILE_STATUS, status.getId());
            cacheDB.update(CacheConstants.CACHE_TABLE, values, WHERE, args);
        }
    }

    public List<CacheFile> getAllCacheFiles() {
        List<CacheFile> cacheFiles = new ArrayList();
        if (cacheDB != null) {
            Cursor cursor = cacheDB.query(CacheConstants.CACHE_TABLE, null, null, null, null, null, null);
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    CacheFile cacheFile = new CacheFile();
                    cacheFile.setCDN(cursor.getString(cursor.getColumnIndex("CDN")));
                    cacheFile.setExt(cursor.getString(cursor.getColumnIndex("Ext")));
                    cacheFile.setMD5(cursor.getString(cursor.getColumnIndex("MD5")));
                    cacheFile.setFileStatus(getFileStatus((long) cursor.getInt(cursor.getColumnIndex(CACHE.FILE_STATUS))));
                    cacheFile.setExpiryTime(cursor.getLong(cursor.getColumnIndex("ExpirationDateUTC")));
                    cacheFile.setFileSize(cursor.getLong(cursor.getColumnIndex("FileSize")));
                    cacheFile.setRelativeUrl(cursor.getString(cursor.getColumnIndex("RelativeUrl")));
                    cacheFile.setOfferId(cursor.getLong(cursor.getColumnIndex("OfferId")));
                    cacheFiles.add(cacheFile);
                } while (cursor.moveToNext());
                if (cursor != null) {
                    cursor.close();
                }
            } else if (cursor != null) {
                cursor.close();
            }
        }
        return cacheFiles;
    }

    public List<CacheFile> getAllCacheFilesByMD5() {
        List<CacheFile> cacheFiles = new ArrayList();
        if (cacheDB != null) {
            Cursor cursor = cacheDB.query(CacheConstants.CACHE_TABLE, null, null, null, "MD5", null, null);
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    CacheFile cacheFile = new CacheFile();
                    cacheFile.setCDN(cursor.getString(cursor.getColumnIndex("CDN")));
                    cacheFile.setExt(cursor.getString(cursor.getColumnIndex("Ext")));
                    cacheFile.setMD5(cursor.getString(cursor.getColumnIndex("MD5")));
                    cacheFile.setFileStatus(getFileStatus((long) cursor.getInt(cursor.getColumnIndex(CACHE.FILE_STATUS))));
                    cacheFile.setExpiryTime(cursor.getLong(cursor.getColumnIndex("ExpirationDateUTC")));
                    cacheFile.setFileSize(cursor.getLong(cursor.getColumnIndex("FileSize")));
                    cacheFile.setRelativeUrl(cursor.getString(cursor.getColumnIndex("RelativeUrl")));
                    cacheFile.setOfferId(cursor.getLong(cursor.getColumnIndex("OfferId")));
                    cacheFiles.add(cacheFile);
                } while (cursor.moveToNext());
                if (cursor != null) {
                    cursor.close();
                }
            } else if (cursor != null) {
                cursor.close();
            }
        }
        return cacheFiles;
    }

    public List<CacheFile> getCacheFilesForPurging() {
        List<CacheFile> cacheFiles = new ArrayList();
        if (cacheDB != null) {
            String[] args = new String[]{FileStatus.STATUS_DELETED.getId(), FileStatus.STATUS_INUSE.getId()};
            Cursor cursor = cacheDB.query(CacheConstants.CACHE_TABLE, null, "FileStatus<>? AND FileStatus<>?", args, "MD5", null, "ExpirationDateUTC ASC");
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    CacheFile cacheFile = new CacheFile();
                    cacheFile.setCDN(cursor.getString(cursor.getColumnIndex("CDN")));
                    cacheFile.setExt(cursor.getString(cursor.getColumnIndex("Ext")));
                    cacheFile.setMD5(cursor.getString(cursor.getColumnIndex("MD5")));
                    cacheFile.setFileStatus(getFileStatus((long) cursor.getInt(cursor.getColumnIndex(CACHE.FILE_STATUS))));
                    cacheFile.setExpiryTime(cursor.getLong(cursor.getColumnIndex("ExpirationDateUTC")));
                    cacheFile.setFileSize(cursor.getLong(cursor.getColumnIndex("FileSize")));
                    cacheFile.setRelativeUrl(cursor.getString(cursor.getColumnIndex("RelativeUrl")));
                    cacheFile.setOfferId(cursor.getLong(cursor.getColumnIndex("OfferId")));
                    cacheFiles.add(cacheFile);
                } while (cursor.moveToNext());
                if (cursor != null) {
                    cursor.close();
                }
            } else if (cursor != null) {
                cursor.close();
            }
        }
        return cacheFiles;
    }

    public List<CacheFile> getCacheFilesForDownload() {
        List<CacheFile> cacheFiles = new ArrayList();
        if (cacheDB != null) {
            String[] args = new String[]{FileStatus.STATUS_PENDING.getId()};
            Cursor cursor = cacheDB.query(CacheConstants.CACHE_TABLE, null, "FileStatus=?", args, "MD5", null, "ExpirationDateUTC DESC");
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    CacheFile cacheFile = new CacheFile();
                    cacheFile.setCDN(cursor.getString(cursor.getColumnIndex("CDN")));
                    cacheFile.setExt(cursor.getString(cursor.getColumnIndex("Ext")));
                    cacheFile.setMD5(cursor.getString(cursor.getColumnIndex("MD5")));
                    cacheFile.setFileStatus(getFileStatus((long) cursor.getInt(cursor.getColumnIndex(CACHE.FILE_STATUS))));
                    cacheFile.setExpiryTime(cursor.getLong(cursor.getColumnIndex("ExpirationDateUTC")));
                    cacheFile.setFileSize(cursor.getLong(cursor.getColumnIndex("FileSize")));
                    cacheFile.setRelativeUrl(cursor.getString(cursor.getColumnIndex("RelativeUrl")));
                    cacheFile.setOfferId(cursor.getLong(cursor.getColumnIndex("OfferId")));
                    cacheFiles.add(cacheFile);
                } while (cursor.moveToNext());
                if (cursor != null) {
                    cursor.close();
                }
            } else if (cursor != null) {
                cursor.close();
            }
        }
        return cacheFiles;
    }

    public List<CacheFile> getCacheFilesMatchingMD5(String md5) {
        List<CacheFile> cacheFiles = new ArrayList();
        if (cacheDB != null) {
            String[] args = new String[]{md5};
            Cursor cursor = cacheDB.query(CacheConstants.CACHE_TABLE, null, "MD5=?", args, null, null, null);
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    CacheFile file = new CacheFile();
                    file.setCDN(cursor.getString(cursor.getColumnIndex("CDN")));
                    file.setExt(cursor.getString(cursor.getColumnIndex("Ext")));
                    file.setMD5(cursor.getString(cursor.getColumnIndex("MD5")));
                    file.setFileStatus(getFileStatus((long) cursor.getInt(cursor.getColumnIndex(CACHE.FILE_STATUS))));
                    file.setExpiryTime(cursor.getLong(cursor.getColumnIndex("ExpirationDateUTC")));
                    file.setFileSize(cursor.getLong(cursor.getColumnIndex("FileSize")));
                    file.setRelativeUrl(cursor.getString(cursor.getColumnIndex("RelativeUrl")));
                    file.setOfferId(cursor.getLong(cursor.getColumnIndex("OfferId")));
                    cacheFiles.add(file);
                } while (cursor.moveToNext());
                if (cursor != null) {
                    cursor.close();
                }
            } else if (cursor != null) {
                cursor.close();
            }
        }
        return cacheFiles;
    }

    public CacheFile getTopOneCacheFileMatchingMD5(String md5) {
        if (cacheDB == null) {
            return null;
        }
        CacheFile cacheFile = null;
        String[] args = new String[]{md5};
        Cursor cursor = cacheDB.query(CacheConstants.CACHE_TABLE, null, "MD5=?", args, null, null, "ExpirationDateUTC DESC limit 1");
        if (cursor == null || !cursor.moveToFirst()) {
            if (cursor != null) {
                cursor.close();
            }
            return cacheFile;
        }
        do {
            cacheFile = new CacheFile();
            cacheFile.setCDN(cursor.getString(cursor.getColumnIndex("CDN")));
            cacheFile.setExt(cursor.getString(cursor.getColumnIndex("Ext")));
            cacheFile.setMD5(cursor.getString(cursor.getColumnIndex("MD5")));
            cacheFile.setFileStatus(getFileStatus((long) cursor.getInt(cursor.getColumnIndex(CACHE.FILE_STATUS))));
            cacheFile.setExpiryTime(cursor.getLong(cursor.getColumnIndex("ExpirationDateUTC")));
            cacheFile.setFileSize(cursor.getLong(cursor.getColumnIndex("FileSize")));
            cacheFile.setRelativeUrl(cursor.getString(cursor.getColumnIndex("RelativeUrl")));
            cacheFile.setOfferId(cursor.getLong(cursor.getColumnIndex("OfferId")));
        } while (cursor.moveToNext());
        if (cursor != null) {
            cursor.close();
        }
        return cacheFile;
    }

    public List<CacheFile> getReadyCacheFilesForOffer(long offerId) {
        List<CacheFile> cacheFiles = new ArrayList();
        if (cacheDB != null) {
            String[] args = new String[]{Long.toString(offerId), FileStatus.STATUS_READY.getId(), FileStatus.STATUS_INUSE.getId()};
            Cursor cursor = cacheDB.query(CacheConstants.CACHE_TABLE, null, "OfferId=? AND (FileStatus=? OR FileStatus=?)", args, null, null, null);
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    CacheFile cacheFile = new CacheFile();
                    cacheFile.setCDN(cursor.getString(cursor.getColumnIndex("CDN")));
                    cacheFile.setExt(cursor.getString(cursor.getColumnIndex("Ext")));
                    cacheFile.setMD5(cursor.getString(cursor.getColumnIndex("MD5")));
                    cacheFile.setFileStatus(getFileStatus((long) cursor.getInt(cursor.getColumnIndex(CACHE.FILE_STATUS))));
                    cacheFile.setExpiryTime(cursor.getLong(cursor.getColumnIndex("ExpirationDateUTC")));
                    cacheFile.setFileSize(cursor.getLong(cursor.getColumnIndex("FileSize")));
                    cacheFile.setRelativeUrl(cursor.getString(cursor.getColumnIndex("RelativeUrl")));
                    cacheFile.setOfferId(cursor.getLong(cursor.getColumnIndex("OfferId")));
                    cacheFiles.add(cacheFile);
                } while (cursor.moveToNext());
                if (cursor != null) {
                    cursor.close();
                }
            } else if (cursor != null) {
                cursor.close();
            }
        }
        return cacheFiles;
    }

    public List<CacheFile> getAllCacheFilesForOffer(long offerId) {
        List<CacheFile> cacheFiles = new ArrayList();
        if (cacheDB != null) {
            String[] args = new String[]{Long.toString(offerId)};
            Cursor cursor = cacheDB.query(CacheConstants.CACHE_TABLE, null, "OfferId=?", args, null, null, null);
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    CacheFile cacheFile = new CacheFile();
                    cacheFile.setCDN(cursor.getString(cursor.getColumnIndex("CDN")));
                    cacheFile.setExt(cursor.getString(cursor.getColumnIndex("Ext")));
                    cacheFile.setMD5(cursor.getString(cursor.getColumnIndex("MD5")));
                    cacheFile.setFileStatus(getFileStatus((long) cursor.getInt(cursor.getColumnIndex(CACHE.FILE_STATUS))));
                    cacheFile.setExpiryTime(cursor.getLong(cursor.getColumnIndex("ExpirationDateUTC")));
                    cacheFile.setFileSize(cursor.getLong(cursor.getColumnIndex("FileSize")));
                    cacheFile.setRelativeUrl(cursor.getString(cursor.getColumnIndex("RelativeUrl")));
                    cacheFile.setOfferId(cursor.getLong(cursor.getColumnIndex("OfferId")));
                    cacheFiles.add(cacheFile);
                } while (cursor.moveToNext());
                if (cursor != null) {
                    cursor.close();
                }
            } else if (cursor != null) {
                cursor.close();
            }
        }
        return cacheFiles;
    }

    public List<CacheFile> getCacheFilesForFileStatusDeleted() {
        List<CacheFile> cacheFiles = new ArrayList();
        if (cacheDB != null) {
            String[] args = new String[]{FileStatus.STATUS_DELETED.getId()};
            Cursor cursor = cacheDB.query(CacheConstants.CACHE_TABLE, null, "FileStatus=?", args, null, null, null);
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    CacheFile cacheFile = new CacheFile();
                    cacheFile.setCDN(cursor.getString(cursor.getColumnIndex("CDN")));
                    cacheFile.setExt(cursor.getString(cursor.getColumnIndex("Ext")));
                    cacheFile.setMD5(cursor.getString(cursor.getColumnIndex("MD5")));
                    cacheFile.setFileStatus(getFileStatus((long) cursor.getInt(cursor.getColumnIndex(CACHE.FILE_STATUS))));
                    cacheFile.setExpiryTime(cursor.getLong(cursor.getColumnIndex("ExpirationDateUTC")));
                    cacheFile.setFileSize(cursor.getLong(cursor.getColumnIndex("FileSize")));
                    cacheFile.setRelativeUrl(cursor.getString(cursor.getColumnIndex("RelativeUrl")));
                    cacheFile.setOfferId(cursor.getLong(cursor.getColumnIndex("OfferId")));
                    cacheFiles.add(cacheFile);
                } while (cursor.moveToNext());
                if (cursor != null) {
                    cursor.close();
                }
            } else if (cursor != null) {
                cursor.close();
            }
        }
        return cacheFiles;
    }

    public Set<String> getMD5InOffer(long offerId) {
        Set<String> md5List = new HashSet();
        if (cacheDB != null) {
            String[] args = new String[]{Long.toString(offerId)};
            Cursor cursor = cacheDB.query(CacheConstants.CACHE_TABLE, null, "OfferId=?", args, "MD5", null, null);
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    md5List.add(cursor.getString(cursor.getColumnIndex("MD5")));
                } while (cursor.moveToNext());
                if (cursor != null) {
                    cursor.close();
                }
            } else if (cursor != null) {
                cursor.close();
            }
        }
        return md5List;
    }

    public Set<String> getFileNamesOfAllCacheFiles() {
        Set<String> fileNameList = new HashSet();
        if (cacheDB != null) {
            Cursor cursor = cacheDB.query(CacheConstants.CACHE_TABLE, null, null, null, "MD5", null, null);
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    String md5 = cursor.getString(cursor.getColumnIndex("MD5"));
                    fileNameList.add(md5 + "." + cursor.getString(cursor.getColumnIndex("Ext")));
                } while (cursor.moveToNext());
                if (cursor != null) {
                    cursor.close();
                }
            } else if (cursor != null) {
                cursor.close();
            }
        }
        return fileNameList;
    }

    public List<Long> getOffersCachedList() {
        List<Long> offersCachedList = new ArrayList();
        if (cacheDB != null) {
            Cursor cursor = cacheDB.rawQuery("SELECT DISTINCT OfferId FROM cache_files", null);
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    long offerId = cursor.getLong(cursor.getColumnIndex("OfferId"));
                    if (isOfferCached(offerId)) {
                        offersCachedList.add(Long.valueOf(offerId));
                    }
                } while (cursor.moveToNext());
                if (cursor != null) {
                    cursor.close();
                }
            } else if (cursor != null) {
                cursor.close();
            }
        }
        return offersCachedList;
    }

    public boolean isOfferIdAndRelativeUrlFoundForMD5(String md5, long offerId, String relativeUrl) {
        boolean z = false;
        if (cacheDB != null) {
            String[] args = new String[]{md5, Long.toString(offerId), relativeUrl};
            z = false;
            Cursor cursor = cacheDB.query(CacheConstants.CACHE_TABLE, null, "MD5=? AND OfferId=? AND RelativeUrl=?", args, null, null, null);
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    z = true;
                } while (cursor.moveToNext());
                if (cursor != null) {
                    cursor.close();
                }
            } else if (cursor != null) {
                cursor.close();
            }
        }
        return z;
    }

    public boolean isOfferCached(long offerId) {
        if (cacheDB == null) {
            return false;
        }
        String[] args = new String[]{Long.toString(offerId)};
        boolean offerCached = true;
        Cursor cursor = cacheDB.query(CacheConstants.CACHE_TABLE, null, "OfferId=?", args, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                int i;
                FileStatus fileStatus = getFileStatus((long) cursor.getInt(cursor.getColumnIndex(CACHE.FILE_STATUS)));
                if (fileStatus == FileStatus.STATUS_READY || fileStatus == FileStatus.STATUS_INUSE) {
                    i = 1;
                } else {
                    i = 0;
                }
                if (i == 0) {
                    offerCached = false;
                    break;
                }
            } while (cursor.moveToNext());
        }
        if (cursor == null) {
            return offerCached;
        }
        cursor.close();
        return offerCached;
    }

    public long getTotalSizeOfCacheFiles() {
        if (cacheDB == null) {
            return 0;
        }
        Cursor cursor = cacheDB.rawQuery("SELECT SUM(FileSize) FROM (SELECT MAX(FileSize) AS FileSize FROM cache_files WHERE FileStatus<>" + FileStatus.STATUS_DELETED.getId() + " GROUP BY " + "MD5" + ") AS TOTALSIZE", null);
        long result = 0;
        if (cursor.moveToFirst()) {
            result = (long) cursor.getInt(0);
        }
        cursor.close();
        return result;
    }

    public long getSizeOfDownloadedCacheFiles() {
        if (cacheDB == null) {
            return 0;
        }
        Cursor cursor = cacheDB.rawQuery("SELECT SUM(FileSize) FROM (SELECT MAX(FileSize) as FileSize FROM cache_files WHERE FileStatus=" + FileStatus.STATUS_READY.getId() + " OR " + CACHE.FILE_STATUS + RequestParameters.EQUAL + FileStatus.STATUS_INUSE.getId() + " GROUP BY " + "MD5" + ") AS TOTALSIZE", null);
        long result = 0;
        if (cursor.moveToFirst()) {
            result = (long) cursor.getInt(0);
        }
        cursor.close();
        return result;
    }

    private FileStatus getFileStatus(long id) {
        for (FileStatus s : FileStatus.values()) {
            if (s.getId().equals(Long.toString(id))) {
                return s;
            }
        }
        return null;
    }

    public void deleteOffersWithFileStatusDeleted() {
        if (cacheDB != null) {
            Cursor cursor = cacheDB.rawQuery("SELECT DISTINCT OfferId FROM cache_files", null);
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    checkFileStatusDeletedForOffer(cursor.getLong(cursor.getColumnIndex("OfferId")));
                } while (cursor.moveToNext());
                if (cursor != null) {
                    cursor.close();
                }
            } else if (cursor != null) {
                cursor.close();
            }
        }
    }

    void checkFileStatusDeletedForOffer(long offerId) {
        if (cacheDB != null) {
            String[] args = new String[]{Long.toString(offerId)};
            List<Long> cacheIds = new ArrayList();
            boolean statusOtherThanDelete = false;
            Cursor cursor = cacheDB.query(CacheConstants.CACHE_TABLE, null, "OfferId=?", args, null, null, null);
            if (cursor == null || !cursor.moveToFirst()) {
                if (!statusOtherThanDelete) {
                    for (Long cacheId : cacheIds) {
                        deleteCacheFileWithCacheId(cacheId.longValue());
                    }
                }
                if (cursor != null) {
                    cursor.close();
                }
            }
            do {
                if (getFileStatus((long) cursor.getInt(cursor.getColumnIndex(CACHE.FILE_STATUS))) == FileStatus.STATUS_DELETED) {
                    cacheIds.add(Long.valueOf(cursor.getLong(cursor.getColumnIndex(CACHE.CACHE_ID))));
                } else {
                    statusOtherThanDelete = true;
                }
            } while (cursor.moveToNext());
            if (statusOtherThanDelete) {
                while (r0.hasNext()) {
                    deleteCacheFileWithCacheId(cacheId.longValue());
                }
            }
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    void deleteCacheFileWithCacheId(long cacheId) {
        if (cacheDB != null) {
            String[] args = new String[]{Long.toString(cacheId), FileStatus.STATUS_INUSE.getId()};
            cacheDB.delete(CacheConstants.CACHE_TABLE, "cache_id=? AND FileStatus<>?", args);
        }
    }

    public void deleteCacheFilesMatchingOfferId(long offerId, String md5) {
        if (cacheDB != null) {
            String[] args = new String[]{Long.toString(offerId), md5, FileStatus.STATUS_INUSE.getId()};
            cacheDB.delete(CacheConstants.CACHE_TABLE, "OfferId=? AND MD5=? AND FileStatus<>?", args);
        }
    }

    public void deleteCacheFilesMatchingMD5(String md5) {
        if (cacheDB != null) {
            String[] args = new String[]{md5, FileStatus.STATUS_INUSE.getId()};
            cacheDB.delete(CacheConstants.CACHE_TABLE, "MD5=? AND FileStatus<>?", args);
        }
    }

    public void deleteAllCacheFilesExceptInUse() {
        if (cacheDB != null) {
            String[] args = new String[]{FileStatus.STATUS_INUSE.getId()};
            cacheDB.delete(CacheConstants.CACHE_TABLE, "FileStatus<>?", args);
        }
    }

    public void deleteAllCacheFiles() {
        if (cacheDB != null) {
            cacheDB.delete(CacheConstants.CACHE_TABLE, null, null);
        }
    }

    void addCacheUtilFreeSpaceMin(long value) throws Exception {
        if (cacheDB != null) {
            ContentValues values = new ContentValues();
            values.put(CACHE_UTILS.CACHE_UTIL, CACHE_UTILS.FREE_SPACE_MIN);
            values.put(CACHE_UTILS.CACHE_UTIL_VALUE_INTEGER, Long.valueOf(value));
            try {
                cacheDB.insertOrThrow(CacheConstants.CACHE_UTIL_TABLE, null, values);
            } catch (Exception ex) {
                ex.printStackTrace();
                throw ex;
            }
        }
    }

    public void updateCacheUtilFreeSpaceMin(long value) {
        if (cacheDB != null) {
            String[] args = new String[]{CACHE_UTILS.FREE_SPACE_MIN};
            ContentValues values = new ContentValues();
            values.put(CACHE_UTILS.CACHE_UTIL_VALUE_INTEGER, Long.valueOf(value));
            if (((long) cacheDB.update(CacheConstants.CACHE_UTIL_TABLE, values, "cache_util=?", args)) == 0) {
                try {
                    addCacheUtilFreeSpaceMin(value);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public long getCacheUtilIntegerFreeSpaceMin() {
        if (cacheDB == null) {
            return 0;
        }
        String[] args = new String[]{CACHE_UTILS.FREE_SPACE_MIN};
        long value = 0;
        Cursor cursor = cacheDB.query(CacheConstants.CACHE_UTIL_TABLE, null, "cache_util=?", args, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                value = cursor.getLong(cursor.getColumnIndex(CACHE_UTILS.CACHE_UTIL_VALUE_INTEGER));
            } while (cursor.moveToNext());
            if (cursor != null) {
                return value;
            }
            cursor.close();
            return value;
        } else if (cursor != null) {
            return value;
        } else {
            cursor.close();
            return value;
        }
    }

    public int addDownloadId(CacheFile cacheFile) {
        if (cacheDB == null) {
            return -1;
        }
        ContentValues values = new ContentValues();
        values.put(DOWNLOAD_IDS.CACHE_FILE, cacheFile.toJson());
        return (int) cacheDB.insertOrThrow(CacheConstants.DOWNLOAD_MAP_TABLE, null, values);
    }

    public void deleteDownloadId(int downloadId) {
        if (cacheDB != null) {
            String[] args = new String[]{Integer.toString(downloadId)};
            cacheDB.delete(CacheConstants.DOWNLOAD_MAP_TABLE, "download_id=?", args);
        }
    }

    public CacheFile getCacheFileForDownloadId(int downloadId) {
        if (cacheDB == null) {
            return null;
        }
        String[] args = new String[]{Integer.toString(downloadId)};
        Cursor cursor = cacheDB.query(CacheConstants.DOWNLOAD_MAP_TABLE, null, "download_id=?", args, null, null, null);
        CacheFile file = null;
        if (cursor == null || !cursor.moveToFirst()) {
            if (cursor != null) {
                cursor.close();
            }
            return file;
        }
        do {
            String cacheFileJson = cursor.getString(cursor.getColumnIndex(DOWNLOAD_IDS.CACHE_FILE));
            if (cacheFileJson != null) {
                file = (CacheFile) new Gson().fromJson(cacheFileJson, CacheFile.class);
            }
        } while (cursor.moveToNext());
        if (cursor != null) {
            cursor.close();
        }
        return file;
    }

    public List<DownloadMap> getDownloadMap() {
        List<DownloadMap> downloadMapList = new ArrayList();
        if (cacheDB != null) {
            Cursor cursor = cacheDB.query(CacheConstants.DOWNLOAD_MAP_TABLE, null, null, null, null, null, null);
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    DownloadMap map = new DownloadMap();
                    map.setDownloadId(cursor.getInt(cursor.getColumnIndex(DOWNLOAD_IDS.DOWNLOAD_ID)));
                    map.setCacheFile((CacheFile) new Gson().fromJson(cursor.getString(cursor.getColumnIndex(DOWNLOAD_IDS.CACHE_FILE)), CacheFile.class));
                    downloadMapList.add(map);
                } while (cursor.moveToNext());
                if (cursor != null) {
                    cursor.close();
                }
            } else if (cursor != null) {
                cursor.close();
            }
        }
        return downloadMapList;
    }
}
