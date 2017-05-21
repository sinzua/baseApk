package com.nativex.common;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Rect;
import com.supersonicads.sdk.precache.DownloadManager;
import com.supersonicads.sdk.utils.Constants.RequestParameters;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.net.URI;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import org.apache.http.impl.cookie.DateUtils;

public class Utilities {
    private static final float bytesPerMegabyte = 1048576.0f;

    private static class FlushedInputStream extends FilterInputStream {
        public FlushedInputStream(InputStream inputStream) {
            super(inputStream);
        }

        public long skip(long n) throws IOException {
            long totalBytesSkipped = 0;
            while (totalBytesSkipped < n) {
                long bytesSkipped = this.in.skip(n - totalBytesSkipped);
                if (bytesSkipped == 0) {
                    if (read() < 0) {
                        break;
                    }
                    bytesSkipped = 1;
                }
                totalBytesSkipped += bytesSkipped;
            }
            return totalBytesSkipped;
        }
    }

    public static String getDateTimeUtcAsString() {
        return System.currentTimeMillis() + "";
    }

    public static boolean isHttpOrHttpsUrl(String url) {
        try {
            URI uri = URI.create(url);
            if (uri.getScheme().equals("http") || uri.getScheme().equals("https")) {
                return true;
            }
        } catch (Exception e) {
        }
        return false;
    }

    private static String convertColorPartToHex(int colorPart) {
        String s = Integer.toHexString(colorPart);
        if (s.length() == 1) {
            return "0" + s;
        }
        return s;
    }

    public static String convertStreamToString(InputStream is) {
        if (is != null) {
            try {
                Writer writer = new StringWriter();
                char[] buffer = new char[1024];
                Reader reader = new BufferedReader(new InputStreamReader(is, DownloadManager.UTF8_CHARSET));
                while (true) {
                    int n = reader.read(buffer);
                    if (n != -1) {
                        writer.write(buffer, 0, n);
                    } else {
                        is.close();
                        return writer.toString();
                    }
                }
            } catch (Exception e) {
                Log.e("Utilities.convertStreamToString(): exception caught.", e);
            } catch (Throwable th) {
                is.close();
            }
        }
        return "";
    }

    public static synchronized byte[] convertInputStreamToByteArray(InputStream is) throws IOException {
        byte[] toByteArray;
        synchronized (Utilities.class) {
            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            byte[] data = new byte[16384];
            while (true) {
                int nRead = is.read(data, 0, data.length);
                if (nRead != -1) {
                    buffer.write(data, 0, nRead);
                } else {
                    buffer.flush();
                    toByteArray = buffer.toByteArray();
                }
            }
        }
        return toByteArray;
    }

    public static void savePictureStreamToFile(OutputStream output, InputStream stream) throws Exception {
        int i = 1024;
        InputStream pictureStream = new FlushedInputStream(stream);
        if (pictureStream.available() < 1024) {
            i = pictureStream.available();
        }
        byte[] buffer = new byte[i];
        while (true) {
            int bytesRead = pictureStream.read(buffer, 0, buffer.length);
            if (bytesRead >= 0) {
                output.write(buffer, 0, bytesRead);
            } else {
                output.close();
                pictureStream.close();
                return;
            }
        }
    }

    private static String getLocaleDateFormat() {
        try {
            return SimpleDateFormat.getDateTimeInstance().format(new Date(System.currentTimeMillis()));
        } catch (Exception e) {
            Log.e("Exception caught when generating picture name while formatting locale date/time.", e);
            return null;
        }
    }

    public static String getDateInFormat(String format) {
        try {
            return new SimpleDateFormat(format).format(new Date(System.currentTimeMillis()));
        } catch (Exception e) {
            Log.e("Exception caught when generating picture name while formatting pattern date/time.", e);
            return getLocaleDateFormat();
        }
    }

    public static Bitmap decodeSampledBitmapFromInternalMemory(Context context, String filename, int reqWidth, int reqHeight) {
        try {
            InputStream stream = context.openFileInput(filename);
            Options options = new Options();
            options.inJustDecodeBounds = true;
            Rect padding = new Rect(-1, -1, -1, -1);
            BitmapFactory.decodeStream(stream, padding, options);
            stream = context.openFileInput(filename);
            options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
            options.inJustDecodeBounds = false;
            return BitmapFactory.decodeStream(stream, padding, options);
        } catch (Exception e) {
            Log.e("Exception caught while decoding Bitmap from stream", e);
            return null;
        }
    }

    private static int calculateInSampleSize(Options options, int reqWidth, int reqHeight) {
        int height = options.outHeight;
        int width = options.outWidth;
        if (height <= reqHeight && width <= reqWidth) {
            return 1;
        }
        int heightRatio = Math.round(((float) height) / ((float) reqHeight));
        int widthRatio = Math.round(((float) width) / ((float) reqWidth));
        if (heightRatio < widthRatio) {
            return heightRatio;
        }
        return widthRatio;
    }

    private static Date parseDate(String date, String[] formats) {
        if (formats != null && formats.length > 0) {
            try {
                return DateUtils.parseDate(date, formats);
            } catch (Exception e) {
                Log.e("DateUtils", e);
            }
        }
        return null;
    }

    public static Date parseHtmlDate(String s) {
        if (s.endsWith("Z")) {
            s = s.replace("Z", "+0000");
        }
        return parseDate(s, new String[]{"yyyy-MM-dd'T'HH:mmZ", "yyyy-MM-dd HH:mmZ", "yyyy-MM-dd'T'HH:mm:ssZ", "yyyy-MM-dd HH:mm:ssZ", "yyyy-MM-dd'T'HH:mm:ss.SSSZ", "yyyy-MM-dd HH:mm:ss.SSSZ", "yyyy-MM-dd'T'HH:mmz", "yyyy-MM-dd HH:mmz", "yyyy-MM-dd'T'HH:mm:ssz", "yyyy-MM-dd HH:mm:ssz", "yyyy-MM-dd'T'HH:mm:ss.SSSz", "yyyy-MM-dd HH:mm:ss.SSSz"});
    }

    public static long millisecondsToMinutes(long abs) {
        return abs / 60000;
    }

    public static boolean stringIsEmpty(String s) {
        if (s == null || s.trim().length() == 0) {
            return true;
        }
        return false;
    }

    public static String appendParamsToUrl(String url, Map<String, String> params) {
        if (params == null || params.size() <= 0) {
            return url;
        }
        StringBuilder builder = new StringBuilder(url);
        if (url.contains("?")) {
            builder.append(RequestParameters.AMPERSAND);
        } else {
            builder.append("?");
        }
        for (Entry<String, String> entry : params.entrySet()) {
            try {
                builder.append(URLEncoder.encode((String) entry.getKey(), DownloadManager.UTF8_CHARSET));
            } catch (Exception e) {
                builder.append((String) entry.getKey());
            }
            builder.append(RequestParameters.EQUAL);
            try {
                builder.append(URLEncoder.encode((String) entry.getValue(), DownloadManager.UTF8_CHARSET));
            } catch (Exception e2) {
                builder.append((String) entry.getValue());
            }
            builder.append(RequestParameters.AMPERSAND);
        }
        builder.deleteCharAt(builder.length() - 1);
        return builder.toString();
    }

    public static String encodeUrl(String urlStr) {
        try {
            URL url = new URL(urlStr);
            urlStr = new URI(url.getProtocol(), url.getUserInfo(), url.getHost(), url.getPort(), url.getPath(), url.getQuery(), url.getRef()).toURL().toString();
        } catch (Exception e) {
            Log.e("The url (" + urlStr + ") cannot be parsed", e);
        }
        return urlStr;
    }

    public static long convertMBtoBytes(long megaBytes) {
        return (megaBytes * 1024) * 1024;
    }

    public static float convertBytesToMB(long bytes) {
        return ((float) bytes) / bytesPerMegabyte;
    }

    public static String convertBytesToMbAsString(long bytes) {
        float mb = convertBytesToMB(bytes);
        return String.format(Locale.US, "%.2fMB", new Object[]{Float.valueOf(mb)});
    }
}
