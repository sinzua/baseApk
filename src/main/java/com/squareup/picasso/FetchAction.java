package com.squareup.picasso;

import android.graphics.Bitmap;
import com.squareup.picasso.Picasso.LoadedFrom;

class FetchAction extends Action<Object> {
    private final Object target = new Object();

    FetchAction(Picasso picasso, Request data, int memoryPolicy, int networkPolicy, Object tag, String key) {
        super(picasso, null, data, memoryPolicy, networkPolicy, 0, null, key, tag, false);
    }

    void complete(Bitmap result, LoadedFrom from) {
    }

    public void error() {
    }

    Object getTarget() {
        return this.target;
    }
}
