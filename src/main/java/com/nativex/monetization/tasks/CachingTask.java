package com.nativex.monetization.tasks;

import com.nativex.common.Log;
import com.nativex.common.Utilities;
import com.nativex.monetization.business.CacheFile;
import com.nativex.monetization.business.CacheOffers;
import com.nativex.monetization.business.GetOfferCacheResponseData;
import com.nativex.monetization.enums.FileStatus;
import com.nativex.monetization.manager.CacheDBManager;
import com.nativex.monetization.manager.CacheDownloadManager;
import com.nativex.monetization.manager.CacheFileManager;
import com.nativex.monetization.manager.CacheManager;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CachingTask implements Runnable {
    private final GetOfferCacheResponseData cacheResponseData;

    public CachingTask(GetOfferCacheResponseData responseData) {
        this.cacheResponseData = responseData;
    }

    public void run() {
        try {
            executeCachingAlgorithm();
        } catch (Exception e) {
            Log.e("Exception in caching algorithm.", e);
        }
    }

    public void executeCachingAlgorithm() {
        long freeSpaceMinMegabytes = CacheDBManager.getInstance().getCacheUtilIntegerFreeSpaceMin();
        if (this.cacheResponseData.getFreeSpaceMinMegabytes() != null) {
            freeSpaceMinMegabytes = (long) this.cacheResponseData.getFreeSpaceMinMegabytes().intValue();
            CacheDBManager.getInstance().updateCacheUtilFreeSpaceMin(freeSpaceMinMegabytes);
        }
        long deviceInternalFreeSpaceInBytes = CacheFileManager.getAvailableInternalMemorySizeInBytes();
        Log.v("Available internal free space " + Utilities.convertBytesToMbAsString(deviceInternalFreeSpaceInBytes));
        long cacheDirectorySizeInBytes = CacheFileManager.getInternalCacheDirectorySizeInBytes();
        long freeSpaceMinInBytes = Utilities.convertMBtoBytes(freeSpaceMinMegabytes);
        long cacheSizeMaxInBytes = Utilities.convertMBtoBytes((long) this.cacheResponseData.getCacheSizeMax());
        Log.v("freeSpaceMin: " + Utilities.convertBytesToMbAsString(freeSpaceMinInBytes) + " cacheSizeMax: " + Utilities.convertBytesToMbAsString(cacheSizeMaxInBytes) + " deviceInternalFreeSpace: " + Utilities.convertBytesToMbAsString(deviceInternalFreeSpaceInBytes));
        synchronized (CacheManager.getExtendedOperationLock()) {
            if (shouldClearCacheByEvaluatingCacheSizeMax(freeSpaceMinInBytes, cacheSizeMaxInBytes, deviceInternalFreeSpaceInBytes, cacheDirectorySizeInBytes)) {
                clearCache();
            } else {
                updateOfferIdToCacheFiles(this.cacheResponseData);
                for (int i = 0; i < this.cacheResponseData.getOffers().size(); i++) {
                    updateDatabaseWithIncomingData(((CacheOffers) this.cacheResponseData.getOffers().get(i)).getOfferId(), ((CacheOffers) this.cacheResponseData.getOffers().get(i)).getFiles());
                }
                long totalCacheSize = CacheDBManager.getInstance().getTotalSizeOfCacheFiles();
                if (totalCacheSize < 0) {
                    totalCacheSize = 0;
                }
                long downloadedCacheSizeInBytes = CacheDBManager.getInstance().getSizeOfDownloadedCacheFiles();
                long freeSpaceNeededToPurgeInBytes = findSpaceNeededForPurging(freeSpaceMinInBytes, cacheSizeMaxInBytes, deviceInternalFreeSpaceInBytes, downloadedCacheSizeInBytes, totalCacheSize - downloadedCacheSizeInBytes);
                if (freeSpaceNeededToPurgeInBytes > 0) {
                    purgeFreeSpace(freeSpaceNeededToPurgeInBytes);
                    Log.v("Cache size after purging " + Utilities.convertBytesToMbAsString(CacheDBManager.getInstance().getTotalSizeOfCacheFiles()));
                }
            }
            CacheDBManager.getInstance().deleteOffersWithFileStatusDeleted();
            CacheFileManager.cleanUpNativeXCachedDirectory();
            deleteFilesWithFileStatusDeleted();
        }
        initiateDownloads();
    }

    public boolean shouldClearCacheByEvaluatingCacheSizeMax(long freeSpaceMin, long cacheSizeMax, long deviceInternalFreeSpace, long cacheDirectorySize) {
        if (cacheSizeMax < 0) {
            cacheSizeMax = 0;
        }
        if (freeSpaceMin < 0) {
            freeSpaceMin = 0;
        }
        if (deviceInternalFreeSpace < 0) {
            deviceInternalFreeSpace = 0;
        }
        if (cacheDirectorySize < 0) {
            cacheDirectorySize = 0;
        }
        long maxDeviceFreeSpaceAvailable = deviceInternalFreeSpace + cacheDirectorySize;
        Log.v("Maximum DeviceFreeSpaceAvailable: " + Utilities.convertBytesToMbAsString(maxDeviceFreeSpaceAvailable));
        if (cacheSizeMax == 0) {
            Log.v("CacheSizeMax is zero, deleting all the cached files.");
            return true;
        } else if (freeSpaceMin <= maxDeviceFreeSpaceAvailable) {
            return false;
        } else {
            Log.v("FreeSpaceMin is greater than maximum available device free space, deleting all the cached files.");
            return true;
        }
    }

    public long findSpaceNeededForPurging(long freeSpaceMin, long cacheSizeMax, long deviceInternalFreeSpace, long downloadedCacheSize, long unDownloadedCacheSize) {
        return Math.max(findSpaceNeededToMaintainFreeSpaceMin(freeSpaceMin, deviceInternalFreeSpace, unDownloadedCacheSize), findSpaceNeededToMaintainCacheSizeMax(cacheSizeMax, downloadedCacheSize, unDownloadedCacheSize));
    }

    public long findSpaceNeededToMaintainFreeSpaceMin(long freeSpaceMin, long deviceInternalFreeSpace, long unDownloadedCacheSize) {
        if (freeSpaceMin < 0) {
            freeSpaceMin = 0;
        }
        if (unDownloadedCacheSize < 0) {
            unDownloadedCacheSize = 0;
        }
        long deviceFreeSpaceAfterCaching = deviceInternalFreeSpace - unDownloadedCacheSize;
        Log.v("DeviceFreeSpaceAfterCaching: " + Utilities.convertBytesToMbAsString(deviceFreeSpaceAfterCaching));
        if (freeSpaceMin > deviceFreeSpaceAfterCaching) {
            return freeSpaceMin - deviceFreeSpaceAfterCaching;
        }
        return 0;
    }

    public long findSpaceNeededToMaintainCacheSizeMax(long cacheSizeMax, long downloadedCacheSize, long unDownloadedCacheSize) {
        if (cacheSizeMax < 0) {
            cacheSizeMax = 0;
        }
        if (downloadedCacheSize < 0) {
            downloadedCacheSize = 0;
        }
        if (unDownloadedCacheSize < 0) {
            unDownloadedCacheSize = 0;
        }
        long totalCacheSize = downloadedCacheSize + unDownloadedCacheSize;
        long spaceNeededToMaintainCacheSizeMax = 0;
        if (totalCacheSize > cacheSizeMax) {
            spaceNeededToMaintainCacheSizeMax = totalCacheSize - cacheSizeMax;
        }
        Log.v("CacheSizeMax: " + Utilities.convertBytesToMbAsString(cacheSizeMax) + " CacheSize (downloaded + undownloaded): " + Utilities.convertBytesToMbAsString(totalCacheSize) + " Free Space needed to purge " + Utilities.convertBytesToMbAsString(spaceNeededToMaintainCacheSizeMax));
        return spaceNeededToMaintainCacheSizeMax;
    }

    public void clearCache() {
        CacheDownloadManager.getInstance().stopAllDownloads();
        CacheDBManager.getInstance().deleteAllCacheFilesExceptInUse();
        CacheFileManager.cleanUpNativeXCachedDirectory();
        deleteFilesWithFileStatusDeleted();
    }

    private void deleteFilesWithFileStatusDeleted() {
        for (CacheFile cacheFile : CacheDBManager.getInstance().getCacheFilesForFileStatusDeleted()) {
            CacheFileManager.deleteFromInternalCache(cacheFile.getFileName());
        }
    }

    private void purgeFreeSpace(long freeSpaceNeeded) {
        long spaceFreed = 0;
        for (CacheFile cacheFile : CacheDBManager.getInstance().getCacheFilesForPurging()) {
            if (spaceFreed >= freeSpaceNeeded) {
                Log.v("Cleared enough space, freeSpaceNeeded:" + Utilities.convertBytesToMbAsString(freeSpaceNeeded) + ", space freed:" + Utilities.convertBytesToMbAsString(spaceFreed));
                return;
            }
            CacheDBManager.getInstance().updateFileStatusWithMD5(cacheFile.getMD5(), FileStatus.STATUS_DELETED);
            CacheFileManager.deleteFromInternalCache(cacheFile.getFileName());
            if (!CacheFileManager.doesFileExistForCacheFile(cacheFile.getFileName())) {
                spaceFreed += cacheFile.getFileSize();
            }
        }
    }

    private void updateOfferIdToCacheFiles(GetOfferCacheResponseData responseData) {
        for (int i = 0; i < responseData.getOffers().size(); i++) {
            long offerId = ((CacheOffers) responseData.getOffers().get(i)).getOfferId();
            List<CacheFile> files = ((CacheOffers) responseData.getOffers().get(i)).getFiles();
            for (int j = 0; j < files.size(); j++) {
                ((CacheFile) files.get(j)).setOfferId(offerId);
            }
        }
    }

    void updateDatabaseWithIncomingData(long offerId, List<CacheFile> cacheFiles) {
        if (cacheFiles != null) {
            syncDBRecordsWithIncomingCacheFiles(offerId, cacheFiles);
            for (CacheFile incomingCacheFile : cacheFiles) {
                String incomingCacheFileMd5 = incomingCacheFile.getMD5();
                CacheFile cacheFileInDB = CacheDBManager.getInstance().getTopOneCacheFileMatchingMD5(incomingCacheFileMd5);
                FileStatus status = FileStatus.STATUS_PENDING;
                if (cacheFileInDB == null) {
                    Log.v("File not present in DB, adding for Downloading." + incomingCacheFile.getFileName());
                    try {
                        incomingCacheFile.setFileStatus(status);
                        CacheDBManager.getInstance().addCache(incomingCacheFile);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    CacheDBManager.getInstance().updateCacheExpiryTime(incomingCacheFileMd5, incomingCacheFile.getExpiryTime());
                    FileStatus dbFileStatus = cacheFileInDB.getFileStatus();
                    if (dbFileStatus == FileStatus.STATUS_INUSE || dbFileStatus == FileStatus.STATUS_DOWNLOADING || dbFileStatus == FileStatus.STATUS_READY) {
                        status = dbFileStatus;
                    }
                    if (CacheDBManager.getInstance().isOfferIdAndRelativeUrlFoundForMD5(incomingCacheFileMd5, offerId, incomingCacheFile.getRelativeUrl())) {
                        CacheDBManager.getInstance().updateFileStatusWithMD5(incomingCacheFileMd5, status);
                    } else {
                        try {
                            incomingCacheFile.setFileStatus(status);
                            CacheDBManager.getInstance().addCache(incomingCacheFile);
                        } catch (Exception e2) {
                            e2.printStackTrace();
                        }
                    }
                }
            }
        }
    }

    void syncDBRecordsWithIncomingCacheFiles(long offerId, List<CacheFile> serverCacheFiles) {
        Set<String> dbCacheFilesMD5Set = CacheDBManager.getInstance().getMD5InOffer(offerId);
        Set<String> serverCacheFilesMD5Set = new HashSet();
        for (CacheFile serverCacheFile : serverCacheFiles) {
            serverCacheFilesMD5Set.add(serverCacheFile.getMD5());
        }
        for (String dbMD5 : dbCacheFilesMD5Set) {
            if (!serverCacheFilesMD5Set.contains(dbMD5)) {
                Log.v("Deleting Cache File from DB during syncing with server cache list for offer " + offerId + ", file " + dbMD5);
                CacheDBManager.getInstance().deleteCacheFilesMatchingOfferId(offerId, dbMD5);
            }
        }
    }

    private void initiateDownloads() {
        List<CacheFile> cacheFiles = CacheDBManager.getInstance().getCacheFilesForDownload();
        for (int i = 0; i < cacheFiles.size(); i++) {
            CacheFile cacheFile = (CacheFile) cacheFiles.get(i);
            if (CacheFileManager.doesFileExistForCacheFile(cacheFile.getFileName())) {
                CacheDBManager.getInstance().updateFileStatusWithMD5IfNotInUse(cacheFile.getMD5(), FileStatus.STATUS_READY);
            } else {
                CacheDownloadManager.getInstance().push(cacheFile);
            }
        }
    }
}
