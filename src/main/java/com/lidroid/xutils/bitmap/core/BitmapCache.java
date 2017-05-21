package com.lidroid.xutils.bitmap.core;

import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.media.ExifInterface;
import com.lidroid.xutils.BitmapUtils.BitmapLoadTask;
import com.lidroid.xutils.bitmap.BitmapDisplayConfig;
import com.lidroid.xutils.bitmap.BitmapGlobalConfig;
import com.lidroid.xutils.bitmap.factory.BitmapFactory;
import com.lidroid.xutils.cache.FileNameGenerator;
import com.lidroid.xutils.cache.LruDiskCache;
import com.lidroid.xutils.cache.LruDiskCache.Editor;
import com.lidroid.xutils.cache.LruMemoryCache;
import com.lidroid.xutils.util.IOUtils;
import com.lidroid.xutils.util.LogUtils;
import com.lidroid.xutils.util.OtherUtils;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

public class BitmapCache {
    private final int DISK_CACHE_INDEX = 0;
    private BitmapGlobalConfig globalConfig;
    private final Object mDiskCacheLock = new Object();
    private LruDiskCache mDiskLruCache;
    private LruMemoryCache<MemoryCacheKey, Bitmap> mMemoryCache;

    private class BitmapMeta {
        public byte[] data;
        public long expiryTimestamp;
        public FileInputStream inputStream;

        private BitmapMeta() {
        }
    }

    public class MemoryCacheKey {
        private String subKey;
        private String uri;

        private MemoryCacheKey(String uri, BitmapDisplayConfig config) {
            this.uri = uri;
            this.subKey = config == null ? null : config.toString();
        }

        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (!(o instanceof MemoryCacheKey)) {
                return false;
            }
            MemoryCacheKey that = (MemoryCacheKey) o;
            if (!this.uri.equals(that.uri)) {
                return false;
            }
            if (this.subKey == null || that.subKey == null) {
                return true;
            }
            return this.subKey.equals(that.subKey);
        }

        public int hashCode() {
            return this.uri.hashCode();
        }
    }

    public BitmapCache(BitmapGlobalConfig globalConfig) {
        if (globalConfig == null) {
            throw new IllegalArgumentException("globalConfig may not be null");
        }
        this.globalConfig = globalConfig;
    }

    public void initMemoryCache() {
        if (this.globalConfig.isMemoryCacheEnabled()) {
            if (this.mMemoryCache != null) {
                try {
                    clearMemoryCache();
                } catch (Throwable th) {
                }
            }
            this.mMemoryCache = new LruMemoryCache<MemoryCacheKey, Bitmap>(this.globalConfig.getMemoryCacheSize()) {
                protected int sizeOf(MemoryCacheKey key, Bitmap bitmap) {
                    if (bitmap == null) {
                        return 0;
                    }
                    return bitmap.getRowBytes() * bitmap.getHeight();
                }
            };
        }
    }

    public void initDiskCache() {
        synchronized (this.mDiskCacheLock) {
            if (this.globalConfig.isDiskCacheEnabled() && (this.mDiskLruCache == null || this.mDiskLruCache.isClosed())) {
                File diskCacheDir = new File(this.globalConfig.getDiskCachePath());
                if (diskCacheDir.exists() || diskCacheDir.mkdirs()) {
                    long availableSpace = OtherUtils.getAvailableSpace(diskCacheDir);
                    long diskCacheSize = (long) this.globalConfig.getDiskCacheSize();
                    if (availableSpace <= diskCacheSize) {
                        diskCacheSize = availableSpace;
                    }
                    try {
                        this.mDiskLruCache = LruDiskCache.open(diskCacheDir, 1, 1, diskCacheSize);
                        this.mDiskLruCache.setFileNameGenerator(this.globalConfig.getFileNameGenerator());
                        LogUtils.d("create disk cache success");
                    } catch (Throwable e) {
                        this.mDiskLruCache = null;
                        LogUtils.e("create disk cache error", e);
                    }
                }
            }
        }
    }

    public void setMemoryCacheSize(int maxSize) {
        if (this.mMemoryCache != null) {
            this.mMemoryCache.setMaxSize(maxSize);
        }
    }

    public void setDiskCacheSize(int maxSize) {
        synchronized (this.mDiskCacheLock) {
            if (this.mDiskLruCache != null) {
                this.mDiskLruCache.setMaxSize((long) maxSize);
            }
        }
    }

    public void setDiskCacheFileNameGenerator(FileNameGenerator fileNameGenerator) {
        synchronized (this.mDiskCacheLock) {
            if (!(this.mDiskLruCache == null || fileNameGenerator == null)) {
                this.mDiskLruCache.setFileNameGenerator(fileNameGenerator);
            }
        }
    }

    public Bitmap downloadBitmap(String uri, BitmapDisplayConfig config, BitmapLoadTask<?> task) {
        OutputStream outputStream;
        BitmapMeta bitmapMeta = new BitmapMeta();
        Closeable outputStream2 = null;
        Closeable snapshot = null;
        Bitmap bitmap = null;
        try {
            if (this.globalConfig.isDiskCacheEnabled()) {
                if (this.mDiskLruCache == null) {
                    initDiskCache();
                }
                if (this.mDiskLruCache != null) {
                    snapshot = this.mDiskLruCache.get(uri);
                    if (snapshot == null) {
                        Editor editor = this.mDiskLruCache.edit(uri);
                        if (editor != null) {
                            outputStream2 = editor.newOutputStream(0);
                            bitmapMeta.expiryTimestamp = this.globalConfig.getDownloader().downloadToStream(uri, outputStream2, task);
                            if (bitmapMeta.expiryTimestamp < 0) {
                                editor.abort();
                                IOUtils.closeQuietly(outputStream2);
                                IOUtils.closeQuietly(snapshot);
                                return null;
                            }
                            editor.setEntryExpiryTimestamp(bitmapMeta.expiryTimestamp);
                            editor.commit();
                            snapshot = this.mDiskLruCache.get(uri);
                        }
                    }
                    if (snapshot != null) {
                        bitmapMeta.inputStream = snapshot.getInputStream(0);
                        bitmap = decodeBitmapMeta(bitmapMeta, config);
                        if (bitmap == null) {
                            bitmapMeta.inputStream = null;
                            this.mDiskLruCache.remove(uri);
                            outputStream = outputStream;
                            if (bitmap != null) {
                                try {
                                    outputStream2 = new ByteArrayOutputStream();
                                    bitmapMeta.expiryTimestamp = this.globalConfig.getDownloader().downloadToStream(uri, outputStream2, task);
                                    if (bitmapMeta.expiryTimestamp >= 0) {
                                        IOUtils.closeQuietly(outputStream2);
                                        IOUtils.closeQuietly(snapshot);
                                        return null;
                                    }
                                    bitmapMeta.data = ((ByteArrayOutputStream) outputStream2).toByteArray();
                                    bitmap = decodeBitmapMeta(bitmapMeta, config);
                                } catch (Throwable th) {
                                    th = th;
                                    outputStream = outputStream;
                                    IOUtils.closeQuietly(outputStream2);
                                    IOUtils.closeQuietly(snapshot);
                                    throw th;
                                }
                            }
                            Object outputStream3;
                            outputStream3 = outputStream;
                            if (bitmap != null) {
                                bitmap = addBitmapToMemoryCache(uri, config, rotateBitmapIfNeeded(uri, config, bitmap), bitmapMeta.expiryTimestamp);
                            }
                            IOUtils.closeQuietly(outputStream2);
                            IOUtils.closeQuietly(snapshot);
                            return bitmap;
                        }
                    }
                }
            }
        } catch (Throwable th2) {
            e = th2;
            LogUtils.e(e.getMessage(), e);
            IOUtils.closeQuietly(outputStream2);
            IOUtils.closeQuietly(snapshot);
            return null;
        }
        outputStream = outputStream;
        if (bitmap != null) {
            outputStream3 = outputStream;
        } else {
            outputStream2 = new ByteArrayOutputStream();
            bitmapMeta.expiryTimestamp = this.globalConfig.getDownloader().downloadToStream(uri, outputStream2, task);
            if (bitmapMeta.expiryTimestamp >= 0) {
                bitmapMeta.data = ((ByteArrayOutputStream) outputStream2).toByteArray();
                bitmap = decodeBitmapMeta(bitmapMeta, config);
            } else {
                IOUtils.closeQuietly(outputStream2);
                IOUtils.closeQuietly(snapshot);
                return null;
            }
        }
        if (bitmap != null) {
            bitmap = addBitmapToMemoryCache(uri, config, rotateBitmapIfNeeded(uri, config, bitmap), bitmapMeta.expiryTimestamp);
        }
        IOUtils.closeQuietly(outputStream2);
        IOUtils.closeQuietly(snapshot);
        return bitmap;
    }

    private Bitmap addBitmapToMemoryCache(String uri, BitmapDisplayConfig config, Bitmap bitmap, long expiryTimestamp) throws IOException {
        if (config != null) {
            BitmapFactory bitmapFactory = config.getBitmapFactory();
            if (bitmapFactory != null) {
                bitmap = bitmapFactory.cloneNew().createBitmap(bitmap);
            }
        }
        if (!(uri == null || bitmap == null || !this.globalConfig.isMemoryCacheEnabled() || this.mMemoryCache == null)) {
            this.mMemoryCache.put(new MemoryCacheKey(uri, config), bitmap, expiryTimestamp);
        }
        return bitmap;
    }

    public Bitmap getBitmapFromMemCache(String uri, BitmapDisplayConfig config) {
        if (this.mMemoryCache == null || !this.globalConfig.isMemoryCacheEnabled()) {
            return null;
        }
        return (Bitmap) this.mMemoryCache.get(new MemoryCacheKey(uri, config));
    }

    public File getBitmapFileFromDiskCache(String uri) {
        synchronized (this.mDiskCacheLock) {
            if (this.mDiskLruCache != null) {
                File cacheFile = this.mDiskLruCache.getCacheFile(uri, 0);
                return cacheFile;
            }
            return null;
        }
    }

    public Bitmap getBitmapFromDiskCache(String uri, BitmapDisplayConfig config) {
        if (uri == null || !this.globalConfig.isDiskCacheEnabled()) {
            return null;
        }
        if (this.mDiskLruCache == null) {
            initDiskCache();
        }
        if (this.mDiskLruCache != null) {
            Closeable snapshot = null;
            try {
                snapshot = this.mDiskLruCache.get(uri);
                if (snapshot != null) {
                    Bitmap bitmap = null;
                    if (config == null || config.isShowOriginal()) {
                        bitmap = BitmapDecoder.decodeFileDescriptor(snapshot.getInputStream(0).getFD());
                    } else {
                        bitmap = BitmapDecoder.decodeSampledBitmapFromDescriptor(snapshot.getInputStream(0).getFD(), config.getBitmapMaxSize(), config.getBitmapConfig());
                    }
                    bitmap = addBitmapToMemoryCache(uri, config, rotateBitmapIfNeeded(uri, config, bitmap), this.mDiskLruCache.getExpiryTimestamp(uri));
                    return bitmap;
                }
                IOUtils.closeQuietly(snapshot);
            } catch (Throwable e) {
                LogUtils.e(e.getMessage(), e);
            } finally {
                IOUtils.closeQuietly(snapshot);
            }
        }
        return null;
    }

    public void clearCache() {
        clearMemoryCache();
        clearDiskCache();
    }

    public void clearMemoryCache() {
        if (this.mMemoryCache != null) {
            this.mMemoryCache.evictAll();
        }
    }

    public void clearDiskCache() {
        synchronized (this.mDiskCacheLock) {
            if (!(this.mDiskLruCache == null || this.mDiskLruCache.isClosed())) {
                try {
                    this.mDiskLruCache.delete();
                    this.mDiskLruCache.close();
                } catch (Throwable e) {
                    LogUtils.e(e.getMessage(), e);
                }
                this.mDiskLruCache = null;
            }
        }
        initDiskCache();
    }

    public void clearCache(String uri) {
        clearMemoryCache(uri);
        clearDiskCache(uri);
    }

    public void clearMemoryCache(String uri) {
        MemoryCacheKey key = new MemoryCacheKey(uri, null);
        if (this.mMemoryCache != null) {
            while (this.mMemoryCache.containsKey(key)) {
                this.mMemoryCache.remove(key);
            }
        }
    }

    public void clearDiskCache(String uri) {
        synchronized (this.mDiskCacheLock) {
            if (!(this.mDiskLruCache == null || this.mDiskLruCache.isClosed())) {
                try {
                    this.mDiskLruCache.remove(uri);
                } catch (Throwable e) {
                    LogUtils.e(e.getMessage(), e);
                }
            }
        }
    }

    public void flush() {
        synchronized (this.mDiskCacheLock) {
            if (this.mDiskLruCache != null) {
                try {
                    this.mDiskLruCache.flush();
                } catch (Throwable e) {
                    LogUtils.e(e.getMessage(), e);
                }
            }
        }
    }

    public void close() {
        synchronized (this.mDiskCacheLock) {
            if (this.mDiskLruCache != null) {
                try {
                    if (!this.mDiskLruCache.isClosed()) {
                        this.mDiskLruCache.close();
                    }
                } catch (Throwable e) {
                    LogUtils.e(e.getMessage(), e);
                }
                this.mDiskLruCache = null;
            }
        }
    }

    private Bitmap decodeBitmapMeta(BitmapMeta bitmapMeta, BitmapDisplayConfig config) throws IOException {
        if (bitmapMeta == null) {
            return null;
        }
        if (bitmapMeta.inputStream != null) {
            if (config == null || config.isShowOriginal()) {
                return BitmapDecoder.decodeFileDescriptor(bitmapMeta.inputStream.getFD());
            }
            return BitmapDecoder.decodeSampledBitmapFromDescriptor(bitmapMeta.inputStream.getFD(), config.getBitmapMaxSize(), config.getBitmapConfig());
        } else if (bitmapMeta.data == null) {
            return null;
        } else {
            if (config == null || config.isShowOriginal()) {
                return BitmapDecoder.decodeByteArray(bitmapMeta.data);
            }
            return BitmapDecoder.decodeSampledBitmapFromByteArray(bitmapMeta.data, config.getBitmapMaxSize(), config.getBitmapConfig());
        }
    }

    private synchronized Bitmap rotateBitmapIfNeeded(String uri, BitmapDisplayConfig config, Bitmap bitmap) {
        Bitmap result;
        Bitmap result2 = bitmap;
        if (config != null) {
            if (config.isAutoRotation()) {
                File bitmapFile = getBitmapFileFromDiskCache(uri);
                if (bitmapFile != null && bitmapFile.exists()) {
                    try {
                        int angle;
                        switch (new ExifInterface(bitmapFile.getPath()).getAttributeInt("Orientation", 0)) {
                            case 3:
                                angle = 180;
                                break;
                            case 6:
                                angle = 90;
                                break;
                            case 8:
                                angle = 270;
                                break;
                            default:
                                angle = 0;
                                break;
                        }
                        if (angle != 0) {
                            Matrix m = new Matrix();
                            m.postRotate((float) angle);
                            result2 = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), m, true);
                            bitmap.recycle();
                        }
                    } catch (Throwable th) {
                        result = result2;
                    }
                }
            }
        }
        result = result2;
        return result;
    }
}
