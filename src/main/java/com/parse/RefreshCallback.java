package com.parse;

public interface RefreshCallback extends ParseCallback2<ParseObject, ParseException> {
    void done(ParseObject parseObject, ParseException parseException);
}
