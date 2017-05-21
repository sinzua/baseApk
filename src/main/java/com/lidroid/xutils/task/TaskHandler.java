package com.lidroid.xutils.task;

public interface TaskHandler {
    void cancel();

    boolean isCancelled();

    boolean isPaused();

    void pause();

    void resume();

    boolean supportCancel();

    boolean supportPause();

    boolean supportResume();
}
