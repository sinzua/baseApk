package com.lidroid.xutils.bitmap;

import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import com.lidroid.xutils.task.TaskHandler;

public class PauseOnScrollListener implements OnScrollListener {
    private final OnScrollListener externalListener;
    private final boolean pauseOnFling;
    private final boolean pauseOnScroll;
    private TaskHandler taskHandler;

    public PauseOnScrollListener(TaskHandler taskHandler, boolean pauseOnScroll, boolean pauseOnFling) {
        this(taskHandler, pauseOnScroll, pauseOnFling, null);
    }

    public PauseOnScrollListener(TaskHandler taskHandler, boolean pauseOnScroll, boolean pauseOnFling, OnScrollListener customListener) {
        this.taskHandler = taskHandler;
        this.pauseOnScroll = pauseOnScroll;
        this.pauseOnFling = pauseOnFling;
        this.externalListener = customListener;
    }

    public void onScrollStateChanged(AbsListView view, int scrollState) {
        switch (scrollState) {
            case 0:
                this.taskHandler.resume();
                break;
            case 1:
                if (this.pauseOnScroll) {
                    this.taskHandler.pause();
                    break;
                }
                break;
            case 2:
                if (this.pauseOnFling) {
                    this.taskHandler.pause();
                    break;
                }
                break;
        }
        if (this.externalListener != null) {
            this.externalListener.onScrollStateChanged(view, scrollState);
        }
    }

    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        if (this.externalListener != null) {
            this.externalListener.onScroll(view, firstVisibleItem, visibleItemCount, totalItemCount);
        }
    }
}
