package com.nativex.monetization.mraid;

import android.app.Activity;

abstract class MRAIDDialogWorker {
    private final MRAIDContainer container;
    private boolean released = false;

    public abstract void dismissDialog();

    public abstract void release();

    public abstract void showDialog(Activity activity);

    MRAIDDialogWorker(MRAIDContainer container) {
        this.container = container;
    }

    MRAIDContainer getContainer() {
        return this.container;
    }

    void finishWorker() {
        if (!this.released) {
            this.released = true;
            try {
                if (this.container != null) {
                    this.container.workerDone(this);
                } else {
                    release();
                }
            } catch (Exception e) {
                try {
                    dismissDialog();
                } catch (Exception e2) {
                }
            }
        }
    }
}
