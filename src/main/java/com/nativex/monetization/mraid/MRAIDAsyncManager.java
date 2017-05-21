package com.nativex.monetization.mraid;

import android.content.Context;
import com.nativex.common.Log;
import com.nativex.common.Utilities;
import com.parse.ParseException;
import java.io.InputStream;
import java.net.URL;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

class MRAIDAsyncManager implements Runnable {
    private Action action = Action.INVALID;
    private Context context;
    private OnActionComplete listener;
    private String url;

    private enum Action {
        DOWNLOAD_PICTURE,
        DOWNLOAD_HTML,
        INVALID
    }

    interface OnActionComplete {
        void onActionComplete(String str, boolean z);
    }

    private MRAIDAsyncManager() {
    }

    public void run() {
        try {
            switch (this.action) {
                case DOWNLOAD_HTML:
                    downloadHtml();
                    return;
                case DOWNLOAD_PICTURE:
                    downloadPicture();
                    return;
                default:
                    fireListener("Unknown HTTP download command", false);
                    return;
            }
        } catch (Exception e) {
            MRAIDLogger.e("Unhandled exception", e);
        }
        MRAIDLogger.e("Unhandled exception", e);
    }

    private void start() {
        new Thread(this).start();
    }

    static void downloadPicture(Context context, String url, OnActionComplete listener) {
        MRAIDAsyncManager manager = new MRAIDAsyncManager();
        manager.action = Action.DOWNLOAD_PICTURE;
        manager.url = url;
        manager.listener = listener;
        manager.context = context.getApplicationContext();
        manager.start();
    }

    static void downloadHtml(String url, OnActionComplete listener, Context context) {
        MRAIDAsyncManager manager = new MRAIDAsyncManager();
        manager.action = Action.DOWNLOAD_HTML;
        manager.url = url;
        manager.listener = listener;
        if (context != null) {
            manager.context = context.getApplicationContext();
        }
        manager.start();
    }

    private synchronized void fireListener(String data, boolean success) {
        if (this.listener != null) {
            this.listener.onActionComplete(data, success);
        }
    }

    private void downloadPicture() {
        try {
            storePictureToDevice(new URL(this.url).openStream());
        } catch (Exception e) {
            String data = "Failed to download image";
            MRAIDLogger.e(data, e);
            fireListener(data, false);
        }
    }

    private void downloadHtml() {
        Exception e;
        Throwable th;
        HttpClient client = null;
        try {
            String data;
            if (this.url.startsWith("file:///")) {
                InputStream stream;
                if (this.url.startsWith("file:///android_asset/")) {
                    stream = this.context.getAssets().open(this.url.replace("file:///android_asset/", ""));
                } else {
                    stream = new URL(this.url).openStream();
                }
                if (stream == null) {
                    fireListener("File not found", false);
                    if (client != null) {
                        try {
                            client.getConnectionManager().shutdown();
                            return;
                        } catch (Exception e2) {
                            return;
                        }
                    }
                    return;
                }
                data = Utilities.convertStreamToString(stream);
                stream.close();
                fireListener(data, true);
            } else {
                HttpClient client2 = new DefaultHttpClient();
                try {
                    client2.getParams().setParameter("http.useragent", "Apache-HttpClient/UNAVAILABLE (java 1.4)");
                    HttpResponse response = client2.execute(new HttpGet(this.url));
                    int statusCode = response.getStatusLine().getStatusCode();
                    if (statusCode != 200) {
                        if (statusCode == ParseException.EMAIL_MISSING) {
                            fireListener("NO AD", true);
                        } else {
                            fireListener("Server responded with status code " + statusCode, false);
                        }
                        if (client2 != null) {
                            try {
                                client2.getConnectionManager().shutdown();
                            } catch (Exception e3) {
                            }
                        }
                        client = client2;
                        return;
                    }
                    HttpEntity entity = response.getEntity();
                    data = Utilities.convertStreamToString(entity.getContent());
                    entity.consumeContent();
                    fireListener(data, true);
                    client = client2;
                } catch (Exception e4) {
                    e = e4;
                    client = client2;
                    try {
                        Log.e("MRAIDController: Exception caught while downloading the content.", e);
                        fireListener("Error occurred while downloading the AD", false);
                        if (client != null) {
                            try {
                                client.getConnectionManager().shutdown();
                            } catch (Exception e5) {
                                return;
                            }
                        }
                    } catch (Throwable th2) {
                        th = th2;
                        if (client != null) {
                            try {
                                client.getConnectionManager().shutdown();
                            } catch (Exception e6) {
                            }
                        }
                        throw th;
                    }
                } catch (Throwable th3) {
                    th = th3;
                    client = client2;
                    if (client != null) {
                        client.getConnectionManager().shutdown();
                    }
                    throw th;
                }
            }
            if (client != null) {
                try {
                    client.getConnectionManager().shutdown();
                } catch (Exception e7) {
                }
            }
        } catch (Exception e8) {
            e = e8;
            Log.e("MRAIDController: Exception caught while downloading the content.", e);
            fireListener("Error occurred while downloading the AD", false);
            if (client != null) {
                client.getConnectionManager().shutdown();
            }
        }
    }

    private void storePictureToDevice(InputStream stream) throws Exception {
        String filePath = "nativeX_temp-" + System.currentTimeMillis();
        Utilities.savePictureStreamToFile(this.context.openFileOutput(filePath, 0), stream);
        fireListener(filePath, true);
    }
}
