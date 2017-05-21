package com.parse;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.os.Build;
import android.os.Build.VERSION;
import android.os.Environment;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.os.Process;
import android.os.StatFs;
import android.os.SystemClock;
import android.provider.Settings.Secure;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.text.format.Time;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;
import com.parse.ProcFileReader.OpenFDLimits;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.RandomAccessFile;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.Thread.UncaughtExceptionHandler;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.GZIPOutputStream;

class ErrorReporter implements UncaughtExceptionHandler {
    public static final String ACRA_DIRNAME = "cr/reports";
    private static final CrashReportType[] ALL_REPORT_TYPES = new CrashReportType[]{CrashReportType.ACRA_CRASH_REPORT, CrashReportType.NATIVE_CRASH_REPORT, CrashReportType.ANR_REPORT};
    private static final String ANDROID_RUNTIME_ART = "ART";
    private static final String ANDROID_RUNTIME_DALVIK = "DALVIK";
    private static final String ANDROID_RUNTIME_UNKNOWN = "UNKNOWN";
    public static final String CRASH_ATTACHMENT_DUMMY_STACKTRACE = "crash attachment";
    public static final long DEFAULT_MAX_REPORT_SIZE = 51200;
    private static int DEFAULT_TRACE_COUNT_LIMIT = 5;
    public static final String DUMPFILE_EXTENSION = ".dmp";
    public static final String DUMP_DIR = "cr/minidumps";
    private static final String IS_PROCESSING_ANOTHER_EXCEPTION = "IS_PROCESSING_ANOTHER_EXCEPTION";
    private static final String JAVA_BOOT_CLASS_PATH = "java.boot.class.path";
    private static final String KNOWN_ART_JAR = "/system/framework/core-libart.jar";
    private static final String KNOWN_DALVIK_JAR = "/system/framework/core.jar";
    public static final long MAX_REPORT_AGE = 86400000;
    public static final int MAX_SEND_REPORTS = 5;
    private static int MAX_TRACE_COUNT_LIMIT = 20;
    private static final long MIN_TEMP_REPORT_AGE = 600000;
    public static final long NATIVE_MAX_REPORT_SIZE = 512000;
    public static final long PREALLOCATED_FILESIZE = 51200;
    public static final String PREALLOCATED_REPORTFILE = "reportfile.prealloc";
    public static final String REPORTFILE_EXTENSION = ".stacktrace";
    public static final String SIGQUIT_DIR = "traces";
    public static final long SIGQUIT_MAX_REPORT_SIZE = 122880;
    public static final String TEMP_REPORTFILE_EXTENSION = ".temp_stacktrace";
    private static final Pattern VERSION_CODE_REGEX = Pattern.compile("^\\d+-[a-zA-Z0-9_\\-]+-(\\d+)\\.(temp_stacktrace|stacktrace)$");
    private static ErrorReporter mInstanceSingleton = null;
    private static final String mInternalException = "ACRA_INTERNAL=java.lang.Exception: An exception occurred while trying to collect data about an ACRA internal error\n\tat com.parse.acra.ErrorReporter.handleException(ErrorReporter.java:810)\n\tat com.parse.acra.ErrorReporter.handleException(ErrorReporter.java:866)\n\tat com.parse.acra.ErrorReporter.uncaughtException(ErrorReporter.java:666)\n\tat java.lang.ThreadGroup.uncaughtException(ThreadGroup.java:693)\n\tat java.lang.ThreadGroup.uncaughtException(ThreadGroup.java:690)\n";
    private static AtomicBoolean mProcessingCrash = new AtomicBoolean(false);
    private final SimpleTraceLogger activityLogger = new SimpleTraceLogger(MAX_TRACE_COUNT_LIMIT);
    private final Time mAppStartDate = new Time();
    private String mAppVersionCode;
    private String mAppVersionName;
    private final Map<ReportField, String> mConstantFields = new HashMap();
    private Context mContext;
    private boolean mCurrentlyProcessingOOM = false;
    private final Map<ReportField, String> mDeviceSpecificFields = new HashMap();
    private UncaughtExceptionHandler mDfltExceptionHandler;
    private FileProvider mFileProvider;
    private boolean mHasNativeCrashDumpOnInit = false;
    Map<String, String> mInstanceCustomParameters = new ConcurrentHashMap();
    Map<String, CustomReportDataSupplier> mInstanceLazyCustomParameters = new ConcurrentHashMap();
    private boolean mIsInternalBuild;
    private LogBridge mLogBridge;
    private long mMaxReportSize = 51200;
    private PackageManagerWrapper mPackageManager;
    private ArrayList<ReportSender> mReportSenders = new ArrayList();
    private final Object mShouldContinueProcessingExceptionLock = new Object();
    private volatile String mUserId;
    private File preallocFile = null;
    private String processNameByAms;
    private boolean processNameByAmsReady;
    private volatile boolean sendInMemoryReport = false;
    private boolean usePreallocatedFile = false;

    private class CrashAttachmentException extends Throwable {
        private CrashAttachmentException() {
        }
    }

    public enum CrashReportType {
        ACRA_CRASH_REPORT(ErrorReporter.ACRA_DIRNAME, 51200, null, ErrorReporter.REPORTFILE_EXTENSION, ErrorReporter.TEMP_REPORTFILE_EXTENSION),
        NATIVE_CRASH_REPORT(ErrorReporter.DUMP_DIR, ErrorReporter.NATIVE_MAX_REPORT_SIZE, ReportField.MINIDUMP, ErrorReporter.DUMPFILE_EXTENSION),
        ANR_REPORT(ErrorReporter.SIGQUIT_DIR, ErrorReporter.SIGQUIT_MAX_REPORT_SIZE, ReportField.SIGQUIT, ErrorReporter.REPORTFILE_EXTENSION, ErrorReporter.TEMP_REPORTFILE_EXTENSION);
        
        private final ReportField attachmentField;
        private final long defaultMaxSize;
        private final String directory;
        private final String[] fileExtensions;

        private CrashReportType(String directory, long maxSize, ReportField attachmentField, String... fileExtensions) {
            this.directory = directory;
            this.defaultMaxSize = maxSize;
            this.attachmentField = attachmentField;
            this.fileExtensions = fileExtensions;
        }
    }

    final class ReportsSenderWorker extends Thread {
        private Throwable exception;
        private CrashReportData mInMemoryReportToSend;
        private final CrashReportType[] mReportTypesToSend;

        public void run() {
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
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:14)
	at jadx.core.ProcessClass.process(ProcessClass.java:37)
	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:59)
	at jadx.core.ProcessClass.process(ProcessClass.java:42)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:306)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
*/
            /*
            r5 = this;
            r1 = 0;
            r1 = r5.acquireWakeLock();	 Catch:{ Throwable -> 0x0030, all -> 0x003f }
            r2 = r5.mInMemoryReportToSend;	 Catch:{ Throwable -> 0x0030, all -> 0x003f }
            if (r2 == 0) goto L_0x0022;	 Catch:{ Throwable -> 0x0030, all -> 0x003f }
        L_0x0009:
            r2 = com.parse.ErrorReporter.this;	 Catch:{ Throwable -> 0x0030, all -> 0x003f }
            r3 = com.parse.ErrorReporter.this;	 Catch:{ Throwable -> 0x0030, all -> 0x003f }
            r3 = r3.mContext;	 Catch:{ Throwable -> 0x0030, all -> 0x003f }
            r4 = r5.mInMemoryReportToSend;	 Catch:{ Throwable -> 0x0030, all -> 0x003f }
            r2.sendInMemoryReport(r3, r4);	 Catch:{ Throwable -> 0x0030, all -> 0x003f }
        L_0x0016:
            if (r1 == 0) goto L_0x0021;
        L_0x0018:
            r2 = r1.isHeld();
            if (r2 == 0) goto L_0x0021;
        L_0x001e:
            r1.release();
        L_0x0021:
            return;
        L_0x0022:
            r2 = com.parse.ErrorReporter.this;	 Catch:{ Throwable -> 0x0030, all -> 0x003f }
            r3 = com.parse.ErrorReporter.this;	 Catch:{ Throwable -> 0x0030, all -> 0x003f }
            r3 = r3.mContext;	 Catch:{ Throwable -> 0x0030, all -> 0x003f }
            r4 = r5.mReportTypesToSend;	 Catch:{ Throwable -> 0x0030, all -> 0x003f }
            r2.checkAndSendReports(r3, r4);	 Catch:{ Throwable -> 0x0030, all -> 0x003f }
            goto L_0x0016;
        L_0x0030:
            r0 = move-exception;
            r5.exception = r0;	 Catch:{ Throwable -> 0x0030, all -> 0x003f }
            if (r1 == 0) goto L_0x0021;
        L_0x0035:
            r2 = r1.isHeld();
            if (r2 == 0) goto L_0x0021;
        L_0x003b:
            r1.release();
            goto L_0x0021;
        L_0x003f:
            r2 = move-exception;
            if (r1 == 0) goto L_0x004b;
        L_0x0042:
            r3 = r1.isHeld();
            if (r3 == 0) goto L_0x004b;
        L_0x0048:
            r1.release();
        L_0x004b:
            throw r2;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.parse.ErrorReporter.ReportsSenderWorker.run():void");
        }

        public ReportsSenderWorker(ErrorReporter errorReporter, CrashReportData inMemoryReportToSend) {
            this(new CrashReportType[0]);
            this.mInMemoryReportToSend = inMemoryReportToSend;
        }

        public ReportsSenderWorker(CrashReportType... reportTypesToSend) {
            this.exception = null;
            this.mReportTypesToSend = reportTypesToSend;
        }

        public Throwable getException() {
            return this.exception;
        }

        private WakeLock acquireWakeLock() {
            if (!new PackageManagerWrapper(ErrorReporter.this.mContext).hasPermission("android.permission.WAKE_LOCK")) {
                return null;
            }
            WakeLock wakeLock = ((PowerManager) ErrorReporter.this.mContext.getSystemService("power")).newWakeLock(1, "crash reporting wakelock");
            wakeLock.setReferenceCounted(false);
            wakeLock.acquire();
            return wakeLock;
        }
    }

    ErrorReporter() {
    }

    public LogBridge getLogBridge() {
        return this.mLogBridge;
    }

    public void setLogBridge(LogBridge bridge) {
        this.mLogBridge = bridge;
    }

    public String getUserId() {
        return this.mUserId;
    }

    public void setUserId(String userId) {
        this.mUserId = userId;
    }

    public String putCustomData(String key, String value) {
        if (value != null) {
            return (String) this.mInstanceCustomParameters.put(key, value);
        }
        return removeCustomData(key);
    }

    public String removeCustomData(String key) {
        return (String) this.mInstanceCustomParameters.remove(key);
    }

    public String getCustomData(String key) {
        return (String) this.mInstanceCustomParameters.get(key);
    }

    public void putLazyCustomData(String key, CustomReportDataSupplier valueSupplier) {
        this.mInstanceLazyCustomParameters.put(key, valueSupplier);
    }

    public String dumpCustomDataToString(Map<String, String> extras, Throwable throwable) {
        StringBuilder customInfo = new StringBuilder();
        dumpCustomDataMap(customInfo, this.mInstanceCustomParameters);
        if (extras != null) {
            dumpCustomDataMap(customInfo, extras);
        }
        dumpLazyCustomDataMap(customInfo, this.mInstanceLazyCustomParameters, throwable);
        return customInfo.toString();
    }

    private void dumpLazyCustomDataMap(StringBuilder sb, Map<String, CustomReportDataSupplier> params, Throwable throwable) {
        for (Entry<String, CustomReportDataSupplier> entry : params.entrySet()) {
            String key = (String) entry.getKey();
            try {
                String value = ((CustomReportDataSupplier) entry.getValue()).getCustomData(throwable);
                if (value != null) {
                    dumpCustomDataEntry(sb, key, value);
                }
            } catch (Throwable th) {
                Log.e(ACRA.LOG_TAG, "Caught throwable while getting custom report data", th);
            }
        }
    }

    private void dumpCustomDataMap(StringBuilder sb, Map<String, String> params) {
        for (Entry<String, String> entry : params.entrySet()) {
            dumpCustomDataEntry(sb, (String) entry.getKey(), (String) entry.getValue());
        }
    }

    private void dumpCustomDataEntry(StringBuilder sb, String key, String value) {
        if (key != null) {
            key = key.replace("\n", "\\n");
        } else {
            key = null;
        }
        if (value != null) {
            value = value.replace("\n", "\\n");
        } else {
            value = null;
        }
        sb.append(key).append(" = ").append(value).append("\n");
    }

    private String getProcessNameFromAmsOrNull() {
        if (this.processNameByAmsReady) {
            return this.processNameByAms;
        }
        this.processNameByAms = null;
        int pid = Process.myPid();
        ActivityManager am = (ActivityManager) this.mContext.getSystemService("activity");
        if (am == null) {
            return this.processNameByAms;
        }
        List<RunningAppProcessInfo> processes = am.getRunningAppProcesses();
        if (processes == null) {
            return this.processNameByAms;
        }
        for (RunningAppProcessInfo rai : processes) {
            if (rai.pid == pid) {
                this.processNameByAms = rai.processName;
                break;
            }
        }
        this.processNameByAmsReady = true;
        return this.processNameByAms;
    }

    private void resetProcessNameByAmsCache() {
        this.processNameByAms = null;
        this.processNameByAmsReady = false;
    }

    private String getProcessNameFromAms() {
        String processName = getProcessNameFromAmsOrNull();
        if (processName == null) {
            return "n/a";
        }
        return processName;
    }

    private String getProcessName() {
        IOException ex;
        String processName = getProcessNameFromAmsOrNull();
        if (processName == null) {
            BufferedReader bufferedReader = null;
            try {
                BufferedReader bufferedReader2 = new BufferedReader(new FileReader("/proc/self/cmdline"), 128);
                try {
                    processName = bufferedReader2.readLine();
                    if (processName != null) {
                        processName = processName.trim();
                    }
                    bufferedReader = bufferedReader2;
                } catch (IOException e) {
                    ex = e;
                    bufferedReader = bufferedReader2;
                    Log.e(ACRA.LOG_TAG, "Failed to get process name.", ex);
                    if (bufferedReader != null) {
                        try {
                            bufferedReader.close();
                        } catch (IOException ex2) {
                            Log.e(ACRA.LOG_TAG, "Failed to close file.", ex2);
                        }
                    }
                    if (processName == null) {
                        return processName;
                    }
                    return "";
                }
            } catch (IOException e2) {
                ex2 = e2;
                Log.e(ACRA.LOG_TAG, "Failed to get process name.", ex2);
                if (bufferedReader != null) {
                    bufferedReader.close();
                }
                if (processName == null) {
                    return "";
                }
                return processName;
            }
            if (bufferedReader != null) {
                bufferedReader.close();
            }
        }
        if (processName == null) {
            return "";
        }
        return processName;
    }

    private String getJailStatus() {
        String buildTags = Build.TAGS;
        if (buildTags != null && buildTags.contains("test-keys")) {
            return "yes";
        }
        try {
            if (new File("/system/app/Superuser.apk").exists()) {
                return "yes";
            }
        } catch (Exception ex) {
            Log.e(ACRA.LOG_TAG, "Failed to find Superuser.pak", ex);
        }
        Map<String, String> env = System.getenv();
        if (env != null) {
            for (String dir : ((String) env.get("PATH")).split(":")) {
                try {
                    File suFile = new File(dir + "/" + "su");
                    if (suFile != null && suFile.exists()) {
                        return "yes";
                    }
                } catch (Exception ex2) {
                    Log.e(ACRA.LOG_TAG, "Failed to find su binary in the PATH", ex2);
                }
            }
        }
        return "no";
    }

    private long getProcessUptime() {
        return Process.getElapsedCpuTime();
    }

    private long getDeviceUptime() {
        return SystemClock.elapsedRealtime();
    }

    public static ErrorReporter getInstance() {
        if (mInstanceSingleton == null) {
            mInstanceSingleton = new ErrorReporter();
        }
        return mInstanceSingleton;
    }

    public void init(Context context, boolean isInternalBuild, FileProvider fileProvider) {
        if (this.mDfltExceptionHandler == null) {
            this.mDfltExceptionHandler = Thread.getDefaultUncaughtExceptionHandler();
            this.mIsInternalBuild = isInternalBuild;
            this.mContext = context;
            this.mFileProvider = fileProvider;
            PackageInfo pi = new PackageManagerWrapper(context).getPackageInfo();
            if (pi != null) {
                this.mAppVersionCode = Integer.toString(pi.versionCode);
                this.mAppVersionName = pi.versionName != null ? pi.versionName : "not set";
            }
            this.mPackageManager = new PackageManagerWrapper(context);
            String osVersion = System.getProperty("os.version");
            boolean isCyanogenmod = osVersion != null ? osVersion.contains("cyanogenmod") : false;
            this.mAppStartDate.setToNow();
            try {
                this.mConstantFields.put(ReportField.ANDROID_ID, Secure.getString(context.getContentResolver(), "android_id"));
                this.mConstantFields.put(ReportField.APP_VERSION_CODE, this.mAppVersionCode);
                this.mConstantFields.put(ReportField.APP_VERSION_NAME, this.mAppVersionName);
                this.mConstantFields.put(ReportField.PACKAGE_NAME, context.getPackageName());
                this.mConstantFields.put(ReportField.PHONE_MODEL, Build.MODEL);
                this.mConstantFields.put(ReportField.ANDROID_VERSION, VERSION.RELEASE);
                this.mConstantFields.put(ReportField.OS_VERSION, osVersion);
                this.mConstantFields.put(ReportField.IS_CYANOGENMOD, Boolean.toString(isCyanogenmod));
                this.mConstantFields.put(ReportField.BRAND, Build.BRAND);
                this.mConstantFields.put(ReportField.PRODUCT, Build.PRODUCT);
                String absolutePath = "n/a";
                File filesDir = context.getFilesDir();
                if (filesDir != null) {
                    absolutePath = filesDir.getAbsolutePath();
                }
                this.mConstantFields.put(ReportField.FILE_PATH, absolutePath);
                if (VERSION.SDK_INT >= 9) {
                    this.mConstantFields.put(ReportField.SERIAL, Build.SERIAL);
                    if (pi != null) {
                        this.mConstantFields.put(ReportField.APP_INSTALL_TIME, formatTimestamp(pi.firstInstallTime));
                        this.mConstantFields.put(ReportField.APP_UPGRADE_TIME, formatTimestamp(pi.lastUpdateTime));
                    }
                }
            } catch (Exception e) {
                Log.e(ACRA.LOG_TAG, "failed to install constants", e);
            }
            this.preallocFile = fileForName(this.mFileProvider, ACRA_DIRNAME, PREALLOCATED_REPORTFILE);
            createPreallocatedReportFile();
        }
    }

    private String formatTimestamp(long time) {
        Time t = new Time();
        t.set(time);
        return t.format3339(false);
    }

    private void createPreallocatedReportFile() {
        IOException e;
        Throwable th;
        FileOutputStream fos = null;
        try {
            if (!this.preallocFile.exists()) {
                byte[] buf = new byte[10240];
                FileOutputStream fos2 = new FileOutputStream(this.preallocFile);
                int i = 0;
                while (((long) i) < 51200) {
                    try {
                        fos2.write(buf);
                        i += buf.length;
                    } catch (IOException e2) {
                        e = e2;
                        fos = fos2;
                    } catch (Throwable th2) {
                        th = th2;
                        fos = fos2;
                    }
                }
                fos = fos2;
            }
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e3) {
                }
            }
        } catch (IOException e4) {
            e = e4;
            try {
                Log.e(ACRA.LOG_TAG, "Failed to pre-allocate crash report file", e);
                if (fos != null) {
                    try {
                        fos.close();
                    } catch (IOException e5) {
                    }
                }
            } catch (Throwable th3) {
                th = th3;
                if (fos != null) {
                    try {
                        fos.close();
                    } catch (IOException e6) {
                    }
                }
                throw th;
            }
        }
    }

    private static long getAvailableInternalMemorySize() {
        try {
            StatFs stat = new StatFs(Environment.getDataDirectory().getPath());
            return ((long) stat.getAvailableBlocks()) * ((long) stat.getBlockSize());
        } catch (Exception e) {
            return -1;
        }
    }

    private static long getTotalInternalMemorySize() {
        try {
            StatFs stat = new StatFs(Environment.getDataDirectory().getPath());
            return ((long) stat.getBlockCount()) * ((long) stat.getBlockSize());
        } catch (Exception e) {
            return -1;
        }
    }

    private void populateConstantDeviceData(CrashReportData crashReport, Writer writer) {
        for (Entry<ReportField, String> entry : getConstantDeviceData().entrySet()) {
            put((ReportField) entry.getKey(), (String) entry.getValue(), crashReport, writer);
        }
    }

    private Map<ReportField, String> getConstantDeviceData() {
        Map<ReportField, String> map;
        synchronized (this.mDeviceSpecificFields) {
            if (this.mDeviceSpecificFields.isEmpty()) {
                this.mDeviceSpecificFields.put(ReportField.BUILD, ReflectionCollector.collectConstants(Build.class));
                this.mDeviceSpecificFields.put(ReportField.JAIL_BROKEN, getJailStatus());
                this.mDeviceSpecificFields.put(ReportField.INSTALLATION_ID, Installation.id(this.mFileProvider));
                this.mDeviceSpecificFields.put(ReportField.TOTAL_MEM_SIZE, Long.toString(getTotalInternalMemorySize()));
                if (this.mPackageManager.hasPermission("android.permission.READ_PHONE_STATE")) {
                    String deviceId = ((TelephonyManager) this.mContext.getSystemService("phone")).getDeviceId();
                    if (deviceId != null) {
                        this.mDeviceSpecificFields.put(ReportField.DEVICE_ID, deviceId);
                    }
                }
                this.mDeviceSpecificFields.put(ReportField.DISPLAY, toString(((WindowManager) this.mContext.getSystemService("window")).getDefaultDisplay()));
                this.mDeviceSpecificFields.put(ReportField.ENVIRONMENT, ReflectionCollector.collectStaticGettersResults(Environment.class));
                this.mDeviceSpecificFields.put(ReportField.DEVICE_FEATURES, DeviceFeaturesCollector.getFeatures(this.mContext));
                this.mDeviceSpecificFields.put(ReportField.SETTINGS_SYSTEM, SettingsCollector.collectSystemSettings(this.mContext));
                this.mDeviceSpecificFields.put(ReportField.SETTINGS_SECURE, SettingsCollector.collectSecureSettings(this.mContext));
                if (VERSION.SDK_INT >= 19) {
                    this.mDeviceSpecificFields.put(ReportField.IS_LOW_RAM_DEVICE, Boolean.toString(((ActivityManager) this.mContext.getSystemService("activity")).isLowRamDevice()));
                }
                this.mDeviceSpecificFields.put(ReportField.ANDROID_RUNTIME, getAndroidRuntime());
            }
            map = this.mDeviceSpecificFields;
        }
        return map;
    }

    private String getAndroidRuntime() {
        if (VERSION.SDK_INT < 19) {
            return ANDROID_RUNTIME_DALVIK;
        }
        String bootClassPath = System.getProperty(JAVA_BOOT_CLASS_PATH);
        if (bootClassPath != null) {
            if (bootClassPath.contains(KNOWN_ART_JAR)) {
                return ANDROID_RUNTIME_ART;
            }
            if (bootClassPath.contains(KNOWN_DALVIK_JAR)) {
                return ANDROID_RUNTIME_DALVIK;
            }
        }
        return ANDROID_RUNTIME_UNKNOWN;
    }

    private void retrieveCrashTimeData(Context context, Throwable e, ReportField[] fields, CrashReportData crashReport, Writer writer) throws Exception {
        List<ReportField> fieldsList = Arrays.asList(fields);
        if (fieldsList.contains(ReportField.REPORT_ID)) {
            put(ReportField.REPORT_ID, UUID.randomUUID().toString(), crashReport, writer);
        }
        if (fieldsList.contains(ReportField.PROCESS_NAME)) {
            put(ReportField.PROCESS_NAME, getProcessName(), crashReport, writer);
        }
        if (fieldsList.contains(ReportField.USER_APP_START_DATE)) {
            put(ReportField.USER_APP_START_DATE, this.mAppStartDate.format3339(false), crashReport, writer);
        }
        if (fieldsList.contains(ReportField.PROCESS_UPTIME)) {
            put(ReportField.PROCESS_UPTIME, Long.toString(getProcessUptime()), crashReport, writer);
        }
        if (fieldsList.contains(ReportField.DEVICE_UPTIME)) {
            put(ReportField.DEVICE_UPTIME, Long.toString(getDeviceUptime()), crashReport, writer);
        }
        if (fieldsList.contains(ReportField.CRASH_CONFIGURATION)) {
            put(ReportField.CRASH_CONFIGURATION, ConfigurationInspector.toString(context.getResources().getConfiguration()), crashReport, writer);
        }
        if (fieldsList.contains(ReportField.AVAILABLE_MEM_SIZE)) {
            put(ReportField.AVAILABLE_MEM_SIZE, Long.toString(getAvailableInternalMemorySize()), crashReport, writer);
        }
        if (fieldsList.contains(ReportField.DUMPSYS_MEMINFO)) {
            put(ReportField.DUMPSYS_MEMINFO, DumpSysCollector.collectMemInfo(context), crashReport, writer);
        }
        if (fieldsList.contains(ReportField.USER_CRASH_DATE)) {
            Time curDate = new Time();
            curDate.setToNow();
            put(ReportField.USER_CRASH_DATE, curDate.format3339(false), crashReport, writer);
        }
        if (fieldsList.contains(ReportField.ACTIVITY_LOG)) {
            String activityLogDump;
            if (e instanceof OutOfMemoryError) {
                activityLogDump = this.activityLogger.toString();
            } else {
                activityLogDump = this.activityLogger.toString(DEFAULT_TRACE_COUNT_LIMIT);
            }
            put(ReportField.ACTIVITY_LOG, activityLogDump, crashReport, writer);
        }
        if (fieldsList.contains(ReportField.PROCESS_NAME_BY_AMS)) {
            put(ReportField.PROCESS_NAME_BY_AMS, getProcessNameFromAms(), crashReport, writer);
        }
        resetProcessNameByAmsCache();
        if (fieldsList.contains(ReportField.OPEN_FD_COUNT)) {
            put(ReportField.OPEN_FD_COUNT, String.valueOf(ProcFileReader.getOpenFDCount()), crashReport, writer);
        }
        if (fieldsList.contains(ReportField.OPEN_FD_SOFT_LIMIT) || fieldsList.contains(ReportField.OPEN_FD_HARD_LIMIT)) {
            OpenFDLimits limits = ProcFileReader.getOpenFDLimits();
            if (limits != null) {
                if (fieldsList.contains(ReportField.OPEN_FD_SOFT_LIMIT)) {
                    put(ReportField.OPEN_FD_SOFT_LIMIT, limits.softLimit, crashReport, writer);
                }
                if (fieldsList.contains(ReportField.OPEN_FD_HARD_LIMIT)) {
                    put(ReportField.OPEN_FD_HARD_LIMIT, limits.hardLimit, crashReport, writer);
                }
            }
        }
        if (VERSION.SDK_INT >= 16 && this.mIsInternalBuild) {
            if (fieldsList.contains(ReportField.LOGCAT)) {
                put(ReportField.LOGCAT, LogCatCollector.collectLogCat(null), crashReport, writer);
            }
            if (fieldsList.contains(ReportField.EVENTSLOG)) {
                put(ReportField.EVENTSLOG, LogCatCollector.collectLogCat(EventEntry.TABLE_NAME), crashReport, writer);
            }
            if (fieldsList.contains(ReportField.RADIOLOG)) {
                put(ReportField.RADIOLOG, LogCatCollector.collectLogCat("radio"), crashReport, writer);
            }
            if (fieldsList.contains(ReportField.DROPBOX)) {
                put(ReportField.DROPBOX, DropBoxCollector.read(this.mContext, ACRA.getConfig().additionalDropBoxTags()), crashReport, writer);
            }
        }
        if (fieldsList.contains(ReportField.LARGE_MEM_HEAP) && VERSION.SDK_INT >= 11) {
            put(ReportField.LARGE_MEM_HEAP, DumpSysCollector.collectLargerMemoryInfo(context), crashReport, writer);
        }
    }

    private static String toString(Display display) {
        DisplayMetrics metrics = new DisplayMetrics();
        display.getMetrics(metrics);
        StringBuilder result = new StringBuilder();
        result.append("width=").append(display.getWidth()).append('\n').append("height=").append(display.getHeight()).append('\n').append("pixelFormat=").append(display.getPixelFormat()).append('\n').append("refreshRate=").append(display.getRefreshRate()).append("fps").append('\n').append("metrics.density=x").append(metrics.density).append('\n').append("metrics.scaledDensity=x").append(metrics.scaledDensity).append('\n').append("metrics.widthPixels=").append(metrics.widthPixels).append('\n').append("metrics.heightPixels=").append(metrics.heightPixels).append('\n').append("metrics.xdpi=").append(metrics.xdpi).append('\n').append("metrics.ydpi=").append(metrics.ydpi);
        return result.toString();
    }

    public void uncaughtException(Thread t, Throwable e) {
        Log.e(ACRA.LOG_TAG, "ParseCrashReporting caught a " + e.getClass().getSimpleName() + " exception for " + this.mContext.getPackageName() + ". Building report.");
        this.usePreallocatedFile = true;
        boolean wasProcessingCrash = mProcessingCrash.getAndSet(true);
        Map<String, String> extra = null;
        try {
            Map<String, String> extra2 = new HashMap();
            try {
                extra2.put(IS_PROCESSING_ANOTHER_EXCEPTION, String.valueOf(wasProcessingCrash));
                extra = extra2;
            } catch (OutOfMemoryError e2) {
                extra = extra2;
            }
        } catch (OutOfMemoryError e3) {
        }
        ReportsSenderWorker worker = handleException(e, extra);
        if (worker != null) {
            while (worker.isAlive()) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e1) {
                    Log.e(ACRA.LOG_TAG, "Error : ", e1);
                }
            }
            Throwable workerException = worker.getException();
            if (workerException != null) {
                Log.e(ACRA.LOG_TAG, "ReportsWorkerSender failed with exception", workerException);
                handleExceptionInternal(workerException, extra, null, getReportFieldsForException(e), false);
            }
        }
        if (this.mDfltExceptionHandler != null) {
            this.mDfltExceptionHandler.uncaughtException(t, e);
        }
    }

    private void writeToLogBridge(String tag, String message, Throwable t, String overrideStackTrace) {
        LogBridge logBridge = getLogBridge();
        if (logBridge != null) {
            if (overrideStackTrace != null) {
                logBridge.log(tag, message + "\n" + overrideStackTrace, null);
            } else {
                logBridge.log(tag, message, t);
            }
        } else if (overrideStackTrace != null) {
            Log.e(tag, message + "\n" + overrideStackTrace);
        } else {
            Log.e(tag, message, t);
        }
    }

    private String throwableToString(Throwable e) {
        if (e == null) {
            e = new Exception("Report requested by developer");
        }
        Writer result = new StringWriter();
        PrintWriter printWriter = new PrintWriter(result);
        e.printStackTrace(printWriter);
        printWriter.close();
        return result.toString();
    }

    private void gatherCrashData(String stackTrace, Throwable e, ReportField[] fields, CrashReportData crashReport, Writer w, Map<String, String> extras) throws Exception {
        if (fields == null) {
            fields = ACRA.MINIMAL_REPORT_FIELDS;
        }
        put(ReportField.UID, getUserId(), crashReport, w);
        put(ReportField.STACK_TRACE, stackTrace, crashReport, w);
        for (Entry<ReportField, String> entry : this.mConstantFields.entrySet()) {
            put((ReportField) entry.getKey(), (String) entry.getValue(), crashReport, w);
        }
        retrieveCrashTimeData(this.mContext, e, fields, crashReport, w);
        populateConstantDeviceData(crashReport, w);
        put(ReportField.CUSTOM_DATA, dumpCustomDataToString(extras, e), crashReport, w);
    }

    private void put(ReportField key, String value, CrashReportData crashReport, Writer writer) {
        try {
            if (this.sendInMemoryReport) {
                writer = null;
            }
            crashReport.put(key, value, writer);
        } catch (IOException e) {
            this.sendInMemoryReport = true;
        }
    }

    public void writeReportToStream(Throwable e, OutputStream os) throws Exception {
        Throwable th = e;
        gatherCrashData(throwableToString(e), th, ACRA.ALL_CRASH_REPORT_FIELDS, new CrashReportData(), CrashReportData.getWriter(os), null);
    }

    public ReportsSenderWorker handleException(Throwable e) {
        return handleException(e, null);
    }

    public ReportsSenderWorker handleException(Throwable e, Map<String, String> extras) {
        return handleExceptionInternal(e, extras, null, getReportFieldsForException(e), !(e instanceof OutOfMemoryError));
    }

    private ReportsSenderWorker handleExceptionInternal(Throwable e, Map<String, String> extras, String overrideStackTrace, ReportField[] fields, boolean shouldSendImmediately) {
        Exception ex;
        FileChannel chan;
        ReportsSenderWorker reportsSenderWorker;
        Throwable mostSignificantCause = getMostSignificantCause(e);
        Class causeClass = mostSignificantCause.getClass();
        if (!shouldContinueProcessingException(mostSignificantCause)) {
            return null;
        }
        CrashReportData crashReport = new CrashReportData();
        String description = e instanceof NonCrashException ? ((NonCrashException) e).getExceptionFriendlyName() : "crash";
        writeToLogBridge(ACRA.LOG_TAG, "Handling exception for " + description, e, overrideStackTrace);
        Log.d(ACRA.LOG_TAG, "Generating report file for " + description);
        File tempReportFile = fileForName(this.mFileProvider, ACRA_DIRNAME, genCrashReportFileName(causeClass, TEMP_REPORTFILE_EXTENSION));
        String reportFileName = genCrashReportFileName(causeClass, REPORTFILE_EXTENSION);
        File reportFile = fileForName(this.mFileProvider, ACRA_DIRNAME, reportFileName);
        RandomAccessFile outputFile = null;
        Writer tempReportWriter = null;
        try {
            if (this.usePreallocatedFile) {
                this.preallocFile.renameTo(tempReportFile);
            }
            RandomAccessFile randomAccessFile = new RandomAccessFile(tempReportFile, "rw");
            try {
                tempReportWriter = CrashReportData.getWriter(new FileOutputStream(randomAccessFile.getFD()));
                outputFile = randomAccessFile;
            } catch (Exception e2) {
                ex = e2;
                outputFile = randomAccessFile;
                Log.e(ACRA.LOG_TAG, "An error occurred while creating the report file ...", ex);
                this.sendInMemoryReport = true;
                put(ReportField.ACRA_REPORT_FILENAME, reportFileName, crashReport, tempReportWriter);
                put(ReportField.EXCEPTION_CAUSE, causeClass.getName(), crashReport, tempReportWriter);
                if (overrideStackTrace == null) {
                    overrideStackTrace = throwableToString(e);
                }
                gatherCrashData(overrideStackTrace, e, fields, crashReport, tempReportWriter, extras);
                if (outputFile != null) {
                    try {
                        chan = outputFile.getChannel();
                        chan.truncate(chan.position());
                        outputFile.close();
                        tempReportFile.renameTo(reportFile);
                    } catch (Exception ex2) {
                        Log.e(ACRA.LOG_TAG, "An error occurred while deleting closing the report file ...", ex2);
                    }
                }
                if (shouldSendImmediately) {
                    if (this.sendInMemoryReport) {
                        reportsSenderWorker = new ReportsSenderWorker(this, crashReport);
                    } else {
                        reportsSenderWorker = new ReportsSenderWorker(CrashReportType.ACRA_CRASH_REPORT);
                    }
                    Log.v(ACRA.LOG_TAG, "About to start ReportSenderWorker from #handleException");
                    wk.start();
                    return wk;
                }
                Log.v(ACRA.LOG_TAG, "ParseCrashReporting caught an OutOfMemoryError. Report upload deferred until next ParseCrashReporting launch.");
                return null;
            }
        } catch (Exception e3) {
            ex2 = e3;
            Log.e(ACRA.LOG_TAG, "An error occurred while creating the report file ...", ex2);
            this.sendInMemoryReport = true;
            put(ReportField.ACRA_REPORT_FILENAME, reportFileName, crashReport, tempReportWriter);
            put(ReportField.EXCEPTION_CAUSE, causeClass.getName(), crashReport, tempReportWriter);
            if (overrideStackTrace == null) {
                overrideStackTrace = throwableToString(e);
            }
            gatherCrashData(overrideStackTrace, e, fields, crashReport, tempReportWriter, extras);
            if (outputFile != null) {
                chan = outputFile.getChannel();
                chan.truncate(chan.position());
                outputFile.close();
                tempReportFile.renameTo(reportFile);
            }
            if (shouldSendImmediately) {
                if (this.sendInMemoryReport) {
                    reportsSenderWorker = new ReportsSenderWorker(this, crashReport);
                } else {
                    reportsSenderWorker = new ReportsSenderWorker(CrashReportType.ACRA_CRASH_REPORT);
                }
                Log.v(ACRA.LOG_TAG, "About to start ReportSenderWorker from #handleException");
                wk.start();
                return wk;
            }
            Log.v(ACRA.LOG_TAG, "ParseCrashReporting caught an OutOfMemoryError. Report upload deferred until next ParseCrashReporting launch.");
            return null;
        }
        try {
            put(ReportField.ACRA_REPORT_FILENAME, reportFileName, crashReport, tempReportWriter);
            put(ReportField.EXCEPTION_CAUSE, causeClass.getName(), crashReport, tempReportWriter);
            if (overrideStackTrace == null) {
                overrideStackTrace = throwableToString(e);
            }
            gatherCrashData(overrideStackTrace, e, fields, crashReport, tempReportWriter, extras);
            if (outputFile != null) {
                chan = outputFile.getChannel();
                chan.truncate(chan.position());
                outputFile.close();
                tempReportFile.renameTo(reportFile);
            }
        } catch (Exception ex22) {
            try {
                Log.e(ACRA.LOG_TAG, "An error occurred while gathering crash data ...", ex22);
                put(ReportField.ACRA_INTERNAL, throwableToString(ex22), crashReport, tempReportWriter);
            } catch (Exception ex1) {
                Log.e(ACRA.LOG_TAG, "An error occurred while gathering internal crash data ...", ex1);
                put(ReportField.ACRA_INTERNAL, mInternalException, crashReport, tempReportWriter);
                if (outputFile != null) {
                    try {
                        chan = outputFile.getChannel();
                        chan.truncate(chan.position());
                        outputFile.close();
                        tempReportFile.renameTo(reportFile);
                    } catch (Exception ex222) {
                        Log.e(ACRA.LOG_TAG, "An error occurred while deleting closing the report file ...", ex222);
                    }
                }
                if (shouldSendImmediately) {
                    Log.v(ACRA.LOG_TAG, "ParseCrashReporting caught an OutOfMemoryError. Report upload deferred until next ParseCrashReporting launch.");
                    return null;
                }
                if (this.sendInMemoryReport) {
                    reportsSenderWorker = new ReportsSenderWorker(CrashReportType.ACRA_CRASH_REPORT);
                } else {
                    reportsSenderWorker = new ReportsSenderWorker(this, crashReport);
                }
                Log.v(ACRA.LOG_TAG, "About to start ReportSenderWorker from #handleException");
                wk.start();
                return wk;
            } finally {
                Log.e(ACRA.LOG_TAG, "An error occurred while gathering crash data ...", ex222);
            }
            if (outputFile != null) {
                chan = outputFile.getChannel();
                chan.truncate(chan.position());
                outputFile.close();
                tempReportFile.renameTo(reportFile);
            }
        } catch (Throwable th) {
            if (outputFile != null) {
                try {
                    chan = outputFile.getChannel();
                    chan.truncate(chan.position());
                    outputFile.close();
                    tempReportFile.renameTo(reportFile);
                } catch (Exception ex2222) {
                    Log.e(ACRA.LOG_TAG, "An error occurred while deleting closing the report file ...", ex2222);
                }
            }
        }
        if (shouldSendImmediately) {
            if (this.sendInMemoryReport) {
                reportsSenderWorker = new ReportsSenderWorker(this, crashReport);
            } else {
                reportsSenderWorker = new ReportsSenderWorker(CrashReportType.ACRA_CRASH_REPORT);
            }
            Log.v(ACRA.LOG_TAG, "About to start ReportSenderWorker from #handleException");
            wk.start();
            return wk;
        }
        Log.v(ACRA.LOG_TAG, "ParseCrashReporting caught an OutOfMemoryError. Report upload deferred until next ParseCrashReporting launch.");
        return null;
    }

    public ReportsSenderWorker handleException(Throwable e, String stackTrace, Map<String, String> extras) {
        return handleExceptionInternal(e, extras, stackTrace, getReportFieldsForException(e), true);
    }

    public void handleExceptionWithCustomFields(Exception e, Map<String, String> data, ReportField[] fields) {
        handleExceptionInternal(e, data, null, fields, true);
    }

    private void sendCrashReport(CrashReportData errorContent) throws ReportSenderException {
        boolean sentAtLeastOnce = false;
        Iterator i$ = this.mReportSenders.iterator();
        while (i$.hasNext()) {
            ReportSender sender = (ReportSender) i$.next();
            try {
                sender.send(errorContent);
                sentAtLeastOnce = true;
            } catch (ReportSenderException e) {
                if (sentAtLeastOnce) {
                    Log.w(ACRA.LOG_TAG, "ReportSender of class " + sender.getClass().getName() + " failed but other senders completed their task. " + "ParseCrashReporting will not send this report again.");
                } else {
                    throw e;
                }
            }
        }
    }

    private String genCrashReportFileName(Class cause, String fileExtension) {
        return Long.toString(System.currentTimeMillis()) + "-" + cause.getSimpleName() + (this.mAppVersionCode != null ? "-" + this.mAppVersionCode : "") + fileExtension;
    }

    public String[] getCrashReportFilesList(String path, final String... extensions) {
        if (this.mContext == null) {
            Log.e(ACRA.LOG_TAG, "Trying to get crash reports but crash reporting is not initialized.");
            return new String[0];
        }
        File dir = this.mFileProvider.getFile(path);
        if (dir != null) {
            Log.d(ACRA.LOG_TAG, "Looking for error files in " + dir.getAbsolutePath());
            String[] result = dir.list(new FilenameFilter() {
                public boolean accept(File dir, String name) {
                    for (String extension : extensions) {
                        if (name.endsWith(extension)) {
                            return true;
                        }
                    }
                    return false;
                }
            });
            if (result == null) {
                return new String[0];
            }
            return result;
        }
        Log.w(ACRA.LOG_TAG, "Application files directory does not exist! The application may not be installed correctly. Please try reinstalling.");
        return new String[0];
    }

    synchronized void checkAndSendReports(Context context, CrashReportType... types) {
        Log.d(ACRA.LOG_TAG, "#checkAndSendReports - start");
        for (CrashReportType reportType : types) {
            if (CrashReportType.ACRA_CRASH_REPORT == reportType) {
                checkAndSendAcraReports(context);
            } else {
                checkAndSendCrashAttachments(context, reportType);
            }
        }
        Log.d(ACRA.LOG_TAG, "#checkAndSendReports - finish");
    }

    private void checkAndSendAcraReports(Context context) {
        String[] reportFiles = getCrashReportFilesList(ACRA_DIRNAME, REPORTFILE_EXTENSION, TEMP_REPORTFILE_EXTENSION);
        Arrays.sort(reportFiles);
        int reportsSentCount = 0;
        String uploadedByProcess = getProcessNameFromAms();
        for (String curFileName : reportFiles) {
            if (reportsSentCount >= 5) {
                deleteFile(ACRA_DIRNAME, curFileName);
            } else {
                Log.d(ACRA.LOG_TAG, "Loading file " + curFileName);
                try {
                    CrashReportData previousCrashReport = loadAcraCrashReport(context, curFileName);
                    if (previousCrashReport != null) {
                        previousCrashReport.put(ReportField.ACRA_REPORT_FILENAME, curFileName);
                        previousCrashReport.put(ReportField.UPLOADED_BY_PROCESS, uploadedByProcess);
                        Log.i(ACRA.LOG_TAG, "Sending file " + curFileName);
                        sendCrashReport(previousCrashReport);
                        deleteFile(ACRA_DIRNAME, curFileName);
                    }
                    reportsSentCount++;
                } catch (RuntimeException e) {
                    Log.e(ACRA.LOG_TAG, "Failed to send crash reports", e);
                    deleteFile(ACRA_DIRNAME, curFileName);
                    return;
                } catch (IOException e2) {
                    Log.e(ACRA.LOG_TAG, "Failed to load crash report for " + curFileName, e2);
                    deleteFile(ACRA_DIRNAME, curFileName);
                    return;
                } catch (ReportSenderException e3) {
                    Log.e(ACRA.LOG_TAG, "Failed to send crash report for " + curFileName, e3);
                    return;
                }
            }
        }
    }

    private int checkAndSendCrashAttachments(Context context, CrashReportType type) {
        Log.d(ACRA.LOG_TAG, "#checkAndSendCrashAttachments - start");
        int dumpsSend = 0;
        String[] dumpFiles = getCrashReportFilesList(type.directory, type.fileExtensions);
        if (dumpFiles != null && dumpFiles.length > 0) {
            Arrays.sort(dumpFiles);
            CrashReportData tempCrashReportData = new CrashReportData();
            try {
                ErrorReporter errorReporter = this;
                gatherCrashData(CRASH_ATTACHMENT_DUMMY_STACKTRACE, new CrashAttachmentException(), ACRA.ALL_CRASH_REPORT_FIELDS, tempCrashReportData, null, null);
            } catch (Exception e) {
                put(ReportField.REPORT_LOAD_THROW, "retrieve exception: " + e.getMessage(), tempCrashReportData, null);
            }
            for (String fname : dumpFiles) {
                if (dumpsSend >= 5) {
                    deleteFile(DUMP_DIR, fname);
                } else {
                    try {
                        CrashReportData reportData = loadCrashAttachment(context, fname, type);
                        String attachment = "load failed";
                        if (reportData != null) {
                            attachment = (String) reportData.get(type.attachmentField);
                        }
                        tempCrashReportData.put(ReportField.REPORT_ID, fname.substring(0, fname.lastIndexOf(46)), null);
                        tempCrashReportData.put(type.attachmentField, attachment, null);
                        tempCrashReportData.put(ReportField.EXCEPTION_CAUSE, CRASH_ATTACHMENT_DUMMY_STACKTRACE, null);
                        sendCrashReport(tempCrashReportData);
                        deleteFile(type.directory, fname);
                        dumpsSend++;
                    } catch (ReportSenderException e2) {
                        Log.e(ACRA.LOG_TAG, "Failed to send crash attachment report " + fname, e2);
                    } catch (Throwable e3) {
                        Log.e(ACRA.LOG_TAG, "Failed on crash attachment file " + fname, e3);
                        deleteFile(type.directory, fname);
                    }
                }
            }
        }
        Log.d(ACRA.LOG_TAG, "#checkAndSendCrashAttachments - finish, sent: " + Integer.toString(dumpsSend));
        return dumpsSend;
    }

    synchronized void sendInMemoryReport(Context context, CrashReportData crashReport) {
        Log.i(ACRA.LOG_TAG, "Sending in-memory report");
        try {
            crashReport.put(ReportField.UPLOADED_BY_PROCESS, getProcessNameFromAms());
            sendCrashReport(crashReport);
            String reportName = (String) crashReport.get(ReportField.ACRA_REPORT_FILENAME);
            if (reportName != null) {
                File reportFile = fileForName(this.mFileProvider, ACRA_DIRNAME, reportName);
                if (reportFile != null) {
                    reportFile.delete();
                }
            }
        } catch (Exception e) {
            Log.e(ACRA.LOG_TAG, "Failed to send in-memory crash report: ", e);
        }
    }

    private CrashReportData loadAcraCrashReport(Context context, String fileName) throws IOException {
        return loadCrashReport(context, fileName, CrashReportType.ACRA_CRASH_REPORT, this.mMaxReportSize);
    }

    private CrashReportData loadCrashAttachment(Context context, String fileName, CrashReportType type) throws IOException {
        return loadCrashReport(context, fileName, type, type.defaultMaxSize);
    }

    private CrashReportData loadCrashReport(Context context, String fileName, CrashReportType crashReportType, long maxSize) throws IOException {
        CrashReportData crashReport = new CrashReportData();
        File rptfp = fileForName(this.mFileProvider, crashReportType.directory, fileName);
        if (System.currentTimeMillis() - rptfp.lastModified() > MAX_REPORT_AGE) {
            Log.w(ACRA.LOG_TAG, "crash report " + fileName + " was too old; deleted");
            deleteFile(crashReportType.directory, fileName);
            return null;
        }
        if (fileName.endsWith(TEMP_REPORTFILE_EXTENSION) && System.currentTimeMillis() - rptfp.lastModified() < MIN_TEMP_REPORT_AGE) {
            Log.w(ACRA.LOG_TAG, "temp file " + fileName + " is too recent; skipping");
            return null;
        } else if (rptfp.length() > maxSize) {
            Log.w(ACRA.LOG_TAG, "" + rptfp.length() + "-byte crash report " + fileName + " exceeded max size of " + maxSize + " bytes; deleted");
            deleteFile(crashReportType.directory, fileName);
            return null;
        } else {
            InputStream input = new FileInputStream(rptfp);
            boolean closed = false;
            try {
                if (crashReportType == CrashReportType.ACRA_CRASH_REPORT) {
                    crashReport.load(input);
                } else {
                    crashReport.put(crashReportType.attachmentField, loadAttachment(input, (int) rptfp.length()));
                }
                if (null == null) {
                    input.close();
                }
            } catch (Throwable th) {
                if (!closed) {
                    input.close();
                }
            }
            crashReport.put(ReportField.ACRA_REPORT_FILENAME, fileName);
            backfillCrashReportData(crashReport);
            return crashReport;
        }
    }

    void backfillCrashReportData(CrashReportData crashReport) {
        boolean hasAppBeenUpgraded = !parseVersionCodeFromFileName(crashReport.getProperty(ReportField.ACRA_REPORT_FILENAME)).equals(this.mAppVersionCode);
        String reportID = (String) crashReport.get(ReportField.REPORT_ID);
        if (reportID == null || reportID.length() == 0) {
            for (Entry<ReportField, String> e : this.mConstantFields.entrySet()) {
                if (((ReportField) e.getKey()).equals(ReportField.APP_VERSION_NAME)) {
                    if (!hasAppBeenUpgraded) {
                        crashReport.put((Enum) e.getKey(), e.getValue());
                    }
                } else if (crashReport.get(e.getKey()) == null) {
                    crashReport.put((Enum) e.getKey(), e.getValue());
                }
            }
        }
        String currentUserId = getUserId();
        String previousUid = (String) crashReport.get(ReportField.UID);
        if (!TextUtils.isEmpty(currentUserId) && TextUtils.isEmpty(previousUid)) {
            crashReport.put(ReportField.UID, currentUserId);
        }
    }

    public String parseVersionCodeFromFileName(String fileName) {
        if (fileName != null) {
            Matcher matcher = VERSION_CODE_REGEX.matcher(fileName);
            if (matcher.matches()) {
                return matcher.group(1);
            }
        }
        return "";
    }

    private String loadAttachment(InputStream in, int nbytes) throws IOException {
        Throwable th;
        int offset = 0;
        int nbytesread = 0;
        byte[] attachment = new byte[nbytes];
        while (nbytes - offset > 0) {
            nbytesread = in.read(attachment, offset, nbytes - offset);
            if (nbytesread == -1) {
                break;
            }
            offset += nbytesread;
        }
        if (nbytesread == 0) {
            return "";
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        GZIPOutputStream gzipOutputStream = null;
        try {
            GZIPOutputStream gzipOutputStream2 = new GZIPOutputStream(baos);
            try {
                gzipOutputStream2.write(attachment, 0, attachment.length);
                gzipOutputStream2.finish();
                String encodeToString = Base64.encodeToString(baos.toByteArray(), 0);
                if (gzipOutputStream2 == null) {
                    return encodeToString;
                }
                gzipOutputStream2.close();
                return encodeToString;
            } catch (Throwable th2) {
                th = th2;
                gzipOutputStream = gzipOutputStream2;
                if (gzipOutputStream != null) {
                    gzipOutputStream.close();
                }
                throw th;
            }
        } catch (Throwable th3) {
            th = th3;
            if (gzipOutputStream != null) {
                gzipOutputStream.close();
            }
            throw th;
        }
    }

    private static File fileForName(FileProvider fileProvider, String path, String fileName) {
        return new File(fileProvider.getFile(path), fileName);
    }

    private void deleteFile(String path, String fileName) {
        if (!fileForName(this.mFileProvider, path, fileName).delete()) {
            Log.w(ACRA.LOG_TAG, "Could not delete error report : " + fileName);
        }
    }

    public ReportsSenderWorker checkReportsOnApplicationStart() {
        String[] filesList = getCrashReportFilesList(ACRA_DIRNAME, REPORTFILE_EXTENSION);
        String[] nativeCrashFileList = getCrashReportFilesList(DUMP_DIR, DUMPFILE_EXTENSION);
        if ((filesList == null || filesList.length <= 0) && (nativeCrashFileList == null || nativeCrashFileList.length <= 0)) {
            return null;
        }
        Log.v(ACRA.LOG_TAG, "About to start ReportSenderWorker from #checkReportOnApplicationStart");
        if (nativeCrashFileList != null && nativeCrashFileList.length > 0) {
            this.mHasNativeCrashDumpOnInit = true;
        }
        return checkReportsOfType(ALL_REPORT_TYPES);
    }

    public ReportsSenderWorker checkReportsOfType(CrashReportType... types) {
        ReportsSenderWorker worker = new ReportsSenderWorker(types);
        worker.start();
        return worker;
    }

    public boolean isNativeCrashedOnPreviousRun() {
        return this.mHasNativeCrashDumpOnInit;
    }

    public void addReportSender(ReportSender sender) {
        this.mReportSenders.add(sender);
    }

    public void removeAllReportSenders() {
        this.mReportSenders.clear();
    }

    public void setMaxReportSize(long size) {
        this.mMaxReportSize = size;
    }

    public void setReportSender(ReportSender sender) {
        removeAllReportSenders();
        addReportSender(sender);
    }

    public void registerActivity(String activityName) {
        if (activityName != null) {
            this.activityLogger.append(activityName);
        }
    }

    private ReportField[] getReportFieldsForException(Throwable e) {
        return e instanceof OutOfMemoryError ? ACRA.MINIMAL_REPORT_FIELDS : ACRA.ALL_CRASH_REPORT_FIELDS;
    }

    Throwable getMostSignificantCause(Throwable e) {
        if (e instanceof NonCrashException) {
            return e;
        }
        Throwable cause = e;
        while (cause.getCause() != null) {
            cause = cause.getCause();
        }
        return cause;
    }

    private boolean shouldContinueProcessingException(Throwable t) {
        boolean z = true;
        synchronized (this.mShouldContinueProcessingExceptionLock) {
            if (this.mCurrentlyProcessingOOM) {
                z = false;
            } else {
                if (t instanceof OutOfMemoryError) {
                    this.mCurrentlyProcessingOOM = true;
                }
            }
        }
        return z;
    }
}
