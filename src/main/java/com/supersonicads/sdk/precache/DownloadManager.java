package com.supersonicads.sdk.precache;

import android.os.Handler;
import android.os.Message;
import com.supersonicads.sdk.data.SSAFile;
import com.supersonicads.sdk.utils.SDKUtils;
import com.supersonicads.sdk.utils.SupersonicSharedPrefHelper;
import com.supersonicads.sdk.utils.SupersonicStorageUtils;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

public class DownloadManager {
    public static final String CAMPAIGNS = "campaigns";
    public static final String FILE_ALREADY_EXIST = "file_already_exist";
    public static final String GLOBAL_ASSETS = "globalAssets";
    public static final int MESSAGE_EMPTY_URL = 1007;
    public static final int MESSAGE_FILE_DOWNLOAD_FAIL = 1017;
    public static final int MESSAGE_FILE_DOWNLOAD_SUCCESS = 1016;
    public static final int MESSAGE_FILE_NOT_FOUND_EXCEPTION = 1018;
    public static final int MESSAGE_GENERAL_HTTP_ERROR_CODE = 1011;
    public static final int MESSAGE_HTTP_EMPTY_RESPONSE = 1006;
    public static final int MESSAGE_HTTP_NOT_FOUND = 1005;
    public static final int MESSAGE_INIT_BC_FAIL = 1014;
    public static final int MESSAGE_IO_EXCEPTION = 1009;
    public static final int MESSAGE_MALFORMED_URL_EXCEPTION = 1004;
    public static final int MESSAGE_NUM_OF_BANNERS_TO_CACHE = 1013;
    public static final int MESSAGE_NUM_OF_BANNERS_TO_INIT_SUCCESS = 1012;
    public static final int MESSAGE_SOCKET_TIMEOUT_EXCEPTION = 1008;
    public static final int MESSAGE_URI_SYNTAX_EXCEPTION = 1010;
    public static final int MESSAGE_ZERO_CAMPAIGNS_TO_INIT_SUCCESS = 1015;
    public static final String NO_DISK_SPACE = "no_disk_space";
    public static final String NO_NETWORK_CONNECTION = "no_network_connection";
    public static final int OPERATION_TIMEOUT = 5000;
    public static final String SETTINGS = "settings";
    public static final String SOTRAGE_UNAVAILABLE = "sotrage_unavailable";
    public static final String UNABLE_TO_CREATE_FOLDER = "unable_to_create_folder";
    public static final String UTF8_CHARSET = "UTF-8";
    private static DownloadManager mDownloadManager;
    private final String FILE_NOT_FOUND_EXCEPTION = "file not found exception";
    private final String HTTP_EMPTY_RESPONSE = "http empty response";
    private final String HTTP_ERROR_CODE = "http error code";
    private final String HTTP_NOT_FOUND = "http not found";
    private final String HTTP_OK = "http ok";
    private final String IO_EXCEPTION = "io exception";
    private final String MALFORMED_URL_EXCEPTION = "malformed url exception";
    private final String SOCKET_TIMEOUT_EXCEPTION = "socket timeout exception";
    private final String TAG = "DownloadManager";
    private final String URI_SYNTAX_EXCEPTION = "uri syntax exception";
    private Handler downloadHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case DownloadManager.MESSAGE_FILE_DOWNLOAD_SUCCESS /*1016*/:
                    if (DownloadManager.this.mListener != null) {
                        DownloadManager.this.mListener.onFileDownloadSuccess((SSAFile) msg.obj);
                        return;
                    }
                    return;
                case DownloadManager.MESSAGE_FILE_DOWNLOAD_FAIL /*1017*/:
                    if (DownloadManager.this.mListener != null) {
                        DownloadManager.this.mListener.onFileDownloadFail((SSAFile) msg.obj);
                        return;
                    }
                    return;
                default:
                    return;
            }
        }
    };
    private String mCacheRootDirectory;
    private OnPreCacheCompletion mListener;
    private Thread mMobileControllerThread;

    public class FileWorkerThread implements Callable<List<Object>> {
        private long mConnectionRetries;
        private String mDirectory;
        private String mFile;
        private String mFolderName;
        private String mLink;

        public FileWorkerThread(String link, String directory, String file, long connectionRetries, String folderName) {
            this.mLink = link;
            this.mDirectory = directory;
            this.mFile = file;
            this.mConnectionRetries = connectionRetries;
            this.mFolderName = folderName;
        }

        public List<Object> call() {
            List<Object> results = null;
            if (this.mConnectionRetries == 0) {
                this.mConnectionRetries = 1;
            }
            for (int tryIndex = 0; ((long) tryIndex) < this.mConnectionRetries; tryIndex++) {
                results = new ArrayList();
                results = DownloadManager.this.downloadContent(this.mLink, this.mDirectory, this.mFile, this.mFolderName, tryIndex);
                int responseCode = ((Integer) results.get(1)).intValue();
                if (responseCode != DownloadManager.MESSAGE_SOCKET_TIMEOUT_EXCEPTION && responseCode != DownloadManager.MESSAGE_IO_EXCEPTION) {
                    return results;
                }
            }
            return results;
        }
    }

    public interface OnPreCacheCompletion {
        void onFileDownloadFail(SSAFile sSAFile);

        void onFileDownloadSuccess(SSAFile sSAFile);
    }

    private class SingleFileWorkerThread implements Runnable {
        private long mConnectionRetries;
        private String mFile;
        private String mFileName = SDKUtils.getFileName(this.mFile);
        private String mFolderName;
        private String mPath;

        public SingleFileWorkerThread(SSAFile file) {
            this.mFile = file.getFile();
            this.mPath = file.getPath();
            this.mFolderName = SupersonicStorageUtils.makeDir(DownloadManager.this.mCacheRootDirectory, this.mPath);
            this.mConnectionRetries = Long.parseLong(SupersonicSharedPrefHelper.getSupersonicPrefHelper().getConnectionRetries());
        }

        /* JADX WARNING: inconsistent code. */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void run() {
            /*
            r19 = this;
            r17 = new com.supersonicads.sdk.data.SSAFile;
            r0 = r19;
            r3 = r0.mFileName;
            r0 = r19;
            r4 = r0.mPath;
            r0 = r17;
            r0.<init>(r3, r4);
            r14 = new android.os.Message;
            r14.<init>();
            r0 = r17;
            r14.obj = r0;
            r0 = r19;
            r3 = r0.mFolderName;
            if (r3 != 0) goto L_0x0035;
        L_0x001e:
            r3 = 1017; // 0x3f9 float:1.425E-42 double:5.025E-321;
            r14.what = r3;
            r3 = "unable_to_create_folder";
            r0 = r17;
            r0.setErrMsg(r3);
            r0 = r19;
            r3 = com.supersonicads.sdk.precache.DownloadManager.this;
            r3 = r3.downloadHandler;
            r3.sendMessage(r14);
        L_0x0034:
            return;
        L_0x0035:
            r3 = 1;
            r13 = java.util.concurrent.Executors.newFixedThreadPool(r3);
            r15 = new java.util.concurrent.ExecutorCompletionService;
            r15.<init>(r13);
            r3 = new com.supersonicads.sdk.precache.DownloadManager$FileWorkerThread;
            r0 = r19;
            r4 = com.supersonicads.sdk.precache.DownloadManager.this;
            r0 = r19;
            r5 = r0.mFile;
            r0 = r19;
            r6 = r0.mFolderName;
            r0 = r19;
            r7 = r0.mFileName;
            r0 = r19;
            r8 = r0.mConnectionRetries;
            r0 = r19;
            r10 = r0.mPath;
            r3.<init>(r5, r6, r7, r8, r10);
            r15.submit(r3);
            r2 = 0;
            r3 = r15.take();	 Catch:{ InterruptedException -> 0x00a3, ExecutionException -> 0x00ae }
            r16 = r3.get();	 Catch:{ InterruptedException -> 0x00a3, ExecutionException -> 0x00ae }
            r16 = (java.util.List) r16;	 Catch:{ InterruptedException -> 0x00a3, ExecutionException -> 0x00ae }
            r3 = 0;
            r0 = r16;
            r18 = r0.get(r3);	 Catch:{ InterruptedException -> 0x00a3, ExecutionException -> 0x00ae }
            r18 = (java.lang.String) r18;	 Catch:{ InterruptedException -> 0x00a3, ExecutionException -> 0x00ae }
            r3 = 1;
            r0 = r16;
            r3 = r0.get(r3);	 Catch:{ InterruptedException -> 0x00a3, ExecutionException -> 0x00ae }
            r3 = (java.lang.Integer) r3;	 Catch:{ InterruptedException -> 0x00a3, ExecutionException -> 0x00ae }
            r2 = r3.intValue();	 Catch:{ InterruptedException -> 0x00a3, ExecutionException -> 0x00ae }
        L_0x0080:
            r13.shutdown();
            r4 = 9223372036854775807; // 0x7fffffffffffffff float:NaN double:NaN;
            r3 = java.util.concurrent.TimeUnit.NANOSECONDS;	 Catch:{ InterruptedException -> 0x00f9 }
            r13.awaitTermination(r4, r3);	 Catch:{ InterruptedException -> 0x00f9 }
        L_0x008d:
            switch(r2) {
                case 200: goto L_0x0091;
                case 404: goto L_0x00b9;
                case 1004: goto L_0x00b7;
                case 1005: goto L_0x00b9;
                case 1006: goto L_0x00bb;
                case 1008: goto L_0x00dc;
                case 1009: goto L_0x00de;
                case 1010: goto L_0x00bd;
                case 1011: goto L_0x00bf;
                case 1018: goto L_0x00c1;
                default: goto L_0x0090;
            };
        L_0x0090:
            goto L_0x0034;
        L_0x0091:
            r12 = "http ok";
            r3 = 1016; // 0x3f8 float:1.424E-42 double:5.02E-321;
            r14.what = r3;
            r0 = r19;
            r3 = com.supersonicads.sdk.precache.DownloadManager.this;
            r3 = r3.downloadHandler;
            r3.sendMessage(r14);
            goto L_0x0034;
        L_0x00a3:
            r11 = move-exception;
            r3 = java.lang.Thread.currentThread();	 Catch:{ all -> 0x00ac }
            r3.interrupt();	 Catch:{ all -> 0x00ac }
            goto L_0x0080;
        L_0x00ac:
            r3 = move-exception;
            throw r3;
        L_0x00ae:
            r11 = move-exception;
            r3 = java.lang.Thread.currentThread();	 Catch:{ all -> 0x00ac }
            r3.interrupt();	 Catch:{ all -> 0x00ac }
            goto L_0x0080;
        L_0x00b7:
            r12 = "malformed url exception";
        L_0x00b9:
            r12 = "http not found";
        L_0x00bb:
            r12 = "http empty response";
        L_0x00bd:
            r12 = "uri syntax exception";
        L_0x00bf:
            r12 = "http error code";
        L_0x00c1:
            r12 = "file not found exception";
            r3 = 1017; // 0x3f9 float:1.425E-42 double:5.025E-321;
            r14.what = r3;
            r0 = r17;
            r0.setErrMsg(r12);
            r0 = r19;
            r3 = com.supersonicads.sdk.precache.DownloadManager.this;
            r3 = r3.downloadHandler;
            r3.sendMessage(r14);
            r13.shutdownNow();
            goto L_0x0034;
        L_0x00dc:
            r12 = "socket timeout exception";
        L_0x00de:
            r12 = "io exception";
            r3 = 1017; // 0x3f9 float:1.425E-42 double:5.025E-321;
            r14.what = r3;
            r0 = r17;
            r0.setErrMsg(r12);
            r0 = r19;
            r3 = com.supersonicads.sdk.precache.DownloadManager.this;
            r3 = r3.downloadHandler;
            r3.sendMessage(r14);
            r13.shutdownNow();
            goto L_0x0034;
        L_0x00f9:
            r3 = move-exception;
            goto L_0x008d;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.supersonicads.sdk.precache.DownloadManager.SingleFileWorkerThread.run():void");
        }
    }

    private DownloadManager(String cacheRootDirectory) {
        this.mCacheRootDirectory = cacheRootDirectory;
    }

    public static synchronized DownloadManager getInstance(String cacheRootDirectory) {
        DownloadManager downloadManager;
        synchronized (DownloadManager.class) {
            if (mDownloadManager == null) {
                mDownloadManager = new DownloadManager(cacheRootDirectory);
            }
            downloadManager = mDownloadManager;
        }
        return downloadManager;
    }

    public void setOnPreCacheCompletion(OnPreCacheCompletion listener) {
        this.mListener = listener;
    }

    public void release() {
        mDownloadManager = null;
        this.mListener = null;
    }

    public void downloadFile(SSAFile file) {
        new Thread(new SingleFileWorkerThread(file)).start();
    }

    public void downloadMobileControllerFile(SSAFile file) {
        this.mMobileControllerThread = new Thread(new SingleFileWorkerThread(file));
        this.mMobileControllerThread.start();
    }

    public boolean isMobileControllerThreadLive() {
        if (this.mMobileControllerThread != null) {
            return this.mMobileControllerThread.isAlive();
        }
        return false;
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public java.util.List<java.lang.Object> downloadContent(java.lang.String r20, java.lang.String r21, java.lang.String r22, java.lang.String r23, int r24) {
        /*
        r19 = this;
        r13 = new java.util.ArrayList;
        r13.<init>();
        r10 = 0;
        r6 = 0;
        r8 = 0;
        r4 = 0;
        r12 = 0;
        r15 = android.text.TextUtils.isEmpty(r20);
        if (r15 == 0) goto L_0x001f;
    L_0x0010:
        r0 = r20;
        r13.add(r0);
        r15 = 1007; // 0x3ef float:1.411E-42 double:4.975E-321;
        r15 = java.lang.Integer.valueOf(r15);
        r13.add(r15);
    L_0x001e:
        return r13;
    L_0x001f:
        r9 = new java.net.URL;	 Catch:{ MalformedURLException -> 0x00ff, URISyntaxException -> 0x0165, SocketTimeoutException -> 0x01cb, FileNotFoundException -> 0x0231, IOException -> 0x0297 }
        r0 = r20;
        r9.<init>(r0);	 Catch:{ MalformedURLException -> 0x00ff, URISyntaxException -> 0x0165, SocketTimeoutException -> 0x01cb, FileNotFoundException -> 0x0231, IOException -> 0x0297 }
        r9.toURI();	 Catch:{ MalformedURLException -> 0x00ff, URISyntaxException -> 0x0165, SocketTimeoutException -> 0x01cb, FileNotFoundException -> 0x0231, IOException -> 0x0297 }
        r15 = r9.openConnection();	 Catch:{ MalformedURLException -> 0x00ff, URISyntaxException -> 0x0165, SocketTimeoutException -> 0x01cb, FileNotFoundException -> 0x0231, IOException -> 0x0297 }
        r0 = r15;
        r0 = (java.net.HttpURLConnection) r0;	 Catch:{ MalformedURLException -> 0x00ff, URISyntaxException -> 0x0165, SocketTimeoutException -> 0x01cb, FileNotFoundException -> 0x0231, IOException -> 0x0297 }
        r4 = r0;
        r15 = "GET";
        r4.setRequestMethod(r15);	 Catch:{ MalformedURLException -> 0x00ff, URISyntaxException -> 0x0165, SocketTimeoutException -> 0x01cb, FileNotFoundException -> 0x0231, IOException -> 0x0297 }
        r15 = 5000; // 0x1388 float:7.006E-42 double:2.4703E-320;
        r4.setConnectTimeout(r15);	 Catch:{ MalformedURLException -> 0x00ff, URISyntaxException -> 0x0165, SocketTimeoutException -> 0x01cb, FileNotFoundException -> 0x0231, IOException -> 0x0297 }
        r15 = 5000; // 0x1388 float:7.006E-42 double:2.4703E-320;
        r4.setReadTimeout(r15);	 Catch:{ MalformedURLException -> 0x00ff, URISyntaxException -> 0x0165, SocketTimeoutException -> 0x01cb, FileNotFoundException -> 0x0231, IOException -> 0x0297 }
        r4.connect();	 Catch:{ MalformedURLException -> 0x00ff, URISyntaxException -> 0x0165, SocketTimeoutException -> 0x01cb, FileNotFoundException -> 0x0231, IOException -> 0x0297 }
        r12 = r4.getResponseCode();	 Catch:{ MalformedURLException -> 0x00ff, URISyntaxException -> 0x0165, SocketTimeoutException -> 0x01cb, FileNotFoundException -> 0x0231, IOException -> 0x0297 }
        r15 = android.text.TextUtils.isEmpty(r23);	 Catch:{ MalformedURLException -> 0x00ff, URISyntaxException -> 0x0165, SocketTimeoutException -> 0x01cb, FileNotFoundException -> 0x0231, IOException -> 0x0297 }
        if (r15 != 0) goto L_0x0056;
    L_0x004d:
        r0 = r19;
        r15 = r0.mCacheRootDirectory;	 Catch:{ MalformedURLException -> 0x00ff, URISyntaxException -> 0x0165, SocketTimeoutException -> 0x01cb, FileNotFoundException -> 0x0231, IOException -> 0x0297 }
        r0 = r23;
        com.supersonicads.sdk.utils.SupersonicStorageUtils.makeDir(r15, r0);	 Catch:{ MalformedURLException -> 0x00ff, URISyntaxException -> 0x0165, SocketTimeoutException -> 0x01cb, FileNotFoundException -> 0x0231, IOException -> 0x0297 }
    L_0x0056:
        r11 = new java.io.File;	 Catch:{ MalformedURLException -> 0x00ff, URISyntaxException -> 0x0165, SocketTimeoutException -> 0x01cb, FileNotFoundException -> 0x0231, IOException -> 0x0297 }
        r15 = new java.lang.StringBuilder;	 Catch:{ MalformedURLException -> 0x00ff, URISyntaxException -> 0x0165, SocketTimeoutException -> 0x01cb, FileNotFoundException -> 0x0231, IOException -> 0x0297 }
        r15.<init>();	 Catch:{ MalformedURLException -> 0x00ff, URISyntaxException -> 0x0165, SocketTimeoutException -> 0x01cb, FileNotFoundException -> 0x0231, IOException -> 0x0297 }
        r0 = r21;
        r15 = r15.append(r0);	 Catch:{ MalformedURLException -> 0x00ff, URISyntaxException -> 0x0165, SocketTimeoutException -> 0x01cb, FileNotFoundException -> 0x0231, IOException -> 0x0297 }
        r16 = java.io.File.separator;	 Catch:{ MalformedURLException -> 0x00ff, URISyntaxException -> 0x0165, SocketTimeoutException -> 0x01cb, FileNotFoundException -> 0x0231, IOException -> 0x0297 }
        r15 = r15.append(r16);	 Catch:{ MalformedURLException -> 0x00ff, URISyntaxException -> 0x0165, SocketTimeoutException -> 0x01cb, FileNotFoundException -> 0x0231, IOException -> 0x0297 }
        r0 = r22;
        r15 = r15.append(r0);	 Catch:{ MalformedURLException -> 0x00ff, URISyntaxException -> 0x0165, SocketTimeoutException -> 0x01cb, FileNotFoundException -> 0x0231, IOException -> 0x0297 }
        r15 = r15.toString();	 Catch:{ MalformedURLException -> 0x00ff, URISyntaxException -> 0x0165, SocketTimeoutException -> 0x01cb, FileNotFoundException -> 0x0231, IOException -> 0x0297 }
        r11.<init>(r15);	 Catch:{ MalformedURLException -> 0x00ff, URISyntaxException -> 0x0165, SocketTimeoutException -> 0x01cb, FileNotFoundException -> 0x0231, IOException -> 0x0297 }
        r7 = new java.io.FileOutputStream;	 Catch:{ MalformedURLException -> 0x03b9, URISyntaxException -> 0x03ad, SocketTimeoutException -> 0x03a1, FileNotFoundException -> 0x0395, IOException -> 0x0389, all -> 0x037f }
        r7.<init>(r11);	 Catch:{ MalformedURLException -> 0x03b9, URISyntaxException -> 0x03ad, SocketTimeoutException -> 0x03a1, FileNotFoundException -> 0x0395, IOException -> 0x0389, all -> 0x037f }
        r8 = r4.getInputStream();	 Catch:{ MalformedURLException -> 0x03bd, URISyntaxException -> 0x03b1, SocketTimeoutException -> 0x03a5, FileNotFoundException -> 0x0399, IOException -> 0x038d, all -> 0x0382 }
        r15 = 102400; // 0x19000 float:1.43493E-40 double:5.05923E-319;
        r2 = new byte[r15];	 Catch:{ MalformedURLException -> 0x03bd, URISyntaxException -> 0x03b1, SocketTimeoutException -> 0x03a5, FileNotFoundException -> 0x0399, IOException -> 0x038d, all -> 0x0382 }
        r3 = 0;
        r14 = 0;
    L_0x0086:
        r3 = r8.read(r2);	 Catch:{ IOException -> 0x0093, MalformedURLException -> 0x03bd, URISyntaxException -> 0x03b1, SocketTimeoutException -> 0x03a5, FileNotFoundException -> 0x0399, all -> 0x0382 }
        r15 = -1;
        if (r3 == r15) goto L_0x0096;
    L_0x008d:
        r15 = 0;
        r7.write(r2, r15, r3);	 Catch:{ IOException -> 0x0093, MalformedURLException -> 0x03bd, URISyntaxException -> 0x03b1, SocketTimeoutException -> 0x03a5, FileNotFoundException -> 0x0399, all -> 0x0382 }
        r14 = r14 + r3;
        goto L_0x0086;
    L_0x0093:
        r5 = move-exception;
        r12 = 1009; // 0x3f1 float:1.414E-42 double:4.985E-321;
    L_0x0096:
        if (r14 != 0) goto L_0x009a;
    L_0x0098:
        r12 = 1006; // 0x3ee float:1.41E-42 double:4.97E-321;
    L_0x009a:
        if (r7 == 0) goto L_0x009f;
    L_0x009c:
        r7.close();	 Catch:{ IOException -> 0x03c2 }
    L_0x009f:
        if (r8 == 0) goto L_0x00a4;
    L_0x00a1:
        r8.close();	 Catch:{ IOException -> 0x03c2 }
    L_0x00a4:
        if (r12 != 0) goto L_0x00b0;
    L_0x00a6:
        r15 = 200; // 0xc8 float:2.8E-43 double:9.9E-322;
        if (r12 >= r15) goto L_0x00b0;
    L_0x00aa:
        r15 = 399; // 0x18f float:5.59E-43 double:1.97E-321;
        if (r12 <= r15) goto L_0x00b0;
    L_0x00ae:
        r12 = 1011; // 0x3f3 float:1.417E-42 double:4.995E-321;
    L_0x00b0:
        r15 = 200; // 0xc8 float:2.8E-43 double:9.9E-322;
        if (r12 == r15) goto L_0x00ea;
    L_0x00b4:
        r15 = "DownloadManager";
        r16 = new java.lang.StringBuilder;
        r16.<init>();
        r17 = " RESPONSE CODE: ";
        r16 = r16.append(r17);
        r0 = r16;
        r16 = r0.append(r12);
        r17 = " URL: ";
        r16 = r16.append(r17);
        r0 = r16;
        r1 = r20;
        r16 = r0.append(r1);
        r17 = " ATTEMPT: ";
        r16 = r16.append(r17);
        r0 = r16;
        r1 = r24;
        r16 = r0.append(r1);
        r16 = r16.toString();
        com.supersonicads.sdk.utils.Logger.i(r15, r16);
    L_0x00ea:
        if (r4 == 0) goto L_0x00ef;
    L_0x00ec:
        r4.disconnect();
    L_0x00ef:
        r0 = r20;
        r13.add(r0);
        r15 = java.lang.Integer.valueOf(r12);
        r13.add(r15);
        r6 = r7;
        r10 = r11;
        goto L_0x001e;
    L_0x00ff:
        r5 = move-exception;
    L_0x0100:
        r12 = 1004; // 0x3ec float:1.407E-42 double:4.96E-321;
        if (r6 == 0) goto L_0x0107;
    L_0x0104:
        r6.close();	 Catch:{ IOException -> 0x03b6 }
    L_0x0107:
        if (r8 == 0) goto L_0x010c;
    L_0x0109:
        r8.close();	 Catch:{ IOException -> 0x03b6 }
    L_0x010c:
        if (r12 != 0) goto L_0x0118;
    L_0x010e:
        r15 = 200; // 0xc8 float:2.8E-43 double:9.9E-322;
        if (r12 >= r15) goto L_0x0118;
    L_0x0112:
        r15 = 399; // 0x18f float:5.59E-43 double:1.97E-321;
        if (r12 <= r15) goto L_0x0118;
    L_0x0116:
        r12 = 1011; // 0x3f3 float:1.417E-42 double:4.995E-321;
    L_0x0118:
        r15 = 200; // 0xc8 float:2.8E-43 double:9.9E-322;
        if (r12 == r15) goto L_0x0152;
    L_0x011c:
        r15 = "DownloadManager";
        r16 = new java.lang.StringBuilder;
        r16.<init>();
        r17 = " RESPONSE CODE: ";
        r16 = r16.append(r17);
        r0 = r16;
        r16 = r0.append(r12);
        r17 = " URL: ";
        r16 = r16.append(r17);
        r0 = r16;
        r1 = r20;
        r16 = r0.append(r1);
        r17 = " ATTEMPT: ";
        r16 = r16.append(r17);
        r0 = r16;
        r1 = r24;
        r16 = r0.append(r1);
        r16 = r16.toString();
        com.supersonicads.sdk.utils.Logger.i(r15, r16);
    L_0x0152:
        if (r4 == 0) goto L_0x0157;
    L_0x0154:
        r4.disconnect();
    L_0x0157:
        r0 = r20;
        r13.add(r0);
        r15 = java.lang.Integer.valueOf(r12);
        r13.add(r15);
        goto L_0x001e;
    L_0x0165:
        r5 = move-exception;
    L_0x0166:
        r12 = 1010; // 0x3f2 float:1.415E-42 double:4.99E-321;
        if (r6 == 0) goto L_0x016d;
    L_0x016a:
        r6.close();	 Catch:{ IOException -> 0x03aa }
    L_0x016d:
        if (r8 == 0) goto L_0x0172;
    L_0x016f:
        r8.close();	 Catch:{ IOException -> 0x03aa }
    L_0x0172:
        if (r12 != 0) goto L_0x017e;
    L_0x0174:
        r15 = 200; // 0xc8 float:2.8E-43 double:9.9E-322;
        if (r12 >= r15) goto L_0x017e;
    L_0x0178:
        r15 = 399; // 0x18f float:5.59E-43 double:1.97E-321;
        if (r12 <= r15) goto L_0x017e;
    L_0x017c:
        r12 = 1011; // 0x3f3 float:1.417E-42 double:4.995E-321;
    L_0x017e:
        r15 = 200; // 0xc8 float:2.8E-43 double:9.9E-322;
        if (r12 == r15) goto L_0x01b8;
    L_0x0182:
        r15 = "DownloadManager";
        r16 = new java.lang.StringBuilder;
        r16.<init>();
        r17 = " RESPONSE CODE: ";
        r16 = r16.append(r17);
        r0 = r16;
        r16 = r0.append(r12);
        r17 = " URL: ";
        r16 = r16.append(r17);
        r0 = r16;
        r1 = r20;
        r16 = r0.append(r1);
        r17 = " ATTEMPT: ";
        r16 = r16.append(r17);
        r0 = r16;
        r1 = r24;
        r16 = r0.append(r1);
        r16 = r16.toString();
        com.supersonicads.sdk.utils.Logger.i(r15, r16);
    L_0x01b8:
        if (r4 == 0) goto L_0x01bd;
    L_0x01ba:
        r4.disconnect();
    L_0x01bd:
        r0 = r20;
        r13.add(r0);
        r15 = java.lang.Integer.valueOf(r12);
        r13.add(r15);
        goto L_0x001e;
    L_0x01cb:
        r5 = move-exception;
    L_0x01cc:
        r12 = 1008; // 0x3f0 float:1.413E-42 double:4.98E-321;
        if (r6 == 0) goto L_0x01d3;
    L_0x01d0:
        r6.close();	 Catch:{ IOException -> 0x039e }
    L_0x01d3:
        if (r8 == 0) goto L_0x01d8;
    L_0x01d5:
        r8.close();	 Catch:{ IOException -> 0x039e }
    L_0x01d8:
        if (r12 != 0) goto L_0x01e4;
    L_0x01da:
        r15 = 200; // 0xc8 float:2.8E-43 double:9.9E-322;
        if (r12 >= r15) goto L_0x01e4;
    L_0x01de:
        r15 = 399; // 0x18f float:5.59E-43 double:1.97E-321;
        if (r12 <= r15) goto L_0x01e4;
    L_0x01e2:
        r12 = 1011; // 0x3f3 float:1.417E-42 double:4.995E-321;
    L_0x01e4:
        r15 = 200; // 0xc8 float:2.8E-43 double:9.9E-322;
        if (r12 == r15) goto L_0x021e;
    L_0x01e8:
        r15 = "DownloadManager";
        r16 = new java.lang.StringBuilder;
        r16.<init>();
        r17 = " RESPONSE CODE: ";
        r16 = r16.append(r17);
        r0 = r16;
        r16 = r0.append(r12);
        r17 = " URL: ";
        r16 = r16.append(r17);
        r0 = r16;
        r1 = r20;
        r16 = r0.append(r1);
        r17 = " ATTEMPT: ";
        r16 = r16.append(r17);
        r0 = r16;
        r1 = r24;
        r16 = r0.append(r1);
        r16 = r16.toString();
        com.supersonicads.sdk.utils.Logger.i(r15, r16);
    L_0x021e:
        if (r4 == 0) goto L_0x0223;
    L_0x0220:
        r4.disconnect();
    L_0x0223:
        r0 = r20;
        r13.add(r0);
        r15 = java.lang.Integer.valueOf(r12);
        r13.add(r15);
        goto L_0x001e;
    L_0x0231:
        r5 = move-exception;
    L_0x0232:
        r12 = 1018; // 0x3fa float:1.427E-42 double:5.03E-321;
        if (r6 == 0) goto L_0x0239;
    L_0x0236:
        r6.close();	 Catch:{ IOException -> 0x0392 }
    L_0x0239:
        if (r8 == 0) goto L_0x023e;
    L_0x023b:
        r8.close();	 Catch:{ IOException -> 0x0392 }
    L_0x023e:
        if (r12 != 0) goto L_0x024a;
    L_0x0240:
        r15 = 200; // 0xc8 float:2.8E-43 double:9.9E-322;
        if (r12 >= r15) goto L_0x024a;
    L_0x0244:
        r15 = 399; // 0x18f float:5.59E-43 double:1.97E-321;
        if (r12 <= r15) goto L_0x024a;
    L_0x0248:
        r12 = 1011; // 0x3f3 float:1.417E-42 double:4.995E-321;
    L_0x024a:
        r15 = 200; // 0xc8 float:2.8E-43 double:9.9E-322;
        if (r12 == r15) goto L_0x0284;
    L_0x024e:
        r15 = "DownloadManager";
        r16 = new java.lang.StringBuilder;
        r16.<init>();
        r17 = " RESPONSE CODE: ";
        r16 = r16.append(r17);
        r0 = r16;
        r16 = r0.append(r12);
        r17 = " URL: ";
        r16 = r16.append(r17);
        r0 = r16;
        r1 = r20;
        r16 = r0.append(r1);
        r17 = " ATTEMPT: ";
        r16 = r16.append(r17);
        r0 = r16;
        r1 = r24;
        r16 = r0.append(r1);
        r16 = r16.toString();
        com.supersonicads.sdk.utils.Logger.i(r15, r16);
    L_0x0284:
        if (r4 == 0) goto L_0x0289;
    L_0x0286:
        r4.disconnect();
    L_0x0289:
        r0 = r20;
        r13.add(r0);
        r15 = java.lang.Integer.valueOf(r12);
        r13.add(r15);
        goto L_0x001e;
    L_0x0297:
        r5 = move-exception;
    L_0x0298:
        if (r5 == 0) goto L_0x02ad;
    L_0x029a:
        r15 = r5.getMessage();	 Catch:{ all -> 0x0312 }
        r15 = android.text.TextUtils.isEmpty(r15);	 Catch:{ all -> 0x0312 }
        if (r15 == 0) goto L_0x02ad;
    L_0x02a4:
        r15 = "DownloadManager";
        r16 = r5.getMessage();	 Catch:{ all -> 0x0312 }
        com.supersonicads.sdk.utils.Logger.i(r15, r16);	 Catch:{ all -> 0x0312 }
    L_0x02ad:
        r12 = 1009; // 0x3f1 float:1.414E-42 double:4.985E-321;
        if (r6 == 0) goto L_0x02b4;
    L_0x02b1:
        r6.close();	 Catch:{ IOException -> 0x0386 }
    L_0x02b4:
        if (r8 == 0) goto L_0x02b9;
    L_0x02b6:
        r8.close();	 Catch:{ IOException -> 0x0386 }
    L_0x02b9:
        if (r12 != 0) goto L_0x02c5;
    L_0x02bb:
        r15 = 200; // 0xc8 float:2.8E-43 double:9.9E-322;
        if (r12 >= r15) goto L_0x02c5;
    L_0x02bf:
        r15 = 399; // 0x18f float:5.59E-43 double:1.97E-321;
        if (r12 <= r15) goto L_0x02c5;
    L_0x02c3:
        r12 = 1011; // 0x3f3 float:1.417E-42 double:4.995E-321;
    L_0x02c5:
        r15 = 200; // 0xc8 float:2.8E-43 double:9.9E-322;
        if (r12 == r15) goto L_0x02ff;
    L_0x02c9:
        r15 = "DownloadManager";
        r16 = new java.lang.StringBuilder;
        r16.<init>();
        r17 = " RESPONSE CODE: ";
        r16 = r16.append(r17);
        r0 = r16;
        r16 = r0.append(r12);
        r17 = " URL: ";
        r16 = r16.append(r17);
        r0 = r16;
        r1 = r20;
        r16 = r0.append(r1);
        r17 = " ATTEMPT: ";
        r16 = r16.append(r17);
        r0 = r16;
        r1 = r24;
        r16 = r0.append(r1);
        r16 = r16.toString();
        com.supersonicads.sdk.utils.Logger.i(r15, r16);
    L_0x02ff:
        if (r4 == 0) goto L_0x0304;
    L_0x0301:
        r4.disconnect();
    L_0x0304:
        r0 = r20;
        r13.add(r0);
        r15 = java.lang.Integer.valueOf(r12);
        r13.add(r15);
        goto L_0x001e;
    L_0x0312:
        r15 = move-exception;
    L_0x0313:
        if (r6 == 0) goto L_0x0318;
    L_0x0315:
        r6.close();	 Catch:{ IOException -> 0x037d }
    L_0x0318:
        if (r8 == 0) goto L_0x031d;
    L_0x031a:
        r8.close();	 Catch:{ IOException -> 0x037d }
    L_0x031d:
        if (r12 != 0) goto L_0x032d;
    L_0x031f:
        r16 = 200; // 0xc8 float:2.8E-43 double:9.9E-322;
        r0 = r16;
        if (r12 >= r0) goto L_0x032d;
    L_0x0325:
        r16 = 399; // 0x18f float:5.59E-43 double:1.97E-321;
        r0 = r16;
        if (r12 <= r0) goto L_0x032d;
    L_0x032b:
        r12 = 1011; // 0x3f3 float:1.417E-42 double:4.995E-321;
    L_0x032d:
        r16 = 200; // 0xc8 float:2.8E-43 double:9.9E-322;
        r0 = r16;
        if (r12 == r0) goto L_0x0369;
    L_0x0333:
        r16 = "DownloadManager";
        r17 = new java.lang.StringBuilder;
        r17.<init>();
        r18 = " RESPONSE CODE: ";
        r17 = r17.append(r18);
        r0 = r17;
        r17 = r0.append(r12);
        r18 = " URL: ";
        r17 = r17.append(r18);
        r0 = r17;
        r1 = r20;
        r17 = r0.append(r1);
        r18 = " ATTEMPT: ";
        r17 = r17.append(r18);
        r0 = r17;
        r1 = r24;
        r17 = r0.append(r1);
        r17 = r17.toString();
        com.supersonicads.sdk.utils.Logger.i(r16, r17);
    L_0x0369:
        if (r4 == 0) goto L_0x036e;
    L_0x036b:
        r4.disconnect();
    L_0x036e:
        r0 = r20;
        r13.add(r0);
        r16 = java.lang.Integer.valueOf(r12);
        r0 = r16;
        r13.add(r0);
        throw r15;
    L_0x037d:
        r16 = move-exception;
        goto L_0x031d;
    L_0x037f:
        r15 = move-exception;
        r10 = r11;
        goto L_0x0313;
    L_0x0382:
        r15 = move-exception;
        r6 = r7;
        r10 = r11;
        goto L_0x0313;
    L_0x0386:
        r15 = move-exception;
        goto L_0x02b9;
    L_0x0389:
        r5 = move-exception;
        r10 = r11;
        goto L_0x0298;
    L_0x038d:
        r5 = move-exception;
        r6 = r7;
        r10 = r11;
        goto L_0x0298;
    L_0x0392:
        r15 = move-exception;
        goto L_0x023e;
    L_0x0395:
        r5 = move-exception;
        r10 = r11;
        goto L_0x0232;
    L_0x0399:
        r5 = move-exception;
        r6 = r7;
        r10 = r11;
        goto L_0x0232;
    L_0x039e:
        r15 = move-exception;
        goto L_0x01d8;
    L_0x03a1:
        r5 = move-exception;
        r10 = r11;
        goto L_0x01cc;
    L_0x03a5:
        r5 = move-exception;
        r6 = r7;
        r10 = r11;
        goto L_0x01cc;
    L_0x03aa:
        r15 = move-exception;
        goto L_0x0172;
    L_0x03ad:
        r5 = move-exception;
        r10 = r11;
        goto L_0x0166;
    L_0x03b1:
        r5 = move-exception;
        r6 = r7;
        r10 = r11;
        goto L_0x0166;
    L_0x03b6:
        r15 = move-exception;
        goto L_0x010c;
    L_0x03b9:
        r5 = move-exception;
        r10 = r11;
        goto L_0x0100;
    L_0x03bd:
        r5 = move-exception;
        r6 = r7;
        r10 = r11;
        goto L_0x0100;
    L_0x03c2:
        r15 = move-exception;
        goto L_0x00a4;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.supersonicads.sdk.precache.DownloadManager.downloadContent(java.lang.String, java.lang.String, java.lang.String, java.lang.String, int):java.util.List<java.lang.Object>");
    }
}
