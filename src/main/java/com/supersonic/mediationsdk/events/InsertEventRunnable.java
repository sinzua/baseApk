package com.supersonic.mediationsdk.events;

import android.content.ContentValues;
import java.util.ArrayList;

class InsertEventRunnable implements Runnable {
    private SupersonicDbHelper mDbHelper;
    private ArrayList<Event> mEvents;

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
        r6 = this;
        r4 = r6.mEvents;
        if (r4 == 0) goto L_0x0010;
    L_0x0004:
        r4 = r6.mEvents;
        r4 = r4.size();
        if (r4 == 0) goto L_0x0010;
    L_0x000c:
        r4 = r6.mDbHelper;
        if (r4 != 0) goto L_0x0011;
    L_0x0010:
        return;
    L_0x0011:
        r4 = r6.mDbHelper;
        r0 = r4.getWritableDatabase();
        r4 = r6.mEvents;	 Catch:{ Exception -> 0x0036, all -> 0x004b }
        r1 = r4.iterator();	 Catch:{ Exception -> 0x0036, all -> 0x004b }
    L_0x001d:
        r4 = r1.hasNext();	 Catch:{ Exception -> 0x0036, all -> 0x004b }
        if (r4 == 0) goto L_0x0041;	 Catch:{ Exception -> 0x0036, all -> 0x004b }
    L_0x0023:
        r2 = r1.next();	 Catch:{ Exception -> 0x0036, all -> 0x004b }
        r2 = (com.supersonic.mediationsdk.events.Event) r2;	 Catch:{ Exception -> 0x0036, all -> 0x004b }
        r3 = r6.getContentValuesForEvent(r2);	 Catch:{ Exception -> 0x0036, all -> 0x004b }
        if (r3 == 0) goto L_0x001d;	 Catch:{ Exception -> 0x0036, all -> 0x004b }
    L_0x002f:
        r4 = "events";	 Catch:{ Exception -> 0x0036, all -> 0x004b }
        r5 = 0;	 Catch:{ Exception -> 0x0036, all -> 0x004b }
        r0.insert(r4, r5, r3);	 Catch:{ Exception -> 0x0036, all -> 0x004b }
        goto L_0x001d;
    L_0x0036:
        r4 = move-exception;
        r4 = r0.isOpen();
        if (r4 == 0) goto L_0x0010;
    L_0x003d:
        r0.close();
        goto L_0x0010;
    L_0x0041:
        r4 = r0.isOpen();
        if (r4 == 0) goto L_0x0010;
    L_0x0047:
        r0.close();
        goto L_0x0010;
    L_0x004b:
        r4 = move-exception;
        r5 = r0.isOpen();
        if (r5 == 0) goto L_0x0055;
    L_0x0052:
        r0.close();
    L_0x0055:
        throw r4;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.supersonic.mediationsdk.events.InsertEventRunnable.run():void");
    }

    public InsertEventRunnable(SupersonicDbHelper dbHelper, ArrayList<Event> eventsToInsert) {
        this.mDbHelper = dbHelper;
        if (eventsToInsert == null) {
            this.mEvents = new ArrayList();
        } else {
            this.mEvents = eventsToInsert;
        }
    }

    private ContentValues getContentValuesForEvent(Event event) {
        if (event == null) {
            return null;
        }
        ContentValues values = new ContentValues(9);
        values.put(EventEntry.COLUMN_NAME_EVENT_ID, Integer.valueOf(event.getEventId()));
        values.put("timestamp", Long.valueOf(event.getTimestamp()));
        values.put("provider", event.getProvider());
        values.put(EventEntry.COLUMN_NAME_SESSION_DEPTH, Integer.valueOf(event.getSessionDepth()));
        values.put("status", Integer.valueOf(event.getStatus()));
        values.put("placement", event.getPlacementName());
        values.put(EventEntry.COLUMN_NAME_REWARD_NAME, event.getRewardName());
        values.put(EventEntry.COLUMN_NAME_REWARD_AMOUNT, Integer.valueOf(event.getRewardAmount()));
        values.put("transId", event.getTransId());
        return values;
    }
}
