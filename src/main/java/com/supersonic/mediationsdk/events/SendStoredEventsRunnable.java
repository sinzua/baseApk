package com.supersonic.mediationsdk.events;

import android.text.TextUtils;
import com.supersonic.mediationsdk.server.HttpFunctions;
import com.supersonic.mediationsdk.utils.SupersonicUtils;
import java.util.ArrayList;
import java.util.Iterator;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

class SendStoredEventsRunnable implements Runnable {
    private final String KEY_EVENTS = EventEntry.TABLE_NAME;
    private SupersonicDbHelper mDbHelper;
    private JSONObject mGeneralProperties;
    private ArrayList<Event> mLocalEvents;
    private String mUrl;

    public void run() {
        /* JADX: method processing error */
/*
Error: java.util.NoSuchElementException
	at java.util.HashMap$HashIterator.nextEntry(Unknown Source)
	at java.util.HashMap$KeyIterator.next(Unknown Source)
	at jadx.core.dex.visitors.blocksmaker.BlockFinallyExtract.applyRemove(BlockFinallyExtract.java:535)
	at jadx.core.dex.visitors.blocksmaker.BlockFinallyExtract.extractFinally(BlockFinallyExtract.java:175)
	at jadx.core.dex.visitors.blocksmaker.BlockFinallyExtract.processExceptionHandler(BlockFinallyExtract.java:79)
	at jadx.core.dex.visitors.blocksmaker.BlockFinallyExtract.visit(BlockFinallyExtract.java:51)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:31)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.ProcessClass.process(ProcessClass.java:37)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:306)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
*/
        /*
        r21 = this;
        r0 = r21;
        r3 = r0.mUrl;
        r3 = android.text.TextUtils.isEmpty(r3);
        if (r3 != 0) goto L_0x0010;
    L_0x000a:
        r0 = r21;
        r3 = r0.mDbHelper;
        if (r3 != 0) goto L_0x0011;
    L_0x0010:
        return;
    L_0x0011:
        r0 = r21;
        r3 = r0.mDbHelper;
        r2 = r3.getWritableDatabase();
        r18 = new org.json.JSONArray;	 Catch:{ Exception -> 0x00a6, all -> 0x00ea }
        r18.<init>();	 Catch:{ Exception -> 0x00a6, all -> 0x00ea }
        r3 = "events";	 Catch:{ Exception -> 0x00a6, all -> 0x00ea }
        r4 = 0;	 Catch:{ Exception -> 0x00a6, all -> 0x00ea }
        r5 = 0;	 Catch:{ Exception -> 0x00a6, all -> 0x00ea }
        r6 = 0;	 Catch:{ Exception -> 0x00a6, all -> 0x00ea }
        r7 = 0;	 Catch:{ Exception -> 0x00a6, all -> 0x00ea }
        r8 = 0;	 Catch:{ Exception -> 0x00a6, all -> 0x00ea }
        r9 = 0;	 Catch:{ Exception -> 0x00a6, all -> 0x00ea }
        r15 = r2.query(r3, r4, r5, r6, r7, r8, r9);	 Catch:{ Exception -> 0x00a6, all -> 0x00ea }
        r3 = r15.getCount();	 Catch:{ Exception -> 0x00a6, all -> 0x00ea }
        if (r3 <= 0) goto L_0x00bc;	 Catch:{ Exception -> 0x00a6, all -> 0x00ea }
    L_0x0030:
        r15.moveToFirst();	 Catch:{ Exception -> 0x00a6, all -> 0x00ea }
    L_0x0033:
        r3 = r15.isAfterLast();	 Catch:{ Exception -> 0x00a6, all -> 0x00ea }
        if (r3 != 0) goto L_0x00b2;	 Catch:{ Exception -> 0x00a6, all -> 0x00ea }
    L_0x0039:
        r3 = "eventid";	 Catch:{ Exception -> 0x00a6, all -> 0x00ea }
        r3 = r15.getColumnIndex(r3);	 Catch:{ Exception -> 0x00a6, all -> 0x00ea }
        r5 = r15.getInt(r3);	 Catch:{ Exception -> 0x00a6, all -> 0x00ea }
        r3 = "provider";	 Catch:{ Exception -> 0x00a6, all -> 0x00ea }
        r3 = r15.getColumnIndex(r3);	 Catch:{ Exception -> 0x00a6, all -> 0x00ea }
        r6 = r15.getString(r3);	 Catch:{ Exception -> 0x00a6, all -> 0x00ea }
        r3 = "sessiondepth";	 Catch:{ Exception -> 0x00a6, all -> 0x00ea }
        r3 = r15.getColumnIndex(r3);	 Catch:{ Exception -> 0x00a6, all -> 0x00ea }
        r7 = r15.getInt(r3);	 Catch:{ Exception -> 0x00a6, all -> 0x00ea }
        r3 = "timestamp";	 Catch:{ Exception -> 0x00a6, all -> 0x00ea }
        r3 = r15.getColumnIndex(r3);	 Catch:{ Exception -> 0x00a6, all -> 0x00ea }
        r8 = r15.getLong(r3);	 Catch:{ Exception -> 0x00a6, all -> 0x00ea }
        r3 = "status";	 Catch:{ Exception -> 0x00a6, all -> 0x00ea }
        r3 = r15.getColumnIndex(r3);	 Catch:{ Exception -> 0x00a6, all -> 0x00ea }
        r10 = r15.getInt(r3);	 Catch:{ Exception -> 0x00a6, all -> 0x00ea }
        r3 = "placement";	 Catch:{ Exception -> 0x00a6, all -> 0x00ea }
        r3 = r15.getColumnIndex(r3);	 Catch:{ Exception -> 0x00a6, all -> 0x00ea }
        r11 = r15.getString(r3);	 Catch:{ Exception -> 0x00a6, all -> 0x00ea }
        r3 = "reward_name";	 Catch:{ Exception -> 0x00a6, all -> 0x00ea }
        r3 = r15.getColumnIndex(r3);	 Catch:{ Exception -> 0x00a6, all -> 0x00ea }
        r12 = r15.getString(r3);	 Catch:{ Exception -> 0x00a6, all -> 0x00ea }
        r3 = "reward_amount";	 Catch:{ Exception -> 0x00a6, all -> 0x00ea }
        r3 = r15.getColumnIndex(r3);	 Catch:{ Exception -> 0x00a6, all -> 0x00ea }
        r13 = r15.getInt(r3);	 Catch:{ Exception -> 0x00a6, all -> 0x00ea }
        r3 = "transId";	 Catch:{ Exception -> 0x00a6, all -> 0x00ea }
        r3 = r15.getColumnIndex(r3);	 Catch:{ Exception -> 0x00a6, all -> 0x00ea }
        r14 = r15.getString(r3);	 Catch:{ Exception -> 0x00a6, all -> 0x00ea }
        r4 = r21;	 Catch:{ Exception -> 0x00a6, all -> 0x00ea }
        r17 = r4.createJSONForEvent(r5, r6, r7, r8, r10, r11, r12, r13, r14);	 Catch:{ Exception -> 0x00a6, all -> 0x00ea }
        if (r17 == 0) goto L_0x00a2;	 Catch:{ Exception -> 0x00a6, all -> 0x00ea }
    L_0x009b:
        r0 = r18;	 Catch:{ Exception -> 0x00a6, all -> 0x00ea }
        r1 = r17;	 Catch:{ Exception -> 0x00a6, all -> 0x00ea }
        r0.put(r1);	 Catch:{ Exception -> 0x00a6, all -> 0x00ea }
    L_0x00a2:
        r15.moveToNext();	 Catch:{ Exception -> 0x00a6, all -> 0x00ea }
        goto L_0x0033;
    L_0x00a6:
        r3 = move-exception;
        r3 = r2.isOpen();
        if (r3 == 0) goto L_0x0010;
    L_0x00ad:
        r2.close();
        goto L_0x0010;
    L_0x00b2:
        r3 = "events";	 Catch:{ Exception -> 0x00a6, all -> 0x00ea }
        r4 = 0;	 Catch:{ Exception -> 0x00a6, all -> 0x00ea }
        r20 = 0;	 Catch:{ Exception -> 0x00a6, all -> 0x00ea }
        r0 = r20;	 Catch:{ Exception -> 0x00a6, all -> 0x00ea }
        r2.delete(r3, r4, r0);	 Catch:{ Exception -> 0x00a6, all -> 0x00ea }
    L_0x00bc:
        r0 = r21;	 Catch:{ Exception -> 0x00a6, all -> 0x00ea }
        r3 = r0.mLocalEvents;	 Catch:{ Exception -> 0x00a6, all -> 0x00ea }
        r0 = r21;	 Catch:{ Exception -> 0x00a6, all -> 0x00ea }
        r1 = r18;	 Catch:{ Exception -> 0x00a6, all -> 0x00ea }
        r19 = r0.createEventsArray(r1, r3);	 Catch:{ Exception -> 0x00a6, all -> 0x00ea }
        if (r19 == 0) goto L_0x00df;	 Catch:{ Exception -> 0x00a6, all -> 0x00ea }
    L_0x00ca:
        r3 = r19.length();	 Catch:{ Exception -> 0x00a6, all -> 0x00ea }
        if (r3 <= 0) goto L_0x00df;	 Catch:{ Exception -> 0x00a6, all -> 0x00ea }
    L_0x00d0:
        r0 = r21;	 Catch:{ Exception -> 0x00a6, all -> 0x00ea }
        r1 = r19;	 Catch:{ Exception -> 0x00a6, all -> 0x00ea }
        r16 = r0.createDataToSend(r1);	 Catch:{ Exception -> 0x00a6, all -> 0x00ea }
        r0 = r21;	 Catch:{ Exception -> 0x00a6, all -> 0x00ea }
        r1 = r16;	 Catch:{ Exception -> 0x00a6, all -> 0x00ea }
        r0.sendEvents(r1);	 Catch:{ Exception -> 0x00a6, all -> 0x00ea }
    L_0x00df:
        r3 = r2.isOpen();
        if (r3 == 0) goto L_0x0010;
    L_0x00e5:
        r2.close();
        goto L_0x0010;
    L_0x00ea:
        r3 = move-exception;
        r4 = r2.isOpen();
        if (r4 == 0) goto L_0x00f4;
    L_0x00f1:
        r2.close();
    L_0x00f4:
        throw r3;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.supersonic.mediationsdk.events.SendStoredEventsRunnable.run():void");
    }

    public SendStoredEventsRunnable(SupersonicDbHelper dbHelper, String url, JSONObject generalProperties, ArrayList<Event> localEvents) {
        this.mDbHelper = dbHelper;
        this.mUrl = url;
        if (localEvents == null) {
            this.mLocalEvents = new ArrayList();
        } else {
            this.mLocalEvents = localEvents;
        }
        if (generalProperties == null) {
            this.mGeneralProperties = new JSONObject();
        } else {
            this.mGeneralProperties = generalProperties;
        }
    }

    private JSONArray createEventsArray(JSONArray events, ArrayList<Event> inMemoryEvents) {
        if (events == null) {
            events = new JSONArray();
        }
        if (inMemoryEvents != null) {
            Iterator i$ = inMemoryEvents.iterator();
            while (i$.hasNext()) {
                events.put(((Event) i$.next()).toJSON());
            }
        }
        return events;
    }

    private void sendEvents(String dataToSend) {
        if (!TextUtils.isEmpty(dataToSend)) {
            boolean succeed = HttpFunctions.getStringFromPost(this.mUrl, dataToSend);
        }
    }

    private String createDataToSend(JSONArray events) {
        String result = "";
        try {
            if (this.mGeneralProperties != null) {
                this.mGeneralProperties.put("timestamp", SupersonicUtils.getTimeStamp());
                this.mGeneralProperties.put(EventEntry.TABLE_NAME, events);
                result = this.mGeneralProperties.toString();
            }
        } catch (Exception e) {
        }
        return result;
    }

    private JSONObject createJSONForEvent(int eventId, String provider, int sessionDepth, long timestamp, int status, String placementName, String rewardName, int rewardAmount, String transId) {
        JSONObject event = new JSONObject();
        try {
            event.put(Event.KEY_EVENT_ID, eventId);
            event.put("provider", provider);
            event.put(Event.KEY_SESSION_DEPTH, sessionDepth);
            event.put("timestamp", timestamp);
            if (status != -1) {
                event.put("status", status == 1 ? "true" : "false");
            }
            if (!TextUtils.isEmpty(placementName)) {
                event.put("placement", placementName);
            }
            if (!TextUtils.isEmpty(rewardName)) {
                event.put(Event.KEY_REWARD_NAME, rewardName);
            }
            if (rewardAmount != -1) {
                event.put(Event.KEY_REWARD_AMOUNT, rewardAmount);
            }
            if (TextUtils.isEmpty(transId)) {
                return event;
            }
            event.put("transId", transId);
            return event;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
}
