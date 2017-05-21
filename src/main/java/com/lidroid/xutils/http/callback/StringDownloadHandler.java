package com.lidroid.xutils.http.callback;

import com.lidroid.xutils.util.IOUtils;
import com.lidroid.xutils.util.OtherUtils;
import java.io.BufferedReader;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStreamReader;
import org.apache.http.HttpEntity;

public class StringDownloadHandler {
    public String handleEntity(HttpEntity entity, RequestCallBackHandler callBackHandler, String charset) throws IOException {
        if (entity == null) {
            return null;
        }
        long current = 0;
        long total = entity.getContentLength();
        if (callBackHandler != null && !callBackHandler.updateProgress(total, 0, true)) {
            return null;
        }
        Closeable inputStream = null;
        StringBuilder sb = new StringBuilder();
        try {
            inputStream = entity.getContent();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, charset));
            String str = "";
            while (true) {
                str = reader.readLine();
                if (str != null) {
                    sb.append(str).append('\n');
                    current += OtherUtils.sizeOfString(str, charset);
                    if (callBackHandler != null && !callBackHandler.updateProgress(total, current, false)) {
                        break;
                    }
                } else {
                    break;
                }
            }
            if (callBackHandler != null) {
                callBackHandler.updateProgress(total, current, true);
            }
            IOUtils.closeQuietly(inputStream);
            return sb.toString().trim();
        } catch (Throwable th) {
            IOUtils.closeQuietly(inputStream);
        }
    }
}
