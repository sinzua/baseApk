package com.parse;

import bolts.Task;
import com.parse.ParseRequest.Method;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

class ParseAWSRequest extends ParseRequest<byte[]> {
    public ParseAWSRequest(Method method, String url) {
        super(method, url);
    }

    protected Task<byte[]> onResponseAsync(ParseHttpResponse response, ProgressCallback downloadProgressCallback) {
        int statusCode = response.getStatusCode();
        if ((statusCode < 200 || statusCode >= 300) && statusCode != 304) {
            String action = this.method == Method.GET ? "Download from" : "Upload to";
            return Task.forError(new ParseException(100, String.format("%s S3 failed. %s", new Object[]{action, response.getReasonPhrase()})));
        } else if (this.method != Method.GET) {
            return null;
        } else {
            int totalSize = response.getTotalSize();
            int downloadedSize = 0;
            InputStream inputStream = null;
            Task<byte[]> forResult;
            try {
                inputStream = response.getContent();
                ByteArrayOutputStream buffer = new ByteArrayOutputStream();
                byte[] data = new byte[32768];
                while (true) {
                    int nRead = inputStream.read(data, 0, data.length);
                    if (nRead == -1) {
                        break;
                    }
                    buffer.write(data, 0, nRead);
                    downloadedSize += nRead;
                    if (!(downloadProgressCallback == null || totalSize == -1)) {
                        downloadProgressCallback.done(Integer.valueOf(Math.round((((float) downloadedSize) / ((float) totalSize)) * 100.0f)));
                    }
                }
                forResult = Task.forResult(buffer.toByteArray());
                return forResult;
            } catch (IOException e) {
                forResult = Task.forError(e);
                return forResult;
            } finally {
                ParseIOUtils.closeQuietly(inputStream);
            }
        }
    }
}
