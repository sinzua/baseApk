package com.lidroid.xutils.bitmap.core;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import com.lidroid.xutils.util.LogUtils;
import java.io.FileDescriptor;

public class BitmapDecoder {
    private static final Object lock = new Object();

    private BitmapDecoder() {
    }

    public static Bitmap decodeSampledBitmapFromResource(Resources res, int resId, BitmapSize maxSize, Config config) {
        Bitmap decodeResource;
        synchronized (lock) {
            Options options = new Options();
            options.inJustDecodeBounds = true;
            options.inPurgeable = true;
            options.inInputShareable = true;
            BitmapFactory.decodeResource(res, resId, options);
            options.inSampleSize = calculateInSampleSize(options, maxSize.getWidth(), maxSize.getHeight());
            options.inJustDecodeBounds = false;
            if (config != null) {
                options.inPreferredConfig = config;
            }
            try {
                decodeResource = BitmapFactory.decodeResource(res, resId, options);
            } catch (Throwable e) {
                LogUtils.e(e.getMessage(), e);
                return null;
            }
        }
        return decodeResource;
    }

    public static Bitmap decodeSampledBitmapFromFile(String filename, BitmapSize maxSize, Config config) {
        Bitmap decodeFile;
        synchronized (lock) {
            Options options = new Options();
            options.inJustDecodeBounds = true;
            options.inPurgeable = true;
            options.inInputShareable = true;
            BitmapFactory.decodeFile(filename, options);
            options.inSampleSize = calculateInSampleSize(options, maxSize.getWidth(), maxSize.getHeight());
            options.inJustDecodeBounds = false;
            if (config != null) {
                options.inPreferredConfig = config;
            }
            try {
                decodeFile = BitmapFactory.decodeFile(filename, options);
            } catch (Throwable e) {
                LogUtils.e(e.getMessage(), e);
                return null;
            }
        }
        return decodeFile;
    }

    public static Bitmap decodeSampledBitmapFromDescriptor(FileDescriptor fileDescriptor, BitmapSize maxSize, Config config) {
        Bitmap bitmap = null;
        synchronized (lock) {
            Options options = new Options();
            options.inJustDecodeBounds = true;
            options.inPurgeable = true;
            options.inInputShareable = true;
            BitmapFactory.decodeFileDescriptor(fileDescriptor, null, options);
            options.inSampleSize = calculateInSampleSize(options, maxSize.getWidth(), maxSize.getHeight());
            options.inJustDecodeBounds = false;
            if (config != null) {
                options.inPreferredConfig = config;
            }
            try {
                bitmap = BitmapFactory.decodeFileDescriptor(fileDescriptor, null, options);
            } catch (Throwable e) {
                LogUtils.e(e.getMessage(), e);
            }
        }
        return bitmap;
    }

    public static Bitmap decodeSampledBitmapFromByteArray(byte[] data, BitmapSize maxSize, Config config) {
        Bitmap decodeByteArray;
        synchronized (lock) {
            Options options = new Options();
            options.inJustDecodeBounds = true;
            options.inPurgeable = true;
            options.inInputShareable = true;
            BitmapFactory.decodeByteArray(data, 0, data.length, options);
            options.inSampleSize = calculateInSampleSize(options, maxSize.getWidth(), maxSize.getHeight());
            options.inJustDecodeBounds = false;
            if (config != null) {
                options.inPreferredConfig = config;
            }
            try {
                decodeByteArray = BitmapFactory.decodeByteArray(data, 0, data.length, options);
            } catch (Throwable e) {
                LogUtils.e(e.getMessage(), e);
                return null;
            }
        }
        return decodeByteArray;
    }

    public static Bitmap decodeResource(Resources res, int resId) {
        Bitmap decodeResource;
        synchronized (lock) {
            Options options = new Options();
            options.inPurgeable = true;
            options.inInputShareable = true;
            try {
                decodeResource = BitmapFactory.decodeResource(res, resId, options);
            } catch (Throwable e) {
                LogUtils.e(e.getMessage(), e);
                return null;
            }
        }
        return decodeResource;
    }

    public static Bitmap decodeFile(String filename) {
        Bitmap decodeFile;
        synchronized (lock) {
            Options options = new Options();
            options.inPurgeable = true;
            options.inInputShareable = true;
            try {
                decodeFile = BitmapFactory.decodeFile(filename, options);
            } catch (Throwable e) {
                LogUtils.e(e.getMessage(), e);
                return null;
            }
        }
        return decodeFile;
    }

    public static Bitmap decodeFileDescriptor(FileDescriptor fileDescriptor) {
        Bitmap bitmap = null;
        synchronized (lock) {
            Options options = new Options();
            options.inPurgeable = true;
            options.inInputShareable = true;
            try {
                bitmap = BitmapFactory.decodeFileDescriptor(fileDescriptor, null, options);
            } catch (Throwable e) {
                LogUtils.e(e.getMessage(), e);
            }
        }
        return bitmap;
    }

    public static Bitmap decodeByteArray(byte[] data) {
        Bitmap decodeByteArray;
        synchronized (lock) {
            Options options = new Options();
            options.inPurgeable = true;
            options.inInputShareable = true;
            try {
                decodeByteArray = BitmapFactory.decodeByteArray(data, 0, data.length, options);
            } catch (Throwable e) {
                LogUtils.e(e.getMessage(), e);
                return null;
            }
        }
        return decodeByteArray;
    }

    public static int calculateInSampleSize(Options options, int maxWidth, int maxHeight) {
        int height = options.outHeight;
        int width = options.outWidth;
        int inSampleSize = 1;
        if (width > maxWidth || height > maxHeight) {
            if (width > height) {
                inSampleSize = Math.round(((float) height) / ((float) maxHeight));
            } else {
                inSampleSize = Math.round(((float) width) / ((float) maxWidth));
            }
            while (((float) (width * height)) / ((float) (inSampleSize * inSampleSize)) > ((float) ((maxWidth * maxHeight) * 2))) {
                inSampleSize++;
            }
        }
        return inSampleSize;
    }
}
