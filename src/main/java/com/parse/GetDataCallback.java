package com.parse;

public interface GetDataCallback extends ParseCallback2<byte[], ParseException> {
    void done(byte[] bArr, ParseException parseException);
}
