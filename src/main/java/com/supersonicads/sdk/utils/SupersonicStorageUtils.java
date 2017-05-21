package com.supersonicads.sdk.utils;

import android.content.Context;
import android.os.Environment;
import com.supersonicads.sdk.data.SSAEnums.ProductType;
import com.supersonicads.sdk.data.SSAFile;
import com.supersonicads.sdk.utils.Constants.ParametersKeys;
import java.io.File;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class SupersonicStorageUtils {
    private static final String SSA_DIRECTORY_NAME = "supersonicads";

    public static String initializeCacheDirectory(Context context) {
        createRootDirectory(context);
        return refreshRootDirectory(context);
    }

    private static String refreshRootDirectory(Context context) {
        String storedVerison = SupersonicSharedPrefHelper.getSupersonicPrefHelper().getCurrentSDKVersion();
        String sdkVer = DeviceProperties.getInstance(context).getSupersonicSdkVersion();
        if (storedVerison.equalsIgnoreCase(sdkVer)) {
            return getDiskCacheDir(context, SSA_DIRECTORY_NAME).getPath();
        }
        SupersonicSharedPrefHelper.getSupersonicPrefHelper().setCurrentSDKVersion(sdkVer);
        File cacheDir = getExternalCacheDir(context);
        if (cacheDir != null) {
            deleteAllFiles(cacheDir.getAbsolutePath() + File.separator + SSA_DIRECTORY_NAME + File.separator);
        }
        deleteAllFiles(getInternalCacheDirPath(context) + File.separator + SSA_DIRECTORY_NAME + File.separator);
        return createRootDirectory(context);
    }

    private static File getExternalCacheDir(Context context) {
        return context.getExternalCacheDir();
    }

    private static String getInternalCacheDirPath(Context context) {
        File internalFile = context.getCacheDir();
        if (internalFile != null) {
            return internalFile.getPath();
        }
        return null;
    }

    private static void deleteAllFiles(String path) {
        File[] files = new File(path).listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    deleteAllFiles(file.getAbsolutePath());
                    file.delete();
                } else {
                    file.delete();
                }
            }
        }
    }

    private static File getDiskCacheDir(Context context, String cacheDirName) {
        if (!isExternalStorageAvailable()) {
            return new File(getInternalCacheDirPath(context) + File.separator + cacheDirName);
        } else if (getExternalCacheDir(context) != null) {
            return new File(getExternalCacheDir(context).getPath() + File.separator + cacheDirName);
        } else {
            return new File(getInternalCacheDirPath(context) + File.separator + cacheDirName);
        }
    }

    private static String createRootDirectory(Context context) {
        File rootDirectory = getDiskCacheDir(context, SSA_DIRECTORY_NAME);
        if (!rootDirectory.exists()) {
            rootDirectory.mkdir();
        }
        return rootDirectory.getPath();
    }

    public static String makeDir(String cacheRootDirectory, String directory) {
        File dir = new File(cacheRootDirectory, directory);
        if (dir.exists() || dir.mkdirs()) {
            return dir.getPath();
        }
        return null;
    }

    public static boolean isExternalStorageAvailable() {
        String state = Environment.getExternalStorageState();
        if ("mounted".equals(state)) {
            return true;
        }
        if ("mounted_ro".equals(state)) {
            return true;
        }
        return false;
    }

    public static synchronized boolean deleteFile(String rootCacheDir, String filePath, String fileName) {
        int isSucceed;
        synchronized (SupersonicStorageUtils.class) {
            File dir = new File(rootCacheDir, filePath);
            if (dir.exists()) {
                File[] files = dir.listFiles();
                if (files == null) {
                    isSucceed = 0;
                } else {
                    for (File entry : files) {
                        if (entry.isFile() && entry.getName().equalsIgnoreCase(fileName)) {
                            boolean isSucceed2 = entry.delete();
                            break;
                        }
                    }
                    isSucceed = 0;
                }
            } else {
                isSucceed = 0;
            }
        }
        return isSucceed;
    }

    public static synchronized boolean isFileCached(String rootDirPath, SSAFile ssaFile) {
        boolean z;
        synchronized (SupersonicStorageUtils.class) {
            File dir = new File(rootDirPath, ssaFile.getPath());
            if (!(dir == null || dir.listFiles() == null)) {
                for (File entry : dir.listFiles()) {
                    if (entry.isFile() && entry.getName().equalsIgnoreCase(SDKUtils.getFileName(ssaFile.getFile()))) {
                        z = true;
                        break;
                    }
                }
            }
            z = false;
        }
        return z;
    }

    public static boolean isPathExist(String cachRootPath, String path) {
        if (new File(cachRootPath, path).exists()) {
            return true;
        }
        return false;
    }

    public static synchronized boolean deleteFolder(String cacheRootDir, String path) {
        boolean deleteFolderRecursive;
        synchronized (SupersonicStorageUtils.class) {
            deleteFolderRecursive = deleteFolderRecursive(new File(cacheRootDir, path));
        }
        return deleteFolderRecursive;
    }

    private static boolean deleteFolderRecursive(File file) {
        if (!file.exists()) {
            return false;
        }
        if (!file.isDirectory()) {
            return file.delete();
        }
        for (File f : file.listFiles()) {
            boolean result = deleteFolderRecursive(f);
        }
        return file.delete();
    }

    public static String getCachedFilesMap(String cacheRootPath, String path) {
        JSONObject jsnObj = buildFilesMap(cacheRootPath, path);
        try {
            jsnObj.put("path", path);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsnObj.toString();
    }

    private static JSONObject buildFilesMap(String cacheRootPath, String path) {
        File dir = new File(cacheRootPath, path);
        JSONObject jsnObj = new JSONObject();
        File[] fileList = dir.listFiles();
        if (fileList != null) {
            for (File entry : fileList) {
                try {
                    Object obj = looping(entry);
                    if (obj instanceof JSONArray) {
                        jsnObj.put("files", looping(entry));
                    } else if (obj instanceof JSONObject) {
                        jsnObj.put(entry.getName(), looping(entry));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    new SupersonicAsyncHttpRequestTask().execute(new String[]{Constants.NATIVE_EXCEPTION_BASE_URL + e.getStackTrace()[0].getMethodName()});
                }
            }
        }
        return jsnObj;
    }

    private static Object looping(File file) {
        JSONObject arr = new JSONObject();
        JSONArray tempArr = new JSONArray();
        try {
            if (file.isFile()) {
                tempArr.put(file.getName());
                return tempArr;
            }
            for (File fileEntry : file.listFiles()) {
                if (fileEntry.isDirectory()) {
                    arr.put(fileEntry.getName(), looping(fileEntry));
                } else {
                    tempArr.put(fileEntry.getName());
                    arr.put("files", tempArr);
                }
            }
            if (file.isDirectory()) {
                String lastUpdate = SupersonicSharedPrefHelper.getSupersonicPrefHelper().getCampaignLastUpdate(file.getName());
                if (lastUpdate != null) {
                    arr.put(ParametersKeys.LAST_UPDATE_TIME, lastUpdate);
                }
            }
            String product = file.getName();
            ProductType type = null;
            if (product.equalsIgnoreCase(ProductType.BrandConnect.toString())) {
                type = ProductType.BrandConnect;
            } else if (product.equalsIgnoreCase(ProductType.OfferWall.toString())) {
                type = ProductType.OfferWall;
            } else if (product.equalsIgnoreCase(ProductType.Interstitial.toString())) {
                type = ProductType.Interstitial;
            }
            if (type != null) {
                arr.put(SDKUtils.encodeString("applicationUserId"), SDKUtils.encodeString(SupersonicSharedPrefHelper.getSupersonicPrefHelper().getUniqueId(type)));
                arr.put(SDKUtils.encodeString("applicationKey"), SDKUtils.encodeString(SupersonicSharedPrefHelper.getSupersonicPrefHelper().getApplicationKey(type)));
            }
            return arr;
        } catch (JSONException e) {
            e.printStackTrace();
            new SupersonicAsyncHttpRequestTask().execute(new String[]{Constants.NATIVE_EXCEPTION_BASE_URL + e.getStackTrace()[0].getMethodName()});
        }
    }
}
