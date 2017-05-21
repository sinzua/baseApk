package com.squareup.okhttp.internal.allocations;

import com.squareup.okhttp.ConnectionPool;
import com.squareup.okhttp.internal.Internal;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public final class Connection {
    private int allocationLimit = 1;
    private final List<StreamAllocationReference> allocations = new ArrayList();
    long idleAt = Long.MAX_VALUE;
    private boolean noNewAllocations;
    private final ConnectionPool pool;

    public static class Stream {
        public final String name;

        public Stream(String name) {
            this.name = name;
        }

        public String toString() {
            return this.name;
        }
    }

    public final class StreamAllocation {
        private boolean released;
        private boolean rescinded;
        private Stream stream;

        private StreamAllocation() {
        }

        public Stream newStream(String name) {
            Stream stream;
            synchronized (Connection.this.pool) {
                if (this.stream != null || this.released) {
                    throw new IllegalStateException();
                } else if (this.rescinded) {
                    stream = null;
                } else {
                    this.stream = new Stream(name);
                    stream = this.stream;
                }
            }
            return stream;
        }

        public void streamComplete(Stream stream) {
            synchronized (Connection.this.pool) {
                if (stream != null) {
                    if (stream == this.stream) {
                        this.stream = null;
                        if (this.released) {
                            Connection.this.remove(this);
                        }
                    }
                }
                throw new IllegalArgumentException();
            }
        }
    }

    private static final class StreamAllocationReference extends WeakReference<StreamAllocation> {
        private final String name;

        public StreamAllocationReference(StreamAllocation streamAllocation, String name) {
            super(streamAllocation);
            this.name = name;
        }

        public void rescind() {
            StreamAllocation streamAllocation = (StreamAllocation) get();
            if (streamAllocation != null) {
                streamAllocation.rescinded = true;
            }
        }
    }

    public Connection(ConnectionPool pool) {
        this.pool = pool;
    }

    public StreamAllocation reserve(String name) {
        StreamAllocation streamAllocation = null;
        synchronized (this.pool) {
            if (this.noNewAllocations || this.allocations.size() >= this.allocationLimit) {
            } else {
                streamAllocation = new StreamAllocation();
                this.allocations.add(new StreamAllocationReference(streamAllocation, name));
            }
        }
        return streamAllocation;
    }

    public void release(StreamAllocation streamAllocation) {
        synchronized (this.pool) {
            if (streamAllocation.released) {
                throw new IllegalStateException("already released");
            }
            streamAllocation.released = true;
            if (streamAllocation.stream == null) {
                remove(streamAllocation);
            }
        }
    }

    private void remove(StreamAllocation streamAllocation) {
        int size = this.allocations.size();
        for (int i = 0; i < size; i++) {
            if (((StreamAllocationReference) this.allocations.get(i)).get() == streamAllocation) {
                this.allocations.remove(i);
                if (this.allocations.isEmpty()) {
                    this.idleAt = System.nanoTime();
                    return;
                }
                return;
            }
        }
        throw new IllegalArgumentException("unexpected allocation: " + streamAllocation);
    }

    public void noNewStreams() {
        synchronized (this.pool) {
            this.noNewAllocations = true;
            for (int i = 0; i < this.allocations.size(); i++) {
                ((StreamAllocationReference) this.allocations.get(i)).rescind();
            }
        }
    }

    public void setAllocationLimit(int allocationLimit) {
        synchronized (this.pool) {
            if (allocationLimit < 0) {
                throw new IllegalArgumentException();
            }
            this.allocationLimit = allocationLimit;
            for (int i = allocationLimit; i < this.allocations.size(); i++) {
                ((StreamAllocationReference) this.allocations.get(i)).rescind();
            }
        }
    }

    public void pruneLeakedAllocations() {
        synchronized (this.pool) {
            Iterator<StreamAllocationReference> i = this.allocations.iterator();
            while (i.hasNext()) {
                StreamAllocationReference reference = (StreamAllocationReference) i.next();
                if (reference.get() == null) {
                    Internal.logger.warning("Call " + reference.name + " leaked a connection. Did you forget to close a response body?");
                    this.noNewAllocations = true;
                    i.remove();
                    if (this.allocations.isEmpty()) {
                        this.idleAt = System.nanoTime();
                    }
                }
            }
        }
    }

    int size() {
        int size;
        synchronized (this.pool) {
            size = this.allocations.size();
        }
        return size;
    }
}
