package com.lidroid.xutils.http.client.multipart.content;

import com.lidroid.xutils.http.client.multipart.MultipartEntity.CallBackInfo;
import java.io.IOException;
import java.io.OutputStream;

public interface ContentBody extends ContentDescriptor {
    String getFilename();

    void setCallBackInfo(CallBackInfo callBackInfo);

    void writeTo(OutputStream outputStream) throws IOException;
}
