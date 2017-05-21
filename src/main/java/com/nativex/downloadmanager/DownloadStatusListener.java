package com.nativex.downloadmanager;

public interface DownloadStatusListener {
    void onDownloadCompleted(int i);

    void onDownloadFailed(int i);

    void onDownloadStarted(int i);
}
