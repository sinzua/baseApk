package com.parse;

public interface GetCallback<T extends ParseObject> extends ParseCallback2<T, ParseException> {
    void done(T t, ParseException parseException);
}
