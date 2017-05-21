package com.lidroid.xutils.task;

public class PriorityRunnable extends PriorityObject<Runnable> implements Runnable {
    public PriorityRunnable(Priority priority, Runnable obj) {
        super(priority, obj);
    }

    public void run() {
        ((Runnable) this.obj).run();
    }
}
