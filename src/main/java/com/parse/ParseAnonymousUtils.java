package com.parse;

import bolts.Task;

public final class ParseAnonymousUtils {
    static final String AUTH_TYPE = "anonymous";
    private static AnonymousAuthenticationProvider provider;

    static AnonymousAuthenticationProvider getProvider() {
        if (provider == null) {
            provider = new AnonymousAuthenticationProvider();
            ParseUser.registerAuthenticationProvider(provider);
        }
        return provider;
    }

    public static boolean isLinked(ParseUser user) {
        return user.isLinked(AUTH_TYPE);
    }

    public static Task<ParseUser> logInInBackground() {
        return getProvider().logInAsync();
    }

    public static void logIn(LogInCallback callback) {
        ParseTaskUtils.callbackOnMainThreadAsync(logInInBackground(), (ParseCallback2) callback);
    }

    private ParseAnonymousUtils() {
    }
}
