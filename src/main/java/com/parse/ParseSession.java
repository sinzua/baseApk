package com.parse;

import bolts.Continuation;
import bolts.Task;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@ParseClassName("_Session")
public class ParseSession extends ParseObject {
    private static final String KEY_CREATED_WITH = "createdWith";
    private static final String KEY_EXPIRES_AT = "expiresAt";
    private static final String KEY_INSTALLATION_ID = "installationId";
    private static final String KEY_RESTRICTED = "restricted";
    private static final String KEY_SESSION_TOKEN = "sessionToken";
    private static final String KEY_USER = "user";
    private static final List<String> READ_ONLY_KEYS = Collections.unmodifiableList(Arrays.asList(new String[]{KEY_SESSION_TOKEN, KEY_CREATED_WITH, KEY_RESTRICTED, KEY_USER, KEY_EXPIRES_AT, KEY_INSTALLATION_ID}));

    private static ParseSessionController getSessionController() {
        return ParseCorePlugins.getInstance().getSessionController();
    }

    public static Task<ParseSession> getCurrentSessionInBackground() {
        return ParseUser.getCurrentSessionTokenAsync().onSuccessTask(new Continuation<String, Task<ParseSession>>() {
            public Task<ParseSession> then(Task<String> task) throws Exception {
                String sessionToken = (String) task.getResult();
                if (sessionToken == null) {
                    return Task.forResult(null);
                }
                return ParseSession.getSessionController().getSessionAsync(sessionToken).onSuccess(new Continuation<State, ParseSession>() {
                    public ParseSession then(Task<State> task) throws Exception {
                        return (ParseSession) ParseObject.from((State) task.getResult());
                    }
                });
            }
        });
    }

    public static void getCurrentSessionInBackground(GetCallback<ParseSession> callback) {
        ParseTaskUtils.callbackOnMainThreadAsync(getCurrentSessionInBackground(), (ParseCallback2) callback);
    }

    static Task<Void> revokeAsync(String sessionToken) {
        if (sessionToken == null || !isRevocableSessionToken(sessionToken)) {
            return Task.forResult(null);
        }
        return getSessionController().revokeAsync(sessionToken);
    }

    static Task<String> upgradeToRevocableSessionAsync(String sessionToken) {
        if (sessionToken == null || isRevocableSessionToken(sessionToken)) {
            return Task.forResult(sessionToken);
        }
        return getSessionController().upgradeToRevocable(sessionToken).onSuccess(new Continuation<State, String>() {
            public String then(Task<State> task) throws Exception {
                return ((ParseSession) ParseObject.from((State) task.getResult())).getSessionToken();
            }
        });
    }

    static boolean isRevocableSessionToken(String sessionToken) {
        return sessionToken.contains("r:");
    }

    public static ParseQuery<ParseSession> getQuery() {
        return ParseQuery.getQuery(ParseSession.class);
    }

    boolean needsDefaultACL() {
        return false;
    }

    boolean isKeyMutable(String key) {
        return !READ_ONLY_KEYS.contains(key);
    }

    public String getSessionToken() {
        return getString(KEY_SESSION_TOKEN);
    }
}
