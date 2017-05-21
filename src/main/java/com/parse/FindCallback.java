package com.parse;

import java.util.List;

public interface FindCallback<T extends ParseObject> extends ParseCallback2<List<T>, ParseException> {
    void done(List<T> list, ParseException parseException);
}
