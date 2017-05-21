package com.lidroid.xutils.bitmap.factory;

import android.graphics.Bitmap;

public interface BitmapFactory {
    BitmapFactory cloneNew();

    Bitmap createBitmap(Bitmap bitmap);
}
