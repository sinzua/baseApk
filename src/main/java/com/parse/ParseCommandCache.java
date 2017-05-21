package com.parse;

import android.content.Context;
import android.content.Intent;
import bolts.Capture;
import bolts.Continuation;
import bolts.Task;
import bolts.Task.TaskCompletionSource;
import com.lidroid.xutils.bitmap.BitmapGlobalConfig;
import com.parse.ConnectivityNotifier.ConnectivityListener;
import java.io.File;
import java.util.HashMap;
import java.util.logging.Logger;
import org.json.JSONObject;

class ParseCommandCache extends ParseEventuallyQueue {
    private static final String TAG = "com.parse.ParseCommandCache";
    private static int filenameCounter = 0;
    private static final Object lock = new Object();
    private File cachePath;
    ConnectivityListener listener = new ConnectivityListener() {
        public void networkConnectivityStatusChanged(Context context, Intent intent) {
            if (intent.getBooleanExtra("noConnectivity", false)) {
                ParseCommandCache.this.setConnected(false);
            } else {
                ParseCommandCache.this.setConnected(ConnectivityNotifier.isConnected(context));
            }
        }
    };
    private Logger log;
    private int maxCacheSizeBytes = BitmapGlobalConfig.MIN_DISK_CACHE_SIZE;
    ConnectivityNotifier notifier;
    private HashMap<File, TaskCompletionSource> pendingTasks = new HashMap();
    private boolean running;
    private final Object runningLock;
    private boolean shouldStop;
    private int timeoutMaxRetries = 5;
    private double timeoutRetryWaitSeconds = 600.0d;
    private boolean unprocessedCommandsExist;

    private void runLoop() {
        /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.JadxRuntimeException: Can't find block by offset: 0x0078 in list []
	at jadx.core.utils.BlockUtils.getBlockByOffset(BlockUtils.java:42)
	at jadx.core.dex.instructions.IfNode.initBlocks(IfNode.java:60)
	at jadx.core.dex.visitors.blocksmaker.BlockFinish.initBlocksInIfNodes(BlockFinish.java:48)
	at jadx.core.dex.visitors.blocksmaker.BlockFinish.visit(BlockFinish.java:33)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:31)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.ProcessClass.process(ProcessClass.java:37)
	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:59)
	at jadx.core.ProcessClass.process(ProcessClass.java:42)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:306)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
*/
        /*
        r9 = this;
        r8 = 4;
        r3 = 0;
        r2 = 1;
        r4 = com.parse.Parse.getLogLevel();
        if (r8 < r4) goto L_0x0010;
    L_0x0009:
        r4 = r9.log;
        r5 = "Parse command cache has started processing queued commands.";
        r4.info(r5);
    L_0x0010:
        r4 = r9.runningLock;
        monitor-enter(r4);
        r5 = r9.running;
        if (r5 == 0) goto L_0x0019;
    L_0x0017:
        monitor-exit(r4);
    L_0x0018:
        return;
    L_0x0019:
        r5 = 1;
        r9.running = r5;
        r5 = r9.runningLock;
        r5.notifyAll();
        monitor-exit(r4);
        r4 = lock;
        monitor-enter(r4);
        r5 = r9.shouldStop;
        if (r5 != 0) goto L_0x0055;
    L_0x0029:
        r5 = java.lang.Thread.interrupted();
        if (r5 != 0) goto L_0x0055;
    L_0x002f:
        r1 = r2;
    L_0x0030:
        monitor-exit(r4);
    L_0x0031:
        if (r1 == 0) goto L_0x0083;
    L_0x0033:
        r5 = lock;
        monitor-enter(r5);
        r4 = r9.timeoutMaxRetries;	 Catch:{ Exception -> 0x005f, all -> 0x007a }
        r9.maybeRunAllCommandsNow(r4);	 Catch:{ Exception -> 0x005f, all -> 0x007a }
        r4 = r9.shouldStop;	 Catch:{ Exception -> 0x005f, all -> 0x007a }
        if (r4 != 0) goto L_0x0048;
    L_0x003f:
        r4 = r9.unprocessedCommandsExist;	 Catch:{ InterruptedException -> 0x005a }
        if (r4 != 0) goto L_0x0048;	 Catch:{ InterruptedException -> 0x005a }
    L_0x0043:
        r4 = lock;	 Catch:{ InterruptedException -> 0x005a }
        r4.wait();	 Catch:{ InterruptedException -> 0x005a }
    L_0x0048:
        r4 = r9.shouldStop;	 Catch:{ Exception -> 0x005f, all -> 0x007a }
        if (r4 != 0) goto L_0x0076;	 Catch:{ Exception -> 0x005f, all -> 0x007a }
    L_0x004c:
        r1 = r2;	 Catch:{ Exception -> 0x005f, all -> 0x007a }
    L_0x004d:
        monitor-exit(r5);	 Catch:{ Exception -> 0x005f, all -> 0x007a }
        goto L_0x0031;	 Catch:{ Exception -> 0x005f, all -> 0x007a }
    L_0x004f:
        r2 = move-exception;
        monitor-exit(r5);	 Catch:{ Exception -> 0x005f, all -> 0x007a }
        throw r2;
    L_0x0052:
        r2 = move-exception;
        monitor-exit(r4);
        throw r2;
    L_0x0055:
        r1 = r3;
        goto L_0x0030;
    L_0x0057:
        r2 = move-exception;
        monitor-exit(r4);
        throw r2;
    L_0x005a:
        r0 = move-exception;
        r4 = 1;
        r9.shouldStop = r4;	 Catch:{ Exception -> 0x005f, all -> 0x007a }
        goto L_0x0048;
    L_0x005f:
        r0 = move-exception;
        r4 = 6;
        r6 = com.parse.Parse.getLogLevel();	 Catch:{ Exception -> 0x005f, all -> 0x007a }
        if (r4 < r6) goto L_0x0070;	 Catch:{ Exception -> 0x005f, all -> 0x007a }
    L_0x0067:
        r4 = r9.log;	 Catch:{ Exception -> 0x005f, all -> 0x007a }
        r6 = java.util.logging.Level.SEVERE;	 Catch:{ Exception -> 0x005f, all -> 0x007a }
        r7 = "saveEventually thread had an error.";	 Catch:{ Exception -> 0x005f, all -> 0x007a }
        r4.log(r6, r7, r0);	 Catch:{ Exception -> 0x005f, all -> 0x007a }
    L_0x0070:
        r4 = r9.shouldStop;
        if (r4 != 0) goto L_0x0078;
    L_0x0074:
        r1 = r2;
    L_0x0075:
        goto L_0x004d;	 Catch:{ Exception -> 0x005f, all -> 0x007a }
    L_0x0076:
        r1 = r3;	 Catch:{ Exception -> 0x005f, all -> 0x007a }
        goto L_0x004d;	 Catch:{ Exception -> 0x005f, all -> 0x007a }
    L_0x0078:
        r1 = r3;
        goto L_0x0075;	 Catch:{ Exception -> 0x005f, all -> 0x007a }
    L_0x007a:
        r4 = move-exception;	 Catch:{ Exception -> 0x005f, all -> 0x007a }
        r6 = r9.shouldStop;	 Catch:{ Exception -> 0x005f, all -> 0x007a }
        if (r6 != 0) goto L_0x0081;	 Catch:{ Exception -> 0x005f, all -> 0x007a }
    L_0x007f:
        r1 = r2;	 Catch:{ Exception -> 0x005f, all -> 0x007a }
    L_0x0080:
        throw r4;	 Catch:{ Exception -> 0x005f, all -> 0x007a }
    L_0x0081:
        r1 = r3;
        goto L_0x0080;
    L_0x0083:
        r3 = r9.runningLock;
        monitor-enter(r3);
        r2 = 0;
        r9.running = r2;
        r2 = r9.runningLock;
        r2.notifyAll();
        monitor-exit(r3);
        r2 = com.parse.Parse.getLogLevel();
        if (r8 < r2) goto L_0x0018;
    L_0x0095:
        r2 = r9.log;
        r3 = "saveEventually thread has stopped processing commands.";
        r2.info(r3);
        goto L_0x0018;
    L_0x009e:
        r2 = move-exception;
        monitor-exit(r3);
        throw r2;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.parse.ParseCommandCache.runLoop():void");
    }

    private static File getCacheDir() {
        File cacheDir = new File(Parse.getParseDir(), "CommandCache");
        cacheDir.mkdirs();
        return cacheDir;
    }

    public static int getPendingCount() {
        int length;
        synchronized (lock) {
            String[] files = getCacheDir().list();
            length = files == null ? 0 : files.length;
        }
        return length;
    }

    public ParseCommandCache(Context context) {
        setConnected(false);
        this.shouldStop = false;
        this.running = false;
        this.runningLock = new Object();
        this.log = Logger.getLogger(TAG);
        this.cachePath = getCacheDir();
        if (Parse.hasPermission("android.permission.ACCESS_NETWORK_STATE")) {
            setConnected(ConnectivityNotifier.isConnected(context));
            this.notifier = ConnectivityNotifier.getNotifier(context);
            this.notifier.addListener(this.listener);
            resume();
        }
    }

    public void onDestroy() {
        this.notifier.removeListener(this.listener);
    }

    public void setTimeoutMaxRetries(int tries) {
        synchronized (lock) {
            this.timeoutMaxRetries = tries;
        }
    }

    public void setTimeoutRetryWaitSeconds(double seconds) {
        synchronized (lock) {
            this.timeoutRetryWaitSeconds = seconds;
        }
    }

    public void setMaxCacheSizeBytes(int bytes) {
        synchronized (lock) {
            this.maxCacheSizeBytes = bytes;
        }
    }

    public void resume() {
        synchronized (this.runningLock) {
            if (!this.running) {
                new Thread("ParseCommandCache.runLoop()") {
                    public void run() {
                        ParseCommandCache.this.runLoop();
                    }
                }.start();
                try {
                    this.runningLock.wait();
                } catch (InterruptedException e) {
                    synchronized (lock) {
                        this.shouldStop = true;
                        lock.notifyAll();
                    }
                }
            }
        }
    }

    public void pause() {
        synchronized (this.runningLock) {
            if (this.running) {
                synchronized (lock) {
                    this.shouldStop = true;
                    lock.notifyAll();
                }
            }
            while (this.running) {
                try {
                    this.runningLock.wait();
                } catch (InterruptedException e) {
                }
            }
        }
    }

    private void removeFile(File file) {
        synchronized (lock) {
            this.pendingTasks.remove(file);
            try {
                commandFromJSON(ParseFileUtils.readFileToJSONObject(file)).releaseLocalIds();
            } catch (Exception e) {
            }
            ParseFileUtils.deleteQuietly(file);
        }
    }

    void simulateReboot() {
        synchronized (lock) {
            this.pendingTasks.clear();
        }
    }

    void fakeObjectUpdate() {
        notifyTestHelper(3);
        notifyTestHelper(1);
        notifyTestHelper(5);
    }

    public Task<JSONObject> enqueueEventuallyAsync(ParseRESTCommand command, ParseObject object) {
        return enqueueEventuallyAsync(command, false, object);
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private bolts.Task<org.json.JSONObject> enqueueEventuallyAsync(com.parse.ParseRESTCommand r27, boolean r28, com.parse.ParseObject r29) {
        /*
        r26 = this;
        r22 = "android.permission.ACCESS_NETWORK_STATE";
        com.parse.Parse.requirePermission(r22);
        r20 = bolts.Task.create();
        if (r29 == 0) goto L_0x001c;
    L_0x000b:
        r22 = r29.getObjectId();	 Catch:{ UnsupportedEncodingException -> 0x0060 }
        if (r22 != 0) goto L_0x001c;
    L_0x0011:
        r22 = r29.getOrCreateLocalId();	 Catch:{ UnsupportedEncodingException -> 0x0060 }
        r0 = r27;
        r1 = r22;
        r0.setLocalId(r1);	 Catch:{ UnsupportedEncodingException -> 0x0060 }
    L_0x001c:
        r13 = r27.toJSONObject();	 Catch:{ UnsupportedEncodingException -> 0x0060 }
        r22 = r13.toString();	 Catch:{ UnsupportedEncodingException -> 0x0060 }
        r23 = "UTF-8";
        r12 = r22.getBytes(r23);	 Catch:{ UnsupportedEncodingException -> 0x0060 }
        r0 = r12.length;
        r22 = r0;
        r0 = r26;
        r0 = r0.maxCacheSizeBytes;
        r23 = r0;
        r0 = r22;
        r1 = r23;
        if (r0 <= r1) goto L_0x0090;
    L_0x0039:
        r22 = 5;
        r23 = com.parse.Parse.getLogLevel();
        r0 = r22;
        r1 = r23;
        if (r0 < r1) goto L_0x0050;
    L_0x0045:
        r0 = r26;
        r0 = r0.log;
        r22 = r0;
        r23 = "Unable to save command for later because it's too big.";
        r22.warning(r23);
    L_0x0050:
        r22 = 4;
        r0 = r26;
        r1 = r22;
        r0.notifyTestHelper(r1);
        r22 = 0;
        r22 = bolts.Task.forResult(r22);
    L_0x005f:
        return r22;
    L_0x0060:
        r5 = move-exception;
        r22 = 5;
        r23 = com.parse.Parse.getLogLevel();
        r0 = r22;
        r1 = r23;
        if (r0 < r1) goto L_0x0080;
    L_0x006d:
        r0 = r26;
        r0 = r0.log;
        r22 = r0;
        r23 = java.util.logging.Level.WARNING;
        r24 = "UTF-8 isn't supported.  This shouldn't happen.";
        r0 = r22;
        r1 = r23;
        r2 = r24;
        r0.log(r1, r2, r5);
    L_0x0080:
        r22 = 4;
        r0 = r26;
        r1 = r22;
        r0.notifyTestHelper(r1);
        r22 = 0;
        r22 = bolts.Task.forResult(r22);
        goto L_0x005f;
    L_0x0090:
        r23 = lock;
        monitor-enter(r23);
        r0 = r26;
        r0 = r0.cachePath;	 Catch:{ IOException -> 0x0263 }
        r22 = r0;
        r8 = r22.list();	 Catch:{ IOException -> 0x0263 }
        if (r8 == 0) goto L_0x015a;
    L_0x009f:
        java.util.Arrays.sort(r8);	 Catch:{ IOException -> 0x0263 }
        r19 = 0;
        r4 = r8;
        r14 = r4.length;	 Catch:{ IOException -> 0x0263 }
        r9 = 0;
    L_0x00a7:
        if (r9 >= r14) goto L_0x00c6;
    L_0x00a9:
        r7 = r4[r9];	 Catch:{ IOException -> 0x0263 }
        r6 = new java.io.File;	 Catch:{ IOException -> 0x0263 }
        r0 = r26;
        r0 = r0.cachePath;	 Catch:{ IOException -> 0x0263 }
        r22 = r0;
        r0 = r22;
        r6.<init>(r0, r7);	 Catch:{ IOException -> 0x0263 }
        r24 = r6.length();	 Catch:{ IOException -> 0x0263 }
        r0 = r24;
        r0 = (int) r0;	 Catch:{ IOException -> 0x0263 }
        r22 = r0;
        r19 = r19 + r22;
        r9 = r9 + 1;
        goto L_0x00a7;
    L_0x00c6:
        r0 = r12.length;	 Catch:{ IOException -> 0x0263 }
        r22 = r0;
        r19 = r19 + r22;
        r0 = r26;
        r0 = r0.maxCacheSizeBytes;	 Catch:{ IOException -> 0x0263 }
        r22 = r0;
        r0 = r19;
        r1 = r22;
        if (r0 <= r1) goto L_0x015a;
    L_0x00d7:
        if (r28 == 0) goto L_0x0105;
    L_0x00d9:
        r22 = 5;
        r24 = com.parse.Parse.getLogLevel();	 Catch:{ IOException -> 0x0263 }
        r0 = r22;
        r1 = r24;
        if (r0 < r1) goto L_0x00f4;
    L_0x00e5:
        r0 = r26;
        r0 = r0.log;	 Catch:{ IOException -> 0x0263 }
        r22 = r0;
        r24 = "Unable to save command for later because storage is full.";
        r0 = r22;
        r1 = r24;
        r0.warning(r1);	 Catch:{ IOException -> 0x0263 }
    L_0x00f4:
        r22 = 0;
        r22 = bolts.Task.forResult(r22);	 Catch:{ IOException -> 0x0263 }
        r24 = lock;	 Catch:{ all -> 0x0102 }
        r24.notifyAll();	 Catch:{ all -> 0x0102 }
        monitor-exit(r23);	 Catch:{ all -> 0x0102 }
        goto L_0x005f;
    L_0x0102:
        r22 = move-exception;
        monitor-exit(r23);	 Catch:{ all -> 0x0102 }
        throw r22;
    L_0x0105:
        r22 = 5;
        r24 = com.parse.Parse.getLogLevel();	 Catch:{ IOException -> 0x0263 }
        r0 = r22;
        r1 = r24;
        if (r0 < r1) goto L_0x0120;
    L_0x0111:
        r0 = r26;
        r0 = r0.log;	 Catch:{ IOException -> 0x0263 }
        r22 = r0;
        r24 = "Deleting old commands to make room in command cache.";
        r0 = r22;
        r1 = r24;
        r0.warning(r1);	 Catch:{ IOException -> 0x0263 }
    L_0x0120:
        r10 = 0;
        r11 = r10;
    L_0x0122:
        r0 = r26;
        r0 = r0.maxCacheSizeBytes;	 Catch:{ IOException -> 0x0263 }
        r22 = r0;
        r0 = r19;
        r1 = r22;
        if (r0 <= r1) goto L_0x015a;
    L_0x012e:
        r0 = r8.length;	 Catch:{ IOException -> 0x0263 }
        r22 = r0;
        r0 = r22;
        if (r11 >= r0) goto L_0x015a;
    L_0x0135:
        r6 = new java.io.File;	 Catch:{ IOException -> 0x0263 }
        r0 = r26;
        r0 = r0.cachePath;	 Catch:{ IOException -> 0x0263 }
        r22 = r0;
        r10 = r11 + 1;
        r24 = r8[r11];	 Catch:{ IOException -> 0x0263 }
        r0 = r22;
        r1 = r24;
        r6.<init>(r0, r1);	 Catch:{ IOException -> 0x0263 }
        r24 = r6.length();	 Catch:{ IOException -> 0x0263 }
        r0 = r24;
        r0 = (int) r0;	 Catch:{ IOException -> 0x0263 }
        r22 = r0;
        r19 = r19 - r22;
        r0 = r26;
        r0.removeFile(r6);	 Catch:{ IOException -> 0x0263 }
        r11 = r10;
        goto L_0x0122;
    L_0x015a:
        r24 = java.lang.System.currentTimeMillis();	 Catch:{ IOException -> 0x0263 }
        r17 = java.lang.Long.toHexString(r24);	 Catch:{ IOException -> 0x0263 }
        r22 = r17.length();	 Catch:{ IOException -> 0x0263 }
        r24 = 16;
        r0 = r22;
        r1 = r24;
        if (r0 >= r1) goto L_0x01a1;
    L_0x016e:
        r22 = r17.length();	 Catch:{ IOException -> 0x0263 }
        r22 = 16 - r22;
        r0 = r22;
        r0 = new char[r0];	 Catch:{ IOException -> 0x0263 }
        r21 = r0;
        r22 = 48;
        java.util.Arrays.fill(r21, r22);	 Catch:{ IOException -> 0x0263 }
        r22 = new java.lang.StringBuilder;	 Catch:{ IOException -> 0x0263 }
        r22.<init>();	 Catch:{ IOException -> 0x0263 }
        r24 = new java.lang.String;	 Catch:{ IOException -> 0x0263 }
        r0 = r24;
        r1 = r21;
        r0.<init>(r1);	 Catch:{ IOException -> 0x0263 }
        r0 = r22;
        r1 = r24;
        r22 = r0.append(r1);	 Catch:{ IOException -> 0x0263 }
        r0 = r22;
        r1 = r17;
        r22 = r0.append(r1);	 Catch:{ IOException -> 0x0263 }
        r17 = r22.toString();	 Catch:{ IOException -> 0x0263 }
    L_0x01a1:
        r22 = filenameCounter;	 Catch:{ IOException -> 0x0263 }
        r24 = r22 + 1;
        filenameCounter = r24;	 Catch:{ IOException -> 0x0263 }
        r18 = java.lang.Integer.toHexString(r22);	 Catch:{ IOException -> 0x0263 }
        r22 = r18.length();	 Catch:{ IOException -> 0x0263 }
        r24 = 8;
        r0 = r22;
        r1 = r24;
        if (r0 >= r1) goto L_0x01ea;
    L_0x01b7:
        r22 = r18.length();	 Catch:{ IOException -> 0x0263 }
        r22 = 8 - r22;
        r0 = r22;
        r0 = new char[r0];	 Catch:{ IOException -> 0x0263 }
        r21 = r0;
        r22 = 48;
        java.util.Arrays.fill(r21, r22);	 Catch:{ IOException -> 0x0263 }
        r22 = new java.lang.StringBuilder;	 Catch:{ IOException -> 0x0263 }
        r22.<init>();	 Catch:{ IOException -> 0x0263 }
        r24 = new java.lang.String;	 Catch:{ IOException -> 0x0263 }
        r0 = r24;
        r1 = r21;
        r0.<init>(r1);	 Catch:{ IOException -> 0x0263 }
        r0 = r22;
        r1 = r24;
        r22 = r0.append(r1);	 Catch:{ IOException -> 0x0263 }
        r0 = r22;
        r1 = r18;
        r22 = r0.append(r1);	 Catch:{ IOException -> 0x0263 }
        r18 = r22.toString();	 Catch:{ IOException -> 0x0263 }
    L_0x01ea:
        r22 = new java.lang.StringBuilder;	 Catch:{ IOException -> 0x0263 }
        r22.<init>();	 Catch:{ IOException -> 0x0263 }
        r24 = "CachedCommand_";
        r0 = r22;
        r1 = r24;
        r22 = r0.append(r1);	 Catch:{ IOException -> 0x0263 }
        r0 = r22;
        r1 = r17;
        r22 = r0.append(r1);	 Catch:{ IOException -> 0x0263 }
        r24 = "_";
        r0 = r22;
        r1 = r24;
        r22 = r0.append(r1);	 Catch:{ IOException -> 0x0263 }
        r0 = r22;
        r1 = r18;
        r22 = r0.append(r1);	 Catch:{ IOException -> 0x0263 }
        r24 = "_";
        r0 = r22;
        r1 = r24;
        r22 = r0.append(r1);	 Catch:{ IOException -> 0x0263 }
        r16 = r22.toString();	 Catch:{ IOException -> 0x0263 }
        r22 = "";
        r0 = r26;
        r0 = r0.cachePath;	 Catch:{ IOException -> 0x0263 }
        r24 = r0;
        r0 = r16;
        r1 = r22;
        r2 = r24;
        r15 = java.io.File.createTempFile(r0, r1, r2);	 Catch:{ IOException -> 0x0263 }
        r0 = r26;
        r0 = r0.pendingTasks;	 Catch:{ IOException -> 0x0263 }
        r22 = r0;
        r0 = r22;
        r1 = r20;
        r0.put(r15, r1);	 Catch:{ IOException -> 0x0263 }
        r27.retainLocalIds();	 Catch:{ IOException -> 0x0263 }
        com.parse.ParseFileUtils.writeByteArrayToFile(r15, r12);	 Catch:{ IOException -> 0x0263 }
        r22 = 3;
        r0 = r26;
        r1 = r22;
        r0.notifyTestHelper(r1);	 Catch:{ IOException -> 0x0263 }
        r22 = 1;
        r0 = r22;
        r1 = r26;
        r1.unprocessedCommandsExist = r0;	 Catch:{ IOException -> 0x0263 }
        r22 = lock;	 Catch:{ all -> 0x0102 }
        r22.notifyAll();	 Catch:{ all -> 0x0102 }
    L_0x025c:
        monitor-exit(r23);	 Catch:{ all -> 0x0102 }
        r22 = r20.getTask();
        goto L_0x005f;
    L_0x0263:
        r5 = move-exception;
        r22 = 5;
        r24 = com.parse.Parse.getLogLevel();	 Catch:{ all -> 0x0289 }
        r0 = r22;
        r1 = r24;
        if (r0 < r1) goto L_0x0283;
    L_0x0270:
        r0 = r26;
        r0 = r0.log;	 Catch:{ all -> 0x0289 }
        r22 = r0;
        r24 = java.util.logging.Level.WARNING;	 Catch:{ all -> 0x0289 }
        r25 = "Unable to save command for later.";
        r0 = r22;
        r1 = r24;
        r2 = r25;
        r0.log(r1, r2, r5);	 Catch:{ all -> 0x0289 }
    L_0x0283:
        r22 = lock;	 Catch:{ all -> 0x0102 }
        r22.notifyAll();	 Catch:{ all -> 0x0102 }
        goto L_0x025c;
    L_0x0289:
        r22 = move-exception;
        r24 = lock;	 Catch:{ all -> 0x0102 }
        r24.notifyAll();	 Catch:{ all -> 0x0102 }
        throw r22;	 Catch:{ all -> 0x0102 }
        */
        throw new UnsupportedOperationException("Method not decompiled: com.parse.ParseCommandCache.enqueueEventuallyAsync(com.parse.ParseRESTCommand, boolean, com.parse.ParseObject):bolts.Task<org.json.JSONObject>");
    }

    public int pendingCount() {
        return getPendingCount();
    }

    public void clear() {
        synchronized (lock) {
            File[] files = this.cachePath.listFiles();
            if (files == null) {
                return;
            }
            for (File file : files) {
                removeFile(file);
            }
            this.pendingTasks.clear();
        }
    }

    public void setConnected(boolean connected) {
        synchronized (lock) {
            if (isConnected() != connected && connected) {
                lock.notifyAll();
            }
            super.setConnected(connected);
        }
    }

    private <T> T waitForTaskWithoutLock(Task<T> task) throws ParseException {
        T wait;
        synchronized (lock) {
            final Capture<Boolean> finished = new Capture(Boolean.valueOf(false));
            task.continueWith(new Continuation<T, Void>() {
                public Void then(Task<T> task) throws Exception {
                    finished.set(Boolean.valueOf(true));
                    synchronized (ParseCommandCache.lock) {
                        ParseCommandCache.lock.notifyAll();
                    }
                    return null;
                }
            }, Task.BACKGROUND_EXECUTOR);
            while (!((Boolean) finished.get()).booleanValue()) {
                try {
                    lock.wait();
                } catch (InterruptedException e) {
                    this.shouldStop = true;
                }
            }
            wait = ParseTaskUtils.wait(task);
        }
        return wait;
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void maybeRunAllCommandsNow(int r27) {
        /*
        r26 = this;
        r21 = lock;
        monitor-enter(r21);
        r20 = 0;
        r0 = r20;
        r1 = r26;
        r1.unprocessedCommandsExist = r0;	 Catch:{ all -> 0x0026 }
        r20 = r26.isConnected();	 Catch:{ all -> 0x0026 }
        if (r20 != 0) goto L_0x0013;
    L_0x0011:
        monitor-exit(r21);	 Catch:{ all -> 0x0026 }
    L_0x0012:
        return;
    L_0x0013:
        r0 = r26;
        r0 = r0.cachePath;	 Catch:{ all -> 0x0026 }
        r20 = r0;
        r12 = r20.list();	 Catch:{ all -> 0x0026 }
        if (r12 == 0) goto L_0x0024;
    L_0x001f:
        r0 = r12.length;	 Catch:{ all -> 0x0026 }
        r20 = r0;
        if (r20 != 0) goto L_0x0029;
    L_0x0024:
        monitor-exit(r21);	 Catch:{ all -> 0x0026 }
        goto L_0x0012;
    L_0x0026:
        r20 = move-exception;
        monitor-exit(r21);	 Catch:{ all -> 0x0026 }
        throw r20;
    L_0x0029:
        java.util.Arrays.sort(r12);	 Catch:{ all -> 0x0026 }
        r4 = r12;
        r0 = r4.length;	 Catch:{ all -> 0x0026 }
        r16 = r0;
        r13 = 0;
    L_0x0031:
        r0 = r16;
        if (r13 >= r0) goto L_0x0291;
    L_0x0035:
        r11 = r4[r13];	 Catch:{ all -> 0x0026 }
        r10 = new java.io.File;	 Catch:{ all -> 0x0026 }
        r0 = r26;
        r0 = r0.cachePath;	 Catch:{ all -> 0x0026 }
        r20 = r0;
        r0 = r20;
        r10.<init>(r0, r11);	 Catch:{ all -> 0x0026 }
        r15 = com.parse.ParseFileUtils.readFileToJSONObject(r10);	 Catch:{ FileNotFoundException -> 0x00ab, IOException -> 0x00cc, JSONException -> 0x00f2 }
        r0 = r26;
        r0 = r0.pendingTasks;	 Catch:{ all -> 0x0026 }
        r20 = r0;
        r0 = r20;
        r20 = r0.containsKey(r10);	 Catch:{ all -> 0x0026 }
        if (r20 == 0) goto L_0x0118;
    L_0x0056:
        r0 = r26;
        r0 = r0.pendingTasks;	 Catch:{ all -> 0x0026 }
        r20 = r0;
        r0 = r20;
        r20 = r0.get(r10);	 Catch:{ all -> 0x0026 }
        r20 = (bolts.Task.TaskCompletionSource) r20;	 Catch:{ all -> 0x0026 }
        r17 = r20;
    L_0x0066:
        r0 = r26;
        r5 = r0.commandFromJSON(r15);	 Catch:{ JSONException -> 0x011c }
        if (r5 != 0) goto L_0x0143;
    L_0x006e:
        r20 = 0;
        r6 = bolts.Task.forResult(r20);	 Catch:{ ParseException -> 0x015c }
        if (r17 == 0) goto L_0x007f;
    L_0x0076:
        r20 = 0;
        r0 = r17;
        r1 = r20;
        r0.setResult(r1);	 Catch:{ ParseException -> 0x015c }
    L_0x007f:
        r20 = 8;
        r0 = r26;
        r1 = r20;
        r0.notifyTestHelper(r1);	 Catch:{ ParseException -> 0x015c }
    L_0x0088:
        r0 = r26;
        r0.waitForTaskWithoutLock(r6);	 Catch:{ ParseException -> 0x015c }
        if (r17 == 0) goto L_0x009a;
    L_0x008f:
        r20 = r17.getTask();	 Catch:{ ParseException -> 0x015c }
        r0 = r26;
        r1 = r20;
        r0.waitForTaskWithoutLock(r1);	 Catch:{ ParseException -> 0x015c }
    L_0x009a:
        r0 = r26;
        r0.removeFile(r10);	 Catch:{ ParseException -> 0x015c }
        r20 = 1;
        r0 = r26;
        r1 = r20;
        r0.notifyTestHelper(r1);	 Catch:{ ParseException -> 0x015c }
    L_0x00a8:
        r13 = r13 + 1;
        goto L_0x0031;
    L_0x00ab:
        r7 = move-exception;
        r20 = 6;
        r22 = com.parse.Parse.getLogLevel();	 Catch:{ all -> 0x0026 }
        r0 = r20;
        r1 = r22;
        if (r0 < r1) goto L_0x00a8;
    L_0x00b8:
        r0 = r26;
        r0 = r0.log;	 Catch:{ all -> 0x0026 }
        r20 = r0;
        r22 = java.util.logging.Level.SEVERE;	 Catch:{ all -> 0x0026 }
        r23 = "File disappeared from cache while being read.";
        r0 = r20;
        r1 = r22;
        r2 = r23;
        r0.log(r1, r2, r7);	 Catch:{ all -> 0x0026 }
        goto L_0x00a8;
    L_0x00cc:
        r7 = move-exception;
        r20 = 6;
        r22 = com.parse.Parse.getLogLevel();	 Catch:{ all -> 0x0026 }
        r0 = r20;
        r1 = r22;
        if (r0 < r1) goto L_0x00ec;
    L_0x00d9:
        r0 = r26;
        r0 = r0.log;	 Catch:{ all -> 0x0026 }
        r20 = r0;
        r22 = java.util.logging.Level.SEVERE;	 Catch:{ all -> 0x0026 }
        r23 = "Unable to read contents of file in cache.";
        r0 = r20;
        r1 = r22;
        r2 = r23;
        r0.log(r1, r2, r7);	 Catch:{ all -> 0x0026 }
    L_0x00ec:
        r0 = r26;
        r0.removeFile(r10);	 Catch:{ all -> 0x0026 }
        goto L_0x00a8;
    L_0x00f2:
        r7 = move-exception;
        r20 = 6;
        r22 = com.parse.Parse.getLogLevel();	 Catch:{ all -> 0x0026 }
        r0 = r20;
        r1 = r22;
        if (r0 < r1) goto L_0x0112;
    L_0x00ff:
        r0 = r26;
        r0 = r0.log;	 Catch:{ all -> 0x0026 }
        r20 = r0;
        r22 = java.util.logging.Level.SEVERE;	 Catch:{ all -> 0x0026 }
        r23 = "Error parsing JSON found in cache.";
        r0 = r20;
        r1 = r22;
        r2 = r23;
        r0.log(r1, r2, r7);	 Catch:{ all -> 0x0026 }
    L_0x0112:
        r0 = r26;
        r0.removeFile(r10);	 Catch:{ all -> 0x0026 }
        goto L_0x00a8;
    L_0x0118:
        r17 = 0;
        goto L_0x0066;
    L_0x011c:
        r7 = move-exception;
        r20 = 6;
        r22 = com.parse.Parse.getLogLevel();	 Catch:{ all -> 0x0026 }
        r0 = r20;
        r1 = r22;
        if (r0 < r1) goto L_0x013c;
    L_0x0129:
        r0 = r26;
        r0 = r0.log;	 Catch:{ all -> 0x0026 }
        r20 = r0;
        r22 = java.util.logging.Level.SEVERE;	 Catch:{ all -> 0x0026 }
        r23 = "Unable to create ParseCommand from JSON.";
        r0 = r20;
        r1 = r22;
        r2 = r23;
        r0.log(r1, r2, r7);	 Catch:{ all -> 0x0026 }
    L_0x013c:
        r0 = r26;
        r0.removeFile(r10);	 Catch:{ all -> 0x0026 }
        goto L_0x00a8;
    L_0x0143:
        r20 = r5.executeAsync();	 Catch:{ ParseException -> 0x015c }
        r22 = new com.parse.ParseCommandCache$4;	 Catch:{ ParseException -> 0x015c }
        r0 = r22;
        r1 = r26;
        r2 = r17;
        r0.<init>(r5, r2);	 Catch:{ ParseException -> 0x015c }
        r0 = r20;
        r1 = r22;
        r6 = r0.continueWithTask(r1);	 Catch:{ ParseException -> 0x015c }
        goto L_0x0088;
    L_0x015c:
        r7 = move-exception;
        r20 = r7.getCode();	 Catch:{ all -> 0x0026 }
        r22 = 100;
        r0 = r20;
        r1 = r22;
        if (r0 != r1) goto L_0x0262;
    L_0x0169:
        if (r27 <= 0) goto L_0x024e;
    L_0x016b:
        r20 = 4;
        r22 = com.parse.Parse.getLogLevel();	 Catch:{ all -> 0x0026 }
        r0 = r20;
        r1 = r22;
        if (r0 < r1) goto L_0x01b5;
    L_0x0177:
        r0 = r26;
        r0 = r0.log;	 Catch:{ all -> 0x0026 }
        r20 = r0;
        r22 = new java.lang.StringBuilder;	 Catch:{ all -> 0x0026 }
        r22.<init>();	 Catch:{ all -> 0x0026 }
        r23 = "Network timeout in command cache. Waiting for ";
        r22 = r22.append(r23);	 Catch:{ all -> 0x0026 }
        r0 = r26;
        r0 = r0.timeoutRetryWaitSeconds;	 Catch:{ all -> 0x0026 }
        r24 = r0;
        r0 = r22;
        r1 = r24;
        r22 = r0.append(r1);	 Catch:{ all -> 0x0026 }
        r23 = " seconds and then retrying ";
        r22 = r22.append(r23);	 Catch:{ all -> 0x0026 }
        r0 = r22;
        r1 = r27;
        r22 = r0.append(r1);	 Catch:{ all -> 0x0026 }
        r23 = " times.";
        r22 = r22.append(r23);	 Catch:{ all -> 0x0026 }
        r22 = r22.toString();	 Catch:{ all -> 0x0026 }
        r0 = r20;
        r1 = r22;
        r0.info(r1);	 Catch:{ all -> 0x0026 }
    L_0x01b5:
        r8 = java.lang.System.currentTimeMillis();	 Catch:{ all -> 0x0026 }
        r0 = r26;
        r0 = r0.timeoutRetryWaitSeconds;	 Catch:{ all -> 0x0026 }
        r22 = r0;
        r24 = 4652007308841189376; // 0x408f400000000000 float:0.0 double:1000.0;
        r22 = r22 * r24;
        r0 = r22;
        r0 = (long) r0;	 Catch:{ all -> 0x0026 }
        r22 = r0;
        r18 = r8 + r22;
    L_0x01cd:
        r20 = (r8 > r18 ? 1 : (r8 == r18 ? 0 : -1));
        if (r20 >= 0) goto L_0x0243;
    L_0x01d1:
        r20 = r26.isConnected();	 Catch:{ all -> 0x0026 }
        if (r20 == 0) goto L_0x01df;
    L_0x01d7:
        r0 = r26;
        r0 = r0.shouldStop;	 Catch:{ all -> 0x0026 }
        r20 = r0;
        if (r20 == 0) goto L_0x01fd;
    L_0x01df:
        r20 = 4;
        r22 = com.parse.Parse.getLogLevel();	 Catch:{ all -> 0x0026 }
        r0 = r20;
        r1 = r22;
        if (r0 < r1) goto L_0x01fa;
    L_0x01eb:
        r0 = r26;
        r0 = r0.log;	 Catch:{ all -> 0x0026 }
        r20 = r0;
        r22 = "Aborting wait because runEventually thread should stop.";
        r0 = r20;
        r1 = r22;
        r0.info(r1);	 Catch:{ all -> 0x0026 }
    L_0x01fa:
        monitor-exit(r21);	 Catch:{ all -> 0x0026 }
        goto L_0x0012;
    L_0x01fd:
        r20 = lock;	 Catch:{ InterruptedException -> 0x0239 }
        r22 = r18 - r8;
        r0 = r20;
        r1 = r22;
        r0.wait(r1);	 Catch:{ InterruptedException -> 0x0239 }
    L_0x0208:
        r8 = java.lang.System.currentTimeMillis();	 Catch:{ all -> 0x0026 }
        r0 = r26;
        r0 = r0.timeoutRetryWaitSeconds;	 Catch:{ all -> 0x0026 }
        r22 = r0;
        r24 = 4652007308841189376; // 0x408f400000000000 float:0.0 double:1000.0;
        r22 = r22 * r24;
        r0 = r22;
        r0 = (long) r0;	 Catch:{ all -> 0x0026 }
        r22 = r0;
        r22 = r18 - r22;
        r20 = (r8 > r22 ? 1 : (r8 == r22 ? 0 : -1));
        if (r20 >= 0) goto L_0x01cd;
    L_0x0224:
        r0 = r26;
        r0 = r0.timeoutRetryWaitSeconds;	 Catch:{ all -> 0x0026 }
        r22 = r0;
        r24 = 4652007308841189376; // 0x408f400000000000 float:0.0 double:1000.0;
        r22 = r22 * r24;
        r0 = r22;
        r0 = (long) r0;	 Catch:{ all -> 0x0026 }
        r22 = r0;
        r8 = r18 - r22;
        goto L_0x01cd;
    L_0x0239:
        r14 = move-exception;
        r20 = 1;
        r0 = r20;
        r1 = r26;
        r1.shouldStop = r0;	 Catch:{ all -> 0x0026 }
        goto L_0x0208;
    L_0x0243:
        r20 = r27 + -1;
        r0 = r26;
        r1 = r20;
        r0.maybeRunAllCommandsNow(r1);	 Catch:{ all -> 0x0026 }
        goto L_0x00a8;
    L_0x024e:
        r20 = 0;
        r0 = r26;
        r1 = r20;
        r0.setConnected(r1);	 Catch:{ all -> 0x0026 }
        r20 = 7;
        r0 = r26;
        r1 = r20;
        r0.notifyTestHelper(r1);	 Catch:{ all -> 0x0026 }
        goto L_0x00a8;
    L_0x0262:
        r20 = 6;
        r22 = com.parse.Parse.getLogLevel();	 Catch:{ all -> 0x0026 }
        r0 = r20;
        r1 = r22;
        if (r0 < r1) goto L_0x0281;
    L_0x026e:
        r0 = r26;
        r0 = r0.log;	 Catch:{ all -> 0x0026 }
        r20 = r0;
        r22 = java.util.logging.Level.SEVERE;	 Catch:{ all -> 0x0026 }
        r23 = "Failed to run command.";
        r0 = r20;
        r1 = r22;
        r2 = r23;
        r0.log(r1, r2, r7);	 Catch:{ all -> 0x0026 }
    L_0x0281:
        r0 = r26;
        r0.removeFile(r10);	 Catch:{ all -> 0x0026 }
        r20 = 2;
        r0 = r26;
        r1 = r20;
        r0.notifyTestHelper(r1, r7);	 Catch:{ all -> 0x0026 }
        goto L_0x00a8;
    L_0x0291:
        monitor-exit(r21);	 Catch:{ all -> 0x0026 }
        goto L_0x0012;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.parse.ParseCommandCache.maybeRunAllCommandsNow(int):void");
    }
}
