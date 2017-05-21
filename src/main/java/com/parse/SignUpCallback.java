package com.parse;

public interface SignUpCallback extends ParseCallback1<ParseException> {
    void done(ParseException parseException);
}
