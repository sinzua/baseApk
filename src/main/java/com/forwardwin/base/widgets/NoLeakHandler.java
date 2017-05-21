package com.forwardwin.base.widgets;

import android.os.Handler;
import android.os.Message;
import java.lang.ref.WeakReference;

public class NoLeakHandler<T extends IMessageHandler> extends Handler {
    protected WeakReference<T> mWeakReference;

    public NoLeakHandler(T target) {
        this.mWeakReference = new WeakReference(target);
    }

    public void handleMessage(Message msg) {
        IMessageHandler activity = (IMessageHandler) this.mWeakReference.get();
        if (activity != null) {
            activity.handleMessage(msg);
        }
    }
}
