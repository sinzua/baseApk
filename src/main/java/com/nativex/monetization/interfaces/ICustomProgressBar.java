package com.nativex.monetization.interfaces;

import android.graphics.drawable.Drawable;

public interface ICustomProgressBar {
    int getBufferedPosition();

    int getCurrentPosition();

    int getMax();

    void setBackgroundDrawable(Drawable drawable);

    void setBufferDrawable(Drawable drawable);

    void setBufferPosition(int i);

    void setBufferProgress(float f);

    void setEmptyDrawable(Drawable drawable);

    void setMax(int i);

    void setPosition(int i);

    void setProgress(float f);

    void setProgressDrawable(Drawable drawable);

    void setTickDrawable(Drawable drawable);
}
