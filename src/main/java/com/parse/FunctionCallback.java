package com.parse;

public interface FunctionCallback<T> extends ParseCallback2<T, ParseException> {
    void done(T t, ParseException parseException);
}
