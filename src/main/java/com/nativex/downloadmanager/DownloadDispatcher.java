package com.nativex.downloadmanager;

import android.os.Handler;
import android.os.Looper;
import android.os.Process;
import android.support.annotation.NonNull;
import android.util.Log;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.SyncFailedException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;

class DownloadDispatcher extends Thread {
    private static final int BUFFER_SIZE = 4096;
    private static final int DEFAULT_TIMEOUT = 20000;
    private static final String LOGGING_TAG = "NativeXDownloadManager";
    private final Executor mCallBackExecutor;
    private final LinkedBlockingQueue<DownloadRequest> mDownloadQueue;
    private volatile boolean mIsCancelRequested = false;
    private volatile boolean mIsQuitRequested = false;

    public DownloadDispatcher(LinkedBlockingQueue<DownloadRequest> downloadQueue) {
        final Handler callbackHandler = new Handler(Looper.getMainLooper());
        this.mCallBackExecutor = new Executor() {
            public void execute(@NonNull Runnable command) {
                callbackHandler.post(command);
            }
        };
        this.mDownloadQueue = downloadQueue;
        setName("NativeX-CacheDownload");
    }

    public void run() {
        try {
            Process.setThreadPriority(10);
            while (!this.mIsQuitRequested) {
                try {
                    DownloadRequest currentDownloadRequest = (DownloadRequest) this.mDownloadQueue.take();
                    this.mIsCancelRequested = false;
                    downloadFile(currentDownloadRequest);
                } catch (InterruptedException e) {
                }
            }
        } catch (Exception e2) {
        }
    }

    void cancelCurrentDownload() {
        this.mIsCancelRequested = true;
    }

    void quit() {
        if (!this.mIsQuitRequested) {
            this.mIsQuitRequested = true;
            interrupt();
        }
    }

    private void downloadFile(DownloadRequest downloadRequest) {
        Exception e;
        Throwable th;
        Log.d(LOGGING_TAG, "Starting download request: " + downloadRequest.toString());
        HttpURLConnection conn = null;
        InputStream inputStream = null;
        OutputStream outputStream = null;
        FileDescriptor outputFileDescriptor = null;
        try {
            conn = (HttpURLConnection) new URL(downloadRequest.getSourceUri().toString()).openConnection();
            conn.setConnectTimeout(20000);
            conn.setReadTimeout(20000);
            cleanUpDestination(downloadRequest);
            inputStream = conn.getInputStream();
            OutputStream outputStream2 = new FileOutputStream(new File(downloadRequest.getDestinationURI().getPath()), true);
            try {
                outputFileDescriptor = ((FileOutputStream) outputStream2).getFD();
                byte[] buffer = new byte[4096];
                boolean hasDownloadStarted = false;
                boolean wasDownloadCancelled = false;
                do {
                    int bytesRead = inputStream.read(buffer);
                    if (bytesRead <= 0) {
                        break;
                    }
                    outputStream2.write(buffer, 0, bytesRead);
                    if (!hasDownloadStarted) {
                        hasDownloadStarted = true;
                        postDownloadStarted(downloadRequest);
                    }
                } while (!this.mIsCancelRequested);
                this.mIsCancelRequested = false;
                wasDownloadCancelled = true;
                if (!wasDownloadCancelled) {
                    postDownloadCompleted(downloadRequest);
                    Log.d(LOGGING_TAG, "Finished download " + downloadRequest.getDownloadId());
                }
                if (conn != null) {
                    conn.disconnect();
                }
                if (inputStream != null) {
                    try {
                        inputStream.close();
                    } catch (IOException e2) {
                    }
                }
                if (outputStream2 != null) {
                    try {
                        outputStream2.close();
                    } catch (IOException e3) {
                    }
                }
                if (outputFileDescriptor != null) {
                    try {
                        outputFileDescriptor.sync();
                        outputStream = outputStream2;
                        return;
                    } catch (SyncFailedException e4) {
                        outputStream = outputStream2;
                        return;
                    }
                }
            } catch (Exception e5) {
                e = e5;
                outputStream = outputStream2;
                try {
                    Log.d(LOGGING_TAG, String.format("Download %d failed: %s", new Object[]{Integer.valueOf(downloadRequest.getDownloadId()), e.getMessage()}));
                    cleanUpDestination(downloadRequest);
                    postDownloadFailed(downloadRequest);
                    if (conn != null) {
                        conn.disconnect();
                    }
                    if (inputStream != null) {
                        try {
                            inputStream.close();
                        } catch (IOException e6) {
                        }
                    }
                    if (outputStream != null) {
                        try {
                            outputStream.close();
                        } catch (IOException e7) {
                        }
                    }
                    if (outputFileDescriptor != null) {
                        try {
                            outputFileDescriptor.sync();
                        } catch (SyncFailedException e8) {
                        }
                    }
                } catch (Throwable th2) {
                    th = th2;
                    if (conn != null) {
                        conn.disconnect();
                    }
                    if (inputStream != null) {
                        try {
                            inputStream.close();
                        } catch (IOException e9) {
                        }
                    }
                    if (outputStream != null) {
                        try {
                            outputStream.close();
                        } catch (IOException e10) {
                        }
                    }
                    if (outputFileDescriptor != null) {
                        try {
                            outputFileDescriptor.sync();
                        } catch (SyncFailedException e11) {
                        }
                    }
                    throw th;
                }
            } catch (Throwable th3) {
                th = th3;
                outputStream = outputStream2;
                if (conn != null) {
                    conn.disconnect();
                }
                if (inputStream != null) {
                    inputStream.close();
                }
                if (outputStream != null) {
                    outputStream.close();
                }
                if (outputFileDescriptor != null) {
                    outputFileDescriptor.sync();
                }
                throw th;
            }
        } catch (Exception e12) {
            e = e12;
            Log.d(LOGGING_TAG, String.format("Download %d failed: %s", new Object[]{Integer.valueOf(downloadRequest.getDownloadId()), e.getMessage()}));
            cleanUpDestination(downloadRequest);
            postDownloadFailed(downloadRequest);
            if (conn != null) {
                conn.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
            if (outputStream != null) {
                outputStream.close();
            }
            if (outputFileDescriptor != null) {
                outputFileDescriptor.sync();
            }
        }
    }

    private void cleanUpDestination(DownloadRequest downloadRequest) {
        File destinationFile = new File(downloadRequest.getDestinationURI().getPath());
        if (destinationFile.exists()) {
            destinationFile.delete();
        }
    }

    private void postDownloadStarted(final DownloadRequest downloadRequest) {
        this.mCallBackExecutor.execute(new Runnable() {
            public void run() {
                try {
                    DownloadStatusListener listener = downloadRequest.getDownloadListener();
                    if (listener != null) {
                        listener.onDownloadStarted(downloadRequest.getDownloadId());
                    }
                } catch (Exception e) {
                }
            }
        });
    }

    private void postDownloadCompleted(final DownloadRequest downloadRequest) {
        this.mCallBackExecutor.execute(new Runnable() {
            public void run() {
                try {
                    DownloadStatusListener listener = downloadRequest.getDownloadListener();
                    if (listener != null) {
                        listener.onDownloadCompleted(downloadRequest.getDownloadId());
                    }
                } catch (Exception e) {
                }
            }
        });
    }

    private void postDownloadFailed(final DownloadRequest downloadRequest) {
        this.mCallBackExecutor.execute(new Runnable() {
            public void run() {
                try {
                    DownloadStatusListener listener = downloadRequest.getDownloadListener();
                    if (listener != null) {
                        listener.onDownloadFailed(downloadRequest.getDownloadId());
                    }
                } catch (Exception e) {
                }
            }
        });
    }
}
