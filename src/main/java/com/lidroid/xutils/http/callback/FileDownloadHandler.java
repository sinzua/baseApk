package com.lidroid.xutils.http.callback;

import android.text.TextUtils;
import com.lidroid.xutils.util.IOUtils;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import org.apache.http.HttpEntity;

public class FileDownloadHandler {
    public File handleEntity(HttpEntity entity, RequestCallBackHandler callBackHandler, String target, boolean isResume, String responseFileName) throws IOException {
        if (entity == null || TextUtils.isEmpty(target)) {
            return null;
        }
        File file = new File(target);
        if (!file.exists()) {
            File dir = file.getParentFile();
            if (dir.exists() || dir.mkdirs()) {
                file.createNewFile();
            }
        }
        long current = 0;
        Closeable bis = null;
        Closeable bos = null;
        if (isResume) {
            try {
                current = file.length();
                FileOutputStream fileOutputStream = new FileOutputStream(target, true);
            } catch (Throwable th) {
                Throwable th2 = th;
                IOUtils.closeQuietly(bis);
                IOUtils.closeQuietly(bos);
                throw th2;
            }
        }
        fileOutputStream = new FileOutputStream(target);
        long total = entity.getContentLength() + current;
        Closeable bis2 = new BufferedInputStream(entity.getContent());
        try {
            Closeable bos2 = new BufferedOutputStream(fileOutputStream);
            if (callBackHandler != null) {
                try {
                    if (!callBackHandler.updateProgress(total, current, true)) {
                        IOUtils.closeQuietly(bis2);
                        IOUtils.closeQuietly(bos2);
                        return file;
                    }
                } catch (Throwable th3) {
                    th2 = th3;
                    bos = bos2;
                    bis = bis2;
                }
            }
            byte[] tmp = new byte[4096];
            while (true) {
                int len = bis2.read(tmp);
                if (len == -1) {
                    break;
                }
                bos2.write(tmp, 0, len);
                current += (long) len;
                if (callBackHandler != null && !callBackHandler.updateProgress(total, current, false)) {
                    IOUtils.closeQuietly(bis2);
                    IOUtils.closeQuietly(bos2);
                    return file;
                }
            }
            bos2.flush();
            if (callBackHandler != null) {
                callBackHandler.updateProgress(total, current, true);
            }
            IOUtils.closeQuietly(bis2);
            IOUtils.closeQuietly(bos2);
            if (!file.exists() || TextUtils.isEmpty(responseFileName)) {
                return file;
            }
            File newFile;
            file = new File(file.getParent(), responseFileName);
            while (newFile.exists()) {
                file = new File(file.getParent(), System.currentTimeMillis() + responseFileName);
            }
            if (!file.renameTo(newFile)) {
                newFile = file;
            }
            return newFile;
        } catch (Throwable th4) {
            th2 = th4;
            bis = bis2;
            IOUtils.closeQuietly(bis);
            IOUtils.closeQuietly(bos);
            throw th2;
        }
    }
}
