package com.parse;

import android.content.Context;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import bolts.Continuation;
import bolts.Task;
import com.supersonicads.sdk.precache.DownloadManager;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Parse {
    public static final int LOG_LEVEL_DEBUG = 3;
    public static final int LOG_LEVEL_ERROR = 6;
    public static final int LOG_LEVEL_INFO = 4;
    public static final int LOG_LEVEL_NONE = Integer.MAX_VALUE;
    public static final int LOG_LEVEL_VERBOSE = 2;
    public static final int LOG_LEVEL_WARNING = 5;
    private static final Object MUTEX = new Object();
    private static final Object MUTEX_CALLBACKS = new Object();
    private static final String PARSE_APPLICATION_ID = "com.parse.APPLICATION_ID";
    private static final String PARSE_CLIENT_KEY = "com.parse.CLIENT_KEY";
    private static Set<ParseCallbacks> callbacks = new HashSet();
    static ParseEventuallyQueue eventuallyQueue = null;
    private static List<ParseNetworkInterceptor> interceptors;
    private static boolean isLocalDatastoreEnabled;
    private static OfflineStore offlineStore;

    interface ParseCallbacks {
        void onParseInitialized();
    }

    public static void enableLocalDatastore(Context context) {
        if (isInitialized()) {
            throw new IllegalStateException("`Parse#enableLocalDatastore(Context)` must be invoked before `Parse#initialize(Context)`");
        }
        isLocalDatastoreEnabled = true;
    }

    static void disableLocalDatastore() {
        setLocalDatastore(null);
        ParseCorePlugins.getInstance().reset();
    }

    static OfflineStore getLocalDatastore() {
        return offlineStore;
    }

    static void setLocalDatastore(OfflineStore offlineStore) {
        isLocalDatastoreEnabled = offlineStore != null;
        offlineStore = offlineStore;
    }

    static boolean isLocalDatastoreEnabled() {
        return isLocalDatastoreEnabled;
    }

    public static void initialize(Context context) {
        Bundle metaData = ManifestInfo.getApplicationMetadata(context.getApplicationContext());
        if (metaData != null) {
            String applicationId = metaData.getString(PARSE_APPLICATION_ID);
            String clientKey = metaData.getString(PARSE_CLIENT_KEY);
            if (applicationId == null) {
                throw new RuntimeException("ApplicationId not defined. You must provide ApplicationId in AndroidManifest.xml.\n<meta-data\n    android:name=\"com.parse.APPLICATION_ID\"\n    android:value=\"<Your Application Id>\" />");
            } else if (clientKey == null) {
                throw new RuntimeException("ClientKey not defined. You must provide ClientKey in AndroidManifest.xml.\n<meta-data\n    android:name=\"com.parse.CLIENT_KEY\"\n    android:value=\"<Your Client Key>\" />");
            } else {
                initialize(context, applicationId, clientKey);
                return;
            }
        }
        throw new RuntimeException("Can't get Application Metadata");
    }

    public static void initialize(Context context, String applicationId, String clientKey) {
        Android.initialize(context, applicationId, clientKey);
        Context applicationContext = context.getApplicationContext();
        ParseHttpClient.setKeepAlive(true);
        ParseHttpClient.setMaxConnections(20);
        ParseRequest.setDefaultClient(ParsePlugins.get().restClient());
        if (interceptors != null) {
            initializeParseHttpClientsWithParseNetworkInterceptors();
        }
        ParseObject.registerParseSubclasses();
        if (isLocalDatastoreEnabled()) {
            offlineStore = new OfflineStore(context);
        } else {
            ParseKeyValueCache.initialize(context);
        }
        checkCacheApplicationId();
        new Thread("Parse.initialize Disk Check & Starting Command Cache") {
            public void run() {
                Parse.getEventuallyQueue();
            }
        }.start();
        ParseFieldOperations.registerDefaultDecoders();
        if (allParsePushIntentReceiversInternal()) {
            GcmRegistrar.getInstance().registerAsync().continueWithTask(new Continuation<Void, Task<Void>>() {
                public Task<Void> then(Task<Void> task) throws Exception {
                    return ParseUser.getCurrentUserAsync().makeVoid();
                }
            }).continueWith(new Continuation<Void, Void>() {
                public Void then(Task<Void> task) throws Exception {
                    ParseConfig.getCurrentConfig();
                    return null;
                }
            }, Task.BACKGROUND_EXECUTOR);
            if (ManifestInfo.getPushType() == PushType.PPNS) {
                PushService.startServiceIfRequired(applicationContext);
            }
            dispatchOnParseInitialized();
            synchronized (MUTEX_CALLBACKS) {
                callbacks = null;
            }
            return;
        }
        throw new SecurityException("To prevent external tampering to your app's notifications, all receivers registered to handle the following actions must have their exported attributes set to false: com.parse.push.intent.RECEIVE, com.parse.push.intent.OPEN, com.parse.push.intent.DELETE");
    }

    static void destroy() {
        synchronized (MUTEX) {
            ParseEventuallyQueue queue = eventuallyQueue;
            eventuallyQueue = null;
        }
        if (queue != null) {
            queue.onDestroy();
        }
        ParseCorePlugins.getInstance().reset();
        ParsePlugins.reset();
    }

    static boolean isInitialized() {
        return ParsePlugins.get() != null;
    }

    static Context getApplicationContext() {
        checkContext();
        return Android.get().applicationContext();
    }

    private static boolean allParsePushIntentReceiversInternal() {
        for (ResolveInfo resolveInfo : ManifestInfo.getIntentReceivers(ParsePushBroadcastReceiver.ACTION_PUSH_RECEIVE, ParsePushBroadcastReceiver.ACTION_PUSH_DELETE, ParsePushBroadcastReceiver.ACTION_PUSH_OPEN)) {
            if (resolveInfo.activityInfo.exported) {
                return false;
            }
        }
        return true;
    }

    @Deprecated
    static File getParseDir() {
        return ParsePlugins.get().getParseDir();
    }

    static File getParseCacheDir() {
        return ParsePlugins.get().getCacheDir();
    }

    static File getParseCacheDir(String subDir) {
        File dir;
        synchronized (MUTEX) {
            dir = new File(getParseCacheDir(), subDir);
            if (!dir.exists()) {
                dir.mkdirs();
            }
        }
        return dir;
    }

    static File getParseFilesDir() {
        return ParsePlugins.get().getFilesDir();
    }

    static File getParseFilesDir(String subDir) {
        File dir;
        synchronized (MUTEX) {
            dir = new File(getParseFilesDir(), subDir);
            if (!dir.exists()) {
                dir.mkdirs();
            }
        }
        return dir;
    }

    static void checkCacheApplicationId() {
        synchronized (MUTEX) {
            String applicationId = ParsePlugins.get().applicationId();
            if (applicationId != null) {
                File dir = getParseCacheDir();
                File applicationIdFile = new File(dir, "applicationId");
                if (applicationIdFile.exists()) {
                    boolean matches = false;
                    try {
                        RandomAccessFile f = new RandomAccessFile(applicationIdFile, "r");
                        byte[] bytes = new byte[((int) f.length())];
                        f.readFully(bytes);
                        f.close();
                        matches = new String(bytes, DownloadManager.UTF8_CHARSET).equals(applicationId);
                    } catch (FileNotFoundException e) {
                    } catch (IOException e2) {
                    }
                    if (!matches) {
                        try {
                            ParseFileUtils.deleteDirectory(dir);
                        } catch (IOException e3) {
                        }
                    }
                }
                try {
                    FileOutputStream out = new FileOutputStream(new File(dir, "applicationId"));
                    out.write(applicationId.getBytes(DownloadManager.UTF8_CHARSET));
                    out.close();
                } catch (FileNotFoundException e4) {
                } catch (UnsupportedEncodingException e5) {
                } catch (IOException e6) {
                }
            }
        }
    }

    static ParseEventuallyQueue getEventuallyQueue() {
        ParseEventuallyQueue parsePinningEventuallyQueue;
        Context context = Android.get().applicationContext();
        synchronized (MUTEX) {
            boolean isLocalDatastoreEnabled = isLocalDatastoreEnabled();
            if (eventuallyQueue == null || ((isLocalDatastoreEnabled && (eventuallyQueue instanceof ParseCommandCache)) || (!isLocalDatastoreEnabled && (eventuallyQueue instanceof ParsePinningEventuallyQueue)))) {
                checkContext();
                if (isLocalDatastoreEnabled) {
                    parsePinningEventuallyQueue = new ParsePinningEventuallyQueue(context);
                } else {
                    parsePinningEventuallyQueue = new ParseCommandCache(context);
                }
                eventuallyQueue = parsePinningEventuallyQueue;
                if (isLocalDatastoreEnabled && ParseCommandCache.getPendingCount() > 0) {
                    ParseCommandCache parseCommandCache = new ParseCommandCache(context);
                }
            }
            parsePinningEventuallyQueue = eventuallyQueue;
        }
        return parsePinningEventuallyQueue;
    }

    static void checkInit() {
        if (ParsePlugins.get() == null) {
            throw new RuntimeException("You must call Parse.initialize(Context) before using the Parse library.");
        } else if (ParsePlugins.get().applicationId() == null) {
            throw new RuntimeException("applicationId is null. You must call Parse.initialize(Context) before using the Parse library.");
        } else if (ParsePlugins.get().clientKey() == null) {
            throw new RuntimeException("clientKey is null. You must call Parse.initialize(Context) before using the Parse library.");
        }
    }

    static void checkContext() {
        if (Android.get().applicationContext() == null) {
            throw new RuntimeException("applicationContext is null. You must call Parse.initialize(Context) before using the Parse library.");
        }
    }

    static boolean hasPermission(String permission) {
        return getApplicationContext().checkCallingOrSelfPermission(permission) == 0;
    }

    static void requirePermission(String permission) {
        if (!hasPermission(permission)) {
            throw new IllegalStateException("To use this functionality, add this to your AndroidManifest.xml:\n<uses-permission android:name=\"" + permission + "\" />");
        }
    }

    static void registerParseCallbacks(ParseCallbacks listener) {
        if (isInitialized()) {
            throw new IllegalStateException("You must register callbacks before Parse.initialize(Context)");
        }
        synchronized (MUTEX_CALLBACKS) {
            if (callbacks == null) {
                return;
            }
            callbacks.add(listener);
        }
    }

    static void unregisterParseCallbacks(ParseCallbacks listener) {
        synchronized (MUTEX_CALLBACKS) {
            if (callbacks == null) {
                return;
            }
            callbacks.remove(listener);
        }
    }

    private static void dispatchOnParseInitialized() {
        ParseCallbacks[] callbacks = collectParseCallbacks();
        if (callbacks != null) {
            for (ParseCallbacks callback : callbacks) {
                callback.onParseInitialized();
            }
        }
    }

    private static ParseCallbacks[] collectParseCallbacks() {
        ParseCallbacks[] callbacks;
        synchronized (MUTEX_CALLBACKS) {
            if (callbacks == null) {
                callbacks = null;
            } else {
                callbacks = new ParseCallbacks[callbacks.size()];
                if (callbacks.size() > 0) {
                    callbacks = (ParseCallbacks[]) callbacks.toArray(callbacks);
                }
            }
        }
        return callbacks;
    }

    public static void setLogLevel(int logLevel) {
        PLog.setLogLevel(logLevel);
    }

    public static int getLogLevel() {
        return PLog.getLogLevel();
    }

    private Parse() {
        throw new AssertionError();
    }

    private static void initializeParseHttpClientsWithParseNetworkInterceptors() {
        if (interceptors != null) {
            List<ParseHttpClient> clients = new ArrayList();
            clients.add(ParsePlugins.get().restClient());
            clients.add(ParseCorePlugins.getInstance().getFileController().awsClient());
            for (ParseHttpClient parseHttpClient : clients) {
                for (ParseNetworkInterceptor interceptor : interceptors) {
                    parseHttpClient.addExternalInterceptor(interceptor);
                }
            }
            interceptors = null;
        }
    }

    static void addParseNetworkInterceptor(ParseNetworkInterceptor interceptor) {
        if (isInitialized()) {
            throw new IllegalStateException("`Parse#addParseNetworkInterceptor(ParseNetworkInterceptor)` must be invoked before `Parse#initialize(Context)`");
        }
        if (interceptors == null) {
            interceptors = new ArrayList();
        }
        interceptors.add(interceptor);
    }

    static void removeParseNetworkInterceptor(ParseNetworkInterceptor interceptor) {
        if (isInitialized()) {
            throw new IllegalStateException("`Parse#addParseNetworkInterceptor(ParseNetworkInterceptor)` must be invoked before `Parse#initialize(Context)`");
        } else if (interceptors != null) {
            interceptors.remove(interceptor);
        }
    }

    static String externalVersionName() {
        return "a1.10.1";
    }
}
