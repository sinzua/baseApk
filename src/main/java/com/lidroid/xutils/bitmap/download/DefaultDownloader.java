package com.lidroid.xutils.bitmap.download;

import com.lidroid.xutils.BitmapUtils.BitmapLoadTask;
import com.lidroid.xutils.util.IOUtils;
import com.lidroid.xutils.util.OtherUtils;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.Closeable;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

public class DefaultDownloader extends Downloader {
    public long downloadToStream(String uri, OutputStream outputStream, BitmapLoadTask<?> task) {
        BufferedInputStream bis;
        Object bis2;
        Throwable th;
        if (task == null || task.isCancelled() || task.getTargetContainer() == null) {
            return -1;
        }
        long fileLen;
        long result;
        Closeable closeable = null;
        OtherUtils.trustAllHttpsURLConnection();
        long currCount = 0;
        if (uri.startsWith("/")) {
            FileInputStream fileInputStream = new FileInputStream(uri);
            fileLen = (long) fileInputStream.available();
            bis = new BufferedInputStream(fileInputStream);
            try {
                result = System.currentTimeMillis() + getDefaultExpiry();
                closeable = bis;
            } catch (Throwable th2) {
                th = th2;
                bis2 = bis;
                IOUtils.closeQuietly(closeable);
                throw th;
            }
        } else if (uri.startsWith("assets/")) {
            InputStream inputStream = getContext().getAssets().open(uri.substring(7, uri.length()));
            fileLen = (long) inputStream.available();
            result = Long.MAX_VALUE;
            bis2 = new BufferedInputStream(inputStream);
        } else {
            URLConnection urlConnection = new URL(uri).openConnection();
            urlConnection.setConnectTimeout(getDefaultConnectTimeout());
            urlConnection.setReadTimeout(getDefaultReadTimeout());
            bis = new BufferedInputStream(urlConnection.getInputStream());
            result = urlConnection.getExpiration();
            if (result < System.currentTimeMillis()) {
                result = System.currentTimeMillis() + getDefaultExpiry();
            }
            fileLen = (long) urlConnection.getContentLength();
            bis2 = bis;
        }
        try {
            if (task.isCancelled() || task.getTargetContainer() == null) {
                IOUtils.closeQuietly(closeable);
                return -1;
            }
            byte[] buffer = new byte[4096];
            BufferedOutputStream out = new BufferedOutputStream(outputStream);
            while (true) {
                int len = closeable.read(buffer);
                if (len == -1) {
                    out.flush();
                    IOUtils.closeQuietly(closeable);
                    return result;
                }
                out.write(buffer, 0, len);
                currCount += (long) len;
                if (task.isCancelled() || task.getTargetContainer() == null) {
                    IOUtils.closeQuietly(closeable);
                } else {
                    task.updateProgress(fileLen, currCount);
                }
            }
            IOUtils.closeQuietly(closeable);
            return -1;
        } catch (Throwable th3) {
            e = th3;
        }
    }
}
