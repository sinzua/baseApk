package com.parse;

public interface LogOutCallback extends ParseCallback1<ParseException> {
    void done(ParseException parseException);
}
