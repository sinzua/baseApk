package com.parse;

import bolts.Task;
import com.nativex.monetization.mraid.objects.ObjectNames.CalendarEntryData;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

class AnonymousAuthenticationProvider extends ParseAuthenticationProvider {
    AnonymousAuthenticationProvider() {
    }

    public Task<Map<String, String>> authenticateAsync() {
        return Task.forResult(getAuthData());
    }

    public Map<String, String> getAuthData() {
        Map<String, String> authData = new HashMap();
        authData.put(CalendarEntryData.ID, UUID.randomUUID().toString());
        return authData;
    }

    public void deauthenticate() {
    }

    public boolean restoreAuthentication(Map<String, String> map) {
        return true;
    }

    public void cancel() {
    }

    public String getAuthType() {
        return "anonymous";
    }
}
