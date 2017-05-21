package com.nativex.monetization.theme;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.SparseArray;
import android.util.SparseIntArray;
import android.view.animation.Animation;
import com.nativex.monetization.manager.MonetizationSharedDataManager;
import com.nativex.volleytoolbox.NativeXVolley;

public class Theme {
    private final SparseArray<Animation> animations = new SparseArray();
    private final SparseIntArray colors = new SparseIntArray();
    private final SparseArray<Drawable> drawables = new SparseArray();
    private boolean initialized = false;

    public synchronized void initialize() {
        this.initialized = true;
    }

    Theme() {
    }

    void setColor(ThemeElementTypes type, int color) {
        this.colors.put(type.getKey(), color);
    }

    public Integer getColor(ThemeElementTypes type) {
        return Integer.valueOf(this.colors.get(type.getKey()));
    }

    void setDrawable(ThemeElementTypes type, Drawable background) {
        if ((background instanceof BitmapDrawable) && !isThemeElementMraidType(type)) {
            NativeXVolley.getInstance().getImageCache().putBitmap(getClass().getCanonicalName() + type.getKey(), ((BitmapDrawable) background).getBitmap());
        }
        this.drawables.put(type.getKey(), background);
    }

    public Drawable getDrawable(ThemeElementTypes type) {
        Drawable drawable = (Drawable) this.drawables.get(type.getKey());
        if (drawable == null) {
            Bitmap bitmap = NativeXVolley.getInstance().getImageCache().getBitmap(getClass().getName() + type.getKey());
            if (bitmap != null) {
                Context context = MonetizationSharedDataManager.getContext();
                if (context != null) {
                    drawable = new BitmapDrawable(context.getResources(), bitmap);
                } else {
                    drawable = new BitmapDrawable(bitmap);
                }
                setDrawable(type, drawable);
            }
        }
        return drawable;
    }

    public synchronized void release() {
        this.initialized = false;
        this.colors.clear();
        for (int i = 0; i < this.drawables.size(); i++) {
            Drawable drawable = (Drawable) this.drawables.get(this.drawables.keyAt(i));
        }
        this.drawables.clear();
    }

    void setAnimation(ThemeElementTypes type, Animation animation) {
        this.animations.put(type.getKey(), animation);
    }

    public Animation getAnimation(ThemeElementTypes type) {
        return (Animation) this.animations.get(type.getKey());
    }

    public void reset() {
        release();
    }

    public boolean isInitialized() {
        return this.initialized;
    }

    private boolean isThemeElementMraidType(ThemeElementTypes type) {
        if (type.equals(ThemeElementTypes.MRAID_CLOSE_BUTTON_BOTTOM_LEFT) || type.equals(ThemeElementTypes.MRAID_CLOSE_BUTTON_BOTTOM_RIGHT) || type.equals(ThemeElementTypes.MRAID_CLOSE_BUTTON_CENTER) || type.equals(ThemeElementTypes.MRAID_CLOSE_BUTTON_DEFAULT) || type.equals(ThemeElementTypes.MRAID_CLOSE_BUTTON_TOP_LEFT) || type.equals(ThemeElementTypes.MRAID_CLOSE_BUTTON_TOP_RIGHT) || type.equals(ThemeElementTypes.MRAID_CLOSE_BUTTON_TOP_CENTER) || type.equals(ThemeElementTypes.MRAID_CLOSE_BUTTON_BOTTOM_CENTER)) {
            return true;
        }
        return false;
    }
}
