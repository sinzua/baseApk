package com.lidroid.xutils.exception;

public class DbException extends BaseException {
    private static final long serialVersionUID = 1;

    public DbException(String detailMessage) {
        super(detailMessage);
    }

    public DbException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }

    public DbException(Throwable throwable) {
        super(throwable);
    }
}
