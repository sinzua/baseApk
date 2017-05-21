package com.parse;

interface ParseCallback1<T extends Throwable> {
    void done(T t);
}
