package com.parse;

public interface LogInCallback extends ParseCallback2<ParseUser, ParseException> {
    void done(ParseUser parseUser, ParseException parseException);
}
