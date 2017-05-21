package com.parse;

import android.content.Context;
import com.supersonicads.sdk.precache.DownloadManager;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import org.json.JSONException;
import org.json.JSONObject;

class ParseKeyValueCache {
    static final int DEFAULT_MAX_KEY_VALUE_CACHE_BYTES = 2097152;
    static final int DEFAULT_MAX_KEY_VALUE_CACHE_FILES = 1000;
    private static final String DIR_NAME = "ParseKeyValueCache";
    private static final Object MUTEX_IO = new Object();
    private static final String TAG = "ParseKeyValueCache";
    private static File directory;
    static int maxKeyValueCacheBytes = 2097152;
    static int maxKeyValueCacheFiles = 1000;

    ParseKeyValueCache() {
    }

    static void initialize(Context context) {
        initialize(new File(context.getCacheDir(), "ParseKeyValueCache"));
    }

    static void initialize(File path) {
        if (path.isDirectory() || path.mkdir()) {
            directory = path;
            return;
        }
        throw new RuntimeException("Could not create ParseKeyValueCache directory");
    }

    private static File getKeyValueCacheDir() {
        return directory;
    }

    static int size() {
        return getKeyValueCacheDir().listFiles().length;
    }

    private static File getKeyValueCacheFile(String key) {
        final String suffix = '.' + key;
        File[] matches = getKeyValueCacheDir().listFiles(new FilenameFilter() {
            public boolean accept(File dir, String filename) {
                return filename.endsWith(suffix);
            }
        });
        return (matches == null || matches.length == 0) ? null : matches[0];
    }

    private static long getKeyValueCacheAge(File cacheFile) {
        String name = cacheFile.getName();
        try {
            return Long.parseLong(name.substring(0, name.indexOf(46)));
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    private static File createKeyValueCacheFile(String key) {
        return new File(getKeyValueCacheDir(), String.valueOf(new Date().getTime()) + '.' + key);
    }

    static void clearKeyValueCacheDir() {
        synchronized (MUTEX_IO) {
            File dir = getKeyValueCacheDir();
            if (dir == null) {
                return;
            }
            File[] entries = dir.listFiles();
            if (entries == null) {
                return;
            }
            for (File entry : entries) {
                entry.delete();
            }
        }
    }

    static void saveToKeyValueCache(String key, String value) {
        synchronized (MUTEX_IO) {
            File prior = getKeyValueCacheFile(key);
            if (prior != null) {
                prior.delete();
            }
            try {
                FileOutputStream out = new FileOutputStream(createKeyValueCacheFile(key));
                out.write(value.getBytes(DownloadManager.UTF8_CHARSET));
                out.close();
            } catch (UnsupportedEncodingException e) {
            } catch (IOException e2) {
            }
            File[] files = getKeyValueCacheDir().listFiles();
            int numFiles = files.length;
            int numBytes = 0;
            for (File file : files) {
                numBytes = (int) (((long) numBytes) + file.length());
            }
            if (numFiles > maxKeyValueCacheFiles || numBytes > maxKeyValueCacheBytes) {
                Arrays.sort(files, new Comparator<File>() {
                    public int compare(File f1, File f2) {
                        int dateCompare = Long.valueOf(f1.lastModified()).compareTo(Long.valueOf(f2.lastModified()));
                        return dateCompare != 0 ? dateCompare : f1.getName().compareTo(f2.getName());
                    }
                });
                for (File file2 : files) {
                    numFiles--;
                    numBytes = (int) (((long) numBytes) - file2.length());
                    file2.delete();
                    if (numFiles <= maxKeyValueCacheFiles && numBytes <= maxKeyValueCacheBytes) {
                        break;
                    }
                }
            }
        }
    }

    static void clearFromKeyValueCache(String key) {
        synchronized (MUTEX_IO) {
            File file = getKeyValueCacheFile(key);
            if (file != null) {
                file.delete();
            }
        }
    }

    static String loadFromKeyValueCache(String key, long maxAgeMilliseconds) {
        String str;
        synchronized (MUTEX_IO) {
            File file = getKeyValueCacheFile(key);
            if (file == null) {
                str = null;
            } else {
                Date now = new Date();
                if (getKeyValueCacheAge(file) < Math.max(0, now.getTime() - maxAgeMilliseconds)) {
                    str = null;
                } else {
                    file.setLastModified(now.getTime());
                    try {
                        RandomAccessFile f = new RandomAccessFile(file, "r");
                        byte[] bytes = new byte[((int) f.length())];
                        f.readFully(bytes);
                        f.close();
                        str = new String(bytes, DownloadManager.UTF8_CHARSET);
                    } catch (IOException e) {
                        PLog.e("ParseKeyValueCache", "error reading from cache", e);
                        str = null;
                    }
                }
            }
        }
        return str;
    }

    static JSONObject jsonFromKeyValueCache(String key, long maxAgeMilliseconds) {
        String raw = loadFromKeyValueCache(key, maxAgeMilliseconds);
        if (raw == null) {
            return null;
        }
        try {
            return new JSONObject(raw);
        } catch (JSONException e) {
            PLog.e("ParseKeyValueCache", "corrupted cache for " + key, e);
            clearFromKeyValueCache(key);
            return null;
        }
    }
}
