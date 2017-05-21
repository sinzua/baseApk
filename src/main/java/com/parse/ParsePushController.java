package com.parse;

import bolts.Task;

class ParsePushController {
    static final String DEVICE_TYPE_ANDROID = "android";
    static final String DEVICE_TYPE_IOS = "ios";
    private final ParseHttpClient restClient;

    public ParsePushController(ParseHttpClient restClient) {
        this.restClient = restClient;
    }

    public Task<Void> sendInBackground(State state, String sessionToken) {
        return buildRESTSendPushCommand(state, sessionToken).executeAsync(this.restClient).makeVoid();
    }

    ParseRESTCommand buildRESTSendPushCommand(State state, String sessionToken) {
        String deviceType = null;
        if (state.queryState() == null) {
            boolean willPushToIOS;
            boolean willPushToAndroid = state.pushToAndroid() != null && state.pushToAndroid().booleanValue();
            if (state.pushToIOS() == null || !state.pushToIOS().booleanValue()) {
                willPushToIOS = false;
            } else {
                willPushToIOS = true;
            }
            if (!(willPushToIOS && willPushToAndroid)) {
                if (willPushToIOS) {
                    deviceType = DEVICE_TYPE_IOS;
                } else if (willPushToAndroid) {
                    deviceType = DEVICE_TYPE_ANDROID;
                }
            }
        }
        return ParseRESTPushCommand.sendPushCommand(state.queryState(), state.channelSet(), deviceType, state.expirationTime(), state.expirationTimeInterval(), state.data(), sessionToken);
    }
}
