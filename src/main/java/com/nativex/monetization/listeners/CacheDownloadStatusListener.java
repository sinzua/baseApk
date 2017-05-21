package com.nativex.monetization.listeners;

import com.nativex.downloadmanager.DownloadStatusListener;
import com.nativex.monetization.business.CacheFile;
import com.nativex.monetization.enums.FileStatus;
import com.nativex.monetization.manager.CacheDBManager;

public class CacheDownloadStatusListener implements DownloadStatusListener {
    public void onDownloadStarted(int downloadId) {
        CacheFile cacheFile = CacheDBManager.getInstance().getCacheFileForDownloadId(downloadId);
        if (cacheFile != null) {
            CacheDBManager.getInstance().updateFileStatusWithMD5IfNotInUse(cacheFile.getMD5(), FileStatus.STATUS_DOWNLOADING);
        }
    }

    public void onDownloadCompleted(int downloadId) {
        CacheFile cacheFile = CacheDBManager.getInstance().getCacheFileForDownloadId(downloadId);
        if (cacheFile != null) {
            CacheDBManager.getInstance().updateFileStatusWithMD5IfNotInUse(cacheFile.getMD5(), FileStatus.STATUS_READY);
            CacheDBManager.getInstance().deleteDownloadId(downloadId);
        }
    }

    public void onDownloadFailed(int downloadId) {
        CacheFile cacheFile = CacheDBManager.getInstance().getCacheFileForDownloadId(downloadId);
        if (cacheFile != null) {
            CacheDBManager.getInstance().updateFileStatusWithMD5IfNotInUse(cacheFile.getMD5(), FileStatus.STATUS_FAILED);
            CacheDBManager.getInstance().deleteDownloadId(downloadId);
        }
    }
}
