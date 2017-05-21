package com.ty.followboom.okhttp;

public abstract class RequestCallback<T> {
    public abstract void onFailure(Exception exception);

    public abstract void onResponse(T t);
}
