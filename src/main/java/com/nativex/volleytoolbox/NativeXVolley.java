package com.nativex.volleytoolbox;

import android.annotation.TargetApi;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.os.Build.VERSION;
import com.nativex.monetization.communication.ServerRequestManager;
import com.nativex.monetization.manager.MonetizationSharedDataManager;
import com.nativex.network.volley.Cache.Entry;
import com.nativex.network.volley.RequestQueue;
import com.nativex.network.volley.toolbox.DiskBasedCache;
import com.nativex.network.volley.toolbox.ImageLoader;
import com.nativex.network.volley.toolbox.ImageLoader.ImageCache;
import com.nativex.network.volley.toolbox.Volley;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.nio.ByteBuffer;

public class NativeXVolley {
    private static NativeXVolley instance = null;
    private static DiskBitmapCache mDiskCache;
    private static ImageLoader mImageLoader;
    private static RequestQueue mRequestQueue;
    private static RequestQueue mSSLRequestQueue;

    public class DiskBitmapCache extends DiskBasedCache implements ImageCache {
        public DiskBitmapCache(File cacheDir) {
            super(cacheDir);
        }

        public Bitmap getBitmap(String url) {
            Entry requestedItem = get(url);
            if (requestedItem == null) {
                return null;
            }
            return BitmapFactory.decodeByteArray(requestedItem.data, 0, requestedItem.data.length);
        }

        public void putBitmap(String url, Bitmap bitmap) {
            Entry entry = new Entry();
            entry.data = NativeXVolley.convertBitmapToBytes(bitmap);
            put(url, entry);
        }
    }

    private NativeXVolley() {
        File nativexVolleyDirectory = new File(MonetizationSharedDataManager.getContext().getCacheDir(), "nativex");
        File nativexVolleyDiskCacheDirectory = new File(nativexVolleyDirectory, "volley");
        RequestQueue newRequestQueue = Volley.newRequestQueue(MonetizationSharedDataManager.getContext(), new File(nativexVolleyDirectory, "volley_request_queue"), new SslHttpStack());
        mSSLRequestQueue = newRequestQueue;
        mRequestQueue = newRequestQueue;
        mDiskCache = new DiskBitmapCache(nativexVolleyDiskCacheDirectory);
        mDiskCache.initialize();
        mImageLoader = new ImageLoader(mRequestQueue, mDiskCache);
    }

    public static NativeXVolley getInstance() {
        if (instance == null) {
            instance = new NativeXVolley();
        }
        return instance;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue != null) {
            return mRequestQueue;
        }
        throw new IllegalStateException("RequestQueue not initialized");
    }

    public RequestQueue getSSLRequestQueue() {
        if (mSSLRequestQueue != null) {
            return mSSLRequestQueue;
        }
        throw new IllegalStateException("RequestQueue not initialized");
    }

    public ImageLoader getImageLoader() {
        if (mImageLoader != null) {
            return mImageLoader;
        }
        throw new IllegalStateException("ImageLoader not initialized");
    }

    public DiskBitmapCache getImageCache() {
        if (mDiskCache != null) {
            return mDiskCache;
        }
        throw new IllegalStateException("DiskBitmapCache not initialized");
    }

    public static void release() {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(ServerRequestManager.REQUEST_TAG);
        }
        if (mSSLRequestQueue != null) {
            mSSLRequestQueue.cancelAll(ServerRequestManager.REQUEST_TAG);
        }
        if (mDiskCache != null) {
            mDiskCache.clear();
        }
        instance = null;
        mRequestQueue = null;
        mSSLRequestQueue = null;
        mImageLoader = null;
        mDiskCache = null;
    }

    @TargetApi(14)
    private static byte[] convertBitmapToBytes(Bitmap bitmap) {
        if (VERSION.SDK_INT >= 14) {
            ByteBuffer buffer = ByteBuffer.allocate(bitmap.getByteCount());
            bitmap.copyPixelsToBuffer(buffer);
            return buffer.array();
        }
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(CompressFormat.JPEG, 100, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }
}
