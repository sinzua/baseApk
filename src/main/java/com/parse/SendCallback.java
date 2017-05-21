package com.parse;

public interface SendCallback extends ParseCallback1<ParseException> {
    void done(ParseException parseException);
}
