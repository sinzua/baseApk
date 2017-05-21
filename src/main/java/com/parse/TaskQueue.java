package com.parse;

import bolts.Continuation;
import bolts.Task;
import java.util.Arrays;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

class TaskQueue {
    private final Lock lock = new ReentrantLock();
    private Task<Void> tail;

    TaskQueue() {
    }

    private Task<Void> getTaskToAwait() {
        this.lock.lock();
        try {
            Task<Void> continueWith = (this.tail != null ? this.tail : Task.forResult(null)).continueWith(new Continuation<Void, Void>() {
                public Void then(Task<Void> task) throws Exception {
                    return null;
                }
            });
            return continueWith;
        } finally {
            this.lock.unlock();
        }
    }

    <T> Task<T> enqueue(Continuation<Void, Task<T>> taskStart) {
        this.lock.lock();
        try {
            Task<Void> oldTail = this.tail != null ? this.tail : Task.forResult(null);
            this.tail = Task.whenAll(Arrays.asList(new Task[]{oldTail, (Task) taskStart.then(getTaskToAwait())}));
            this.lock.unlock();
            return task;
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e2) {
            throw new RuntimeException(e2);
        } catch (Throwable th) {
            this.lock.unlock();
        }
    }

    static <T> Continuation<T, Task<T>> waitFor(final Task<Void> toAwait) {
        return new Continuation<T, Task<T>>() {
            public Task<T> then(final Task<T> task) throws Exception {
                return toAwait.continueWithTask(new Continuation<Void, Task<T>>() {
                    public Task<T> then(Task<Void> task) throws Exception {
                        return task;
                    }
                });
            }
        };
    }

    Lock getLock() {
        return this.lock;
    }

    void waitUntilFinished() throws InterruptedException {
        this.lock.lock();
        try {
            if (this.tail != null) {
                this.tail.waitForCompletion();
                this.lock.unlock();
            }
        } finally {
            this.lock.unlock();
        }
    }
}
