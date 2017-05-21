package com.supersonic.mediationsdk.events;

import android.os.Handler;
import android.os.HandlerThread;
import com.supersonic.mediationsdk.logger.ThreadExceptionHandler;

class SuperLooper extends Thread {
    private static SuperLooper mInstance;
    private SupersonicSdkThread mSdkThread = new SupersonicSdkThread(getClass().getSimpleName());

    private class SupersonicSdkThread extends HandlerThread {
        private Handler mHandler;

        public SupersonicSdkThread(String name) {
            super(name);
            setUncaughtExceptionHandler(new ThreadExceptionHandler());
        }

        public void prepareHandler() {
            this.mHandler = new Handler(getLooper());
        }

        public Handler getCallbackHandler() {
            return this.mHandler;
        }
    }

    private SuperLooper() {
        this.mSdkThread.start();
        this.mSdkThread.prepareHandler();
    }

    public static synchronized SuperLooper getLooper() {
        SuperLooper superLooper;
        synchronized (SuperLooper.class) {
            if (mInstance == null) {
                mInstance = new SuperLooper();
            }
            superLooper = mInstance;
        }
        return superLooper;
    }

    public synchronized void post(Runnable runnable) {
        if (this.mSdkThread != null) {
            Handler callbackHandler = this.mSdkThread.getCallbackHandler();
            if (callbackHandler != null) {
                callbackHandler.post(runnable);
            }
        }
    }
}
