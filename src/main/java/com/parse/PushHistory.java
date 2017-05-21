package com.parse;

import java.util.HashSet;
import java.util.Iterator;
import java.util.PriorityQueue;
import org.json.JSONException;
import org.json.JSONObject;

class PushHistory {
    private static final String TAG = "com.parse.PushHistory";
    private final PriorityQueue<Entry> entries;
    private String lastTime = null;
    private final int maxHistoryLength;
    private final HashSet<String> pushIds;

    private static class Entry implements Comparable<Entry> {
        public String pushId;
        public String timestamp;

        public Entry(String pushId, String timestamp) {
            this.pushId = pushId;
            this.timestamp = timestamp;
        }

        public int compareTo(Entry other) {
            return this.timestamp.compareTo(other.timestamp);
        }
    }

    public PushHistory(int maxHistoryLength, JSONObject json) {
        this.maxHistoryLength = maxHistoryLength;
        this.entries = new PriorityQueue(maxHistoryLength + 1);
        this.pushIds = new HashSet(maxHistoryLength + 1);
        if (json != null) {
            JSONObject jsonHistory = json.optJSONObject("seen");
            if (jsonHistory != null) {
                Iterator<String> it = jsonHistory.keys();
                while (it.hasNext()) {
                    String pushId = (String) it.next();
                    String timestamp = jsonHistory.optString(pushId, null);
                    if (!(pushId == null || timestamp == null)) {
                        tryInsertPush(pushId, timestamp);
                    }
                }
            }
            setLastReceivedTimestamp(json.optString("lastTime", null));
        }
    }

    public JSONObject toJSON() throws JSONException {
        JSONObject json = new JSONObject();
        if (this.entries.size() > 0) {
            JSONObject history = new JSONObject();
            Iterator i$ = this.entries.iterator();
            while (i$.hasNext()) {
                Entry e = (Entry) i$.next();
                history.put(e.pushId, e.timestamp);
            }
            json.put("seen", history);
        }
        json.putOpt("lastTime", this.lastTime);
        return json;
    }

    public String getLastReceivedTimestamp() {
        return this.lastTime;
    }

    public void setLastReceivedTimestamp(String lastTime) {
        this.lastTime = lastTime;
    }

    public boolean tryInsertPush(String pushId, String timestamp) {
        if (timestamp == null) {
            throw new IllegalArgumentException("Can't insert null pushId or timestamp into history");
        }
        if (this.lastTime == null || timestamp.compareTo(this.lastTime) > 0) {
            this.lastTime = timestamp;
        }
        if (this.pushIds.contains(pushId)) {
            PLog.e(TAG, "Ignored duplicate push " + pushId);
            return false;
        }
        this.entries.add(new Entry(pushId, timestamp));
        this.pushIds.add(pushId);
        while (this.entries.size() > this.maxHistoryLength) {
            this.pushIds.remove(((Entry) this.entries.remove()).pushId);
        }
        return true;
    }
}
