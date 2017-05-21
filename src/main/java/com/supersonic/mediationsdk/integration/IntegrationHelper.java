package com.supersonic.mediationsdk.integration;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.text.TextUtils;
import android.util.Log;
import android.util.Pair;
import com.supersonic.mediationsdk.sdk.SupersonicFactory;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class IntegrationHelper {
    private static final String TAG = "IntegrationHelper";

    public static void validateIntegration(Activity activity) {
        String supersonic = "Supersonic";
        String adcolony = "AdColony";
        String applovin = "AppLovin";
        String chartboost = "Chartboost";
        String flurry = "Flurry";
        String hyprmx = "HyprMX";
        String nativex = "NativeX";
        String unityads = "UnityAds";
        String vungle = "Vungle";
        String inmobi = "InMobi";
        List<String> generalPermissions = Arrays.asList(new String[]{"android.permission.WRITE_EXTERNAL_STORAGE", "android.permission.INTERNET", "android.permission.ACCESS_NETWORK_STATE"});
        List<String> adColonyPermissions = Arrays.asList(new String[]{"android.permission.VIBRATE"});
        List<String> supersonicActivities = Arrays.asList(new String[]{"com.supersonicads.sdk.controller.ControllerActivity", "com.supersonicads.sdk.controller.InterstitialActivity", "com.supersonicads.sdk.controller.OpenUrlActivity"});
        List<String> adColonyActivities = Arrays.asList(new String[]{"com.jirbo.adcolony.AdColonyOverlay", "com.jirbo.adcolony.AdColonyFullscreen", "com.jirbo.adcolony.AdColonyBrowser"});
        List<String> appLovinActivities = Arrays.asList(new String[]{"com.applovin.adview.AppLovinInterstitialActivity", "com.applovin.adview.AppLovinConfirmationActivity"});
        List<String> chartboostActivities = Arrays.asList(new String[]{"com.chartboost.sdk.CBImpressionActivity"});
        List<String> flurryActivities = Arrays.asList(new String[]{"com.flurry.android.FlurryFullscreenTakeoverActivity"});
        List<String> hyprMXActivities = Arrays.asList(new String[]{"com.supersonic.adapters.hyprmx.MediationHMXActivity"});
        List<String> nativeXActivities = Arrays.asList(new String[]{"com.nativex.monetization.activities.InterstitialActivity", "com.nativex.videoplayer.VideoActivity"});
        List<String> vungleActivities = Arrays.asList(new String[]{"com.vungle.publisher.FullScreenAdActivity"});
        List<String> inMobiActivities = Arrays.asList(new String[]{"com.inmobi.rendering.InMobiAdActivity"});
        List<String> inMobiBroadcastReceivers = Arrays.asList(new String[]{"com.inmobi.commons.core.utilities.uid.ImIdShareBroadCastReceiver"});
        ArrayList<Pair<String, String>> nativeXExternalLibraries = new ArrayList<Pair<String, String>>() {
            {
                add(new Pair("com.google.gson.Gson", "gson-2.3.1.jar"));
            }
        };
        ArrayList<Pair<String, String>> vungleExternalLibraries = new ArrayList<Pair<String, String>>() {
            {
                add(new Pair("javax.inject.Inject", "javax.inject-1.jar"));
                add(new Pair("com.nineoldandroids.animation.Animator", "nineoldandroids-2.4.0.jar"));
                add(new Pair("dagger.Module", "dagger-1.2.2.jar"));
            }
        };
        String hyprMXSdk = "com.hyprmx.android.activities.HyprMXActivity";
        String unityAdsSdk = "com.unity3d.ads.android.UnityAds";
        final AdapterObject supersonicAdapter = new AdapterObject("Supersonic", supersonicActivities, false);
        supersonicAdapter.setPermissions(generalPermissions);
        final AdapterObject adColonyAdapter = new AdapterObject("AdColony", adColonyActivities, true);
        adColonyAdapter.setPermissions(adColonyPermissions);
        final AdapterObject appLovinAdapter = new AdapterObject("AppLovin", appLovinActivities, true);
        final AdapterObject chartboostAdapter = new AdapterObject("Chartboost", chartboostActivities, true);
        final AdapterObject flurryAdapter = new AdapterObject("Flurry", flurryActivities, true);
        final AdapterObject hyprMXAdapter = new AdapterObject("HyprMX", hyprMXActivities, true);
        hyprMXAdapter.setSdkName("com.hyprmx.android.activities.HyprMXActivity");
        final AdapterObject nativeXAdapter = new AdapterObject("NativeX", nativeXActivities, true);
        nativeXAdapter.setExternalLibraries(nativeXExternalLibraries);
        final AdapterObject unityAdsAdapter = new AdapterObject("UnityAds", null, true);
        unityAdsAdapter.setSdkName("com.unity3d.ads.android.UnityAds");
        final AdapterObject vungleAdapter = new AdapterObject("Vungle", vungleActivities, true);
        vungleAdapter.setExternalLibraries(vungleExternalLibraries);
        final AdapterObject inMobiAdapter = new AdapterObject("InMobi", inMobiActivities, true);
        inMobiAdapter.setBroadcastReceivers(inMobiBroadcastReceivers);
        ArrayList<AdapterObject> adapters = new ArrayList<AdapterObject>() {
        };
        Log.i(TAG, "Verifying Integration:");
        Iterator i$ = adapters.iterator();
        while (i$.hasNext()) {
            AdapterObject adapterObject = (AdapterObject) i$.next();
            boolean verified = true;
            Log.w(TAG, "--------------- " + adapterObject.getName() + " --------------");
            if (adapterObject.isAdapter() && !validateAdapter(adapterObject.getAdapterName())) {
                verified = false;
            }
            if (verified) {
                if (!(adapterObject.getSdkName() == null || validateSdk(adapterObject.getSdkName()))) {
                    verified = false;
                }
                if (!(adapterObject.getPermissions() == null || validatePermissions(activity, adapterObject.getPermissions()))) {
                    verified = false;
                }
                if (!(adapterObject.getActivities() == null || validateActivities(activity, adapterObject.getActivities()))) {
                    verified = false;
                }
                if (!(adapterObject.getExternalLibraries() == null || validateExternalLibraries(adapterObject.getExternalLibraries()))) {
                    verified = false;
                }
                if (!(adapterObject.getBroadcastReceivers() == null || validateBroadcastReceivers(activity, adapterObject.getBroadcastReceivers()))) {
                    verified = false;
                }
            }
            if (verified) {
                Log.w(TAG, ">>>> " + adapterObject.getName() + " - VERIFIED");
            } else {
                Log.e(TAG, ">>>> " + adapterObject.getName() + " - NOT VERIFIED");
            }
        }
        validateGooglePlayServices(activity);
    }

    private static void validateGooglePlayServices(final Activity activity) {
        String mGooglePlayServicesMetaData = "com.google.android.gms.version";
        String mGooglePlayServices = "Google Play Services";
        new Thread() {
            public void run() {
                try {
                    Log.w(IntegrationHelper.TAG, "--------------- Google Play Services --------------");
                    if (activity.getPackageManager().getApplicationInfo(activity.getPackageName(), 128).metaData.containsKey("com.google.android.gms.version")) {
                        IntegrationHelper.validationMessage("Google Play Services", true);
                        String gaid = SupersonicFactory.getInstance().getAdvertiserId(activity);
                        if (!TextUtils.isEmpty(gaid)) {
                            Log.i(IntegrationHelper.TAG, "GAID is: " + gaid + " (use this for test devices)");
                            return;
                        }
                        return;
                    }
                    IntegrationHelper.validationMessage("Google Play Services", false);
                } catch (Exception e) {
                    IntegrationHelper.validationMessage("Google Play Services", false);
                }
            }
        }.start();
    }

    private static boolean validateAdapter(String adapter) {
        boolean result = false;
        try {
            Class localClass = Class.forName(adapter);
            result = true;
            validationMessage("Adapter", true);
            return true;
        } catch (ClassNotFoundException e) {
            validationMessage("Adapter", false);
            return result;
        }
    }

    private static boolean validateSdk(String sdkName) {
        boolean result = false;
        try {
            Class localClass = Class.forName(sdkName);
            result = true;
            validationMessage("SDK", true);
            return true;
        } catch (ClassNotFoundException e) {
            validationMessage("SDK", false);
            return result;
        }
    }

    private static boolean validateActivities(Activity activity, List<String> activities) {
        boolean result = true;
        PackageManager packageManager = activity.getPackageManager();
        Log.i(TAG, "*** Activities ***");
        for (String act : activities) {
            try {
                if (packageManager.queryIntentActivities(new Intent(activity, Class.forName(act)), 65536).size() > 0) {
                    validationMessage(act, true);
                } else {
                    result = false;
                    validationMessage(act, false);
                }
            } catch (ClassNotFoundException e) {
                result = false;
                validationMessage(act, false);
            }
        }
        return result;
    }

    private static boolean validatePermissions(Activity activity, List<String> permissions) {
        boolean result = true;
        PackageManager packageManager = activity.getPackageManager();
        Log.i(TAG, "*** Permissions ***");
        for (String permission : permissions) {
            if (packageManager.checkPermission(permission, activity.getPackageName()) == 0) {
                validationMessage(permission, true);
            } else {
                result = false;
                validationMessage(permission, false);
            }
        }
        return result;
    }

    private static boolean validateExternalLibraries(ArrayList<Pair<String, String>> externalLibraries) {
        boolean result = true;
        Log.i(TAG, "*** External Libraries ***");
        Iterator i$ = externalLibraries.iterator();
        while (i$.hasNext()) {
            Pair<String, String> externalLibrary = (Pair) i$.next();
            try {
                Class localClass = Class.forName((String) externalLibrary.first);
                validationMessage((String) externalLibrary.second, true);
            } catch (ClassNotFoundException e) {
                result = false;
                validationMessage((String) externalLibrary.second, false);
            }
        }
        return result;
    }

    private static boolean validateBroadcastReceivers(Activity activity, List<String> broadcastReceivers) {
        boolean result = true;
        PackageManager packageManager = activity.getPackageManager();
        Log.i(TAG, "*** Broadcast Receivers ***");
        for (String broadcastReceiver : broadcastReceivers) {
            try {
                if (packageManager.queryBroadcastReceivers(new Intent(activity, Class.forName(broadcastReceiver)), 65536).size() > 0) {
                    validationMessage(broadcastReceiver, true);
                } else {
                    result = false;
                    validationMessage(broadcastReceiver, false);
                }
            } catch (ClassNotFoundException e) {
                result = false;
                validationMessage(broadcastReceiver, false);
            }
        }
        return result;
    }

    private static void validationMessage(String paramToValidate, boolean successful) {
        if (successful) {
            Log.i(TAG, paramToValidate + " - VERIFIED");
        } else {
            Log.e(TAG, paramToValidate + " - MISSING");
        }
    }
}
