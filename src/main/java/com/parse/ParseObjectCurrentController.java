package com.parse;

import bolts.Task;

interface ParseObjectCurrentController<T extends ParseObject> {
    void clearFromDisk();

    void clearFromMemory();

    Task<Boolean> existsAsync();

    Task<T> getAsync();

    boolean isCurrent(T t);

    Task<Void> setAsync(T t);
}
