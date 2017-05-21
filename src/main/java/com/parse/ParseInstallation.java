package com.parse;

import android.content.Context;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.text.TextUtils;
import bolts.Continuation;
import bolts.Task;
import com.nativex.monetization.mraid.objects.ObjectNames.CalendarEntryData;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

@ParseClassName("_Installation")
public class ParseInstallation extends ParseObject {
    private static final String KEY_APP_IDENTIFIER = "appIdentifier";
    private static final String KEY_APP_NAME = "appName";
    private static final String KEY_APP_VERSION = "appVersion";
    static final String KEY_CHANNELS = "channels";
    private static final String KEY_DEVICE_TOKEN = "deviceToken";
    private static final String KEY_DEVICE_TYPE = "deviceType";
    private static final String KEY_INSTALLATION_ID = "installationId";
    private static final String KEY_LOCALE = "localeIdentifier";
    private static final String KEY_PARSE_VERSION = "parseVersion";
    private static final String KEY_PUSH_TYPE = "pushType";
    private static final String KEY_TIME_ZONE = "timeZone";
    private static final List<String> READ_ONLY_FIELDS = Collections.unmodifiableList(Arrays.asList(new String[]{KEY_DEVICE_TYPE, KEY_INSTALLATION_ID, KEY_DEVICE_TOKEN, KEY_PUSH_TYPE, "timeZone", KEY_LOCALE, "appVersion", "appName", KEY_PARSE_VERSION, KEY_APP_IDENTIFIER}));
    private static final String TAG = "com.parse.ParseInstallation";

    static ParseCurrentInstallationController getCurrentInstallationController() {
        return ParseCorePlugins.getInstance().getCurrentInstallationController();
    }

    public static ParseInstallation getCurrentInstallation() {
        try {
            return (ParseInstallation) ParseTaskUtils.wait(getCurrentInstallationController().getAsync());
        } catch (ParseException e) {
            return null;
        }
    }

    public static ParseQuery<ParseInstallation> getQuery() {
        return ParseQuery.getQuery(ParseInstallation.class);
    }

    public String getInstallationId() {
        return getString(KEY_INSTALLATION_ID);
    }

    boolean needsDefaultACL() {
        return false;
    }

    boolean isKeyMutable(String key) {
        return !READ_ONLY_FIELDS.contains(key);
    }

    void updateBeforeSave() {
        super.updateBeforeSave();
        if (getCurrentInstallationController().isCurrent(this)) {
            updateTimezone();
            updateVersionInfo();
            updateDeviceInfo();
            updateLocaleIdentifier();
        }
    }

    <T extends ParseObject> Task<T> fetchAsync(final String sessionToken, final Task<Void> toAwait) {
        Task<T> onSuccessTask;
        synchronized (this.mutex) {
            Task<Void> result;
            if (getObjectId() == null) {
                result = saveAsync(sessionToken, (Task) toAwait);
            } else {
                result = Task.forResult(null);
            }
            onSuccessTask = result.onSuccessTask(new Continuation<Void, Task<T>>() {
                public Task<T> then(Task<Void> task) throws Exception {
                    return super.fetchAsync(sessionToken, toAwait);
                }
            });
        }
        return onSuccessTask;
    }

    Task<Void> handleSaveResultAsync(State result, ParseOperationSet operationsBeforeSave) {
        Task<Void> task = super.handleSaveResultAsync(result, operationsBeforeSave);
        return result == null ? task : task.onSuccessTask(new Continuation<Void, Task<Void>>() {
            public Task<Void> then(Task<Void> task) throws Exception {
                return ParseInstallation.getCurrentInstallationController().setAsync(ParseInstallation.this);
            }
        });
    }

    Task<Void> handleFetchResultAsync(State newState) {
        return super.handleFetchResultAsync(newState).onSuccessTask(new Continuation<Void, Task<Void>>() {
            public Task<Void> then(Task<Void> task) throws Exception {
                return ParseInstallation.getCurrentInstallationController().setAsync(ParseInstallation.this);
            }
        });
    }

    private void updateTimezone() {
        String zone = TimeZone.getDefault().getID();
        if ((zone.indexOf(47) > 0 || zone.equals("GMT")) && !zone.equals(get("timeZone"))) {
            performPut("timeZone", zone);
        }
    }

    private void updateVersionInfo() {
        synchronized (this.mutex) {
            try {
                Context context = Parse.getApplicationContext();
                String packageName = context.getPackageName();
                PackageManager pm = context.getPackageManager();
                String appVersion = pm.getPackageInfo(packageName, 0).versionName;
                String appName = pm.getApplicationLabel(pm.getApplicationInfo(packageName, 0)).toString();
                if (!(packageName == null || packageName.equals(get(KEY_APP_IDENTIFIER)))) {
                    performPut(KEY_APP_IDENTIFIER, packageName);
                }
                if (!(appName == null || appName.equals(get("appName")))) {
                    performPut("appName", appName);
                }
                if (!(appVersion == null || appVersion.equals(get("appVersion")))) {
                    performPut("appVersion", appVersion);
                }
            } catch (NameNotFoundException e) {
                PLog.w(TAG, "Cannot load package info; will not be saved to installation");
            }
            if (!"1.10.1".equals(get(KEY_PARSE_VERSION))) {
                performPut(KEY_PARSE_VERSION, "1.10.1");
            }
        }
    }

    private void updateLocaleIdentifier() {
        Locale locale = Locale.getDefault();
        String language = locale.getLanguage();
        String country = locale.getCountry();
        if (!TextUtils.isEmpty(language)) {
            if (language.equals("iw")) {
                language = "he";
            }
            if (language.equals("in")) {
                language = CalendarEntryData.ID;
            }
            if (language.equals("ji")) {
                language = "yi";
            }
            String localeString = language;
            if (!TextUtils.isEmpty(country)) {
                localeString = String.format(Locale.US, "%s-%s", new Object[]{language, country});
            }
            if (!localeString.equals(get(KEY_LOCALE))) {
                performPut(KEY_LOCALE, localeString);
            }
        }
    }

    void updateDeviceInfo() {
        updateDeviceInfo(ParsePlugins.get().installationId());
    }

    void updateDeviceInfo(InstallationId installationId) {
        if (!has(KEY_INSTALLATION_ID)) {
            performPut(KEY_INSTALLATION_ID, installationId.get());
        }
        String deviceType = "android";
        if (!deviceType.equals(get(KEY_DEVICE_TYPE))) {
            performPut(KEY_DEVICE_TYPE, deviceType);
        }
    }

    PushType getPushType() {
        return PushType.fromString(super.getString(KEY_PUSH_TYPE));
    }

    void setPushType(PushType pushType) {
        if (pushType != null) {
            performPut(KEY_PUSH_TYPE, pushType.toString());
        }
    }

    void removePushType() {
        performRemove(KEY_PUSH_TYPE);
    }

    String getDeviceToken() {
        return super.getString(KEY_DEVICE_TOKEN);
    }

    void setDeviceToken(String deviceToken) {
        if (deviceToken != null && deviceToken.length() > 0) {
            performPut(KEY_DEVICE_TOKEN, deviceToken);
        }
    }

    void removeDeviceToken() {
        performRemove(KEY_DEVICE_TOKEN);
    }
}
