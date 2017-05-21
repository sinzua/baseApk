package com.hw.exceptions;

public class InstagramException {
    public static final int CODE_LOGIN_FAILED = 501;
    public static final String MESSAGE_LOGIN_FAILED = "login failed";
    private int code;
    private String message;

    public InstagramException(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
