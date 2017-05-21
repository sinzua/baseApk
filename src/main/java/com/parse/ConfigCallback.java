package com.parse;

public interface ConfigCallback extends ParseCallback2<ParseConfig, ParseException> {
    void done(ParseConfig parseConfig, ParseException parseException);
}
