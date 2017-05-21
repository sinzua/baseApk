package com.parse;

import bolts.Continuation;
import bolts.Task;
import java.util.Map;

abstract class ParseAuthenticationProvider {

    interface ParseAuthenticationCallback {
        void onCancel();

        void onError(Throwable th);

        void onSuccess(Map<String, String> map);
    }

    public abstract Task<Map<String, String>> authenticateAsync();

    public abstract void cancel();

    public abstract void deauthenticate();

    public abstract String getAuthType();

    public abstract boolean restoreAuthentication(Map<String, String> map);

    ParseAuthenticationProvider() {
    }

    public Task<ParseUser> logInAsync() {
        return authenticateAsync().onSuccessTask(new Continuation<Map<String, String>, Task<ParseUser>>() {
            public Task<ParseUser> then(Task<Map<String, String>> task) throws Exception {
                return ParseAuthenticationProvider.this.logInAsync((Map) task.getResult());
            }
        });
    }

    public Task<ParseUser> logInAsync(Map<String, String> authData) {
        return ParseUser.logInWithAsync(getAuthType(), authData);
    }

    public Task<Void> linkAsync(final ParseUser user) {
        return authenticateAsync().onSuccessTask(new Continuation<Map<String, String>, Task<Void>>() {
            public Task<Void> then(Task<Map<String, String>> task) throws Exception {
                return ParseAuthenticationProvider.this.linkAsync(user, (Map) task.getResult());
            }
        });
    }

    public Task<Void> linkAsync(ParseUser user, Map<String, String> authData) {
        return user.linkWithAsync(getAuthType(), authData, user.getSessionToken());
    }

    public Task<Void> unlinkAsync(ParseUser user) {
        return user.unlinkFromAsync(getAuthType());
    }
}
