package com.parse;

import com.supersonicads.sdk.precache.DownloadManager;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.UUID;

class InstallationId {
    private static final String TAG = "InstallationId";
    private final File file;
    private String installationId;
    private final Object lock = new Object();

    public InstallationId(File file) {
        this.file = file;
    }

    public String get() {
        synchronized (this.lock) {
            if (this.installationId == null) {
                try {
                    this.installationId = ParseFileUtils.readFileToString(this.file, DownloadManager.UTF8_CHARSET);
                } catch (FileNotFoundException e) {
                    PLog.i(TAG, "Couldn't find existing installationId file. Creating one instead.");
                } catch (IOException e2) {
                    PLog.e(TAG, "Unexpected exception reading installation id from disk", e2);
                }
            }
            if (this.installationId == null) {
                setInternal(UUID.randomUUID().toString());
            }
        }
        return this.installationId;
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void set(java.lang.String r3) {
        /*
        r2 = this;
        r1 = r2.lock;
        monitor-enter(r1);
        r0 = com.parse.ParseTextUtils.isEmpty(r3);	 Catch:{ all -> 0x001a }
        if (r0 != 0) goto L_0x0013;
    L_0x0009:
        r0 = r2.get();	 Catch:{ all -> 0x001a }
        r0 = r3.equals(r0);	 Catch:{ all -> 0x001a }
        if (r0 == 0) goto L_0x0015;
    L_0x0013:
        monitor-exit(r1);	 Catch:{ all -> 0x001a }
    L_0x0014:
        return;
    L_0x0015:
        r2.setInternal(r3);	 Catch:{ all -> 0x001a }
        monitor-exit(r1);	 Catch:{ all -> 0x001a }
        goto L_0x0014;
    L_0x001a:
        r0 = move-exception;
        monitor-exit(r1);	 Catch:{ all -> 0x001a }
        throw r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.parse.InstallationId.set(java.lang.String):void");
    }

    private void setInternal(String newInstallationId) {
        synchronized (this.lock) {
            try {
                ParseFileUtils.writeStringToFile(this.file, newInstallationId, DownloadManager.UTF8_CHARSET);
            } catch (IOException e) {
                PLog.e(TAG, "Unexpected exception writing installation id to disk", e);
            }
            this.installationId = newInstallationId;
        }
    }

    void clear() {
        synchronized (this.lock) {
            this.installationId = null;
            ParseFileUtils.deleteQuietly(this.file);
        }
    }
}
