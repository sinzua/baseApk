package com.lidroid.xutils.bitmap.callback;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;
import com.lidroid.xutils.bitmap.BitmapDisplayConfig;

public abstract class BitmapLoadCallBack<T extends View> {
    private BitmapSetter<T> bitmapSetter;

    public abstract void onLoadCompleted(T t, String str, Bitmap bitmap, BitmapDisplayConfig bitmapDisplayConfig, BitmapLoadFrom bitmapLoadFrom);

    public abstract void onLoadFailed(T t, String str, Drawable drawable);

    public void onPreLoad(T t, String uri, BitmapDisplayConfig config) {
    }

    public void onLoadStarted(T t, String uri, BitmapDisplayConfig config) {
    }

    public void onLoading(T t, String uri, BitmapDisplayConfig config, long total, long current) {
    }

    public void setBitmapSetter(BitmapSetter<T> bitmapSetter) {
        this.bitmapSetter = bitmapSetter;
    }

    public void setBitmap(T container, Bitmap bitmap) {
        if (this.bitmapSetter != null) {
            this.bitmapSetter.setBitmap(container, bitmap);
        } else if (container instanceof ImageView) {
            ((ImageView) container).setImageBitmap(bitmap);
        } else {
            container.setBackgroundDrawable(new BitmapDrawable(container.getResources(), bitmap));
        }
    }

    public void setDrawable(T container, Drawable drawable) {
        if (this.bitmapSetter != null) {
            this.bitmapSetter.setDrawable(container, drawable);
        } else if (container instanceof ImageView) {
            ((ImageView) container).setImageDrawable(drawable);
        } else {
            container.setBackgroundDrawable(drawable);
        }
    }

    public Drawable getDrawable(T container) {
        if (this.bitmapSetter != null) {
            return this.bitmapSetter.getDrawable(container);
        }
        if (container instanceof ImageView) {
            return ((ImageView) container).getDrawable();
        }
        return container.getBackground();
    }
}
