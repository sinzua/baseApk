package com.parse;

import android.app.ActivityManager;
import android.app.ActivityManager.MemoryInfo;
import android.content.Context;
import android.os.Build.VERSION;
import android.os.Debug;
import java.util.Locale;

class DumpSysCollector {
    DumpSysCollector() {
    }

    protected static String collectMemInfo(Context context) {
        StringBuilder meminfo = new StringBuilder();
        ActivityManager am = (ActivityManager) context.getSystemService("activity");
        MemoryInfo mi = new MemoryInfo();
        am.getMemoryInfo(mi);
        Debug.MemoryInfo dmi = new Debug.MemoryInfo();
        Debug.getMemoryInfo(dmi);
        int memclass = am.getMemoryClass();
        int pct_dn = (int) (100.0f * (((float) (dmi.nativePrivateDirty + dmi.dalvikPrivateDirty)) / (1024.0f * ((float) memclass))));
        int pct_n = (int) (((float) (dmi.nativePrivateDirty * 100)) / (1024.0f * ((float) memclass)));
        int pct_dno = (int) (((float) (((dmi.nativePrivateDirty + dmi.dalvikPrivateDirty) + dmi.otherPrivateDirty) * 100)) / (1024.0f * ((float) memclass)));
        int pct_o = (int) (((float) (dmi.otherPrivateDirty * 100)) / (1024.0f * ((float) memclass)));
        meminfo.append(String.format(Locale.US, "percent dalvik+native / native / d+n+other / other %d / %d / %d / %d", new Object[]{Integer.valueOf(pct_dn), Integer.valueOf(pct_n), Integer.valueOf(pct_dno), Integer.valueOf(pct_o)}));
        meminfo.append("avail/thresh/low? " + mi.availMem + "/" + mi.threshold + "/" + mi.lowMemory + "/" + "(" + ((int) (((float) (100 * mi.threshold)) / ((float) mi.availMem))) + "%) memclass=" + memclass);
        meminfo.append("DebugMemInfo(kB): Private / Proportional / Shared");
        meminfo.append(String.format(Locale.US, "          dalvik: %7d / %7d / %7d", new Object[]{Integer.valueOf(dmi.dalvikPrivateDirty), Integer.valueOf(dmi.dalvikPss), Integer.valueOf(dmi.dalvikSharedDirty)}));
        meminfo.append(String.format(Locale.US, "          native: %7d / %7d / %7d", new Object[]{Integer.valueOf(dmi.dalvikPrivateDirty), Integer.valueOf(dmi.nativePss), Integer.valueOf(dmi.nativeSharedDirty)}));
        meminfo.append(String.format(Locale.US, "           other: %7d / %7d / %7d", new Object[]{Integer.valueOf(dmi.otherPrivateDirty), Integer.valueOf(dmi.otherPss), Integer.valueOf(dmi.otherSharedDirty)}));
        meminfo.append(String.format(Locale.US, "GC: %d GCs, %d freed, %d free count", new Object[]{Integer.valueOf(Debug.getGlobalGcInvocationCount()), Integer.valueOf(Debug.getGlobalFreedSize()), Integer.valueOf(Debug.getGlobalFreedCount())}));
        meminfo.append(String.format(Locale.US, "Native Heap: size/allocated/free %7d / %7d / %7d", new Object[]{Long.valueOf(Debug.getNativeHeapSize()), Long.valueOf(Debug.getNativeHeapAllocatedSize()), Long.valueOf(Debug.getNativeHeapFreeSize())}));
        meminfo.append(String.format(Locale.US, "Threads: alloc count/alloc size/ext ac/ext as %7d / %7d / %7d / %7d", new Object[]{Integer.valueOf(Debug.getThreadAllocCount()), Integer.valueOf(Debug.getThreadAllocSize()), Integer.valueOf(Debug.getThreadExternalAllocCount()), Integer.valueOf(Debug.getThreadExternalAllocSize())}));
        Runtime runtime = Runtime.getRuntime();
        meminfo.append(String.format(Locale.US, "Java Heap: size/allocated/free %7d / %7d / %7d", new Object[]{Long.valueOf(runtime.maxMemory()), Long.valueOf(runtime.totalMemory() - runtime.freeMemory()), Long.valueOf(runtime.freeMemory())}));
        return meminfo.toString();
    }

    protected static String collectLargerMemoryInfo(Context context) {
        if (VERSION.SDK_INT < 11) {
            return "";
        }
        StringBuilder writer = new StringBuilder();
        writer.append("Large heap size =" + ((ActivityManager) context.getSystemService("activity")).getLargeMemoryClass());
        return writer.toString();
    }
}
