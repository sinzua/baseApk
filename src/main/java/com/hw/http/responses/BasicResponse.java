package com.hw.http.responses;

public class BasicResponse {
    protected String message;
    protected String status;

    public String getStatus() {
        return this.status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isSuccessful() {
        return this.status != null && this.status.equals("ok");
    }

    public boolean needLogin() {
        return this.status != null && this.status.equals("fail") && this.message.equals("login_required");
    }
}
