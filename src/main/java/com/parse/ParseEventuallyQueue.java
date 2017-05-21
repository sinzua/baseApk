package com.parse;

import android.util.SparseArray;
import bolts.Task;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import org.json.JSONException;
import org.json.JSONObject;

abstract class ParseEventuallyQueue {
    private boolean isConnected;
    private TestHelper testHelper;

    public static class TestHelper {
        public static final int COMMAND_ENQUEUED = 3;
        public static final int COMMAND_FAILED = 2;
        public static final int COMMAND_NOT_ENQUEUED = 4;
        public static final int COMMAND_OLD_FORMAT_DISCARDED = 8;
        public static final int COMMAND_SUCCESSFUL = 1;
        private static final int MAX_EVENTS = 1000;
        public static final int NETWORK_DOWN = 7;
        public static final int OBJECT_REMOVED = 6;
        public static final int OBJECT_UPDATED = 5;
        private SparseArray<Semaphore> events;

        public static String getEventString(int event) {
            switch (event) {
                case 1:
                    return "COMMAND_SUCCESSFUL";
                case 2:
                    return "COMMAND_FAILED";
                case 3:
                    return "COMMAND_ENQUEUED";
                case 4:
                    return "COMMAND_NOT_ENQUEUED";
                case 5:
                    return "OBJECT_UPDATED";
                case 6:
                    return "OBJECT_REMOVED";
                case 7:
                    return "NETWORK_DOWN";
                case 8:
                    return "COMMAND_OLD_FORMAT_DISCARDED";
                default:
                    throw new IllegalStateException("Encountered unknown event: " + event);
            }
        }

        private TestHelper() {
            this.events = new SparseArray();
            clear();
        }

        public void clear() {
            this.events.clear();
            this.events.put(1, new Semaphore(1000));
            this.events.put(2, new Semaphore(1000));
            this.events.put(3, new Semaphore(1000));
            this.events.put(4, new Semaphore(1000));
            this.events.put(5, new Semaphore(1000));
            this.events.put(6, new Semaphore(1000));
            this.events.put(7, new Semaphore(1000));
            this.events.put(8, new Semaphore(1000));
            for (int i = 0; i < this.events.size(); i++) {
                ((Semaphore) this.events.get(this.events.keyAt(i))).acquireUninterruptibly(1000);
            }
        }

        public int unexpectedEvents() {
            int sum = 0;
            for (int i = 0; i < this.events.size(); i++) {
                sum += ((Semaphore) this.events.get(this.events.keyAt(i))).availablePermits();
            }
            return sum;
        }

        public List<String> getUnexpectedEvents() {
            List<String> unexpectedEvents = new ArrayList();
            for (int i = 0; i < this.events.size(); i++) {
                int event = this.events.keyAt(i);
                if (((Semaphore) this.events.get(event)).availablePermits() > 0) {
                    unexpectedEvents.add(getEventString(event));
                }
            }
            return unexpectedEvents;
        }

        public void notify(int event) {
            notify(event, null);
        }

        public void notify(int event, Throwable t) {
            ((Semaphore) this.events.get(event)).release();
        }

        public boolean waitFor(int event) {
            return waitFor(event, 1);
        }

        public boolean waitFor(int event, int permits) {
            try {
                return ((Semaphore) this.events.get(event)).tryAcquire(permits, 10, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                e.printStackTrace();
                return false;
            }
        }
    }

    public abstract void clear();

    public abstract Task<JSONObject> enqueueEventuallyAsync(ParseRESTCommand parseRESTCommand, ParseObject parseObject);

    public abstract void onDestroy();

    public abstract void pause();

    public abstract int pendingCount();

    public abstract void resume();

    abstract void simulateReboot();

    ParseEventuallyQueue() {
    }

    public void setConnected(boolean connected) {
        this.isConnected = connected;
    }

    public boolean isConnected() {
        return this.isConnected;
    }

    public void setTimeoutRetryWaitSeconds(double seconds) {
    }

    public void setMaxCacheSizeBytes(int bytes) {
    }

    public TestHelper getTestHelper() {
        if (this.testHelper == null) {
            this.testHelper = new TestHelper();
        }
        return this.testHelper;
    }

    protected void notifyTestHelper(int event) {
        notifyTestHelper(event, null);
    }

    protected void notifyTestHelper(int event, Throwable t) {
        if (this.testHelper != null) {
            this.testHelper.notify(event, t);
        }
    }

    protected ParseRESTCommand commandFromJSON(JSONObject json) throws JSONException {
        if (ParseRESTCommand.isValidCommandJSONObject(json)) {
            return ParseRESTCommand.fromJSONObject(json);
        }
        if (ParseRESTCommand.isValidOldFormatCommandJSONObject(json)) {
            return null;
        }
        throw new JSONException("Failed to load command from JSON.");
    }

    Task<JSONObject> waitForOperationSetAndEventuallyPin(ParseOperationSet operationSet, EventuallyPin eventuallyPin) {
        return Task.forResult(null);
    }

    void fakeObjectUpdate() {
        if (this.testHelper != null) {
            this.testHelper.notify(3);
            this.testHelper.notify(1);
            this.testHelper.notify(5);
        }
    }
}
