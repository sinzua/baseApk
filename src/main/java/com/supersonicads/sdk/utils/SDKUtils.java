package com.supersonicads.sdk.utils;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkInfo.State;
import android.os.IBinder;
import android.os.StatFs;
import android.provider.Settings.Secure;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.WindowManager;
import com.anjlab.android.iab.v3.Constants;
import com.nativex.common.StringConstants;
import com.supersonicads.sdk.precache.DownloadManager;
import com.supersonicads.sdk.utils.Constants.ParametersKeys;
import com.supersonicads.sdk.utils.Constants.RequestParameters;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.lang.reflect.Method;
import java.math.BigInteger;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.zip.GZIPInputStream;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class SDKUtils {
    private static final String TAG = SDKUtils.class.getSimpleName();
    private static String mAdvertisingId;
    private static String mControllerConfig;
    private static String mControllerUrl;
    private static int mDebugMode = 0;
    private static boolean mIsLimitedTrackingEnabled = true;

    public static void loadGoogleAdvertiserInfo(android.content.Context r11) {
        /* JADX: method processing error */
/*
Error: java.util.NoSuchElementException
	at java.util.HashMap$HashIterator.nextEntry(Unknown Source)
	at java.util.HashMap$KeyIterator.next(Unknown Source)
	at jadx.core.dex.visitors.blocksmaker.BlockFinallyExtract.applyRemove(BlockFinallyExtract.java:535)
	at jadx.core.dex.visitors.blocksmaker.BlockFinallyExtract.extractFinally(BlockFinallyExtract.java:175)
	at jadx.core.dex.visitors.blocksmaker.BlockFinallyExtract.processExceptionHandler(BlockFinallyExtract.java:79)
	at jadx.core.dex.visitors.blocksmaker.BlockFinallyExtract.visit(BlockFinallyExtract.java:51)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:31)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.ProcessClass.process(ProcessClass.java:37)
	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:59)
	at jadx.core.ProcessClass.process(ProcessClass.java:42)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:306)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
*/
        /*
        r1 = 0;
        r7 = "com.google.android.gms.ads.identifier.AdvertisingIdClient";	 Catch:{ ClassNotFoundException -> 0x00b5, NoSuchMethodException -> 0x0116, IllegalAccessException -> 0x0178, IllegalArgumentException -> 0x01da, InvocationTargetException -> 0x023c, all -> 0x029e }
        r5 = java.lang.Class.forName(r7);	 Catch:{ ClassNotFoundException -> 0x00b5, NoSuchMethodException -> 0x0116, IllegalAccessException -> 0x0178, IllegalArgumentException -> 0x01da, InvocationTargetException -> 0x023c, all -> 0x029e }
        r7 = "getAdvertisingIdInfo";	 Catch:{ ClassNotFoundException -> 0x00b5, NoSuchMethodException -> 0x0116, IllegalAccessException -> 0x0178, IllegalArgumentException -> 0x01da, InvocationTargetException -> 0x023c, all -> 0x029e }
        r8 = 1;	 Catch:{ ClassNotFoundException -> 0x00b5, NoSuchMethodException -> 0x0116, IllegalAccessException -> 0x0178, IllegalArgumentException -> 0x01da, InvocationTargetException -> 0x023c, all -> 0x029e }
        r8 = new java.lang.Class[r8];	 Catch:{ ClassNotFoundException -> 0x00b5, NoSuchMethodException -> 0x0116, IllegalAccessException -> 0x0178, IllegalArgumentException -> 0x01da, InvocationTargetException -> 0x023c, all -> 0x029e }
        r9 = 0;	 Catch:{ ClassNotFoundException -> 0x00b5, NoSuchMethodException -> 0x0116, IllegalAccessException -> 0x0178, IllegalArgumentException -> 0x01da, InvocationTargetException -> 0x023c, all -> 0x029e }
        r10 = android.content.Context.class;	 Catch:{ ClassNotFoundException -> 0x00b5, NoSuchMethodException -> 0x0116, IllegalAccessException -> 0x0178, IllegalArgumentException -> 0x01da, InvocationTargetException -> 0x023c, all -> 0x029e }
        r8[r9] = r10;	 Catch:{ ClassNotFoundException -> 0x00b5, NoSuchMethodException -> 0x0116, IllegalAccessException -> 0x0178, IllegalArgumentException -> 0x01da, InvocationTargetException -> 0x023c, all -> 0x029e }
        r2 = r5.getMethod(r7, r8);	 Catch:{ ClassNotFoundException -> 0x00b5, NoSuchMethodException -> 0x0116, IllegalAccessException -> 0x0178, IllegalArgumentException -> 0x01da, InvocationTargetException -> 0x023c, all -> 0x029e }
        r7 = 0;	 Catch:{ ClassNotFoundException -> 0x00b5, NoSuchMethodException -> 0x0116, IllegalAccessException -> 0x0178, IllegalArgumentException -> 0x01da, InvocationTargetException -> 0x023c, all -> 0x029e }
        r8 = 1;	 Catch:{ ClassNotFoundException -> 0x00b5, NoSuchMethodException -> 0x0116, IllegalAccessException -> 0x0178, IllegalArgumentException -> 0x01da, InvocationTargetException -> 0x023c, all -> 0x029e }
        r8 = new java.lang.Object[r8];	 Catch:{ ClassNotFoundException -> 0x00b5, NoSuchMethodException -> 0x0116, IllegalAccessException -> 0x0178, IllegalArgumentException -> 0x01da, InvocationTargetException -> 0x023c, all -> 0x029e }
        r9 = 0;	 Catch:{ ClassNotFoundException -> 0x00b5, NoSuchMethodException -> 0x0116, IllegalAccessException -> 0x0178, IllegalArgumentException -> 0x01da, InvocationTargetException -> 0x023c, all -> 0x029e }
        r8[r9] = r11;	 Catch:{ ClassNotFoundException -> 0x00b5, NoSuchMethodException -> 0x0116, IllegalAccessException -> 0x0178, IllegalArgumentException -> 0x01da, InvocationTargetException -> 0x023c, all -> 0x029e }
        r6 = r2.invoke(r7, r8);	 Catch:{ ClassNotFoundException -> 0x00b5, NoSuchMethodException -> 0x0116, IllegalAccessException -> 0x0178, IllegalArgumentException -> 0x01da, InvocationTargetException -> 0x023c, all -> 0x029e }
        r7 = r6.getClass();	 Catch:{ ClassNotFoundException -> 0x00b5, NoSuchMethodException -> 0x0116, IllegalAccessException -> 0x0178, IllegalArgumentException -> 0x01da, InvocationTargetException -> 0x023c, all -> 0x029e }
        r8 = "getId";	 Catch:{ ClassNotFoundException -> 0x00b5, NoSuchMethodException -> 0x0116, IllegalAccessException -> 0x0178, IllegalArgumentException -> 0x01da, InvocationTargetException -> 0x023c, all -> 0x029e }
        r9 = 0;	 Catch:{ ClassNotFoundException -> 0x00b5, NoSuchMethodException -> 0x0116, IllegalAccessException -> 0x0178, IllegalArgumentException -> 0x01da, InvocationTargetException -> 0x023c, all -> 0x029e }
        r9 = new java.lang.Class[r9];	 Catch:{ ClassNotFoundException -> 0x00b5, NoSuchMethodException -> 0x0116, IllegalAccessException -> 0x0178, IllegalArgumentException -> 0x01da, InvocationTargetException -> 0x023c, all -> 0x029e }
        r3 = r7.getMethod(r8, r9);	 Catch:{ ClassNotFoundException -> 0x00b5, NoSuchMethodException -> 0x0116, IllegalAccessException -> 0x0178, IllegalArgumentException -> 0x01da, InvocationTargetException -> 0x023c, all -> 0x029e }
        r7 = r6.getClass();	 Catch:{ ClassNotFoundException -> 0x00b5, NoSuchMethodException -> 0x0116, IllegalAccessException -> 0x0178, IllegalArgumentException -> 0x01da, InvocationTargetException -> 0x023c, all -> 0x029e }
        r8 = "isLimitAdTrackingEnabled";	 Catch:{ ClassNotFoundException -> 0x00b5, NoSuchMethodException -> 0x0116, IllegalAccessException -> 0x0178, IllegalArgumentException -> 0x01da, InvocationTargetException -> 0x023c, all -> 0x029e }
        r9 = 0;	 Catch:{ ClassNotFoundException -> 0x00b5, NoSuchMethodException -> 0x0116, IllegalAccessException -> 0x0178, IllegalArgumentException -> 0x01da, InvocationTargetException -> 0x023c, all -> 0x029e }
        r9 = new java.lang.Class[r9];	 Catch:{ ClassNotFoundException -> 0x00b5, NoSuchMethodException -> 0x0116, IllegalAccessException -> 0x0178, IllegalArgumentException -> 0x01da, InvocationTargetException -> 0x023c, all -> 0x029e }
        r4 = r7.getMethod(r8, r9);	 Catch:{ ClassNotFoundException -> 0x00b5, NoSuchMethodException -> 0x0116, IllegalAccessException -> 0x0178, IllegalArgumentException -> 0x01da, InvocationTargetException -> 0x023c, all -> 0x029e }
        r7 = 0;	 Catch:{ ClassNotFoundException -> 0x00b5, NoSuchMethodException -> 0x0116, IllegalAccessException -> 0x0178, IllegalArgumentException -> 0x01da, InvocationTargetException -> 0x023c, all -> 0x029e }
        r7 = new java.lang.Object[r7];	 Catch:{ ClassNotFoundException -> 0x00b5, NoSuchMethodException -> 0x0116, IllegalAccessException -> 0x0178, IllegalArgumentException -> 0x01da, InvocationTargetException -> 0x023c, all -> 0x029e }
        r7 = r3.invoke(r6, r7);	 Catch:{ ClassNotFoundException -> 0x00b5, NoSuchMethodException -> 0x0116, IllegalAccessException -> 0x0178, IllegalArgumentException -> 0x01da, InvocationTargetException -> 0x023c, all -> 0x029e }
        r7 = r7.toString();	 Catch:{ ClassNotFoundException -> 0x00b5, NoSuchMethodException -> 0x0116, IllegalAccessException -> 0x0178, IllegalArgumentException -> 0x01da, InvocationTargetException -> 0x023c, all -> 0x029e }
        mAdvertisingId = r7;	 Catch:{ ClassNotFoundException -> 0x00b5, NoSuchMethodException -> 0x0116, IllegalAccessException -> 0x0178, IllegalArgumentException -> 0x01da, InvocationTargetException -> 0x023c, all -> 0x029e }
        r7 = 0;	 Catch:{ ClassNotFoundException -> 0x00b5, NoSuchMethodException -> 0x0116, IllegalAccessException -> 0x0178, IllegalArgumentException -> 0x01da, InvocationTargetException -> 0x023c, all -> 0x029e }
        r7 = new java.lang.Object[r7];	 Catch:{ ClassNotFoundException -> 0x00b5, NoSuchMethodException -> 0x0116, IllegalAccessException -> 0x0178, IllegalArgumentException -> 0x01da, InvocationTargetException -> 0x023c, all -> 0x029e }
        r7 = r4.invoke(r6, r7);	 Catch:{ ClassNotFoundException -> 0x00b5, NoSuchMethodException -> 0x0116, IllegalAccessException -> 0x0178, IllegalArgumentException -> 0x01da, InvocationTargetException -> 0x023c, all -> 0x029e }
        r7 = (java.lang.Boolean) r7;	 Catch:{ ClassNotFoundException -> 0x00b5, NoSuchMethodException -> 0x0116, IllegalAccessException -> 0x0178, IllegalArgumentException -> 0x01da, InvocationTargetException -> 0x023c, all -> 0x029e }
        r7 = r7.booleanValue();	 Catch:{ ClassNotFoundException -> 0x00b5, NoSuchMethodException -> 0x0116, IllegalAccessException -> 0x0178, IllegalArgumentException -> 0x01da, InvocationTargetException -> 0x023c, all -> 0x029e }
        mIsLimitedTrackingEnabled = r7;	 Catch:{ ClassNotFoundException -> 0x00b5, NoSuchMethodException -> 0x0116, IllegalAccessException -> 0x0178, IllegalArgumentException -> 0x01da, InvocationTargetException -> 0x023c, all -> 0x029e }
        if (r1 == 0) goto L_0x00b4;
    L_0x0058:
        r7 = r1.getMessage();
        if (r7 == 0) goto L_0x0086;
    L_0x005e:
        r7 = TAG;
        r8 = new java.lang.StringBuilder;
        r8.<init>();
        r9 = r1.getClass();
        r9 = r9.getSimpleName();
        r8 = r8.append(r9);
        r9 = ": ";
        r8 = r8.append(r9);
        r9 = r1.getMessage();
        r8 = r8.append(r9);
        r8 = r8.toString();
        com.supersonicads.sdk.utils.Logger.i(r7, r8);
    L_0x0086:
        r7 = r1.getCause();
        if (r7 == 0) goto L_0x00b4;
    L_0x008c:
        r7 = TAG;
        r8 = new java.lang.StringBuilder;
        r8.<init>();
        r9 = r1.getClass();
        r9 = r9.getSimpleName();
        r8 = r8.append(r9);
        r9 = ": ";
        r8 = r8.append(r9);
        r9 = r1.getCause();
        r8 = r8.append(r9);
        r8 = r8.toString();
        com.supersonicads.sdk.utils.Logger.i(r7, r8);
    L_0x00b4:
        return;
    L_0x00b5:
        r0 = move-exception;
        r1 = r0;
        if (r1 == 0) goto L_0x00b4;
    L_0x00b9:
        r7 = r1.getMessage();
        if (r7 == 0) goto L_0x00e7;
    L_0x00bf:
        r7 = TAG;
        r8 = new java.lang.StringBuilder;
        r8.<init>();
        r9 = r1.getClass();
        r9 = r9.getSimpleName();
        r8 = r8.append(r9);
        r9 = ": ";
        r8 = r8.append(r9);
        r9 = r1.getMessage();
        r8 = r8.append(r9);
        r8 = r8.toString();
        com.supersonicads.sdk.utils.Logger.i(r7, r8);
    L_0x00e7:
        r7 = r1.getCause();
        if (r7 == 0) goto L_0x00b4;
    L_0x00ed:
        r7 = TAG;
        r8 = new java.lang.StringBuilder;
        r8.<init>();
        r9 = r1.getClass();
        r9 = r9.getSimpleName();
        r8 = r8.append(r9);
        r9 = ": ";
        r8 = r8.append(r9);
        r9 = r1.getCause();
        r8 = r8.append(r9);
        r8 = r8.toString();
        com.supersonicads.sdk.utils.Logger.i(r7, r8);
        goto L_0x00b4;
    L_0x0116:
        r0 = move-exception;
        r1 = r0;
        if (r1 == 0) goto L_0x00b4;
    L_0x011a:
        r7 = r1.getMessage();
        if (r7 == 0) goto L_0x0148;
    L_0x0120:
        r7 = TAG;
        r8 = new java.lang.StringBuilder;
        r8.<init>();
        r9 = r1.getClass();
        r9 = r9.getSimpleName();
        r8 = r8.append(r9);
        r9 = ": ";
        r8 = r8.append(r9);
        r9 = r1.getMessage();
        r8 = r8.append(r9);
        r8 = r8.toString();
        com.supersonicads.sdk.utils.Logger.i(r7, r8);
    L_0x0148:
        r7 = r1.getCause();
        if (r7 == 0) goto L_0x00b4;
    L_0x014e:
        r7 = TAG;
        r8 = new java.lang.StringBuilder;
        r8.<init>();
        r9 = r1.getClass();
        r9 = r9.getSimpleName();
        r8 = r8.append(r9);
        r9 = ": ";
        r8 = r8.append(r9);
        r9 = r1.getCause();
        r8 = r8.append(r9);
        r8 = r8.toString();
        com.supersonicads.sdk.utils.Logger.i(r7, r8);
        goto L_0x00b4;
    L_0x0178:
        r0 = move-exception;
        r1 = r0;
        if (r1 == 0) goto L_0x00b4;
    L_0x017c:
        r7 = r1.getMessage();
        if (r7 == 0) goto L_0x01aa;
    L_0x0182:
        r7 = TAG;
        r8 = new java.lang.StringBuilder;
        r8.<init>();
        r9 = r1.getClass();
        r9 = r9.getSimpleName();
        r8 = r8.append(r9);
        r9 = ": ";
        r8 = r8.append(r9);
        r9 = r1.getMessage();
        r8 = r8.append(r9);
        r8 = r8.toString();
        com.supersonicads.sdk.utils.Logger.i(r7, r8);
    L_0x01aa:
        r7 = r1.getCause();
        if (r7 == 0) goto L_0x00b4;
    L_0x01b0:
        r7 = TAG;
        r8 = new java.lang.StringBuilder;
        r8.<init>();
        r9 = r1.getClass();
        r9 = r9.getSimpleName();
        r8 = r8.append(r9);
        r9 = ": ";
        r8 = r8.append(r9);
        r9 = r1.getCause();
        r8 = r8.append(r9);
        r8 = r8.toString();
        com.supersonicads.sdk.utils.Logger.i(r7, r8);
        goto L_0x00b4;
    L_0x01da:
        r0 = move-exception;
        r1 = r0;
        if (r1 == 0) goto L_0x00b4;
    L_0x01de:
        r7 = r1.getMessage();
        if (r7 == 0) goto L_0x020c;
    L_0x01e4:
        r7 = TAG;
        r8 = new java.lang.StringBuilder;
        r8.<init>();
        r9 = r1.getClass();
        r9 = r9.getSimpleName();
        r8 = r8.append(r9);
        r9 = ": ";
        r8 = r8.append(r9);
        r9 = r1.getMessage();
        r8 = r8.append(r9);
        r8 = r8.toString();
        com.supersonicads.sdk.utils.Logger.i(r7, r8);
    L_0x020c:
        r7 = r1.getCause();
        if (r7 == 0) goto L_0x00b4;
    L_0x0212:
        r7 = TAG;
        r8 = new java.lang.StringBuilder;
        r8.<init>();
        r9 = r1.getClass();
        r9 = r9.getSimpleName();
        r8 = r8.append(r9);
        r9 = ": ";
        r8 = r8.append(r9);
        r9 = r1.getCause();
        r8 = r8.append(r9);
        r8 = r8.toString();
        com.supersonicads.sdk.utils.Logger.i(r7, r8);
        goto L_0x00b4;
    L_0x023c:
        r0 = move-exception;
        r1 = r0;
        if (r1 == 0) goto L_0x00b4;
    L_0x0240:
        r7 = r1.getMessage();
        if (r7 == 0) goto L_0x026e;
    L_0x0246:
        r7 = TAG;
        r8 = new java.lang.StringBuilder;
        r8.<init>();
        r9 = r1.getClass();
        r9 = r9.getSimpleName();
        r8 = r8.append(r9);
        r9 = ": ";
        r8 = r8.append(r9);
        r9 = r1.getMessage();
        r8 = r8.append(r9);
        r8 = r8.toString();
        com.supersonicads.sdk.utils.Logger.i(r7, r8);
    L_0x026e:
        r7 = r1.getCause();
        if (r7 == 0) goto L_0x00b4;
    L_0x0274:
        r7 = TAG;
        r8 = new java.lang.StringBuilder;
        r8.<init>();
        r9 = r1.getClass();
        r9 = r9.getSimpleName();
        r8 = r8.append(r9);
        r9 = ": ";
        r8 = r8.append(r9);
        r9 = r1.getCause();
        r8 = r8.append(r9);
        r8 = r8.toString();
        com.supersonicads.sdk.utils.Logger.i(r7, r8);
        goto L_0x00b4;
    L_0x029e:
        r7 = move-exception;
        if (r1 == 0) goto L_0x02fd;
    L_0x02a1:
        r8 = r1.getMessage();
        if (r8 == 0) goto L_0x02cf;
    L_0x02a7:
        r8 = TAG;
        r9 = new java.lang.StringBuilder;
        r9.<init>();
        r10 = r1.getClass();
        r10 = r10.getSimpleName();
        r9 = r9.append(r10);
        r10 = ": ";
        r9 = r9.append(r10);
        r10 = r1.getMessage();
        r9 = r9.append(r10);
        r9 = r9.toString();
        com.supersonicads.sdk.utils.Logger.i(r8, r9);
    L_0x02cf:
        r8 = r1.getCause();
        if (r8 == 0) goto L_0x02fd;
    L_0x02d5:
        r8 = TAG;
        r9 = new java.lang.StringBuilder;
        r9.<init>();
        r10 = r1.getClass();
        r10 = r10.getSimpleName();
        r9 = r9.append(r10);
        r10 = ": ";
        r9 = r9.append(r10);
        r10 = r1.getCause();
        r9 = r9.append(r10);
        r9 = r9.toString();
        com.supersonicads.sdk.utils.Logger.i(r8, r9);
    L_0x02fd:
        throw r7;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.supersonicads.sdk.utils.SDKUtils.loadGoogleAdvertiserInfo(android.content.Context):void");
    }

    public static String getFileName(String url) {
        String[] assetSplit = url.split(File.separator);
        String encodedlFileName = null;
        try {
            encodedlFileName = URLEncoder.encode(assetSplit[assetSplit.length - 1].split("\\?")[0], DownloadManager.UTF8_CHARSET);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return encodedlFileName;
    }

    public static long getAvailableSpaceInMB(Context context, String cacheRootDirectory) {
        StatFs stat = new StatFs(cacheRootDirectory);
        return (((long) stat.getAvailableBlocks()) * ((long) stat.getBlockSize())) / ParseFileUtils.ONE_MB;
    }

    public static int pxToDp(long px) {
        return (int) ((((float) px) / Resources.getSystem().getDisplayMetrics().density) + 0.5f);
    }

    public static int dpToPx(long dp) {
        return (int) ((((float) dp) * Resources.getSystem().getDisplayMetrics().density) + 0.5f);
    }

    public static int getDeviceWidth() {
        return Resources.getSystem().getDisplayMetrics().widthPixels;
    }

    public static int getDeviceHeight() {
        return Resources.getSystem().getDisplayMetrics().heightPixels;
    }

    public static int convertPxToDp(int pixels) {
        return (int) TypedValue.applyDimension(1, (float) pixels, Resources.getSystem().getDisplayMetrics());
    }

    public static int convertDpToPx(int dp) {
        return (int) TypedValue.applyDimension(0, (float) dp, Resources.getSystem().getDisplayMetrics());
    }

    public static int getApplicationRotation(Context context) {
        return ((WindowManager) context.getSystemService("window")).getDefaultDisplay().getRotation();
    }

    public static int getDeviceDefaultOrientation(Context context) {
        WindowManager windowManager = (WindowManager) context.getSystemService("window");
        Configuration config = context.getResources().getConfiguration();
        int rotation = windowManager.getDefaultDisplay().getRotation();
        if ((rotation == 0 || rotation == 2) && config.orientation == 2) {
            return 2;
        }
        if ((rotation == 1 || rotation == 3) && config.orientation == 1) {
            return 2;
        }
        return 1;
    }

    public static JSONObject getOrientation(Context context) {
        int orientation = context.getResources().getConfiguration().orientation;
        String strOrientation = ParametersKeys.ORIENTATION_PORTRAIT;
        if (orientation == 2) {
            strOrientation = ParametersKeys.ORIENTATION_LANDSCAPE;
        }
        JSONObject obj = new JSONObject();
        try {
            obj.put(ParametersKeys.ORIENTATION, strOrientation);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return obj;
    }

    public static JSONObject getOrientationNew(Context context) {
        int defaultOrientation = getDeviceDefaultOrientation(context);
        int rotation = getApplicationRotation(context);
        String strOrientation = ParametersKeys.ORIENTATION_PORTRAIT;
        boolean shouldChange = rotation == 1 || rotation == 3;
        if (shouldChange) {
            if (defaultOrientation == 1) {
                strOrientation = ParametersKeys.ORIENTATION_LANDSCAPE;
            }
        } else if (defaultOrientation == 2) {
            strOrientation = ParametersKeys.ORIENTATION_LANDSCAPE;
        }
        JSONObject obj = new JSONObject();
        try {
            obj.put(ParametersKeys.ORIENTATION, strOrientation);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return obj;
    }

    public static int getRotation(Context context) {
        return ((WindowManager) context.getSystemService("window")).getDefaultDisplay().getRotation();
    }

    public static String createErrorMessage(String action, String stage) {
        return String.format("%s Failure occurred during initiation at: %s", new Object[]{action, stage});
    }

    public static String getPackageName(Context context) {
        return context.getPackageName();
    }

    public static Long getCurrentTimeMillis() {
        return Long.valueOf(System.currentTimeMillis());
    }

    public static float getDeviceScale() {
        return Resources.getSystem().getDisplayMetrics().density;
    }

    public static boolean isApplicationVisible(Context context) {
        String packageName = context.getPackageName();
        for (RunningAppProcessInfo appProcess : ((ActivityManager) context.getSystemService("activity")).getRunningAppProcesses()) {
            if (appProcess.processName.equalsIgnoreCase(packageName) && appProcess.importance == 100) {
                return true;
            }
        }
        return false;
    }

    public static boolean isConnectingToInternet(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService("connectivity");
        if (connectivity != null) {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null) {
                for (NetworkInfo state : info) {
                    if (state.getState() == State.CONNECTED) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public static boolean isOnline(Context context) {
        NetworkInfo networkInfo = ((ConnectivityManager) context.getSystemService("connectivity")).getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }

    public static void showNoInternetDialog(Context context) {
        new Builder(context).setMessage("No Internet Connection").setPositiveButton(StringConstants.MESSAGE_DIALOG_BUTTON_TEXT, new OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }).show();
    }

    public static byte[] encrypt(String x) {
        MessageDigest digest = null;
        try {
            digest = MessageDigest.getInstance("SHA-1");
            digest.reset();
            digest.update(x.getBytes(DownloadManager.UTF8_CHARSET));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e2) {
            e2.printStackTrace();
        }
        if (digest != null) {
            return digest.digest();
        }
        return null;
    }

    public static String convertStreamToString(InputStream is, boolean isGzipEnabled, String dir, String fileName) throws IOException {
        Throwable th;
        InputStream cleanedIs = is;
        if (isGzipEnabled) {
            cleanedIs = new GZIPInputStream(is);
        }
        BufferedReader reader = null;
        Writer writer = new BufferedWriter(new FileWriter(new File(dir, fileName)));
        try {
            BufferedReader reader2 = new BufferedReader(new InputStreamReader(cleanedIs, DownloadManager.UTF8_CHARSET));
            try {
                StringBuilder sb = new StringBuilder();
                while (true) {
                    String line = reader2.readLine();
                    if (line == null) {
                        break;
                    }
                    sb.append(line);
                    sb.append("\n");
                }
                writer.write(sb.toString());
                String stringBuilder = sb.toString();
                if (reader2 != null) {
                    reader2.close();
                }
                cleanedIs.close();
                if (isGzipEnabled) {
                    is.close();
                }
                writer.close();
                return stringBuilder;
            } catch (Throwable th2) {
                th = th2;
                reader = reader2;
                if (reader != null) {
                    reader.close();
                }
                cleanedIs.close();
                if (isGzipEnabled) {
                    is.close();
                }
                writer.close();
                throw th;
            }
        } catch (Throwable th3) {
            th = th3;
            if (reader != null) {
                reader.close();
            }
            cleanedIs.close();
            if (isGzipEnabled) {
                is.close();
            }
            writer.close();
            throw th;
        }
    }

    public static String getConnectionType(Context context) {
        StringBuilder connectionType = new StringBuilder();
        NetworkInfo activeNetwork = ((ConnectivityManager) context.getSystemService("connectivity")).getActiveNetworkInfo();
        if (activeNetwork != null && activeNetwork.isConnected()) {
            String typeName = activeNetwork.getTypeName();
            String subTypeName = activeNetwork.getSubtypeName();
            int typeId = activeNetwork.getType();
            if (typeId == 0) {
                connectionType.append(RequestParameters.NETWORK_TYPE_3G);
            } else if (typeId == 1) {
                connectionType.append(RequestParameters.NETWORK_TYPE_WIFI);
            } else {
                connectionType.append(typeName);
            }
        }
        return connectionType.toString();
    }

    public static String encodeString(String value) {
        String encodedString = "";
        try {
            encodedString = URLEncoder.encode(value, DownloadManager.UTF8_CHARSET).replace("+", "%20");
        } catch (UnsupportedEncodingException e) {
        }
        return encodedString;
    }

    public static boolean isPermissionGranted(Context context, String permission) {
        return context.checkCallingOrSelfPermission(permission) == 0;
    }

    private static String getAndroidID(Context context) {
        return Secure.getString(context.getContentResolver(), "android_id");
    }

    public static String getMD5(String input) {
        try {
            String hashtext = new BigInteger(1, MessageDigest.getInstance("MD5").digest(input.getBytes())).toString(16);
            while (hashtext.length() < 32) {
                hashtext = "0" + hashtext;
            }
            return hashtext;
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public static String getAdvertiserId() {
        return mAdvertisingId;
    }

    public static boolean isLimitAdTrackingEnabled() {
        return mIsLimitedTrackingEnabled;
    }

    public static Object getIInAppBillingServiceClass(IBinder binder) {
        Object object = null;
        Exception exception = null;
        try {
            object = Class.forName("com.android.vending.billing.IInAppBillingService$Stub").getMethod("asInterface", new Class[]{IBinder.class}).invoke(null, new Object[]{binder});
            if (exception != null) {
                if (exception.getMessage() != null) {
                    Logger.i(TAG, exception.getClass().getSimpleName() + ": " + exception.getMessage());
                }
                if (exception.getCause() != null) {
                    Logger.i(TAG, exception.getClass().getSimpleName() + ": " + exception.getCause());
                }
            }
        } catch (Exception e) {
            exception = e;
            if (exception != null) {
                if (exception.getMessage() != null) {
                    Logger.i(TAG, exception.getClass().getSimpleName() + ": " + exception.getMessage());
                }
                if (exception.getCause() != null) {
                    Logger.i(TAG, exception.getClass().getSimpleName() + ": " + exception.getCause());
                }
            }
        } catch (Exception e2) {
            exception = e2;
            if (exception != null) {
                if (exception.getMessage() != null) {
                    Logger.i(TAG, exception.getClass().getSimpleName() + ": " + exception.getMessage());
                }
                if (exception.getCause() != null) {
                    Logger.i(TAG, exception.getClass().getSimpleName() + ": " + exception.getCause());
                }
            }
        } catch (Exception e22) {
            exception = e22;
            if (exception != null) {
                if (exception.getMessage() != null) {
                    Logger.i(TAG, exception.getClass().getSimpleName() + ": " + exception.getMessage());
                }
                if (exception.getCause() != null) {
                    Logger.i(TAG, exception.getClass().getSimpleName() + ": " + exception.getCause());
                }
            }
        } catch (Exception e222) {
            exception = e222;
            if (exception != null) {
                if (exception.getMessage() != null) {
                    Logger.i(TAG, exception.getClass().getSimpleName() + ": " + exception.getMessage());
                }
                if (exception.getCause() != null) {
                    Logger.i(TAG, exception.getClass().getSimpleName() + ": " + exception.getCause());
                }
            }
        } catch (Exception e2222) {
            exception = e2222;
            if (exception != null) {
                if (exception.getMessage() != null) {
                    Logger.i(TAG, exception.getClass().getSimpleName() + ": " + exception.getMessage());
                }
                if (exception.getCause() != null) {
                    Logger.i(TAG, exception.getClass().getSimpleName() + ": " + exception.getCause());
                }
            }
        } catch (Throwable th) {
            if (exception != null) {
                if (exception.getMessage() != null) {
                    Logger.i(TAG, exception.getClass().getSimpleName() + ": " + exception.getMessage());
                }
                if (exception.getCause() != null) {
                    Logger.i(TAG, exception.getClass().getSimpleName() + ": " + exception.getCause());
                }
            }
        }
        return object;
    }

    public static String queryingPurchasedItems(Object object, String pckName) {
        JSONArray purchases = new JSONArray();
        Exception exception = null;
        try {
            Object mBundleClass = object.getClass().getMethod("getPurchases", new Class[]{Integer.TYPE, String.class, String.class, String.class}).invoke(object, new Object[]{Integer.valueOf(3), pckName, Constants.PRODUCT_TYPE_MANAGED, null});
            Method mGetIntmethod = mBundleClass.getClass().getMethod("getInt", new Class[]{String.class});
            Method mGetStringArrayListMethod = mBundleClass.getClass().getMethod("getStringArrayList", new Class[]{String.class});
            Method mGetStringMethod = mBundleClass.getClass().getMethod("getString", new Class[]{String.class});
            if (((Integer) mGetIntmethod.invoke(mBundleClass, new Object[]{Constants.RESPONSE_CODE})).intValue() == 0) {
                ArrayList<String> ownedSkus = (ArrayList) mGetStringArrayListMethod.invoke(mBundleClass, new Object[]{"INAPP_PURCHASE_ITEM_LIST"});
                ArrayList<String> purchaseDataList = (ArrayList) mGetStringArrayListMethod.invoke(mBundleClass, new Object[]{Constants.INAPP_PURCHASE_DATA_LIST});
                ArrayList<String> signatureList = (ArrayList) mGetStringArrayListMethod.invoke(mBundleClass, new Object[]{Constants.INAPP_DATA_SIGNATURE_LIST});
                String continuationToken = (String) mGetStringMethod.invoke(mBundleClass, new Object[]{"INAPP_CONTINUATION_TOKEN"});
                for (int i = 0; i < purchaseDataList.size(); i++) {
                    String purchaseData = (String) purchaseDataList.get(i);
                    String signature = (String) signatureList.get(i);
                    String sku = (String) ownedSkus.get(i);
                    Logger.i(TAG, purchaseData);
                    Logger.i(TAG, signature);
                    Logger.i(TAG, sku);
                    JSONObject item = new JSONObject();
                    try {
                        item.put("purchaseData", purchaseData);
                        item.put("signature", purchaseData);
                        item.put("sku", purchaseData);
                        purchases.put(item);
                    } catch (JSONException e) {
                    }
                }
            }
            if (exception != null) {
                if (exception.getMessage() != null) {
                    Logger.i(TAG, exception.getClass().getSimpleName() + ": " + exception.getMessage());
                }
                if (exception.getCause() != null) {
                    Logger.i(TAG, exception.getClass().getSimpleName() + ": " + exception.getCause());
                }
            }
        } catch (Exception e2) {
            exception = e2;
            if (exception != null) {
                if (exception.getMessage() != null) {
                    Logger.i(TAG, exception.getClass().getSimpleName() + ": " + exception.getMessage());
                }
                if (exception.getCause() != null) {
                    Logger.i(TAG, exception.getClass().getSimpleName() + ": " + exception.getCause());
                }
            }
        } catch (Exception e22) {
            exception = e22;
            if (exception != null) {
                if (exception.getMessage() != null) {
                    Logger.i(TAG, exception.getClass().getSimpleName() + ": " + exception.getMessage());
                }
                if (exception.getCause() != null) {
                    Logger.i(TAG, exception.getClass().getSimpleName() + ": " + exception.getCause());
                }
            }
        } catch (Exception e222) {
            exception = e222;
            if (exception != null) {
                if (exception.getMessage() != null) {
                    Logger.i(TAG, exception.getClass().getSimpleName() + ": " + exception.getMessage());
                }
                if (exception.getCause() != null) {
                    Logger.i(TAG, exception.getClass().getSimpleName() + ": " + exception.getCause());
                }
            }
        } catch (Exception e2222) {
            exception = e2222;
            if (exception != null) {
                if (exception.getMessage() != null) {
                    Logger.i(TAG, exception.getClass().getSimpleName() + ": " + exception.getMessage());
                }
                if (exception.getCause() != null) {
                    Logger.i(TAG, exception.getClass().getSimpleName() + ": " + exception.getCause());
                }
            }
        } catch (Throwable th) {
            if (exception != null) {
                if (exception.getMessage() != null) {
                    Logger.i(TAG, exception.getClass().getSimpleName() + ": " + exception.getMessage());
                }
                if (exception.getCause() != null) {
                    Logger.i(TAG, exception.getClass().getSimpleName() + ": " + exception.getCause());
                }
            }
        }
        return purchases.toString();
    }

    public static String getSDKVersion() {
        return Constants.SDK_VERSION;
    }

    public static void setControllerUrl(String url) {
        mControllerUrl = url;
    }

    public static String getControllerUrl() {
        if (TextUtils.isEmpty(mControllerUrl)) {
            return "";
        }
        return mControllerUrl;
    }

    public static void setControllerConfig(String config) {
        mControllerConfig = config;
    }

    public static String getControllerConfig() {
        return mControllerConfig;
    }

    public static void setDebugMode(int debugMode) {
        mDebugMode = debugMode;
    }

    public static int getDebugMode() {
        return mDebugMode;
    }
}
