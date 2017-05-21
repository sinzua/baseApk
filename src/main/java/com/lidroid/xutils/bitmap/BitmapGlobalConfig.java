package com.lidroid.xutils.bitmap;

import android.app.ActivityManager;
import android.content.Context;
import android.text.TextUtils;
import com.lidroid.xutils.bitmap.core.BitmapCache;
import com.lidroid.xutils.bitmap.download.DefaultDownloader;
import com.lidroid.xutils.bitmap.download.Downloader;
import com.lidroid.xutils.cache.FileNameGenerator;
import com.lidroid.xutils.task.Priority;
import com.lidroid.xutils.task.PriorityAsyncTask;
import com.lidroid.xutils.task.PriorityExecutor;
import com.lidroid.xutils.util.LogUtils;
import com.lidroid.xutils.util.OtherUtils;
import java.util.HashMap;

public class BitmapGlobalConfig {
    private static final PriorityExecutor BITMAP_LOAD_EXECUTOR = new PriorityExecutor(5);
    private static final int DEFAULT_POOL_SIZE = 5;
    private static final PriorityExecutor DISK_CACHE_EXECUTOR = new PriorityExecutor(2);
    public static final int MIN_DISK_CACHE_SIZE = 10485760;
    public static final int MIN_MEMORY_CACHE_SIZE = 2097152;
    private static final HashMap<String, BitmapGlobalConfig> configMap = new HashMap(1);
    private BitmapCache bitmapCache;
    private BitmapCacheListener bitmapCacheListener;
    private long defaultCacheExpiry = 2592000000L;
    private int defaultConnectTimeout = 15000;
    private int defaultReadTimeout = 15000;
    private boolean diskCacheEnabled = true;
    private String diskCachePath;
    private int diskCacheSize = 52428800;
    private Downloader downloader;
    private FileNameGenerator fileNameGenerator;
    private Context mContext;
    private boolean memoryCacheEnabled = true;
    private int memoryCacheSize = 4194304;

    private class BitmapCacheManagementTask extends PriorityAsyncTask<Object, Void, Object[]> {
        public static final int MESSAGE_CLEAR = 4;
        public static final int MESSAGE_CLEAR_BY_KEY = 7;
        public static final int MESSAGE_CLEAR_DISK = 6;
        public static final int MESSAGE_CLEAR_DISK_BY_KEY = 9;
        public static final int MESSAGE_CLEAR_MEMORY = 5;
        public static final int MESSAGE_CLEAR_MEMORY_BY_KEY = 8;
        public static final int MESSAGE_CLOSE = 3;
        public static final int MESSAGE_FLUSH = 2;
        public static final int MESSAGE_INIT_DISK_CACHE = 1;
        public static final int MESSAGE_INIT_MEMORY_CACHE = 0;

        private BitmapCacheManagementTask() {
            setPriority(Priority.UI_TOP);
        }

        /* JADX WARNING: inconsistent code. */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        protected java.lang.Object[] doInBackground(java.lang.Object... r5) {
            /*
            r4 = this;
            r3 = 2;
            if (r5 == 0) goto L_0x0006;
        L_0x0003:
            r2 = r5.length;
            if (r2 != 0) goto L_0x0007;
        L_0x0006:
            return r5;
        L_0x0007:
            r2 = com.lidroid.xutils.bitmap.BitmapGlobalConfig.this;
            r0 = r2.getBitmapCache();
            if (r0 == 0) goto L_0x0006;
        L_0x000f:
            r2 = 0;
            r2 = r5[r2];	 Catch:{ Throwable -> 0x0020 }
            r2 = (java.lang.Integer) r2;	 Catch:{ Throwable -> 0x0020 }
            r2 = r2.intValue();	 Catch:{ Throwable -> 0x0020 }
            switch(r2) {
                case 0: goto L_0x001c;
                case 1: goto L_0x0029;
                case 2: goto L_0x002d;
                case 3: goto L_0x0031;
                case 4: goto L_0x0038;
                case 5: goto L_0x003c;
                case 6: goto L_0x0040;
                case 7: goto L_0x0044;
                case 8: goto L_0x0052;
                case 9: goto L_0x0060;
                default: goto L_0x001b;
            };	 Catch:{ Throwable -> 0x0020 }
        L_0x001b:
            goto L_0x0006;
        L_0x001c:
            r0.initMemoryCache();	 Catch:{ Throwable -> 0x0020 }
            goto L_0x0006;
        L_0x0020:
            r1 = move-exception;
            r2 = r1.getMessage();
            com.lidroid.xutils.util.LogUtils.e(r2, r1);
            goto L_0x0006;
        L_0x0029:
            r0.initDiskCache();	 Catch:{ Throwable -> 0x0020 }
            goto L_0x0006;
        L_0x002d:
            r0.flush();	 Catch:{ Throwable -> 0x0020 }
            goto L_0x0006;
        L_0x0031:
            r0.clearMemoryCache();	 Catch:{ Throwable -> 0x0020 }
            r0.close();	 Catch:{ Throwable -> 0x0020 }
            goto L_0x0006;
        L_0x0038:
            r0.clearCache();	 Catch:{ Throwable -> 0x0020 }
            goto L_0x0006;
        L_0x003c:
            r0.clearMemoryCache();	 Catch:{ Throwable -> 0x0020 }
            goto L_0x0006;
        L_0x0040:
            r0.clearDiskCache();	 Catch:{ Throwable -> 0x0020 }
            goto L_0x0006;
        L_0x0044:
            r2 = r5.length;	 Catch:{ Throwable -> 0x0020 }
            if (r2 != r3) goto L_0x0006;
        L_0x0047:
            r2 = 1;
            r2 = r5[r2];	 Catch:{ Throwable -> 0x0020 }
            r2 = java.lang.String.valueOf(r2);	 Catch:{ Throwable -> 0x0020 }
            r0.clearCache(r2);	 Catch:{ Throwable -> 0x0020 }
            goto L_0x0006;
        L_0x0052:
            r2 = r5.length;	 Catch:{ Throwable -> 0x0020 }
            if (r2 != r3) goto L_0x0006;
        L_0x0055:
            r2 = 1;
            r2 = r5[r2];	 Catch:{ Throwable -> 0x0020 }
            r2 = java.lang.String.valueOf(r2);	 Catch:{ Throwable -> 0x0020 }
            r0.clearMemoryCache(r2);	 Catch:{ Throwable -> 0x0020 }
            goto L_0x0006;
        L_0x0060:
            r2 = r5.length;	 Catch:{ Throwable -> 0x0020 }
            if (r2 != r3) goto L_0x0006;
        L_0x0063:
            r2 = 1;
            r2 = r5[r2];	 Catch:{ Throwable -> 0x0020 }
            r2 = java.lang.String.valueOf(r2);	 Catch:{ Throwable -> 0x0020 }
            r0.clearDiskCache(r2);	 Catch:{ Throwable -> 0x0020 }
            goto L_0x0006;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.lidroid.xutils.bitmap.BitmapGlobalConfig.BitmapCacheManagementTask.doInBackground(java.lang.Object[]):java.lang.Object[]");
        }

        protected void onPostExecute(Object[] params) {
            if (BitmapGlobalConfig.this.bitmapCacheListener != null && params != null && params.length != 0) {
                try {
                    switch (((Integer) params[0]).intValue()) {
                        case 0:
                            BitmapGlobalConfig.this.bitmapCacheListener.onInitMemoryCacheFinished();
                            return;
                        case 1:
                            BitmapGlobalConfig.this.bitmapCacheListener.onInitDiskFinished();
                            return;
                        case 2:
                            BitmapGlobalConfig.this.bitmapCacheListener.onFlushCacheFinished();
                            return;
                        case 3:
                            BitmapGlobalConfig.this.bitmapCacheListener.onCloseCacheFinished();
                            return;
                        case 4:
                            BitmapGlobalConfig.this.bitmapCacheListener.onClearCacheFinished();
                            return;
                        case 5:
                            BitmapGlobalConfig.this.bitmapCacheListener.onClearMemoryCacheFinished();
                            return;
                        case 6:
                            BitmapGlobalConfig.this.bitmapCacheListener.onClearDiskCacheFinished();
                            return;
                        case 7:
                            if (params.length == 2) {
                                BitmapGlobalConfig.this.bitmapCacheListener.onClearCacheFinished(String.valueOf(params[1]));
                                return;
                            }
                            return;
                        case 8:
                            if (params.length == 2) {
                                BitmapGlobalConfig.this.bitmapCacheListener.onClearMemoryCacheFinished(String.valueOf(params[1]));
                                return;
                            }
                            return;
                        case 9:
                            if (params.length == 2) {
                                BitmapGlobalConfig.this.bitmapCacheListener.onClearDiskCacheFinished(String.valueOf(params[1]));
                                return;
                            }
                            return;
                        default:
                            return;
                    }
                } catch (Throwable e) {
                    LogUtils.e(e.getMessage(), e);
                }
                LogUtils.e(e.getMessage(), e);
            }
        }
    }

    private BitmapGlobalConfig(Context context, String diskCachePath) {
        if (context == null) {
            throw new IllegalArgumentException("context may not be null");
        }
        this.mContext = context;
        this.diskCachePath = diskCachePath;
        initBitmapCache();
    }

    public static synchronized BitmapGlobalConfig getInstance(Context context, String diskCachePath) {
        BitmapGlobalConfig bitmapGlobalConfig;
        synchronized (BitmapGlobalConfig.class) {
            if (TextUtils.isEmpty(diskCachePath)) {
                diskCachePath = OtherUtils.getDiskCacheDir(context, "xBitmapCache");
            }
            if (configMap.containsKey(diskCachePath)) {
                bitmapGlobalConfig = (BitmapGlobalConfig) configMap.get(diskCachePath);
            } else {
                BitmapGlobalConfig config = new BitmapGlobalConfig(context, diskCachePath);
                configMap.put(diskCachePath, config);
                bitmapGlobalConfig = config;
            }
        }
        return bitmapGlobalConfig;
    }

    private void initBitmapCache() {
        new BitmapCacheManagementTask().execute(Integer.valueOf(0));
        new BitmapCacheManagementTask().execute(Integer.valueOf(1));
    }

    public String getDiskCachePath() {
        return this.diskCachePath;
    }

    public Downloader getDownloader() {
        if (this.downloader == null) {
            this.downloader = new DefaultDownloader();
        }
        this.downloader.setContext(this.mContext);
        this.downloader.setDefaultExpiry(getDefaultCacheExpiry());
        this.downloader.setDefaultConnectTimeout(getDefaultConnectTimeout());
        this.downloader.setDefaultReadTimeout(getDefaultReadTimeout());
        return this.downloader;
    }

    public void setDownloader(Downloader downloader) {
        this.downloader = downloader;
    }

    public long getDefaultCacheExpiry() {
        return this.defaultCacheExpiry;
    }

    public void setDefaultCacheExpiry(long defaultCacheExpiry) {
        this.defaultCacheExpiry = defaultCacheExpiry;
    }

    public int getDefaultConnectTimeout() {
        return this.defaultConnectTimeout;
    }

    public void setDefaultConnectTimeout(int defaultConnectTimeout) {
        this.defaultConnectTimeout = defaultConnectTimeout;
    }

    public int getDefaultReadTimeout() {
        return this.defaultReadTimeout;
    }

    public void setDefaultReadTimeout(int defaultReadTimeout) {
        this.defaultReadTimeout = defaultReadTimeout;
    }

    public BitmapCache getBitmapCache() {
        if (this.bitmapCache == null) {
            this.bitmapCache = new BitmapCache(this);
        }
        return this.bitmapCache;
    }

    public int getMemoryCacheSize() {
        return this.memoryCacheSize;
    }

    public void setMemoryCacheSize(int memoryCacheSize) {
        if (memoryCacheSize >= 2097152) {
            this.memoryCacheSize = memoryCacheSize;
            if (this.bitmapCache != null) {
                this.bitmapCache.setMemoryCacheSize(this.memoryCacheSize);
                return;
            }
            return;
        }
        setMemCacheSizePercent(0.3f);
    }

    public void setMemCacheSizePercent(float percent) {
        if (percent < 0.05f || percent > 0.8f) {
            throw new IllegalArgumentException("percent must be between 0.05 and 0.8 (inclusive)");
        }
        this.memoryCacheSize = Math.round(((((float) getMemoryClass()) * percent) * 1024.0f) * 1024.0f);
        if (this.bitmapCache != null) {
            this.bitmapCache.setMemoryCacheSize(this.memoryCacheSize);
        }
    }

    public int getDiskCacheSize() {
        return this.diskCacheSize;
    }

    public void setDiskCacheSize(int diskCacheSize) {
        if (diskCacheSize >= MIN_DISK_CACHE_SIZE) {
            this.diskCacheSize = diskCacheSize;
            if (this.bitmapCache != null) {
                this.bitmapCache.setDiskCacheSize(this.diskCacheSize);
            }
        }
    }

    public int getThreadPoolSize() {
        return BITMAP_LOAD_EXECUTOR.getPoolSize();
    }

    public void setThreadPoolSize(int threadPoolSize) {
        BITMAP_LOAD_EXECUTOR.setPoolSize(threadPoolSize);
    }

    public PriorityExecutor getBitmapLoadExecutor() {
        return BITMAP_LOAD_EXECUTOR;
    }

    public PriorityExecutor getDiskCacheExecutor() {
        return DISK_CACHE_EXECUTOR;
    }

    public boolean isMemoryCacheEnabled() {
        return this.memoryCacheEnabled;
    }

    public void setMemoryCacheEnabled(boolean memoryCacheEnabled) {
        this.memoryCacheEnabled = memoryCacheEnabled;
    }

    public boolean isDiskCacheEnabled() {
        return this.diskCacheEnabled;
    }

    public void setDiskCacheEnabled(boolean diskCacheEnabled) {
        this.diskCacheEnabled = diskCacheEnabled;
    }

    public FileNameGenerator getFileNameGenerator() {
        return this.fileNameGenerator;
    }

    public void setFileNameGenerator(FileNameGenerator fileNameGenerator) {
        this.fileNameGenerator = fileNameGenerator;
        if (this.bitmapCache != null) {
            this.bitmapCache.setDiskCacheFileNameGenerator(fileNameGenerator);
        }
    }

    public BitmapCacheListener getBitmapCacheListener() {
        return this.bitmapCacheListener;
    }

    public void setBitmapCacheListener(BitmapCacheListener bitmapCacheListener) {
        this.bitmapCacheListener = bitmapCacheListener;
    }

    private int getMemoryClass() {
        return ((ActivityManager) this.mContext.getSystemService("activity")).getMemoryClass();
    }

    public void clearCache() {
        new BitmapCacheManagementTask().execute(Integer.valueOf(4));
    }

    public void clearMemoryCache() {
        new BitmapCacheManagementTask().execute(Integer.valueOf(5));
    }

    public void clearDiskCache() {
        new BitmapCacheManagementTask().execute(Integer.valueOf(6));
    }

    public void clearCache(String uri) {
        new BitmapCacheManagementTask().execute(Integer.valueOf(7), uri);
    }

    public void clearMemoryCache(String uri) {
        new BitmapCacheManagementTask().execute(Integer.valueOf(8), uri);
    }

    public void clearDiskCache(String uri) {
        new BitmapCacheManagementTask().execute(Integer.valueOf(9), uri);
    }

    public void flushCache() {
        new BitmapCacheManagementTask().execute(Integer.valueOf(2));
    }

    public void closeCache() {
        new BitmapCacheManagementTask().execute(Integer.valueOf(3));
    }
}
