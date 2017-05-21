package com.ty.followboom.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.support.v4.internal.view.SupportMenu;
import android.util.AttributeSet;
import android.view.View;
import com.ty.followboom.R;

public class RoundProgressBar extends View {
    public static final int FILL = 1;
    public static final int STROKE = 0;
    private int max;
    private Paint paint;
    private int progress;
    private int roundColor;
    private int roundProgressColor;
    private float roundWidth;
    private int style;
    private int textColor;
    private boolean textIsDisplayable;
    private float textSize;

    public RoundProgressBar(Context context) {
        this(context, null);
    }

    public RoundProgressBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RoundProgressBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.paint = new Paint();
        TypedArray mTypedArray = context.obtainStyledAttributes(attrs, R.styleable.RoundProgressBar);
        this.roundColor = mTypedArray.getColor(2, SupportMenu.CATEGORY_MASK);
        this.roundProgressColor = mTypedArray.getColor(3, -16711936);
        this.textColor = mTypedArray.getColor(0, -16711936);
        this.textSize = mTypedArray.getDimension(1, 15.0f);
        this.roundWidth = mTypedArray.getDimension(4, 5.0f);
        this.max = mTypedArray.getInteger(5, 100);
        this.textIsDisplayable = mTypedArray.getBoolean(6, true);
        this.style = mTypedArray.getInt(7, 0);
        mTypedArray.recycle();
    }

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int centre = getWidth() / 2;
        int radius = (int) (((float) centre) - (this.roundWidth / 2.0f));
        this.paint.setColor(this.roundColor);
        this.paint.setStyle(Style.STROKE);
        this.paint.setStrokeWidth(this.roundWidth);
        this.paint.setAntiAlias(true);
        canvas.drawCircle((float) centre, (float) centre, (float) radius, this.paint);
        this.paint.setStrokeWidth(0.0f);
        this.paint.setColor(this.textColor);
        this.paint.setTextSize(this.textSize);
        this.paint.setTypeface(Typeface.DEFAULT_BOLD);
        int percent = (int) ((((float) this.progress) / ((float) this.max)) * 100.0f);
        float textWidth = this.paint.measureText(percent + "%");
        if (this.textIsDisplayable && percent != 0 && this.style == 0) {
            canvas.drawText(percent + "%", ((float) centre) - (textWidth / 2.0f), ((float) centre) + (this.textSize / 2.0f), this.paint);
        }
        this.paint.setStrokeWidth(this.roundWidth);
        this.paint.setColor(this.roundProgressColor);
        RectF oval = new RectF((float) (centre - radius), (float) (centre - radius), (float) (centre + radius), (float) (centre + radius));
        switch (this.style) {
            case 0:
                this.paint.setStyle(Style.STROKE);
                canvas.drawArc(oval, 0.0f, (float) ((this.progress * 360) / this.max), false, this.paint);
                return;
            case 1:
                this.paint.setStyle(Style.FILL_AND_STROKE);
                if (this.progress != 0) {
                    canvas.drawArc(oval, 0.0f, (float) ((this.progress * 360) / this.max), true, this.paint);
                    return;
                }
                return;
            default:
                return;
        }
    }

    public synchronized int getMax() {
        return this.max;
    }

    public synchronized void setMax(int max) {
        if (max < 0) {
            throw new IllegalArgumentException("max not less than 0");
        }
        this.max = max;
    }

    public synchronized int getProgress() {
        return this.progress;
    }

    public synchronized void setProgress(int progress) {
        if (progress < 0) {
            throw new IllegalArgumentException("progress not less than 0");
        }
        if (progress > this.max) {
            progress = this.max;
        }
        if (progress <= this.max) {
            this.progress = progress;
            postInvalidate();
        }
    }

    public int getCricleColor() {
        return this.roundColor;
    }

    public void setCricleColor(int cricleColor) {
        this.roundColor = cricleColor;
    }

    public int getCricleProgressColor() {
        return this.roundProgressColor;
    }

    public void setCricleProgressColor(int cricleProgressColor) {
        this.roundProgressColor = cricleProgressColor;
    }

    public int getTextColor() {
        return this.textColor;
    }

    public void setTextColor(int textColor) {
        this.textColor = textColor;
    }

    public float getTextSize() {
        return this.textSize;
    }

    public void setTextSize(float textSize) {
        this.textSize = textSize;
    }

    public float getRoundWidth() {
        return this.roundWidth;
    }

    public void setRoundWidth(float roundWidth) {
        this.roundWidth = roundWidth;
    }
}
