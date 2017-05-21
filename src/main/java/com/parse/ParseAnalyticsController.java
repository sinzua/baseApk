package com.parse;

import bolts.Task;
import org.json.JSONObject;

class ParseAnalyticsController {
    ParseEventuallyQueue eventuallyQueue;

    public ParseAnalyticsController(ParseEventuallyQueue eventuallyQueue) {
        this.eventuallyQueue = eventuallyQueue;
    }

    public Task<Void> trackEventInBackground(String name, JSONObject jsonDimensions, String sessionToken) {
        return this.eventuallyQueue.enqueueEventuallyAsync(ParseRESTAnalyticsCommand.trackEventCommand(name, jsonDimensions, sessionToken), null).makeVoid();
    }

    public Task<Void> trackAppOpenedInBackground(String pushHash, String sessionToken) {
        return this.eventuallyQueue.enqueueEventuallyAsync(ParseRESTAnalyticsCommand.trackAppOpenedCommand(pushHash, sessionToken), null).makeVoid();
    }
}
