package com.nativex.monetization.manager;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.NinePatch;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.NinePatchDrawable;
import android.util.Base64;
import com.nativex.common.Log;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

public class ImageService {
    public Drawable getDrawableImage(Context context, String fileName) {
        try {
            return Drawable.createFromStream(context.getAssets().open(fileName), "image");
        } catch (Throwable e) {
            Log.e("Exception caught in ImageService in getDrawableImage():", e);
            e.printStackTrace();
            return null;
        }
    }

    public Drawable loadDrawableFromBase64String(Context context, String encodedString) {
        try {
            byte[] decodedByte = Base64.decode(encodedString, 0);
            Bitmap bitmap = BitmapFactory.decodeByteArray(decodedByte, 0, decodedByte.length);
            if (bitmap == null) {
                return null;
            }
            return new BitmapDrawable(context.getResources(), bitmap);
        } catch (Throwable e) {
            Log.e("Exception caught in ImageService in loadDrawableFromBase64String():", e);
            return null;
        }
    }

    Drawable loadDrawableFromInputStream(Context context, InputStream is) {
        try {
            Bitmap bitmap = BitmapFactory.decodeStream(is);
            if (bitmap == null) {
                return null;
            }
            byte[] chunk = bitmap.getNinePatchChunk();
            if (chunk != null && NinePatch.isNinePatchChunk(chunk)) {
                return new NinePatchDrawable(context.getResources(), bitmap, chunk, new Rect(), "image");
            }
            return new BitmapDrawable(context.getResources(), bitmap);
        } catch (Throwable e) {
            Log.e("Exception caught in ImageService in loadDrawableFromInputStream():", e);
            return null;
        }
    }

    public Drawable loadDrawableFromAssets(Context context, String filename) {
        InputStream is = null;
        try {
            is = context.getAssets().open(filename);
            Drawable loadDrawableFromInputStream = loadDrawableFromInputStream(context, is);
            if (is == null) {
                return loadDrawableFromInputStream;
            }
            try {
                is.close();
                return loadDrawableFromInputStream;
            } catch (Throwable th) {
                return loadDrawableFromInputStream;
            }
        } catch (Throwable th2) {
        }
        return null;
    }

    public Drawable loadDrawableFromFile(Context context, String filename) {
        Throwable e;
        Throwable th;
        Drawable drawable = null;
        InputStream is = null;
        try {
            File file = new File(filename);
            if (file.exists()) {
                InputStream is2 = new FileInputStream(file);
                try {
                    drawable = loadDrawableFromInputStream(context, is2);
                    if (is2 != null) {
                        try {
                            is2.close();
                        } catch (Throwable th2) {
                        }
                    }
                    is = is2;
                } catch (Throwable th3) {
                    th = th3;
                    is = is2;
                    if (is != null) {
                        is.close();
                    }
                    throw th;
                }
            } else if (is != null) {
                try {
                    is.close();
                } catch (Throwable th4) {
                }
            }
        } catch (Throwable th5) {
            e = th5;
            Log.e("ImageService: Exception caught in loadDrawableFromFile()", e);
            if (is != null) {
                is.close();
            }
            return drawable;
        }
        return drawable;
    }
}
