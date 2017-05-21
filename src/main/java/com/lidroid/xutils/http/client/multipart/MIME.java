package com.lidroid.xutils.http.client.multipart;

import com.supersonicads.sdk.precache.DownloadManager;
import java.nio.charset.Charset;

public class MIME {
    public static final String CONTENT_DISPOSITION = "Content-Disposition";
    public static final String CONTENT_TRANSFER_ENC = "Content-Transfer-Encoding";
    public static final String CONTENT_TYPE = "Content-Type";
    public static final Charset DEFAULT_CHARSET = Charset.forName(DownloadManager.UTF8_CHARSET);
    public static final String ENC_8BIT = "8bit";
    public static final String ENC_BINARY = "binary";
}
