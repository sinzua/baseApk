package com.parse;

import android.os.SystemClock;
import java.util.LinkedList;
import java.util.Queue;

class SimpleTraceLogger {
    public static int NO_LIMIT = 0;
    public static final String TAG = "SimpleTraceLogger";
    private Queue<TraceLogLine> mTrace;
    protected final int mTraceCountLimit;

    protected static class TraceLogLine {
        public final long time;
        public final String trace;

        TraceLogLine(String trace, long time) {
            this.trace = trace;
            this.time = time;
        }

        public String toString() {
            return String.format("[%d] %s", new Object[]{Long.valueOf(this.time), this.trace});
        }
    }

    public SimpleTraceLogger(int traceCountLimit) {
        this.mTraceCountLimit = traceCountLimit;
        clear();
    }

    public void append(String str, Object... args) {
        append(String.format(str, args));
    }

    public void append(String str) {
        synchronized (this) {
            if (this.mTraceCountLimit > NO_LIMIT && this.mTrace.size() == this.mTraceCountLimit) {
                this.mTrace.remove();
            }
            this.mTrace.offer(new TraceLogLine(str, SystemClock.elapsedRealtime()));
        }
    }

    public synchronized String toString() {
        return toString(NO_LIMIT);
    }

    public synchronized String toString(int limit) {
        StringBuilder retval;
        int startFrom;
        retval = new StringBuilder();
        if (limit <= NO_LIMIT) {
            startFrom = 0;
        } else {
            startFrom = Math.max(this.mTrace.size() - limit, 0);
        }
        int index = 0;
        for (TraceLogLine line : this.mTrace) {
            if (index >= startFrom) {
                retval.append(line.toString()).append('\n');
            }
            index++;
        }
        return retval.toString();
    }

    public synchronized void clear() {
        this.mTrace = new LinkedList();
    }
}
