package com.lidroid.xutils.task;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.AbstractQueue;
import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class PriorityObjectBlockingQueue<E> extends AbstractQueue<E> implements BlockingQueue<E>, Serializable {
    private static final long serialVersionUID = -6903933977591709194L;
    private final int capacity;
    private final AtomicInteger count;
    transient Node<E> head;
    private transient Node<E> last;
    private final Condition notEmpty;
    private final Condition notFull;
    private final ReentrantLock putLock;
    private final ReentrantLock takeLock;

    private class Itr implements Iterator<E> {
        private Node<E> current;
        private E currentElement;
        private Node<E> lastRet;

        Itr() {
            PriorityObjectBlockingQueue.this.fullyLock();
            try {
                this.current = PriorityObjectBlockingQueue.this.head.next;
                if (this.current != null) {
                    this.currentElement = this.current.getValue();
                }
                PriorityObjectBlockingQueue.this.fullyUnlock();
            } catch (Throwable th) {
                PriorityObjectBlockingQueue.this.fullyUnlock();
            }
        }

        public boolean hasNext() {
            return this.current != null;
        }

        private Node<E> nextNode(Node<E> p) {
            while (true) {
                Node<E> s = p.next;
                if (s == p) {
                    return PriorityObjectBlockingQueue.this.head.next;
                }
                if (s == null || s.getValue() != null) {
                    return s;
                }
                p = s;
            }
        }

        public E next() {
            PriorityObjectBlockingQueue.this.fullyLock();
            try {
                if (this.current == null) {
                    throw new NoSuchElementException();
                }
                E x = this.currentElement;
                this.lastRet = this.current;
                this.current = nextNode(this.current);
                this.currentElement = this.current == null ? null : this.current.getValue();
                return x;
            } finally {
                PriorityObjectBlockingQueue.this.fullyUnlock();
            }
        }

        public void remove() {
            if (this.lastRet == null) {
                throw new IllegalStateException();
            }
            PriorityObjectBlockingQueue.this.fullyLock();
            try {
                Node<E> node = this.lastRet;
                this.lastRet = null;
                Node<E> trail = PriorityObjectBlockingQueue.this.head;
                Node<E> p = trail.next;
                while (p != null) {
                    if (p == node) {
                        PriorityObjectBlockingQueue.this.unlink(p, trail);
                        break;
                    } else {
                        trail = p;
                        p = p.next;
                    }
                }
                PriorityObjectBlockingQueue.this.fullyUnlock();
            } catch (Throwable th) {
                PriorityObjectBlockingQueue.this.fullyUnlock();
            }
        }
    }

    private void signalNotEmpty() {
        ReentrantLock takeLock = this.takeLock;
        takeLock.lock();
        try {
            this.notEmpty.signal();
        } finally {
            takeLock.unlock();
        }
    }

    private void signalNotFull() {
        ReentrantLock putLock = this.putLock;
        putLock.lock();
        try {
            this.notFull.signal();
        } finally {
            putLock.unlock();
        }
    }

    private synchronized E opQueue(Node<E> node) {
        E _dequeue;
        if (node == null) {
            _dequeue = _dequeue();
        } else {
            _enqueue(node);
            _dequeue = null;
        }
        return _dequeue;
    }

    private void _enqueue(Node<E> node) {
        boolean added = false;
        for (Node<E> curr = this.head; curr.next != null; curr = curr.next) {
            Node<E> temp = curr.next;
            if (temp.getPriority().ordinal() > node.getPriority().ordinal()) {
                curr.next = node;
                node.next = temp;
                added = true;
                break;
            }
        }
        if (!added) {
            this.last.next = node;
            this.last = node;
        }
    }

    private E _dequeue() {
        Node<E> h = this.head;
        Node<E> first = h.next;
        h.next = h;
        this.head = first;
        E x = first.getValue();
        first.setValue(null);
        return x;
    }

    void fullyLock() {
        this.putLock.lock();
        this.takeLock.lock();
    }

    void fullyUnlock() {
        this.takeLock.unlock();
        this.putLock.unlock();
    }

    public PriorityObjectBlockingQueue() {
        this(Integer.MAX_VALUE);
    }

    public PriorityObjectBlockingQueue(int capacity) {
        this.count = new AtomicInteger();
        this.takeLock = new ReentrantLock();
        this.notEmpty = this.takeLock.newCondition();
        this.putLock = new ReentrantLock();
        this.notFull = this.putLock.newCondition();
        if (capacity <= 0) {
            throw new IllegalArgumentException();
        }
        this.capacity = capacity;
        Node node = new Node(null);
        this.head = node;
        this.last = node;
    }

    public PriorityObjectBlockingQueue(Collection<? extends E> c) {
        this(Integer.MAX_VALUE);
        ReentrantLock putLock = this.putLock;
        putLock.lock();
        int n = 0;
        Iterator it = c.iterator();
        while (it.hasNext()) {
            Object e = it.next();
            if (e == null) {
                throw new NullPointerException();
            }
            try {
                if (n == this.capacity) {
                    throw new IllegalStateException("Queue full");
                }
                opQueue(new Node(e));
                n++;
            } finally {
                putLock.unlock();
            }
        }
        this.count.set(n);
    }

    public int size() {
        return this.count.get();
    }

    public int remainingCapacity() {
        return this.capacity - this.count.get();
    }

    public void put(E e) throws InterruptedException {
        if (e == null) {
            throw new NullPointerException();
        }
        Node<E> node = new Node(e);
        ReentrantLock putLock = this.putLock;
        AtomicInteger count = this.count;
        putLock.lockInterruptibly();
        while (count.get() == this.capacity) {
            try {
                this.notFull.await();
            } catch (Throwable th) {
                putLock.unlock();
            }
        }
        opQueue(node);
        int c = count.getAndIncrement();
        if (c + 1 < this.capacity) {
            this.notFull.signal();
        }
        putLock.unlock();
        if (c == 0) {
            signalNotEmpty();
        }
    }

    public boolean offer(E e, long timeout, TimeUnit unit) throws InterruptedException {
        if (e == null) {
            throw new NullPointerException();
        }
        long nanos = unit.toNanos(timeout);
        ReentrantLock putLock = this.putLock;
        AtomicInteger count = this.count;
        putLock.lockInterruptibly();
        while (count.get() == this.capacity) {
            try {
                if (nanos <= 0) {
                    return false;
                }
                nanos = this.notFull.awaitNanos(nanos);
            } finally {
                putLock.unlock();
            }
        }
        opQueue(new Node(e));
        int c = count.getAndIncrement();
        if (c + 1 < this.capacity) {
            this.notFull.signal();
        }
        putLock.unlock();
        if (c == 0) {
            signalNotEmpty();
        }
        return true;
    }

    public boolean offer(E e) {
        if (e == null) {
            throw new NullPointerException();
        }
        AtomicInteger count = this.count;
        if (count.get() == this.capacity) {
            return false;
        }
        int c = -1;
        Node<E> node = new Node(e);
        ReentrantLock putLock = this.putLock;
        putLock.lock();
        try {
            if (count.get() < this.capacity) {
                opQueue(node);
                c = count.getAndIncrement();
                if (c + 1 < this.capacity) {
                    this.notFull.signal();
                }
            }
            putLock.unlock();
            if (c == 0) {
                signalNotEmpty();
            }
            if (c >= 0) {
                return true;
            }
            return false;
        } catch (Throwable th) {
            putLock.unlock();
        }
    }

    public E take() throws InterruptedException {
        AtomicInteger count = this.count;
        ReentrantLock takeLock = this.takeLock;
        takeLock.lockInterruptibly();
        while (count.get() == 0) {
            try {
                this.notEmpty.await();
            } catch (Throwable th) {
                takeLock.unlock();
            }
        }
        E x = opQueue(null);
        int c = count.getAndDecrement();
        if (c > 1) {
            this.notEmpty.signal();
        }
        takeLock.unlock();
        if (c == this.capacity) {
            signalNotFull();
        }
        return x;
    }

    public E poll(long timeout, TimeUnit unit) throws InterruptedException {
        long nanos = unit.toNanos(timeout);
        AtomicInteger count = this.count;
        ReentrantLock takeLock = this.takeLock;
        takeLock.lockInterruptibly();
        while (count.get() == 0) {
            if (nanos <= 0) {
                return null;
            }
            try {
                nanos = this.notEmpty.awaitNanos(nanos);
            } finally {
                takeLock.unlock();
            }
        }
        E x = opQueue(null);
        int c = count.getAndDecrement();
        if (c > 1) {
            this.notEmpty.signal();
        }
        takeLock.unlock();
        if (c != this.capacity) {
            return x;
        }
        signalNotFull();
        return x;
    }

    public E poll() {
        E e = null;
        AtomicInteger count = this.count;
        if (count.get() != 0) {
            e = null;
            int c = -1;
            ReentrantLock takeLock = this.takeLock;
            takeLock.lock();
            try {
                if (count.get() > 0) {
                    e = opQueue(null);
                    c = count.getAndDecrement();
                    if (c > 1) {
                        this.notEmpty.signal();
                    }
                }
                takeLock.unlock();
                if (c == this.capacity) {
                    signalNotFull();
                }
            } catch (Throwable th) {
                takeLock.unlock();
            }
        }
        return e;
    }

    public E peek() {
        E e = null;
        if (this.count.get() != 0) {
            ReentrantLock takeLock = this.takeLock;
            takeLock.lock();
            try {
                Node<E> first = this.head.next;
                if (first != null) {
                    e = first.getValue();
                    takeLock.unlock();
                }
            } finally {
                takeLock.unlock();
            }
        }
        return e;
    }

    void unlink(Node<E> p, Node<E> trail) {
        p.setValue(null);
        trail.next = p.next;
        if (this.last == p) {
            this.last = trail;
        }
        if (this.count.getAndDecrement() == this.capacity) {
            this.notFull.signal();
        }
    }

    public boolean remove(Object o) {
        if (o == null) {
            return false;
        }
        fullyLock();
        Node<E> trail = this.head;
        Node<E> p = trail.next;
        while (p != null) {
            try {
                if (o.equals(p.getValue())) {
                    unlink(p, trail);
                    return true;
                }
                trail = p;
                p = p.next;
            } finally {
                fullyUnlock();
            }
        }
        fullyUnlock();
        return false;
    }

    public boolean contains(Object o) {
        if (o == null) {
            return false;
        }
        fullyLock();
        Node<E> p = this.head.next;
        while (p != null) {
            try {
                if (o.equals(p.getValue())) {
                    return true;
                }
                p = p.next;
            } finally {
                fullyUnlock();
            }
        }
        fullyUnlock();
        return false;
    }

    public Object[] toArray() {
        fullyLock();
        try {
            Object[] a = new Object[this.count.get()];
            Node<E> p = this.head.next;
            int k = 0;
            while (p != null) {
                int k2 = k + 1;
                a[k] = p.getValue();
                p = p.next;
                k = k2;
            }
            return a;
        } finally {
            fullyUnlock();
        }
    }

    public <T> T[] toArray(T[] a) {
        fullyLock();
        try {
            int size = this.count.get();
            if (a.length < size) {
                a = (Object[]) Array.newInstance(a.getClass().getComponentType(), size);
            }
            Node<T> p = this.head.next;
            int k = 0;
            while (p != null) {
                int k2 = k + 1;
                a[k] = p.getValue();
                p = p.next;
                k = k2;
            }
            if (a.length > k) {
                a[k] = null;
            }
            fullyUnlock();
            return a;
        } catch (Throwable th) {
            fullyUnlock();
        }
    }

    public void clear() {
        fullyLock();
        try {
            Node<E> h = this.head;
            while (true) {
                Node<E> p = h.next;
                if (p == null) {
                    break;
                }
                h.next = h;
                p.setValue(null);
                h = p;
            }
            this.head = this.last;
            if (this.count.getAndSet(0) == this.capacity) {
                this.notFull.signal();
            }
            fullyUnlock();
        } catch (Throwable th) {
            fullyUnlock();
        }
    }

    public int drainTo(Collection<? super E> c) {
        return drainTo(c, Integer.MAX_VALUE);
    }

    public int drainTo(Collection<? super E> c, int maxElements) {
        Node<E> h;
        if (c == null) {
            throw new NullPointerException();
        } else if (c == this) {
            throw new IllegalArgumentException();
        } else if (maxElements <= 0) {
            return 0;
        } else {
            boolean signalNotFull = false;
            ReentrantLock takeLock = this.takeLock;
            takeLock.lock();
            int i;
            try {
                int n = Math.min(maxElements, this.count.get());
                h = this.head;
                i = 0;
                while (i < n) {
                    Node<E> p = h.next;
                    c.add(p.getValue());
                    p.setValue(null);
                    h.next = h;
                    h = p;
                    i++;
                }
                if (i > 0) {
                    this.head = h;
                    if (this.count.getAndAdd(-i) == this.capacity) {
                        signalNotFull = true;
                    } else {
                        signalNotFull = false;
                    }
                }
                takeLock.unlock();
                if (signalNotFull) {
                    signalNotFull();
                }
                return n;
            } catch (Throwable th) {
                takeLock.unlock();
                if (null != null) {
                    signalNotFull();
                }
            }
        }
    }

    public Iterator<E> iterator() {
        return new Itr();
    }

    private void writeObject(ObjectOutputStream s) throws IOException {
        fullyLock();
        try {
            s.defaultWriteObject();
            for (Node<E> p = this.head.next; p != null; p = p.next) {
                s.writeObject(p.getValue());
            }
            s.writeObject(null);
        } finally {
            fullyUnlock();
        }
    }

    private void readObject(ObjectInputStream s) throws IOException, ClassNotFoundException {
        s.defaultReadObject();
        this.count.set(0);
        Node node = new Node(null);
        this.head = node;
        this.last = node;
        while (true) {
            E item = s.readObject();
            if (item != null) {
                add(item);
            } else {
                return;
            }
        }
    }
}
