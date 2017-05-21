package com.lidroid.xutils.bitmap.core;

import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.PorterDuff.Mode;
import android.graphics.Rect;
import android.graphics.Region;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.Drawable.ConstantState;
import android.support.v4.media.TransportMediator;
import android.view.View;
import com.lidroid.xutils.BitmapUtils.BitmapLoadTask;
import java.lang.ref.WeakReference;

public class AsyncDrawable<T extends View> extends Drawable {
    private final Drawable baseDrawable;
    private final WeakReference<BitmapLoadTask<T>> bitmapLoadTaskReference;

    public AsyncDrawable(Drawable drawable, BitmapLoadTask<T> bitmapWorkerTask) {
        if (bitmapWorkerTask == null) {
            throw new IllegalArgumentException("bitmapWorkerTask may not be null");
        }
        this.baseDrawable = drawable;
        this.bitmapLoadTaskReference = new WeakReference(bitmapWorkerTask);
    }

    public BitmapLoadTask<T> getBitmapWorkerTask() {
        return (BitmapLoadTask) this.bitmapLoadTaskReference.get();
    }

    public void draw(Canvas canvas) {
        if (this.baseDrawable != null) {
            this.baseDrawable.draw(canvas);
        }
    }

    public void setAlpha(int i) {
        if (this.baseDrawable != null) {
            this.baseDrawable.setAlpha(i);
        }
    }

    public void setColorFilter(ColorFilter colorFilter) {
        if (this.baseDrawable != null) {
            this.baseDrawable.setColorFilter(colorFilter);
        }
    }

    public int getOpacity() {
        return this.baseDrawable == null ? TransportMediator.KEYCODE_MEDIA_PAUSE : this.baseDrawable.getOpacity();
    }

    public void setBounds(int left, int top, int right, int bottom) {
        if (this.baseDrawable != null) {
            this.baseDrawable.setBounds(left, top, right, bottom);
        }
    }

    public void setBounds(Rect bounds) {
        if (this.baseDrawable != null) {
            this.baseDrawable.setBounds(bounds);
        }
    }

    public void setChangingConfigurations(int configs) {
        if (this.baseDrawable != null) {
            this.baseDrawable.setChangingConfigurations(configs);
        }
    }

    public int getChangingConfigurations() {
        return this.baseDrawable == null ? 0 : this.baseDrawable.getChangingConfigurations();
    }

    public void setDither(boolean dither) {
        if (this.baseDrawable != null) {
            this.baseDrawable.setDither(dither);
        }
    }

    public void setFilterBitmap(boolean filter) {
        if (this.baseDrawable != null) {
            this.baseDrawable.setFilterBitmap(filter);
        }
    }

    public void invalidateSelf() {
        if (this.baseDrawable != null) {
            this.baseDrawable.invalidateSelf();
        }
    }

    public void scheduleSelf(Runnable what, long when) {
        if (this.baseDrawable != null) {
            this.baseDrawable.scheduleSelf(what, when);
        }
    }

    public void unscheduleSelf(Runnable what) {
        if (this.baseDrawable != null) {
            this.baseDrawable.unscheduleSelf(what);
        }
    }

    public void setColorFilter(int color, Mode mode) {
        if (this.baseDrawable != null) {
            this.baseDrawable.setColorFilter(color, mode);
        }
    }

    public void clearColorFilter() {
        if (this.baseDrawable != null) {
            this.baseDrawable.clearColorFilter();
        }
    }

    public boolean isStateful() {
        return this.baseDrawable != null && this.baseDrawable.isStateful();
    }

    public boolean setState(int[] stateSet) {
        return this.baseDrawable != null && this.baseDrawable.setState(stateSet);
    }

    public int[] getState() {
        return this.baseDrawable == null ? null : this.baseDrawable.getState();
    }

    public Drawable getCurrent() {
        return this.baseDrawable == null ? null : this.baseDrawable.getCurrent();
    }

    public boolean setVisible(boolean visible, boolean restart) {
        return this.baseDrawable != null && this.baseDrawable.setVisible(visible, restart);
    }

    public Region getTransparentRegion() {
        return this.baseDrawable == null ? null : this.baseDrawable.getTransparentRegion();
    }

    public int getIntrinsicWidth() {
        return this.baseDrawable == null ? 0 : this.baseDrawable.getIntrinsicWidth();
    }

    public int getIntrinsicHeight() {
        return this.baseDrawable == null ? 0 : this.baseDrawable.getIntrinsicHeight();
    }

    public int getMinimumWidth() {
        return this.baseDrawable == null ? 0 : this.baseDrawable.getMinimumWidth();
    }

    public int getMinimumHeight() {
        return this.baseDrawable == null ? 0 : this.baseDrawable.getMinimumHeight();
    }

    public boolean getPadding(Rect padding) {
        return this.baseDrawable != null && this.baseDrawable.getPadding(padding);
    }

    public Drawable mutate() {
        return this.baseDrawable == null ? null : this.baseDrawable.mutate();
    }

    public ConstantState getConstantState() {
        return this.baseDrawable == null ? null : this.baseDrawable.getConstantState();
    }
}
