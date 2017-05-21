package com.lidroid.xutils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.Animation;
import com.lidroid.xutils.bitmap.BitmapCacheListener;
import com.lidroid.xutils.bitmap.BitmapCommonUtils;
import com.lidroid.xutils.bitmap.BitmapDisplayConfig;
import com.lidroid.xutils.bitmap.BitmapGlobalConfig;
import com.lidroid.xutils.bitmap.callback.BitmapLoadCallBack;
import com.lidroid.xutils.bitmap.callback.BitmapLoadFrom;
import com.lidroid.xutils.bitmap.callback.DefaultBitmapLoadCallBack;
import com.lidroid.xutils.bitmap.core.AsyncDrawable;
import com.lidroid.xutils.bitmap.core.BitmapSize;
import com.lidroid.xutils.bitmap.download.Downloader;
import com.lidroid.xutils.cache.FileNameGenerator;
import com.lidroid.xutils.task.PriorityAsyncTask;
import com.lidroid.xutils.task.PriorityExecutor;
import com.lidroid.xutils.task.TaskHandler;
import java.io.File;
import java.lang.ref.WeakReference;

public class BitmapUtils implements TaskHandler {
    private boolean cancelAllTask;
    private Context context;
    private BitmapDisplayConfig defaultDisplayConfig;
    private BitmapGlobalConfig globalConfig;
    private boolean pauseTask;
    private final Object pauseTaskLock;

    public class BitmapLoadTask<T extends View> extends PriorityAsyncTask<Object, Object, Bitmap> {
        private static final int PROGRESS_LOADING = 1;
        private static final int PROGRESS_LOAD_STARTED = 0;
        private final BitmapLoadCallBack<T> callBack;
        private final WeakReference<T> containerReference;
        private final BitmapDisplayConfig displayConfig;
        private BitmapLoadFrom from = BitmapLoadFrom.DISK_CACHE;
        private final String uri;

        public BitmapLoadTask(T container, String uri, BitmapDisplayConfig config, BitmapLoadCallBack<T> callBack) {
            if (container == null || uri == null || config == null || callBack == null) {
                throw new IllegalArgumentException("args may not be null");
            }
            this.containerReference = new WeakReference(container);
            this.callBack = callBack;
            this.uri = uri;
            this.displayConfig = config;
        }

        /* JADX WARNING: inconsistent code. */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        protected android.graphics.Bitmap doInBackground(java.lang.Object... r5) {
            /*
            r4 = this;
            r3 = 0;
            r1 = com.lidroid.xutils.BitmapUtils.this;
            r2 = r1.pauseTaskLock;
            monitor-enter(r2);
        L_0x0008:
            r1 = com.lidroid.xutils.BitmapUtils.this;	 Catch:{ all -> 0x007b }
            r1 = r1.pauseTask;	 Catch:{ all -> 0x007b }
            if (r1 == 0) goto L_0x0016;
        L_0x0010:
            r1 = r4.isCancelled();	 Catch:{ all -> 0x007b }
            if (r1 == 0) goto L_0x0067;
        L_0x0016:
            monitor-exit(r2);	 Catch:{ all -> 0x007b }
            r0 = 0;
            r1 = r4.isCancelled();
            if (r1 != 0) goto L_0x0042;
        L_0x001e:
            r1 = r4.getTargetContainer();
            if (r1 == 0) goto L_0x0042;
        L_0x0024:
            r1 = 1;
            r1 = new java.lang.Object[r1];
            r2 = java.lang.Integer.valueOf(r3);
            r1[r3] = r2;
            r4.publishProgress(r1);
            r1 = com.lidroid.xutils.BitmapUtils.this;
            r1 = r1.globalConfig;
            r1 = r1.getBitmapCache();
            r2 = r4.uri;
            r3 = r4.displayConfig;
            r0 = r1.getBitmapFromDiskCache(r2, r3);
        L_0x0042:
            if (r0 != 0) goto L_0x0066;
        L_0x0044:
            r1 = r4.isCancelled();
            if (r1 != 0) goto L_0x0066;
        L_0x004a:
            r1 = r4.getTargetContainer();
            if (r1 == 0) goto L_0x0066;
        L_0x0050:
            r1 = com.lidroid.xutils.BitmapUtils.this;
            r1 = r1.globalConfig;
            r1 = r1.getBitmapCache();
            r2 = r4.uri;
            r3 = r4.displayConfig;
            r0 = r1.downloadBitmap(r2, r3, r4);
            r1 = com.lidroid.xutils.bitmap.callback.BitmapLoadFrom.URI;
            r4.from = r1;
        L_0x0066:
            return r0;
        L_0x0067:
            r1 = com.lidroid.xutils.BitmapUtils.this;	 Catch:{ Throwable -> 0x007e }
            r1 = r1.pauseTaskLock;	 Catch:{ Throwable -> 0x007e }
            r1.wait();	 Catch:{ Throwable -> 0x007e }
            r1 = com.lidroid.xutils.BitmapUtils.this;	 Catch:{ Throwable -> 0x007e }
            r1 = r1.cancelAllTask;	 Catch:{ Throwable -> 0x007e }
            if (r1 == 0) goto L_0x0008;
        L_0x0078:
            monitor-exit(r2);	 Catch:{ all -> 0x007b }
            r0 = 0;
            goto L_0x0066;
        L_0x007b:
            r1 = move-exception;
            monitor-exit(r2);	 Catch:{ all -> 0x007b }
            throw r1;
        L_0x007e:
            r1 = move-exception;
            goto L_0x0008;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.lidroid.xutils.BitmapUtils.BitmapLoadTask.doInBackground(java.lang.Object[]):android.graphics.Bitmap");
        }

        public void updateProgress(long total, long current) {
            publishProgress(Integer.valueOf(1), Long.valueOf(total), Long.valueOf(current));
        }

        protected void onProgressUpdate(Object... values) {
            if (values != null && values.length != 0) {
                T container = getTargetContainer();
                if (container != null) {
                    switch (((Integer) values[0]).intValue()) {
                        case 0:
                            this.callBack.onLoadStarted(container, this.uri, this.displayConfig);
                            return;
                        case 1:
                            if (values.length == 3) {
                                this.callBack.onLoading(container, this.uri, this.displayConfig, ((Long) values[1]).longValue(), ((Long) values[2]).longValue());
                                return;
                            }
                            return;
                        default:
                            return;
                    }
                }
            }
        }

        protected void onPostExecute(Bitmap bitmap) {
            T container = getTargetContainer();
            if (container == null) {
                return;
            }
            if (bitmap != null) {
                this.callBack.onLoadCompleted(container, this.uri, bitmap, this.displayConfig, this.from);
                return;
            }
            this.callBack.onLoadFailed(container, this.uri, this.displayConfig.getLoadFailedDrawable());
        }

        protected void onCancelled(Bitmap bitmap) {
            synchronized (BitmapUtils.this.pauseTaskLock) {
                BitmapUtils.this.pauseTaskLock.notifyAll();
            }
        }

        public T getTargetContainer() {
            View container = (View) this.containerReference.get();
            return this == BitmapUtils.getBitmapTaskFromContainer(container, this.callBack) ? container : null;
        }
    }

    public BitmapUtils(Context context) {
        this(context, null);
    }

    public BitmapUtils(Context context, String diskCachePath) {
        this.pauseTask = false;
        this.cancelAllTask = false;
        this.pauseTaskLock = new Object();
        if (context == null) {
            throw new IllegalArgumentException("context may not be null");
        }
        this.context = context.getApplicationContext();
        this.globalConfig = BitmapGlobalConfig.getInstance(this.context, diskCachePath);
        this.defaultDisplayConfig = new BitmapDisplayConfig();
    }

    public BitmapUtils(Context context, String diskCachePath, int memoryCacheSize) {
        this(context, diskCachePath);
        this.globalConfig.setMemoryCacheSize(memoryCacheSize);
    }

    public BitmapUtils(Context context, String diskCachePath, int memoryCacheSize, int diskCacheSize) {
        this(context, diskCachePath);
        this.globalConfig.setMemoryCacheSize(memoryCacheSize);
        this.globalConfig.setDiskCacheSize(diskCacheSize);
    }

    public BitmapUtils(Context context, String diskCachePath, float memoryCachePercent) {
        this(context, diskCachePath);
        this.globalConfig.setMemCacheSizePercent(memoryCachePercent);
    }

    public BitmapUtils(Context context, String diskCachePath, float memoryCachePercent, int diskCacheSize) {
        this(context, diskCachePath);
        this.globalConfig.setMemCacheSizePercent(memoryCachePercent);
        this.globalConfig.setDiskCacheSize(diskCacheSize);
    }

    public BitmapUtils configDefaultLoadingImage(Drawable drawable) {
        this.defaultDisplayConfig.setLoadingDrawable(drawable);
        return this;
    }

    public BitmapUtils configDefaultLoadingImage(int resId) {
        this.defaultDisplayConfig.setLoadingDrawable(this.context.getResources().getDrawable(resId));
        return this;
    }

    public BitmapUtils configDefaultLoadingImage(Bitmap bitmap) {
        this.defaultDisplayConfig.setLoadingDrawable(new BitmapDrawable(this.context.getResources(), bitmap));
        return this;
    }

    public BitmapUtils configDefaultLoadFailedImage(Drawable drawable) {
        this.defaultDisplayConfig.setLoadFailedDrawable(drawable);
        return this;
    }

    public BitmapUtils configDefaultLoadFailedImage(int resId) {
        this.defaultDisplayConfig.setLoadFailedDrawable(this.context.getResources().getDrawable(resId));
        return this;
    }

    public BitmapUtils configDefaultLoadFailedImage(Bitmap bitmap) {
        this.defaultDisplayConfig.setLoadFailedDrawable(new BitmapDrawable(this.context.getResources(), bitmap));
        return this;
    }

    public BitmapUtils configDefaultBitmapMaxSize(int maxWidth, int maxHeight) {
        this.defaultDisplayConfig.setBitmapMaxSize(new BitmapSize(maxWidth, maxHeight));
        return this;
    }

    public BitmapUtils configDefaultBitmapMaxSize(BitmapSize maxSize) {
        this.defaultDisplayConfig.setBitmapMaxSize(maxSize);
        return this;
    }

    public BitmapUtils configDefaultImageLoadAnimation(Animation animation) {
        this.defaultDisplayConfig.setAnimation(animation);
        return this;
    }

    public BitmapUtils configDefaultAutoRotation(boolean autoRotation) {
        this.defaultDisplayConfig.setAutoRotation(autoRotation);
        return this;
    }

    public BitmapUtils configDefaultShowOriginal(boolean showOriginal) {
        this.defaultDisplayConfig.setShowOriginal(showOriginal);
        return this;
    }

    public BitmapUtils configDefaultBitmapConfig(Config config) {
        this.defaultDisplayConfig.setBitmapConfig(config);
        return this;
    }

    public BitmapUtils configDefaultDisplayConfig(BitmapDisplayConfig displayConfig) {
        this.defaultDisplayConfig = displayConfig;
        return this;
    }

    public BitmapUtils configDownloader(Downloader downloader) {
        this.globalConfig.setDownloader(downloader);
        return this;
    }

    public BitmapUtils configDefaultCacheExpiry(long defaultExpiry) {
        this.globalConfig.setDefaultCacheExpiry(defaultExpiry);
        return this;
    }

    public BitmapUtils configDefaultConnectTimeout(int connectTimeout) {
        this.globalConfig.setDefaultConnectTimeout(connectTimeout);
        return this;
    }

    public BitmapUtils configDefaultReadTimeout(int readTimeout) {
        this.globalConfig.setDefaultReadTimeout(readTimeout);
        return this;
    }

    public BitmapUtils configThreadPoolSize(int threadPoolSize) {
        this.globalConfig.setThreadPoolSize(threadPoolSize);
        return this;
    }

    public BitmapUtils configMemoryCacheEnabled(boolean enabled) {
        this.globalConfig.setMemoryCacheEnabled(enabled);
        return this;
    }

    public BitmapUtils configDiskCacheEnabled(boolean enabled) {
        this.globalConfig.setDiskCacheEnabled(enabled);
        return this;
    }

    public BitmapUtils configDiskCacheFileNameGenerator(FileNameGenerator fileNameGenerator) {
        this.globalConfig.setFileNameGenerator(fileNameGenerator);
        return this;
    }

    public BitmapUtils configBitmapCacheListener(BitmapCacheListener listener) {
        this.globalConfig.setBitmapCacheListener(listener);
        return this;
    }

    public <T extends View> void display(T container, String uri) {
        display(container, uri, null, null);
    }

    public <T extends View> void display(T container, String uri, BitmapDisplayConfig displayConfig) {
        display(container, uri, displayConfig, null);
    }

    public <T extends View> void display(T container, String uri, BitmapLoadCallBack<T> callBack) {
        display(container, uri, null, callBack);
    }

    public <T extends View> void display(T container, String uri, BitmapDisplayConfig displayConfig, BitmapLoadCallBack<T> callBack) {
        if (container != null) {
            if (callBack == null) {
                callBack = new DefaultBitmapLoadCallBack();
            }
            if (displayConfig == null || displayConfig == this.defaultDisplayConfig) {
                displayConfig = this.defaultDisplayConfig.cloneNew();
            }
            BitmapSize size = displayConfig.getBitmapMaxSize();
            displayConfig.setBitmapMaxSize(BitmapCommonUtils.optimizeMaxSizeByView(container, size.getWidth(), size.getHeight()));
            container.clearAnimation();
            if (TextUtils.isEmpty(uri)) {
                callBack.onLoadFailed(container, uri, displayConfig.getLoadFailedDrawable());
                return;
            }
            callBack.onPreLoad(container, uri, displayConfig);
            Bitmap bitmap = this.globalConfig.getBitmapCache().getBitmapFromMemCache(uri, displayConfig);
            if (bitmap != null) {
                callBack.onLoadStarted(container, uri, displayConfig);
                callBack.onLoadCompleted(container, uri, bitmap, displayConfig, BitmapLoadFrom.MEMORY_CACHE);
            } else if (!bitmapLoadTaskExist(container, uri, callBack)) {
                BitmapLoadTask<T> loadTask = new BitmapLoadTask(container, uri, displayConfig, callBack);
                PriorityExecutor executor = this.globalConfig.getBitmapLoadExecutor();
                File diskCacheFile = getBitmapFileFromDiskCache(uri);
                boolean diskCacheExist = diskCacheFile != null && diskCacheFile.exists();
                if (diskCacheExist && executor.isBusy()) {
                    executor = this.globalConfig.getDiskCacheExecutor();
                }
                callBack.setDrawable(container, new AsyncDrawable(displayConfig.getLoadingDrawable(), loadTask));
                loadTask.setPriority(displayConfig.getPriority());
                loadTask.executeOnExecutor(executor, new Object[0]);
            }
        }
    }

    public void clearCache() {
        this.globalConfig.clearCache();
    }

    public void clearMemoryCache() {
        this.globalConfig.clearMemoryCache();
    }

    public void clearDiskCache() {
        this.globalConfig.clearDiskCache();
    }

    public void clearCache(String uri) {
        this.globalConfig.clearCache(uri);
    }

    public void clearMemoryCache(String uri) {
        this.globalConfig.clearMemoryCache(uri);
    }

    public void clearDiskCache(String uri) {
        this.globalConfig.clearDiskCache(uri);
    }

    public void flushCache() {
        this.globalConfig.flushCache();
    }

    public void closeCache() {
        this.globalConfig.closeCache();
    }

    public File getBitmapFileFromDiskCache(String uri) {
        return this.globalConfig.getBitmapCache().getBitmapFileFromDiskCache(uri);
    }

    public Bitmap getBitmapFromMemCache(String uri, BitmapDisplayConfig config) {
        if (config == null) {
            config = this.defaultDisplayConfig;
        }
        return this.globalConfig.getBitmapCache().getBitmapFromMemCache(uri, config);
    }

    public boolean supportPause() {
        return true;
    }

    public boolean supportResume() {
        return true;
    }

    public boolean supportCancel() {
        return true;
    }

    public void pause() {
        this.pauseTask = true;
        flushCache();
    }

    public void resume() {
        this.pauseTask = false;
        synchronized (this.pauseTaskLock) {
            this.pauseTaskLock.notifyAll();
        }
    }

    public void cancel() {
        this.pauseTask = true;
        this.cancelAllTask = true;
        synchronized (this.pauseTaskLock) {
            this.pauseTaskLock.notifyAll();
        }
    }

    public boolean isPaused() {
        return this.pauseTask;
    }

    public boolean isCancelled() {
        return this.cancelAllTask;
    }

    private static <T extends View> BitmapLoadTask<T> getBitmapTaskFromContainer(T container, BitmapLoadCallBack<T> callBack) {
        if (container != null) {
            Drawable drawable = callBack.getDrawable(container);
            if (drawable instanceof AsyncDrawable) {
                return ((AsyncDrawable) drawable).getBitmapWorkerTask();
            }
        }
        return null;
    }

    private static <T extends View> boolean bitmapLoadTaskExist(T container, String uri, BitmapLoadCallBack<T> callBack) {
        BitmapLoadTask<T> oldLoadTask = getBitmapTaskFromContainer(container, callBack);
        if (oldLoadTask != null) {
            String oldUrl = oldLoadTask.uri;
            if (!TextUtils.isEmpty(oldUrl) && oldUrl.equals(uri)) {
                return true;
            }
            oldLoadTask.cancel(true);
        }
        return false;
    }
}
