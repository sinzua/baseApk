package com.parse;

import bolts.Continuation;
import bolts.Task;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import org.json.JSONException;
import org.json.JSONObject;

public class ParsePush {
    static String KEY_DATA_MESSAGE = "alert";
    private static final String TAG = "com.parse.ParsePush";
    final Builder builder;

    static class State {
        private final Set<String> channelSet;
        private final JSONObject data;
        private final Long expirationTime;
        private final Long expirationTimeInterval;
        private final Boolean pushToAndroid;
        private final Boolean pushToIOS;
        private final State<ParseInstallation> queryState;

        static class Builder {
            private Set<String> channelSet;
            private JSONObject data;
            private Long expirationTime;
            private Long expirationTimeInterval;
            private Boolean pushToAndroid;
            private Boolean pushToIOS;
            private ParseQuery<ParseInstallation> query;

            Builder() {
            }

            public Builder expirationTime(Long expirationTime) {
                this.expirationTime = expirationTime;
                this.expirationTimeInterval = null;
                return this;
            }

            public Builder expirationTimeInterval(Long expirationTimeInterval) {
                this.expirationTimeInterval = expirationTimeInterval;
                this.expirationTime = null;
                return this;
            }

            public Builder pushToIOS(Boolean pushToIOS) {
                ParsePush.checkArgument(this.query == null, "Cannot set push targets (i.e. setPushToAndroid or setPushToIOS) when pushing to a query");
                this.pushToIOS = pushToIOS;
                return this;
            }

            public Builder pushToAndroid(Boolean pushToAndroid) {
                ParsePush.checkArgument(this.query == null, "Cannot set push targets (i.e. setPushToAndroid or setPushToIOS) when pushing to a query");
                this.pushToAndroid = pushToAndroid;
                return this;
            }

            public Builder data(JSONObject data) {
                this.data = data;
                return this;
            }

            public Builder channelSet(Collection<String> channelSet) {
                boolean z;
                if (channelSet != null) {
                    z = true;
                } else {
                    z = false;
                }
                ParsePush.checkArgument(z, "channels collection cannot be null");
                for (String channel : channelSet) {
                    if (channel != null) {
                        z = true;
                    } else {
                        z = false;
                    }
                    ParsePush.checkArgument(z, "channel cannot be null");
                }
                this.channelSet = new HashSet(channelSet);
                this.query = null;
                return this;
            }

            public Builder query(ParseQuery<ParseInstallation> query) {
                boolean z = true;
                ParsePush.checkArgument(query != null, "Cannot target a null query");
                if (!(this.pushToIOS == null && this.pushToAndroid == null)) {
                    z = false;
                }
                ParsePush.checkArgument(z, "Cannot set push targets (i.e. setPushToAndroid or setPushToIOS) when pushing to a query");
                ParsePush.checkArgument(query.getClassName().equals(ParseObject.getClassName(ParseInstallation.class)), "Can only push to a query for Installations");
                this.channelSet = null;
                this.query = query;
                return this;
            }

            public State build() {
                if (this.data != null) {
                    return new State();
                }
                throw new IllegalArgumentException("Cannot send a push without calling either setMessage or setData");
            }
        }

        private State(Builder builder) {
            State state = null;
            this.channelSet = builder.channelSet == null ? null : Collections.unmodifiableSet(new HashSet(builder.channelSet));
            if (builder.query != null) {
                state = builder.query.getBuilder().build();
            }
            this.queryState = state;
            this.expirationTime = builder.expirationTime;
            this.expirationTimeInterval = builder.expirationTimeInterval;
            this.pushToIOS = builder.pushToIOS;
            this.pushToAndroid = builder.pushToAndroid;
            JSONObject copyData = null;
            try {
                copyData = new JSONObject(builder.data.toString());
            } catch (JSONException e) {
            }
            this.data = copyData;
        }

        public Set<String> channelSet() {
            return this.channelSet;
        }

        public State<ParseInstallation> queryState() {
            return this.queryState;
        }

        public Long expirationTime() {
            return this.expirationTime;
        }

        public Long expirationTimeInterval() {
            return this.expirationTimeInterval;
        }

        public Boolean pushToIOS() {
            return this.pushToIOS;
        }

        public Boolean pushToAndroid() {
            return this.pushToAndroid;
        }

        public JSONObject data() {
            try {
                return new JSONObject(this.data.toString());
            } catch (JSONException e) {
                return null;
            }
        }
    }

    static ParsePushController getPushController() {
        return ParseCorePlugins.getInstance().getPushController();
    }

    static ParsePushChannelsController getPushChannelsController() {
        return ParseCorePlugins.getInstance().getPushChannelsController();
    }

    private static void checkArgument(boolean expression, Object errorMessage) {
        if (!expression) {
            throw new IllegalArgumentException(String.valueOf(errorMessage));
        }
    }

    public ParsePush() {
        this(new Builder());
    }

    private ParsePush(Builder builder) {
        this.builder = builder;
    }

    public static Task<Void> subscribeInBackground(String channel) {
        return getPushChannelsController().subscribeInBackground(channel);
    }

    public static void subscribeInBackground(String channel, SaveCallback callback) {
        ParseTaskUtils.callbackOnMainThreadAsync(subscribeInBackground(channel), (ParseCallback1) callback);
    }

    public static Task<Void> unsubscribeInBackground(String channel) {
        return getPushChannelsController().unsubscribeInBackground(channel);
    }

    public static void unsubscribeInBackground(String channel, SaveCallback callback) {
        ParseTaskUtils.callbackOnMainThreadAsync(unsubscribeInBackground(channel), (ParseCallback1) callback);
    }

    public static Task<Void> sendMessageInBackground(String message, ParseQuery<ParseInstallation> query) {
        ParsePush push = new ParsePush();
        push.setQuery(query);
        push.setMessage(message);
        return push.sendInBackground();
    }

    public static void sendMessageInBackground(String message, ParseQuery<ParseInstallation> query, SendCallback callback) {
        ParseTaskUtils.callbackOnMainThreadAsync(sendMessageInBackground(message, query), (ParseCallback1) callback);
    }

    public static Task<Void> sendDataInBackground(JSONObject data, ParseQuery<ParseInstallation> query) {
        ParsePush push = new ParsePush();
        push.setQuery(query);
        push.setData(data);
        return push.sendInBackground();
    }

    public static void sendDataInBackground(JSONObject data, ParseQuery<ParseInstallation> query, SendCallback callback) {
        ParseTaskUtils.callbackOnMainThreadAsync(sendDataInBackground(data, query), (ParseCallback1) callback);
    }

    public void setChannel(String channel) {
        this.builder.channelSet(Collections.singletonList(channel));
    }

    public void setChannels(Collection<String> channels) {
        this.builder.channelSet(channels);
    }

    public void setQuery(ParseQuery<ParseInstallation> query) {
        this.builder.query(query);
    }

    public void setExpirationTime(long time) {
        this.builder.expirationTime(Long.valueOf(time));
    }

    public void setExpirationTimeInterval(long timeInterval) {
        this.builder.expirationTimeInterval(Long.valueOf(timeInterval));
    }

    public void clearExpiration() {
        this.builder.expirationTime(null);
        this.builder.expirationTimeInterval(null);
    }

    @Deprecated
    public void setPushToIOS(boolean pushToIOS) {
        this.builder.pushToIOS(Boolean.valueOf(pushToIOS));
    }

    @Deprecated
    public void setPushToAndroid(boolean pushToAndroid) {
        this.builder.pushToAndroid(Boolean.valueOf(pushToAndroid));
    }

    public void setData(JSONObject data) {
        this.builder.data(data);
    }

    public void setMessage(String message) {
        JSONObject data = new JSONObject();
        try {
            data.put(KEY_DATA_MESSAGE, message);
        } catch (JSONException e) {
            PLog.e(TAG, "JSONException in setMessage", e);
        }
        setData(data);
    }

    public Task<Void> sendInBackground() {
        final State state = this.builder.build();
        return ParseUser.getCurrentSessionTokenAsync().onSuccessTask(new Continuation<String, Task<Void>>() {
            public Task<Void> then(Task<String> task) throws Exception {
                return ParsePush.getPushController().sendInBackground(state, (String) task.getResult());
            }
        });
    }

    public void send() throws ParseException {
        ParseTaskUtils.wait(sendInBackground());
    }

    public void sendInBackground(SendCallback callback) {
        ParseTaskUtils.callbackOnMainThreadAsync(sendInBackground(), (ParseCallback1) callback);
    }
}
