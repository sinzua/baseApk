package com.nativex.downloadmanager;

import java.util.concurrent.LinkedBlockingQueue;

public class DownloadManager {
    private final DownloadDispatcher mDownloadDispatcher = new DownloadDispatcher(this.mDownloadQueue);
    private final LinkedBlockingQueue<DownloadRequest> mDownloadQueue = new LinkedBlockingQueue();
    private boolean mIsReleased = false;

    public DownloadManager() {
        this.mDownloadDispatcher.start();
    }

    public void requestDownload(DownloadRequest downloadRequest) throws IllegalArgumentException, IllegalStateException {
        if (downloadRequest == null) {
            throw new IllegalArgumentException("downloadRequest cannot be null");
        } else if (this.mIsReleased) {
            throw new IllegalStateException("DownloadManager was already released");
        } else {
            this.mDownloadQueue.add(downloadRequest);
        }
    }

    public void cancelAllDownloads() {
        if (!this.mIsReleased) {
            this.mDownloadQueue.clear();
            this.mDownloadDispatcher.cancelCurrentDownload();
        }
    }

    public void release() {
        if (!this.mIsReleased) {
            cancelAllDownloads();
            this.mIsReleased = true;
            this.mDownloadDispatcher.quit();
        }
    }
}
