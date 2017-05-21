package com.lidroid.xutils.task;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class PriorityExecutor implements Executor {
    private static final int CORE_POOL_SIZE = 5;
    private static final int KEEP_ALIVE = 1;
    private static final int MAXIMUM_POOL_SIZE = 256;
    private static final ThreadFactory sThreadFactory = new ThreadFactory() {
        private final AtomicInteger mCount = new AtomicInteger(1);

        public Thread newThread(Runnable r) {
            return new Thread(r, "PriorityExecutor #" + this.mCount.getAndIncrement());
        }
    };
    private final BlockingQueue<Runnable> mPoolWorkQueue;
    private final ThreadPoolExecutor mThreadPoolExecutor;

    public PriorityExecutor() {
        this(5);
    }

    public PriorityExecutor(int poolSize) {
        this.mPoolWorkQueue = new PriorityObjectBlockingQueue();
        this.mThreadPoolExecutor = new ThreadPoolExecutor(poolSize, 256, 1, TimeUnit.SECONDS, this.mPoolWorkQueue, sThreadFactory);
    }

    public int getPoolSize() {
        return this.mThreadPoolExecutor.getCorePoolSize();
    }

    public void setPoolSize(int poolSize) {
        if (poolSize > 0) {
            this.mThreadPoolExecutor.setCorePoolSize(poolSize);
        }
    }

    public boolean isBusy() {
        return this.mThreadPoolExecutor.getActiveCount() >= this.mThreadPoolExecutor.getCorePoolSize();
    }

    public void execute(Runnable r) {
        this.mThreadPoolExecutor.execute(r);
    }
}
