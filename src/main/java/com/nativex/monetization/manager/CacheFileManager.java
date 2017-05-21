package com.nativex.monetization.manager;

import android.os.Environment;
import android.os.StatFs;
import com.nativex.common.Log;
import com.nativex.monetization.business.CacheFile;
import java.io.File;
import java.util.Set;

public class CacheFileManager {
    private static final String NATIVEX_CACHE_DIRECTORY = "/nativex/ad_assets/";
    private static final String pathSeparator = "/";

    public static void createNativeXCacheDirectoryIfNotExists() {
        String nativeXCacheDirectoryPath = getNativeXCacheDirectoryPath();
        if (nativeXCacheDirectoryPath == null) {
            Log.e("Couldn't Create Nativex Cache directory. Couldn't find the application's files directory path.");
            return;
        }
        File nativeXCacheDirectoryFile = new File(nativeXCacheDirectoryPath);
        if (!nativeXCacheDirectoryFile.exists()) {
            nativeXCacheDirectoryFile.mkdirs();
        }
    }

    public static String getNativeXCacheDirectoryPath() {
        String appFilesDirectoryPath = MonetizationSharedDataManager.getApplicationFilesDirectoryPath();
        if (appFilesDirectoryPath != null) {
            return appFilesDirectoryPath + NATIVEX_CACHE_DIRECTORY;
        }
        return null;
    }

    public static long getAvailableInternalMemorySizeInBytes() {
        StatFs stat = new StatFs(Environment.getDataDirectory().getPath());
        return ((long) stat.getAvailableBlocks()) * ((long) stat.getBlockSize());
    }

    public static void cleanUpCacheDB() {
        for (CacheFile cacheFile : CacheDBManager.getInstance().getAllCacheFilesByMD5()) {
            if (!doesFileExistForCacheFile(cacheFile.getFileName())) {
                Log.i("Cleaning up record from cache DB that doesn't have associated physical cached file." + cacheFile.getFileName());
                CacheDBManager.getInstance().deleteCacheFilesMatchingMD5(cacheFile.getMD5());
            }
        }
    }

    public static void cleanUpNativeXCachedDirectory() {
        String internalCacheDirectoryPath = getNativeXCacheDirectoryPath();
        if (internalCacheDirectoryPath != null) {
            Set<String> fileNamesOfCacheFiles = CacheDBManager.getInstance().getFileNamesOfAllCacheFiles();
            File[] files = new File(internalCacheDirectoryPath).listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.isDirectory()) {
                        file.delete();
                    } else if (!fileNamesOfCacheFiles.contains(file.getName())) {
                        if (file.delete()) {
                            Log.i("Cleaning up unknown file from Nativex cache directory. " + file.getName());
                        } else {
                            Log.e("FAILED to clean up unknown file from Nativex cache directory. " + file.getName());
                        }
                    }
                }
            }
        }
    }

    public static void deleteFromInternalCache(String filename) {
        String internalCacheDirectoryPath = getNativeXCacheDirectoryPath();
        if (internalCacheDirectoryPath != null) {
            File internalCacheFile = new File(internalCacheDirectoryPath + filename);
            if (internalCacheFile.exists()) {
                internalCacheFile.delete();
            }
        }
    }

    public static void deleteAllFiles() {
        String cacheDirectoryPath = getNativeXCacheDirectoryPath();
        if (cacheDirectoryPath != null) {
            for (File file : new File(cacheDirectoryPath).listFiles()) {
                file.delete();
            }
        }
    }

    public static boolean doesFileExistForCacheFile(String fileName) {
        String internalCacheDirectoryPath = getNativeXCacheDirectoryPath();
        if (internalCacheDirectoryPath != null) {
            return new File(internalCacheDirectoryPath + fileName).exists();
        }
        Log.e("Could not find the application files directory path.");
        return false;
    }

    public static long getInternalCacheDirectorySizeInBytes() {
        long j = 0;
        String internalCacheDirectoryPath = getNativeXCacheDirectoryPath();
        if (internalCacheDirectoryPath != null) {
            File[] files = new File(internalCacheDirectoryPath).listFiles();
            if (!(files == null || files.length == 0)) {
                j = 0;
                for (File file : files) {
                    j += file.length();
                }
            }
        }
        return j;
    }
}
