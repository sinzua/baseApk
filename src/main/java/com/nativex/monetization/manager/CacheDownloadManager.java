package com.nativex.monetization.manager;

import android.net.Uri;
import com.nativex.common.Log;
import com.nativex.downloadmanager.DownloadManager;
import com.nativex.downloadmanager.DownloadRequest;
import com.nativex.monetization.business.CacheFile;
import com.nativex.monetization.business.DownloadMap;
import com.nativex.monetization.enums.FileStatus;
import com.nativex.monetization.listeners.CacheDownloadStatusListener;
import java.io.File;

public class CacheDownloadManager {
    private static CacheDownloadStatusListener downloadListener;
    private static DownloadManager downloadManager;
    private static CacheDownloadManager instance;
    private static final Object singletonLock = new Object();

    public static CacheDownloadManager getInstance() {
        if (instance == null) {
            synchronized (singletonLock) {
                if (instance == null) {
                    instance = new CacheDownloadManager();
                }
            }
        }
        return instance;
    }

    private CacheDownloadManager() {
        downloadManager = new DownloadManager();
        downloadListener = new CacheDownloadStatusListener();
    }

    public static void release() {
        if (downloadManager != null) {
            downloadManager.release();
        }
        downloadManager = null;
        instance = null;
    }

    public void push(CacheFile cacheFile) {
        String downloadUrl = cacheFile.getDownloadUrl();
        String fileName = cacheFile.getFileName();
        downloadManager.requestDownload(new DownloadRequest(CacheDBManager.getInstance().addDownloadId(cacheFile), Uri.parse(downloadUrl), Uri.fromFile(new File(CacheFileManager.getNativeXCacheDirectoryPath() + fileName)), downloadListener));
    }

    public synchronized void stopAllDownloads() {
        Log.v(" Stopping All Running Downloads ");
        downloadManager.cancelAllDownloads();
        for (DownloadMap downloadMap : CacheDBManager.getInstance().getDownloadMap()) {
            CacheDBManager.getInstance().updateFileStatusWithMD5IfNotInUse(downloadMap.getCacheFile().getMD5(), FileStatus.STATUS_PENDING);
            CacheDBManager.getInstance().deleteDownloadId(downloadMap.getDownloadId());
        }
    }
}
