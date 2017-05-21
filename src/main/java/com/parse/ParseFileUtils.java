package com.parse;

import com.supersonicads.sdk.precache.DownloadManager;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import org.json.JSONException;
import org.json.JSONObject;

class ParseFileUtils {
    private static final long FILE_COPY_BUFFER_SIZE = 31457280;
    public static final long ONE_KB = 1024;
    public static final long ONE_MB = 1048576;

    ParseFileUtils() {
    }

    public static byte[] readFileToByteArray(File file) throws IOException {
        InputStream inputStream = null;
        try {
            inputStream = openInputStream(file);
            byte[] toByteArray = ParseIOUtils.toByteArray(inputStream);
            return toByteArray;
        } finally {
            ParseIOUtils.closeQuietly(inputStream);
        }
    }

    public static FileInputStream openInputStream(File file) throws IOException {
        if (!file.exists()) {
            throw new FileNotFoundException("File '" + file + "' does not exist");
        } else if (file.isDirectory()) {
            throw new IOException("File '" + file + "' exists but is a directory");
        } else if (file.canRead()) {
            return new FileInputStream(file);
        } else {
            throw new IOException("File '" + file + "' cannot be read");
        }
    }

    public static void writeByteArrayToFile(File file, byte[] data) throws IOException {
        OutputStream outputStream = null;
        try {
            outputStream = openOutputStream(file);
            outputStream.write(data);
        } finally {
            ParseIOUtils.closeQuietly(outputStream);
        }
    }

    public static FileOutputStream openOutputStream(File file) throws IOException {
        if (!file.exists()) {
            File parent = file.getParentFile();
            if (!(parent == null || parent.exists() || parent.mkdirs())) {
                throw new IOException("File '" + file + "' could not be created");
            }
        } else if (file.isDirectory()) {
            throw new IOException("File '" + file + "' exists but is a directory");
        } else if (!file.canWrite()) {
            throw new IOException("File '" + file + "' cannot be written to");
        }
        return new FileOutputStream(file);
    }

    public static void moveFile(File srcFile, File destFile) throws IOException {
        if (srcFile == null) {
            throw new NullPointerException("Source must not be null");
        } else if (destFile == null) {
            throw new NullPointerException("Destination must not be null");
        } else if (!srcFile.exists()) {
            throw new FileNotFoundException("Source '" + srcFile + "' does not exist");
        } else if (srcFile.isDirectory()) {
            throw new IOException("Source '" + srcFile + "' is a directory");
        } else if (destFile.exists()) {
            throw new IOException("Destination '" + destFile + "' already exists");
        } else if (destFile.isDirectory()) {
            throw new IOException("Destination '" + destFile + "' is a directory");
        } else if (!srcFile.renameTo(destFile)) {
            copyFile(srcFile, destFile);
            if (!srcFile.delete()) {
                deleteQuietly(destFile);
                throw new IOException("Failed to delete original file '" + srcFile + "' after copy to '" + destFile + "'");
            }
        }
    }

    public static void copyFile(File srcFile, File destFile) throws IOException {
        copyFile(srcFile, destFile, true);
    }

    public static void copyFile(File srcFile, File destFile, boolean preserveFileDate) throws IOException {
        if (srcFile == null) {
            throw new NullPointerException("Source must not be null");
        } else if (destFile == null) {
            throw new NullPointerException("Destination must not be null");
        } else if (!srcFile.exists()) {
            throw new FileNotFoundException("Source '" + srcFile + "' does not exist");
        } else if (srcFile.isDirectory()) {
            throw new IOException("Source '" + srcFile + "' exists but is a directory");
        } else if (srcFile.getCanonicalPath().equals(destFile.getCanonicalPath())) {
            throw new IOException("Source '" + srcFile + "' and destination '" + destFile + "' are the same");
        } else {
            File parentFile = destFile.getParentFile();
            if (parentFile != null && !parentFile.mkdirs() && !parentFile.isDirectory()) {
                throw new IOException("Destination '" + parentFile + "' directory cannot be created");
            } else if (!destFile.exists() || destFile.canWrite()) {
                doCopyFile(srcFile, destFile, preserveFileDate);
            } else {
                throw new IOException("Destination '" + destFile + "' exists but is read-only");
            }
        }
    }

    private static void doCopyFile(File srcFile, File destFile, boolean preserveFileDate) throws IOException {
        OutputStream fileOutputStream;
        Throwable th;
        if (destFile.exists() && destFile.isDirectory()) {
            throw new IOException("Destination '" + destFile + "' exists but is a directory");
        }
        InputStream fis = null;
        OutputStream fos = null;
        try {
            InputStream fis2 = new FileInputStream(srcFile);
            try {
                fileOutputStream = new FileOutputStream(destFile);
            } catch (Throwable th2) {
                th = th2;
                fis = fis2;
                ParseIOUtils.closeQuietly((Closeable) null);
                ParseIOUtils.closeQuietly(fos);
                ParseIOUtils.closeQuietly((Closeable) null);
                ParseIOUtils.closeQuietly(fis);
                throw th;
            }
            try {
                Closeable input = fis2.getChannel();
                Closeable output = fileOutputStream.getChannel();
                long size = input.size();
                long pos = 0;
                while (pos < size) {
                    long count;
                    long remain = size - pos;
                    if (remain > FILE_COPY_BUFFER_SIZE) {
                        count = FILE_COPY_BUFFER_SIZE;
                    } else {
                        count = remain;
                    }
                    long bytesCopied = output.transferFrom(input, pos, count);
                    if (bytesCopied == 0) {
                        break;
                    }
                    pos += bytesCopied;
                }
                ParseIOUtils.closeQuietly(output);
                ParseIOUtils.closeQuietly(fileOutputStream);
                ParseIOUtils.closeQuietly(input);
                ParseIOUtils.closeQuietly(fis2);
                long srcLen = srcFile.length();
                long dstLen = destFile.length();
                if (srcLen != dstLen) {
                    throw new IOException("Failed to copy full contents from '" + srcFile + "' to '" + destFile + "' Expected length: " + srcLen + " Actual: " + dstLen);
                } else if (preserveFileDate) {
                    destFile.setLastModified(srcFile.lastModified());
                }
            } catch (Throwable th3) {
                th = th3;
                fos = fileOutputStream;
                fis = fis2;
                ParseIOUtils.closeQuietly((Closeable) null);
                ParseIOUtils.closeQuietly(fos);
                ParseIOUtils.closeQuietly((Closeable) null);
                ParseIOUtils.closeQuietly(fis);
                throw th;
            }
        } catch (Throwable th4) {
            th = th4;
            ParseIOUtils.closeQuietly((Closeable) null);
            ParseIOUtils.closeQuietly(fos);
            ParseIOUtils.closeQuietly((Closeable) null);
            ParseIOUtils.closeQuietly(fis);
            throw th;
        }
    }

    public static void deleteDirectory(File directory) throws IOException {
        if (directory.exists()) {
            if (!isSymlink(directory)) {
                cleanDirectory(directory);
            }
            if (!directory.delete()) {
                throw new IOException("Unable to delete directory " + directory + ".");
            }
        }
    }

    public static boolean deleteQuietly(File file) {
        boolean z = false;
        if (file != null) {
            try {
                if (file.isDirectory()) {
                    cleanDirectory(file);
                }
            } catch (Exception e) {
            }
            try {
                z = file.delete();
            } catch (Exception e2) {
            }
        }
        return z;
    }

    public static void cleanDirectory(File directory) throws IOException {
        if (!directory.exists()) {
            throw new IllegalArgumentException(directory + " does not exist");
        } else if (directory.isDirectory()) {
            File[] files = directory.listFiles();
            if (files == null) {
                throw new IOException("Failed to list contents of " + directory);
            }
            IOException exception = null;
            for (File file : files) {
                try {
                    forceDelete(file);
                } catch (IOException ioe) {
                    exception = ioe;
                }
            }
            if (exception != null) {
                throw exception;
            }
        } else {
            throw new IllegalArgumentException(directory + " is not a directory");
        }
    }

    public static void forceDelete(File file) throws IOException {
        if (file.isDirectory()) {
            deleteDirectory(file);
            return;
        }
        boolean filePresent = file.exists();
        if (!file.delete()) {
            if (filePresent) {
                throw new IOException("Unable to delete file: " + file);
            }
            throw new FileNotFoundException("File does not exist: " + file);
        }
    }

    public static boolean isSymlink(File file) throws IOException {
        if (file == null) {
            throw new NullPointerException("File must not be null");
        }
        File fileInCanonicalDir;
        if (file.getParent() == null) {
            fileInCanonicalDir = file;
        } else {
            fileInCanonicalDir = new File(file.getParentFile().getCanonicalFile(), file.getName());
        }
        if (fileInCanonicalDir.getCanonicalFile().equals(fileInCanonicalDir.getAbsoluteFile())) {
            return false;
        }
        return true;
    }

    public static String readFileToString(File file, Charset encoding) throws IOException {
        return new String(readFileToByteArray(file), encoding);
    }

    public static String readFileToString(File file, String encoding) throws IOException {
        return readFileToString(file, Charset.forName(encoding));
    }

    public static void writeStringToFile(File file, String string, Charset encoding) throws IOException {
        writeByteArrayToFile(file, string.getBytes(encoding));
    }

    public static void writeStringToFile(File file, String string, String encoding) throws IOException {
        writeStringToFile(file, string, Charset.forName(encoding));
    }

    public static JSONObject readFileToJSONObject(File file) throws IOException, JSONException {
        return new JSONObject(readFileToString(file, DownloadManager.UTF8_CHARSET));
    }

    public static void writeJSONObjectToFile(File file, JSONObject json) throws IOException {
        writeByteArrayToFile(file, json.toString().getBytes(Charset.forName(DownloadManager.UTF8_CHARSET)));
    }
}
