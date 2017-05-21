package com.nativex.monetization.manager;

import android.os.Handler;
import android.os.HandlerThread;
import com.nativex.common.Log;
import com.nativex.common.SharedPreferenceManager;
import com.nativex.common.Utilities;
import com.nativex.monetization.business.CacheFile;
import com.nativex.monetization.business.GetOfferCacheResponseData;
import com.nativex.monetization.communication.ServerRequestManager;
import com.nativex.monetization.enums.FileStatus;
import com.nativex.monetization.listeners.OnGetCacheDownloadCompletedListener;
import com.nativex.monetization.tasks.CachingTask;
import com.supersonicads.sdk.utils.Constants.ControllerParameters;
import java.util.Set;

public class CacheManager {
    private static final Object extendedCacheOperationLock = new Object();
    private static HandlerThread handlerThread;
    private static CacheManager instance;
    private static boolean isGetOfferCacheRequestRunning = false;
    private static Runnable restartCachingRunnable;
    private static final Object singletonLock = new Object();
    private static Runnable stopCachingRunnable;
    private static Handler threadHandler;

    private static class restartCachingRunnable implements Runnable {
        private restartCachingRunnable() {
        }

        public void run() {
            try {
                CacheManager.actualRestartCaching();
            } catch (Exception e) {
                Log.e("Unhandled exception", e);
            }
        }
    }

    private static class stopCachingRunnable implements Runnable {
        private stopCachingRunnable() {
        }

        public void run() {
            try {
                CacheManager.actualStopCaching();
            } catch (Exception e) {
                Log.e("Unhandled exception", e);
            }
        }
    }

    private CacheManager() {
        handlerThread = new HandlerThread("CachingTask-Thread");
        handlerThread.start();
        threadHandler = new Handler(handlerThread.getLooper());
        restartCachingRunnable = new restartCachingRunnable();
        stopCachingRunnable = new stopCachingRunnable();
    }

    public static CacheManager getInstance() {
        if (instance == null) {
            synchronized (singletonLock) {
                if (instance == null) {
                    instance = new CacheManager();
                }
            }
        }
        return instance;
    }

    public static void restartOrStopCaching(int cachingFrequency) {
        if (cachingFrequency <= 0) {
            stopCaching();
            return;
        }
        long previousCachedTime = SharedPreferenceManager.getPreviousCachedTime();
        if (previousCachedTime == 0 || System.currentTimeMillis() > ((long) ((cachingFrequency * 60) * ControllerParameters.SECOND)) + previousCachedTime) {
            SharedPreferenceManager.storePreviousCachedTime(System.currentTimeMillis());
            restartCaching();
            return;
        }
        Log.v("Caching is skipped as current time has not elapsed caching frequency wait time.");
    }

    public static void restartCaching() {
        if (threadHandler != null) {
            threadHandler.removeCallbacks(restartCachingRunnable);
            threadHandler.post(restartCachingRunnable);
            return;
        }
        Log.e("Caching couldn't be started due to unavailability of 'threadHandler' instance.");
    }

    private static void actualRestartCaching() {
        if (MonetizationSharedDataManager.getContext() != null) {
            actualStopCaching();
            getOfferCache();
            Log.v("Caching started");
            return;
        }
        Log.v("Caching couldn't be started due to unavailability of 'Context' instance.");
    }

    public static void stopCaching() {
        if (threadHandler != null) {
            threadHandler.removeCallbacks(stopCachingRunnable);
            threadHandler.post(stopCachingRunnable);
        }
    }

    private static void actualStopCaching() {
        if (instance != null) {
            CacheDownloadManager.getInstance().stopAllDownloads();
        }
        CacheFileManager.cleanUpNativeXCachedDirectory();
        CacheFileManager.cleanUpCacheDB();
        Log.v("Caching stopped");
    }

    public static void release() {
        actualStopCaching();
        actualRelease();
    }

    private static void actualRelease() {
        CacheDownloadManager.release();
        CacheDBManager.release();
        if (handlerThread != null) {
            handlerThread.quit();
        }
        handlerThread = null;
        threadHandler = null;
        stopCachingRunnable = null;
        instance = null;
    }

    private static void getOfferCache() {
        if (isGetOfferCacheRequestRunning) {
            Log.d("Skipping GetOfferCache since one is already running");
            return;
        }
        isGetOfferCacheRequestRunning = true;
        ServerRequestManager.getInstance().getOfferCache(new OnGetCacheDownloadCompletedListener() {
            public void downloadComplete(GetOfferCacheResponseData cacheResponseData) {
                if (cacheResponseData != null) {
                    try {
                        CacheManager.threadHandler.post(new CachingTask(cacheResponseData));
                    } catch (IllegalThreadStateException e) {
                        Log.e("Exception in starting caching task.");
                        e.printStackTrace();
                    }
                }
                CacheManager.isGetOfferCacheRequestRunning = false;
            }
        });
    }

    public boolean isOfferCacheAvailable() {
        long freeSpaceMinInMegaBytes = CacheDBManager.getInstance().getCacheUtilIntegerFreeSpaceMin();
        long freeSpaceMinInBytes = Utilities.convertMBtoBytes(freeSpaceMinInMegaBytes);
        if (freeSpaceMinInMegaBytes != 0 && CacheFileManager.getAvailableInternalMemorySizeInBytes() < freeSpaceMinInBytes) {
            return false;
        }
        return true;
    }

    public void verifyFileStatusForAllRecords() {
        for (CacheFile cacheFile : CacheDBManager.getInstance().getAllCacheFilesByMD5()) {
            if (CacheFileManager.doesFileExistForCacheFile(cacheFile.getFileName())) {
                if (cacheFile.getFileStatus() == FileStatus.STATUS_DELETED) {
                    Log.e("Wrong file status found for DELETED " + cacheFile.getMD5());
                    CacheFileManager.deleteFromInternalCache(cacheFile.getFileName());
                } else {
                    CacheDBManager.getInstance().updateFileStatusWithMD5(cacheFile.getMD5(), FileStatus.STATUS_READY);
                }
            }
        }
    }

    public void checkToUpdateStatusToReady(Set<String> releasedAdMD5List, Set<String> allOtherAdsMD5List) {
        for (String releasedAdMd5 : releasedAdMD5List) {
            boolean updateStatusToReady = true;
            if (allOtherAdsMD5List.contains(releasedAdMd5)) {
                updateStatusToReady = false;
            }
            if (updateStatusToReady) {
                CacheDBManager.getInstance().updateFileStatusWithMD5(releasedAdMd5, FileStatus.STATUS_READY);
            }
        }
    }

    public static Object getExtendedOperationLock() {
        return extendedCacheOperationLock;
    }
}
