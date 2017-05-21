package com.parse;

public interface RequestPasswordResetCallback extends ParseCallback1<ParseException> {
    void done(ParseException parseException);
}
