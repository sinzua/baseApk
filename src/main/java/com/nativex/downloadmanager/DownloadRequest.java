package com.nativex.downloadmanager;

import android.net.Uri;
import com.supersonicads.sdk.utils.Constants.RequestParameters;

public class DownloadRequest {
    private Uri mDestinationUri;
    private int mDownloadId;
    private DownloadStatusListener mDownloadListener;
    private final Uri mSourceUri;

    public DownloadRequest(int downloadId, Uri sourceUri, Uri destinationUri, DownloadStatusListener downloadListener) {
        if (sourceUri == null) {
            throw new NullPointerException("Download source Uri cannot be null");
        } else if (destinationUri == null) {
            throw new NullPointerException("Destination Uri cannot be null");
        } else {
            String scheme = sourceUri.getScheme();
            if (scheme == null || !(scheme.equals("http") || scheme.equals("https"))) {
                throw new IllegalArgumentException("Can only download HTTP/HTTPS URIs: " + sourceUri);
            }
            this.mDownloadId = downloadId;
            this.mSourceUri = sourceUri;
            this.mDestinationUri = destinationUri;
            this.mDownloadListener = downloadListener;
        }
    }

    public String toString() {
        StringBuilder downloadString = new StringBuilder();
        downloadString.append(RequestParameters.LEFT_BRACKETS);
        downloadString.append("DownloadId:");
        downloadString.append(this.mDownloadId);
        downloadString.append("; ");
        downloadString.append("Source URI:");
        downloadString.append(this.mSourceUri.toString());
        downloadString.append("; ");
        downloadString.append("Destination Path:");
        downloadString.append(this.mDestinationUri.toString());
        downloadString.append("; ");
        downloadString.append("Thread:");
        downloadString.append(Thread.currentThread().getName());
        downloadString.append(RequestParameters.RIGHT_BRACKETS);
        return downloadString.toString();
    }

    Uri getSourceUri() {
        return this.mSourceUri;
    }

    Uri getDestinationURI() {
        return this.mDestinationUri;
    }

    final int getDownloadId() {
        return this.mDownloadId;
    }

    DownloadStatusListener getDownloadListener() {
        return this.mDownloadListener;
    }
}
