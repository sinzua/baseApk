package com.lidroid.xutils.bitmap;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import com.lidroid.xutils.bitmap.core.BitmapSize;
import java.lang.reflect.Field;

public class BitmapCommonUtils {
    private static BitmapSize screenSize = null;

    private BitmapCommonUtils() {
    }

    public static BitmapSize getScreenSize(Context context) {
        if (screenSize == null) {
            DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
            screenSize = new BitmapSize(displayMetrics.widthPixels, displayMetrics.heightPixels);
        }
        return screenSize;
    }

    public static BitmapSize optimizeMaxSizeByView(View view, int maxImageWidth, int maxImageHeight) {
        int width = maxImageWidth;
        int height = maxImageHeight;
        if (width > 0 && height > 0) {
            return new BitmapSize(width, height);
        }
        LayoutParams params = view.getLayoutParams();
        if (params != null) {
            if (params.width > 0) {
                width = params.width;
            } else if (params.width != -2) {
                width = view.getWidth();
            }
            if (params.height > 0) {
                height = params.height;
            } else if (params.height != -2) {
                height = view.getHeight();
            }
        }
        if (width <= 0) {
            width = getImageViewFieldValue(view, "mMaxWidth");
        }
        if (height <= 0) {
            height = getImageViewFieldValue(view, "mMaxHeight");
        }
        BitmapSize screenSize = getScreenSize(view.getContext());
        if (width <= 0) {
            width = screenSize.getWidth();
        }
        if (height <= 0) {
            height = screenSize.getHeight();
        }
        return new BitmapSize(width, height);
    }

    private static int getImageViewFieldValue(Object object, String fieldName) {
        if (!(object instanceof ImageView)) {
            return 0;
        }
        try {
            Field field = ImageView.class.getDeclaredField(fieldName);
            field.setAccessible(true);
            int fieldValue = ((Integer) field.get(object)).intValue();
            if (fieldValue <= 0 || fieldValue >= Integer.MAX_VALUE) {
                return 0;
            }
            return fieldValue;
        } catch (Throwable th) {
            return 0;
        }
    }
}
