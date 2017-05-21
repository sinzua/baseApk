package com.ty.followboom.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;

public class PostItemView extends FrameLayout {
    public PostItemView(Context context) {
        super(context);
    }

    public PostItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PostItemView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, widthMeasureSpec);
    }
}
