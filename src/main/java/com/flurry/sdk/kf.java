package com.flurry.sdk;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.RunnableFuture;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor.DiscardPolicy;
import java.util.concurrent.TimeUnit;

public class kf<T extends lz> {
    private static final String a = kf.class.getSimpleName();
    private final jx<Object, T> b = new jx();
    private final HashMap<T, Object> c = new HashMap();
    private final HashMap<T, Future<?>> d = new HashMap();
    private final ThreadPoolExecutor e;

    public kf(String str, int i, int i2, long j, TimeUnit timeUnit, BlockingQueue<Runnable> blockingQueue) {
        this.e = new ThreadPoolExecutor(this, i, i2, j, timeUnit, blockingQueue) {
            final /* synthetic */ kf a;

            protected void beforeExecute(Thread thread, Runnable runnable) {
                super.beforeExecute(thread, runnable);
                final lz a = this.a.a(runnable);
                if (a != null) {
                    new ly(this) {
                        final /* synthetic */ AnonymousClass1 b;

                        public void a() {
                            a.o();
                        }
                    }.run();
                }
            }

            protected void afterExecute(Runnable runnable, Throwable th) {
                super.afterExecute(runnable, th);
                final lz a = this.a.a(runnable);
                if (a != null) {
                    synchronized (this.a.d) {
                        this.a.d.remove(a);
                    }
                    this.a.b(a);
                    new ly(this) {
                        final /* synthetic */ AnonymousClass1 b;

                        public void a() {
                            a.p();
                        }
                    }.run();
                }
            }

            protected <V> RunnableFuture<V> newTaskFor(Callable<V> callable) {
                throw new UnsupportedOperationException("Callable not supported");
            }

            protected <V> RunnableFuture<V> newTaskFor(Runnable runnable, V v) {
                RunnableFuture keVar = new ke(runnable, v);
                synchronized (this.a.d) {
                    this.a.d.put((lz) runnable, keVar);
                }
                return keVar;
            }
        };
        this.e.setRejectedExecutionHandler(new DiscardPolicy(this) {
            final /* synthetic */ kf a;

            {
                this.a = r1;
            }

            public void rejectedExecution(Runnable runnable, ThreadPoolExecutor threadPoolExecutor) {
                super.rejectedExecution(runnable, threadPoolExecutor);
                final lz a = this.a.a(runnable);
                if (a != null) {
                    synchronized (this.a.d) {
                        this.a.d.remove(a);
                    }
                    this.a.b(a);
                    new ly(this) {
                        final /* synthetic */ AnonymousClass2 b;

                        public void a() {
                            a.q();
                        }
                    }.run();
                }
            }
        });
        this.e.setThreadFactory(new lm(str, 1));
    }

    public synchronized void a(Object obj, T t) {
        if (!(obj == null || t == null)) {
            b(obj, t);
            this.e.submit(t);
        }
    }

    public synchronized void a(Object obj) {
        if (obj != null) {
            Collection<lz> hashSet = new HashSet();
            hashSet.addAll(this.b.a(obj));
            for (lz a : hashSet) {
                a(a);
            }
        }
    }

    public synchronized void a(final T t) {
        if (t != null) {
            Future future;
            synchronized (this.d) {
                future = (Future) this.d.remove(t);
            }
            b((lz) t);
            if (future != null) {
                future.cancel(true);
            }
            new ly(this) {
                final /* synthetic */ kf b;

                public void a() {
                    t.m();
                }
            }.run();
        }
    }

    public synchronized void c() {
        Set<Object> hashSet = new HashSet();
        hashSet.addAll(this.b.c());
        for (Object a : hashSet) {
            a(a);
        }
    }

    public synchronized long b(Object obj) {
        long j;
        if (obj == null) {
            j = 0;
        } else {
            j = (long) this.b.a(obj).size();
        }
        return j;
    }

    private synchronized void b(Object obj, T t) {
        this.b.a(obj, (Object) t);
        this.c.put(t, obj);
    }

    private synchronized void b(T t) {
        c(this.c.get(t), t);
    }

    private synchronized void c(Object obj, T t) {
        this.b.b(obj, t);
        this.c.remove(t);
    }

    private T a(Runnable runnable) {
        if (runnable instanceof ke) {
            return (lz) ((ke) runnable).a();
        }
        if (runnable instanceof lz) {
            return (lz) runnable;
        }
        kg.a(6, a, "Unknown runnable class: " + runnable.getClass().getName());
        return null;
    }
}
