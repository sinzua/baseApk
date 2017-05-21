package com.parse;

public interface SaveCallback extends ParseCallback1<ParseException> {
    void done(ParseException parseException);
}
