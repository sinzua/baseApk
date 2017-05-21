package com.parse;

public interface DeleteCallback extends ParseCallback1<ParseException> {
    void done(ParseException parseException);
}
