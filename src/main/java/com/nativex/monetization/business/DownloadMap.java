package com.nativex.monetization.business;

public class DownloadMap {
    private CacheFile cacheFile;
    private int downloadId;

    public int getDownloadId() {
        return this.downloadId;
    }

    public void setDownloadId(int downloadId) {
        this.downloadId = downloadId;
    }

    public CacheFile getCacheFile() {
        return this.cacheFile;
    }

    public void setCacheFile(CacheFile cacheFile) {
        this.cacheFile = cacheFile;
    }
}
